package com.huang.BBS.service;

import com.huang.BBS.vo.CategoryVo;
import com.huang.BBS.vo.Result;

public interface CategoryService {

    CategoryVo findCategoryById(Long categoryId);

    Result findAll();

    Result findAllDetail();

    Result categoriesDetailById(Long id);
}
