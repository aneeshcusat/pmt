package com.famstack.projectscheduler.manager;

import javax.annotation.Resource;

import com.famstack.projectscheduler.BaseFamstackService;
import com.famstack.projectscheduler.dataaccess.FamstackDataAccessObjectManager;

public class BaseFamstackManager extends BaseFamstackService {
	/** The delivery interface data access object manager. */
	@Resource
	FamstackDataAccessObjectManager famstackDataAccessObjectManager;

	public FamstackDataAccessObjectManager getFamstackDataAccessObjectManager() {
		return famstackDataAccessObjectManager;
	}

}
