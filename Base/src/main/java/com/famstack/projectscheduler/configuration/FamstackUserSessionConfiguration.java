package com.famstack.projectscheduler.configuration;

import java.io.Serializable;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import com.famstack.projectscheduler.datatransferobject.UserItem;
import com.famstack.projectscheduler.security.login.LoginResult;
import com.famstack.projectscheduler.security.login.LoginResult.Status;

/**
 * The Class FamstackUserSessionConfiguration.
 */
@Component
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class FamstackUserSessionConfiguration implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -2779591617048681125L;

	private static final int CURRENT_DAY = 0;

	private static final int PROJECT_VIEW_LIMIT_DAYS = -30;

	/** The login result. */
	private LoginResult loginResult;

	/** The auto refresh. */
	private boolean autoRefresh = true;

	/**
	 * Gets the login result.
	 *
	 * @return the login result
	 */
	public LoginResult getLoginResult() {
		if (loginResult == null) {
			loginResult = new LoginResult();
			loginResult.setStatus(Status.FAILED);
		}
		return loginResult;
	}

	/**
	 * Sets the login result.
	 *
	 * @param loginResult
	 *            the new login result
	 */
	public void setLoginResult(LoginResult loginResult) {
		this.loginResult = loginResult;
	}

	/**
	 * Checks if is auto refresh.
	 *
	 * @return true, if is auto refresh
	 */
	public boolean isAutoRefresh() {
		return autoRefresh;
	}

	/**
	 * Sets the auto refresh.
	 *
	 * @param autoRefresh
	 *            the new auto refresh
	 */
	public void setAutoRefresh(boolean autoRefresh) {
		this.autoRefresh = autoRefresh;
	}

	public UserItem getCurrentUser() {
		return getLoginResult().getUserItem();
	}

	public int getDashboardViewDays() {
		return CURRENT_DAY;
	}

	public int getProjectViewLimit() {
		return PROJECT_VIEW_LIMIT_DAYS;
	}

}
