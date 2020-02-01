package com.mmall.service.impl;

import com.mmall.common.ServletResponse;
import com.mmall.dao.UserMapper;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("iUserService")
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public ServletResponse<User> login(String username, String password) {
        int resultCount = userMapper.checkUsername(username);

        if(resultCount == 0){
            return ServletResponse.createByErrorMessage("该用户不存在");
        }

        //todo 密码登录MD5

        User user = userMapper.selectLogin(username,password);
        if(user == null){

            return ServletResponse.createByErrorMessage("密码错误");
        }


        //将其密码置为空
        user.setPassword(org.apache.commons.lang3.StringUtils.EMPTY);


        return ServletResponse.createBySuccess("登陆成功",user);


    }
}
