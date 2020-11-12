package com.ktt.service.impl;

import com.ktt.common.ServerResponse;
import com.ktt.dao.InfoMapper;
import com.ktt.dao.TrendMapper;
import com.ktt.dao.VideoMapper;
import com.ktt.pojo.Info;
import com.ktt.pojo.Trend;
import com.ktt.pojo.Video;
import com.ktt.service.TrendService;
import com.ktt.utils.DataFormat;
import com.ktt.utils.RandomData;
import com.ktt.vo.TrendInformation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service("trendService")
public class TrendServiceImpl implements TrendService {

    @Autowired
    private RandomData randomData;

    @Autowired
    private DataFormat dataFormat;

    @Autowired
    private TrendMapper trendMapper;

    @Autowired
    private VideoMapper videoMapper;
    
    @Autowired
    private InfoMapper infoMapper;

    // 发布评论
    @Override
    public ServerResponse<Trend> submitTrend(String userId,Trend trend) {

        // 随机动态id
        trend.setTrendId(randomData.getRandomId());
        trend.setUserId(userId);

        trend.setRecommend(0);
        trend.setCommentNum(0);

        Date createTime = dataFormat.formatDate(new Date());
        trend.setCreateTime(createTime);
        trend.setUpdateTime(createTime);

        // 判断动态类型(0为视频,1为评论)
        if(trend.getType() == 0){
            // 目标id的视频转发量加1
            int videoTransmission = videoMapper.addVideoTransmission(trend.getVideoUrl());
            if(videoTransmission <= 0){
                return ServerResponse.createByErrorMessage("发布评论失败!");
            }
        }

        // 正向插入trend
        int result = trendMapper.insertTrend(trend);
        if(result <= 0){
            return ServerResponse.createByErrorMessage("发布动态失败!");
        }
        
        // 反响插入trend
        int results = trendMapper.insertReverseTrend(trend);
        if(results <= 0){
            return ServerResponse.createByErrorMessage("发布动态失败!");
        }

        return ServerResponse.createBySuccessMessage("发布动态成功!");
    }

    // 获取所有动态
    @Override
    public ServerResponse<List<TrendInformation>> getUserTrend(String userId) {

        List<TrendInformation> trendInformationList = new ArrayList<>();

        // 根据userId查询出所有trendId
        List<Trend> trends = trendMapper.selectTrendByUserIdOrRelationId(userId);
        if(trends.size() == 0 || trends == null){
            return ServerResponse.createBySuccess("获取动态成功!",trendInformationList);
        }

        // 根基videoUrl 查询 video对象
        List<Video> videos = new ArrayList<>();
        for(int i = 0;i < trends.size();i++){
            Video video = videoMapper.selectVideoByVideoUrl(trends.get(i).getVideoUrl());
            videos.add(video);
        }

        // 根据自己的userId查询出userInfo
        List<String> publishUserInfoIds = new ArrayList<>();
        for(int i = 0;i < trends.size();i++){
            String publishUserInfoId = infoMapper.selectUserInfoIdByUserId(trends.get(i).getUserId());
            publishUserInfoIds.add(publishUserInfoId);
        }

        List<Info> publishInfos = new ArrayList<>();
        for(int i = 0;i < publishUserInfoIds.size();i++){
            Info publishInfo = infoMapper.selectInfoByUserInfoId(publishUserInfoIds.get(i));
            publishInfos.add(publishInfo);
        }

        // 根据relationId查询出其他人的信息
        List<String> relationInfoIds = new ArrayList<>();
        for(int i = 0;i < trends.size();i++){
            String relationInfoId = infoMapper.selectUserInfoIdByUserId(trends.get(i).getRelationId());
            relationInfoIds.add(relationInfoId);
        }

        // 根据relationInfoId查询出Info
        List<Info> relationInfos = new ArrayList<>();
        for(int i = 0;i < relationInfoIds.size();i++){
            Info relationInfo = infoMapper.selectInfoByUserInfoId(relationInfoIds.get(i));
            relationInfos.add(relationInfo);
        }

        // 组装vo
        for(int i = 0;i < trends.size();i++){
            TrendInformation trendInformation = new TrendInformation();

            trendInformation.setPublishId(trends.get(i).getUserId());
            trendInformation.setPublishName(publishInfos.get(i).getInfoName());
            trendInformation.setPublishImg(publishInfos.get(i).getImgUrl());
            trendInformation.setPublishTime(trends.get(i).getCreateTime());
            trendInformation.setCommentContext(trends.get(i).getContent());
            trendInformation.setDynamicId(trends.get(i).getTrendId());
            trendInformation.setDynamicFromId(trends.get(i).getRelationId());
            trendInformation.setDynamicFromName(relationInfos.get(i).getInfoName());
            trendInformation.setDynamicFromContext(videos.get(i).getTitle());
            trendInformation.setVideoUrl(videos.get(i).getVideoUrl());
            int isLike = trendMapper.checkIsLike(userId, trends.get(i).getTrendId());
            if(isLike == 0){
                // 没点赞
                trendInformation.setIsLike(false);
            }else{
                trendInformation.setIsLike(true);
            }
            trendInformation.setRecommend(trends.get(i).getRecommend());
            trendInformation.setCommentNum(trends.get(i).getCommentNum());
            trendInformation.setTransmission(trends.get(i).getTransmission());
            trendInformation.setType(trends.get(i).getType());

            trendInformationList.add(trendInformation);
        }

        return ServerResponse.createBySuccess("获取动态成功!",trendInformationList);
    }

    // 点赞动态
    @Override
    public ServerResponse<Trend> starTrend(String userId, String trendId,int type) {

        // 判断是不是点赞关系
        if(type != 0){
            // 存在点赞关系,删除点赞关系,让动态的点赞量减一
            int starTrend = trendMapper.deleteStarTrend(userId, trendId);
            if(starTrend <= 0){
                return ServerResponse.createByErrorMessage("取消点赞失败!");
            }
            int trendRecommend = trendMapper.minusTrendRecommend(trendId);
            if(trendRecommend <= 0){
                return ServerResponse.createByErrorMessage("取消点赞失败!");
            }
            return ServerResponse.createBySuccessMessage("取消点赞成功!");
        }
        // 不存在点赞关系,添加点赞关系,让动态的点赞量加一
        int starTrend = trendMapper.insertStarTrend(userId, trendId);
        if(starTrend <= 0){
            return ServerResponse.createByErrorMessage("点赞失败!");
        }
        int trendRecommend = trendMapper.addTrendRecommend(trendId);
        if(trendRecommend <= 0){
            return ServerResponse.createByErrorMessage("点赞失败!");
        }

        return ServerResponse.createBySuccessMessage("点赞成功！");
    }
}
