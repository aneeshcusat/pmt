package com.famstack.projectscheduler.notification.services;

import org.springframework.scheduling.annotation.Async;

import com.famstack.projectscheduler.notification.bean.NotificationItem;

public class FamstackSMSNotificationService extends FamstackBaseNotificationService
{

    @Async
    @Override
    public void notify(NotificationItem notificationItem)
    {

    }

    @Override
    public boolean isEnabled()
    {
        return false;
    }

}
