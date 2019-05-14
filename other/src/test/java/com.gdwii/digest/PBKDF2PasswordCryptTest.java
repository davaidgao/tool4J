package com.gdwii.digest;

import static org.junit.Assert.*;

import org.junit.Test;

public class PBKDF2PasswordCryptTest {

	@Test
	public void verifyTrue() {
		String password = "test";
		String cryptPassword = PBKDF2PasswordCrypt.generate(password);
		assertTrue(PBKDF2PasswordCrypt.verify(password, cryptPassword));
	}
	
	@Test
	public void verifyFalse() {
		String password = "test";
		String cryptPassword = PBKDF2PasswordCrypt.generate(password);
		assertFalse(PBKDF2PasswordCrypt.verify("test1", cryptPassword));
	}
}
