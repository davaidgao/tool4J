package com.gdwii.converter;

/**
 * 数据类型转换
 * 是jdk的 javax.xml.bind.DatatypeConverter的模拟
 */
public class DatatypeConverter {
	private static final char[] hexCode = "0123456789ABCDEF".toCharArray();
	
	public static String printHexBinary(byte[] data) {
		StringBuilder r = new StringBuilder(data.length * 2);
		for (byte b : data) {
			r.append(hexCode[(b >> 4) & 0xF]);
			r.append(hexCode[(b & 0xF)]);
		}
		return r.toString();
	}
}
