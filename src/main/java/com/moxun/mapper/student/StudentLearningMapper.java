package com.moxun.mapper.student;

import com.moxun.Pojo.Vo.ChapterVO;
import com.moxun.Pojo.Vo.LearningProgressVO;
import com.moxun.Pojo.Vo.SectionVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 学生端 - 学习进度 Mapper
 * 提供课程目录、学习进度等数据访问操作
 */
@Mapper
public interface StudentLearningMapper {

    /**
     * 获取课程目录（章节+课时）
     * 👑 面试考点：嵌套 resultMap 实现一对多查询
     *
     * @param courseId 课程ID
     * @return 章节列表（含课时）
     */
    List<ChapterVO> getCourseCatalog(@Param("courseId") Long courseId);

    /**
     * 获取某个课时信息
     *
     * @param sectionId 课时ID
     * @return 课时信息
     */
    SectionVO getSectionById(@Param("sectionId") Long sectionId);

    /**
     * 通过课时ID获取课程ID
     * 👑 面试考点：跨表关联查询（sections -> chapters -> courses）
     *
     * @param sectionId 课时ID
     * @return 课程ID
     */
    Long getCourseIdBySectionId(@Param("sectionId") Long sectionId);

    /**
     * 获取用户的学习进度
     *
     * @param userId   用户ID
     * @param sectionId 课时ID
     * @return 学习进度
     */
    LearningProgressVO getProgress(
            @Param("userId") Long userId,
            @Param("sectionId") Long sectionId
    );

    /**
     * 插入学习进度（首次学习）
     */
    void insertProgress(
            @Param("userId") Long userId,
            @Param("courseId") Long courseId,
            @Param("sectionId") Long sectionId,
            @Param("position") Integer position,
            @Param("finished") Integer finished
    );

    /**
     * 更新学习进度（已存在记录）
     * 👑 面试考点：只更新 position 和 finished，不更新时间戳
     */
    void updateProgress(
            @Param("userId") Long userId,
            @Param("sectionId") Long sectionId,
            @Param("position") Integer position,
            @Param("finished") Integer finished
    );

    /**
     * 获取课程总课时数
     *
     * @param courseId 课程ID
     * @return 课时数
     */
    int countSections(@Param("courseId") Long courseId);

    /**
     * 获取用户已完成课时数
     *
     * @param userId   用户ID
     * @param courseId 课程ID
     * @return 已完成课时数
     */
    int countFinishedSections(
            @Param("userId") Long userId,
            @Param("courseId") Long courseId
    );

    /**
     * 获取用户最后学习的课时ID
     *
     * @param userId   用户ID
     * @param courseId 课程ID
     * @return 最后学习的课时ID
     */
    Long getLastSectionId(
            @Param("userId") Long userId,
            @Param("courseId") Long courseId
    );
}
