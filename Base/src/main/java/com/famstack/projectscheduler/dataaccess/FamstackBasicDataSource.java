package com.famstack.projectscheduler.dataaccess;

import org.apache.commons.dbcp.BasicDataSource;

import com.famstack.projectscheduler.security.hasher.FamstackSecurityTokenManager;

public class FamstackBasicDataSource extends BasicDataSource {

	private static final String HASHKEY = "NGNW#zcc+N@RY%kSK#46DO+Rzt@j)Ylm";
	
	@Override
	public void setPassword(String password) {
		super.setPassword(FamstackSecurityTokenManager.decrypt(password, HASHKEY));
	}

}
