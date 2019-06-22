package com.famstack.projectscheduler.manager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.famstack.projectscheduler.contants.ProjectStatus;
import com.famstack.projectscheduler.contants.ProjectType;
import com.famstack.projectscheduler.dashboard.bean.ProjectTaskActivityDetails;
import com.famstack.projectscheduler.employees.bean.FamstackDateRange;
import com.famstack.projectscheduler.util.DateTimePeriod;
import com.famstack.projectscheduler.util.DateUtils;
import com.famstack.projectscheduler.util.StringUtils;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class FamstackStaticXLSImportManager extends BaseFamstackManager
{

	public Map<String, Object> readXLS(FamstackDateRange famstackDateRange, HttpServletRequest request, HttpServletResponse response)
    {
		
		String groupId = getFamstackApplicationConfiguration().getCurrentUserGroupId();
		  String sheetName = getFamstackApplicationConfiguration().getUserGroupMap().get(groupId).getName();
		 List<ProjectTaskActivityDetails> projectTaskAssigneeDataList = new ArrayList<>();
		String fullPath =
            request.getServletContext().getRealPath("/WEB-INF/classes/templates/staticreportdata/"+sheetName+".xlsx");
		Date  startDate = famstackDateRange.getStartDate();
		Date endDate = famstackDateRange.getEndDate();
		List<FamstackDateRange> newDateRangeList = new ArrayList<>();
    	newDateRangeList.add(famstackDateRange);
    	 String insertLocation = "F";
    	logInfo("Static report import started : " +  sheetName);
        try {
        	File file = new File(fullPath);
        	if (file.exists()) {
	            Workbook workbook =  WorkbookFactory.create(new File(fullPath));
	            Sheet sheet = workbook.getSheetAt(0);

	            Row dateRangeRow = getRow(sheet, 1);
            	if (dateRangeRow != null) {
            		String dateRange = getStringCellValue(dateRangeRow, 1);
            		logInfo("Sheet dateRange :" +  dateRange);
            		Date sheetStartDate = null;
                    Date sheetEndDate = null;
                    
                    Date staticReportStartDate = null;
                    Date staticReportEndDate = null;
                   
                    if (StringUtils.isNotBlank(dateRange)) {
                    	newDateRangeList.clear();
                    	FamstackDateRange famstackSheetDateRange = DateUtils.parseDateRangeString(dateRange);
                    	sheetStartDate = famstackSheetDateRange.getStartDate();
                    	sheetEndDate = famstackSheetDateRange.getEndDate();
                    	//sheetEndDate =  DateUtils.getNextPreviousDate(DateTimePeriod.DAY_END, famstackSheetDateRange.getEndDate(), 0);
                    	if (startDate.before(sheetStartDate) && endDate.after(sheetEndDate)) {
                    		newDateRangeList.add(new FamstackDateRange(startDate, DateUtils.getNextPreviousDate(DateTimePeriod.DAY, sheetStartDate, -1)));
                    		newDateRangeList.add(new FamstackDateRange(DateUtils.getNextPreviousDate(DateTimePeriod.DAY, sheetEndDate, 1),endDate));
                    		staticReportStartDate = sheetStartDate;
                    		staticReportEndDate = sheetEndDate;
                    		insertLocation = "M";
                    		logInfo("overlapping data both side");
                    	} else if((startDate.after(sheetStartDate) || startDate.equals(sheetStartDate)) && (endDate.before(sheetEndDate) || endDate.equals(sheetEndDate))) {
                    		staticReportStartDate = startDate;
                    		staticReportEndDate = endDate;
                			newDateRangeList.add(new FamstackDateRange(null,null));
                			logInfo("With in static range");
                			insertLocation = "F";
                    	} else if ((startDate.before(sheetStartDate) || startDate.after(sheetEndDate)) && (endDate.after(sheetEndDate) || endDate.before(sheetStartDate))) {
                    		staticReportStartDate = null;
                    		staticReportEndDate = null;
                			newDateRangeList.add(new FamstackDateRange(startDate,endDate));
                			logInfo("out side in static range");
                			insertLocation = "F";
                    	} else if (startDate.before(sheetStartDate) && endDate.equals(sheetStartDate)) {
                    		staticReportStartDate = sheetStartDate;
                    		staticReportEndDate = sheetStartDate;
                			newDateRangeList.add(new FamstackDateRange(startDate,DateUtils.getNextPreviousDate(DateTimePeriod.DAY, sheetStartDate, -1)));
                			logInfo("search data on the left side and static equal start");
                			insertLocation = "E";
                    	} else if (startDate.before(sheetStartDate) && endDate.after(sheetStartDate)) {
                    		staticReportStartDate = sheetStartDate;
                    		staticReportEndDate = endDate;
                			newDateRangeList.add(new FamstackDateRange(startDate,DateUtils.getNextPreviousDate(DateTimePeriod.DAY, sheetStartDate, -1)));
                			logInfo("search data on the left side static on right");
                			insertLocation = "E";
                    	} else if (startDate.before(sheetEndDate) && endDate.after(sheetEndDate)) {
                    		staticReportStartDate = startDate;
                    		staticReportEndDate = sheetEndDate;
                			newDateRangeList.add(new FamstackDateRange(DateUtils.getNextPreviousDate(DateTimePeriod.DAY, sheetEndDate, 1), endDate));
                			logInfo("search data on the right side and static equal end");
                			insertLocation = "F";
                    	} else if (startDate.equals(sheetEndDate) && endDate.after(sheetEndDate)) {
                    		staticReportStartDate = sheetEndDate;
                    		staticReportEndDate = sheetEndDate;
                			newDateRangeList.add(new FamstackDateRange(DateUtils.getNextPreviousDate(DateTimePeriod.DAY, sheetEndDate, 1), endDate));
                			logInfo("search data on the right side and static on left");
                			insertLocation = "F";
                    	}  else if (startDate.equals(sheetStartDate) && endDate.equals(sheetStartDate)) {
                    		staticReportStartDate = sheetStartDate;
                    		staticReportEndDate = sheetStartDate;
                			newDateRangeList.add(new FamstackDateRange(null,null));
                			logInfo("static start and end equal left");
                			insertLocation = "F";
                    	}  else if (startDate.equals(sheetEndDate) && endDate.equals(sheetEndDate)) {
                    		staticReportStartDate = sheetEndDate;
                    		staticReportEndDate = sheetEndDate;
                			newDateRangeList.add(new FamstackDateRange(null,null));
                			logInfo("static start and end equal right");
                			insertLocation = "F";
                    	}
                    	
                    	logDebug("Sheet staticReportStartDate :" +  staticReportStartDate);
                    	logDebug("Sheet staticReportEndDate :" +  staticReportEndDate);
                    	logInfo("newDateRangeList :" +  newDateRangeList);
                    	logDebug("Sheet insertLocation :" +  insertLocation);
                    }
                    
                    if (staticReportStartDate != null&& staticReportEndDate != null) {
                    	Row userCountRow = getRow(sheet, 2);
                        Integer userCount = (int) getNumericCellValue(userCountRow, 1).intValue();
                        
                        Row userDetailsRow = getRow(sheet, 4);
                        int rowCount = 6;
                        int userIdStartIndex = 15;
                        logDebug("Sheet userCount :" +  userCount);
                        List<Integer> sheetUserList = getUserIdList(userDetailsRow, userIdStartIndex, userCount);
                        logDebug("Sheet sheetUserList :" +  sheetUserList);

                    	Row sheetRow = getRow(sheet, rowCount++);
                    	String reportDateString = getStringCellValue(sheetRow, 0);
                    	do{
                    		Date sheetProjectDate = DateUtils.tryParse(reportDateString, DateUtils.DATE_FORMAT);
                    		staticReportStartDate = DateUtils.getNextPreviousDate(DateTimePeriod.DAY_START, staticReportStartDate, 0);
                    		staticReportEndDate = DateUtils.getNextPreviousDate(DateTimePeriod.DAY_END, staticReportEndDate, 0);
                    		if (!sheetProjectDate.before(staticReportStartDate) && !sheetProjectDate.after(staticReportEndDate)) {
                    			
	                    		ProjectTaskActivityDetails projectDetails = new ProjectTaskActivityDetails();
	                    		projectDetails.setTaskActivityStartTime(sheetProjectDate);
	                    		projectDetails.setProjectCode(getStringCellValue(sheetRow, 1));
	                    		
	                    		String projectId = getStringCellValue(sheetRow, 2);
	                    		if (StringUtils.isNotBlank(projectId)) {
	                    			projectDetails.setProjectId(Integer.parseInt(projectId));
	                    		}
	                    		
	                    		projectDetails.setProjectName(getStringCellValue(sheetRow, 4));
	                    		projectDetails.setProjectNumber(getStringCellValue(sheetRow, 5));
	                    		
	                    		String projectStatus = getStringCellValue(sheetRow, 6);
	                    		if (StringUtils.isNotBlank(projectStatus)) {
	                    			projectDetails.setProjectStatus(ProjectStatus.valueOf(projectStatus));
	                    		}
	                    		
	                    		String projectType = getStringCellValue(sheetRow, 7);
	                    		if (StringUtils.isNotBlank(projectType)) {
	                    			projectDetails.setProjectType(ProjectType.valueOf(projectType));
	                    		}
	                    		projectDetails.setProjectCategory(getStringCellValue(sheetRow, 9));
	                    		projectDetails.setAccountName(getStringCellValue(sheetRow, 10));
	                    		projectDetails.setTeamName(getStringCellValue(sheetRow, 11));
	                    		projectDetails.setSubTeamName(getStringCellValue(sheetRow, 12));
	                    		projectDetails.setClientName(getStringCellValue(sheetRow, 13));
	                    		projectDetails.setTaskName(getStringCellValue(sheetRow, 14));
	                    		int numberOfActivities = 0;
	                    		for (int index = 0; index < userCount; index++) {
	                    			Double userTime = getNumericCellValue(sheetRow, userIdStartIndex + index);
	                    			if (userTime != null && userTime > 0.0) {
	                    				if (numberOfActivities == 0) {
		                    				projectDetails.setUserId(sheetUserList.get(index));
		                    				projectDetails.setTaskActivityDuration(convertToActualTimeInMins(userTime));
		                    				projectDetails.setTaskActivityTimeXls(userTime);
	                    				} else {
	                    					ProjectTaskActivityDetails projectTaskActivityDetailsClone = projectDetails.getClone();
	                    					projectTaskActivityDetailsClone.setUserId(sheetUserList.get(index));
	                    					projectTaskActivityDetailsClone.setTaskActivityDuration(convertToActualTimeInMins(userTime));
	                    					projectTaskActivityDetailsClone.setTaskActivityTimeXls(userTime);
	                    					projectTaskAssigneeDataList.add(projectTaskActivityDetailsClone);
	                    				}
	                    				numberOfActivities++;
	                    			}
	                    		}
	                    		projectTaskAssigneeDataList.add(projectDetails);
                    		} else {
                    			logDebug("Excluded date : " + reportDateString);
                    		}
                    		sheetRow = getRow(sheet, rowCount++);
	                    	reportDateString = getStringCellValue(sheetRow, 0);
	                    	
                    	} while (StringUtils.isNotBlank(reportDateString));
                    	
                    	logInfo("Sheet project rowCount :" +  rowCount);
		            }
	            }
	            workbook.close();
        	}
        } catch (IOException | EncryptedDocumentException | InvalidFormatException ex) {
            ex.printStackTrace();
        } 
        Map<String, Object> staticProjectData = new HashMap<>();
        staticProjectData.put("PROJECTDATALIST", projectTaskAssigneeDataList);
        staticProjectData.put("NEWDATERANGELIST", newDateRangeList);
        staticProjectData.put("INSERTLOCATION", insertLocation);
		return staticProjectData;
    }
	
	  private List<Integer> getUserIdList(Row userDetailsRow,
			int userIdStartIndex, Integer userCount) {
		  List<Integer> sheetUserList = new ArrayList<>();
		  for (int index = 0;index < userCount; index++) {
			  sheetUserList.add((int) getNumericCellValue(userDetailsRow, index + userIdStartIndex).intValue());
		  }
		  
		  return sheetUserList;
	}

	private int convertToActualTimeInMins(Double actualTime)
	    {
	        if (actualTime != null && actualTime > 0) {
	            return (int) (actualTime * 24 * 60);
	        }
	        return 0;
	    }

    private String getStringCellValue(Row sheetRow, int cellNumber) {
    	Cell cell = sheetRow.getCell(cellNumber);
		if (sheetRow != null &&  cell != null) {
			if (cell.getCellTypeEnum() == CellType.NUMERIC)  {
				Double cellValue = cell.getNumericCellValue();
				if (cellValue != null && cellValue > 0) {
					return "" + (int) cellValue.intValue();
				}
			} else {
				return cell.getStringCellValue();
			}
		}
		return "";
	}
    
    private Double getNumericCellValue(Row sheetRow, int cellNumber) {
    	try{
    		Cell cell = sheetRow.getCell(cellNumber);
    		if (sheetRow != null &&  cell != null) {
    			if (cell.getCellTypeEnum() == CellType.STRING)  {
    				String cellValue = cell.getStringCellValue();
    				if (StringUtils.isNotBlank(cellValue)) {
    					return Double.parseDouble(cellValue);
    				}
    			} else {
    				return cell.getNumericCellValue();
    			}
    		}
    	} catch(Exception e) {
    		System.out.println(sheetRow.getRowNum() + "  " + cellNumber);
    		logError(e.getMessage());
    	}
		return null;
	}


	private Row getRow(Sheet sheet, int rowIndex)
    {
        Row projectDetailsRow = sheet.getRow(rowIndex);
        if (projectDetailsRow == null) {
            projectDetailsRow = sheet.createRow(rowIndex);
        }
        return projectDetailsRow;
    }
    

	public Map<String, Object> getAllStaticProjectTaskAssigneeData(FamstackDateRange famstackDateRange, HttpServletRequest request,
	        HttpServletResponse response) {
		return readXLS(famstackDateRange, request, response);
	}

}
