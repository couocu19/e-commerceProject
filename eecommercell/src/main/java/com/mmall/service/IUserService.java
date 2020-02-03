package com.mmall.service;

import com.mmall.common.ServletResponse;
import com.mmall.pojo.User;

public interface IUserService {


    ServletResponse<User> login(String username, String password);

    ServletResponse<String> register(User user);

    ServletResponse<String> checkVaild(String str,String type);
}
