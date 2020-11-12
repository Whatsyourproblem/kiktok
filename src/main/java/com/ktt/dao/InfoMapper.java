package com.ktt.dao;

import com.ktt.pojo.Info;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InfoMapper {

    // 注册时上传默认信息(注册时)
    @Insert("insert into user_info (user_id,user_info_id) values (#{userId},#{userInfoId})")
    int insertUserIdAndUserInfoId(@Param("userId") String userId,@Param("userInfoId") String userInfoId);

    // 插入info
    @Insert("insert into info (user_info_id,img_url,info_name,intro,email,gender,birthday,address,fans_num,focus_num,publish_num,collect_num,createtime,updatetime) values (#{userInfoId},#{imgUrl},#{infoName},#{intro},#{email},#{gender},#{birthday},#{address},#{fansNum},#{focusNum},#{publishNum},#{collectNum},#{createTime},#{updateTime})")
    int insertUserInfo(Info info);

    // 根据userId 查询 userInfoId
    @Select("select user_info_id from user_info where user_id = #{userId}")
    String selectUserInfoIdByUserId(String userId);

    // 根据userInfoId 查询 Info
    @Select("select * from info where user_info_id = #{userInfoId}")
    Info selectInfoByUserInfoId(String userInfoId);

    // 修改头像地址并修改更新日期
    @Update("update info set img_url = #{imgUrl},updatetime = #{updateTime} where user_info_id = #{userInfoId}")
    int updateHeadImg(Info info);

    // 修改名称并同步时间
    @Update("update info set info_name = #{infoName},updatetime = #{updateTime} where user_info_id = #{userInfoId}")
    int updateName(Info info);

    // 修改简介并同步时间
    @Update("update info set intro = #{intro},updatetime = #{updateTime} where user_info_id = #{userInfoId}")
    int updateIntro(Info info);

    // 根据邮箱查询用户信息
    @Select("select count(1) from info where email = #{email}")
    int selectInfoByEmail(String email);

    // 修改邮箱并同步时间
    @Update("update info set email = #{email},updatetime = #{updateTime} where user_info_id = #{userInfoId}")
    int updateEmail(Info info);

    // 修改性别并同步时间
    @Update("update info set gender = #{gender},updatetime = #{updateTime} where user_info_id = #{userInfoId}")
    int updateGender(Info info);

    // 修改生日并同步时间
    @Update("update info set birthday = #{birthday},updatetime = #{updateTime} where user_info_id = #{userInfoId}")
    int updateBirthday(Info info);

    // 修改生日并同步时间
    @Update("update info set address = #{address},updatetime = #{updateTime} where user_info_id = #{userInfoId}")
    int updateAddress(Info info);

    // 增加视频的收藏量
    @Update("update info set collect_num = collect_num + 1 where user_info_id = #{userInfoId}")
    int addCollectVideoNum(String userInfoId);

    // 减少视频的收藏量
    @Update("update info set collect_num = collect_num - 1 where user_info_id = #{userInfoId}")
    int minusCollectVideoNum(String userInfoId);

    // 增加视频的发布量
    @Update("update info set publish_num = publish_num + 1 where user_info_id = #{userInfoId}")
    int addPublishVideoNum(String userInfoId);

    // 减少视频的发布量
    @Update("update info set publish_num = publish_num - 1 where user_info_id = #{userInfoId}")
    int minusPublishVideoNum(String userInfoId);

    // 判断有无关注关系
    @Select("select count(1) from user_focus where user_id = #{userId} and focus_id = #{focusId}")
    int checkIsFocus(@Param("userId") String userId,@Param("focusId") String focusId);

    // 添加关注关系
    @Insert("insert into user_focus (user_id,focus_id) values (#{userId},#{relationId})")
    int insertUserFocus(@Param("userId") String userId,@Param("relationId") String relationId);

    // 删除关注关系
    @Delete("delete from user_focus where user_id = #{userId} and focus_id = #{relationId}")
    int deleteUserFocus(@Param("userId") String userId,@Param("relationId") String relationId);

    // 添加粉丝关系
    @Insert("insert into user_fan (user_id,fan_id) values (#{userId},#{fanId})")
    int insertUserFan(@Param("userId") String userId,@Param("fanId") String fanId);

    // 删除粉丝关系
    @Delete("delete from user_fan where user_id = #{userId} and fan_id = #{fanId}")
    int deleteUserFan(@Param("userId") String userId,@Param("fanId") String fanId);

    // 增加粉丝量
    @Update("update info set fans_num = fans_num + 1 where user_info_id = #{userInfoId}")
    int addFansNum(String userInfoId);

    // 减少粉丝量
    @Update("update info set fans_num = fans_num - 1 where user_info_id = #{userInfoId}")
    int minusFansNum(String userInfoId);

    // 增加关注量
    @Update("update info set focus_num = focus_num + 1 where user_info_id = #{userInfoId}")
    int addFocusNum(String userInfoId);

    // 减少关注量
    @Update("update info set focus_num = focus_num - 1 where user_info_id = #{userInfoId}")
    int minusFocusNum(String userInfoId);

    // 查询关注的人
    @Select("select focus_id from user_focus where user_id = #{userId}")
    List<String> selectFocusId(String userId);

    // 查询粉丝
    @Select("select fan_id from user_fan where user_id = #{userId}")
    List<String> selectFanId(String userId);


}
