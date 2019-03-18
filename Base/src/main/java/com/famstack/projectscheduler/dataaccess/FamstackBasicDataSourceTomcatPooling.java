package com.famstack.projectscheduler.dataaccess;

import org.apache.tomcat.jdbc.pool.DataSource;

import com.famstack.projectscheduler.security.hasher.FamstackSecurityTokenManager;

public class FamstackBasicDataSourceTomcatPooling extends DataSource {

	private static final String HASHKEY = "NGNW#zcc+N@RY%kSK#46DO+Rzt@j)Ylm";
	
	@Override
	public void setPassword(String password) {
		super.setPassword(FamstackSecurityTokenManager.decrypt(password, HASHKEY));
	}

}
