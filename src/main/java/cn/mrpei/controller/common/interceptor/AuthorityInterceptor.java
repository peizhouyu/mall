package cn.mrpei.controller.common.interceptor;

import cn.mrpei.common.Const;
import cn.mrpei.common.ServerResponse;
import cn.mrpei.pojo.User;
import cn.mrpei.util.CookieUtil;
import cn.mrpei.util.JsonUtil;
import cn.mrpei.util.RedisShardedPoolUtil;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;

/**
 * 请求拦截器 统一判断用户权限
 */
@Slf4j
public class AuthorityInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object handler) throws Exception {
        log.info("preHandle");

        //请求controller中的方法名

        HandlerMethod handlerMethod = (HandlerMethod)handler;

        //解析HandlerMethod

        String methodName = handlerMethod.getMethod().getName();
        String className = handlerMethod.getBean().getClass().getSimpleName();


        StringBuffer requestParamBuffer = new StringBuffer();
        Map paramMap = httpServletRequest.getParameterMap();
        Iterator it = paramMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String mapKey = (String) entry.getKey();
            String mapValue = "";

            //request的这个参数map的value返回的是一个String[]
            Object obj = entry.getValue();
            if (obj instanceof String[]){
                String[] strs = (String[])obj;
                mapValue = Arrays.toString(strs);
            }
            requestParamBuffer.append(mapKey).append("=").append(mapValue);
        }

        //如果是登录请求则 直接放行
        if (StringUtils.equals(className,"UserManageController")  && StringUtils.equals(methodName,"login")){
            log.info("权限拦截器拦截到请求，className:{},methodName:{}",className,methodName);
            return true;
        }

        log.info("权限拦截器拦截到请求,className:{},methodName:{},param:{}",className,methodName,requestParamBuffer);

        // 正式处理业务逻辑
        User user = null;
        String loginToken  = CookieUtil.readLoginToken(httpServletRequest);
        if(StringUtils.isNotEmpty(loginToken)){
            String userJsonStr = RedisShardedPoolUtil.get(loginToken);
            user = JsonUtil.stringToObj(userJsonStr,User.class);
        }

        if (user == null || (user.getRole().intValue() != Const.Role.ROLE_ADMIN)){
            //返回false  不会调用controller 里的方法
            httpServletResponse.reset(); //添加reset 否则 getWriter() 报异常
            httpServletResponse.setCharacterEncoding("UTF-8");
            httpServletResponse.setContentType("application/json;charset=UTF-8");
            PrintWriter out = httpServletResponse.getWriter();

            if (user == null) {
                //富文本编辑器返回值处理
                if (StringUtils.equals(className,"ProductManageController")  && StringUtils.equals(methodName,"richtextImgUpload")){
                    Map resultMap = Maps.newHashMap();
                    resultMap.put("success",false);
                    resultMap.put("msg","请以管理员身份登录");
                    out.print(JsonUtil.objToString(resultMap));
                }else {
                    out.print(JsonUtil.objToString(ServerResponse.createByErrorMessage("拦截器拦截，用户未登录")));
                }
            }else {
                if (StringUtils.equals(className,"ProductManageController")  && StringUtils.equals(methodName,"richtextImgUpload")){
                    Map resultMap = Maps.newHashMap();
                    resultMap.put("success",false);
                    resultMap.put("msg","无权限操作");
                    out.print(JsonUtil.objToString(resultMap));
                }else {
                    out.print(JsonUtil.objToString(ServerResponse.createByErrorMessage("拦截器拦截，用户无权限操作")));
                }
            }

            out.flush();
            out.close();

            return false;
        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
        log.info("postHandle");
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
        log.info("afterCompletion");
    }
}
