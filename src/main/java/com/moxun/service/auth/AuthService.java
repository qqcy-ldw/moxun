package com.moxun.service.auth;


import com.moxun.Pojo.Dto.LoginDTO;
import com.moxun.Pojo.Dto.UserUpdateDTO;
import com.moxun.Pojo.Entity.User;
import com.moxun.Pojo.Vo.LoginResultVO;
import com.moxun.Pojo.Vo.UserProfileVO;

public interface AuthService {

    /**
     * 根据用户名查询所有信息
     * @param username
     * @return
     */
    LoginResultVO CommonLogin(String username, String password, String ipAddress);


    /**
     * 根据用户名查询用户信息
     * @param username
     * @return
     */
    User getByUserName(String username);

    /**
     * 注册用户
     * @param loginDTO
     */
    void CommonRegister(LoginDTO loginDTO);

    /**
     * 根据用户id查询所有信息
     * @param id
     * @return
     */
    UserProfileVO getUserInfo(Integer id);

    /**
     * 修改用户信息
     * @param userUpdateDTO
     */
    void modifyUpdateUser(UserUpdateDTO userUpdateDTO);

    /**
     * 上传用户头像到数据库
     * @param upload
     */
    void uploadUserAvatar(String upload);
}
