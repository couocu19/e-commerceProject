package com.mmall.controller.protal;

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
@RequestMapping("/user/")
public class UserController {

    @Autowired
    private IUserService iUserService;

    //登录接口
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

    //登出接口
    @RequestMapping(value = "logout.do",method = RequestMethod.GET)
    @ResponseBody
    public ServletResponse<String> logout(HttpSession session){
        session.removeAttribute(Const.CURRENT_USER);

        return ServletResponse.createBySuccess();

    }


    //注册接口
    @RequestMapping(value = "register.do",method = RequestMethod.GET)
    @ResponseBody
    public ServletResponse<String> register(User user){

        return iUserService.register(user);
    }

    //校验接口
    @RequestMapping(value = "check_vaild.do",method = RequestMethod.GET)
    @ResponseBody
    public ServletResponse<String> checkVaild(String str,String type){

        return iUserService.checkVaild(str,type);
    }

}
