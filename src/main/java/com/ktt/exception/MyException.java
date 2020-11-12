package com.ktt.exception;

public class MyException extends RuntimeException {
    private String code;
    private String msg;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public MyException(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public MyException(String msg) {
        this.msg = msg;
    }

    public MyException(String message, String code, String msg) {
        super(message);
        this.code = code;
        this.msg = msg;
    }

    public MyException(String message, Throwable cause, String code, String msg) {
        super(message, cause);
        this.code = code;
        this.msg = msg;
    }

    public MyException(Throwable cause, String code, String msg) {
        super(cause);
        this.code = code;
        this.msg = msg;
    }

    public MyException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, String code, String msg) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.code = code;
        this.msg = msg;
    }
}
