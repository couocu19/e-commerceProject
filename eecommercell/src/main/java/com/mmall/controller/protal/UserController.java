package com.mmall.controller.protal;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServletResponse;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import org.omg.PortableServer.SERVANT_RETENTION_POLICY_ID;
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
    @RequestMapping(value = "logout.do",method = RequestMethod.POST)
    @ResponseBody
    public ServletResponse<String> logout(HttpSession session){
        session.removeAttribute(Const.CURRENT_USER);

        return ServletResponse.createBySuccess();

    }


    //注册接口
    @RequestMapping(value = "register.do",method = RequestMethod.POST)
    @ResponseBody
    public ServletResponse<String> register(User user){

        return iUserService.register(user);
    }

    //校验接口
    @RequestMapping(value = "check_vaild.do",method = RequestMethod.POST)
    @ResponseBody
    public ServletResponse<String> checkVaild(String str,String type){

        return iUserService.checkVaild(str,type);
    }

    //获取用户信息的接口
    @RequestMapping(value = "get_user_info.do",method = RequestMethod.POST)
    @ResponseBody
    public ServletResponse<User> getUserInfo(HttpSession session){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user!=null){
            return ServletResponse.createBySuccess(user);
        }
        return ServletResponse.createByErrorMessage("用户未登录，无法获取用户当前的信息");
    }


    @RequestMapping(value = "forget_get_question.do",method = RequestMethod.POST)
    @ResponseBody
    public ServletResponse<String> forgetGetQuestion(String username){

        return iUserService.forgetGetQuestion(username);

    }


    @RequestMapping(value = "forget_check_answer.do",method = RequestMethod.POST)
    @ResponseBody
    public ServletResponse<String> forgetCheckAnswer(String username,String question,String answer){

        return iUserService.checkQuestion(username,question,answer);

    }


    //忘记密码前提下的重置密码
    @RequestMapping(value = "forget_reset_password.do",method = RequestMethod.POST)
    @ResponseBody
    public ServletResponse<String> forgetResetPassword(String username,String passwordNew,String forgetToken){

        return iUserService.forgetResetPassword(username,passwordNew,forgetToken);
    }



    //在已经登录状态下的重置密码
    @RequestMapping(value = "reset_password.do",method = RequestMethod.POST)
    @ResponseBody
    public ServletResponse<String> resetPassword(HttpSession session,String passwordNew,String passwordOld){

        User user = (User) session.getAttribute(Const.CURRENT_USER);

        if(user == null){
            return ServletResponse.createByErrorMessage("用户未登录");
        }
        return iUserService.resetPassword(user,passwordOld,passwordNew);
    }

    @RequestMapping(value = "update_information.do",method = RequestMethod.POST)
    @ResponseBody
    public ServletResponse<User> update_information(HttpSession session,User user){
        User currentUser = (User)session.getAttribute(Const.CURRENT_USER);
        if(currentUser == null){
            return ServletResponse.createByErrorMessage("用户未登录");
        }

        //用户的id和username不能更改
        user.setId(currentUser.getId());
        user.setUsername(currentUser.getUsername());

        ServletResponse<User> response = iUserService.updateInformation(user);

        //如果更新信息成功，就要更新session域中的user的信息
        if(response.isSuccess()){
            session.setAttribute(Const.CURRENT_USER,response.getData());
        }

        return response;

    }


    @RequestMapping(value = "get_information.do",method = RequestMethod.POST)
    @ResponseBody
    public ServletResponse<User> get_informaion(HttpSession session){
        User currentUser = (User)session.getAttribute(Const.CURRENT_USER);
        if (currentUser == null){
            return ServletResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),
                    "用户未登录,需要强制登录status=10");
        }

        Integer id = currentUser.getId();

        return iUserService.get_information(id);

    }


}
