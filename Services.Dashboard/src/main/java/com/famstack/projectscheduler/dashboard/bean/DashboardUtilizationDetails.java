package com.famstack.projectscheduler.dashboard.bean;

import java.util.Date;

import com.famstack.projectscheduler.employees.bean.AccountDetails;
import com.famstack.projectscheduler.employees.bean.ProjectSubTeamDetails;
import com.famstack.projectscheduler.employees.bean.ProjectTeamDetails;
import com.famstack.projectscheduler.manager.FamstackAccountManager;


public class DashboardUtilizationDetails {

	private Date actualTaskStartTime;
	private Integer userId;
	private Integer accountId;
	private Integer teamId;
	private Integer subTeamId;
	private Double billableMins;
	private Double nonBillableMins;
	private String type;
	private Double grandTotal;
	private Double totalWorkingHoursInMis;
	
	public Date getActualTaskStartTime() {
		return actualTaskStartTime;
	}
	public void setActualTaskStartTime(Date actualTaskStartTime) {
		this.actualTaskStartTime = actualTaskStartTime;
	}
	public Integer getAccountId() {
		return accountId;
	}
	public void setAccountId(Integer accountId) {
		this.accountId = accountId;
	}
	public Integer getTeamId() {
		return teamId;
	}
	public void setTeamId(Integer teamId) {
		this.teamId = teamId;
	}
	public Integer getSubTeamId() {
		return subTeamId;
	}
	public void setSubTeamId(Integer subTeamId) {
		this.subTeamId = subTeamId;
	}
	
	@Override
	public String toString() {
		return "[accountId:"+accountId+", teamId:"+teamId+", teamId:"+teamId+", subTeam:"+subTeamId+"]";
	}
	
	public String getLabel(){
		
		if ("Accounts".equalsIgnoreCase(type)){
			AccountDetails accountDetails = FamstackAccountManager.getAccountmap().get(accountId);
			return accountDetails != null ? accountDetails.getName() : "Acc" + accountId;
		} else if  ("Teams".equalsIgnoreCase(type)){
			ProjectTeamDetails projectTeamDetails = FamstackAccountManager.getTeammap().get(teamId);
			return projectTeamDetails != null ? projectTeamDetails.getName() : "Team" + teamId;
		} else if  ("Sub Teams".equalsIgnoreCase(type)){
			ProjectSubTeamDetails projectSubTeamDetails =FamstackAccountManager.getSubteammap().get(subTeamId);
			return projectSubTeamDetails != null ? projectSubTeamDetails.getName() : "SubTeam" + subTeamId;
		}
		return "";
		
	} 
	
	public int getNonBillablePercentage(){
		 return (int) ((nonBillableMins/(billableMins+nonBillableMins))*100);
	}
	
	public int getBillablePercentage(){
		 return (int) ((billableMins/(billableMins+nonBillableMins))*100);
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Integer getGrandTotalPercentage(){
		return (int) (((billableMins+nonBillableMins)/grandTotal) * 100);
	}
	public Double getBillableMins() {
		return billableMins;
	}
	public void setBillableMins(Double billableMins) {
		this.billableMins = billableMins > 0? billableMins : 0;
	}
	public Double getNonBillableMins() {
		return nonBillableMins;
	}
	public void setNonBillableMins(Double nonBillableMins) {
		this.nonBillableMins = nonBillableMins > 0? nonBillableMins : 0;;
	}
	
	public String getNonBillableHrs(){
        int hours = (int) (nonBillableMins / 60); // since both are ints, you get an int
        int minutes = (int) (nonBillableMins % 60);
        return String.format("%d:%02d", hours, minutes);
    }
	public String getBillableHrs(){
        int hours = (int) (billableMins / 60); // since both are ints, you get an int
        int minutes = (int) (billableMins % 60);
        return String.format("%d:%02d", hours, minutes);
    }
	
	public void setGrandTotal(Double grandTotal) {
		this.grandTotal = grandTotal;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public Double getGrandTotal() {
		return grandTotal;
	}
	public Double getTotalWorkingHoursInMis() {
		return totalWorkingHoursInMis;
	}
	public void setTotalWorkingHoursInMis(Double totalWorkingHoursInMis) {
		this.totalWorkingHoursInMis = totalWorkingHoursInMis;
	}
	
	public Double getFreeMins(){
		Double totalFreeMins = totalWorkingHoursInMis - (billableMins+nonBillableMins);
		return totalFreeMins >= 0 ? totalFreeMins : 0;
	}
	
	public String getFreeHrs(){
		 int hours = (int) (getFreeMins() / 60); // since both are ints, you get an int
	     int minutes = (int) (getFreeMins() % 60);
	     return String.format("%d:%02d", hours, minutes);
	}
	
	public int getResourceNonBillablePercentage(){
		Double freeMins = getFreeMins();
		Double totalMins = billableMins+nonBillableMins;
		if (freeMins > 0) {
			totalMins += freeMins;
		}
		 return (int) ((nonBillableMins/(totalMins))*100);
	}
	
	public int getResourceBillablePercentage(){
		Double freeMins = getFreeMins();
		Double totalMins = billableMins+nonBillableMins;
		if (freeMins > 0) {
			totalMins += freeMins;
		}
		 return (int) ((billableMins/(totalMins))*100);
	}
	
	public Integer getGrandResourceTotal(){
		return (int) (((billableMins+nonBillableMins)/getTotalWorkingHoursInMis()) * 100);
	}
}
