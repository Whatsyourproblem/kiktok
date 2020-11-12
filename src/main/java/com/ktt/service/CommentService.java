package com.ktt.service;

import com.ktt.common.ServerResponse;
import com.ktt.pojo.Comment;
import com.ktt.vo.UserComment;

import java.util.List;

public interface CommentService {

    // 发布评论(视频)
    ServerResponse<UserComment> submitVideoComment(String userId,String videoId,Comment comment);

    // 发布评论(动态)
    ServerResponse<UserComment> submitTrendComment(String userId,String trendId,Comment comment);

    // 获取某一视频的全部评论
    ServerResponse<List<UserComment>> getCommentsByVideoId(String userId,String videoId);

    // 获取某一动态的全部评论
    ServerResponse<List<UserComment>> getCommentsByTrendId(String trendId);

    // 获取用户自身的评论
    ServerResponse<List<Comment>> getCommentsByMyself(String userId);

    // 获取某一用户的所有评论
    ServerResponse<List<Comment>> getCommentsByUserId(String userId);

    // 用户修改自身的评论
    ServerResponse<Comment> updateCommentByUser(String userId,Comment comment);

    // 用户删除自己的评论
    ServerResponse<Comment> deleteCommentByUser(String videoId,String commentId,String userId);

    // 点赞评论
    ServerResponse<Comment> starComment(String userId,String commentId);

    // 取消点赞评论
    ServerResponse<Comment> cancelComment(String userId,String commentId);
}
