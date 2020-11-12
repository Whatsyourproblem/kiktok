package com.ktt.utils;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.FileInputStream;
import java.io.IOException;

@Component
public class QiniuUtils {



    // 构造一个带指定Zone对象的配置类, 注意这里的Zone.zone0需要根据主机选择
    Configuration cfg = new Configuration(Zone.zone2());




    public String postUserInforUpDate(HttpServletRequest request, MultipartFile file,String id){

        // 获取文件的后缀名
        String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);
        System.out.println(suffix);

        // 用来获取其他参数
        MultipartHttpServletRequest params = ((MultipartHttpServletRequest) request);
        if (!file.isEmpty()) {
            FileInputStream inputStream = null;
            try {
                inputStream = (FileInputStream) file.getInputStream();
                return uploadQNVideo(inputStream,id);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "上传失败";
    }



    /**
     * 将视频传到七牛云
     */
    public String uploadQNVideo(FileInputStream file, String key) {
        // 构造一个带指定Zone对象的配置类, 注意这里的Zone.zone0需要根据主机选择
        Configuration cfg = new Configuration(Zone.zone2());
        // 其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);
        // 生成上传凭证，然后准备上传

        try {
            //    Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
            String upToken = auth.uploadToken(BUCKETNAME);
            try {
                Response response = uploadManager.put(file, key, upToken, null, null);
                // 解析上传成功的结果
                DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);

                String url = "http://" + DOMAIN + "/" + putRet.key;
                // 这个returnPath是获得到的外链地址,通过这个地址可以直接打开视频
                return url;
            } catch (QiniuException ex) {
                Response r = ex.response;
                System.err.println(r.toString());
                try {
                    System.err.println(r.bodyString());
                } catch (QiniuException ex2) {
                    //ignore
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


    //删除文件
    public int deleteFileFromQiniu(String url){
        //构造一个带指定Zone对象的配置类
        Configuration cfg = new Configuration(Zone.zone2());
        String key = url;
        Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
        BucketManager bucketManager = new BucketManager(auth, cfg);
        try {
            Response delete = bucketManager.delete(BUCKETNAME, key);
            return delete.statusCode;
        } catch (QiniuException ex) {
            //如果遇到异常，说明删除失败
            ex.printStackTrace();
            System.err.println(ex.code());
            System.err.println(ex.response.toString());
        }
        return -1;
    }
}
