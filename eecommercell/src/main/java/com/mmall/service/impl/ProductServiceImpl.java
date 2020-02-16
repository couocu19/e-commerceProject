package com.mmall.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServletResponse;
import com.mmall.dao.CategoryMapper;
import com.mmall.dao.ProductMapper;
import com.mmall.pojo.Category;
import com.mmall.pojo.Product;
import com.mmall.service.IProductService;
import com.mmall.util.DateTimeUtil;
import com.mmall.util.PropertiesUtil;
import com.mmall.vo.ProductDetailVo;
import com.mmall.vo.ProductListVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.ws.ServiceMode;
import java.rmi.ServerError;
import java.util.ArrayList;
import java.util.List;

@Service("IProductService")
public class ProductServiceImpl implements IProductService {

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private CategoryMapper categoryMapper;


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

    public ServletResponse<ProductDetailVo> manageProductDetail(Integer productId){
        if(productId == null){
            return ServletResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }

        Product product = productMapper.selectByPrimaryKey(productId);

        if(product == null){
            return ServletResponse.createByErrorMessage("商品已下架或者被删除");

        }
        ProductDetailVo productDetailVo = assembleProductDetailVo(product);

        return ServletResponse.createBySuccess(productDetailVo);
    }

    private ProductDetailVo assembleProductDetailVo(Product product){
        ProductDetailVo productDetailVo = new ProductDetailVo();
        productDetailVo.setId(product.getId());
        productDetailVo.setCategoryId(product.getCategoryId());
        productDetailVo.setName(product.getName());
        productDetailVo.setStatus(product.getStatus());
        productDetailVo.setSubtitle(product.getSubtitle());
        productDetailVo.setPrice(product.getPrice());
        productDetailVo.setStock(product.getStock());
        productDetailVo.setMainImage(product.getMainImage());
        productDetailVo.setSubImages(product.getSubImages());
        productDetailVo.setDetail(product.getDetail());

        //imageHost
        productDetailVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix","http://img.happymmall.com/"));
        //parentCategoryId
        Category category = categoryMapper.selectByPrimaryKey(product.getCategoryId());
        if(category == null){
            productDetailVo.setCategoryId(0); //默认为根节点
        }else{
            productDetailVo.setCategoryId(category.getParentId());
        }

        productDetailVo.setCreateTime(DateTimeUtil.dateToStr(product.getCreateTime()));
        productDetailVo.setUpdateTime(DateTimeUtil.dateToStr(product.getUpdateTime()));

        return productDetailVo;

    }

    public ServletResponse<PageInfo> getProductList(int pageNum, int pageSize){
        //startPage-start
        PageHelper.startPage(pageNum,pageSize);
        //填充自己的查询逻辑
        List<Product> selectList = productMapper.selectList();
        List<ProductListVo> resultList = new ArrayList<>();
        for(Product product:selectList){
            ProductListVo productListVo = assembleProductListVo(product) ;
            resultList.add(productListVo);
        }
        //pageHelper收尾
        PageInfo pageInfo = new PageInfo(selectList);
        pageInfo.setList(resultList);

        return ServletResponse.createBySuccess(pageInfo);

    }

    private ProductListVo assembleProductListVo(Product product){
        ProductListVo productListVo = new ProductListVo();
        productListVo.setId(product.getId());
        productListVo.setCategoryId(product.getCategoryId());
        productListVo.setImageHost(product.getMainImage());
        productListVo.setMainImage(product.getMainImage());
        productListVo.setPrice(product.getPrice());
        productListVo.setStatus(product.getStatus());
        productListVo.setName(product.getName());
        productListVo.setSubtitle(product.getSubtitle());

        return productListVo;

    }

}
