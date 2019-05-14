package com.gdwii.response;

/**
 * 基本的错误编码
 */
public enum ErrorCodeBase implements ErrorCode{
    Success(0, "成功"),
    Fail(100, "失败"),
    InvalidParameter(101, "参数非法"),
    ;

    ErrorCodeBase(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    private int code;

    private String msg;

    @Override
    public int code() {
        return code;
    }

    @Override
    public String msg() {
        return msg;
    }
}
