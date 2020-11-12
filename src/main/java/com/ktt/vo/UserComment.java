package com.ktt.vo;

import com.ktt.pojo.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

// vo 增强pojo

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserComment{

    private String userId;
    private String commentId;
    private String commentUserName;
    private String commentContent;
    private String commentUserImg;
    private Integer commentRecommend;
    private Boolean commentIsLike;
    private String commentTime;
}
