package com.mmall.service.impl;

import com.mmall.common.Const;
import com.mmall.common.ServletResponse;
import com.mmall.dao.UserMapper;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import com.mmall.util.MD5Util;
import org.apache.commons.lang3.StringUtils;
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
        String mdPassword = MD5Util.MD5EncodeUtf8(password);
        User user = userMapper.selectLogin(username,mdPassword);

        if(user == null){
            return ServletResponse.createByErrorMessage("密码错误");
        }
        //将其密码置为空
        user.setPassword(org.apache.commons.lang3.StringUtils.EMPTY);
        return ServletResponse.createBySuccess("登陆成功",user);
    }


    public ServletResponse<String> register(User user){
        //注册时首先校验用户名和email是否已经存在
        ServletResponse vaildResponse = this.checkVaild(user.getUsername(),Const.USERNAME);
        if(!vaildResponse.isSuccess()){
            return vaildResponse;
        }
        vaildResponse = this.checkVaild(user.getUsername(),Const.USERNAME);
        if(!vaildResponse.isSuccess()){
            return vaildResponse;
        }

        user.setRole(Const.Role.ROLE_CUSTOMER);
        //Md5加密
        user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));

        int resultCount = userMapper.insert(user);

        if(resultCount == 0){
            return ServletResponse.createByErrorMessage("注册失败");
        }

        return ServletResponse.createBySuccessMessage("注册成功~");


    }

    //检测email或者用户名是否有效
    //防止外界恶意利用接口登录
    public ServletResponse<String> checkVaild(String str,String type){
        //首先确认接收的信息为有效信息,不为空
        if(StringUtils.isNoneBlank(type)){
            int resultCount;
            if(Const.USERNAME.equals(type)){
                 resultCount = userMapper.checkUsername(str);
                if(resultCount > 0){
                    return ServletResponse.createByErrorMessage("该用户已存在！");
                }
            }
            if(Const.EMAIL.equals(type)){
                resultCount = userMapper.checkEmail(str);
                if(resultCount > 0){
                    return ServletResponse.createByErrorMessage("email已存在！");
                }

            }

        }else{

            return ServletResponse.createByErrorMessage("参数错误");
        }

        return ServletResponse.createBySuccessMessage("校验成功！");


    }







}
