package com.famstack.projectscheduler.manager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.famstack.projectscheduler.contants.HQLStrings;
import com.famstack.projectscheduler.datatransferobject.GroupItem;
import com.famstack.projectscheduler.datatransferobject.GroupMessageItem;
import com.famstack.projectscheduler.datatransferobject.UserItem;
import com.famstack.projectscheduler.employees.bean.EmployeeDetails;
import com.famstack.projectscheduler.employees.bean.GroupDetails;
import com.famstack.projectscheduler.employees.bean.GroupMessageDetails;
import com.famstack.projectscheduler.notification.services.FamstackDesktopNotificationService;
import com.famstack.projectscheduler.util.DateTimePeriod;
import com.famstack.projectscheduler.util.DateUtils;

/**
 * The Class FamstackGroupMessageManager.
 * 
 * @author Kiran Thankaraj
 * @version 1.0
 */
@Component
public class FamstackGroupMessageManager extends BaseFamstackManager {

	@Resource
	FamstackUserProfileManager userProfileManager;

	@Resource
	FamstackDesktopNotificationService famstackDesktopNotificationService;

	private final Map<Integer, GroupDetails> groupDetailsCache = new HashMap<>();

	public void createGroupItem(GroupDetails groupDetails) {
		GroupItem groupItem = new GroupItem();
		groupItem.setSubscribers(getSubscriberItems(groupDetails.getSubscriberIds()));
		groupItem.setDescription(groupDetails.getDescription());
		groupItem.setName(groupDetails.getName());
		groupItem.setCreatedBy(getFamstackUserSessionConfiguration().getLoginResult().getUserItem());
		famstackDataAccessObjectManager.saveOrUpdateItem(groupItem);
	}

	public void updateGroupItem(GroupDetails groupDetails) {
		GroupItem groupItem = (GroupItem) famstackDataAccessObjectManager.getItemById(groupDetails.getGroupId(),
				GroupItem.class);
		groupItem.setSubscribers(getSubscriberItems(groupDetails.getSubscriberIds()));
		groupItem.setDescription(groupDetails.getDescription());
		groupItem.setName(groupDetails.getName());
		famstackDataAccessObjectManager.saveOrUpdateItem(groupItem);
	}

	public Set<UserItem> getSubscriberItems(int[] subscriberIds) {
		Set<UserItem> userItemSet = new HashSet<UserItem>();
		if (subscriberIds != null) {
			for (int userId : subscriberIds) {
				UserItem userItem = new UserItem();
				userItem.setId(userId);
				userItemSet.add(userItem);
			}
		}
		return userItemSet;
	}

	public void deleteGroup(int groupId) {
		GroupItem groupItem = (GroupItem) famstackDataAccessObjectManager.getItemById(groupId, GroupItem.class);
		if (groupItem != null) {
			famstackDataAccessObjectManager.deleteItem(groupItem);
		}
	}

	public GroupDetails mapGroupItemToGroupDetails(GroupItem groupItem) {

		if (groupItem != null) {
			GroupDetails groupDetails = new GroupDetails();

			groupDetails.setDescription(groupItem.getDescription());
			groupDetails.setName(groupItem.getName());

			if (groupItem.getCreatedBy() != null) {
				UserItem userItem = userProfileManager.getUserItemById(groupItem.getCreatedBy().getId());
				groupDetails.setCreatedBy(userProfileManager.mapEmployeeDetails(userItem));
			}
			groupDetails.setCreatedDate(groupItem.getCreatedDate());
			groupDetails.setLastModifiedDate(groupItem.getLastModifiedDate());

			groupDetails.setGroupId(groupItem.getGroupId());
			groupDetails.setMessages(sort(mapMessageItems(groupItem.getMessages())));
			groupDetails.setSubscriberIds(getSubscriberIds(groupItem.getSubscribers()));

			groupDetails.setSubscribers(getSubscriberDetails(groupItem.getSubscribers()));
			return groupDetails;

		}
		return null;
	}

	public GroupDetails getGroupDetails(int groupId) {
		if (groupId != 0) {
			GroupDetails groupDetails = groupDetailsCache.get(groupId);
			if (groupDetails == null) {
				GroupItem groupItem = (GroupItem) famstackDataAccessObjectManager.getItemById(groupId, GroupItem.class);
				groupDetails = mapGroupItemToGroupDetails(groupItem);
				groupDetailsCache.put(groupId, groupDetails);
			}
			return groupDetails;
		}
		return null;
	}

	private Set<EmployeeDetails> getSubscriberDetails(Set<UserItem> subscribers) {
		Set<EmployeeDetails> employeeDetailsList = new HashSet<EmployeeDetails>();
		if (subscribers != null) {
			for (Object userItemObj : subscribers) {
				UserItem userItem = (UserItem) userItemObj;
				EmployeeDetails employeeDetails = userProfileManager.mapEmployeeDetails(userItem);
				if (employeeDetails != null) {
					employeeDetailsList.add(employeeDetails);
				}
			}
		}
		return employeeDetailsList;
	}

	public List<GroupDetails> getGroupsForUser(Integer userId) {
		List<GroupDetails> groupDetailsList = new ArrayList<GroupDetails>();
		UserItem userItem = userProfileManager.getUserItemById(userId);
		Set<GroupItem> groupItemsList = userItem.getGroups();
		if (groupItemsList != null) {
			for (GroupItem groupItem : groupItemsList) {
				groupDetailsList.add(mapGroupItemToGroupDetails(groupItem));
			}
		}
		return groupDetailsList;
	}

	public List<GroupItem> getGroupItems(Integer userId) {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("id", String.valueOf(userId));
		List<GroupItem> userItemList = (List<GroupItem>) getFamstackDataAccessObjectManager()
				.executeQuery(HQLStrings.getString("FamstackQueryStringsGroupsByUserId"), dataMap);
		return userItemList;
	}

	public List<GroupMessageDetails> mapMessageItems(Set<GroupMessageItem> messages) {
		List<GroupMessageDetails> messageDetailsList = new ArrayList<GroupMessageDetails>();
		if (messages != null) {
			for (GroupMessageItem messageItem : messages) {
				GroupMessageDetails messageDetails = mapGroupMessageItemToGroupMessageDetails(messageItem);
				messageDetailsList.add(messageDetails);
			}
		}
		return messageDetailsList;
	}

	public int[] getSubscriberIds(Set<UserItem> subscribers) {
		int[] subscriberIds = null;
		if (subscribers != null) {
			subscriberIds = new int[subscribers.size()];
			int i = 0;
			for (UserItem userItem : subscribers) {
				subscriberIds[i] = userItem.getId();
				i++;
			}
		}
		return subscriberIds;
	}

	public void createGroupMessageItem(GroupMessageDetails groupMessageDetails) {
		GroupMessageItem groupMessage = new GroupMessageItem();
		groupMessage.setDescription(groupMessageDetails.getDescription());
		GroupItem groupItem = new GroupItem();
		groupItem.setGroupId(groupMessageDetails.getGroup());
		groupMessage.setGroupItem(groupItem);
		UserItem userItem = getFamstackUserSessionConfiguration().getCurrentUser();
		groupMessage.setUser(userItem);
		famstackDataAccessObjectManager.saveOrUpdateItem(groupMessage);

		GroupDetails groupDetails = getGroupDetails(groupMessageDetails.getGroup());
		groupMessageDetails.setUser(userItem.getId());
		groupMessageDetails.setUserFullName(userItem.getFirstName());
		groupMessageDetails.setGroupName(groupDetails.getName());

		famstackDesktopNotificationService.notifyMessage(groupMessageDetails, groupDetails.getSubscriberIds());
	}

	public GroupMessageDetails mapGroupMessageItemToGroupMessageDetails(GroupMessageItem groupMessageItem) {

		if (groupMessageItem != null) {
			GroupMessageDetails groupMessageDetails = new GroupMessageDetails();
			groupMessageDetails.setDescription(groupMessageItem.getDescription());
			if (groupMessageItem.getUser() != null) {
				UserItem userItem = userProfileManager.getUserItemById(groupMessageItem.getUser().getId());
				groupMessageDetails.setUser(userItem.getId());
				groupMessageDetails.setUserFullName(userItem.getFirstName() + " " + userItem.getLastName());
			}
			groupMessageDetails.setCreatedDate(groupMessageItem.getCreatedDate());
			groupMessageDetails.setLastModifiedDate(groupMessageItem.getLastModifiedDate());
			groupMessageDetails.setGroup(groupMessageItem.getGroupItem().getGroupId());
			groupMessageDetails.setMessageId(groupMessageItem.getMessageId());
			groupMessageDetails.setCreatedDateDisplay(DateUtils.getDisplayDate(groupMessageItem.getCreatedDate()));
			return groupMessageDetails;
		}
		return null;
	}

	public List<GroupMessageDetails> getGroupMessages(int groupId, int messageId) {
		String hqlString = null;
		List<GroupMessageDetails> messageList = new ArrayList<GroupMessageDetails>();
		Date date = DateUtils.getNextPreviousDate(DateTimePeriod.DAY, new Date(), -1);
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("groupId", groupId);
		dataMap.put("date", date);
		if (messageId > 0) {
			hqlString = "getMessagesAfterId";
			dataMap.put("messageId", messageId);
		} else {
			hqlString = "getGroupMessages";
		}
		List<?> messageItems = getFamstackDataAccessObjectManager().executeQuery(HQLStrings.getString(hqlString),
				dataMap);
		for (Object messageItem : messageItems) {
			messageList.add(mapGroupMessageItemToGroupMessageDetails((GroupMessageItem) messageItem));
		}
		return sort(messageList);
	}

	public List<GroupMessageDetails> sort(List<GroupMessageDetails> groupMessageDetails) {
		Collections.sort(groupMessageDetails, new Comparator<GroupMessageDetails>() {
			@Override
			public int compare(GroupMessageDetails messageDetailsOne, GroupMessageDetails messageDetailsTwo) {
				return messageDetailsOne.getCreatedDate().getTime() > messageDetailsTwo.getCreatedDate().getTime() ? 1
						: -1;
			}
		});

		return groupMessageDetails;
	}
}
