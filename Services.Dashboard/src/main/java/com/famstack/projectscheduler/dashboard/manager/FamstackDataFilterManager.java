package com.famstack.projectscheduler.dashboard.manager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.famstack.projectscheduler.BaseFamstackService;
import com.famstack.projectscheduler.dashboard.bean.ProjectTaskActivityDetails;
import com.famstack.projectscheduler.employees.bean.EmployeeDetails;
import com.famstack.projectscheduler.employees.bean.TaskActivityDetails;
import com.famstack.projectscheduler.employees.bean.TaskDetails;

@Component
public class FamstackDataFilterManager extends BaseFamstackService {

	public List<ProjectTaskActivityDetails> filterProjectTaskActivityDetails(
			List<ProjectTaskActivityDetails> projectTaskAssigneeDataList) {
		Map<Integer, EmployeeDetails> allUsersMap = getFamstackApplicationConfiguration().getAllUsersMap();
		List<ProjectTaskActivityDetails> filteredProjectTaskAssigneeDataList = new ArrayList<>();
		if (projectTaskAssigneeDataList != null ) {
			for (ProjectTaskActivityDetails projectTaskActivityDetails : projectTaskAssigneeDataList) {
				if(isUserHasPermission(allUsersMap.get(projectTaskActivityDetails.getUserId()))) {
					filteredProjectTaskAssigneeDataList.add(projectTaskActivityDetails);
				}
			}
		}
		
		return filteredProjectTaskAssigneeDataList;
		
	}

	private boolean isUserHasPermission(EmployeeDetails employeeDetails) {
		if (employeeDetails != null) {
			Integer currentUserDesignationNumber = getFamstackApplicationConfiguration().
					getDesignationMap().get(getFamstackApplicationConfiguration().
							getCurrentUser().getDesignation());
			Integer employeeDesignationNumber =  getFamstackApplicationConfiguration().
					getDesignationMap().get(employeeDetails.getDesignation());
			if (currentUserDesignationNumber != null && employeeDesignationNumber != null && 
					currentUserDesignationNumber >= employeeDesignationNumber) {
				return true;
			}
		}
		return false;
	}

	public List<TaskActivityDetails> filterTaskActivityDetails(
			List<TaskActivityDetails> taskActivitiesList) {
		Map<Integer, EmployeeDetails> allUsersMap = getFamstackApplicationConfiguration().getAllUsersMap();
		List<TaskActivityDetails> filteredTaskActivityDetails = new ArrayList<>();
		if (taskActivitiesList != null ) {
			for (TaskActivityDetails taskActivities : taskActivitiesList) {
				if(isUserHasPermission(allUsersMap.get(taskActivities.getUserId()))) {
					filteredTaskActivityDetails.add(taskActivities);
				}
			}
		}
		
		return filteredTaskActivityDetails;
		
	}

	public List<TaskDetails> filterTaskDetails(List<TaskDetails> taskDetailsList) {
		Map<Integer, EmployeeDetails> allUsersMap = getFamstackApplicationConfiguration().getAllUsersMap();
		List<TaskDetails> filteredTaskDetails = new ArrayList<>();
		for (TaskDetails  taskDetails : taskDetailsList) {

			if(isUserHasPermission(allUsersMap.get(taskDetails.getAssignee()))) {
				filteredTaskDetails.add(taskDetails);
			}
		}
		
		return filteredTaskDetails;
	}

}
