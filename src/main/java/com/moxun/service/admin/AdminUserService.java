package com.moxun.service.admin;

import com.moxun.Pojo.Entity.User;
import com.moxun.Pojo.Vo.PageResult;

import java.util.List;

public interface AdminUserService {
    PageResult listUsers(String username, Integer page, Integer pageSize, Integer status);

    void deleteUser(List<Integer> id);
}
