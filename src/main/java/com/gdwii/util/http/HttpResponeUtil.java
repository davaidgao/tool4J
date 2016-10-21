package com.gdwii.util.http;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class HttpResponeUtil {
	/**
	 * 请求重定向
	 * @param response
	 * @param url
	 */
	public static void redirect(HttpServletResponse response, String url){
		response.setStatus(HttpServletResponse.SC_MOVED_TEMPORARILY);
		response.addHeader("Location", url);
	}
	
	/**
	 * 请求转发
	 * @param request
	 * @param response
	 * @param url
	 * @throws IOException 
	 * @throws ServletException 
	 */
	public static void forward(HttpServletRequest request, HttpServletResponse response, String url){
		try {
			request.getRequestDispatcher(url).forward(request, response);
		} catch (ServletException|IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	
	/**
	 * 定时刷新页面
	 * @param response
	 * @param url 刷新的页面
	 * @param period 间隔多长时间开始刷新
	 */
	public static void refresh(HttpServletResponse response, String url, int period){
		response.setHeader("Refresh", period + ";" + url);
	}
	
	/**
	 * 设置下载信息
	 * @param request
	 * @param response
	 * @param fileName 下载后用户看到的文件名
	 */
	public static void setDownloadHeader(HttpServletRequest request, HttpServletResponse response, String fileName){
		response.setContentType("application/octet-stream");
		response.addHeader( "Content-Disposition", "attachment;filename=\"" + downloadFileNameProcess(fileName, request)  + "\"");
	}
	
	/**
	 * 为了防止乱码,需要对文件名进行处理
	 * @param fileName 待处理文件名
	 * @param request
	 * @return
	 */
	public static String downloadFileNameProcess(String fileName, HttpServletRequest request){
		String agent = request.getHeader("User-Agent");
		boolean isFireFox = (agent != null && agent.toLowerCase().indexOf("firefox") != -1);
		try{
			if(isFireFox){
				return new String(fileName.getBytes("UTF-8"), "ISO8859-1");
			}else{
				return URLEncoder.encode(fileName, "UTF-8").replace("+", "%20");
			}
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
}