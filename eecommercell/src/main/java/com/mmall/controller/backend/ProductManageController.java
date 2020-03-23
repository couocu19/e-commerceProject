package com.mmall.controller.backend;


import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServletResponse;
import com.mmall.pojo.Product;
import com.mmall.pojo.User;
import com.mmall.service.IFileService;
import com.mmall.service.IProductService;
import com.mmall.service.IUserService;
import com.mmall.util.PropertiesUtil;
import com.mmall.vo.ProductDetailVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

@Controller
@RequestMapping("manage/product")
public class
ProductManageController {

    @Autowired
    private IUserService iUserService;

    @Autowired
    private IProductService iProductService;

    @Autowired
    private IFileService iFileService;

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

    @RequestMapping("search.do")
    @ResponseBody
    //pageSize:一个页面的页容量 //pageNum：第几页
    public ServletResponse<PageInfo> searchProduct(HttpSession session, Integer productId,String productName,@RequestParam(name = "pageNum",defaultValue = "1") int pageNum, @RequestParam(name = "pageSize",defaultValue = "10")int pageSize){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServletResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录,请先登录.");
        }
        if(iUserService.check_admin_role(user).isSuccess()){
            //业务层
            return iProductService.searchProduct(productId,productName,pageNum,pageSize);
        }else{
            return ServletResponse.createByErrorMessage("没有操作权限");
        }

    }

    @RequestMapping("upload.do")
    @ResponseBody
    //required:是否为必须？
    public ServletResponse upload(@RequestParam(value = "upload_file",required = false) MultipartFile file, HttpServletRequest request,HttpSession session){

        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServletResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录,请先登录.");
        }
        //首先要判断权限
        if(iUserService.check_admin_role(user).isSuccess()){
            //业务层
            String path = request.getSession().getServletContext().getRealPath("upload");
            String targetFileName = iFileService.upload(file,path);
            String url = PropertiesUtil.getProperty("ftp.server.http.prefix")+targetFileName;

            Map fileMap = Maps.newHashMap();
            fileMap.put("uri",targetFileName);
            fileMap.put("url",url);
            System.out.println(url);
            return ServletResponse.createBySuccess(fileMap);
        }else{
            return ServletResponse.createByErrorMessage("没有操作权限");
        }

    }

    @RequestMapping("richtext_img_upload.do")
    @ResponseBody
    //富文本上传文件
    public Map richTextImgUpload(@RequestParam(value = "upload_file",required = false) MultipartFile file, HttpServletRequest request, HttpSession session, HttpServletResponse response){
        Map resultMap = Maps.newHashMap();
        //富文本中对于返回值有自己的要求,我们使用的是simditor所以按照simditor的要求进行返回
        //上传文件时,必须返回一个指定格式的 json, 不然报错
//        {
//                'success' => true,
//                'msg' => '图片上传错误信息',
//                'file_path' => '/upload/2018_10_11/1.jpg'
//        }
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            resultMap.put("success",false);
            resultMap.put("msg","请先登录管理员");
            return resultMap;
        }
        if(iUserService.check_admin_role(user).isSuccess()){
            //业务层
            String path = request.getSession().getServletContext().getRealPath("upload");
            String targetFileName = iFileService.upload(file,path);

            if(StringUtils.isBlank(targetFileName)){
                resultMap.put("success",false);
                resultMap.put("msg","上传失败");
                return resultMap;

            }
            String url = PropertiesUtil.getProperty("ftp.server.http.prefix"+targetFileName);

            resultMap.put("success",true);
            resultMap.put("msg","上传成功");
            resultMap.put("file_path",url);

            response.addHeader("Access-Control_Allow_Headers","X-File_Name");
            return resultMap;
        }else{
            resultMap.put("success",false);
            resultMap.put("msg","无权限操作");
            return resultMap;
        }

    }





}
