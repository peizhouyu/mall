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
    ServerResponse<String> register(User user);
    ServerResponse<String> checkValid(String str, String type);
    ServerResponse selectQuestion(String username);
    ServerResponse<String> checkAnswer(String username, String question, String answer);
    ServerResponse<String> forgetRestPassword(String username, String passwordNew, String forgetToken);
    ServerResponse<String> resetPassowrd(String passwordOld, String passwordNew, User user);
    ServerResponse<User> updateInformation(User user);
    ServerResponse<User> getInformation(Integer userId);
    ServerResponse checkAdminRole(User user);
}
