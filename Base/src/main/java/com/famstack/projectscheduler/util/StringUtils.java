package com.famstack.projectscheduler.util;

/**
 * The Class StringUtils.
 * 
 * @author Aneeshkumar
 * @version 1.0
 */
public class StringUtils {

	/**
	 * Checks if is not blank.
	 *
	 * @param value
	 *            the value
	 * @return true, if is not blank
	 */
	public static boolean isNotBlank(String value) {
		return value != null && value.length() > 0 ? true : false;
	}
}
