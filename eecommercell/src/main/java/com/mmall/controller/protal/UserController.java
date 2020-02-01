package com.mmall.controller.protal;

import com.mmall.common.Const;
import com.mmall.common.ServletResponse;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.HttpSessionRequiredException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;


@Controller
@RequestMapping("/user/")
public class UserController {


    @Autowired
    private IUserService iUserService;




    @RequestMapping(value = "login.do",method = RequestMethod.POST)
    @ResponseBody
    public ServletResponse<User> login(String username, String password, HttpSession session){

        //service->mybatis->dao
        ServletResponse<User> response = iUserService.login(username,password);
        if(response.isSuccess()){

            session.setAttribute(Const.CURRENT_USER,response.getData());
        }
        return response;
    }


    
}
