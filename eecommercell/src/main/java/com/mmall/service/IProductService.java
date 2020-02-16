package com.mmall.service;

import com.github.pagehelper.PageInfo;
import com.mmall.common.ServletResponse;
import com.mmall.pojo.Product;
import com.mmall.vo.ProductDetailVo;

public interface IProductService {

    ServletResponse saveOrUpdateProduct(Product product);

    ServletResponse setStatus(Integer productId,Integer status);

    ServletResponse<ProductDetailVo> manageProductDetail(Integer productId);

    ServletResponse<PageInfo> getProductList(int pageNum, int pageSize)
}
