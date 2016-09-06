package com.famstack.projectscheduler.configuration;

import java.util.Map;

import org.springframework.stereotype.Component;
@Component
public class FamstackApplicationConfiguration {

    private boolean initialized = false;

    private String hostName;

    private int portNumber;

    private String protocol;

	public Map<String, String> getConfigSettings() {
		return null;
	}

}
