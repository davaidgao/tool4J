package com.gdwii.response;

import java.io.Serializable;

public class Result implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6155977986846833743L;

	private int code;
	
	private String msg;

    public Result(){}

	public Result(int code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	public Result(ErrorCode errorCode) {
		this.code = errorCode.code();
		this.msg = errorCode.msg();
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
}
