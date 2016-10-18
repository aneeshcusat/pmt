package com.famstack.projectscheduler.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.famstack.projectscheduler.datatransferobject.AccountItem;
import com.famstack.projectscheduler.datatransferobject.ClientItem;
import com.famstack.projectscheduler.datatransferobject.ProjectSubTeamItem;
import com.famstack.projectscheduler.datatransferobject.ProjectTeamItem;
import com.famstack.projectscheduler.employees.bean.AccountDetails;
import com.famstack.projectscheduler.employees.bean.ClientDetails;
import com.famstack.projectscheduler.employees.bean.ProjectSubTeamDetails;
import com.famstack.projectscheduler.employees.bean.ProjectTeamDetails;

@Component
public class FamstackAccountManager extends BaseFamstackManager {

	private final Map<Integer, AccountDetails> accountMap = new HashMap<>();
	private final Map<Integer, ProjectTeamDetails> teamMap = new HashMap<>();
	private final Map<Integer, ProjectSubTeamDetails> subTeamMap = new HashMap<>();
	private final Map<Integer, ClientDetails> clientMap = new HashMap<>();

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

					Set<ClientItem> clientItems = projectSubTeamItem.getClientItem();

					for (ClientItem clientItem : clientItems) {
						ClientDetails clientDetails = new ClientDetails();
						clientDetails.setClientId(clientItem.getClientId());
						clientDetails.setName(clientItem.getName());
						projectSubTeamDetails.getClientItems().add(clientDetails);
					}
					projectTeamDetails.getProjectSubTeams().add(projectSubTeamDetails);
				}

				accountDetails.getProjectTeams().add(projectTeamDetails);
			}
		}
		return accountDetails;

	}

	public List<AccountDetails> getAllAccountDetails() {
		List<AccountDetails> accountDetailsList = new ArrayList<>();
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
		return accountDetailsList;
	}

}
