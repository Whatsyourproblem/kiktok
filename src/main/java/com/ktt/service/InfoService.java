package com.ktt.service;

import com.ktt.common.ServerResponse;
import com.ktt.pojo.Info;
import com.ktt.vo.FFInformation;
import com.ktt.vo.UserInformation;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface InfoService {

    // 设置头像
    ServerResponse<Info> uploadHeadImg(HttpServletRequest request,MultipartFile file,String userId);

    // 设置名字
    ServerResponse<Info> updateName(String userId,Info info);

    // 设置简介
    ServerResponse<Info> updateIntro(String userId,Info info);

    // 设置邮箱
    ServerResponse<Info> updateEmail(String userId,Info info);

    // 设置性别
    ServerResponse<Info> updateGender(String userId,Info info);

    // 设置生日
    ServerResponse<Info> updateBirthday(String userId,Info info);

    // 设置地址
    ServerResponse<Info> updateAddress(String userId,Info info);

    // 获取某人的详细信息
    ServerResponse<UserInformation> getUserInfo(String userId);

    // 关注某人
    ServerResponse<Info> focusUser(String userId,String relationId);

    // 获取关注/收藏列表
    ServerResponse<List<FFInformation>> getFocusOrFanList(String userId, int type);

}
