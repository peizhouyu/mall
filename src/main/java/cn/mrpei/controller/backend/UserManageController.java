package cn.mrpei.controller.backend;

import cn.mrpei.common.Const;
import cn.mrpei.common.ServerResponse;
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

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Date:2017/11/10
 * Time:13:39
 *
 * @author 裴周宇
 */
@Controller
@RequestMapping("/manage/user")
public class UserManageController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "login.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<User> login(String username, String password, HttpSession session, HttpServletResponse httpServletResponse){
        ServerResponse<User> response = userService.login(username,password);
        if (response.isSuccess()){
            User user = response.getData();
            if (user.getRole() == Const.Role.ROLE_ADMIN){
                //session.setAttribute(Const.CURRENT_USER, user);
                //新增redis共享cookie
                CookieUtil.writeLoginToken(httpServletResponse,session.getId());
                RedisShardedPoolUtil.setEx(session.getId(), JsonUtil.objToString(response.getData()),Const.RedisCacheExtime.REDIS_SESSION_EXTIME);
                return response;
            }else {
                return ServerResponse.createByErrorMessage("不是管理员");
            }
        }
        return response;
    }
}
