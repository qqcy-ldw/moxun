package com.moxun.mapper.auth;


import com.moxun.Pojo.Entity.User;
import com.moxun.Pojo.Vo.UserProfileVO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface AuthMapper {

    @Select("select * from users where username = #{username}")
    User CommonLogin(String username);

    @Insert("insert into users(username,password,email,phone,status,gender,avatar,create_time,update_time) " +
            "values(#{username},#{password},#{email},#{phone},#{status},#{gender},#{avatar},#{createTime},#{updateTime})")
    void CommonRegister(User user);

    @Update("update users set status = #{status},password_expire_time = #{passwordExpireTime} where username = #{username}")
    void setStatus(User loginUser);

    @Update("update users set update_time = #{updateTime}, last_login_ip =#{lastLoginIp} where username = #{username}")
    void modifyUpdateTime(User loginUser);

    @Select("select id,username,email,phone,gender,avatar,status,last_login_ip,last_login_time,create_time from users where id = #{id}")
    UserProfileVO getUserInfo(Integer id);

    void modifyUpdateUser(User user);

    @Update("update users set avatar = #{upload} where username = #{username}")
    void uploadUserAvatar(String upload, String username);
}
