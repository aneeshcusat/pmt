package com.famstack.projectscheduler;

import javax.annotation.Resource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.famstack.projectscheduler.configuration.FamstackApplicationConfiguration;
import com.famstack.projectscheduler.configuration.FamstackUserSessionConfiguration;
import com.famstack.projectscheduler.util.StringUtils;

/**
 * The Class BaseFamstackService.
 */
public class BaseFamstackService
{

    /** The famstack application configuration. */
    @Resource
    FamstackApplicationConfiguration famstackApplicationConfiguration;

    /** The famstack user session configuration. */
    @Resource
    FamstackUserSessionConfiguration famstackUserSessionConfiguration;

    /** The logger. */
    private Logger logger = null;

    /**
     * Log debug.
     *
     * @param message the message
     */
    public void logDebug(String message)
    {
        if (famstackApplicationConfiguration.isLogDebug()) {
            getLogger().debug(message);
        }
    }

    /**
     * Log warning.
     *
     * @param message the message
     */
    public void logWarning(String message)
    {
        getLogger().warn(message);
    }

    /**
     * Log info.
     *
     * @param message the message
     */
    public void logInfo(String message)
    {
        getLogger().info(message);
    }

    /**
     * Log error.
     *
     * @param message the message
     */
    public void logError(String message)
    {
        getLogger().error(message);
    }

    /**
     * Log error.
     *
     * @param message the message
     * @param throwable the throwable
     */
    public void logError(String message, Throwable throwable)
    {
        getLogger().error(message, throwable);
    }

    /**
     * Log trace.
     *
     * @param message the message
     */
    public void logTrace(String message)
    {
        getLogger().trace(message);
    }

    /**
     * Checks if is log info enabled.
     *
     * @return true, if is log info enabled
     */
    public boolean isLogInfoEnabled()
    {
        return getLogger().isInfoEnabled();
    }

    /**
     * Checks if is log warning enabled.
     *
     * @return true, if is log warning enabled
     */
    public boolean isLogWarningEnabled()
    {
        return getLogger().isWarnEnabled();
    }

    /**
     * Gets the logger.
     *
     * @return the logger
     */
    public Logger getLogger()
    {
        if (logger == null) {
            logger = LogManager.getLogger(this.getClass());
        }
        return logger;
    }

    /**
     * Gets the static logger.
     *
     * @param classType the class type
     * @return the static logger
     */
    public static Logger getStaticLogger(Class<?> classType)
    {
        return LogManager.getLogger(classType);
    }

    /**
     * Gets the famstack application configuration.
     *
     * @return the famstack application configuration
     */
    public FamstackApplicationConfiguration getFamstackApplicationConfiguration()
    {
        return famstackApplicationConfiguration;
    }

    /**
     * Gets the famstack user session configuration.
     *
     * @return the famstack user session configuration
     */
    public FamstackUserSessionConfiguration getFamstackUserSessionConfiguration()
    {
        return famstackUserSessionConfiguration;
    }
    
    public String sanitizeCellValue(String value) {

        if( StringUtils.isNotBlank(value)) {
        	if( "=-+@".indexOf(value.charAt(0)) >= 0) {
        		value = value.substring(1);	
        	}
        	System.out.println(value);
        	if( "=-+@".indexOf(value.charAt(0)) >= 0) {
        		value = value.substring(1);		
        	}
        	System.out.println(value);
        	if( "=-+@".indexOf(value.charAt(0)) >= 0) {
        		value = value.substring(1);	
        	}
        	System.out.println(value);
        	if( "=-+@".indexOf(value.charAt(0)) >= 0) {
        		value = value.substring(1);	
        	}
        	System.out.println(value);
        }
        
        return value;
    }
    
    public static void main(String[] args) {
		new BaseFamstackService().sanitizeCellValue("=+-@hello");
	}
}
