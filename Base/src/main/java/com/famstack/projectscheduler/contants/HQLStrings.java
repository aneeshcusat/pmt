package com.famstack.projectscheduler.contants;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class HQLStrings {
	private static final String BUNDLE_NAME = "com.famstack.projectscheduler.constants.HQLStrings";

	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

	private HQLStrings() {
	}

	public static String getString(String key) {
		try {
			return RESOURCE_BUNDLE.getString(key);
		} catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}
}
