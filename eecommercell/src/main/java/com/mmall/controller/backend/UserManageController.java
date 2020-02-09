package com.mmall.controller.backend;

import com.mmall.common.Const;
import com.mmall.common.ServletResponse;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("manage/user")
public class UserManageController {
    @Autowired
    private IUserService iUserService;


    @RequestMapping(value = "login.do",method = RequestMethod.POST)
    @ResponseBody
    public ServletResponse<User> login(String username, String password, HttpSession session){

        ServletResponse<User> response = iUserService.login(username,password);
        //登陆成功
        if(response.isSuccess()){
            User user = response.getData();
            //确定是管理员的身份
            if(user.getRole().equals(Const.Role.ROLE_ADMIN)){
                session.setAttribute(Const.CURRENT_USER,user);
            }else{
                return ServletResponse.createByErrorMessage("不是管理员,无法登陆");
            }

        }

        return response;

    }


}
