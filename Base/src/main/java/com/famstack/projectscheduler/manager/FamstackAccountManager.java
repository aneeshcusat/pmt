package com.famstack.projectscheduler.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
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

public class FamstackAccountManager extends BaseFamstackManager
{

    private static final Map<Integer, AccountDetails> accountMap = new HashMap<>();

    private static final Map<Integer, ProjectTeamDetails> teamMap = new HashMap<>();

    private static final Map<Integer, ProjectSubTeamDetails> subTeamMap = new HashMap<>();

    private static final Map<Integer, ClientDetails> clientMap = new HashMap<>();

    private static final List<AccountDetails> accountDetailsList = new ArrayList<>();

    private boolean initialized = false;

    public AccountDetails mapProjectItemToProjectDetails(AccountItem accountItem)
    {
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
                        clientDetails.setEmail(clientItem.getEmail());
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

    public void initialize()
    {
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

        initialized = true;
    }

    public List<AccountDetails> getAllAccountDetails()
    {
        return accountDetailsList;
    }

    public static Map<Integer, AccountDetails> getAccountmap()
    {
        return accountMap;
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

    public static List<AccountDetails> getAccountdetailslist()
    {
        return accountDetailsList;
    }

    public void createAccount(String name, String holder, String type)
    {
        AccountItem accountItem = new AccountItem();
        accountItem.setName(name);
        accountItem.setHolder(holder);
        accountItem.setType(type);

        famstackDataAccessObjectManager.saveOrUpdateItem(accountItem);
        initialize();
    }

    public void createTeam(String name, String poc, int accountId)
    {
        Set<AccountItem> accountSet = new HashSet<>();
        AccountItem accountItem =
            (AccountItem) famstackDataAccessObjectManager.getItemById(accountId, AccountItem.class);
        accountSet.add(accountItem);
        ProjectTeamItem projectTeamItem = new ProjectTeamItem();
        projectTeamItem.setName(name);
        projectTeamItem.setPoc(poc);
        projectTeamItem.setAccountItems(accountSet);
        famstackDataAccessObjectManager.saveOrUpdateItem(projectTeamItem);
        initialize();
    }

    public void createSubTeam(String name, String poId, int teamId)
    {

        ProjectTeamItem projectTeamItem =
            (ProjectTeamItem) famstackDataAccessObjectManager.getItemById(teamId, ProjectTeamItem.class);
        ProjectSubTeamItem projectSubTeamItem = new ProjectSubTeamItem();
        projectSubTeamItem.setName(name);
        projectSubTeamItem.setPoId(poId);
        projectSubTeamItem.setProjectTeamItem(projectTeamItem);

        famstackDataAccessObjectManager.saveOrUpdateItem(projectSubTeamItem);
        initialize();
    }

    public void createClient(String name, String email, int subTeamId)
    {
        ProjectSubTeamItem projectSubTeamItem =
            (ProjectSubTeamItem) famstackDataAccessObjectManager.getItemById(subTeamId, ProjectSubTeamItem.class);
        ClientItem clientItem = new ClientItem();
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
