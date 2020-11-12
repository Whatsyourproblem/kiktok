package com.ktt.utils;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class AliyunUtils {



    /* 短信发送 */
    public SendSmsResponse sendSms(String phone,String code) throws ClientException {

        /* 超时时间，可自主调整 */
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");

        /* 初始化acsClient,暂不支持region化 */
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
        DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
        IAcsClient acsClient = new DefaultAcsClient(profile);

        /* 组装请求对象-具体描述见控制台-文档部分内容 */
        SendSmsRequest request = new SendSmsRequest();
        /* 必填:待发送手机号 */
        request.setPhoneNumbers(phone);
        /* 必填:短信签名-可在短信控制台中找到 */
        request.setSignName("kiktok小程序"); //TODO: 这里是你短信签名
        /* 必填:短信模板code-可在短信控制台中找到 */
        request.setTemplateCode("SMS_205405339"); //TODO: 这里是你的模板code
        /* 可选:模板中的变量替换JSON串,如模板内容为"亲爱的用户,您的验证码为${code}"时,此处的值为 */
        request.setTemplateParam("{\"code\":\"" + code + "\"}");
        // hint 此处可能会抛出异常，注意catch
        SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);
        return sendSmsResponse;
    }


}