package com.famstack.projectscheduler.utils;

import java.io.IOException;
import java.net.InetAddress;
import java.text.ParseException;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.quartz.CronExpression;

import com.famstack.projectscheduler.BaseFamstackService;
import com.famstack.projectscheduler.dashboard.bean.ProjectTaskActivityDetails;
import com.famstack.projectscheduler.dashboard.bean.SearchRequest;
import com.famstack.projectscheduler.employees.bean.EmployeeDetails;
import com.famstack.projectscheduler.util.StringUtils;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class FamstackUtils extends BaseFamstackService
{

    private static final String UNKNOWNSYSTEM = "unknownsystem";

    /** The host name. */
    private static String hostName;

    /**
     * Sets the host name.
     * 
     * @return the host name
     */
    public static synchronized String getHostName()
    {
        if (StringUtils.isNotBlank(hostName) && !UNKNOWNSYSTEM.equalsIgnoreCase(hostName)) {
            return hostName;
        }

        try {
            hostName = InetAddress.getLocalHost().getHostName();
        } catch (Exception e) {
            BaseFamstackService.getStaticLogger(FamstackUtils.class).error("Unable to get hostname", e);
            hostName = UNKNOWNSYSTEM;
        }
        return hostName;
    }

    public static synchronized String getJsonFromObject(Object dataObject)
    {
        String jsonString = null;
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            jsonString = objectMapper.writeValueAsString(dataObject);
        } catch (JsonGenerationException e) {
            getStaticLogger(FamstackUtils.class).error(e.getMessage(), e);
        } catch (JsonMappingException e) {
            getStaticLogger(FamstackUtils.class).error(e.getMessage(), e);
        } catch (IOException e) {
            getStaticLogger(FamstackUtils.class).error(e.getMessage(), e);
        }
        return jsonString;
    }

    public static Date getNextRunFromCron(String cronExpression, Date date)
    {
        CronExpression exp = null;
        try {
            exp = new CronExpression(cronExpression);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return exp != null ? exp.getNextValidTimeAfter(date == null ? new Date() : date) : null;
    }

	public static void sortProjectTaskAssigneeDataList(
			List<ProjectTaskActivityDetails> projectTaskAssigneeDataList,
			final Map<Integer, EmployeeDetails> allUsersMap) {

		Collections.sort(projectTaskAssigneeDataList,
				new Comparator<ProjectTaskActivityDetails>() {
					@Override
					public int compare(
							ProjectTaskActivityDetails projectDetails2,
							ProjectTaskActivityDetails projectDetails1) {
						int useId1 = projectDetails1.getUserId();
						int useId2 = projectDetails2.getUserId();
						EmployeeDetails emp1 = allUsersMap.get(useId1);
						EmployeeDetails emp2 = allUsersMap.get(useId2);
						if (emp1 != null && emp2 != null) {
							return emp2.getFirstName().compareTo(
									emp1.getFirstName());
						}
						return 0;
					}
				});
	}
	
	public static void main(String[] args) {
		System.out.println(getNextRunFromCron("0 0 15,17,19 * * ?",new Date()));
		//0 0 15,17,19 * * ?
		//0 0 {hours} ? * {weekdays} *
		//MON,TUE,WED,THU,FRI,SAT,SUN
	}
	
	public static Map getJsonObjectFromJson(String jsonString) {
	        ObjectMapper objectMapper = new ObjectMapper();
	        try {
	            return  objectMapper.readValue(jsonString, Map.class);
	        } catch (JsonGenerationException e) {
	            getStaticLogger(FamstackUtils.class).error(e.getMessage(), e);
	        } catch (JsonMappingException e) {
	            getStaticLogger(FamstackUtils.class).error(e.getMessage(), e);
	        } catch (IOException e) {
	            getStaticLogger(FamstackUtils.class).error(e.getMessage(), e);
	        }
	        return null;
	}


	public static SearchRequest getObjectFromJson(String searchRequest) {
		SearchRequest jsonString = null;
	        ObjectMapper objectMapper = new ObjectMapper();
	        try {
	            jsonString = objectMapper.readValue(searchRequest, SearchRequest.class);
	        } catch (JsonGenerationException e) {
	            getStaticLogger(FamstackUtils.class).error(e.getMessage(), e);
	        } catch (JsonMappingException e) {
	            getStaticLogger(FamstackUtils.class).error(e.getMessage(), e);
	        } catch (IOException e) {
	            getStaticLogger(FamstackUtils.class).error(e.getMessage(), e);
	        }
	        return jsonString;
	   
	}
}
