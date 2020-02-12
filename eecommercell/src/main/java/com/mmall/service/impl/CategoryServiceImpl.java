package com.mmall.service.impl;


import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mmall.common.ServletResponse;
import com.mmall.dao.CategoryMapper;
import com.mmall.pojo.Category;
import com.mmall.service.ICategoryService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import sun.awt.geom.AreaOp;

import java.util.List;
import java.util.Set;


@Service("ICategoryService")
public class CategoryServiceImpl implements ICategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    private Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);

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


    public ServletResponse<String> setCategoryName(String categoryName,Integer categoryId){
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

    public ServletResponse<List<Category>> getChildrenParallelCategory(Integer categoryId){
        List<Category> list = categoryMapper.selectCategoryChildrenByParentId(categoryId);

        if(CollectionUtils.isEmpty(list)){
            logger.info("未找到当前分类的子分类");
        }
        return ServletResponse.createBySuccess(list);
    }

    /**
     * 递归查询本节点的id及孩子节点的id(深度查询)
     * @param categoryId
     * @return
     */
    public ServletResponse<List<Integer>> selectCategoryAndChildrenCategory(Integer categoryId) {

        Set<Category> set = Sets.newHashSet();
        findChildrenId(set,categoryId);


        List<Integer> categoryList = Lists.newArrayList();
        if(categoryId!=null) {
            for (Category category : set) {
                categoryList.add(category.getId());
            }
        }
        return ServletResponse.createBySuccess(categoryList);


    }

    private Set<Category> findChildrenId(Set<Category> categorySet,Integer id){

        //首先找到当前id对应的category
        Category category = categoryMapper.selectByPrimaryKey(id);
        if(category!=null){
            categorySet.add(category);
        }

        List<Category> list = categoryMapper.selectCategoryChildrenByParentId(id);

        //递归算法一定要有一个终止的条件

        for(Category category1:list){
            findChildrenId(categorySet,category1.getId());
        }


        return categorySet;




    }
}
