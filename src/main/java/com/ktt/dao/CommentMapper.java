package com.ktt.dao;

import com.ktt.pojo.Comment;
import com.ktt.pojo.Info;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentMapper {



    // 将用户Id和评论Id放入到用户评论信息表中
    @Insert("insert into user_comment (user_id,comment_id) values (#{userId},#{commentId})")
    int insertUserIdAndCommentId(@Param("userId") String userId,@Param("commentId") String commentId);

    // 将视频Id和评论Id放入到视频评论表中
    @Insert("insert into video_comment (video_id,comment_id) values (#{videoId},#{commentId})")
    int insertVideoIdAndCommentId(@Param("videoId") String videoId,@Param("commentId") String commentId);

    // 根据动态Id查询出评论Id
    @Select("select comment_id from trend_comment where trend_id = #{trendId}")
    List<String> selectCommentIdByTrendId(String trendId);

    // 插入动态评论
    @Insert("insert into trend_comment (trend_id,comment_id) values (#{trendId},#{commentId})")
    int insertTrendIdAndCommentId(@Param("trendId") String trendId,@Param("commentId") String commentId);

    // 插入评论
    @Insert("insert into comment (comment_id,content,recommend,createtime,updatetime) values (#{commentId},#{content},#{recommend},#{createTime},#{updateTime})")
    int insertComment(Comment comment);

    // 通过视频的id查询所有评论
    @Select("select * from comment where comment_id in (select comment_id from video_comment where video_id = #{videoId})")
    List<Comment> getAllCommentsByVideoId(String videoId);

    // 通过评论id来查询用户id
    @Select("select user_id from user_comment where comment_id = #{commentId}")
    String selectUserIdByCommentId(String commentId);

    // 通过用户id来查询评论
    @Select("select * from comment where comment_id in (select comment_id from user_comment where user_id = #{userId})")
    List<Comment> getAllCommentsByUserId(String userId);

    // 判断这条评论是不是由该用户发出的
    @Select("select count(1) from user_comment where user_id = #{userId} and comment_id = #{commentId}")
    int checkCommentBelongToUser(@Param("userId") String userId,@Param("commentId") String commentId);

    // 用户修改自己的评论
    @Update("update comment set content = #{content},updatetime = #{updateTime} where comment_id = #{commentId}")
    int updateCommentByUser(Comment comment);

    // 删除用户和评论的关系
    @Delete("delete from user_comment where user_id = #{userId} and comment_id = #{commentId}")
    int deleteUserIdAndCommentId(@Param("userId") String userId,@Param("commentId") String commentId);

    // 删除视频和评论的关系
    @Delete("delete from video_comment where video_id = #{videoId} and comment_id = #{commentId}")
    int deleteVideoIdAndCommentId(@Param("videoId") String videoId,@Param("commentId") String commentId);

    // 删除自己的评论
    @Delete("delete from comment where comment_id = #{commentId}")
    int deleteCommentByCommentId(String commentId);

    // 根据commentId查询评论
    @Select("select * from comment where comment_id = #{commentId}")
    Comment selectCommentByCommentId(String commentId);

    // 根据视频id来查询用户id
    @Select("select user_id from user_comment where comment_id in (select comment_id from video_comment where video_id = #{videoId})")
    List<String> selectUserIdByVideoId(String videoId);


    // 根据user_id 查询user_info_id
    @Select("select user_info_id from user_info where user_id = #{userId}")
    String selectUserInfoIdByUserId(String userId);

    // 根据user_info_id 查询 info_name
    @Select("select info_name from info where user_info_id = #{userInfoId}")
    String selectInfoNameByUserInfoId(String userInfoId);

    // 根据user_info_id 查询 info_name
    @Select("select img_url from info where user_info_id = #{userInfoId}")
    String selectInfoImgByUserInfoId(String userInfoId);

    // 根据视频id来查询用户昵称
    @Select("select * from info where user_info_id in " +
            "(select user_info_id from user_info where user_id in " +
            "(select user_id from user_comment where comment_id in " +
            "(select comment_id from video_comment where video_id = #{videoId})))")
    List<Info> selectInfoNameByVideoId(String videoId);

    // 判断点赞关系
    @Select("select count(user_id) from user_comment_star where user_id = #{userId} and comment_id = #{commentId}")
    int checkIsStar(@Param("userId") String userId,@Param("commentId") String commentId);

    // 删除点赞关系
    @Delete("delete from user_comment_star where user_id = #{userId} and comment_id = #{commentId}")
    int deleteUserCommentStar(@Param("userId") String userId,@Param("commentId") String commentId);

    // 插入点赞关系
    @Insert("insert into user_comment_star (user_id,comment_id) values (#{userId},#{commentId})")
    int insertUserCommentStar(@Param("userId") String userId,@Param("commentId") String commentId);

    // 增加评论点赞数
    @Update("update comment set recommend = recommend + 1 where comment_id = #{commentId}")
    int addCommentRecommend(String commentId);

    // 减少评论点赞数
    @Update("update comment set recommend = recommend - 1 where comment_id = #{commentId}")
    int minusCommentRecommend(String commentId);

    // 根据userId查询出commentId
    @Select("select comment_id from user_comment where user_id = #{userId}")
    List<String> selectCommentIdByUserId(String userId);
}
