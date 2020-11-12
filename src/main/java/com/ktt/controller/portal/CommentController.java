package com.ktt.controller.portal;

import com.ktt.common.Const;
import com.ktt.common.ResponseCode;
import com.ktt.common.ServerResponse;
import com.ktt.pojo.Comment;
import com.ktt.pojo.User;
import com.ktt.service.CommentService;
import com.ktt.vo.UserComment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    // 用户发布评论(视频)
    @PostMapping("/submit_video_comment_user/{videoId}/{userId}")
    public ServerResponse<UserComment> submitComment(HttpSession session,@PathVariable String videoId,@RequestBody Comment comment,@PathVariable String userId){
        return commentService.submitVideoComment(userId,videoId,comment);
    }

    // 用户发布评论(动态)
    @PostMapping("/submit_trend_comment_user/{trendId}/{userId}")
    public ServerResponse<UserComment> submitComment(@PathVariable String trendId,@PathVariable String userId,@RequestBody Comment comment){
        return commentService.submitTrendComment(userId,trendId,comment);
    }

    // 获取某一视频下的所有评论
    @PostMapping("/get_comments_video/{userId}/{videoId}")
    public ServerResponse<List<UserComment>> getCommentsByVideoId(@PathVariable String userId,@PathVariable String videoId){
        return commentService.getCommentsByVideoId(userId,videoId);
    }

    // 获取某一动态下的所有评论
    @PostMapping("/get_comments_trend/{trendId}")
    public ServerResponse<List<UserComment>> getCommentsByTrendId(@PathVariable String trendId){
        return commentService.getCommentsByTrendId(trendId);
    }

    // 获取自己的评论
    /*@PostMapping("/get_comments_user/{userId}")
    ServerResponse<List<Comment>> getCommentsByMyself(@PathVariable String userId){
       return commentService.getCommentsByMyself(userId);
    }*/

    // 获取某一用户(非自身)的所有评论
    @PostMapping("/get_comments_user/{userId}")
    public ServerResponse<List<Comment>> getCommentsByUserId(@PathVariable String userId){
        return commentService.getCommentsByUserId(userId);
    }

    // 用户修改自己的评论
    @PostMapping("/update_comment_user")
    public ServerResponse<Comment> updateCommentByUserId(@RequestBody Comment comment,HttpSession session){

        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByError(ResponseCode.NEED_LOGIN.getCode(),"用户未登录,请登录!");
        }

        return commentService.updateCommentByUser(user.getUserId(),comment);
    }

    // 用户删除自己的评论
    @PostMapping("/delete_comment_user/{videoId}/{commentId}")
    public ServerResponse<Comment> deleteCommentByUserId(@PathVariable String videoId,@PathVariable String commentId,HttpSession session){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByError(ResponseCode.NEED_LOGIN.getCode(),"用户未登录,请登录!");
        }
        return commentService.deleteCommentByUser(videoId,commentId,user.getUserId());
    }

    // 用户点赞评论
    @PostMapping("/recommend_comment_user/{userId}/{commentId}")
    public ServerResponse<Comment> starComment(@PathVariable String userId,@PathVariable String commentId){
        return commentService.starComment(userId,commentId);
    }

    // 用户取消点赞评论
    @PostMapping("/cancel_comment_user/{userId}/{commentId}")
    public ServerResponse<Comment> cancelComment(@PathVariable String userId,@PathVariable String commentId){
        return commentService.cancelComment(userId,commentId);
    }
}
