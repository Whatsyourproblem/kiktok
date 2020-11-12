package com.ktt.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Info {
    // 用户信息id
    private String userInfoId;
    // 图片地址
    private String imgUrl;
    // 昵称
    private String infoName;
    // 简介
    private String intro;
    // 邮箱
    private String email;
    // 性别
    private String gender;
    // 出生日期
    private Date birthday;
    // 地址
    private String address;
    // 粉丝数
    private Integer fansNum;
    // 关注数
    private Integer focusNum;
    // 发布视频数
    private Integer publishNum;
    // 收藏视频数
    private Integer collectNum;
    // 创建日期
    private Date createTime;
    // 修改日期
    private Date updateTime;
}
