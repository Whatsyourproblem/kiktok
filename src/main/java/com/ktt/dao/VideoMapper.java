package com.ktt.dao;

import com.ktt.pojo.Video;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VideoMapper {

    // 根据videoId查询video
    @Select("select * from video where video_id = #{videoId}")
    Video selectVideoByVideoId(String videoId);

    // 向用户上传视频表里插入数据
    @Insert("insert into user_publish (user_id,video_id) values (#{userId},#{videoId})")
    int insertUserIdAndVideoId(@Param("userId") String userId,@Param("videoId") String videoId);

    @Insert("insert into video (video_id,title,type,video_url,recommend,transmission,commentnum,createtime,updatetime) values" +
            " (#{videoId},#{title},#{type},#{videoUrl},#{recommend},#{transmission},#{commentNum},#{createTime},#{updateTime})")
    int insertVideo(Video video);

    // 通过userId来获取视频
    @Select("select * from video where video_id in " +
            "(select video_id from user_publish where user_id = #{userId})")
    List<Video> getVideosByUserId(String userId);

    // 通过videoUrl来获取视频
    @Select("select * from video where video_url = #{videoUrl}")
    Video selectVideoByVideoUrl(String videoUrl);

    // 让视频的转发量+1
    @Update("update video set transmission = transmission + 1 where video_url = #{videoUrl}")
    int addVideoTransmission(String videoUrl);

    // 通过userId获取用户发布的videoId
    @Select("select video_id from user_publish where user_id = #{userId}")
    List<String> selectPublishVideoIdByUserId(String userId);

    // 通过userId获取用户收藏的videoId
    @Select("select video_id from user_collect where user_id = #{userId}")
    List<String> selectCollectVideoIdByUserId(String userId);

    @Select("select * from video")
    List<Video> getAllVideos();

    // 判断点赞关系
    @Select("select count(user_id) from user_collect where user_id = #{userId} and video_id = #{videoId}")
    int checkIsLike(@Param("userId") String userId,@Param("videoId") String videoId);

    // 删除点赞关系
    @Delete("delete from user_collect where user_id = #{userId} and video_id = #{videoId}")
    int deleteUserCollect(@Param("userId") String userId,@Param("videoId") String videoId);

    // 添加点赞关系
    @Insert("insert into user_collect (user_id,video_id) values (#{userId},#{videoId})")
    int insertUserCollect(@Param("userId") String userId,@Param("videoId") String videoId);

    // 让视频点赞量减一
    @Update("update video set recommend = recommend - 1 where video_id = #{videoId}")
    int minusVideoRecommend(String videoId);

    // 让视频点赞量加一
    @Update("update video set recommend = recommend + 1 where video_id = #{videoId}")
    int addVideoRecommend(String videoId);

    // 根据videoId查询出userId
    @Select("select user_id from user_publish where video_id = #{videoId}")
    String selectUserIdByVideoId(String videoId);

    // 让视频评论量减一
    @Update("update video set commentnum = commentnum - 1 where video_id = #{videoId}")
    int minusVideoCommentNum(String videoId);

    // 让视频评论量加一
    @Update("update video set commentnum = commentnum + 1 where video_id = #{videoId}")
    int addVideoCommentNum(String videoId);

    // 根据关键字模糊查询
    @Select("select * from video where title like #{keyWord}")
    List<Video> selectVideoByKeyWord(String keyWord);

    // 查询所有视频并按热点信息降序
    @Select("select * from video order by recommend desc limit 10")
    List<Video> selectHotVideo();

    @Select("select * from video order by RAND() limit 10")
    List<Video> selectRandomVideo();
}
