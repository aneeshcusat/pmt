package com.famstack.projectscheduler.utils;

import java.io.IOException;
import java.net.InetAddress;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.famstack.projectscheduler.BaseFamstackService;
import com.famstack.projectscheduler.util.StringUtils;

public class FamstackUtils extends BaseFamstackService {

	private static final String UNKNOWNSYSTEM = "unknownsystem";

	/** The host name. */
	private static String hostName;

	/**
	 * Sets the host name.
	 *
	 * @return the host name
	 */
	public static synchronized String getHostName() {
		if (StringUtils.isNotBlank(hostName) && !UNKNOWNSYSTEM.equalsIgnoreCase(hostName)) {
			return hostName;
		}

		try {
			hostName = InetAddress.getLocalHost().getHostName();
		} catch (Exception e) {
			BaseFamstackService.getStaticLogger(FamstackUtils.class).error("Unable to get hostname", e);
			hostName = UNKNOWNSYSTEM;
		}
		return hostName;
	}

	public static synchronized String getJsonFromObject(Object dataObject) {
		String jsonString = null;
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			jsonString = objectMapper.writeValueAsString(dataObject);
		} catch (JsonGenerationException e) {
			getStaticLogger(FamstackUtils.class).error(e.getMessage());
		} catch (JsonMappingException e) {
			getStaticLogger(FamstackUtils.class).error(e.getMessage());
		} catch (IOException e) {
			getStaticLogger(FamstackUtils.class).error(e.getMessage());
		}
		return jsonString;
	}
}
