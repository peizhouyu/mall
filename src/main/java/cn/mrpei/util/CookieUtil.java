package cn.mrpei.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Date:2017/11/30
 * Time:20:09
 *
 * @author 裴周宇
 */
@Slf4j
public class CookieUtil {

    private final static String COOKIE_DOMAIN = PropertiesUtil.getProperty("cookie.domain");
    private final static String COOKIE_NAME = PropertiesUtil.getProperty("cookie_name");


    public static String readLoginToken(HttpServletRequest request){
        Cookie[] cookies = request.getCookies();
        if (cookies != null){
            for (Cookie cookie : cookies){
                log.info("read write cookieName:{},cookieValue:{}",cookie.getName(),cookie.getValue());
                if (StringUtils.equals(cookie.getName(),COOKIE_NAME)){
                    log.info("return cookieName:{},cookieValue:{}",cookie.getName(),cookie.getValue());
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    public static void writeLoginToken(HttpServletResponse response, String token){
        Cookie cookie = new Cookie(COOKIE_NAME,token);
        cookie.setDomain(COOKIE_DOMAIN);
        cookie.setPath("/"); //设置在根目录
        cookie.setHttpOnly(true); //不许脚本获取cookie
        //单位是秒
        //如果这个maxAge 不设置的话，cookie就不会写入硬盘，而是写在内存。只在当前页面有效
        cookie.setMaxAge(60 * 60 * 24 * 365); //如果是 -1 ，代表永久
        log.info("write cookieName:{},cookieValue:{}",cookie.getName(),cookie.getValue());

        response.addCookie(cookie);
    }

    public static void delLoginToken(HttpServletRequest request, HttpServletResponse response){
        Cookie[] cookies = request.getCookies();
        if (cookies != null){
            for (Cookie cookie : cookies){
                if (StringUtils.equals(cookie.getName(),COOKIE_NAME)){
                    cookie.setDomain(COOKIE_DOMAIN);
                    cookie.setPath("/");
                    cookie.setMaxAge(0); //设置成 0 ，代表删除此cookie
                    log.info("del cookieName:{},cookieValue:{}",cookie.getName(),cookie.getValue());
                    response.addCookie(cookie);
                    return;
                }
            }
        }
    }

}
