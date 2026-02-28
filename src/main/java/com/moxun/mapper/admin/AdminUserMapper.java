package com.moxun.mapper.admin;

import com.github.pagehelper.Page;
import com.moxun.Pojo.Entity.User;
import com.moxun.Pojo.Vo.PageResult;
import com.moxun.Pojo.Vo.UserListVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AdminUserMapper {

    Page<UserListVO> listUsers(String username, Integer status);


    List<Integer> isAdmin(List<Integer> id);

    @Delete("delete from users where id = #{id}")
    void deleteUser(Integer integer);

    void deleteUsers(List<Integer> integer);

    @Delete("delete from sys_user_role where user_id = #{id}")
    void deleteRole(Integer integer);

    void deleteRoles(List<Integer> id);
}
