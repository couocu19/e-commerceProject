package com.mmall.service.impl;

import com.mmall.common.ResponseCode;
import com.mmall.common.ServletResponse;
import com.mmall.dao.ProductMapper;
import com.mmall.pojo.Product;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.ws.ServiceMode;
import java.rmi.ServerError;

@Service("IProductService")
public class ProductServiceImpl {

    @Autowired
    private ProductMapper productMapper;


    public ServletResponse saveOrUpdateProduct(Product product){
        if(product!=null){
            if(StringUtils.isNotBlank(product.getSubImages())){
                //对幅图进行一个分割
                String[] subImageArray = product.getSubImages().split(",");
                if(subImageArray.length>0){
                    //将第一张附图设为主图
                    product.setMainImage(subImageArray[0]);
                }
                if(product.getId()!=null){
                   int rowCount =  productMapper.updateByPrimaryKey(product);
                   if(rowCount>0){
                       return ServletResponse.createBySuccess("更新产品信息成功！");
                   }

                   return ServletResponse.createByErrorMessage("更新商品信息失败！");

                }else{
                    int rowCount = productMapper.insert(product);
                    if(rowCount>0){
                        return ServletResponse.createBySuccess("更新产品信息成功！");
                    }

                    return ServletResponse.createByErrorMessage("更新商品信息失败！");

                }
            }
        }
        return ServletResponse.createByErrorMessage("商品的参数不正确！");
    }

    public ServletResponse setStatus(Integer productId,Integer status){

        if(productId == null || status == null){
            return ServletResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());

        }
        Product product = new Product();
        product.setStatus(status);
        product.setId(productId);

        int rowCount = productMapper.updateByPrimaryKeySelective(product);
        if(rowCount>0){
            return ServletResponse.createBySuccess("修改产品销售状态成功！");
        }

        return ServletResponse.createByErrorMessage("修改产品销售状态失败！");

    }

}
