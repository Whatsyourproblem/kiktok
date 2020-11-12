package com.ktt.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// vo
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VideoInformation {

    // 视频id
    private String videoId;
    // 视频标题
    private String title;
    // 视频是否点赞
    private Boolean isLike;
    // 视频发布者头像
    private String publishImg;
    // 视频发布者Id
    private String publishId;
    // 视频发布者名字
    private String publishName;
    // 视频点赞量(收藏量)
    private Integer recommend;
    // 视频评论量
    private Integer commentNum;
    // 视频转发量
    private Integer transmission;
    // 视频地址
    private String videoUrl;
}
