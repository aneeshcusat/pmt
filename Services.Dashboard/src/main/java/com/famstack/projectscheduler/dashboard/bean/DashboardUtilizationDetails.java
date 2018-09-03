package com.famstack.projectscheduler.dashboard.bean;

import java.util.Date;

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
			return FamstackAccountManager.getAccountmap().get(accountId).getName();
		} else if  ("Teams".equalsIgnoreCase(type)){
			return FamstackAccountManager.getTeammap().get(teamId).getName();
		} else if  ("Sub Teams".equalsIgnoreCase(type)){
			return FamstackAccountManager.getSubteammap().get(subTeamId).getName();
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
		this.billableMins = billableMins;
	}
	public Double getNonBillableMins() {
		return nonBillableMins;
	}
	public void setNonBillableMins(Double nonBillableMins) {
		this.nonBillableMins = nonBillableMins;
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
