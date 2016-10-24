package com.gdwii.util.http;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Executor;

import com.gdwii.util.StringUtils;

/**
 * 多线程下载
 * 
 * 思路：
 * 		文件分块。 文件分块大小block = 文件大小 % 线程任务数 == 0 ? 文件大小 / 线程任务 ： 文件大小 / 线程任务 + 1
 * 		确定每一个线程下载对应用文件的位置指针。
 * 			现假设为每个线程分别编号runnableId     0 1 2 3 4
 * 			则第一个线程负责的下载位置是： 0*分块大小  到  (0+1)*分块大小-1
 * 			第二个线程负责的下载位置是： 1*分块大小   到  (1+1)*分块大小-1
 * 			即有开始下载位置 start = runnableId * block;
 * 			即有结束下载位置 end = (runnableId + 1) * block-1;
 * 		最后通过设置连接的属性， conn.setRequestProperty("Range", "bytes=" + start + "-" + end);
 */
public class MultiThreadDownload {
	/**
	 * 下载文件的URL
	 */
	private String url;
	
	/**
	 * 下载的存放路径
	 */
	private String destPath;
	
	/**
	 * 启用几个线程任务
	 */
	private int runnableNum;
	
	/**
	 * 下载文件的尺寸
	 */
	private long size;
	
	/**
	 * 获取每个下载任务下载的文件块大小
	 * -1表示文件不支持分块下载
	 */
	private long block;
	
	public MultiThreadDownload(String url, String destPath, int runnableNum) {
		this.url = url;
		this.destPath = destPath;
		this.runnableNum = runnableNum;
	}
	
	public void download(){
		setDownloadProperties();
		if(block == -1){ // 单线程下载
			new Thread(new SignleThreadDownloadRunnable()).start();
			System.out.println(url + "下载完成");
			return;
		}
		
		// 多线程下载
		createDestFile(destPath, size);
		for(int i = 0; i < runnableNum; i ++){
			new Thread(new PartialDownloadRunnable(i)).start();
		}
		System.out.println(url + "下载完成");
	}
	
	public void download(Executor executor){
		setDownloadProperties();
		if(block == -1){ // 单线程下载
			executor.execute(new SignleThreadDownloadRunnable());
			System.out.println(url + "下载完成");
			return;
		}
		
		// 多线程下载
		createDestFile(destPath, size);
		for(int i = 0; i < runnableNum; i ++){
			executor.execute(new PartialDownloadRunnable(i));
		}
		System.out.println(url + "下载完成");
	}
	
	/**
	 * 设置下载属性
	 * 包含：下载文件大小、分块下载大小
	 */
	private void setDownloadProperties() {
        try {
        	HttpURLConnection conn  = (HttpURLConnection) new URL(url).openConnection(); 
			conn.setRequestMethod("HEAD");
			
			// 下载文件大小
			size = conn.getContentLength();
			if(size <= 0){
				throw new RuntimeException("下载文件" + url + "不存在");
			}
			
			// 设置每个下载任务下载的文件块大小
			String acceptRanges = conn.getHeaderField("Accept-Ranges");
			if(StringUtils.isEmpty(acceptRanges) || !acceptRanges.trim().equals("bytes")){
				block = -1; // 不支持分块下载
			}else{
				block = size / runnableNum;
				block = block * runnableNum == size ? block : block + 1;
			}
		} catch (IOException e) {
			throw new RuntimeException("获取下载文件大小失败", e);
		}
	}
	
	
	private static void createDestFile(String destFile, long length) {
		try (RandomAccessFile file = new RandomAccessFile(destFile, "rw");){
			file.setLength(length);
		} catch (IOException e) {
			throw new RuntimeException("创建下载目标下载文件失败", e);
		}
	}
	
	private class PartialDownloadRunnable implements Runnable{
		private long start; // 开始下载点
		private long end; // 截止下载点
		
		public PartialDownloadRunnable(int currentId) {
			start = currentId * block;
			end = start + block - 1;
			if(end >= size){
				end = size - 1;
			}
		}

		@Override
		public void run() {
			try {
				HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
				conn.setRequestMethod("GET");
				conn.setReadTimeout(5000);

				// 设置要下载的文件块
				conn.setRequestProperty("Range", "bytes=" + start + "-" + end);

				if (conn.getResponseCode() == 206) {
					try(RandomAccessFile destFile = new RandomAccessFile(destPath, "rw");){
						// 移动指针至该线程负责写入数据的位置。
						destFile.seek(start);
						// 读取数据并写入
						InputStream inStream = conn.getInputStream();
						byte[] b = new byte[8192];
						int len = 0;
						while ((len = inStream.read(b)) > 0) {
							destFile.write(b, 0, len);
						}
						System.out.println(url + "从" + start + "到" + end + "下载完成");
					}
				}
			} catch (IOException e) {
				throw new RuntimeException(url + "从" + start + "到" + end + "下载失败", e);
			}
		}
	}
	
	private class SignleThreadDownloadRunnable implements Runnable{
		@Override
		public void run() {
			try {
				HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
				conn.setRequestMethod("GET");
				conn.setReadTimeout(5000);

				if (conn.getResponseCode() == 200) {
					try(OutputStream out = new FileOutputStream(destPath);){
						// 读取数据并写入
						InputStream inStream = conn.getInputStream();
						byte[] b = new byte[8192];
						int len = 0;
						while ((len = inStream.read(b)) > 0) {
							out.write(b, 0, len);
						}
					}
				}
			} catch (IOException e) {
				throw new RuntimeException(url + "下载失败", e);
			}
		}
	}
}
