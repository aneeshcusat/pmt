package com.famstack.projectscheduler.dashboard.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.famstack.projectscheduler.contants.ProjectStatus;

public class DashBoardProjectDetails  extends ProjectTaskActivityDetails {

	private List<String> assigneeNameList = new ArrayList<>();
	private Set<Integer> assigneeIdList = new HashSet<>();
	
	public String getProjectStatusLabel(){
		ProjectStatus status = getProjectStatus();	
		
		if (getProjectCompletionTime().before(new Date()) && status != ProjectStatus.COMPLETED &&  status != ProjectStatus.INPROGRESS){
			return "BACKLOG";
		} else if (getProjectStartTime().after(new Date()) && status != ProjectStatus.INPROGRESS){
			return "UPCOMING";
		} else if (status == ProjectStatus.COMPLETED || status == ProjectStatus.INPROGRESS || status == ProjectStatus.UNASSIGNED) {
			return status.toString();
		}
		return "UPCOMING";
	}
	
	public List<String> getAssigneeNameList() {
		return assigneeNameList;
	}
	public void setAssigneeNameList(List<String> assigneeNameList) {
		this.assigneeNameList = assigneeNameList;
	}
	public Set<Integer> getAssigneeIdList() {
		return assigneeIdList;
	}
	public void setAssigneeIdList(Set<Integer> assigneeIdList) {
		this.assigneeIdList = assigneeIdList;
	}
}
