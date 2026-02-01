package com.moxun.mapper.admin;

import com.github.pagehelper.Page;
import com.moxun.Pojo.Vo.PageResult;
import com.moxun.Pojo.Vo.UserListVO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AdminUserMapper {

    Page<UserListVO> listUsers(String username, Integer status);
}
