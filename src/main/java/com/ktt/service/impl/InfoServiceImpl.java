package com.ktt.service.impl;

import com.ktt.common.ServerResponse;
import com.ktt.dao.CommentMapper;
import com.ktt.dao.InfoMapper;
import com.ktt.dao.TrendMapper;
import com.ktt.dao.VideoMapper;
import com.ktt.pojo.Comment;
import com.ktt.pojo.Info;
import com.ktt.pojo.Trend;
import com.ktt.pojo.Video;
import com.ktt.service.InfoService;
import com.ktt.utils.DataFormat;
import com.ktt.utils.RandomData;
import com.ktt.utils.QiniuUtils;
import com.ktt.vo.FFInformation;
import com.ktt.vo.UserInformation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service("infoService")
public class InfoServiceImpl implements InfoService {

    @Autowired
    private InfoMapper infoMapper;
    
    @Autowired
    private QiniuUtils qiniuUtils;

    @Autowired
    private DataFormat dataFormat;

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private VideoMapper videoMapper;

    @Autowired
    private TrendMapper trendMapper;

    @Override
    public ServerResponse<Info> uploadHeadImg(HttpServletRequest request, MultipartFile file, String userId) {

        // 根据userId 查询 userInfoId
        String userInfoId = infoMapper.selectUserInfoIdByUserId(userId);
        if(userInfoId == null || "".equals(userInfoId)){
            return ServerResponse.createByErrorMessage("更换头像失败!");
        }

        // 根据userInfoId 查询 Info 对象
        Info info = infoMapper.selectInfoByUserInfoId(userInfoId);
        if(info == null){
            return ServerResponse.createByErrorMessage("更换头像失败!");
        }

        // 上传图片并返回地址
        String url = qiniuUtils.postUserInforUpDate(request, file, info.getUserInfoId());
        System.out.println(url);
        if(url == null || "".equals(url)){
            return ServerResponse.createByErrorMessage("更换头像失败!");
        }

        info.setImgUrl(url);
        info.setUpdateTime(dataFormat.formatDate(new Date()));

        // 修改头像地址并更新修改日期
        int count = infoMapper.updateHeadImg(info);
        if(count <= 0){
            return ServerResponse.createByErrorMessage("更换头像失败!");
        }


        Info infos = infoMapper.selectInfoByUserInfoId(info.getUserInfoId());

        return ServerResponse.createBySuccess("更换头像成功!",infos);
    }

    // 设置名字
    @Override
    public ServerResponse<Info> updateName(String userId, Info info) {
        // 根据userId 查询到userInfoId
        String userInfoId = infoMapper.selectUserInfoIdByUserId(userId);
        if(userInfoId == null || "".equals(userInfoId)){
            return ServerResponse.createByErrorMessage("设置名字失败!");
        }

        // 根据userInfoId查询到 Info
        Info newInfo = infoMapper.selectInfoByUserInfoId(userInfoId);
        if(newInfo == null){
            return ServerResponse.createByErrorMessage("设置名字失败!");
        }

        // 设置昵称
        newInfo.setInfoName(info.getInfoName());
        newInfo.setUpdateTime(dataFormat.formatDate(new Date()));

        // 修改名称并同步时间
        int count = infoMapper.updateName(newInfo);
        if(count <= 0){
            return ServerResponse.createByErrorMessage("设置名字失败!");
        }

        return ServerResponse.createBySuccess("设置名字成功!",info);
    }

    // 设置简介
    @Override
    public ServerResponse<Info> updateIntro(String userId, Info info) {

        // 根据userId 查询到userInfoId
        String userInfoId = infoMapper.selectUserInfoIdByUserId(userId);
        if(userInfoId == null || "".equals(userInfoId)){
            return ServerResponse.createByErrorMessage("设置简介失败!");
        }

        // 根据userInfoId查询到 Info
        Info newInfo = infoMapper.selectInfoByUserInfoId(userInfoId);
        if(newInfo == null){
            return ServerResponse.createByErrorMessage("设置简介失败!");
        }

        // 设置简介
        newInfo.setIntro(info.getIntro());
        newInfo.setUpdateTime(dataFormat.formatDate(new Date()));

        int count = infoMapper.updateIntro(newInfo);
        if(count <= 0){
            return ServerResponse.createByErrorMessage("设置简介失败!");
        }

        return ServerResponse.createBySuccess("设置简介成功!",info);
    }

    // 设置邮箱
    @Override
    public ServerResponse<Info> updateEmail(String userId, Info info) {

        // 检验邮箱是否唯一
        int result = infoMapper.selectInfoByEmail(info.getEmail());
        if(result > 0){
            return ServerResponse.createByErrorMessage("该邮箱已被使用!");
        }

        // 根据userId 查询到userInfoId
        String userInfoId = infoMapper.selectUserInfoIdByUserId(userId);
        if(userInfoId == null || "".equals(userInfoId)){
            return ServerResponse.createByErrorMessage("设置邮箱失败!");
        }

        // 根据userInfoId查询到 Info
        Info newInfo = infoMapper.selectInfoByUserInfoId(userInfoId);
        if(newInfo == null){
            return ServerResponse.createByErrorMessage("设置邮箱失败!");
        }

        // 设置邮箱
        newInfo.setEmail(info.getEmail());
        newInfo.setUpdateTime(dataFormat.formatDate(new Date()));

        int count = infoMapper.updateEmail(newInfo);
        if(count <= 0){
            return ServerResponse.createByErrorMessage("设置邮箱失败!");
        }

        return ServerResponse.createBySuccess("设置邮箱成功!",info);
    }

    // 设置性别
    @Override
    public ServerResponse<Info> updateGender(String userId, Info info) {

        // 根据userId 查询到userInfoId
        String userInfoId = infoMapper.selectUserInfoIdByUserId(userId);
        if(userInfoId == null || "".equals(userInfoId)){
            return ServerResponse.createByErrorMessage("设置性别失败!");
        }

        // 根据userInfoId查询到 Info
        Info newInfo = infoMapper.selectInfoByUserInfoId(userInfoId);
        if(newInfo == null){
            return ServerResponse.createByErrorMessage("设置性别失败!");
        }

        // 设置性别
        newInfo.setGender(info.getGender());
        newInfo.setUpdateTime(dataFormat.formatDate(new Date()));

        int count = infoMapper.updateGender(newInfo);
        if(count <= 0){
            return ServerResponse.createByErrorMessage("设置性别失败!");
        }

        return ServerResponse.createBySuccess("设置性别成功!",info);
    }

    // 设置生日
    @Override
    public ServerResponse<Info> updateBirthday(String userId, Info info) {

        // 根据userId 查询到userInfoId
        String userInfoId = infoMapper.selectUserInfoIdByUserId(userId);
        if(userInfoId == null || "".equals(userInfoId)){
            return ServerResponse.createByErrorMessage("设置生日失败!");
        }

        // 根据userInfoId查询到 Info
        Info newInfo = infoMapper.selectInfoByUserInfoId(userInfoId);
        if(newInfo == null){
            return ServerResponse.createByErrorMessage("设置生日失败!");
        }

        // 设置生日
        newInfo.setBirthday(dataFormat.formatDate(info.getBirthday()));
        newInfo.setUpdateTime(dataFormat.formatDate(new Date()));

        int count = infoMapper.updateBirthday(newInfo);
        if(count <= 0){
            return ServerResponse.createByErrorMessage("设置生日失败!");
        }

        return ServerResponse.createBySuccess("设置生日成功!",info);
    }

    // 设置地址
    @Override
    public ServerResponse<Info> updateAddress(String userId, Info info) {

        // 根据userId 查询到userInfoId
        String userInfoId = infoMapper.selectUserInfoIdByUserId(userId);
        if(userInfoId == null || "".equals(userInfoId)){
            return ServerResponse.createByErrorMessage("设置地址失败!");
        }

        // 根据userInfoId查询到 Info
        Info newInfo = infoMapper.selectInfoByUserInfoId(userInfoId);
        if(newInfo == null){
            return ServerResponse.createByErrorMessage("设置地址失败!");
        }

        // 设置地址
        newInfo.setAddress(info.getAddress());
        newInfo.setUpdateTime(dataFormat.formatDate(new Date()));

        int count = infoMapper.updateAddress(newInfo);
        if(count <= 0){
            return ServerResponse.createByErrorMessage("设置地址失败!");
        }

        return ServerResponse.createBySuccess("设置地址成功!",info);
    }

    // 获取某人的详细信息
    @Override
    public ServerResponse<UserInformation> getUserInfo(String userId) {

        System.out.println(userId);
        // 根据userId查询出Info,并封装成vo
        String userInfoId = infoMapper.selectUserInfoIdByUserId(userId);
        Info info = infoMapper.selectInfoByUserInfoId(userInfoId);
        System.out.println(userInfoId);
        System.out.println(info);

        int total = 0;
        // 计算出点赞总和(所有评论的点赞数+所有动态的点赞数+所有视频点赞数)
        // 计算评论点赞数
        int commentTotal = 0;
        List<String> commentIds = commentMapper.selectCommentIdByUserId(userId);
        if(commentIds.size() == 0 || commentIds == null){
            commentTotal = 0;
        }
        
        List<Comment> comments = new ArrayList<>();
        for(int i = 0;i < commentIds.size();i++){
            Comment comment = commentMapper.selectCommentByCommentId(commentIds.get(i));
            commentTotal += comment.getRecommend();
            comments.add(comment);
        }
        
        // 计算动态点赞数
        int trendTotal = 0;
        List<Trend> trends = trendMapper.selectTrendByUserId(userId);
        if(trends.size() == 0 || trends == null){
            trendTotal = 0;
        }
        for (Trend trend : trends) {
            trendTotal += trend.getRecommend();
        }

        // 计算发布视频的点赞量
        int videoTotal = 0;
        List<String> videoIds = videoMapper.selectPublishVideoIdByUserId(userId);
        if(videoIds.size() == 0 || videoIds == null){
            videoTotal = 0;
        }
        List<Video> videos = new ArrayList<>();
        for(int i = 0;i < videoIds.size();i++){
            Video video = videoMapper.selectVideoByVideoId(videoIds.get(i));
            videoTotal += video.getRecommend();
            videos.add(video);
        }

        total = commentTotal + trendTotal + videoTotal;

        // 封装
        UserInformation userInformation = new UserInformation();

        userInformation.setUserId(userId);
        userInformation.setUserInfoId(userInfoId);
        userInformation.setUserInfoImg(info.getImgUrl());
        userInformation.setUserInfoName(info.getInfoName());
        userInformation.setUserInfoIntro(info.getIntro());
        userInformation.setUserInfoBirth(info.getBirthday() == null ? null : dataFormat.formatSimpleDate(info.getBirthday()));
        userInformation.setUserInfoAddress(info.getAddress());
        userInformation.setUserInfoGender(info.getGender());
        userInformation.setUserInfoEmail(info.getEmail());
        userInformation.setUserInfoStar(total);
        userInformation.setUserInfoFocus(info.getFocusNum());
        userInformation.setUserInfoFans(info.getFansNum());
        userInformation.setUserInfoPublish(info.getPublishNum());
        userInformation.setUserInfoCollects(info.getCollectNum());

        return ServerResponse.createBySuccess("获取信息成功!",userInformation);
    }

    // 关注
    @Override
    public ServerResponse<Info> focusUser(String userId, String relationId) {

        // 判断是该relationId是否被关注
        int isFocus = infoMapper.checkIsFocus(userId, relationId);
        if(isFocus != 0){
            // 存在关注关系,删除关注关系,删除粉丝关系,并且将userId的关注量减一,将relationId的粉丝量减一
            int userFocus = infoMapper.deleteUserFocus(userId, relationId);
            if(userFocus <= 0){
                return ServerResponse.createByErrorMessage("取消关注失败!");
            }
            int userFan = infoMapper.deleteUserFan(relationId, userId);
            if(userFan <= 0){
                return ServerResponse.createByErrorMessage("取消关注失败!");
            }
            String userInfoId = infoMapper.selectUserInfoIdByUserId(userId);
            String relationInfoId = infoMapper.selectUserInfoIdByUserId(relationId);

            int focusNum = infoMapper.minusFocusNum(userInfoId);
            if(focusNum <= 0){
                return ServerResponse.createByErrorMessage("取消关注失败!");
            }
            int fansNum = infoMapper.minusFansNum(relationInfoId);
            if(fansNum <= 0){
                return ServerResponse.createByErrorMessage("取消关注失败!");
            }
            return ServerResponse.createBySuccessMessage("取消关注成功!");
        }

        // 不存在关注关系,增加关注关系,增加粉丝关系,并将userId的关注量加一,将relationId的粉丝量加一
        int userFocus = infoMapper.insertUserFocus(userId, relationId);
        if(userFocus <= 0){
            return ServerResponse.createByErrorMessage("关注失败!");
        }
        int userFan = infoMapper.insertUserFan(relationId,userId);
        if(userFan <= 0){
            return ServerResponse.createByErrorMessage("关注失败!");
        }
        String userInfoId = infoMapper.selectUserInfoIdByUserId(userId);
        String relationUserId = infoMapper.selectUserInfoIdByUserId(relationId);
        int focusNum = infoMapper.addFocusNum(userInfoId);
        if(focusNum <= 0){
            return ServerResponse.createByErrorMessage("关注失败!");
        }
        int fansNum = infoMapper.addFansNum(relationUserId);
        if(fansNum <= 0){
            return ServerResponse.createByErrorMessage("关注失败!");
        }
        return ServerResponse.createBySuccessMessage("关注成功!");
    }

    // 返回关注/粉丝列表
    @Override
    public ServerResponse<List<FFInformation>> getFocusOrFanList(String userId, int type) {

        // 判断type为0还是1,0返回关注列表,1返回粉丝列表
        if(type == 0){
            List<FFInformation> ffInformationList = new ArrayList<>();
                    // 返回关注列表 封装成vo集合
            List<String> focusIds = infoMapper.selectFocusId(userId);
            System.out.println(focusIds);
            if(focusIds.size() == 0 || focusIds == null){
                return ServerResponse.createBySuccess("成功!",ffInformationList);
            }

            List<String> userInfoIds = new ArrayList<>();
            for(int i = 0;i < focusIds.size() ;i++){
                String userInfoId = infoMapper.selectUserInfoIdByUserId(focusIds.get(i));
                userInfoIds.add(userInfoId);
            }

            List<Info> infos = new ArrayList<>();
            for (int i = 0;i < userInfoIds.size();i++){
                Info info = infoMapper.selectInfoByUserInfoId(userInfoIds.get(i));
                infos.add(info);
            }

            // 封装vo
            for(int i = 0;i < infos.size();i++){
                FFInformation ffInformation = new FFInformation();
                ffInformation.setUserId(focusIds.get(i));
                ffInformation.setUserName(infos.get(i).getInfoName());
                ffInformation.setUserImg(infos.get(i).getImgUrl());
                ffInformation.setUserIntro(infos.get(i).getIntro());

                ffInformationList.add(ffInformation);
            }
            return ServerResponse.createBySuccess("返回关注列表成功!",ffInformationList);
        }

        // 返回粉丝列表
        List<FFInformation> ffInformationList = new ArrayList<>();
        // 返回关注列表 封装成vo集合
        List<String> fanIds = infoMapper.selectFanId(userId);
        if(fanIds.size() == 0 || fanIds == null){
            return ServerResponse.createBySuccess("成功!",ffInformationList);
        }

        List<String> userInfoIds = new ArrayList<>();
        for(int i = 0;i < fanIds.size() ;i++){
            String userInfoId = infoMapper.selectUserInfoIdByUserId(fanIds.get(i));
            userInfoIds.add(userInfoId);
        }

        List<Info> infos = new ArrayList<>();
        for (int i = 0;i < userInfoIds.size();i++){
            Info info = infoMapper.selectInfoByUserInfoId(userInfoIds.get(i));
            infos.add(info);
        }

        // 封装vo
        for(int i = 0;i < infos.size();i++){
            FFInformation ffInformation = new FFInformation();
            ffInformation.setUserId(fanIds.get(i));
            ffInformation.setUserName(infos.get(i).getInfoName());
            ffInformation.setUserImg(infos.get(i).getImgUrl());
            ffInformation.setUserIntro(infos.get(i).getIntro());

            ffInformationList.add(ffInformation);
        }
        return ServerResponse.createBySuccess("返回关注列表成功!",ffInformationList);
    }

}
