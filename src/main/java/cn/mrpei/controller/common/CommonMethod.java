package cn.mrpei.controller.common;

import cn.mrpei.common.ServerResponse;
import cn.mrpei.pojo.User;
import cn.mrpei.util.CookieUtil;
import cn.mrpei.util.JsonUtil;
import cn.mrpei.util.RedisPoolUtil;

import javax.servlet.http.HttpServletRequest;

/**
 * Date:2017/11/30
 * Time:22:12
 *
 * @author 裴周宇
 */
public class CommonMethod {

    public static User checkLoginStatus(HttpServletRequest httpServletRequest){
        String loginToken = CookieUtil.readLoginToken(httpServletRequest);
        if (loginToken == null){
            return null;
        }
        String userJsonStr = RedisPoolUtil.get(loginToken);
        User user = JsonUtil.stringToObj(userJsonStr, User.class);
        return user;
    }


}
