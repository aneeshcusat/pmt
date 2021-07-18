package com.famstack.projectscheduler.export.processors;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import com.famstack.projectscheduler.BaseFamstackService;
import com.famstack.projectscheduler.contants.ProjectType;
import com.famstack.projectscheduler.contants.ReportType;
import com.famstack.projectscheduler.dashboard.bean.POEstimateProjectTaskActivityDetails;
import com.famstack.projectscheduler.employees.bean.DailyTimesheetDumpDetails;
import com.famstack.projectscheduler.employees.bean.UserSiteActivityDetails;
import com.famstack.projectscheduler.employees.bean.UserSiteActivityStatus;
import com.famstack.projectscheduler.employees.bean.UserUtilizationDetails;
import com.famstack.projectscheduler.employees.bean.UserUtilizationWeekWiseDetails;
import com.famstack.projectscheduler.employees.bean.UtilizationByUserSkillOrCategory;
import com.famstack.projectscheduler.employees.bean.UtilizationProjectDetails;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class FamstackXLSReportProcessor extends BaseFamstackService
    implements FamstackBaseXLSExportProcessor {
  private XSSFColor colorIndexYellow = null;
  private XSSFColor colorIndexRed = null;
  private XSSFColor colorIndexLightBlue = null;
  private XSSFCellStyle headerCellStyle = null;
  private XSSFCellStyle valueCellStyle = null;
  private XSSFCellStyle boldCenterRedCellStyle = null;
  private XSSFCellStyle boldCenterYelloCellStyle = null;
  private XSSFCellStyle boldCenterValueCellStyle = null;
  private XSSFCellStyle boldCenterTimeValueCellStyle = null;
  private XSSFCellStyle boldCenterTimeYelloCellStyle = null;
  private XSSFCellStyle valueDateCellStyle = null;
  private XSSFCellStyle valueDateTimeCellStyle = null;
  

  @Override
  public void renderReport(Map<String, Object> dataMap) {
    XSSFSheet sheet = (XSSFSheet) dataMap.get("sheet");
    XSSFWorkbook workBook = (XSSFWorkbook) dataMap.get("workbook");
    if (sheet != null) {
      headerCellStyle = initializeCellStyle(workBook, getColor(105, 213, 237, colorIndexLightBlue),
          true, headerCellStyle, null);
      boldCenterRedCellStyle = initializeCellStyle(workBook, getColor(255, 0, 0, colorIndexRed),
          true, boldCenterRedCellStyle, HorizontalAlignment.CENTER);
      boldCenterYelloCellStyle =
          initializeCellStyle(workBook, getColor(247, 251, 91, colorIndexYellow), true,
              boldCenterYelloCellStyle, HorizontalAlignment.CENTER);
      valueCellStyle = initializeCellStyle(workBook, null, false, valueCellStyle, null);
      boldCenterValueCellStyle = initializeCellStyle(workBook, null, true, boldCenterValueCellStyle,
          HorizontalAlignment.CENTER);
      
      boldCenterTimeValueCellStyle =  initializeCellStyle(workBook, null, true, boldCenterTimeValueCellStyle,
              HorizontalAlignment.CENTER, "[h]:mm");
      boldCenterTimeYelloCellStyle = initializeCellStyle(workBook, getColor(247, 251, 91, colorIndexYellow), true,
    		  boldCenterTimeYelloCellStyle, HorizontalAlignment.CENTER, "[h]:mm");
      
      valueDateCellStyle = initializeCellStyle(workBook, null, false, valueDateCellStyle, null, "yyyy/m/dd");
      valueDateTimeCellStyle = initializeCellStyle(workBook, null, false, valueDateTimeCellStyle, null, "yyyy/m/dd hh:mm");

      ReportType reportType = (ReportType) dataMap.get("reportType");
      if (ReportType.USER_SITE_ACTIVITY == reportType) {
        fillUserActivityReportData(dataMap, sheet, workBook);
      } else if (ReportType.USER_UTILIZATION == reportType) {
        fillUserUtilizationReportData(dataMap, sheet, workBook);
      } else if (ReportType.WEEKWISE_USER_UTILIZATION_MONTHLY == reportType) {
        fillUserUtilizationMonthlyReportData(dataMap, sheet, workBook);
      } else if (ReportType.WEEKWISE_USER_UTILIZATION_MONTHLY_ENE == reportType) {
        fillUserUtilizationMonthlyReportData(dataMap, sheet, workBook);
      } else if (ReportType.DAILYWISE_USER_UTILIZATION_WEEKLY == reportType) {
        // TODO :
      } else if (ReportType.WEEKLY_PO_ESTIMATION == reportType) {
        fillPOEstimationReportData(dataMap, sheet, workBook);
      } else if (ReportType.WEEKLY_PROJECT_HOURS == reportType) {
        fillProjectHoursReportData(dataMap, sheet, workBook);
      }  else if (ReportType.TIME_SHEET_DUMP == reportType) {
          fillTimeSheetDumpReportData(dataMap, sheet, workBook);
        } else  if(ReportType.UTILIZATION_BY_SKILLS == reportType || ReportType.UTILIZATION_BY_EMPLOYEE_BY_SKILLS == reportType || ReportType.UTILIZATION_BY_EMPLOYEE_BY_PROJECT_CATEGORY == reportType) {
          fillUtilizationBySkillsReportData(dataMap, sheet, workBook, reportType);
        }
    }
  }

  private void fillTimeSheetDumpReportData(Map<String, Object> dataMap, XSSFSheet sheet, XSSFWorkbook workBook) {
	  
	  setCellValue(sheet, 0, 0, "Sl No", headerCellStyle);
	    
	    setCellValue(sheet, 0, 1, "Employee Name", headerCellStyle);
	    setCellValue(sheet, 0, 2, "Employee Code", headerCellStyle);
	    setCellValue(sheet, 0, 3, "Delivery Lead", headerCellStyle);
	    setCellValue(sheet, 0, 4, "Client", headerCellStyle);
	    setCellValue(sheet, 0, 5, "Project code", headerCellStyle);
	    setCellValue(sheet, 0, 6, "ID", headerCellStyle);
	    setCellValue(sheet, 0, 7, "PO Id", headerCellStyle);
	    setCellValue(sheet, 0, 8, "Order book Ref", headerCellStyle);
	    setCellValue(sheet, 0, 9, "Proposal Number", headerCellStyle);
	    setCellValue(sheet, 0, 10, "Project Name", headerCellStyle);
	    setCellValue(sheet, 0, 11, "Project Status", headerCellStyle);
	    setCellValue(sheet, 0, 12, "Project Type", headerCellStyle);
	    setCellValue(sheet, 0, 13, "Project Category", headerCellStyle);
	    setCellValue(sheet, 0, 14, "New Project Category", headerCellStyle);
	    setCellValue(sheet, 0, 15, "Task Name", headerCellStyle);
	    setCellValue(sheet, 0, 16, "Account", headerCellStyle);
	    setCellValue(sheet, 0, 17, "Team", headerCellStyle);
	    setCellValue(sheet, 0, 18, "Sub Team", headerCellStyle);
	    setCellValue(sheet, 0, 19, "Date", headerCellStyle);
	    setCellValue(sheet, 0, 20, "Actual Hours Spent", headerCellStyle);
	    setCellValue(sheet, 0, 21, "comment", headerCellStyle);
	    setCellValue(sheet, 0, 22, "Updated Date & Time", headerCellStyle);

	    List<DailyTimesheetDumpDetails> data =
	        (List<DailyTimesheetDumpDetails>) dataMap.get("DATA");

	    int rowIndex = 1;
	    for (DailyTimesheetDumpDetails dailyTimesheetDumpDetails : data) {
	      setCellValue(sheet, rowIndex, 0, "" + rowIndex, null);
	      setCellValue(sheet, rowIndex, 1, dailyTimesheetDumpDetails.getFullName(), null);
	      setCellValue(sheet, rowIndex, 2, dailyTimesheetDumpDetails.getEmployeeeId(), null);
	      setCellValue(sheet, rowIndex, 3, dailyTimesheetDumpDetails.getDeliveryLead(), null);
	      setCellValue(sheet, rowIndex, 4, dailyTimesheetDumpDetails.getClientName(), null);
	      setCellValue(sheet, rowIndex, 5, dailyTimesheetDumpDetails.getProjectCode(), null);
	      setCellValue(sheet, rowIndex, 6, dailyTimesheetDumpDetails.getProjectId(), null);
	      
	      setCellValue(sheet, rowIndex, 7, dailyTimesheetDumpDetails.getProjectNumber(), null);
	      setCellValue(sheet, rowIndex, 8, dailyTimesheetDumpDetails.getOrderRefNumber(), null);
	      setCellValue(sheet, rowIndex, 9, dailyTimesheetDumpDetails.getProposalNumber(), null);
	      setCellValue(sheet, rowIndex, 10, "" + dailyTimesheetDumpDetails.getProjectName(),
	          null);
	      setCellValue(sheet, rowIndex, 11, dailyTimesheetDumpDetails.getProjectStatus(), null);
	      setCellValue(sheet, rowIndex, 12, toString(dailyTimesheetDumpDetails.getProjectType()),
	          null);
	      setCellValue(sheet, rowIndex, 13, dailyTimesheetDumpDetails.getProjectCategory(),
		          null);
	      setCellValue(sheet, rowIndex, 14, dailyTimesheetDumpDetails.getNewProjectCategory(), null);
	      setCellValue(sheet, rowIndex, 15,
	          dailyTimesheetDumpDetails.getTaskName(), null);
	      setCellValue(sheet, rowIndex, 16,
	          dailyTimesheetDumpDetails.getAccountName(), null);
	      setCellValue(sheet, rowIndex, 17,
	          dailyTimesheetDumpDetails.getTeamName(),
	          null);
	      setCellValue(sheet, rowIndex, 18,
	          dailyTimesheetDumpDetails.getSubTeamName(),
	          null);
	      setCellValue(sheet, rowIndex, 19,
	           dailyTimesheetDumpDetails.getTaskActivityStartTime(),
	           valueDateCellStyle);
	      setCellValue(sheet, rowIndex, 20,convertToActualTimeString(dailyTimesheetDumpDetails.getActDurationInMins()),
	          boldCenterTimeValueCellStyle);
	      setCellValue(sheet, rowIndex, 21,
		          dailyTimesheetDumpDetails.getTaskCompletionComments(),
		          null);
	      setCellValue(sheet, rowIndex, 22,
		          dailyTimesheetDumpDetails.getLastModifiedTime(),
		          valueDateTimeCellStyle);
	      rowIndex++;
	    }
	
}

private String toString(Object value) {
	return value !=null ? value.toString() : "";
}

private void fillUtilizationBySkillsReportData(Map<String, Object> dataMap, XSSFSheet sheet, XSSFWorkbook workBook, ReportType reportType) {
	  	setCellValue(sheet, 0, 0, "Sl No", headerCellStyle);
	  	 int rowHeaderCount = 1;
	  	 if(ReportType.UTILIZATION_BY_EMPLOYEE_BY_SKILLS == reportType || ReportType.UTILIZATION_BY_EMPLOYEE_BY_PROJECT_CATEGORY == reportType) {
	  		  setCellValue(sheet, 0, rowHeaderCount++, "Employee Code", headerCellStyle);
	  		  setCellValue(sheet, 0, rowHeaderCount++, "Employe Name", headerCellStyle);
	  		  setCellValue(sheet, 0, rowHeaderCount++, "Designation", headerCellStyle);
	      }
	  	 if(ReportType.UTILIZATION_BY_EMPLOYEE_BY_PROJECT_CATEGORY == reportType) {
	  		 setCellValue(sheet, 0, rowHeaderCount++, "Category", headerCellStyle);
	  		 setCellValue(sheet, 0, rowHeaderCount++, "Accounts", headerCellStyle);
	  	 } else {
	  		 setCellValue(sheet, 0, rowHeaderCount++, "Skillset", headerCellStyle);
	  	 }
	    setCellValue(sheet, 0, rowHeaderCount++, "Month", headerCellStyle);
	    setCellValue(sheet, 0, rowHeaderCount++, "Billable Hours", headerCellStyle);
	    setCellValue(sheet, 0, rowHeaderCount++, "Non Billable Hours", headerCellStyle);
	    setCellValue(sheet, 0, rowHeaderCount++, "Total Hours", headerCellStyle);

	    List<UtilizationByUserSkillOrCategory> data = (List<UtilizationByUserSkillOrCategory>) dataMap.get("DATA");

	    int rowIndex = 1;
	    for (UtilizationByUserSkillOrCategory byUserSkillOrCategory : data) {
	      setCellValue(sheet, rowIndex, 0, "" + rowIndex, null);
	      int rowCount = 1;
	      if(ReportType.UTILIZATION_BY_EMPLOYEE_BY_SKILLS == reportType || ReportType.UTILIZATION_BY_EMPLOYEE_BY_PROJECT_CATEGORY == reportType) {
	    	  setCellValue(sheet, rowIndex, rowCount++, byUserSkillOrCategory.getEmployeeCode(), null);
	    	  setCellValue(sheet, rowIndex, rowCount++, byUserSkillOrCategory.getEmployeeName(), null);
	    	  setCellValue(sheet, rowIndex, rowCount++, byUserSkillOrCategory.getDesignation(), null);
	      }
	    
	      setCellValue(sheet, rowIndex, rowCount++, byUserSkillOrCategory.getSkillOrCategory(), null);
	      if(ReportType.UTILIZATION_BY_EMPLOYEE_BY_PROJECT_CATEGORY == reportType){
	    	  setCellValue(sheet, rowIndex, rowCount++, byUserSkillOrCategory.getProjectAccounts() != null && byUserSkillOrCategory.getProjectAccounts().size() > 0 ? byUserSkillOrCategory.getProjectAccounts().toString() : "", null);
	      }
	      setCellValue(sheet, rowIndex, rowCount++, byUserSkillOrCategory.getMonthYear(), null);
	      setCellValue(sheet, rowIndex, rowCount++, byUserSkillOrCategory.getBillableHours(),
	          boldCenterValueCellStyle);
	      setCellValue(sheet, rowIndex, rowCount++, byUserSkillOrCategory.getNonBillableHours(),
	          boldCenterValueCellStyle);
	      setCellValue(sheet, rowIndex, rowCount++, byUserSkillOrCategory.getTotalHrs(),
	          boldCenterValueCellStyle);
	      rowIndex++;
	    }
	
}

private void setCellValue(Sheet sheet, int rowIndex, int colIndex, String value,
      CellStyle cellStyle) {
    Row row = getRow(sheet, rowIndex);
    Cell cell = getCell(sheet, row, colIndex);
    cell.setCellValue(sanitizeCellValue(value));
    if (cellStyle != null) {
      cell.setCellStyle(cellStyle);
    } else {
      cell.setCellStyle(valueCellStyle);
    }
    if (rowIndex < 2) {
      sheet.autoSizeColumn(colIndex);
    }
  }

private void setCellValue(Sheet sheet, int rowIndex, int colIndex, Double value,
	      CellStyle cellStyle) {
	    Row row = getRow(sheet, rowIndex);
	    Cell cell = getCell(sheet, row, colIndex);
	    cell.setCellValue(value);
	    if (cellStyle != null) {
	      cell.setCellStyle(cellStyle);
	    } else {
	      cell.setCellStyle(valueCellStyle);
	    }
	    if (rowIndex < 2) {
	      sheet.autoSizeColumn(colIndex);
	    }
	  }
private void setCellValue(Sheet sheet, int rowIndex, int colIndex, Date value,
	      CellStyle cellStyle) {
	    Row row = getRow(sheet, rowIndex);
	    Cell cell = getCell(sheet, row, colIndex);
	    if(value != null){
	    	cell.setCellValue(value);
	    }
	    if (cellStyle != null) {
	      cell.setCellStyle(cellStyle);
	    } else {
	      cell.setCellStyle(valueCellStyle);
	    }
	    if (rowIndex < 2) {
	      sheet.autoSizeColumn(colIndex);
	    }
	  }

  private void fillUserActivityReportData(Map<String, Object> dataMap, Sheet sheet,
      XSSFWorkbook workBook) {

    setCellValue(sheet, 0, 0, "Sl No", headerCellStyle);
    setCellValue(sheet, 0, 1, "Employee Name", headerCellStyle);
    setCellValue(sheet, 0, 2, "Reporting Manager", headerCellStyle);
    List<String> dateList = (List<String>) dataMap.get("DATE_LIST");
    int colIndex = 0;
    for (String headerDate : dateList) {
      setCellValue(sheet, 0, 3 + colIndex, "Status (" + headerDate + ")", headerCellStyle);
      colIndex++;
    }

    List<UserSiteActivityDetails> data = (List<UserSiteActivityDetails>) dataMap.get("DATA");

    int rowIndex = 1;
    for (UserSiteActivityDetails userSiteActivityDetails : data) {
      setCellValue(sheet, rowIndex, 0, "" + rowIndex, null);
      setCellValue(sheet, rowIndex, 1, userSiteActivityDetails.getEmployeeName(), null);
      setCellValue(sheet, rowIndex, 2, userSiteActivityDetails.getReportingManager(), null);
      colIndex = 0;
      for (UserSiteActivityStatus status : userSiteActivityDetails.getStatusList()) {
        if ("Active".equalsIgnoreCase(status.getStatus())) {
          setCellValue(sheet, rowIndex, 3 + colIndex, status.getStatus(), boldCenterValueCellStyle);
        } else {
          setCellValue(sheet, rowIndex, 3 + colIndex, status.getStatus(), boldCenterRedCellStyle);
        }
        colIndex++;
      }
      rowIndex++;
    }
  }

  private void fillUserUtilizationReportData(Map<String, Object> dataMap, Sheet sheet,
      XSSFWorkbook workBook) {

    setCellValue(sheet, 0, 0, "Sl No", headerCellStyle);
    setCellValue(sheet, 0, 1, "Employee Name", headerCellStyle);
    setCellValue(sheet, 0, 2, "Employee Code", headerCellStyle);
    setCellValue(sheet, 0, 3, "Reporting Manager", headerCellStyle);
    setCellValue(sheet, 0, 4, "Billable Hours", headerCellStyle);
    setCellValue(sheet, 0, 5, "Non Billable Hours", headerCellStyle);
    setCellValue(sheet, 0, 6, "Leave or Holiday", headerCellStyle);
    setCellValue(sheet, 0, 7, "Utilization", headerCellStyle);
    setCellValue(sheet, 0, 8, "Total Hours", headerCellStyle);

    List<UserUtilizationDetails> data = (List<UserUtilizationDetails>) dataMap.get("DATA");

    int rowIndex = 1;
    for (UserUtilizationDetails userUtilizationDetails : data) {
      setCellValue(sheet, rowIndex, 0, "" + rowIndex, null);
      setCellValue(sheet, rowIndex, 1, userUtilizationDetails.getEmployeeName(), null);
      setCellValue(sheet, rowIndex, 2, userUtilizationDetails.getEmpId(), null);
      setCellValue(sheet, rowIndex, 3, userUtilizationDetails.getReportingManager(), null);
      setCellValue(sheet, rowIndex, 4, convertToActualTimeString(userUtilizationDetails.getBillableMins()),
          boldCenterTimeValueCellStyle);
      setCellValue(sheet, rowIndex, 5, convertToActualTimeString(userUtilizationDetails.getNonBillableMins()),
          boldCenterTimeValueCellStyle);
      if (userUtilizationDetails.getLeaveOrHolidayMins() > 0) {
        setCellValue(sheet, rowIndex, 6, convertToActualTimeString(userUtilizationDetails.getLeaveOrHolidayMins()),
            boldCenterTimeYelloCellStyle);
      } else {
        setCellValue(sheet, rowIndex, 6, convertToActualTimeString(userUtilizationDetails.getLeaveOrHolidayMins()),
            boldCenterTimeValueCellStyle);
      }
      if (userUtilizationDetails.isUnderOrOverUtilized()) {
        setCellValue(sheet, rowIndex, 7, userUtilizationDetails.getUtilization() + " %",
            boldCenterRedCellStyle);
      } else {
        setCellValue(sheet, rowIndex, 7, userUtilizationDetails.getUtilization() + " %",
            boldCenterValueCellStyle);
      }

      setCellValue(sheet, rowIndex, 8, convertToActualTimeString(userUtilizationDetails.getTotalMins()),
          boldCenterTimeValueCellStyle);
      rowIndex++;
    }
  }

  private void fillUserUtilizationMonthlyReportData(Map<String, Object> dataMap, Sheet sheet,
      XSSFWorkbook workBook) {
    setCellValue(sheet, 0, 0, "", null);
    sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 2));

    setCellValue(sheet, 1, 0, "Sl No", headerCellStyle);
    setCellValue(sheet, 1, 1, "Employee Name", headerCellStyle);
    setCellValue(sheet, 1, 2, "Employee Code", headerCellStyle);
    setCellValue(sheet, 1, 3, "Reporting Manager", headerCellStyle);

    List<String> weekList = (List<String>) dataMap.get("WEEK_LIST");
    Map<String, String> dateMap = (Map<String, String>) dataMap.get("DATE_LIST");
    int colIndex = 0;
    for (String weekNumber : weekList) {
      sheet.addMergedRegion(new CellRangeAddress(0, 0, 3 + colIndex, 3 + colIndex + 3));
      setCellValue(sheet, 0, 4 + colIndex, dateMap.get(weekNumber), boldCenterValueCellStyle);
      setCellValue(sheet, 1, 4 + colIndex++, "Billable", headerCellStyle);
      setCellValue(sheet, 1, 4 + colIndex++, "Non Billable", headerCellStyle);
      setCellValue(sheet, 1, 4 + colIndex++, "Leave/Holiday", headerCellStyle);
      setCellValue(sheet, 1, 4 + colIndex++, "Total Hours", headerCellStyle);
    }

    List<UserUtilizationWeekWiseDetails> data =
        (List<UserUtilizationWeekWiseDetails>) dataMap.get("DATA");

    int rowIndex = 2;
    for (UserUtilizationWeekWiseDetails userUtilizationWeekWiseDetails : data) {
      setCellValue(sheet, rowIndex, 0, "" + (rowIndex - 1), null);
      setCellValue(sheet, rowIndex, 1, userUtilizationWeekWiseDetails.getEmployeeName(), null);
      setCellValue(sheet, rowIndex, 2, userUtilizationWeekWiseDetails.getEmpId(), null);
      setCellValue(sheet, rowIndex, 3, userUtilizationWeekWiseDetails.getReportingManager(), null);

      colIndex = 0;
      for (String weekNumber : weekList) {
        if (userUtilizationWeekWiseDetails.getUserUtilizationMap().get(weekNumber)
            .getBillableMins() > 0) {
          setCellValue(
              sheet, rowIndex, 4 + colIndex++, userUtilizationWeekWiseDetails
                  .getUserUtilizationMap().get(weekNumber).getBillableHours(),
              boldCenterValueCellStyle);
        } else {
          setCellValue(sheet, rowIndex, 4 + colIndex++, "", boldCenterValueCellStyle);
        }
        if (userUtilizationWeekWiseDetails.getUserUtilizationMap().get(weekNumber)
            .getNonBillableMins() > 0) {
          setCellValue(
              sheet, rowIndex, 4 + colIndex++, userUtilizationWeekWiseDetails
                  .getUserUtilizationMap().get(weekNumber).getNonBillableHours(),
              boldCenterValueCellStyle);
        } else {
          setCellValue(sheet, rowIndex, 4 + colIndex++, "", boldCenterValueCellStyle);
        }
        if (userUtilizationWeekWiseDetails.getUserUtilizationMap().get(weekNumber)
            .getLeaveOrHolidayMins() > 0) {
          setCellValue(
              sheet, rowIndex, 4 + colIndex++, userUtilizationWeekWiseDetails
                  .getUserUtilizationMap().get(weekNumber).getLeaveOrHolidayHours(),
              boldCenterYelloCellStyle);
        } else {
          setCellValue(sheet, rowIndex, 4 + colIndex++, "", boldCenterValueCellStyle);
        }
        if (userUtilizationWeekWiseDetails.getUserUtilizationMap().get(weekNumber)
            .getTotalWithLeaveMins() > 0) {
          if (userUtilizationWeekWiseDetails.getUserUtilizationMap().get(weekNumber)
              .isNotifyUsers()) {
            setCellValue(
                sheet, rowIndex, 4 + colIndex++, userUtilizationWeekWiseDetails
                    .getUserUtilizationMap().get(weekNumber).getTotalWithLeaveHrs(),
                boldCenterRedCellStyle);
          } else {
            setCellValue(
                sheet, rowIndex, 4 + colIndex++, userUtilizationWeekWiseDetails
                    .getUserUtilizationMap().get(weekNumber).getTotalWithLeaveHrs(),
                boldCenterValueCellStyle);
          }
        } else {
          setCellValue(sheet, rowIndex, 4 + colIndex++, "00.00", boldCenterRedCellStyle);
        }
      }
      rowIndex++;

    }
  }

  private void fillPOEstimationReportData(Map<String, Object> dataMap, Sheet sheet,
      XSSFWorkbook workBook) {

    setCellValue(sheet, 0, 0, "Sl No", headerCellStyle);
    
    setCellValue(sheet, 0, 1, "Client Name", headerCellStyle);
    setCellValue(sheet, 0, 2, "Order book ref id", headerCellStyle);
    setCellValue(sheet, 0, 3, "Proposal Number", headerCellStyle);
    setCellValue(sheet, 0, 4, "Offshore/Onshore", headerCellStyle);
    setCellValue(sheet, 0, 5, "Delivery Lead", headerCellStyle);
    setCellValue(sheet, 0, 6, "Team Name", headerCellStyle);
    setCellValue(sheet, 0, 7, "Account Name", headerCellStyle);
    setCellValue(sheet, 0, 8, "Project Name", headerCellStyle);
    setCellValue(sheet, 0, 9, "Project Id", headerCellStyle);
    setCellValue(sheet, 0, 10, "Project Code", headerCellStyle);
    setCellValue(sheet, 0, 11, "PO No", headerCellStyle);
    setCellValue(sheet, 0, 12, "SOW", headerCellStyle);
    setCellValue(sheet, 0, 13, "Start Date", headerCellStyle);
    setCellValue(sheet, 0, 14, "End Date", headerCellStyle);
    setCellValue(sheet, 0, 15, "Current Status", headerCellStyle);
    setCellValue(sheet, 0, 16, "Total Estimation Hours", headerCellStyle);
    setCellValue(sheet, 0, 17, "Consumed Hours", headerCellStyle);
    setCellValue(sheet, 0, 18, "% of Utilization", headerCellStyle);

    List<POEstimateProjectTaskActivityDetails> data =
        (List<POEstimateProjectTaskActivityDetails>) dataMap.get("DATA");

    int rowIndex = 1;
    for (POEstimateProjectTaskActivityDetails pOEstimateProjectTaskActivityDetails : data) {
      setCellValue(sheet, rowIndex, 0, "" + rowIndex, null);
      setCellValue(sheet, rowIndex, 1, pOEstimateProjectTaskActivityDetails.getClientName(), null);
      setCellValue(sheet, rowIndex, 2, pOEstimateProjectTaskActivityDetails.getOrderBookId(), null);
      setCellValue(sheet, rowIndex, 3, pOEstimateProjectTaskActivityDetails.getProposalNumber(), null);
      setCellValue(sheet, rowIndex, 4, pOEstimateProjectTaskActivityDetails.getLocation(), null);
      setCellValue(sheet, rowIndex, 5, pOEstimateProjectTaskActivityDetails.getDeliveryLeadName(), null);
      
      setCellValue(sheet, rowIndex, 6, pOEstimateProjectTaskActivityDetails.getTeamName(), null);
      setCellValue(sheet, rowIndex, 7, pOEstimateProjectTaskActivityDetails.getAccountName(), null);
      setCellValue(sheet, rowIndex, 8, pOEstimateProjectTaskActivityDetails.getProjectName(), null);
      setCellValue(sheet, rowIndex, 9, "" + pOEstimateProjectTaskActivityDetails.getProjectId(),
          null);
      setCellValue(sheet, rowIndex, 10, pOEstimateProjectTaskActivityDetails.getProjectCode(), null);
      setCellValue(sheet, rowIndex, 11, pOEstimateProjectTaskActivityDetails.getProjectNumber(),
          null);
      setCellValue(sheet, rowIndex, 12, pOEstimateProjectTaskActivityDetails.getSowLineItem(), null);
      setCellValue(sheet, rowIndex, 13,
          pOEstimateProjectTaskActivityDetails.getProjectStartTimeFormated(), null);
      setCellValue(sheet, rowIndex, 14,
          pOEstimateProjectTaskActivityDetails.getProjectCompletionTimeFormated(), null);
      setCellValue(sheet, rowIndex, 15,
          pOEstimateProjectTaskActivityDetails.getProjectEstimateStatus(),
          boldCenterValueCellStyle);
      setCellValue(sheet, rowIndex, 16,
          "" + pOEstimateProjectTaskActivityDetails.getProjectDurationHrs(),
          boldCenterValueCellStyle);
      setCellValue(sheet, rowIndex, 17,
          "" + pOEstimateProjectTaskActivityDetails.getTaskActivityDurationHrs(),
          boldCenterValueCellStyle);
      setCellValue(sheet, rowIndex, 18,
          pOEstimateProjectTaskActivityDetails.getUtilizationString() + "%",
          boldCenterValueCellStyle);
      rowIndex++;
    }
  }

  private void fillProjectHoursReportData(Map<String, Object> dataMap, Sheet sheet,
      XSSFWorkbook workBook) {

    setCellValue(sheet, 0, 0, "Sl No", headerCellStyle);
    setCellValue(sheet, 0, 1, "Team Name", headerCellStyle);
    setCellValue(sheet, 0, 2, "Account Name", headerCellStyle);
    setCellValue(sheet, 0, 3, "Year", headerCellStyle);
    setCellValue(sheet, 0, 4, "Month", headerCellStyle);
    setCellValue(sheet, 0, 5, "Week No", headerCellStyle);
    setCellValue(sheet, 0, 6, "Client Name", headerCellStyle);
    setCellValue(sheet, 0, 7, "PO No", headerCellStyle);
    setCellValue(sheet, 0, 8, "Project Name", headerCellStyle);
    setCellValue(sheet, 0, 9, "Start Date", headerCellStyle);
    setCellValue(sheet, 0, 10, "End Date", headerCellStyle);
    setCellValue(sheet, 0, 11, "Team Members", headerCellStyle);
    setCellValue(sheet, 0, 12, "Funded/Non Funded Resource", headerCellStyle);
    setCellValue(sheet, 0, 13, "Owner Name", headerCellStyle);
    setCellValue(sheet, 0, 14, "Estimated Hours", headerCellStyle);
    setCellValue(sheet, 0, 15, "Actual Hours", headerCellStyle);
    setCellValue(sheet, 0, 16, "Leave/Holiday", headerCellStyle);

    List<UserUtilizationDetails> data = (List<UserUtilizationDetails>) dataMap.get("DATA");

    int rowIndex = 1;
    int itemIndex = 1;
    for (UserUtilizationDetails userUtilizationDetails : data) {
      boolean canIncrement = true;
      setCellValue(sheet, rowIndex, 0, "" + itemIndex, null);
      setCellValue(sheet, rowIndex, 11, "" + userUtilizationDetails.getTeamMembers(), null);
      setCellValue(sheet, rowIndex, 12, ""
          + (userUtilizationDetails.getFunded() != null ? userUtilizationDetails.getFunded() : ""),
          null);
      setCellValue(sheet, rowIndex, 13, "" + userUtilizationDetails.getEmployeeName(), null);
      setCellValue(sheet, rowIndex, 14, "" + userUtilizationDetails.getEstimatedHours(),
          boldCenterValueCellStyle);
      setCellValue(sheet, rowIndex, 15, "" + userUtilizationDetails.getActualHours(),
          boldCenterValueCellStyle);
      setCellValue(sheet, rowIndex, 16, "" + userUtilizationDetails.getLeaveOrHolidayHours(),
          boldCenterValueCellStyle);

      for (UtilizationProjectDetails utilizationProjectDetails : userUtilizationDetails
          .getUtilizationProjectDetailsList()) {
        setCellValue(sheet, rowIndex, 1, utilizationProjectDetails.getTeamName(), null);
        setCellValue(sheet, rowIndex, 2, utilizationProjectDetails.getAccountName(), null);
        setCellValue(sheet, rowIndex, 3, "" + utilizationProjectDetails.getYear(), null);
        setCellValue(sheet, rowIndex, 4, "" + utilizationProjectDetails.getMonth(), null);
        setCellValue(sheet, rowIndex, 5, "" + utilizationProjectDetails.getWeekNumber(), null);
        setCellValue(sheet, rowIndex, 6, utilizationProjectDetails.getClientName(), null);
        setCellValue(sheet, rowIndex, 7, utilizationProjectDetails.getProjectNumber(), null);
        setCellValue(sheet, rowIndex, 8, utilizationProjectDetails.getProjectName(), null);
        setCellValue(sheet, rowIndex, 9, utilizationProjectDetails.getStartDateString(), null);
        setCellValue(sheet, rowIndex, 10, utilizationProjectDetails.getEndDateString(), null);
        rowIndex++;
        canIncrement = false;
      }
      if (canIncrement) {
        rowIndex++;
      }
      itemIndex++;
    }
  }

  private Cell getCell(Sheet sheet, Row row, int cellNumber) {
    Cell cell = row.getCell(cellNumber);
    if (cell == null) {
      cell = row.createCell(cellNumber);
    }
    return cell;
  }

  private Row getRow(Sheet sheet, int rowIndex) {
    Row projectDetailsRow = sheet.getRow(rowIndex);
    if (projectDetailsRow == null) {
      projectDetailsRow = sheet.createRow(rowIndex);
    }
    return projectDetailsRow;
  }
  private XSSFCellStyle initializeCellStyle(XSSFWorkbook workbook, XSSFColor colorIndex,
	      boolean isBold, XSSFCellStyle cellStyle, HorizontalAlignment horizontalAlignment) {
	  return initializeCellStyle(workbook, colorIndex, isBold, cellStyle, horizontalAlignment, null);
  }

  private XSSFCellStyle initializeCellStyle(XSSFWorkbook workbook, XSSFColor colorIndex,
      boolean isBold, XSSFCellStyle cellStyle, HorizontalAlignment horizontalAlignment, String dateFormat) {
    if (cellStyle == null) {
      cellStyle = workbook.createCellStyle();
      if (colorIndex != null) {
        cellStyle.setFillForegroundColor(colorIndex);
        cellStyle.setFillBackgroundColor(colorIndex);
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
      }
      Font font = workbook.createFont();
      font.setBold(isBold);
      if (horizontalAlignment != null) {
        cellStyle.setAlignment(horizontalAlignment);
      }
      cellStyle.setFont(font);
      cellStyle.setBorderBottom(BorderStyle.THIN);
      cellStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());

      cellStyle.setBorderRight(BorderStyle.THIN);
      cellStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());

      cellStyle.setBorderLeft(BorderStyle.THIN);
      cellStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());

      cellStyle.setBorderTop(BorderStyle.THIN);
      cellStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());
      
      if(dateFormat != null) {
    	CreationHelper createHelper = workbook.getCreationHelper();
      	cellStyle.setDataFormat(createHelper.createDataFormat().getFormat(dateFormat));
      }
    }
    return cellStyle;
  }

  private XSSFColor getColor(int R, int G, int B, XSSFColor color) {
    if (color == null) {
      color = new XSSFColor(new java.awt.Color(R, G, B));
    }
    return color;
  }
  
  private double convertToActualTimeString(Integer timeInMins)
  {

      if (timeInMins != null && timeInMins > 0) {
          int hour = timeInMins / 60;
          int minutes = timeInMins % 60;
          return hour / 24d + (minutes / 60d) / 24d;
      }

      return 0;

  }
}
