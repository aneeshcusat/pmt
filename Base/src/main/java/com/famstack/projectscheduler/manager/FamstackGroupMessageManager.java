package com.famstack.projectscheduler.manager;

import java.util.ArrayList;
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

	public void createGroupItem(GroupDetails groupDetails) {
		GroupItem groupItem = new GroupItem();
		groupItem.setSubscribers(getSubscriberItems(groupDetails.getSubscriberIds()));
		groupItem.setDescription(groupDetails.getDescription());
		groupItem.setName(groupDetails.getName());
		groupItem.setCreatedBy(getFamstackUserSessionConfiguration().getLoginResult().getUserItem());
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
			groupDetails.setMessages(mapMessageItems(groupItem.getMessages()));
			groupDetails.setSubscriberIds(getSubscriberIds(groupItem.getSubscribers()));

			groupDetails.setSubscribers(getSubscriberDetails(groupItem.getSubscribers()));
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

	public Set<GroupMessageDetails> mapMessageItems(Set<GroupMessageItem> messages) {
		Set<GroupMessageDetails> messageDetailsSet = new HashSet<GroupMessageDetails>();
		if (messages != null) {
			for (GroupMessageItem messageItem : messages) {
				GroupMessageDetails messageDetails = mapGroupMessageItemToGroupMessageDetails(messageItem);
				messageDetailsSet.add(messageDetails);
			}
		}
		return messageDetailsSet;
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
		groupMessage.setUser(getFamstackUserSessionConfiguration().getLoginResult().getUserItem());
		famstackDataAccessObjectManager.saveOrUpdateItem(groupMessage);
	}

	public GroupMessageDetails mapGroupMessageItemToGroupMessageDetails(GroupMessageItem groupMessageItem) {

		if (groupMessageItem != null) {
			GroupMessageDetails groupMessageDetails = new GroupMessageDetails();
			groupMessageDetails.setDescription(groupMessageItem.getDescription());
			if (groupMessageItem.getUser() != null) {
				UserItem userItem = userProfileManager.getUserItemById(groupMessageItem.getUser().getId());
				groupMessageDetails.setUser(userProfileManager.mapEmployeeDetails(userItem));
			}
			groupMessageDetails.setCreatedDate(groupMessageItem.getCreatedDate());
			groupMessageDetails.setLastModifiedDate(groupMessageItem.getLastModifiedDate());
			groupMessageDetails.setGroup(groupMessageItem.getGroupItem().getGroupId());
			groupMessageDetails.setMessageId(groupMessageItem.getMessageId());
			return groupMessageDetails;
		}
		return null;
	}
}
