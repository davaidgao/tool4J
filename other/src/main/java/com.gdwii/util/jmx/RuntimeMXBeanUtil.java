package com.gdwii.util.jmx;

import java.lang.management.ManagementFactory;

public class RuntimeMXBeanUtil {
	/**
	 * 返回JVM运行的pid
	 * @return
	 */
	public static String getPid(){
		String name = ManagementFactory.getRuntimeMXBean().getName();
		return name.substring(0, name.indexOf('@'));
	}
}
