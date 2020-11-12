package com.ktt.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Comment {

    // 评论id
    private String commentId;
    // 评论内容
    private String content;
    // 评论点赞量
    private Integer recommend;
    // 创建时间
    private Date createTime;
    // 修改时间
    private Date updateTime;

}
