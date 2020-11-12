package com.ktt.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    // 用户id
    private String userId;
    // 手机号
    private String phoneNum;
    // 密码
    private String passWord;
    // 用户状态
    private Integer status;
    // 盐
    private String salt;
    // 创建日期
    private Date createTime;
    // 最后一次修改日期
    private Date updateTime;
}
