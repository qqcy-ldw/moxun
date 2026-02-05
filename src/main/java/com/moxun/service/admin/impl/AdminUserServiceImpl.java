package com.moxun.service.admin.impl;

import cn.hutool.core.lang.hash.Hash;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.moxun.Pojo.Entity.User;
import com.moxun.Pojo.Vo.PageResult;
import com.moxun.Pojo.Vo.UserListVO;
import com.moxun.exception.BusinessException;
import com.moxun.mapper.admin.AdminUserMapper;
import com.moxun.service.admin.AdminUserService;
import com.moxun.util.UserContext;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Service
public class AdminUserServiceImpl implements AdminUserService {

    @Autowired
    private AdminUserMapper adminUserMapper;

    /**
     * 分页查询用户列表
     * @param username
     * @param page
     * @param pageSize
     * @param status
     * @return
     */
    @Override
    public PageResult listUsers(String username, Integer page, Integer pageSize, Integer status) {
        PageHelper.startPage(page, pageSize);
        Page<UserListVO> userPage = adminUserMapper.listUsers(username,status);
        return new PageResult(userPage.getTotal(),userPage.getResult());
    }


    /**
     * 删除用户
     * @param id
     */
    @Override
    @Transactional
    public void deleteUser(List<Integer> id) {
        //判断是否是管理员
        List<Integer> userIds = adminUserMapper.isAdmin(id);
        if (!userIds.isEmpty()){
            throw new BusinessException("管理员账户不能被删除");
        }
        //判断要删除的名单中是否有自己
        Map<String, Object> map = UserContext.getCurrentUser();
        if (Objects.isNull(map)){
            throw new BusinessException("用户不存在");
        }
        Integer userId = Integer.valueOf(map.get("userId").toString());
        if (id.contains(userId)){
            throw new BusinessException("不能删除自己");
        }

        if (id.size() == 1){
            adminUserMapper.deleteUser(id.get(0));
        }else {
            //批量删除用户
            adminUserMapper.deleteUser(id);
        }
    }


//    public Boolean isAdmin() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        for (GrantedAuthority authority : authentication.getAuthorities()) {
//            if (authority.getAuthority().equals("ROLE_ADMIN")) {
//                return true;
//            }
//        }
//        return false;
//    }
}
