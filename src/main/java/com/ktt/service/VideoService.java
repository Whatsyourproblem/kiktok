package com.ktt.service;

import com.ktt.common.ServerResponse;
import com.ktt.pojo.Video;
import com.ktt.vo.VideoInformation;
import org.springframework.http.HttpRequest;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface VideoService {

    // 用户上传视频
    ServerResponse<Video> uploadVideo(HttpServletRequest request, MultipartFile file, String userId, Video video);

    // 获取用户发布的视频
    ServerResponse<Video> getUserPublish(String userId);

    // 获取全部视频
    ServerResponse<List<VideoInformation>> getAllVideos(String userId);

    // 用户给视频点赞
    ServerResponse<Video> recommendVideo(String userId,String videoId,int type);

    // 搜索视频
    ServerResponse<List<VideoInformation>> selectVideoByKeyWord(String keyWord);

    // 获得热点视频信息
    ServerResponse<List<Video>> getHotVideoInfo();

    // 获得随机视频信息
    ServerResponse<List<Video>> getRandomVideoInfo();

    // 获取用户发布的视频
    ServerResponse<List<VideoInformation>> getUserPublishVideo(String userId);

    // 获取用户收藏的视频
    ServerResponse<List<VideoInformation>> getUserCollectVideo(String userId);

    // 获取单个视频
    ServerResponse<VideoInformation> getVideoByVideoId(String userId,String videoId);

}
