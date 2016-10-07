package com.famstack.email.contants;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class EmailMessages {
	private static final String BUNDLE_NAME = "com.famstack.email.contants.EmailMessages"; //$NON-NLS-1$

	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

	private EmailMessages() {
	}

	public static String getString(String key) {
		try {
			return RESOURCE_BUNDLE.getString(key);
		} catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}
}
