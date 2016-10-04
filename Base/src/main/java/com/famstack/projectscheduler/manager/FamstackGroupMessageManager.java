package com.famstack.projectscheduler.manager;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.famstack.projectscheduler.datatransferobject.GroupItem;
import com.famstack.projectscheduler.datatransferobject.GroupMessageItem;
import com.famstack.projectscheduler.datatransferobject.UserItem;
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

	private Set<UserItem> getSubscriberItems(String subscriberIds) {
		Set<UserItem> userItemSet = new HashSet<UserItem>();
		if (subscriberIds != null) {
			String[] userIdArray = subscriberIds.split(",");
			for (String userId: userIdArray) {
				if (!StringUtils.isEmpty(userId)) {
					UserItem userItem = new UserItem();
					userItem.setId(Integer.parseInt(userId));
					userItemSet.add(userItem);
				}
			}
		}
		return userItemSet;
	}
	
	public void deleteGroup(int groupId) {
		GroupItem groupItem = (GroupItem) famstackDataAccessObjectManager.getItemById(groupId,
				GroupItem.class);
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
			// TODO groupDetails.setMessages(groupItem.getMessages());
			
			groupDetails.setSubscriberIds(getSubscriberIds(groupItem.getSubscribers()));
			
			
			
			return groupDetails;

		}
		return null;
	}

	private String getSubscriberIds(Set<UserItem> subscribers) {
		String subscriberIds = new String();
		if (subscribers != null) {
			for (UserItem userItem : subscribers) {
				subscriberIds += userItem.getId() + ",";
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
}
