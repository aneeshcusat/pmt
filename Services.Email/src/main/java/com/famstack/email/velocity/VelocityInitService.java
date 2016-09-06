package com.famstack.email.velocity;

import java.util.Properties;

import org.apache.velocity.app.VelocityEngine;

/**
 * The Class VelocityInitService.
 * 
 * @author Aneeshkumar
 * @version 1.0
 */
public class VelocityInitService {

    /** The Constant FILE_RESOURCE_LOADER_CLASS_PROPERTY. */
    private static final String FILE_RESOURCE_LOADER_CLASS_PROPERTY = "class.resource.loader.class";

    /** The Constant RESOURCE_LOADER_PROPERTY. */
    private static final String RESOURCE_LOADER_PROPERTY = "resource.loader";

    /** The Constant RESOURCE_LOADER_CLASS. */
    private static final String RESOURCE_LOADER_CLASS = "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader";

    /** The Constant RESOURCE_LOADER_TYPE. */
    private static final String RESOURCE_LOADER_TYPE = "class";

    /** The template dir path. */
    private String templateDirPath;

    /** The velocity engine. */
    private static VelocityEngine velocityEngine;

    /**
     * Gets the template dir path.
     *
     * @return the template dir path
     */
    public String getTemplateDirPath() {
        return templateDirPath;
    }

    /**
     * Sets the template dir path.
     *
     * @param templateDirPath the new template dir path
     */
    public void setTemplateDirPath(String templateDirPath) {
        this.templateDirPath = templateDirPath;
    }

    /**
     * Gets the velocity engine.
     *
     * @return the velocity engine
     */
    public static VelocityEngine getVelocityEngine() {
        if (velocityEngine == null) {
            velocityEngine = new VelocityEngine();
            Properties properties = new Properties();
            properties.put(RESOURCE_LOADER_PROPERTY, RESOURCE_LOADER_TYPE);
            properties.put(FILE_RESOURCE_LOADER_CLASS_PROPERTY, RESOURCE_LOADER_CLASS);
            velocityEngine.init(properties);
        }
        return velocityEngine;
    }
}
