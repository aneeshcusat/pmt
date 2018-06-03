package com.famstack.projectscheduler.configuration;

import java.io.Serializable;
import java.util.Date;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import com.famstack.projectscheduler.contants.DashBoardDateRange;
import com.famstack.projectscheduler.datatransferobject.UserItem;
import com.famstack.projectscheduler.security.login.LoginResult;
import com.famstack.projectscheduler.security.login.LoginResult.Status;
import com.famstack.projectscheduler.util.DateTimePeriod;
import com.famstack.projectscheduler.util.DateUtils;

/**
 * The Class FamstackUserSessionConfiguration.
 */
@Component
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class FamstackUserSessionConfiguration implements Serializable
{

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -2779591617048681125L;

    private static final int CURRENT_DAY = 0;

    private static final int PROJECT_VIEW_LIMIT_DAYS = -30;

    /** The login result. */
    private LoginResult loginResult;

    private Date userLastActivityDate;

    /** The auto refresh. */
    private boolean autoRefresh = true;

    private DashBoardDateRange dashBoardDateRange;

    private String userGroupIdSelection;

    /**
     * Gets the login result.
     * 
     * @return the login result
     */
    public LoginResult getLoginResult()
    {
        if (loginResult == null) {
            loginResult = new LoginResult();
            loginResult.setStatus(Status.FAILED);
        }
        return loginResult;
    }

    /**
     * Sets the login result.
     * 
     * @param loginResult the new login result
     */
    public void setLoginResult(LoginResult loginResult)
    {
        this.loginResult = loginResult;
    }

    /**
     * Checks if is auto refresh.
     * 
     * @return true, if is auto refresh
     */
    public boolean isAutoRefresh()
    {
        return autoRefresh;
    }

    /**
     * Sets the auto refresh.
     * 
     * @param autoRefresh the new auto refresh
     */
    public void setAutoRefresh(boolean autoRefresh)
    {
        this.autoRefresh = autoRefresh;
    }

    public UserItem getCurrentUser()
    {
        return getLoginResult().getUserItem();
    }

    public int getProjectViewLimit()
    {
        return PROJECT_VIEW_LIMIT_DAYS;
    }

    public DashBoardDateRange getDashBoardDateRange()
    {
        return dashBoardDateRange;
    }

    public void setDashBoardDateRange(DashBoardDateRange dashBoardDateRange)
    {
        this.dashBoardDateRange = dashBoardDateRange;
    }

    public Date getDashboardViewStartDate()
    {
        return DateUtils.getNextPreviousDate(DateTimePeriod.DAY_START, new Date(), CURRENT_DAY);
    }

    public Date getDashboardViewEndDate()
    {
        return DateUtils.getNextPreviousDate(DateTimePeriod.DAY_END, new Date(), CURRENT_DAY);
    }

    public String getUserGroupId()
    {
        if (userGroupIdSelection == null) {

            UserItem userItem = getCurrentUser();
            if (userItem != null) {
                return userItem.getUserGroupId();
            }
        } else {
            return userGroupIdSelection;
        }
        return "";
    }

    public String getUserGroupIdSelection()
    {
        return userGroupIdSelection;
    }

    public void setUserGroupIdSelection(String userGroupIdSelection)
    {
        this.userGroupIdSelection = userGroupIdSelection;
    }

    public Date getUserLastActivityDate()
    {
        return userLastActivityDate;
    }

    public void setUserLastActivityDate(Date userLastActivityDate)
    {
        this.userLastActivityDate = userLastActivityDate;
    }

}
