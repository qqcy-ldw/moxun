package com.moxun.service.admin.impl;

import com.moxun.Pojo.Dto.CourseCategoryDTO;
import com.moxun.Pojo.Vo.CourseCategoryVO;
import com.moxun.mapper.admin.AdminCourseCategoryMapper;
import com.moxun.service.admin.AdminCourseCategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AdminCourseCategoryServiceImpl implements AdminCourseCategoryService {

    @Autowired
    private AdminCourseCategoryMapper adminCourseCategoryMapper;

    /**
     * 获取所有分类
     * @return
     */
    @Override
    public List<CourseCategoryVO> listCategories(Integer page, Integer pageSize) {
        List<CourseCategoryVO> courseCategory = adminCourseCategoryMapper.listCategories();
        log.info("获取所有分类:{}", courseCategory);
        //找出顶级分类（parentId为null或0）
        List<CourseCategoryVO> rootCategories = courseCategory.stream().
                filter(
                        courseCategories -> courseCategories.getParentId() == null || courseCategories.getParentId() == 0
            ).toList();
        log.info("过滤后获取所有分类:{}", courseCategory);
        //使用递归找出子分类
        //1.循环构建树
        for (CourseCategoryVO courseCategoryVO : courseCategory){
            buildChildren(courseCategoryVO,courseCategory);
        }
        return rootCategories;
    }

    /**
     * 根据父类ID获取所有子分类
     * @return
     */
    @Override
    public List<CourseCategoryVO> listChildren(Integer parentId) {
        // 1. 查出当前父节点的直接子节点（顶层）
        List<CourseCategoryVO> directChildren = adminCourseCategoryMapper.listChildren(parentId);
        // 2. 递归为每个子节点挂载它们的子节点
        for (CourseCategoryVO child : directChildren) {
            List<CourseCategoryVO> grandchildren = listChildren(Math.toIntExact(child.getId())); // 递归调用
            child.setChildren(grandchildren);
        }
        return directChildren;
    }

    /**
     * 添加分类
     * @param courseCategoryDTO
     */
    @Override
    public void addCategory(CourseCategoryDTO courseCategoryDTO) {
        courseCategoryDTO.setParentId(0);
        courseCategoryDTO.setSort(0);
        adminCourseCategoryMapper.addCategory(courseCategoryDTO);
    }

    /**
     * 修改分类
     * @param courseCategoryDTO
     */
    @Override
    public void updateCategory(CourseCategoryDTO courseCategoryDTO) {
        if (courseCategoryDTO.getId() == null) throw new RuntimeException("分类ID不能为空");
        adminCourseCategoryMapper.updateCategory(courseCategoryDTO);
    }

    /**
     * 删除分类
     * @param parentId
     */
    @Override
    @Transactional
    public void deleteCategory(Integer parentId) {
        //获取所有顶级分类
        List<CourseCategoryVO> courseCategoryVO = adminCourseCategoryMapper.listCategories();
        List<CourseCategoryVO> parentCourseCategory =courseCategoryVO.stream()
                .filter(courseCategory -> courseCategory.getParentId() == null || courseCategory.getParentId() == 0)
                .toList();
        //删除顶级父类及以下的子类
        for (CourseCategoryVO courseCategory : parentCourseCategory){
            if (Long.valueOf(parentId).equals(courseCategory.getId())){
                List<CourseCategoryVO> posterity = adminCourseCategoryMapper.listChildren(parentId);
                List<Long> posterityId = posterity.stream().map(CourseCategoryVO::getId).toList();
                adminCourseCategoryMapper.delPosterityCategory(posterityId);
                break;
            }
        }
        //删除子类
        adminCourseCategoryMapper.delCourseCategory(parentId);
    }

    //2.递归方法
    private void buildChildren(CourseCategoryVO parent, List<CourseCategoryVO> allCategories) {
        // 1. 找出所有子节点（从完整列表中查找）
        List<CourseCategoryVO> children = allCategories.stream()
                .filter(category ->
                        category.getParentId() != null &&  // 防止NPE
                                parent.getId().equals(category.getParentId())
                )
                .sorted(Comparator.comparingInt(CourseCategoryVO::getSort))
                .collect(Collectors.toList());

        // 2. 设置子节点（不管是否为空）
        parent.setChildren(children);

        // 3. 递归处理子节点
        for (CourseCategoryVO child : children) {
            buildChildren(child, allCategories);
        }
    }
}
