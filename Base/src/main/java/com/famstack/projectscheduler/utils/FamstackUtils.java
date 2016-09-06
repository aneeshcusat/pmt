package com.famstack.projectscheduler.utils;

import java.net.InetAddress;

import com.famstack.projectscheduler.BaseFamstackService;
import com.famstack.projectscheduler.util.StringUtils;

public class FamstackUtils {

    private static final String UNKNOWNSYSTEM = "unknownsystem";

    /** The host name. */
    private static String hostName;

    /**
     * Sets the host name.
     *
     * @return the host name
     */
    public static String getHostName() {
        if (StringUtils.isNotBlank(hostName) && !UNKNOWNSYSTEM.equalsIgnoreCase(hostName)) {
            return hostName;
        }

        try {
            hostName = InetAddress.getLocalHost().getHostName();
        }
        catch (Exception e) {
            BaseFamstackService.getStaticLogger(FamstackUtils.class).error("Unable to get hostname", e);
            hostName = UNKNOWNSYSTEM;
        }
        return hostName;
    }
}
