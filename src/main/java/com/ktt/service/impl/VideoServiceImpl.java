package com.ktt.service.impl;

import com.ktt.common.ServerResponse;
import com.ktt.dao.CategoryMapper;
import com.ktt.dao.InfoMapper;
import com.ktt.dao.VideoMapper;
import com.ktt.pojo.Info;
import com.ktt.pojo.Video;
import com.ktt.service.VideoService;
import com.ktt.utils.DataFormat;
import com.ktt.utils.RandomData;
import com.ktt.utils.QiniuUtils;
import com.ktt.utils.RedisUtils;
import com.ktt.vo.VideoInformation;
import lombok.Synchronized;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service("videoService")
public class VideoServiceImpl implements VideoService {
    
    @Autowired
    private VideoMapper videoMapper;

    @Autowired
    private CategoryMapper categoryMapper;
    
    @Autowired
    private RandomData randomData;


    @Autowired
    private DataFormat dataFormat;

    @Autowired
    private QiniuUtils qiniuUtils;
    
    @Autowired
    private InfoMapper infoMapper;

    @Autowired
    private RedisUtils redisUtils;

    // 处理用户上传视频
    @Override
    public ServerResponse<Video> uploadVideo(HttpServletRequest request, MultipartFile file, String userId, Video video) {

        // 设置视频id
        String randomId = randomData.getRandomId();
        video.setVideoId(randomId);

        // 上传用户的视频并返回url地址
        String url = qiniuUtils.postUserInforUpDate(request, file, video.getVideoId());
        System.out.println(url);


        // 将用户id和视频id插入到 用户发布视频表里
        int count = videoMapper.insertUserIdAndVideoId(userId, video.getVideoId());
        if(count <= 0){
            return ServerResponse.createByErrorMessage("上传失败!");
        }

        // 前台的格式为 xx,xx,xx, 分割字符串
        String[] source = video.getType().split(",");
        String l = "";
        for (String s : source) {
            // 根据视频的类别名称查询到类别id 并将类别id和视频id存到视频类别表中
            String categoryId = categoryMapper.selectCategoryIdByCategoryName(s);
            int res = categoryMapper.insertVideoAndCategory(video.getVideoId(),categoryId);
            if(res <= 0){
                return ServerResponse.createByErrorMessage("上传失败!");
            }
            l+= s + "/";
        }
        l = l.substring(0,l.length()-1);
        video.setType(l);

        // 设置连接地址
        video.setVideoUrl(url);

        // 设置视频上传时间
        Date createTime = dataFormat.formatDate(new Date());
        // 设置点赞量为0
        video.setRecommend(0);
        // 设置转发量
        video.setTransmission(0);
        // 设置评论量
        video.setCommentNum(0);
        // 设置视频的创建时间
        video.setCreateTime(createTime);
        // 设置视频的修改时间
        video.setUpdateTime(createTime);

        int result = videoMapper.insertVideo(video);
        if(result <= 0){
            return ServerResponse.createByErrorMessage("上传失败!");
        }

        // 增加用户视频发布量
        String userInfoId = infoMapper.selectUserInfoIdByUserId(userId);
        int publishVideoNum = infoMapper.addPublishVideoNum(userInfoId);
        if(publishVideoNum <= 0){
            return ServerResponse.createByErrorMessage("上传失败!");
        }

        return ServerResponse.createBySuccess("上传成功!",video);

    }



    @Override
    public ServerResponse<Video> getUserPublish(String userId) {
        return null;
    }


    @Override
    public ServerResponse<List<VideoInformation>> getAllVideos(String userId) {

        List<Video> videos = videoMapper.getAllVideos();
        if(videos.size() == 0){
            return ServerResponse.createByErrorMessage("获取视频失败!");
        }

        // 封装成videoInformation对象
        // 根据videoId 查询出所有的 userId
        List<String> userIds = new ArrayList<>();
        for (Video video : videos) {
            String s = videoMapper.selectUserIdByVideoId(video.getVideoId());
            userIds.add(s);
        }
        
        // 根据userId查询出所有的userInfoId
        List<String> userInfoIds = new ArrayList<>();
        for (int i = 0;i < userIds.size();i++){
            String s = infoMapper.selectUserInfoIdByUserId(userIds.get(i));
            userInfoIds.add(s);
        }
        
        // 根据userInfoId 查询出 Info
        List<Info> Infos = new ArrayList<>();
        for(int i = 0;i < userInfoIds.size();i++){
            Info info = infoMapper.selectInfoByUserInfoId(userInfoIds.get(i));
            Infos.add(info);
        }

        List<VideoInformation> videoInformationList = new ArrayList<>();
        for(int i = 0;i < videos.size();i++){
            VideoInformation videoInformation = new VideoInformation();
            videoInformation.setVideoId(videos.get(i).getVideoId());
            videoInformation.setTitle(videos.get(i).getTitle());
            int flag = videoMapper.checkIsLike(userId, videos.get(i).getVideoId());
            if(flag != 0){
                videoInformation.setIsLike(true);
            }else{
                videoInformation.setIsLike(false);
            }
            videoInformation.setPublishImg(Infos.get(i).getImgUrl());
            videoInformation.setPublishId(userIds.get(i));
            videoInformation.setPublishName(Infos.get(i).getInfoName());
            videoInformation.setRecommend(videos.get(i).getRecommend());
            videoInformation.setCommentNum(videos.get(i).getCommentNum());
            videoInformation.setTransmission(videos.get(i).getTransmission());
            videoInformation.setVideoUrl(videos.get(i).getVideoUrl());

            videoInformationList.add(videoInformation);
        }

        return ServerResponse.createBySuccess("获取视频成功!",videoInformationList);
    }



    // 点赞(收藏)
    @Override
    public ServerResponse<Video> recommendVideo(String userId, String videoId,int type) {
        // 判断缓存中是否存在点赞关系
        if(type == 0){
            // 不存在点赞关系
            int count = videoMapper.insertUserCollect(userId, videoId);
            if(count <= 0){
                return ServerResponse.createByErrorMessage("收藏失败!");
            }
            int result = videoMapper.addVideoRecommend(videoId);
            if(result <= 0){
                return ServerResponse.createByErrorMessage("收藏失败!");
            }

            String userInfoId = infoMapper.selectUserInfoIdByUserId(userId);
            int collectVideoNum = infoMapper.addCollectVideoNum(userInfoId);
            if(collectVideoNum <= 0){
                return ServerResponse.createByErrorMessage("收藏失败!");
            }

            return ServerResponse.createBySuccessMessage("收藏成功!");
        }
        // 存在点赞关系
        // 已经存在点赞关系,视频点赞量减一,并删除点赞关系,并将信息表里的视频收藏量减一
        int count = videoMapper.deleteUserCollect(userId, videoId);
        if(count <= 0){
            return ServerResponse.createByErrorMessage("取消收藏失败!");
        }
        int result = videoMapper.minusVideoRecommend(videoId);
        if(result <= 0){
            return ServerResponse.createByErrorMessage("取消收藏失败!");
        }
        String userInfoId = infoMapper.selectUserInfoIdByUserId(userId);
        int collectVideoNum = infoMapper.minusCollectVideoNum(userInfoId);
        if(collectVideoNum <= 0){
            return ServerResponse.createByErrorMessage("取消收藏失败!");
        }

        return ServerResponse.createBySuccessMessage("取消收藏成功!");

    }


    // 点赞(收藏)
/*    @Override
    public ServerResponse<Video> recommendVideo(String userId,String videoId) {

        // 判断是否已经点赞
        int isLike = videoMapper.checkIsLike(userId, videoId);
        if(isLike != 0){
            // 已经存在点赞关系,视频点赞量减一,并删除点赞关系,并将信息表里的视频收藏量减一
            int count = videoMapper.deleteUserCollect(userId, videoId);
            if(count <= 0){
                return ServerResponse.createByErrorMessage("取消收藏失败!");
            }
            int result = videoMapper.minusVideoRecommend(videoId);
            if(result <= 0){
                return ServerResponse.createByErrorMessage("取消收藏失败!");
            }
            String userInfoId = infoMapper.selectUserInfoIdByUserId(userId);
            int collectVideoNum = infoMapper.minusCollectVideoNum(userInfoId);
            if(collectVideoNum <= 0){
                return ServerResponse.createByErrorMessage("取消收藏失败!");
            }

            return ServerResponse.createBySuccessMessage("取消收藏成功!");
        }
        // 不存在点赞关系,插入点赞关系并让视频点赞量加一
        int count = videoMapper.insertUserCollect(userId, videoId);
        if(count <= 0){
            return ServerResponse.createByErrorMessage("收藏失败!");
        }
        int result = videoMapper.addVideoRecommend(videoId);
        if(result <= 0){
            return ServerResponse.createByErrorMessage("收藏失败!");
        }

        String userInfoId = infoMapper.selectUserInfoIdByUserId(userId);
        int collectVideoNum = infoMapper.addCollectVideoNum(userInfoId);
        if(collectVideoNum <= 0){
            return ServerResponse.createByErrorMessage("收藏失败!");
        }

        return ServerResponse.createBySuccessMessage("收藏成功!");
    }*/

    // 关键字模糊查询
    @Override
    public ServerResponse<List<VideoInformation>> selectVideoByKeyWord(String keyWord) {

        // todo 暂时使用模糊查询,后续升级成elasticsearch
        // 根据关键字进行模糊查询,查询出视频id集合
        String newKeyWord = "%" + keyWord + "%";
        List<Video> videos = videoMapper.selectVideoByKeyWord(newKeyWord);
        if(videos == null || videos.size() == 0){
            return ServerResponse.createBySuccessMessage("没有匹配的结果!");
        }

        // 封装成videoInformation对象
        // 根据videoId 查询出所有的 userId
        List<String> userIds = new ArrayList<>();
        for (Video video : videos) {
            String s = videoMapper.selectUserIdByVideoId(video.getVideoId());
            userIds.add(s);
        }

        // 根据userId查询出所有的userInfoId
        List<String> userInfoIds = new ArrayList<>();
        for (int i = 0;i < userIds.size();i++){
            String s = infoMapper.selectUserInfoIdByUserId(userIds.get(i));
            userInfoIds.add(s);
        }

        // 根据userInfoId 查询出 Info
        List<Info> Infos = new ArrayList<>();
        for(int i = 0;i < userInfoIds.size();i++){
            Info info = infoMapper.selectInfoByUserInfoId(userInfoIds.get(i));
            Infos.add(info);
        }

        List<VideoInformation> videoInformationList = new ArrayList<>();
        for(int i = 0;i < videos.size();i++){
            VideoInformation videoInformation = new VideoInformation();
            videoInformation.setVideoId(videos.get(i).getVideoId());
            videoInformation.setTitle(videos.get(i).getTitle());
            int flag = videoMapper.checkIsLike(userIds.get(i), videos.get(i).getVideoId());
            if(flag != 0){
                videoInformation.setIsLike(true);
            }else{
                videoInformation.setIsLike(false);
            }
            videoInformation.setPublishImg(Infos.get(i).getImgUrl());
            videoInformation.setPublishId(userIds.get(i));
            videoInformation.setPublishName(Infos.get(i).getInfoName());
            videoInformation.setRecommend(videos.get(i).getRecommend());
            videoInformation.setCommentNum(videos.get(i).getCommentNum());
            videoInformation.setTransmission(videos.get(i).getTransmission());
            videoInformation.setVideoUrl(videos.get(i).getVideoUrl());

            videoInformationList.add(videoInformation);
        }

        return ServerResponse.createBySuccess("查询成功!",videoInformationList);
    }

    @Override
    public ServerResponse<List<Video>> getHotVideoInfo() {

        // 按热度降序查询所有视频
        List<Video> videos = videoMapper.selectHotVideo();
        if(videos.size() == 0 || videos == null){
            return ServerResponse.createByErrorMessage("发生了预期之外的错误!");
        }

        return ServerResponse.createBySuccess("查询成功!",videos);
    }

    @Override
    public ServerResponse<List<Video>> getRandomVideoInfo() {
        // 按随机返回视频信息
        List<Video> videos = videoMapper.selectRandomVideo();
        if(videos.size() == 0 || videos == null){
            return ServerResponse.createByErrorMessage("发生了预期之外的错误!");
        }

        return ServerResponse.createBySuccess("查询成功!",videos);
    }

    @Override
    public ServerResponse<List<VideoInformation>> getUserPublishVideo(String userId) {

        // 根据userId查询出所有的videoId
        List<String> videoIds = videoMapper.selectPublishVideoIdByUserId(userId);
        if(videoIds.size() == 0 || videoIds == null){
            List<VideoInformation> videoInformationList = new ArrayList<>();
            return ServerResponse.createBySuccess("获取视频成功!",videoInformationList);
        }
        
        // 根据userId查询出userInfoId;
        String userInfoId = infoMapper.selectUserInfoIdByUserId(userId);
        
        // 根据userInfoId查询出Info
        Info info = infoMapper.selectInfoByUserInfoId(userInfoId);

        // 根据videoId查询出video
        List<Video> videos = new ArrayList<>();
        for(int i = 0;i < videoIds.size();i++){
            Video video = videoMapper.selectVideoByVideoId(videoIds.get(i));
            videos.add(video);
        }

        List<VideoInformation> videoInformationList = new ArrayList<>();
        for(int i = 0;i < videos.size();i++){
            VideoInformation videoInformation = new VideoInformation();

            videoInformation.setVideoId(videos.get(i).getVideoId());
            videoInformation.setTitle(videos.get(i).getTitle());
            int flag = videoMapper.checkIsLike(userId, videos.get(i).getVideoId());
            if(flag != 0){
                videoInformation.setIsLike(true);
            }else{
                videoInformation.setIsLike(false);
            }
            videoInformation.setPublishImg(info.getImgUrl());
            videoInformation.setPublishId(userId);
            videoInformation.setPublishName(info.getInfoName());
            videoInformation.setRecommend(videos.get(i).getRecommend());
            videoInformation.setCommentNum(videos.get(i).getCommentNum());
            videoInformation.setTransmission(videos.get(i).getTransmission());
            videoInformation.setVideoUrl(videos.get(i).getVideoUrl());

            videoInformationList.add(videoInformation);
        }

        return ServerResponse.createBySuccess("查询成功!",videoInformationList);
    }

    // 查看收藏
    @Override
    public ServerResponse<List<VideoInformation>> getUserCollectVideo(String userId) {

        List<String> videoIds = videoMapper.selectCollectVideoIdByUserId(userId);

        if(videoIds.size() == 0 || videoIds == null){
            List<VideoInformation> videoInformationList = new ArrayList<>();
            return ServerResponse.createBySuccess("获取视频成功!",videoInformationList);
        }
        
        // 根据videoId查询出userId(即收藏的视频的发布者)
        List<String> userIds = new ArrayList<>();
        for(int i = 0;i < videoIds.size();i++){
            String newUserId = videoMapper.selectUserIdByVideoId(videoIds.get(i));
            userIds.add(newUserId);
        }
        
        // 根据userId查询出userInfoId
        List<String> userInfoIds = new ArrayList<>();
        for(int i = 0;i < userIds.size();i++){
            String userInfoId = infoMapper.selectUserInfoIdByUserId(userIds.get(i));
            userInfoIds.add(userInfoId);
        }
        
        // 根据userInfoId查询Info
        List<Info> infos = new ArrayList<>();
        for(int i = 0;i < userInfoIds.size();i++){
            Info info = infoMapper.selectInfoByUserInfoId(userInfoIds.get(i));
            infos.add(info);
        }

        // 根据videoId查询出video
        List<Video> videos = new ArrayList<>();
        for(int i = 0;i < videoIds.size();i++){
            Video video = videoMapper.selectVideoByVideoId(videoIds.get(i));
            videos.add(video);
        }

        List<VideoInformation> videoInformationList = new ArrayList<>();
        for(int i = 0;i < videos.size();i++){
            VideoInformation videoInformation = new VideoInformation();

            videoInformation.setVideoId(videos.get(i).getVideoId());
            videoInformation.setTitle(videos.get(i).getTitle());
            int flag = videoMapper.checkIsLike(userIds.get(i), videos.get(i).getVideoId());
            if(flag != 0){
                videoInformation.setIsLike(true);
            }else{
                videoInformation.setIsLike(false);
            }
            videoInformation.setPublishImg(infos.get(i).getImgUrl());
            videoInformation.setPublishId(userIds.get(i));
            videoInformation.setPublishName(infos.get(i).getInfoName());
            videoInformation.setRecommend(videos.get(i).getRecommend());
            videoInformation.setCommentNum(videos.get(i).getCommentNum());
            videoInformation.setTransmission(videos.get(i).getTransmission());
            videoInformation.setVideoUrl(videos.get(i).getVideoUrl());

            videoInformationList.add(videoInformation);
        }

        return ServerResponse.createBySuccess("查询成功!",videoInformationList);
    }

    @Override
    public ServerResponse<VideoInformation> getVideoByVideoId(String userId,String videoId) {

        Video video = videoMapper.selectVideoByVideoId(videoId);
        if(video == null){
            return ServerResponse.createByErrorMessage("获取失败!");
        }

        String userIds = videoMapper.selectUserIdByVideoId(videoId);
        if(userId == null || userId.equals("")){
            return ServerResponse.createByErrorMessage("获取失败!");
        }

        String userInfoId = infoMapper.selectUserInfoIdByUserId(userIds);
        if(userInfoId == null || userInfoId.equals("")){
            return ServerResponse.createByErrorMessage("获取失败!");
        }

        Info info = infoMapper.selectInfoByUserInfoId(userInfoId);
        if(info == null){
            return ServerResponse.createByErrorMessage("获取失败!");
        }

        // 封装vo
        VideoInformation videoInformation = new VideoInformation();
        videoInformation.setVideoId(videoId);
        videoInformation.setTitle(video.getTitle());
        int flag = videoMapper.checkIsLike(userId,videoId);
        if(flag != 0){
            videoInformation.setIsLike(true);
        }else{
            videoInformation.setIsLike(false);
        }
        videoInformation.setPublishImg(info.getImgUrl());
        videoInformation.setPublishId(userId);
        videoInformation.setPublishName(info.getInfoName());
        videoInformation.setRecommend(video.getRecommend());
        videoInformation.setCommentNum(video.getCommentNum());
        videoInformation.setTransmission(video.getTransmission());
        videoInformation.setVideoUrl(video.getVideoUrl());

        return ServerResponse.createBySuccess("获取成功!",videoInformation);
    }
}
