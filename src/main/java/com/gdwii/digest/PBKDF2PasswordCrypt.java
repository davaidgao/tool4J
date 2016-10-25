package com.gdwii.digest;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.xml.bind.DatatypeConverter;

/**
 * 创建和验证PBKDF2加密的密码
 * 生成PBKDF2密码采用Base64编码
 * @author gdwwii
 */
public class PBKDF2PasswordCrypt {
	private static final String ALGORITHM = "PBKDF2WithHmacSHA1"; // 采取的加密算法
	public static final int SALT_BYTE_SIZE = 24; // 盐的长度
	public static final int HASH_BYTE_SIZE = 18; // 生成的密码的长度
	public static final int PBKDF2_ITERATIONS = 1000; // 迭代次数
	
	private static final String ALGORITHM_SIGN = "sha1"; // 生成密码的PBKDF2算法的标记
	private static final String PASSWORD_SEPARATOR = ":"; // 生成密码的各项分隔符
	private static final int PASSWORD_ITEM_NUM = 4; // 生成密码有几项构成
	private static final int ALGORITHM_SIGN_INDEX = 0; // 算法标记在生成密码的第几项
	private static final int ITERATIONS_INDEX = 1; // 迭代次数在生成密码的第几项
	private static final int SALT_INDEX = 2; // 盐在生成密码的第几项
	private static final int HASH_INDEX = 3; // Hash结果在生成密码的第几项
	
	/**
	 * 生成Hash后的password
	 * @param password
	 * @return 格式为：PBKDF2底层使用的算法:迭代次数:盐:Hash值
	 */
	public static String generate(String password){
		byte[] salt = generateSalt();
		byte[] hash = pbkdf2(password.toCharArray(), salt, PBKDF2_ITERATIONS, HASH_BYTE_SIZE);
		return ALGORITHM_SIGN + PASSWORD_SEPARATOR + PBKDF2_ITERATIONS + PASSWORD_SEPARATOR 
				+ toBase64(salt) + PASSWORD_SEPARATOR + toBase64(hash);
	}
	
	public static boolean verify(String password, String hashPassword){
		String[] hashParameter = hashPassword.split(PASSWORD_SEPARATOR);
		if(hashParameter.length != PASSWORD_ITEM_NUM){
			throw new PBKDF2PasswordException("Fields are missing from the password hash.");
		}
		
		if(!hashParameter[ALGORITHM_SIGN_INDEX].equals(ALGORITHM_SIGN)){
			throw new PBKDF2PasswordException("Unsupported hash type.");
		}
		
		int iterationCount = 0;
		try{
			iterationCount = Integer.parseInt(hashParameter[ITERATIONS_INDEX]);
		}catch(NumberFormatException e){
			throw new PBKDF2PasswordException("Could not parse the iteration count as an integer.", e);
		}
		if(iterationCount < 1){
			throw new PBKDF2PasswordException("Invalid number of iterations. Must be >= 1.");
		}
		
		byte[] salt = null;
		try {
			salt = fromBase64(hashParameter[SALT_INDEX]);
		} catch (IllegalArgumentException ex) {
			throw new PBKDF2PasswordException("Base64 decoding of salt failed.", ex);
		}

		byte[] hash = null;
		try {
			hash = fromBase64(hashParameter[HASH_INDEX]);
		} catch (IllegalArgumentException ex) {
			throw new PBKDF2PasswordException("Base64 decoding of pbkdf2 output failed.", ex);
		}
		
		// Compute the hash of the provided password, using the same salt,
		// iteration count, and hash length
		byte[] testHash = pbkdf2(password.toCharArray(), salt, iterationCount, hash.length);
		
		// Compare the hashes in constant time. The password is correct if
		// both hashes match.
		return slowEquals(hash, testHash);
	}
	
	private static boolean slowEquals(byte[] hash, byte[] testHash) {
		int diff = hash.length ^ testHash.length;
		
		for(int i = 0; i < hash.length; i ++){
			diff |= hash[i] ^ testHash[i];
		}
		
		return diff == 0;
	}

	private static byte[] pbkdf2(char[] password, byte[] salt, int iterationCount, int keyLength){
		PBEKeySpec keySpec = new PBEKeySpec(password, salt, iterationCount, keyLength * 8);
		try {
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM);
			return keyFactory.generateSecret(keySpec).getEncoded();
		} catch (NoSuchAlgorithmException ex) {
			throw new PBKDF2PasswordException("Hash algorithm not supported.", ex);
		} catch (InvalidKeySpecException ex) {
			throw new PBKDF2PasswordException("Invalid key spec.", ex);
		}
	}
	
	private static byte[] generateSalt(){
		byte[] salt = new byte[SALT_BYTE_SIZE];
		SecureRandom secureRandom = new SecureRandom();
		secureRandom.nextBytes(salt);
		return salt;
	}
	
	private static String toBase64(byte[] val) {
		return DatatypeConverter.printBase64Binary(val);
	}
	
	private static byte[] fromBase64(String val) {
		return DatatypeConverter.parseBase64Binary(val);
	}
	
	private static class PBKDF2PasswordException extends RuntimeException{
		private static final long serialVersionUID = -1412964202216087336L;
		public PBKDF2PasswordException(String message) {
			super(message);
		}
		public PBKDF2PasswordException(String message, Throwable cause) {
			super(message, cause);
		}
	}
}
