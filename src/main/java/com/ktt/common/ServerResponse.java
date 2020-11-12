package com.ktt.common;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.codehaus.jackson.annotate.JsonIgnore;

import java.io.Serializable;

// 自定义响应类型
public class ServerResponse<T> implements Serializable {

    private int status;
    private String msg;
    private T data;

    private ServerResponse(int status){
        this.status = status;
    }

    private ServerResponse(int status,T data){
        this.status = status;
        this.data = data;
    }

    private ServerResponse(int status,String msg,T data){
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    private ServerResponse(int status,String msg){
        this.status = status;
        this.msg = msg;
    }

    @JsonIgnore
    // 使一下内容不在json序列化结果当中
    public boolean isSuccess(){
        return this.status == ResponseCode.SUCCESS.getCode();
    }

    public int getStatus(){
        return status;
    }

    public T getData(){
        return data;
    }

    public String getMsg(){
        return msg;
    }

    // 返回成功创建的文本,供前台使用

    // 返回成功的状态码
    public static <T> ServerResponse<T> createBySuccessCode(){
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode());
    }

    // 返回成功的状态码和信息
    public static <T> ServerResponse<T> createBySuccessMessage(String msg){
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode(),msg);
    }

    // 返回成功的状态码和数据
    public static <T> ServerResponse<T> createBySuccessData(T data){
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode(),data);
    }

    // 返回状态码和信息以及数据
    public static <T> ServerResponse<T> createBySuccess(String msg,T data){
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode(),msg,data);
    }

    // 返回失败的文本

    // 返回失败的状态码
    public static <T> ServerResponse<T> createByErrorCode(){
        return new ServerResponse<T>(ResponseCode.ERROR.getCode());
    }

    // 返回失败的状态码和信息
    public static <T> ServerResponse<T> createByErrorMessage(String msg){
        return new ServerResponse<T>(ResponseCode.ERROR.getCode(),msg);
    }

    // 自定义返回状态码和错误信息
    public static <T> ServerResponse<T> createByError(int errorCode,String errorMessage){
        return new ServerResponse<T>(errorCode,errorMessage);
    }
}
