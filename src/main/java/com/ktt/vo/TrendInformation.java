package com.ktt.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrendInformation {

    // 动态发布者id
    private String publishId;
    // 发布者名字
    private String publishName;
    // 发布者头像
    private String publishImg;
    // 发布时间
    private Date publishTime;
    // 发布内容(可能是自己发布的内容,也可能是别人对你的回复)
    private String commentContext;
    // 动态id
    private String dynamicId;
    // 动态来源人的id
    private String dynamicFromId;
    // 动态来源的name
    private String dynamicFromName;
    // 动态来源标题
    private String dynamicFromContext;
    // 视频链接
    private String videoUrl;
    // 是否点赞
    private Boolean isLike;
    // 点赞量
    private Integer recommend;
    // 评论量
    private Integer commentNum;
    // 转发量
    private Integer transmission;
    // 动态类型(0.转发类型,1.评论类型)
    private Integer type;
}
