package com.ktt.controller.portal;

import com.ktt.common.ServerResponse;
import com.ktt.pojo.Info;
import com.ktt.service.InfoService;
import com.ktt.vo.FFInformation;
import com.ktt.vo.UserInformation;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/info")
public class InfoController {

    @Autowired
    private InfoService infoService;

    // todo 测试一下接口

    // 更换头像
    @PostMapping("/update_head_user/{userId}")
    public ServerResponse<Info> updateHeadImg(@PathVariable String userId,@RequestParam("file") MultipartFile file,HttpServletRequest request){
        return infoService.uploadHeadImg(request,file,userId);
    }

    // 更改昵称(名字)
    @PostMapping("/update_name_user/{userId}")
    public ServerResponse<Info> updateName(@PathVariable String userId,@RequestBody Info info){
        System.out.println(info.getInfoName());
        return infoService.updateName(userId,info);
    }

    // 设置简介
    @PostMapping("/update_intro_user/{userId}")
    public ServerResponse<Info> updateIntro(@PathVariable String userId,@RequestBody Info info){
        System.out.println(info.getIntro());
        return infoService.updateIntro(userId,info);
    }

    // 设置邮箱
    @PostMapping("/update_email_user/{userId}")
    public ServerResponse<Info> updateEmail(@PathVariable String userId,@RequestBody Info info){
        return infoService.updateEmail(userId,info);
    }

    // 设置性别
    @PostMapping("/update_gender_user/{userId}")
    public ServerResponse<Info> updateGender(@PathVariable String userId,@RequestBody Info info){
        return infoService.updateGender(userId,info);
    }

    // 设置生日
    @PostMapping("/update_birth_user/{userId}")
    public ServerResponse<Info> updateBirthday(@PathVariable String userId,@RequestBody Info info){
        return infoService.updateBirthday(userId,info);
    }

    // 设置地址
    @PostMapping("/update_address_user/{userId}")
    public ServerResponse<Info> updateAddress(@PathVariable String userId,@RequestBody Info info){
        return infoService.updateAddress(userId,info);
    }

    // 获取某人的详细信息
    @GetMapping("/get_info_user/{userId}")
    public ServerResponse<UserInformation> getUserInfo(@PathVariable String userId){
        return infoService.getUserInfo(userId);
    }

    // 关注某人
    @PostMapping("/focus_user/{userId}/{relationId}")
    public ServerResponse<Info> focusUser(@PathVariable String userId,@PathVariable String relationId){
        System.out.println(userId);
        System.out.println(relationId);
        return infoService.focusUser(userId,relationId);
    }

    // 获取关注/粉丝列表
    @GetMapping("/get_ff_list/{userId}/{type}")
    public ServerResponse<List<FFInformation>> getFocusOrFanList(@PathVariable String userId, @PathVariable int type){
        return infoService.getFocusOrFanList(userId,type);
    }

}
