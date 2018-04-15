package com.famstack.projectscheduler.notification.services;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.famstack.email.FamstackEmailSender;
import com.famstack.email.contants.EmailMessages;
import com.famstack.email.contants.Templates;
import com.famstack.email.util.FamstackTemplateEmailInfo;
import com.famstack.projectscheduler.notification.bean.EmailNotificationItem;
import com.famstack.projectscheduler.notification.bean.NotificationItem;
import com.famstack.projectscheduler.util.StringUtils;

@Component
public class FamstackEmailNotificationService extends FamstackBaseNotificationService
{

    @Resource
    FamstackEmailSender famstackEmailSender;

    @Resource
    FamstackTemplateEmailInfo famstackTemplateEmailInfo;

    /**
     * Send email notification for error.
     * 
     * @param orderExceptionState the order exception state
     * @param dataObjMap the data map
     * @param deliveryOrderItem the delivery order item
     */
    @Async
    @Override
    public void notify(NotificationItem notificationItem)
    {

        EmailNotificationItem emailNotificationItem = (EmailNotificationItem) notificationItem;
        if (!emailNotificationItem.getToList().isEmpty() && emailNotificationItem.isEmailEnabled()) {
            Map<String, Object> dataMap = emailNotificationItem.getData();

            String templateName = emailNotificationItem.getTemplates().getTemplate();

            if (!StringUtils.isNotBlank(templateName)) {
                templateName = Templates.DEFAULT.getTemplate();
            }

            String mailSubject = getNotificationSubject(emailNotificationItem.getTemplates());

            famstackTemplateEmailInfo.setMailSubject(mailSubject);
            famstackTemplateEmailInfo.setVelocityTemplateName(templateName);
            famstackTemplateEmailInfo.setTemplateParameters(dataMap);

            Set<String> filteredToList =
                getFilteredEmailToList(emailNotificationItem, emailNotificationItem.getTemplates());

            famstackEmailSender.sendEmail(famstackTemplateEmailInfo, filteredToList.toArray(new String[0]));
        } else {
            logDebug("Unable to send emails" + emailNotificationItem.getToList());
        }
    }

    private Set<String> getFilteredEmailToList(EmailNotificationItem emailNotificationItem, Templates templates)
    {
        Set<String> toList = emailNotificationItem.getToList();

        if (templates == Templates.RESET_PASSWORD || templates == Templates.FORGOT_PASSWORD
            || templates == Templates.USER_REGISTRAION || templates == Templates.USER_UPDATE) {
            return toList;
        }

        Set<String> filteredToList = new HashSet<>();
        if (toList != null) {
            for (String userId : toList) {
                if (!getFamstackApplicationConfiguration().isEmailDisabled(userId)) {
                    filteredToList.add(userId);
                } else {
                    logInfo("Email Disalbed for : " + userId);
                }
            }
        }
        return filteredToList;
    }

    private String getNotificationSubject(Templates emailTemplate)
    {
        return EmailMessages.getString(emailTemplate.getSubjectkey());
    }

    @Override
    public boolean isEnabled()
    {
        return getFamstackApplicationConfiguration().isEmailAllEnabled();
    }

}
