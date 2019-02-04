package com.famstack.email;

import org.springframework.mail.javamail.JavaMailSenderImpl;

import com.famstack.projectscheduler.security.hasher.FamstackSecurityTokenManager;


public class FamstackJavaMailSenderImpl extends JavaMailSenderImpl {
	
	private static final String HASHKEY = "NGNW#zcc+N@RY%kSK#46DO+Rzt@j)Ylm";

	@Override
	public void setPassword(String password) {
		super.setPassword(FamstackSecurityTokenManager.decrypt(password, HASHKEY));
	}

}
