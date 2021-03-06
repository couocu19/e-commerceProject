package com.mmall.controller.backend;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServletResponse;
import com.mmall.dao.CategoryMapper;
import com.mmall.pojo.Category;
import com.mmall.pojo.User;
import com.mmall.service.ICategoryService;
import com.mmall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.util.List;

@Controller
@RequestMapping(("/manage/category"))
public class CategoryMangeController {

    @Autowired
    private IUserService iUserService;

    @Autowired
    private ICategoryService iCategoryService;


    @RequestMapping("add_category.do")
    @ResponseBody
    public ServletResponse addCategory(HttpSession session,String categoryName,@RequestParam(name = "categoryId",defaultValue = "0") Integer categoryId){

        User user = (User)session.getAttribute(Const.CURRENT_USER);

        if (user == null) {
            return ServletResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登录");
        }

        //确认该用户是不是管理员身份
        if(iUserService.check_admin_role(user).isSuccess()){
            return iCategoryService.addCategory(categoryName,categoryId);
        }else{
            return ServletResponse.createByErrorMessage("该用户不是管理员,没有权限！");
        }

    }


    @RequestMapping("set_category_name.do")
    @ResponseBody
    public ServletResponse<String> setCategoryName(HttpSession session,String categoryName,Integer categoryId){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServletResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录！");
        }

        if(iUserService.check_admin_role(user).isSuccess()){

            return iCategoryService.setCategoryName(categoryName,categoryId);
        }else{

            return ServletResponse.createByErrorMessage("不是管理员，没有操作权限！");
        }

    }


    @RequestMapping("get_children_parallel_category.do")
    @ResponseBody
    public ServletResponse<List<Category>> getChildrenParallelCategory(HttpSession session,@RequestParam(name = "categoryId",defaultValue = "0") Integer categoryId){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServletResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录！");
        }
        if(iUserService.check_admin_role(user).isSuccess()){
            //查询子节点category的信息,并且不递归,保持平级
            return iCategoryService.getChildrenParallelCategory(categoryId);
        }else{
            return ServletResponse.createByErrorMessage("不是管理员，没有操作权限！");
        }
    }

    @RequestMapping("get_category_and_children.do")
    @ResponseBody
    public ServletResponse<List<Integer>> getCategoryAndDeepChildrenCategory(HttpSession session,@RequestParam(name = "parentId",defaultValue = "0") Integer parentId) {

        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServletResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录！");
        }
        if(iUserService.check_admin_role(user).isSuccess()){
            //查询当前节点的id和当前节点的子节点的id
            //递归查询

            return iCategoryService.selectCategoryAndChildrenCategory(parentId);

        }else{
            return ServletResponse.createByErrorMessage("不是管理员，没有操作权限！");
        }


    }

}
