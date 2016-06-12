package com.gdwii.response;

public class DataResult<T> extends Result{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5883148671229077181L;
	
	private T data;

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}
}
