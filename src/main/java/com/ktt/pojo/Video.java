package com.ktt.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Video {
    // 视频id
    private String videoId;
    // 视频标题
    private String title;
    // 视频类别名称
    private String type;
    // 视频链接地址
    private String videoUrl;
    // 视频点赞量
    private Integer recommend;
    // 视频转发量
    private Integer transmission;
    // 视频的评论量
    private Integer commentNum;
    // 视频创建日期
    private Date createTime;
    // 修改日期
    private Date updateTime;
}
