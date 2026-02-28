package com.moxun.service.teacher;

import com.moxun.Pojo.Vo.PageResult;
import com.moxun.Pojo.Vo.UserListVO;

/**
 * 教师端 - 学生服务接口
 * 查看选了自己课程的学生
 */
public interface TeacherStudentService {

    PageResult listMyCourseStudents(Long courseId, String username, Integer page, Integer pageSize);
}
