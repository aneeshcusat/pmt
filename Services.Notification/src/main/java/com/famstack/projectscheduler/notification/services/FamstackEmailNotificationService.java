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
            FamstackTemplateEmailInfo famstackTemplateEmailInfoNew = new FamstackTemplateEmailInfo();
            
            famstackTemplateEmailInfoNew.setMailTo(famstackTemplateEmailInfo.getMailTo());
            famstackTemplateEmailInfoNew.setMailBcc(famstackTemplateEmailInfo.getMailBcc());
            famstackTemplateEmailInfoNew.setMailCc(famstackTemplateEmailInfo.getMailCc());
            famstackTemplateEmailInfoNew.setMailFrom(famstackTemplateEmailInfo.getMailFrom());
            
            famstackTemplateEmailInfoNew.setMailSubject(mailSubject);
            famstackTemplateEmailInfoNew.setVelocityTemplateName(templateName);
            famstackTemplateEmailInfoNew.setTemplateParameters(dataMap);

            Set<String> filteredToList =
                getFilteredEmailAddressList( emailNotificationItem.getToList(), emailNotificationItem.getTemplates());
            
            Set<String> filteredCcList =
                    getFilteredEmailAddressList( emailNotificationItem.getCcList(), emailNotificationItem.getTemplates());

            famstackEmailSender.sendEmail(famstackTemplateEmailInfoNew,filteredCcList.toArray(new String[0]), null, filteredToList.toArray(new String[0]));
        } else {
            logDebug("Unable to send emails" + emailNotificationItem.getToList());
        }
    }

    private Set<String> getFilteredEmailAddressList(Set<String> toList, Templates templates)
    {
        if (templates == Templates.RESET_PASSWORD || templates == Templates.FORGOT_PASSWORD
            || templates == Templates.USER_REGISTRAION || templates == Templates.USER_UPDATE || templates == Templates.USER_ACTIVITY_REPORT || templates == Templates.USER_UTILIZATION_REPORT) {
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
