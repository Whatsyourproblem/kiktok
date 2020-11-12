package com.ktt.service.impl;

import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.ktt.common.Const;
import com.ktt.common.ServerResponse;
import com.ktt.dao.*;
import com.ktt.pojo.*;
import com.ktt.service.UserService;
import com.ktt.utils.*;
import com.ktt.vo.UserInformation;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service("userService")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    private CheckValid checkValid;

    @Autowired
    private RandomData randomData;

    @Autowired
    private Md5Encry md5Encry;

    @Autowired
    private DataFormat dataFormat;
    
    @Autowired
    private InfoMapper infoMapper;

    @Autowired
    private TrendMapper trendMapper;

    @Autowired
    private VideoMapper videoMapper;

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private AliyunUtils aliyunUtils;

    @Autowired
    private RedisUtils redisUtils;

    @Override
    public ServerResponse<UserInformation> login(String phoneNum, String passWord) {

        // 验证手机号是否存在
        int result = userMapper.checkUserPhoneNum(phoneNum);
        if(result == 0){
            // 手机号不存在
            return ServerResponse.createByErrorMessage("手机号不存在!");
        }

        // 手机号存在,根据手机号查询出盐
        String salt = userMapper.selectSaltByPhoneNum(phoneNum);
        // 加密
        String encodedPassword = md5Encry.enCode(passWord,salt);

        // 验证加密后的密码和数据表中的密码是否相等
        User user = userMapper.selectLoginUser(phoneNum, encodedPassword);
        if(user == null){
            return ServerResponse.createByErrorMessage("用户密码错误!");
        }

        User newUser = userMapper.selectUserByPhoneNum(user.getPhoneNum());
        // 这里先暂时使用BySuccess,不需要data


        // 封装vo
        // 根据userId查询出Info,并封装成vo
        String userInfoId = infoMapper.selectUserInfoIdByUserId(newUser.getUserId());
        Info info = infoMapper.selectInfoByUserInfoId(userInfoId);

        int total = 0;
        // 计算出点赞总和(所有评论的点赞数+所有动态的点赞数+所有视频点赞数)
        // 计算评论点赞数
        int commentTotal = 0;
        List<String> commentIds = commentMapper.selectCommentIdByUserId(newUser.getUserId());
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

        List<Trend> trends = trendMapper.selectTrendByUserId(newUser.getUserId());

        if(trends.size() == 0 || trends == null){
            trendTotal = 0;
        }else{
            for (Trend trend : trends) {
                trendTotal += trend.getRecommend();
            }
        }


        // 计算发布视频的点赞量
        int videoTotal = 0;
        List<String> videoIds = videoMapper.selectPublishVideoIdByUserId(newUser.getUserId());
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

        userInformation.setUserId(newUser.getUserId());
        userInformation.setUserInfoId(userInfoId);
        userInformation.setUserInfoImg(info.getImgUrl());
        userInformation.setUserInfoName(info.getInfoName());
        userInformation.setUserInfoIntro(info.getIntro());
        userInformation.setUserInfoBirth(info.getBirthday() == null ? null: dataFormat.formatSimpleDate(info.getBirthday()));
        userInformation.setUserInfoAddress(info.getAddress());
        userInformation.setUserInfoGender(info.getGender());
        userInformation.setUserInfoEmail(info.getEmail());
        userInformation.setUserInfoStar(total);
        userInformation.setUserInfoFocus(info.getFocusNum());
        userInformation.setUserInfoFans(info.getFansNum());
        userInformation.setUserInfoPublish(info.getPublishNum());
        userInformation.setUserInfoCollects(info.getCollectNum());
        
        return ServerResponse.createBySuccess("登录成功!",userInformation);
    }

    @Override
    public ServerResponse<User> register(String code,User user) {
        // 验证手机号是否符合规范(暂时先这样做,后续会改)
        boolean result = checkValid.checkPhoneNum(user.getPhoneNum());
        if(!result){
            return ServerResponse.createByErrorMessage("用户手机号不符合规范");
        }

        // 验证手机号是否存在
        int num = userMapper.checkUserPhoneNum(user.getPhoneNum());
        if(num > 0){
            // 手机号已存在,账户已经被注册
            return ServerResponse.createByErrorMessage("该手机号已被注册!");
        }

        // 验证验证码的是否与传来的值相等
        String OldCode = (String) redisUtils.get(user.getPhoneNum());
        System.out.println(OldCode);
        if(!OldCode.equals(code) || !OldCode.equals(code)){
            // 验证码错误
            return ServerResponse.createByErrorMessage("验证码输入错误!");
        }

        // 设置随机Id
        user.setUserId(randomData.getRandomId());
        // 获取盐
        String salt = md5Encry.getSalt();
        // 获取加密密码
        String encodedPassword = md5Encry.enCode(user.getPassWord(),salt);
        // 设置盐
        user.setSalt(salt);
        // 设置密码(加密后的)
        user.setPassWord(encodedPassword);
        // 设置时间
        user.setCreateTime(dataFormat.formatDate(new Date()));
        user.setUpdateTime(dataFormat.formatDate(new Date()));
        // 设置用户状态 默认为0 启用
        user.setStatus(0);

        int count = userMapper.insertUser(user);
        // 插入失败
        if(count <= 0){
            return ServerResponse.createByErrorMessage("用户注册失败!");
        }

        // 用户注册成功了,这里创建一个默认用户信息
        Info info = new Info();
        // 设置userInfoId
        String id = randomData.getRandomId();
        info.setUserInfoId(id);
        // 设置默认信息
        String imgUrl = "https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=720805849,3535101081&fm=11&gp=0.jpg";
        // 默认头像
        info.setImgUrl(imgUrl);
        // 默认昵称
        info.setInfoName("kk_" + id);
        // 默认简介
        info.setIntro("暂时没有简介...");
        // 设置默认量
        info.setFansNum(0);
        info.setFocusNum(0);
        info.setPublishNum(0);
        info.setCollectNum(0);

        // 默认时间
        Date createTime = dataFormat.formatDate(new Date());
        info.setCreateTime(createTime);
        info.setUpdateTime(createTime);

        // 将userid和userInfoId存入 userInfo表
        int results = infoMapper.insertUserIdAndUserInfoId(user.getUserId(), info.getUserInfoId());
        if(results <= 0){
            ServerResponse.createByErrorMessage("用户注册失败!");
        }

        // 将用户信息插入用户信息表
        int counts = infoMapper.insertUserInfo(info);
        if(counts <= 0){
            ServerResponse.createByErrorMessage("用户注册失败!");
        }

        // 这里先暂时使用BySuccess,不需要data
        return ServerResponse.createBySuccess("用户注册成功!",user);
    }

    @Override
    public ServerResponse<User> updatePassword(User user) {

        // 验证手机号是否存在
        int result = userMapper.checkUserPhoneNum(user.getPhoneNum());
        if(result <= 0){
            return ServerResponse.createByErrorMessage("该手机号未注册!");
        }
        
        // todo 后续改成调用短信或者邮箱服务接口
        
        String newPassword = user.getPassWord();
        // 根据手机号查询出盐
        String salt = userMapper.selectSaltByPhoneNum(user.getPhoneNum());

        String encodedNewPassword = md5Encry.enCode(newPassword, salt);

        Date updateTime = dataFormat.formatDate(new Date());
        user.setPassWord(encodedNewPassword);
        user.setUpdateTime(updateTime);

        int count = userMapper.updatePassword(user);
        if(count <= 0){
            ServerResponse.createByErrorMessage("修改密码失败!");
        }

        User newUser = userMapper.selectUserByPhoneNum(user.getPhoneNum());
        if(newUser == null){
            ServerResponse.createByErrorMessage("修改密码失败!");
        }
        // 先展示后面隐藏数据
        return ServerResponse.createBySuccess("修改密码成功!",newUser);
    }

    @Override
    public ServerResponse<String> getMessageCode(String phoneNum) {

        String code = randomData.getMsgCode();

        // 调用短信服务接口 并把phoneNum 和 Code 放入缓存
        try {
            SendSmsResponse sendSmsResponse = aliyunUtils.sendSms(phoneNum,code);
            if (sendSmsResponse.getCode() != null && sendSmsResponse.getCode().equals("OK")) {
                System.out.println("短信发送成功！验证码：" + code);
                // 放入缓存
                redisUtils.set(phoneNum,code);
                // 设置缓存过期时间
                redisUtils.expire(phoneNum,60);
                System.out.println(redisUtils.get(phoneNum));

                return ServerResponse.createBySuccess("发送成功!",code);
            } else {
                System.out.println("失败!");
                return ServerResponse.createByErrorMessage("获取验证码失败!");
            }
        } catch (ClientException e) {
            e.printStackTrace();
            return ServerResponse.createByErrorMessage("获取验证码失败!");
        }
    }


}
