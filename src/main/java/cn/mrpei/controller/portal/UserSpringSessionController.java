package cn.mrpei.controller.portal;

import cn.mrpei.common.Const;
import cn.mrpei.common.ResponseCode;
import cn.mrpei.common.ServerResponse;
import cn.mrpei.controller.common.CommonMethod;
import cn.mrpei.pojo.User;
import cn.mrpei.service.UserService;
import cn.mrpei.util.CookieUtil;
import cn.mrpei.util.JsonUtil;
import cn.mrpei.util.RedisShardedPoolUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Date:2017/11/9
 * Time:10:30
 *  Spring Session demo Spring Session 默认无法使用redis 分片 缓存  优点：框架非入侵 只需要在web.xml 配置springSessionRepositoryFilter  分情况场合酌情使用
 * @author 裴周宇
 */
@Controller
@RequestMapping("/user/springsession")
public class UserSpringSessionController {

    @Autowired
    private UserService userService;

    /**
     *  登录
     * @param username
     * @param password
     * @param session
     * @return
     */
    @RequestMapping(value = "/login.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> login(String username, String password, HttpSession session, HttpServletResponse httpServletResponse){
        ServerResponse<User> response = userService.login(username, password);
        if (response.isSuccess()){
              session.setAttribute(Const.CURRENT_USER,response.getData());
//            CookieUtil.writeLoginToken(httpServletResponse,session.getId());
//            RedisShardedPoolUtil.setEx(session.getId(), JsonUtil.objToString(response.getData()),Const.RedisCacheExtime.REDIS_SESSION_EXTIME);
        }
        return response;
    }

    @RequestMapping(value = "/logout.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> logout(HttpServletRequest httpServletRequest, HttpSession session, HttpServletResponse httpServletResponse){
        session.removeAttribute(Const.CURRENT_USER);
//        String loginToken = CookieUtil.readLoginToken(httpServletRequest);
//        CookieUtil.delLoginToken(httpServletRequest,httpServletResponse);
//        RedisShardedPoolUtil.del(loginToken);
        return ServerResponse.createBySuccess();
    }


    @RequestMapping(value = "/get_user_info.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> getUserInfo(HttpServletRequest httpServletRequest, HttpSession session){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
//        String loginToken = CookieUtil.readLoginToken(httpServletRequest);
//        if (loginToken == null){
//            return ServerResponse.createByErrorMessage("用户未登录");
//        }
//        String userJsonStr = RedisShardedPoolUtil.get(loginToken);
//        User user = JsonUtil.stringToObj(userJsonStr, User.class);
       // User user = CommonMethod.checkLoginStatus(httpServletRequest);
        if (user != null){
            return ServerResponse.createBySuccess(user);
        }
        return ServerResponse.createByErrorMessage("用户未登录");
    }



}
