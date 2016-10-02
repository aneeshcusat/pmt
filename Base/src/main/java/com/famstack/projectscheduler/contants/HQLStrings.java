package com.famstack.projectscheduler.contants;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * The Class HQLStrings.
 */
public class HQLStrings {

	/** The Constant BUNDLE_NAME. */
	private static final String BUNDLE_NAME = "com.famstack.projectscheduler.constants.HQLStrings";

	/** The Constant RESOURCE_BUNDLE. */
	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

	/**
	 * Instantiates a new HQL strings.
	 */
	private HQLStrings() {
	}

	/**
	 * Gets the string.
	 *
	 * @param key
	 *            the key
	 * @return the string
	 */
	public static String getString(String key) {
		try {
			return RESOURCE_BUNDLE.getString(key);
		} catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}
}
