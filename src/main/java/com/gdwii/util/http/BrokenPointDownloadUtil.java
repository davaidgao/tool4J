package com.gdwii.util.http;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 分段下载实现
 * 
 * 现代WEB服务器都支持大文件分段下载,加快下载速度.
 * 判断WEB服务器是否支持分段下载通过返回头是否有 Accept-Ranges: bytes 字段。
 * 
 * 分段下载分为两种: 一种就是一次请求一个分段，一种就是一次请求多个分段。
 * @author gdwii
 *
 */
public class BrokenPointDownloadUtil {
	private HttpServletRequest request;
	private HttpServletResponse response;
	private File file;
	
	/**
	 * 开始下载点
	 */
	private long start;
	
	/**
	 * 截止下载点
	 */
	private long end;
	
	public BrokenPointDownloadUtil(HttpServletRequest request, HttpServletResponse response, File file) {
		this.request = request;
		this.response = response;
		this.file = file;
	}

	public void download(){
		processRange();
		
//		long finisNum = finishNum(request);
//		setHeaders(request, response, file, finisNum);
		
		
		
		
	}
	
	private static void setHeaders(HttpServletRequest request, HttpServletResponse response, File file, long finishNum) {
		long size = file.length();
		
		response.setContentType("application/x-download; charset=utf-8");
		response.setHeader("Accept-Ranges", "bytes");    
        response.setHeader("Content-Length", String.valueOf(size));    
        response.addHeader( "Content-Disposition", "attachment;filename=\"" + HttpResponeUtil.downloadFileNameProcess(file.getName(), request)  + "\"");
        response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT); 
        
        /*
         * Content-Range
         * 		Content-Range: bytes 0-10/3103
         * 这个表示，服务器响应了前(0-10)个字节的数据，该资源一共有(3103)个字节大小。
         */
//        StringBuilder contentRange = new StringBuilder("bytes ").append(finishNum).append("-").append(size).append("/").append(fSize+"").toString();  
//        response.setHeader("Content-Range", contentRange);  
	}

	/**
	 * 处理Range,获取从那下载,下载多少
	 * 
	 * Range 请求头格式
	 * 		Range: bytes=start-end
	 * 这个表示[start,end]，即是包含请求头的start及end字节的，所以，下一个请求，应该是上一个请求的[end+1, nextEnd]
	 * 
	 * Range字段支持的写法
	 * 		Range: bytes=0-1024 获取最前面1025个字节
	 * 		Range: bytes=-500   获取最后500个字节
	 * 		Range: bytes=1025-  获取从1025开始到文件末尾所有的字节
	 * 		Range: 0-0          获取第一个字节
	 * 		Range: -1           获取最后一个字节
	 * 
	 * @return 
	 */
	private void processRange(){
		String range = request.getHeader("Range");
//		if(range == null){
//			return 0;
//		}
//		try{
//			return Long.parseLong(range.substring(range.indexOf("-") + 1).trim());
//		}catch(NumberFormatException e){
//			throw new RuntimeException("Range:" + range, e);
//		}
		
//		http://www.cnblogs.com/ourroad/p/4837271.html
//		http://cuisuqiang.iteye.com/blog/2095644?utm_source=tuicool&utm_medium=referral
	}
}
