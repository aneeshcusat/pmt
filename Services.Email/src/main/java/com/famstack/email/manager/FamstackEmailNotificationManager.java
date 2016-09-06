package com.famstack.email.manager;

import java.util.Map;

import javax.annotation.Resource;

import com.famstack.email.FamstackEmailSender;
import com.famstack.email.util.FamstackTemplateEmailInfo;

public class FamstackEmailNotificationManager {

    /** The delivery interface email sender. */
    @Resource
    FamstackEmailSender famstackEmailSender;

    /** The delivery interface template email info. */
    @Resource
    FamstackTemplateEmailInfo famstackTemplateEmailInfo;

    /**
     * Send email notification for error.
     *
     * @param orderExceptionState the order exception state
     * @param dataObjMap the data map
     * @param deliveryOrderItem the delivery order item
     */
    public void sendEmailNotificationForError(Map<String, Object> dataObjMap) {
        Map<String, Object> dataMap = dataObjMap;
        famstackTemplateEmailInfo.setVelocityTemplateName("famstacktNotification");
        famstackTemplateEmailInfo.setTemplateParameters(dataMap);
        famstackEmailSender.sendEmail(famstackTemplateEmailInfo);
        }

}
