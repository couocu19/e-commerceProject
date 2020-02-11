package com.mmall.service.impl;


import com.mmall.common.ServletResponse;
import com.mmall.dao.CategoryMapper;
import com.mmall.pojo.Category;
import com.mmall.service.ICategoryService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("ICategoryService")
public class CategoryServiceImpl implements ICategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    public ServletResponse addCategory(String categoryName,Integer parentId){

        if(parentId == null|| StringUtils.isBlank(categoryName)){

            return ServletResponse.createByErrorMessage("品类参数传递错误");
        }
        Category category = new Category();
        category.setName(categoryName);
        category.setParentId(parentId);
        category.setStatus(true);

        int rowCount = categoryMapper.insert(category);
        if(rowCount>0){
            return ServletResponse.createBySuccess("添加品类成功！");
        }

        return ServletResponse.createByErrorMessage("添加品类失败");

    }


    public ServletResponse<String> updateCategoryName(String categoryName,Integer categoryId){
        if(categoryId == null || StringUtils.isBlank(categoryName)){
            return ServletResponse.createByErrorMessage("品类参数错误!");
        }

        Category category = new Category();
        category.setId(categoryId);
        category.setName(categoryName);

        int rowCount = categoryMapper.updateByPrimaryKeySelective(category);
        if(rowCount>0){
            return ServletResponse.createBySuccess("品类信息更新成功！");
        }


        return ServletResponse.createByErrorMessage("品类信息更新失败");

    }

}
