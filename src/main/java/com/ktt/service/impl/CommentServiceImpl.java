package com.ktt.service.impl;

import com.ktt.common.ServerResponse;
import com.ktt.dao.CommentMapper;
import com.ktt.dao.InfoMapper;
import com.ktt.dao.TrendMapper;
import com.ktt.dao.VideoMapper;
import com.ktt.pojo.Comment;
import com.ktt.pojo.Info;
import com.ktt.service.CommentService;
import com.ktt.utils.DataFormat;
import com.ktt.utils.RandomData;
import com.ktt.vo.UserComment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service("commentService")
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private RandomData randomData;

    @Autowired
    private DataFormat dataFormat;
    
    @Autowired
    private VideoMapper videoMapper;

    @Autowired
    private TrendMapper trendMapper;
    
    @Autowired
    private InfoMapper infoMapper;

    // 发布评论(视频)
    @Override
    public ServerResponse<UserComment> submitVideoComment(String userId,String videoId,Comment comment) {
        // 先给评论随机id
        String randomId = randomData.getRandomId();
        comment.setCommentId(randomId);

        // 先将用户id和评论id存入用户评论表
        int count = commentMapper.insertUserIdAndCommentId(userId, comment.getCommentId());
        if(count <= 0){
            return ServerResponse.createByErrorMessage("发布评论失败!");
        }
        // 再将视频id和评论id存入视频评论表
        int counts = commentMapper.insertVideoIdAndCommentId(videoId, comment.getCommentId());
        if(counts <= 0){
            return ServerResponse.createByErrorMessage("发布评论失败!");
        }

        // 将评论存入评论表
        Date createTime = dataFormat.formatDate(new Date());
        // 设置点赞量
        comment.setRecommend(0);
        // 设置创建时间
        comment.setCreateTime(createTime);
        // 设置修改时间
        comment.setUpdateTime(createTime);
        int result = commentMapper.insertComment(comment);
        if(result <= 0){
            return ServerResponse.createByErrorMessage("发布评论失败");
        }

        // 将视频评论量加一
        int videoCommentNum = videoMapper.addVideoCommentNum(videoId);
        if(videoCommentNum <= 0){
            return ServerResponse.createByErrorMessage("发布评论失败");
        }

        // 查询出发布者的信息
        String userInfoId = infoMapper.selectUserInfoIdByUserId(userId);
        Info info = infoMapper.selectInfoByUserInfoId(userInfoId);

        // 封装vo
        UserComment userComment = new UserComment();
        userComment.setUserId(userId);
        userComment.setCommentId(comment.getCommentId());
        userComment.setCommentUserName(info.getInfoName());
        userComment.setCommentContent(comment.getContent());
        userComment.setCommentUserImg(info.getUserInfoId());
        userComment.setCommentUserImg(info.getImgUrl());
        userComment.setCommentRecommend(comment.getRecommend());
        userComment.setCommentTime(dataFormat.formatOtherSimpleDate(comment.getCreateTime()));

        return ServerResponse.createBySuccess("评论发布成功!",userComment);
    }

    // 发布评论(动态)
    @Override
    public ServerResponse<UserComment> submitTrendComment(String userId, String trendId, Comment comment) {

        // 先给评论随机id
        String randomId = randomData.getRandomId();
        comment.setCommentId(randomId);

        // 先将用户id和评论id存入用户评论表
        int count = commentMapper.insertUserIdAndCommentId(userId, comment.getCommentId());
        if(count <= 0){
            return ServerResponse.createByErrorMessage("发布评论失败!");
        }
        // 再将动态id和评论id存入动态评论表
        int counts = commentMapper.insertTrendIdAndCommentId(trendId,comment.getCommentId());
        if(counts <= 0){
            return ServerResponse.createByErrorMessage("发布评论失败!");
        }

        // 将评论存入评论表
        Date createTime = dataFormat.formatDate(new Date());
        // 设置点赞量
        comment.setRecommend(0);
        // 设置创建时间
        comment.setCreateTime(createTime);
        // 设置修改时间
        comment.setUpdateTime(createTime);
        int result = commentMapper.insertComment(comment);
        if(result <= 0){
            return ServerResponse.createByErrorMessage("发布评论失败");
        }

        // 将动态评论量加一
        int trendCommentNum = trendMapper.addTrendCommentNum(trendId);
        if(trendCommentNum <= 0){
            return ServerResponse.createByErrorMessage("发布评论失败");
        }

        // 查询出发布者的信息
        String userInfoId = infoMapper.selectUserInfoIdByUserId(userId);
        Info info = infoMapper.selectInfoByUserInfoId(userInfoId);

        // 封装vo
        UserComment userComment = new UserComment();
        userComment.setUserId(userId);
        userComment.setCommentId(comment.getCommentId());
        userComment.setCommentUserName(info.getInfoName());
        userComment.setCommentContent(comment.getContent());
        userComment.setCommentUserImg(info.getUserInfoId());
        userComment.setCommentUserImg(info.getImgUrl());
        userComment.setCommentRecommend(comment.getRecommend());
        userComment.setCommentTime(dataFormat.formatOtherSimpleDate(comment.getCreateTime()));

        return ServerResponse.createBySuccess("评论发布成功!",userComment);
    }

    // 获取某一视频下的所有评论
    @Override
    public ServerResponse<List<UserComment>> getCommentsByVideoId(String userId,String videoId) {

        List<UserComment> userComments = new ArrayList<UserComment>();


        // 查询某一视频下的所有评论
        List<Comment> comments = commentMapper.getAllCommentsByVideoId(videoId);
        if(comments.size() == 0 || comments == null){
            ServerResponse.createBySuccess("查询成功!",userComments);
        }

        // 查询某一视频下的用户id
        List<String> userIds = commentMapper.selectUserIdByVideoId(videoId);
        if(userIds.size() == 0){
            ServerResponse.createByErrorMessage("发生了预期之外的错误!");
        }

        // 根据user_id查询user_info_id
        List<String> userInfoIds = new ArrayList<>();
        for (String otherUserId : userIds) {
            String userInfoId = commentMapper.selectUserInfoIdByUserId(otherUserId);
            userInfoIds.add(userInfoId);
        }

        for (String userInfo : userInfoIds) {
            System.out.println(userInfo);
        }
        
        // 根据user_info_id 查询 info_name
        List<String> userInfoNames = new ArrayList<>();
        List<String> userInfoImgs = new ArrayList<>();
        for (String userInfoId : userInfoIds) {
            String userInfoName = commentMapper.selectInfoNameByUserInfoId(userInfoId);
            String userInfoImg = commentMapper.selectInfoImgByUserInfoId(userInfoId);
            userInfoNames.add(userInfoName);
            userInfoImgs.add(userInfoImg);
        }


        // 组合vo,返回给前台(UserComment)

        for(int i = 0;i < comments.size();i++){

            UserComment userComment = new UserComment();
            userComment.setUserId(userIds.get(i));
            userComment.setCommentId(comments.get(i).getCommentId());
            userComment.setCommentUserName(userInfoNames.get(i));
            userComment.setCommentContent(comments.get(i).getContent());
            userComment.setCommentUserImg(userInfoImgs.get(i));
            userComment.setCommentRecommend(comments.get(i).getRecommend());
            int flag = commentMapper.checkIsStar(userId, comments.get(i).getCommentId());
            if(flag != 0){
                userComment.setCommentIsLike(true);
            }else{
                userComment.setCommentIsLike(false);
            }
            userComment.setCommentTime(dataFormat.formatOtherSimpleDate(comments.get(i).getCreateTime()));


            userComments.add(userComment);
        }
        

        return ServerResponse.createBySuccess("查询成功!",userComments);
    }

    // 获取某一动态下的所有评论
    @Override
    public ServerResponse<List<UserComment>> getCommentsByTrendId(String trendId) {

        List<UserComment> userComments = new ArrayList<UserComment>();


        // 查询某一动态下的所有评论
        List<String> commentIds = commentMapper.selectCommentIdByTrendId(trendId);
        if(commentIds.size() == 0 || commentIds == null){
            return ServerResponse.createBySuccess("查询成功!",userComments);
        }
        
        List<Comment> comments = new ArrayList<>();
        for(int i = 0;i < commentIds.size();i++){
            Comment comment = commentMapper.selectCommentByCommentId(commentIds.get(i));
            comments.add(comment);
        }

        // 查询某一动态下的用户id
        List<String> userIds = new ArrayList<>();
        for(int i = 0;i < commentIds.size();i++){
            String userId = commentMapper.selectUserIdByCommentId(commentIds.get(i));
            userIds.add(userId);
        }

        // 根据user_id查询user_info_id
        List<String> userInfoIds = new ArrayList<>();
        for (String userId : userIds) {
            String userInfoId = commentMapper.selectUserInfoIdByUserId(userId);
            userInfoIds.add(userInfoId);
        }

        // 根据user_info_id 查询 info_name
        List<String> userInfoNames = new ArrayList<>();
        List<String> userInfoImgs = new ArrayList<>();
        for (String userInfoId : userInfoIds) {
            String userInfoName = commentMapper.selectInfoNameByUserInfoId(userInfoId);
            String userInfoImg = commentMapper.selectInfoImgByUserInfoId(userInfoId);
            userInfoNames.add(userInfoName);
            userInfoImgs.add(userInfoImg);
        }


        // 组合vo,返回给前台(UserComment)

        for(int i = 0;i < comments.size();i++){

            UserComment userComment = new UserComment();
            userComment.setUserId(userIds.get(i));
            userComment.setCommentId(comments.get(i).getCommentId());
            userComment.setCommentUserName(userInfoNames.get(i));
            userComment.setCommentContent(comments.get(i).getContent());
            userComment.setCommentUserImg(userInfoImgs.get(i));
            userComment.setCommentRecommend(comments.get(i).getRecommend());
            int flag = commentMapper.checkIsStar(userIds.get(i),comments.get(i).getCommentId());
            if(flag != 0){
                userComment.setCommentIsLike(true);
            }else{
                userComment.setCommentIsLike(false);
            }
            userComment.setCommentTime(dataFormat.formatOtherSimpleDate(comments.get(i).getCreateTime()));
            userComments.add(userComment);
        }

        return ServerResponse.createBySuccess("查询成功!",userComments);
    }


    // 获取自身评论
    @Override
    public ServerResponse<List<Comment>> getCommentsByMyself(String userId) {
        List<Comment> comments = commentMapper.getAllCommentsByUserId(userId);
        if(comments.size() == 0){
            ServerResponse.createByErrorMessage("发生了预期之外的错误!");
        }
        return ServerResponse.createBySuccess("查询成功!",comments);
    }

    // 获取某一用户下的所有评论
    @Override
    public ServerResponse<List<Comment>> getCommentsByUserId(String userId) {
        List<Comment> comments = commentMapper.getAllCommentsByUserId(userId);
        if(comments.size() == 0){
            ServerResponse.createBySuccess("查询成功!",comments);
        }

        // 格式化评论的发布时间
        List<Date> list = new ArrayList<>();
        /*for (Comment comment : comments) {
            list.add();
        }*/
        return ServerResponse.createBySuccess("查询成功!",comments);
    }

    // 用户修改自己的评论
    @Override
    public ServerResponse<Comment> updateCommentByUser(String userId,Comment comment) {

        // todo 这里需要加用户认证 判断这个评论是不是用户自己的,不然无法修改
        // 暂时使用传入 评论id和用户id 判断是否存在关系
        int count = commentMapper.checkCommentBelongToUser(userId,comment.getCommentId());
        if(count <= 0){
            // 不是自己发布的,无法修改
            return ServerResponse.createByErrorMessage("无法修改别人的评论!");
        }
        // 设置时间
        Date updateTime = dataFormat.formatDate(new Date());
        comment.setUpdateTime(updateTime);

        // 修改评论
        int result = commentMapper.updateCommentByUser(comment);
        if(result <= 0){
            return ServerResponse.createByErrorMessage("修改自己评论失败!");
        }

        Comment newComment = commentMapper.selectCommentByCommentId(comment.getCommentId());
        if(newComment == null){
            return ServerResponse.createByErrorMessage("修改自己的评论失败!");
        }

        return ServerResponse.createBySuccess("修改自己评论成功!",newComment);
    }

    // 用户删除自身的评论
    @Override
    public ServerResponse<Comment> deleteCommentByUser(String videoId,String commentId, String userId) {

        // todo 这里需要加用户认证 判断这个评论是不是用户自己的,不然无法删除
        // 暂时使用传入 评论id和用户id 判断是否存在关系
        int count = commentMapper.checkCommentBelongToUser(userId,commentId);
        if(count <= 0){
            // 不是自己发布的,无法修改
            return ServerResponse.createByErrorMessage("无法删除别人的评论!");
        }

        // 先删除用户和评论的关系
        int result = commentMapper.deleteUserIdAndCommentId(userId, commentId);
        if(result <= 0){
            return ServerResponse.createByErrorMessage("删除失败!");
        }

        // 再删除视频和评论的关系
        int results = commentMapper.deleteVideoIdAndCommentId(videoId, commentId);
        if(result <= 0){
            return ServerResponse.createByErrorMessage("删除失败!");
        }
        // 删除评论
        int res = commentMapper.deleteCommentByCommentId(commentId);
        if(res <= 0){
            return ServerResponse.createByErrorMessage("删除失败!");
        }

        // 视频评论量减一
        int videoCommentNum = videoMapper.minusVideoCommentNum(videoId);
        if(videoCommentNum <= 0){
            return ServerResponse.createByErrorMessage("删除失败!");
        }

        return ServerResponse.createBySuccessMessage("删除评论成功!");
    }

    // 评论点赞
    @Override
    public ServerResponse<Comment> starComment(String userId, String commentId) {

        // 没有点赞关系,添加关系,该评论的点赞量加一
        int commentStar = commentMapper.insertUserCommentStar(userId, commentId);
        if(commentStar <= 0){
            return ServerResponse.createByErrorMessage("点赞失败!");
        }
        int commentRecommend = commentMapper.addCommentRecommend(commentId);
        if(commentRecommend <= 0){
            return ServerResponse.createByErrorMessage("点赞失败!");
        }

        return ServerResponse.createBySuccessMessage("点赞成功!");
    }

    @Override
    public ServerResponse<Comment> cancelComment(String userId, String commentId) {
        // 已经有点赞关系,删除关系,该评论的点赞量减一
        int commentStar = commentMapper.deleteUserCommentStar(userId, commentId);
        if(commentStar <= 0){
            return ServerResponse.createByErrorMessage("取消点赞失败!");
        }
        int commentRecommend = commentMapper.minusCommentRecommend(commentId);
        if(commentRecommend <= 0){
            return ServerResponse.createByErrorMessage("取消点赞失败!");
        }

        return ServerResponse.createBySuccessMessage("取消点赞成功!");
    }

}
