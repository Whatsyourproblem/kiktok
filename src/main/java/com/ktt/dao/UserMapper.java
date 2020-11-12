package com.ktt.dao;

import com.ktt.pojo.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository //这个注解没有必要添加,只是为了不报红
public interface UserMapper {

    // 验证手机号存在
    @Select("select count(1) from user where phoneNum = #{phoneNum}")
    int checkUserPhoneNum(String phoneNum);


    // 插入user
    @Insert("insert into user (user_id,phonenum,password,status,salt,createtime,updatetime)" +
            "values (#{userId},#{phoneNum},#{passWord},#{status},#{salt},#{createTime},#{updateTime})")
    int insertUser(User user);

    // 通过手机号(唯一标识)来查询盐
    @Select("select salt from user where phonenum = #{phoneNum}")
    String selectSaltByPhoneNum(String phoneNum);

    // 验证登录
    @Select("select * from user where phonenum = #{phoneNum} and password = #{passWord}")
    User selectLoginUser(@Param("phoneNum") String phoneNum,@Param("passWord") String passWord);

    // 修改密码
    @Update("update user set password = #{passWord},updatetime = #{updateTime} where phonenum = #{phoneNum}")
    int updatePassword(User user);

    // 通过手机号查出user
    @Select("select * from user where phoneNum = #{phoneNum}")
    User selectUserByPhoneNum(String phoneNum);
}
