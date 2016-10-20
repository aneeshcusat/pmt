package com.famstack.email.velocity;

import java.io.StringWriter;
import java.util.Map;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import com.famstack.projectscheduler.BaseFamstackService;

/**
 * The Class famstackVelocityService.
 * 
 * @author Aneeshkumar
 * @version 1.0
 */
public class FamstackVelocityService extends BaseFamstackService {

    /** The Constant LOG_TAG. */
    private static final String LOG_TAG = "evaluate";

    /** The Constant PLACEHOLDER. */
    public static final String PLACEHOLDER = "${";

    /**
     * Evaluate string.
     *
     * @param evaluatee the evaluatee
     * @param data the data
     * @return the string
     */
    public String evaluateString(String evaluatee, Map<String, Object> data) {
        if (evaluatee.contains(PLACEHOLDER)) {
            StringWriter writer = createWriter();
            evaluate(evaluatee, data, writer);
            return writer.toString();
        }
        return evaluatee;
    }

    /**
     * Evaluate template.
     *
     * @param templateURL the template URL
     * @param data the data
     * @return the string
     */
    public String evaluateTemplate(String templateURL, Map<String, Object> data) {
        final VelocityContext context = createVelocityContext(data);
        final StringWriter writer = createWriter();
        getTemplate(templateURL).merge(context, writer);
        return writer.toString();
    }

    /**
     * Creates the writer.
     *
     * @return the string writer
     */
    protected StringWriter createWriter() {
        return new StringWriter();
    }

    /**
     * Evaluate.
     *
     * @param evaluatee the evaluatee
     * @param data the data
     * @param writer the writer
     */
    protected void evaluate(String evaluatee, Map<String, Object> data, StringWriter writer) {
        Velocity.evaluate(new VelocityContext(data), writer, LOG_TAG, evaluatee);
    }

    /**
     * Gets the template.
     *
     * @param templateUrl the template url
     * @return the template
     */
    protected Template getTemplate(String templateUrl) {
        return VelocityInitService.getVelocityEngine().getTemplate(templateUrl);
    }

    /**
     * Creates the velocity context.
     *
     * @param templateData the template data
     * @return the velocity context
     */
    protected VelocityContext createVelocityContext(Map<String, Object> templateData) {
        return new VelocityContext(templateData);
    }
}
