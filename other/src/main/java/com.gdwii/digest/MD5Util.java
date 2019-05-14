package com.gdwii.digest;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.gdwii.converter.DatatypeConverter;

public abstract class MD5Util {
	private static final String ALGORITHM = "MD5";

	public static String digest(String input) {
		return DatatypeConverter.printHexBinary(digest(input.getBytes(StandardCharsets.UTF_8))).toLowerCase();
	}

	private static byte[] digest(byte[] input) {
		try {
			MessageDigest messageDigest = MessageDigest.getInstance(ALGORITHM);
			return messageDigest.digest(input);
		} catch (NoSuchAlgorithmException nsae) {
			throw new InternalError("MD5 not supported", nsae);
		}
	}
}
