package com.ktt.dao;

import com.ktt.pojo.Trend;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface TrendMapper {

    // 添加正向(发布者,被转发者)动态
    @Insert("insert into trend (trend_id,user_id,relation_id,video_url,type,content,recommend,commentnum,createtime,updatetime)" +
            "values (#{trendId},#{userId},#{relationId},#{videoUrl},#{type},#{content},#{recommend},#{commentNum},#{createTime},#{updateTime})")
    int insertTrend(Trend trend);

    // 反向插入(被转发者,发布者)动态
    @Insert("insert into trend (trend_id,user_id,relation_id,video_url,type,content,recommend,commentnum,createtime,updatetime)" +
            "values (#{trendId},#{relationId},#{userId},#{videoUrl},#{type},#{content},#{recommend},#{commentNum},#{createTime},#{updateTime})")
    int insertReverseTrend(Trend trend);

    // 查询所有关于user_id的动态
    @Select("select * from trend where user_id = #{userId} or relation_id = #{userId}")
    List<Trend> selectTrendByUserIdOrRelationId(String userId);

    // 查询所有userId发布的动态
    @Select("select * from trend where user_id = #{userId}")
    List<Trend> selectTrendByUserId(String userId);

    // 判断是否点赞
    @Select("select count(1) from user_trend_star where user_id = #{userId} and trend_id = #{trendId}")
    int checkIsLike(@Param("userId") String userId,@Param("trendId") String trendId);

    // 新增点赞关系
    @Insert("insert into user_trend_star (user_id,trend_id) values (#{userId},#{trendId})")
    int insertStarTrend(@Param("userId") String userId,@Param("trendId") String trendId);

    // 删除点赞关系
    @Delete("delete from user_trend_star where user_id = #{userId} and trend_id = #{trendId}")
    int deleteStarTrend(@Param("userId") String userId,@Param("trendId") String trendId);

    // 动态点赞量加一
    @Update("update trend set recommend = recommend + 1 where trend_id = #{trendId}")
    int addTrendRecommend(String trendId);

    // 动态点赞量减一
    @Update("update trend set recommend = recommend - 1 where trend_id = #{trendId}")
    int minusTrendRecommend(String trendId);

    // 动态评论量加一
    @Update("update trend set commentnum = commentnum + 1 where trend_id = #{trendId}")
    int addTrendCommentNum(String trendId);

    // 动态评论量减一
    @Update("update trend set commentnum = commentnum - 1 where trend_id = #{trendId}")
    int minusTrendCommentNum(String trendId);

}
