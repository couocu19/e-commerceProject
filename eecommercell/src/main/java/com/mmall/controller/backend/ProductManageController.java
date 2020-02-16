package com.mmall.controller.backend;


import com.github.pagehelper.PageInfo;
import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServletResponse;
import com.mmall.pojo.Product;
import com.mmall.pojo.User;
import com.mmall.service.IProductService;
import com.mmall.service.IUserService;
import com.mmall.vo.ProductDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/manage/product")
public class ProductManageController {

    @Autowired
    private IUserService iUserService;

    @Autowired
    private IProductService iProductService;

    @RequestMapping("product_save.do")
    @ResponseBody
   public ServletResponse productSave(HttpSession session, Product product){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServletResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录,请先登录.");
        }

        if(iUserService.check_admin_role(user).isSuccess()){
            //业务逻辑部分
            return iProductService.saveOrUpdateProduct(product);

        }else{

            return ServletResponse.createByErrorMessage("没有操作权限");
        }


    }


    //设置产品的出售状态
    @RequestMapping("set_status.do")
    @ResponseBody
    public ServletResponse setStatus(HttpSession session, Integer productId,Integer status){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServletResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录,请先登录.");
        }

        if(iUserService.check_admin_role(user).isSuccess()){
            //业务逻辑部分
            return iProductService.setStatus(productId,status);
        }else{
            return ServletResponse.createByErrorMessage("没有操作权限");
        }

    }


    //获取产品接口
    @RequestMapping("detail.do")
    @ResponseBody
    public ServletResponse<ProductDetailVo> getDetail(HttpSession session, Integer productId){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServletResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录,请先登录.");
        }

        if(iUserService.check_admin_role(user).isSuccess()){
            return iProductService.manageProductDetail(productId);
        }else{
            return ServletResponse.createByErrorMessage("没有操作权限");
        }

    }

    @RequestMapping("list.do")
    @ResponseBody
    //pageSize:一个页面的页容量 //pageNum：第几页
    public ServletResponse<PageInfo> getList(HttpSession session, @RequestParam(name = "pageNum",defaultValue = "1") int pageNum, @RequestParam(name = "pageSize",defaultValue = "10")int pageSize){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServletResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录,请先登录.");
        }
        if(iUserService.check_admin_role(user).isSuccess()){
            //业务层
            return iProductService.getProductList(pageNum,pageSize);

        }else{
            return ServletResponse.createByErrorMessage("没有操作权限");
        }

    }




}
