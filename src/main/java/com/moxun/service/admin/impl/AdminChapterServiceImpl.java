package com.moxun.service.admin.impl;

import com.moxun.Pojo.Dto.ChapterSaveDTO;
import com.moxun.Pojo.Entity.Chapter;
import com.moxun.Pojo.Entity.Section;
import com.moxun.Pojo.Vo.ChapterVO;
import com.moxun.exception.BusinessException;
import com.moxun.mapper.admin.AdminChapterMapper;
import com.moxun.service.admin.AdminChapterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 管理员 - 章节管理
 */
@Slf4j
@Service
public class AdminChapterServiceImpl implements AdminChapterService {

    @Autowired
    private AdminChapterMapper adminChapterMapper;

    /**
     * 获取课程下的章节列表（含课时）
     *
     * @param courseId 课程ID
     * @return 章节列表
     */
    @Override
    public List<ChapterVO> listChaptersByCourse(Long courseId) {
        if (courseId == null){
            throw new BusinessException("课程ID不能为空");
        }
        //实现章节列表（含课时）
        ArrayList<ChapterVO> arrayList = adminChapterMapper.listChaptersByCourse(courseId);
        log.info("listChaptersByCourse: {}", arrayList);
        return arrayList;
    }

    /**
     * 根据ID获取章节信息
     *
     * @param id 章节ID
     * @return 章节信息
     */
    @Override
    public Chapter getChapterById(Long id) {
        if (id == null){
            throw new BusinessException("章节ID不能为空");
        }
        Chapter chapter = adminChapterMapper.getChapterById(id);
        return chapter;
    }

    /**
     * 新增章节
     *
     * @param dto 章节信息
     */
    @Override
    public void addChapter(ChapterSaveDTO dto) {
        Chapter chapter = new Chapter();
        BeanUtils.copyProperties(dto, chapter);
        chapter.setSort(1);
        chapter.setCreateTime(LocalDateTime.now());
        chapter.setUpdateTime(LocalDateTime.now());
        Chapter chapterById = adminChapterMapper.getChapterById(chapter.getId());
        if (chapterById != null){
            throw new BusinessException("章节已存在");
        }
        adminChapterMapper.addChapter(chapter);
    }

    /**
     * 修改章节
     *
     * @param dto 章节信息
     */
    @Override
    public void updateChapter(ChapterSaveDTO dto) {
        adminChapterMapper.updateChapter(dto);
    }

    /**
     * 删除章节
     *
     * @param id 章节ID
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteChapter(Long id) {
        // 1. 参数校验
        if (id == null) {
            throw new IllegalArgumentException("章节ID不能为空");
        }
        // 2. 判断章节是否存在
        Chapter chapterById = adminChapterMapper.getChapterById(id);
        if (Objects.isNull(chapterById)) {
             throw new BusinessException("章节不存在");
         }

        // 3. 删除课时(当前章节ID有课时就删除)
        Section section = adminChapterMapper.isSections(id);
        if (Objects.nonNull(section)){
            adminChapterMapper.deleteSections(id);
        }

        // 4. 删除章节
        int deletedChapters = adminChapterMapper.deleteChapter(id);
        if (deletedChapters == 0) {
            log.error("删除章节失败，章节ID {} 可能不存在", id);
            throw new BusinessException("删除章节失败");
        }

        log.info("章节删除成功，ID：{}", id);
    }
}
