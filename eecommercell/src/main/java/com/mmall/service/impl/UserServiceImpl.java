package com.mmall.service.impl;

import com.mmall.common.Const;
import com.mmall.common.ServletResponse;
import com.mmall.common.TokenCache;
import com.mmall.dao.UserMapper;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import com.mmall.util.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.omg.PortableServer.SERVANT_RETENTION_POLICY_ID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

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
        System.out.println(mdPassword);

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

    public ServletResponse forgetGetQuestion(String username){
        ServletResponse vaildResponse = this.checkVaild(username,Const.USERNAME);
        if(vaildResponse.isSuccess()){
            return ServletResponse.createByErrorMessage("该用户不存在！");
        }

        String question = userMapper.checkQuestionByUsername(username);

        if(StringUtils.isNoneBlank(question)) {
            return ServletResponse.createBySuccess(question);
        }
        return ServletResponse.createByErrorMessage("找回密码的问题是空的");
    }

    public ServletResponse<String> checkQuestion(String username,String question,String answer){

        int resultCount = userMapper.checkQuestion(username,question,answer);
        if(resultCount>0){
            //说明问及问题的答案是这个用户的,并且答案是正确的
            String forgetToken = UUID.randomUUID().toString();
            TokenCache.setKey(TokenCache.TOKEN_PREFIX+username,forgetToken);
            return ServletResponse.createBySuccess(forgetToken);
        }
        return ServletResponse.createByErrorMessage("问题答案错误");

    }

    public ServletResponse<String> forgetResetPassword(String username,String passwordNew,String forgetToken){

        if(StringUtils.isBlank(forgetToken)){
            return ServletResponse.createByErrorMessage("参数错误,token需要传递");
        }

        ServletResponse vaildResponse = this.checkVaild(username,Const.USERNAME);
        if(vaildResponse.isSuccess()){
            return ServletResponse.createByErrorMessage("该用户不存在");
        }

        String token = TokenCache.getKey(TokenCache.TOKEN_PREFIX+username);
        if(StringUtils.isNoneBlank(token)){
            return ServletResponse.createByErrorMessage("token无效或者过期");
        }

        if(StringUtils.equals(token,forgetToken)){
            String password = MD5Util.MD5EncodeUtf8(passwordNew);
           int rowCount = userMapper.updatePasswordByUsername(username,password);

           if(rowCount>0){
               return ServletResponse.createBySuccessMessage("修改密码成功!");
           }
        }else{

            return ServletResponse.createByErrorMessage("token错误,请重置获取密码的token");
        }

        return ServletResponse.createByErrorMessage("修改密码失败!");


    }
    public ServletResponse<String> resetPassword(User user,String passwordOld,String passwordNew){
        int id = user.getId();

        int rowCount = userMapper.checkPassword(MD5Util.MD5EncodeUtf8(passwordOld),id);

        //校验旧密码是否错误
        if(rowCount == 0){
            return ServletResponse.createByErrorMessage("旧密码错误");
        }
        user.setPassword(MD5Util.MD5EncodeUtf8(passwordNew));
        int rowCount1 = userMapper.updateByPrimaryKeySelective(user);
        if(rowCount1>0){
            return ServletResponse.createBySuccessMessage("修改密码成功");
        }
        return ServletResponse.createByErrorMessage("修改密码失败!");


    }

    public ServletResponse<User> updateInformation(User user){

        //username不能修改
        //校验email：校验当前email在数据库中是不是已经存在，如果存在这个email不能为当前用户所用

        String email = user.getEmail();
        Integer id = user.getId();

        int rowCount = userMapper.checkEmailByUserId(email,id);
        if(rowCount>0){
            return ServletResponse.createByErrorMessage("当前email已经存在!请更换email后再尝试更新");
        }

        User updateUser = new User();

        updateUser.setId(id);
        updateUser.setEmail(email);

        updateUser.setPhone(user.getPhone());
        updateUser.setQuestion(user.getQuestion());
        updateUser.setAnswer(user.getAnswer());

        int updateCount = userMapper.updateByPrimaryKeySelective(updateUser);

        if(updateCount>0){

            return ServletResponse.createBySuccess("更新信息成功",updateUser);
        }

        return ServletResponse.createByErrorMessage("更新信息失败!");


    }

    public ServletResponse<User> get_information(Integer userId){

        User user = userMapper.selectByPrimaryKey(userId);
        if(user == null){
            return ServletResponse.createByErrorMessage("找不到当前用户");
        }

        user.setPassword(StringUtils.EMPTY);

        return ServletResponse.createBySuccess(user);

    }

    public ServletResponse<String> check_admin_role(User user){
        if(user!=null && user.getRole().intValue() == Const.Role.ROLE_ADMIN){
            return ServletResponse.createBySuccess();
        }
        return ServletResponse.createByError();
    }










}
