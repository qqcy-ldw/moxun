package com.moxun.service.admin.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.moxun.Pojo.Entity.User;
import com.moxun.Pojo.Vo.PageResult;
import com.moxun.Pojo.Vo.UserListVO;
import com.moxun.mapper.admin.AdminUserMapper;
import com.moxun.service.admin.AdminUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminUserServiceImpl implements AdminUserService {

    @Autowired
    private AdminUserMapper adminUserMapper;

    @Override
    public PageResult listUsers(String username, Integer page, Integer pageSize, Integer status) {
        PageHelper.startPage(page, pageSize);
        Page<UserListVO> userPage = adminUserMapper.listUsers(username,status);
        return new PageResult(userPage.getTotal(),userPage.getResult());
    }
}
