package com.mmall.service;

import com.mmall.common.ServletResponse;

public interface ICategoryService {

    ServletResponse addCategory(String categoryName, Integer categoryId);

    ServletResponse<String> updateCategoryName(String categoryName,Integer categoryId);

}
