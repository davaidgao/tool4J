<%@page import="com.gdwii.sample.exector.JavaClassExecuter"%>
<%@page import="java.io.FileInputStream"%>
<%@page import="java.io.InputStream"%>
<!-- 演示通过外部上传一个Class文件到服务器,服务器自动执行,并打印出结果 -->
<%
	InputStream is = new FileInputStream("c:/TestClass.class");
	byte[] b = new byte[is.available()];
	is.read(b);
	is.close();
	
	out.println("<textarea style='width:1000;height:800'>");
	out.println(JavaClassExecuter.execute(b));
	out.println("</textarea>");
%>
