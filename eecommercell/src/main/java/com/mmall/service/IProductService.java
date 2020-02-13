package com.mmall.service;

import com.mmall.common.ServletResponse;
import com.mmall.pojo.Product;

public interface IProductService {

    ServletResponse saveOrUpdateProduct(Product product);

    ServletResponse setStatus(Integer productId,Integer status);

}
