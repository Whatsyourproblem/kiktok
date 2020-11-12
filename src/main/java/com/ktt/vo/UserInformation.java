package com.ktt.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

// vo
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInformation {

    // 用户名
    private String userId;
    // 用户账号
    private String userInfoId;
    // 用户头像
    private String userInfoImg;
    // 用户昵称
    private String userInfoName;
    // 用户简介
    private String userInfoIntro;
    // 用户生日
    private String userInfoBirth;
    // 用户地址
    private String userInfoAddress;
    // 用户性别
    private String userInfoGender;
    // 用户邮箱
    private String userInfoEmail;
    // 用户获赞量(所有视频和动态和评论的点赞量总和)
    private Integer userInfoStar;
    // 用户关注量
    private Integer userInfoFocus;
    // 用户粉丝量
    private Integer userInfoFans;
    // 用户发布作品量
    private Integer userInfoPublish;
    // 用户收藏视频量
    private Integer userInfoCollects;

}
