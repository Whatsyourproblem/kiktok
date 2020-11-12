package com.ktt.utils;

import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.stereotype.Component;

@Component
// 加密相关操作
public class Md5Encry {

    // 生成盐(默认16位)
    public String getSalt(){

        String salt = new SecureRandomNumberGenerator().nextBytes().toString();
        return salt;
    }

    // 加密
    public String enCode(String password,String salt){

        // 设置hash迭代次数
        int times = 2;

        // 得到经过hash加密后的密码
        String encodedPassword = new SimpleHash("md5", password, salt, times).toString();

        return encodedPassword;
    }
}
