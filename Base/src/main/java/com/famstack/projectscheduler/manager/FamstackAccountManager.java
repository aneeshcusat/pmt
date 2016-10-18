package com.famstack.projectscheduler.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.famstack.projectscheduler.datatransferobject.AccountItem;
import com.famstack.projectscheduler.datatransferobject.ClientItem;
import com.famstack.projectscheduler.datatransferobject.ProjectSubTeamItem;
import com.famstack.projectscheduler.datatransferobject.ProjectTeamItem;
import com.famstack.projectscheduler.employees.bean.AccountDetails;
import com.famstack.projectscheduler.employees.bean.ClientDetails;
import com.famstack.projectscheduler.employees.bean.ProjectSubTeamDetails;
import com.famstack.projectscheduler.employees.bean.ProjectTeamDetails;

public class FamstackAccountManager extends BaseFamstackManager {

	private static final Map<Integer, AccountDetails> accountMap = new HashMap<>();
	private static final Map<Integer, ProjectTeamDetails> teamMap = new HashMap<>();
	private static final Map<Integer, ProjectSubTeamDetails> subTeamMap = new HashMap<>();
	private static final Map<Integer, ClientDetails> clientMap = new HashMap<>();
	private static final List<AccountDetails> accountDetailsList = new ArrayList<>();

	public AccountDetails mapProjectItemToProjectDetails(AccountItem accountItem) {
		AccountDetails accountDetails = new AccountDetails();
		if (accountItem != null) {
			accountDetails.setAccountId(accountItem.getAccountId());
			accountDetails.setCode(accountItem.getCode());
			accountDetails.setHolder(accountItem.getHolder());
			accountDetails.setName(accountItem.getName());
			accountDetails.setType(accountItem.getType());
			Set<ProjectTeamItem> projectTeamItems = accountItem.getProjectTeam();
			for (ProjectTeamItem projectTeamItem : projectTeamItems) {
				ProjectTeamDetails projectTeamDetails = new ProjectTeamDetails();
				projectTeamDetails.setCode(projectTeamItem.getCode());
				projectTeamDetails.setName(projectTeamItem.getName());
				projectTeamDetails.setPoc(projectTeamItem.getPoc());
				projectTeamDetails.setTeamId(projectTeamItem.getTeamId());

				Set<ProjectSubTeamItem> subTeamItems = projectTeamItem.getProjectSubTeam();

				for (ProjectSubTeamItem projectSubTeamItem : subTeamItems) {
					ProjectSubTeamDetails projectSubTeamDetails = new ProjectSubTeamDetails();

					projectSubTeamDetails.setCode(projectSubTeamItem.getCode());
					projectSubTeamDetails.setName(projectSubTeamItem.getName());
					projectSubTeamDetails.setPoId(projectSubTeamItem.getPoId());
					projectSubTeamDetails.setSubTeamId(projectSubTeamItem.getSubTeamId());
					projectSubTeamDetails.setTeamId(projectSubTeamItem.getProjectTeamItem().getTeamId());
					Set<ClientItem> clientItems = projectSubTeamItem.getClientItem();

					for (ClientItem clientItem : clientItems) {
						ClientDetails clientDetails = new ClientDetails();
						clientDetails.setClientId(clientItem.getClientId());
						clientDetails.setName(clientItem.getName());
						projectSubTeamDetails.getClientItems().add(clientDetails);
						clientMap.put(clientDetails.getClientId(), clientDetails);
					}
					projectTeamDetails.getProjectSubTeams().add(projectSubTeamDetails);
					subTeamMap.put(projectSubTeamDetails.getSubTeamId(), projectSubTeamDetails);
				}
				accountDetails.getProjectTeams().add(projectTeamDetails);
				teamMap.put(projectTeamDetails.getTeamId(), projectTeamDetails);
			}
			accountMap.put(accountDetails.getAccountId(), accountDetails);
		}
		return accountDetails;

	}

	public void initialize() {
		accountDetailsList.clear();
		accountMap.clear();
		teamMap.clear();
		subTeamMap.clear();
		clientMap.clear();

		logDebug("Initializing accounts");
		List<?> accountItemList = famstackDataAccessObjectManager.getAllItems("AccountItem");
		if (accountItemList != null) {
			for (Object accountItemObj : accountItemList) {
				AccountItem accountItem = (AccountItem) accountItemObj;

				AccountDetails accountDetails = mapProjectItemToProjectDetails(accountItem);
				if (accountDetails != null) {
					accountDetailsList.add(accountDetails);
				}
			}
		}
	}

	public List<AccountDetails> getAllAccountDetails() {
		return accountDetailsList;
	}

	public static Map<Integer, AccountDetails> getAccountmap() {
		return accountMap;
	}

	public static Map<Integer, ProjectTeamDetails> getTeammap() {
		return teamMap;
	}

	public static Map<Integer, ProjectSubTeamDetails> getSubteammap() {
		return subTeamMap;
	}

	public static Map<Integer, ClientDetails> getClientmap() {
		return clientMap;
	}

	public static List<AccountDetails> getAccountdetailslist() {
		return accountDetailsList;
	}

}
