package com.ktt.controller.portal;

import com.ktt.common.ServerResponse;
import com.ktt.pojo.Video;
import com.ktt.service.VideoService;
import com.ktt.utils.RandomData;
import com.ktt.utils.QiniuUtils;
import com.ktt.vo.VideoInformation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/video")
public class VideoController {

    @Autowired
    private VideoService videoService;

    @Autowired
    private QiniuUtils qiniuUtils;

    @Autowired
    private RandomData randomData;


    // 用户上传视频
    @PostMapping("/upload/{userId}")
    public ServerResponse<Video> uploadVideo(HttpServletRequest request, @RequestParam("file") MultipartFile file,Video video,HttpSession session,@PathVariable String userId){

        // todo 这里的userId暂时通过请求发送过来,后续会改成token令牌验证或者cookie中存session

        /*User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorMessage("用户未登录!");
        }*/

        return videoService.uploadVideo(request,file,userId,video);
    }


    // 获取用户发布的视频
    @PostMapping("/get_user_publish/{userId}")
    public ServerResponse<List<VideoInformation>> getUserPublish(@PathVariable String userId){
        return videoService.getUserPublishVideo(userId);
    }

    // 获取用户收藏的视频
    @PostMapping("/get_user_collect/{userId}")
    public ServerResponse<List<VideoInformation>> getUserCollect(@PathVariable String userId){
        return videoService.getUserCollectVideo(userId);
    }


    // 获取全部视频
    @GetMapping("/get_all_videos/{userId}")
    public ServerResponse<List<VideoInformation>> getAllVideos(@PathVariable String userId){
        return videoService.getAllVideos(userId);
    }

    // 视频点赞
    @PostMapping("/recommend_video/{userId}/{videoId}/{type}")
    public ServerResponse<Video> starVideo(@PathVariable String userId,@PathVariable String videoId,@PathVariable int type){
        return videoService.recommendVideo(userId,videoId,type);
    }

    // 视频搜索(暂时先做成模糊查询,后期引入elasticsearch)
    @GetMapping("/select_video_keyword/{keyWord}")
    public ServerResponse<List<VideoInformation>> selectVideoByKeyWord(@PathVariable String keyWord){
        System.out.println(keyWord);
        return videoService.selectVideoByKeyWord(keyWord);
    }

    // 获取热点视频信息
    @GetMapping("/get_hot_info")
    public ServerResponse<List<Video>> getHotVideoInfo(){
        return videoService.getHotVideoInfo();
    }

    // 获取随机视频信息
    @GetMapping("/get_random_info")
    public ServerResponse<List<Video>> getRandomVideoInfo(){
        return videoService.getRandomVideoInfo();
    }

    // 根据视频id获取视频
    @GetMapping("/get_single_video/{userId}/{videoId}")
    public ServerResponse<VideoInformation> getVideoByVideoId(@PathVariable String userId,@PathVariable String videoId){
        return videoService.getVideoByVideoId(userId,videoId);
    }
}
