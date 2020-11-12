package com.ktt.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FFInformation {

    // 用户id
    private String userId;
    // 用户头像地址
    private String userImg;
    // 用户名字
    private String userName;
    // 简介
    private String userIntro;
}
