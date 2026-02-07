package com.moxun.service.admin;

import com.moxun.Pojo.Dto.CourseCategoryDTO;
import com.moxun.Pojo.Vo.CourseCategoryVO;
import com.moxun.Pojo.Vo.PageResult;

import java.util.List;

public interface AdminCourseCategoryService {

    List<CourseCategoryVO> listCategories(Integer page, Integer pageSize);

    List<CourseCategoryVO> listChildren(Integer parentId);

    void addCategory(CourseCategoryDTO courseCategoryDTO);

    void updateCategory(CourseCategoryDTO courseCategoryDTO);

    void deleteCategory(Integer parentId);
}
