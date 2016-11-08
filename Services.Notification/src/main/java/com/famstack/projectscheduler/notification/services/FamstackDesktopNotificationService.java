package com.famstack.projectscheduler.notification.services;

import java.util.HashMap;
import java.util.Map;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.famstack.projectscheduler.employees.bean.GroupMessageDetails;
import com.famstack.projectscheduler.notification.bean.NotificationItem;
import com.famstack.projectscheduler.util.LimitedQueue;

@Component
public class FamstackDesktopNotificationService extends FamstackBaseNotificationService
{

    private final Map<Integer, LimitedQueue<NotificationItem>> userNotificationMap = new HashMap<>();

    private final Map<Integer, Map<Integer, LimitedQueue<GroupMessageDetails>>> userMessageNotificationMap =
        new HashMap<>();

    private final int notificatioSize = 50;

    private final int messageNotificatioSize = 100;

    @Async
    @Override
    public void notify(NotificationItem notificationItem)
    {
        if (notificationItem != null) {
            logDebug("adding desktop notification for " + notificationItem.getNotificationType());
            for (Integer userId : notificationItem.getSubscriberList()) {
                LimitedQueue<NotificationItem> notificationQueue = userNotificationMap.get(userId);
                if (notificationQueue == null) {
                    logDebug("creating a new map for the user" + userId);
                    notificationQueue = new LimitedQueue<NotificationItem>(notificatioSize);
                    userNotificationMap.put(userId, notificationQueue);
                }
                notificationQueue.add((NotificationItem) notificationItem.clone());
            }
        }
    }

    public LimitedQueue<NotificationItem> getNotificatioItems(int userId)
    {
        logDebug("getting notification for user " + userId);
        LimitedQueue<NotificationItem> notificationQueue = userNotificationMap.get(userId);
        if (notificationQueue != null) {
            return notificationQueue;
        }
        return null;
    }

    @Async
    public void notifyMessage(GroupMessageDetails groupMessageDetails, int[] subscriberIds)
    {
        if (groupMessageDetails != null) {
            int currentUserId = groupMessageDetails.getUser();
            logDebug("adding message notificaion " + subscriberIds);
            logDebug("adding message notificaion current user" + currentUserId);

            if (subscriberIds.length > 0) {
                for (Integer subscriberId : subscriberIds) {
                    if (subscriberId != currentUserId) {
                        Map<Integer, LimitedQueue<GroupMessageDetails>> userMessageMap =
                            userMessageNotificationMap.get(subscriberId);

                        if (userMessageMap == null) {
                            userMessageMap = new HashMap<Integer, LimitedQueue<GroupMessageDetails>>();
                            userMessageNotificationMap.put(subscriberId, userMessageMap);
                        }

                        logDebug("adding desktop message notification for " + currentUserId);
                        int groupId = groupMessageDetails.getGroup();
                        LimitedQueue<GroupMessageDetails> messageNotificationQueue = userMessageMap.get(groupId);
                        if (messageNotificationQueue == null) {
                            logDebug("creating a new map for the group" + groupId);
                            messageNotificationQueue = new LimitedQueue<GroupMessageDetails>(messageNotificatioSize);
                            userMessageMap.put(groupId, messageNotificationQueue);
                        }
                        groupMessageDetails.setRead(false);
                        messageNotificationQueue.add((GroupMessageDetails) groupMessageDetails.clone());
                    }

                }
            }
        }
    }

    public LimitedQueue<GroupMessageDetails> getMessageNotificatioItems(int userId, int groupId)
    {
        logDebug("getting notification for group " + groupId);
        Map<Integer, LimitedQueue<GroupMessageDetails>> userMessageMap = userMessageNotificationMap.get(userId);
        if (userMessageMap != null) {
            LimitedQueue<GroupMessageDetails> messageNotificationQueue = userMessageMap.get(groupId);

            if (messageNotificationQueue != null) {
                return messageNotificationQueue;
            }
        }
        return null;
    }

    @Override
    public boolean isEnabled()
    {
        return getFamstackApplicationConfiguration().isDesktopNotificationEnabled();
    }
}
