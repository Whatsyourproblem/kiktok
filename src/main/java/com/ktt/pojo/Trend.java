package com.ktt.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Trend {

    // 动态Id
    private String trendId;
    // 发布动态人的Id
    private String userId;
    // 目标的id
    private String relationId;
    // 视频url
    private String videoUrl;
    // 动态类型(0为转发的视频,1为别人回复的评论)
    private Integer type;
    // 动态的文字
    private String content;
    // 动态点赞量
    private Integer recommend;
    // 动态评论量
    private Integer commentNum;
    // 动态转发量
    private Integer transmission;
    // 动态创建时间
    private Date createTime;
    // 动态修改时间
    private Date updateTime;
}
