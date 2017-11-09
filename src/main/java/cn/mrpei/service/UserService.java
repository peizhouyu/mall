package cn.mrpei.service;

import cn.mrpei.common.ServerResponse;
import cn.mrpei.pojo.User;

/**
 * Date:2017/11/9
 * Time:11:18
 *
 * @author 裴周宇
 */
public interface UserService {
    ServerResponse<User> login(String username, String password);
}
