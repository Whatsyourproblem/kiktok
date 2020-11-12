package com.ktt.service;

import com.ktt.common.ServerResponse;
import com.ktt.pojo.User;
import com.ktt.vo.UserInformation;
import org.apache.ibatis.annotations.Select;

import javax.servlet.http.HttpSession;

public interface UserService {

    ServerResponse<UserInformation> login(String phoneNum, String passWord);

    ServerResponse<User> register(String code,User user);

    ServerResponse<User> updatePassword(User user);

    // 获取验证码
    ServerResponse<String> getMessageCode(String phoneNum);

}
