package com.gdwii.util.http;

import org.junit.Test;

public class DownloadTest {
	@Test
	public void download() throws InterruptedException{
		new MultiThreadDownload("http://127.0.0.1:7070/http/partalDownload", "d:/download.text", 10).download();
		Thread.sleep(10000);
	}
	
	@Test
	public void downloadSinle() throws InterruptedException{
		new MultiThreadDownload("http://127.0.0.1:7070/http/download", "d:/cache.txt", 10).download();
		Thread.sleep(10000);
	}
}
