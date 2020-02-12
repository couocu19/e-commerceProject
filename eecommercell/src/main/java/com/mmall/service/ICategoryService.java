package com.mmall.service;

import com.mmall.common.ServletResponse;
import com.mmall.pojo.Category;

import java.util.List;

public interface ICategoryService {

    ServletResponse addCategory(String categoryName, Integer categoryId);

    ServletResponse<String> setCategoryName(String categoryName,Integer categoryId);

    ServletResponse<List<Category>> getChildrenParallelCategory(Integer categoryId);

    ServletResponse<List<Integer>> selectCategoryAndChildrenCategory(Integer categoryId);


}
