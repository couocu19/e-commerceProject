package com.mmall.service;

import com.mmall.common.ServletResponse;
import com.mmall.pojo.User;

public interface IUserService {


    ServletResponse<User> login(String username, String password);

    ServletResponse<String> register(User user);

    ServletResponse<String> checkVaild(String str,String type);

    ServletResponse forgetGetQuestion(String username);

    ServletResponse<String> checkQuestion(String username,String question,String answer);

    ServletResponse<String> forgetResetPassword(String username,String passwordNew,String forgetToken);

    ServletResponse<String> resetPassword(User user,String passwordOld,String passwordNew);

}
