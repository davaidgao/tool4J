package com.gdwii.util.http;

import com.gdwii.util.StringUtils;

import java.io.*;
import java.text.ParseException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.sun.corba.se.impl.activation.CommandHandler.parseError;
import static com.sun.xml.internal.bind.WhiteSpaceProcessor.replace;

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
public class RangetDownloadUtil {
	private HttpServletRequest request;
	private HttpServletResponse response;
	private File file;
	private long totalSize;

	/**
	 * 开始下载点
	 */
	private long start;
	
	/**
	 * 截止下载点
	 */
	private long end;
	
	public RangetDownloadUtil(HttpServletRequest request, HttpServletResponse response, File file) {
		this.request = request;
		this.response = response;
		this.file = file;
		totalSize = file.length();
	}


	public void download(){
		processRange(request.getHeader("Range")); // Range表示从哪个位置开始下载
		// 设置头消息
		response.setContentType("application/octet-stream");
		response.setHeader("Content-Length", String.valueOf(end - start + 1));
		response.addHeader( "Content-Disposition", "attachment;filename=\"" + HttpResponeUtil.downloadFileNameProcess(file.getName(), request)  + "\"");

		copyData(); // 拷贝数据
	}

	private void copyData() {
		BufferedInputStream in = null;
		OutputStream out = null;
		try{
			in = new BufferedInputStream(new FileInputStream(file));
			in.skip(start);
			out = response.getOutputStream();

			byte[] b = new byte[1024];
			int len;
			while((len = in.read(b)) > 0){
				out.write(b, 0, len);
				out.flush();
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}catch (IOException e){
			throw new RuntimeException(e);
		}finally {
			if(in != null){
				try {
					in.close();
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
		}
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
	public void processRange(String range){
		// 不存在,则直接将文件全部返回
		if(StringUtils.isEmpty(range)){
			start = 0;
			end = file.length() - 1;
			return ;
		}
		// 设置分段下载的响应消息头
		setHeaders();
		String rangeNum = range.replace("bytes=", "").trim();

		// 是否是数字,如果是数字的话，它必须是负的，否则不满足条件
		// 即满足Range: bytes=-500和Range: -1格式
		if(StringUtils.isNumeric(rangeNum)){
			end = parseRangeNumToLong(rangeNum, range);
			if(end > 0){
				throw new RuntimeException(range + "is not a illegal Range Header format");
			}

			start = totalSize + end;
			end = totalSize - 1;

			if(start < 0){
				throw new RuntimeException();
			}

			return ;
		}

		int strikeIndex = rangeNum.indexOf("-");
		// 中划线不存在,则是不合法的
		if(strikeIndex <= 0){
			throw new RuntimeException(range + "is not a illegal Range Header format");
		}

		start = getRegularRangeIndex(rangeNum.substring(0, strikeIndex), range);

		// 满足格式Range: bytes=1025-
		if(strikeIndex == rangeNum.length() - 1){
			end = totalSize - 1;
			return ;
		}

		// 最终只剩下格式Range: bytes=0-1024
		end = getRegularRangeIndex(rangeNum.substring(strikeIndex + 1), range);

		// 开始下载点不能大于截止下载点
		if(start > end){
			throw new RuntimeException(range + "is not a illegal Range Header format");
		}
	}

	private void setHeaders() {
		 /*
         * Content-Range
         * 		Content-Range: bytes 0-10/3103
         * 这个表示，服务器响应了前(0-10)个字节的数据，该资源一共有(3103)个字节大小。
         */
		response.setHeader("Accept-Ranges", "bytes");
		String contentRange = new StringBuilder("bytes ").append(start).append("-").append(end).append("/").append(totalSize).toString();
		response.setHeader("Content-Range", contentRange);
		response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
	}


	private static long parseRangeNumToLong(String number, String range){
		try{
			return Long.parseLong(number);
		}catch (NumberFormatException e){
			throw new RuntimeException(range + "is not a illegal Range Header format", e);
		}
	}

	private long getRegularRangeIndex(String number, String range){
		long index = parseRangeNumToLong(number, range);

		// 越界检查
		if(index < 0 || index > totalSize){
			throw new RuntimeException("the file's total size is :" + totalSize + ", range(= " + range + ") cross the border");
		}

		return index;
	}
}
