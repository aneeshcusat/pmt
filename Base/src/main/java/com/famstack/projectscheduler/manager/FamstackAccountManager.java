package com.famstack.projectscheduler.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.famstack.projectscheduler.datatransferobject.AccountItem;
import com.famstack.projectscheduler.datatransferobject.ClientItem;
import com.famstack.projectscheduler.datatransferobject.ProjectSubTeamItem;
import com.famstack.projectscheduler.datatransferobject.ProjectTeamItem;
import com.famstack.projectscheduler.employees.bean.AccountDetails;
import com.famstack.projectscheduler.employees.bean.ClientDetails;
import com.famstack.projectscheduler.employees.bean.ProjectSubTeamDetails;
import com.famstack.projectscheduler.employees.bean.ProjectTeamDetails;

public class FamstackAccountManager extends BaseFamstackManager
{
	 @Resource
	 private FamstackRemoteServiceRefreshManager famstackRemoteServiceRefreshManager;

    private static final Map<Integer, AccountDetails> accountMap = new HashMap<>();

    private static final Map<Integer, ProjectTeamDetails> teamMap = new HashMap<>();

    private static final Map<Integer, ProjectSubTeamDetails> subTeamMap = new HashMap<>();

    private static final Map<Integer, ClientDetails> clientMap = new HashMap<>();

    private static final List<AccountDetails> accountDetailsList = new ArrayList<>();

    public AccountDetails mapProjectItemToProjectDetails(AccountItem accountItem)
    {
        AccountDetails accountDetails = new AccountDetails();
        if (accountItem != null) {
            accountDetails.setAccountId(accountItem.getAccountId());
            accountDetails.setCode(accountItem.getCode());
            accountDetails.setHolder(accountItem.getHolder());
            accountDetails.setName(accountItem.getName());
            accountDetails.setType(accountItem.getType());
            accountDetails.setUserGoupId(accountItem.getUserGroupId());
            Set<ProjectTeamItem> projectTeamItems = accountItem.getProjectTeam();
            for (ProjectTeamItem projectTeamItem : projectTeamItems) {
                ProjectTeamDetails projectTeamDetails = new ProjectTeamDetails();
                projectTeamDetails.setCode(projectTeamItem.getCode());
                projectTeamDetails.setName(projectTeamItem.getName());
                projectTeamDetails.setPoc(projectTeamItem.getPoc());
                projectTeamDetails.setTeamId(projectTeamItem.getTeamId());
                projectTeamDetails.setUserGoupId(projectTeamItem.getUserGroupId());
                Set<ProjectSubTeamItem> subTeamItems = projectTeamItem.getProjectSubTeam();

                for (ProjectSubTeamItem projectSubTeamItem : subTeamItems) {
                    ProjectSubTeamDetails projectSubTeamDetails = new ProjectSubTeamDetails();

                    projectSubTeamDetails.setCode(projectSubTeamItem.getCode());
                    projectSubTeamDetails.setName(projectSubTeamItem.getName());
                    projectSubTeamDetails.setPoId(projectSubTeamItem.getPoId());
                    projectSubTeamDetails.setSubTeamId(projectSubTeamItem.getSubTeamId());
                    projectSubTeamDetails.setTeamId(projectSubTeamItem.getProjectTeamItem().getTeamId());
                    projectSubTeamDetails.setUserGoupId(projectSubTeamItem.getUserGroupId());

                    Set<ClientItem> clientItems = projectSubTeamItem.getClientItem();

                    for (ClientItem clientItem : clientItems) {
                        ClientDetails clientDetails = new ClientDetails();
                        clientDetails.setClientId(clientItem.getClientId());
                        clientDetails.setName(clientItem.getName());
                        clientDetails.setEmail(clientItem.getEmail());
                        clientDetails.setUserGoupId(clientItem.getUserGroupId());
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
    
    public void forceInitialize() {
    	initialize();
    	famstackRemoteServiceRefreshManager.createOrUpdateRemoteRefreshItem(getFamstackApplicationConfiguration().getInstanceName(), "account", false);
    }

    public synchronized void initialize()
    {

        accountDetailsList.clear();
        accountMap.clear();
        teamMap.clear();
        subTeamMap.clear();
        clientMap.clear();
        logDebug("Initializing accounts");
        List<?> accountItemList = famstackDataAccessObjectManager.getAllGroupItems("AccountItem");
        if (accountItemList != null) {
            for (Object accountItemObj : accountItemList) {
                AccountItem accountItem = (AccountItem) accountItemObj;
                AccountDetails accountDetails = mapProjectItemToProjectDetails(accountItem);
                if (accountDetails != null) {
                    accountDetailsList.add(accountDetails);
                }
            }
        }
        
        famstackRemoteServiceRefreshManager.createOrUpdateRemoteRefreshItem(getFamstackApplicationConfiguration().getInstanceName(), "account", true);
    }

    public List<AccountDetails> getAllAccountDetails()
    {
        List<AccountDetails> accountsList = new ArrayList<>();

        if (accountDetailsList != null) {
            for (AccountDetails accountDetails : accountDetailsList) {
                if (accountDetails.getUserGoupId() != null
                    && accountDetails.getUserGoupId().equalsIgnoreCase(
                        getFamstackUserSessionConfiguration().getUserGroupId())) {
                    accountsList.add(accountDetails);
                }
            }
        }
        return accountsList;
    }

    public static Map<Integer, AccountDetails> getAccountmap()
    {
        return accountMap;
    }
    
    public List<String> getAccountMap()
    {
    	return accountMap.keySet().stream()
        .map((key) -> accountMap.get(key).getName()).distinct()
        .collect(Collectors.toList());
    }

    public static Map<Integer, ProjectTeamDetails> getTeammap()
    {
        return teamMap;
    }

    public static Map<Integer, ProjectSubTeamDetails> getSubteammap()
    {
        return subTeamMap;
    }

    public static Map<Integer, ClientDetails> getClientmap()
    {
        return clientMap;
    }

    public  List<String> getClientMap()
    {
    	return clientMap.keySet().stream()
    	        .map((key) -> clientMap.get(key).getName()).distinct()
    	        .collect(Collectors.toList());
    }
   
    public void createAccount(String name, String holder, String type, int id)
    {
        AccountItem accountItem = (AccountItem) famstackDataAccessObjectManager.getItemById(id, AccountItem.class);
        if (accountItem == null) {
            accountItem = new AccountItem();
        }
        accountItem.setName(name);
        accountItem.setHolder(holder);
        accountItem.setType(type);

        famstackDataAccessObjectManager.saveOrUpdateItem(accountItem);
        initialize();
    }

    public void createTeam(String name, String poc, int accountId, int id)
    {
        ProjectTeamItem projectTeamItem =
            (ProjectTeamItem) famstackDataAccessObjectManager.getItemById(id, ProjectTeamItem.class);

        Set<AccountItem> accountSet = new HashSet<>();
        AccountItem accountItem =
            (AccountItem) famstackDataAccessObjectManager.getItemById(accountId, AccountItem.class);
        accountSet.add(accountItem);
        if (projectTeamItem == null) {
            projectTeamItem = new ProjectTeamItem();
        }

        projectTeamItem.setName(name);
        projectTeamItem.setPoc(poc);
        projectTeamItem.setAccountItems(accountSet);
        famstackDataAccessObjectManager.saveOrUpdateItem(projectTeamItem);
        initialize();
    }

    public void createSubTeam(String name, String poId, int teamId, int id)
    {
        ProjectSubTeamItem projectSubTeamItem =
            (ProjectSubTeamItem) famstackDataAccessObjectManager.getItemById(id, ProjectSubTeamItem.class);

        ProjectTeamItem projectTeamItem =
            (ProjectTeamItem) famstackDataAccessObjectManager.getItemById(teamId, ProjectTeamItem.class);
        if (projectSubTeamItem == null) {
            projectSubTeamItem = new ProjectSubTeamItem();
        }
        projectSubTeamItem.setName(name);
        projectSubTeamItem.setPoId(poId);
        projectSubTeamItem.setProjectTeamItem(projectTeamItem);

        famstackDataAccessObjectManager.saveOrUpdateItem(projectSubTeamItem);
        initialize();
    }

    public void createClient(String name, String email, int subTeamId, int id)
    {
        ClientItem clientItem = (ClientItem) famstackDataAccessObjectManager.getItemById(id, ClientItem.class);

        ProjectSubTeamItem projectSubTeamItem =
            (ProjectSubTeamItem) famstackDataAccessObjectManager.getItemById(subTeamId, ProjectSubTeamItem.class);
        if (clientItem == null) {
            clientItem = new ClientItem();
        }
        clientItem.setName(name);
        clientItem.setEmail(email);
        clientItem.setProjectSubTeamItem(projectSubTeamItem);
        famstackDataAccessObjectManager.saveOrUpdateItem(clientItem);
        initialize();

    }

    public void deleteAccount(int id)
    {
        AccountItem accountItem = (AccountItem) famstackDataAccessObjectManager.getItemById(id, AccountItem.class);
        famstackDataAccessObjectManager.deleteItem(accountItem);
        initialize();
    }

    public void deleteTeam(int id)
    {
        ProjectTeamItem projectTeamItem =
            (ProjectTeamItem) famstackDataAccessObjectManager.getItemById(id, ProjectTeamItem.class);
        famstackDataAccessObjectManager.deleteItem(projectTeamItem);
        initialize();

    }

    public void deleteSubTeam(int id)
    {
        ProjectSubTeamItem projectSubTeamItem =
            (ProjectSubTeamItem) famstackDataAccessObjectManager.getItemById(id, ProjectSubTeamItem.class);
        famstackDataAccessObjectManager.deleteItem(projectSubTeamItem);
        initialize();
    }

    public void deleteClient(int id)
    {
        ClientItem clientItem = (ClientItem) famstackDataAccessObjectManager.getItemById(id, ClientItem.class);
        famstackDataAccessObjectManager.deleteItem(clientItem);
        initialize();

    }

}
