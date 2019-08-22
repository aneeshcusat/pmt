package com.famstack.projectscheduler.export.processors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.famstack.projectscheduler.BaseFamstackService;
import com.famstack.projectscheduler.dashboard.bean.ProjectTaskActivityDetails;
import com.famstack.projectscheduler.datatransferobject.UserTaskActivityItem;
import com.famstack.projectscheduler.employees.bean.EmployeeDetails;
import com.famstack.projectscheduler.employees.bean.ProjectDetails;
import com.famstack.projectscheduler.employees.bean.TaskDetails;
import com.famstack.projectscheduler.manager.FamstackUserActivityManager;
import com.famstack.projectscheduler.util.DateUtils;
import com.famstack.projectscheduler.util.StringUtils;

/**
 * visual service
 * 
 * @author Aneeshkumar
 */
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class FamstackXLSExportProcessorDefaultBck extends BaseFamstackService implements FamstackBaseXLSExportProcessor
{

    @Resource
    FamstackUserActivityManager famstackUserActivityManager;

    private int maxThread;

    @Override
    public void renderReport(Map<String, Object> dataMap)

    {
        XSSFWorkbook workBook = (XSSFWorkbook) dataMap.get("workBook");
        Sheet sheet = (Sheet) dataMap.get("sheet");
        String teamName = (String) dataMap.get("teamName");
        List<ProjectTaskActivityDetails> exportDataList =
            (List<ProjectTaskActivityDetails>) dataMap.get("exportDataList");
        String dateString = (String) dataMap.get("dateString");
        List<EmployeeDetails> employees = (List<EmployeeDetails>) dataMap.get("employees");
        List<EmployeeDetails> deletedEmployees = getDeletedEmployees(employees, exportDataList);
        employees.addAll(deletedEmployees);
     
        Map<String, Map<Integer, Integer>> nonBillableTaskActivities =
            (Map<String, Map<Integer, Integer>>) dataMap.get("nonBillableTaskActivities");

        List<EmployeeDetails> deletedNonBillableEmployees = getDeletedEmployees(employees, nonBillableTaskActivities);
        employees.addAll(deletedNonBillableEmployees);
     
        logDebug("Rendering reoprt for  FamstackXLSExportProcessor11");

        int holidayHours = 0;

        XSSFColor myColorIndexYellow = getColor(255, 192, 0, workBook);
        XSSFColor myColorIndexGray = getColor(217, 217, 217, workBook);
        XSSFColor myColorIndexLightGray = getColor(242, 242, 242, workBook);
        XSSFColor myColorIndexLightGreen = getColor(198, 224, 180, workBook);

        CellStyle xssfCellProjectTaskHrsStyle = getProjectTotalStyle(workBook, null);
        CellStyle xssfCellUserHeaderStyle = getUserHeaderStyle(workBook, myColorIndexGray);
        CellStyle xssfCellUtilizationStyle = getCellStyle(workBook, myColorIndexLightGreen);
        CellStyle xssfCellProjectTotalStyle = getProjectTotalStyle(workBook, myColorIndexLightGray);
        CellStyle xssfCellProjectGrandTotalStyle = getProjectTotalStyle(workBook, myColorIndexYellow);
        CellStyle hiddenStyle = getHiddenContentStyle(workBook);

        int availableWorkingDayHours = getWorkingDaysHours(dateString);
        String sheetName = getSheetNameFromDate(dateString);
        workBook.setSheetName(0, sheetName);

        CellStyle xssfCellTextWrapStyle = getTextWrapStyle(workBook);
        createHeader(workBook, sheet, dateString, teamName, xssfCellUserHeaderStyle, xssfCellProjectTotalStyle,
            employees, hiddenStyle);
        createBody(workBook, sheet, exportDataList, employees, xssfCellProjectTotalStyle, xssfCellTextWrapStyle,
            xssfCellProjectTaskHrsStyle, xssfCellProjectGrandTotalStyle, xssfCellUtilizationStyle,
            availableWorkingDayHours, holidayHours, nonBillableTaskActivities, hiddenStyle);

    }

    private List<EmployeeDetails> getDeletedEmployees(
			List<EmployeeDetails> employees,
			Map<String, Map<Integer, Integer>> nonBillableTaskActivities) {
    	List<Integer> availableIdList = getEmployeeIndexList(employees);
    	List<EmployeeDetails> deletedEmployeeList = new ArrayList<>();
    	Map<Integer, EmployeeDetails> allEmployeesMap = getFamstackApplicationConfiguration().getAllUsersMap();
    	boolean otherAdded = false;
    	if (nonBillableTaskActivities != null) {
    		 for (String taskCategory : nonBillableTaskActivities.keySet()) {
    			 Map<Integer, Integer> nonBillableMap = nonBillableTaskActivities.get(taskCategory);
    			 for (Integer userId : nonBillableMap.keySet()) {
    				 if (!availableIdList.contains(userId)){
    					 EmployeeDetails employeeDetails = allEmployeesMap.get(userId);
    					 if (employeeDetails != null) {
     						if (!deletedEmployeeList.contains(employeeDetails)) {
     							deletedEmployeeList.add(employeeDetails);	
     						}
	     				} else if (!otherAdded) {
	     					EmployeeDetails otherEmp = new EmployeeDetails();
	 				        otherEmp.setId(0);
	 				        otherEmp.setFirstName("Unknown User");
	 				        deletedEmployeeList.add(otherEmp);
	 				        otherAdded = true;
	     				}
    				 }
    			 }
    			 
    		}
    	}
		return deletedEmployeeList;
	}

	private String getSheetNameFromDate(String dateRange)
    {
        Date startDate = null;
        Date endDate = null;
        if (StringUtils.isNotBlank(dateRange)) {
            String[] dateRanges = dateRange.split("-");

            if (dateRanges != null && dateRanges.length > 1) {
                startDate = DateUtils.tryParse(dateRanges[0].trim(), DateUtils.DATE_FORMAT_DP);
                endDate = DateUtils.tryParse(dateRanges[1].trim(), DateUtils.DATE_FORMAT_DP);

                return DateUtils.format(startDate, "dd MMM YYYY") + " - " + DateUtils.format(endDate, "dd MMM YYYY");
            }
        }
        return "";
    }

    private int getWorkingDaysHours(String dateRange)
    {
        Date startDate = null;
        Date endDate = null;
        if (StringUtils.isNotBlank(dateRange)) {
            String[] dateRanges = dateRange.split("-");

            if (dateRanges != null && dateRanges.length > 1) {
                startDate = DateUtils.tryParse(dateRanges[0].trim(), DateUtils.DATE_FORMAT_DP);
                endDate = DateUtils.tryParse(dateRanges[1].trim(), DateUtils.DATE_FORMAT_DP);
            }
        }

        return DateUtils.getWorkingDaysBetweenTwoDates(startDate, endDate) * 8;
    }

    private void createBody(final XSSFWorkbook workBook, final Sheet sheet,
        final List<ProjectTaskActivityDetails> exportDataList, final List<EmployeeDetails> employees,
        final CellStyle xssfCellProjectTotalStyle, final CellStyle xssfCellTextWrapStyle,
        CellStyle xssfCellProjectTaskHrsStyle, CellStyle xssfCellProjectGrandTotalStyle,
        CellStyle xssfCellUtilizationStyle, int availableWorkingDayHours, int holidayHours,
        Map<String, Map<Integer, Integer>> nonBillableTaskActivities, CellStyle hiddenStyle)
    {
        int projectDetailsRowCount = 6;
        if (exportDataList != null) {
            if (exportDataList.size() > 0) {
                for (ProjectTaskActivityDetails projectDetails : exportDataList) {
                    createProjectDetailsRow(workBook, sheet, projectDetailsRowCount, projectDetails, employees,
                        xssfCellTextWrapStyle, xssfCellProjectTotalStyle, xssfCellProjectTaskHrsStyle, hiddenStyle);
                    projectDetailsRowCount++;
                }
            }
        }

        if (nonBillableTaskActivities != null) {
            // Row blankRow = sheet.getRow((++projectDetailsRowCount) + 1);
            // createProjectSumFunctionCell(blankRow, 11 + employees.size(), xssfCellProjectTotalStyle);
            Map<Integer, Integer> leaveDataMap = nonBillableTaskActivities.get("Leave");
            nonBillableTaskActivities.remove("Leave");

            projectDetailsRowCount++;
            for (String taskCategory : nonBillableTaskActivities.keySet()) {
                createNonBillableDetailsRow(workBook, sheet, projectDetailsRowCount, taskCategory,
                    nonBillableTaskActivities.get(taskCategory), employees, xssfCellTextWrapStyle,
                    xssfCellProjectTotalStyle, xssfCellProjectTaskHrsStyle);

                projectDetailsRowCount++;
            }
            fillLeaveDetails(workBook, sheet, projectDetailsRowCount, leaveDataMap, employees, xssfCellTextWrapStyle,
                xssfCellProjectTotalStyle, xssfCellProjectTaskHrsStyle);
        }

        createTotalSummaryDetailsRows(workBook, sheet, projectDetailsRowCount, employees, xssfCellTextWrapStyle,
            xssfCellProjectTotalStyle, xssfCellProjectTaskHrsStyle, xssfCellProjectGrandTotalStyle,
            xssfCellUtilizationStyle, availableWorkingDayHours, holidayHours);

    }

    private void fillLeaveDetails(XSSFWorkbook workBook, Sheet sheet, int projectDetailsRowCount,
        Map<Integer, Integer> leaveDataMap, List<EmployeeDetails> employees, CellStyle xssfCellTextWrapStyle,
        CellStyle xssfCellProjectTotalStyle, CellStyle xssfCellProjectTaskHrsStyle)
    {

        if (leaveDataMap != null) {
            Row leaveRow = getRow(sheet,projectDetailsRowCount + 2);
            List<Integer> employeeIndexList = getEmployeeIndexList(employees);
            for (Integer userId : leaveDataMap.keySet()) {
                int userCellIndex = employeeIndexList.indexOf(userId);
                if(userCellIndex < 0) {
                	userCellIndex = employeeIndexList.indexOf(0);
                }
                createTaskTimeCell(sheet, 15 + userCellIndex, leaveDataMap.get(userId),null, leaveRow,
                    xssfCellProjectTaskHrsStyle);
            }
        }

    }

    private void createTotalSummaryDetailsRows(XSSFWorkbook workBook, Sheet sheet, int rowCount,
        List<EmployeeDetails> employees, CellStyle xssfCellTextWrapStyle, CellStyle xssfCellProjectTotalStyle,
        CellStyle xssfCellProjectTaskHrsStyle, CellStyle xssfCellProjectGrandTotalStyle,
        CellStyle xssfCellUtilizationStyle, int availableWorkingDayHours, int holidayHours)
    {

        Row totalUserProjectHoursRow = getRow(sheet,rowCount);
        Row holidayRow = getRow(sheet, rowCount + 1);
        Row LeaveHrsRow = getRow(sheet, rowCount + 2);
        Row excessWorkingHrsRow = getRow(sheet, rowCount + 3);
        Row utilizationRow = getRow(sheet, rowCount + 4);
        Row availableHrsRow = getRow(sheet, rowCount + 5);
        Row totalHrsRow = getRow(sheet, rowCount + 6);
        int columnNumber = 15;
        
        createSummaryDetailsLabel(workBook, totalUserProjectHoursRow, columnNumber-1, "Total Task Hours", false);
        createSummaryDetailsLabel(workBook, holidayRow, columnNumber-1, "Public Holiday hours", false);
        createSummaryDetailsLabel(workBook, LeaveHrsRow, columnNumber-1, "Leave hours", false);
        createSummaryDetailsLabel(workBook, holidayRow, columnNumber-8, "NON BILLABLE", false);
        createSummaryDetailsLabel(workBook, LeaveHrsRow, columnNumber-8, "NON BILLABLE", false);
        createSummaryDetailsLabel(workBook, excessWorkingHrsRow, columnNumber-1, "Excess Working Hours", false);
        createSummaryDetailsLabel(workBook, utilizationRow, columnNumber-1, "Utilization", true);
        createSummaryDetailsLabel(workBook, availableHrsRow, columnNumber-1, "Total Hours Available", false);
        createSummaryDetailsLabel(workBook, totalHrsRow, columnNumber-1, "Total hours", false);

        for (EmployeeDetails employeeDetails : employees) {
            Cell totalUserProjectHoursCell = getCell(totalUserProjectHoursRow, columnNumber);
            totalUserProjectHoursCell.setCellStyle(xssfCellProjectTotalStyle);

            String cellLetter = CellReference.convertNumToColString(columnNumber);
            String strFormula = "SUM(" + cellLetter + 7 + ":" + cellLetter + rowCount + ")";
            totalUserProjectHoursCell.setCellType(CellType.FORMULA);
            totalUserProjectHoursCell.setCellFormula(strFormula);

            Cell holidayCell = getCell(holidayRow, columnNumber);
            holidayCell.setCellStyle(xssfCellProjectTaskHrsStyle);
            holidayCell.setCellValue(convertToActualTimeString(holidayHours * 60));

            Cell availableHrsCell = getCell(availableHrsRow, columnNumber);
            availableHrsCell.setCellStyle(xssfCellProjectTaskHrsStyle);
            availableHrsCell.setCellValue(convertToActualTimeString(availableWorkingDayHours * 60));

            Cell totalHrsCell = getCell(totalHrsRow, columnNumber);
            totalHrsCell.setCellStyle(xssfCellProjectTaskHrsStyle);
            String totalHrsCalString =
                cellLetter + (availableHrsRow.getRowNum() + 1) + "-(" + cellLetter + (holidayRow.getRowNum() + 1) + "+"
                    + cellLetter + (LeaveHrsRow.getRowNum() + 1) + ")";
            strFormula = "IF(" + totalHrsCalString + "> 0, " + totalHrsCalString + ",0)";
            totalHrsCell.setCellType(CellType.FORMULA);
            totalHrsCell.setCellFormula(strFormula);

            Cell excessWorkingHrsCell = getCell(excessWorkingHrsRow, columnNumber);
            excessWorkingHrsCell.setCellStyle(xssfCellProjectTaskHrsStyle);
            strFormula =
                "IF(" + cellLetter + (totalUserProjectHoursRow.getRowNum() + 1) + "-" + cellLetter
                    + (totalHrsRow.getRowNum() + 1) + ">0," + cellLetter + (totalUserProjectHoursRow.getRowNum() + 1)
                    + "-" + cellLetter + (totalHrsRow.getRowNum() + 1) + ",0)";
            excessWorkingHrsCell.setCellType(CellType.FORMULA);
            excessWorkingHrsCell.setCellFormula(strFormula);

            Cell utilizationCell = getCell(utilizationRow, columnNumber);
            xssfCellUtilizationStyle.setDataFormat(workBook.createDataFormat().getFormat("0.00"));
            utilizationCell.setCellStyle(xssfCellUtilizationStyle);
            strFormula =
                "IFERROR(ROUND(((" + cellLetter + (totalUserProjectHoursRow.getRowNum() + 1) + "*24*60)/(" + cellLetter
                    + (totalHrsRow.getRowNum() + 1) + "*24*60))*100,0), 0)&\" %\"";
            utilizationCell.setCellType(CellType.FORMULA);
            utilizationCell.setCellFormula(strFormula);

            // sheet.autoSizeColumn(columnNumber);
            columnNumber++;
        }

        Cell projectGrandTotalCell = getCell(totalUserProjectHoursRow, columnNumber);
        projectGrandTotalCell.setCellStyle(xssfCellProjectGrandTotalStyle);
        String cellLetter = CellReference.convertNumToColString(columnNumber);
        String strFormula = "SUM(" + cellLetter + 7 + ":" + cellLetter + rowCount + ")";
        projectGrandTotalCell.setCellType(CellType.FORMULA);
        projectGrandTotalCell.setCellFormula(strFormula);
    }

	private void createSummaryDetailsLabel(XSSFWorkbook workbook,
			Row row, int columnNumber, String label, boolean isBold) {
		Cell cell =  getCell(row, columnNumber);
        cell.setCellStyle(getRightAllighStyle(workbook, isBold));
        cell.setCellValue(label);
	}

	private CellStyle getRightAllighStyle(XSSFWorkbook workbook, boolean isBold) {
        XSSFCellStyle cellStyle = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(isBold);
        cellStyle.setFont(font);
        cellStyle.setAlignment(HorizontalAlignment.RIGHT);
        return cellStyle;

	}

	private void createNonBillableDetailsRow(XSSFWorkbook workBook, Sheet sheet, int rowCount, String taskCategory,
        Map<Integer, Integer> nonBillableMap, List<EmployeeDetails> employees, CellStyle xssfCellTextWrapStyle,
        CellStyle xssfCellProjectTotalStyle, CellStyle xssfCellProjectTaskHrsStyle)
    {

        if (nonBillableMap != null) {
            int nonBillableDetailsColumnCount = 15;
            //sheet.shiftRows(rowCount, sheet.getLastRowNum() + 1, 1, true, true);
            Row nonBillableItemRow = getRow(sheet, rowCount++);
            xssfCellTextWrapStyle = null;
            createProjectDetailsColoumn(sheet, 7, "NON BILLABLE", nonBillableItemRow, xssfCellTextWrapStyle);
            createProjectDetailsColoumn(sheet, 8, taskCategory, nonBillableItemRow, xssfCellTextWrapStyle);

            List<Integer> employeeIndexList = getEmployeeIndexList(employees);

            for (Integer userId : nonBillableMap.keySet()) {
                int userCellIndex = employeeIndexList.indexOf(userId);

                if(userCellIndex < 0) {
                	userCellIndex = employeeIndexList.indexOf(0);
                }
                
                createTaskTimeCell(sheet, nonBillableDetailsColumnCount + userCellIndex, nonBillableMap.get(userId), null,
                    nonBillableItemRow, xssfCellProjectTaskHrsStyle);
            }

            createProjectSumFunctionCell(nonBillableItemRow, nonBillableDetailsColumnCount + employees.size(),
                xssfCellProjectTotalStyle);

        }

    }

    public void createProjectDetailsRow(XSSFWorkbook workBook, Sheet sheet, int projectDetailsRowCount,
        ProjectTaskActivityDetails projectDetails, List<EmployeeDetails> employees, CellStyle textWrapStyle,
        CellStyle xssfCellProjectTotalStyle, CellStyle xssfCellProjectTaskHrsStyle, CellStyle hiddenStyle)
    {
        if (projectDetails != null) {
            int projectDetailsUserColumnCount = 15;
            //sheet.shiftRows(projectDetailsRowCount, sheet.getLastRowNum() + 1, 1, true, true);
            Row projectDetailsRow = getRow(sheet, projectDetailsRowCount);
            // Map<Integer, Map<Integer, Integer>> taskUserActualTimeMap = getProjectTaskDuration(projectDetails);

            // logDebug("Task User Actual time map " + taskUserActualTimeMap);
            textWrapStyle = null;
            createProjectDetailsColoumn(sheet, 0,
                DateUtils.format(projectDetails.getTaskActivityStartTime(), DateUtils.DATE_FORMAT), projectDetailsRow,
                textWrapStyle);
            createProjectDetailsColoumn(sheet, 1, projectDetails.getProjectCode(), projectDetailsRow, textWrapStyle);

            createProjectDetailsColoumn(sheet, 2, "" + (projectDetails.getProjectId() == null ? "" : projectDetails.getProjectId()), projectDetailsRow, textWrapStyle);

            createProjectDetailsColoumn(sheet, 3, projectDetails.getProjectNumber(), projectDetailsRow, textWrapStyle);
            createProjectDetailsColoumn(sheet, 4, projectDetails.getSowLineItem(), projectDetailsRow, textWrapStyle);
            
            createProjectDetailsColoumn(sheet, 5, projectDetails.getProjectName(), projectDetailsRow, textWrapStyle);
            createProjectDetailsColoumn(sheet, 6, projectDetails.getProjectStatus().toString(), projectDetailsRow,
                textWrapStyle);
            createProjectDetailsColoumn(sheet, 7, projectDetails.getProjectType().toString(), projectDetailsRow,
                textWrapStyle);
            createProjectDetailsColoumn(sheet, 8, projectDetails.getProjectCategory(), projectDetailsRow, textWrapStyle);
            createProjectDetailsColoumn(sheet, 9, projectDetails.getNewProjectCategory(), projectDetailsRow, textWrapStyle);
            
            createProjectDetailsColoumn(sheet, 10, projectDetails.getAccountName(), projectDetailsRow, textWrapStyle);
            createProjectDetailsColoumn(sheet, 11, projectDetails.getTeamName(), projectDetailsRow, textWrapStyle);
            createProjectDetailsColoumn(sheet, 12, projectDetails.getSubTeamName(), projectDetailsRow, textWrapStyle);
            createProjectDetailsColoumn(sheet, 13, projectDetails.getClientName(), projectDetailsRow, textWrapStyle);
            createProjectDetailsColoumn(sheet, 14, projectDetails.getTaskName(), projectDetailsRow, textWrapStyle);

            createProjectDetailsColoumn(sheet, 15 + employees.size() + 1, "" + projectDetails.getAllTaskActivityIds(), projectDetailsRow, hiddenStyle);

            
            List<Integer> employeeIndexList = getEmployeeIndexList(employees);
            int userCellIndex = employeeIndexList.indexOf(projectDetails.getUserId());
            if(userCellIndex < 0) {
            	userCellIndex = employeeIndexList.indexOf(0);
            }
            
            createTaskTimeCell(sheet, projectDetailsUserColumnCount + userCellIndex,
                projectDetails.getTaskActivityDuration(), projectDetails.getTaskActivityTimeXls(), projectDetailsRow, xssfCellProjectTaskHrsStyle);
            
           /* 
            if (projectDetails.getSubItems().size() > 0) {
            	for (ProjectTaskActivityDetails subActivityDetails : projectDetails.getSubItems()) {
            		 userCellIndex = employeeIndexList.indexOf(subActivityDetails.getUserId());
                    if(userCellIndex < 0) {
                    	userCellIndex = employeeIndexList.indexOf(0);
                    }
                    createTaskTimeCell(sheet, projectDetailsUserColumnCount + userCellIndex,
                    		subActivityDetails.getTaskActivityDuration(), subActivityDetails.getTaskActivityTimeXls(), projectDetailsRow, xssfCellProjectTaskHrsStyle);
            	}
            }*/
            
            createProjectSumFunctionCell(projectDetailsRow, projectDetailsUserColumnCount + employees.size(),
                xssfCellProjectTotalStyle);

            /*
             * createTaskTimeCell(sheet, projectDetailsUserColumnCount + employees.size(),
             * projectDetails.getActualDuration(), projectDetailsRow, xssfCellProjectTotalStyle);
             */
        }
    }

    private List<Integer> getEmployeeIndexList(List<EmployeeDetails> employees)
    {
        List<Integer> employeeIndexList = new ArrayList<>();

        for (EmployeeDetails employeeDetails : employees) {
            employeeIndexList.add(employeeDetails.getId());
        }
        return employeeIndexList;
    }
    
    private List<EmployeeDetails> getDeletedEmployees(List<EmployeeDetails> employees, List<ProjectTaskActivityDetails> exportDataList){
    	List<Integer> availableIdList = getEmployeeIndexList(employees);
    	List<EmployeeDetails> deletedEmployeeList = new ArrayList<>();
    	Map<Integer, EmployeeDetails> allEmployeesMap = getFamstackApplicationConfiguration().getAllUsersMap();
    	boolean otherAdded = false;
    	if (exportDataList != null) {
    		for (ProjectTaskActivityDetails projectTaskActivityDetail : exportDataList) {
    			if (!availableIdList.contains(projectTaskActivityDetail.getUserId())){
    				EmployeeDetails employeeDetails = allEmployeesMap.get(projectTaskActivityDetail.getUserId());
    				if (employeeDetails != null) {
    						if (!deletedEmployeeList.contains(employeeDetails)) {
    							deletedEmployeeList.add(employeeDetails);	
    						}
    				} else if (!otherAdded) {
    					EmployeeDetails otherEmp = new EmployeeDetails();
				        otherEmp.setId(0);
				        otherEmp.setFirstName("Unknown User");
				        deletedEmployeeList.add(otherEmp);
				        otherAdded = true;
    				}
    			}
    		}
    	}
		return deletedEmployeeList;
    }

    private void createProjectSumFunctionCell(Row projectDetailsRow, int columnIndex,
        CellStyle xssfCellProjectTotalStyle)
    {
        Cell cell = projectDetailsRow.getCell(columnIndex);
        if (cell == null) {
            cell = projectDetailsRow.createCell(columnIndex);
        }
        cell.setCellStyle(xssfCellProjectTotalStyle);
        String cellLetter = CellReference.convertNumToColString(columnIndex - 1);
        int rowNumber = projectDetailsRow.getRowNum() + 1;
        String strFormula = "SUM(M" + rowNumber + ":" + cellLetter + rowNumber + ")";
        cell.setCellType(CellType.FORMULA);
        cell.setCellFormula(strFormula);
    }

    private Row getRow(Sheet sheet, int rowIndex)
    {
        Row projectDetailsRow = sheet.getRow(rowIndex);
        if (projectDetailsRow == null) {
            projectDetailsRow = sheet.createRow(rowIndex);
        }
        return projectDetailsRow;
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

    private void createProjectDetailsColoumn(Sheet sheet, int projectDetailsColumnCount, String value,
        Row projectDetailsRow, CellStyle cellStyle)
    {
        Cell userCell = projectDetailsRow.getCell(projectDetailsColumnCount);
        if (userCell == null) {
            userCell = projectDetailsRow.createCell(projectDetailsColumnCount);
        }
        if (cellStyle != null) {
            userCell.setCellStyle(cellStyle);
        }
        // sheet.autoSizeColumn(projectDetailsColumnCount);
        userCell.setCellValue(value);
    }

    private void createTaskTimeCell(Sheet sheet, int columnIndex, Integer userTaskTimeMins,Double userTaskTimeXls, Row projectDetailsRow,
        CellStyle cellStyle)
    {

        if (userTaskTimeMins == null) {
            userTaskTimeMins = 0;
        }
        logDebug("User task time cell index " + columnIndex);
        Cell userCell = projectDetailsRow.getCell(columnIndex);
        if (userCell == null) {
            userCell = projectDetailsRow.createCell(columnIndex);
        }
        if (cellStyle != null) {
            userCell.setCellStyle(cellStyle);
        }
        // sheet.autoSizeColumn(columnIndex);
        double time = userTaskTimeXls == null? convertToActualTimeString(userTaskTimeMins) : userTaskTimeXls;
        if (time > 0) {
            userCell.setCellValue(time);
        }
    }

    private void createHeader(XSSFWorkbook workBook, Sheet sheet, String dateString, String teamName,
        CellStyle xssfCellUserHeaderStyle, CellStyle xssfCellProjectTotalStyle, List<EmployeeDetails> employees, CellStyle hiddenStyle)
    {
        Row teamNameRow = sheet.getRow(0);
        Row monthRow = sheet.getRow(1);
        Row userRow = sheet.getRow(2);
        Row userIdHeaderRow = sheet.getRow(4);
        Row userDetailsHeaderRow = sheet.getRow(5);
        teamNameRow.getCell(0).setCellValue(teamName);
        monthRow.getCell(1).setCellValue(dateString);

        int userDetailsColumnCount = 1;
        if (employees != null) {
            userRow.getCell(1).setCellValue(employees.size());
            for (EmployeeDetails employeeDetails : employees) {
                Cell userHeaderCell = getCell(userDetailsHeaderRow, userDetailsColumnCount + 14);
                userHeaderCell.setCellValue(employeeDetails.getFirstName());
                userHeaderCell.setCellStyle(xssfCellUserHeaderStyle);
                
                Cell userIdHeaderCell = getCell(userIdHeaderRow, userDetailsColumnCount + 14);
                userIdHeaderCell.setCellValue(employeeDetails.getId());
                userIdHeaderCell.setCellStyle(hiddenStyle);
                
                sheet.autoSizeColumn(userDetailsColumnCount + 14);
                userDetailsColumnCount++;
            }

            /*
             * Cell projectTotalAdjustmentCell = getCell(userDetailsHeaderRow, userDetailsRowCount + 9);
             * sheet.autoSizeColumn(userDetailsRowCount + 9);
             * projectTotalAdjustmentCell.setCellStyle(xssfCellUserHeaderStyle);
             * projectTotalAdjustmentCell.setCellValue("Adjustment");
             */
            Cell projectTotalCell = getCell(userDetailsHeaderRow, userDetailsColumnCount + 14);
            sheet.autoSizeColumn(userDetailsColumnCount + 14);
            projectTotalCell.setCellStyle(xssfCellUserHeaderStyle);
            projectTotalCell.setCellValue("Project Total Hrs");
        }
    }

    private Cell getCell(Row row, int cellNumber)
    {
        Cell cell = row.getCell(cellNumber);
        if (cell == null) {
            cell = row.createCell(cellNumber);
        }
        return cell;
    }

    public Map<Integer, Map<Integer, Integer>> getProjectTaskDuration(ProjectDetails projectDetails)
    {
        Map<Integer, Map<Integer, Integer>> taskUserActualTimeMap = new HashMap<>();
        if (projectDetails.getProjectTaskDeatils() != null) {
            for (TaskDetails taskDetails : projectDetails.getProjectTaskDeatils()) {

                Map<Integer, Integer> userTaskActualTimeMap = taskUserActualTimeMap.get(taskDetails.getTaskId());

                if (userTaskActualTimeMap == null) {
                    userTaskActualTimeMap = new HashMap<>();
                    taskUserActualTimeMap.put(taskDetails.getTaskId(), userTaskActualTimeMap);

                    Set<Integer> contributers = new HashSet<>(Arrays.asList(taskDetails.getContributers()));
                    logDebug("Task id :" + taskDetails.getTaskId() + " Contributers :" + contributers);
                    if (contributers.size() > 1) {
                        List<UserTaskActivityItem> userTaskActivityItems =
                            (List<UserTaskActivityItem>) famstackUserActivityManager
                                .getUserTaskActivityItemByTaskId(taskDetails.getTaskId());
                        for (Integer contributer : contributers) {
                            Integer currentTime = 0;
                            for (UserTaskActivityItem userTaskActivityItem : userTaskActivityItems) {
                                if (userTaskActivityItem.getUserActivityItem().getUserItem().getId() == contributer) {
                                    currentTime = userTaskActualTimeMap.get(contributer);
                                    if (currentTime != null) {
                                        currentTime += userTaskActivityItem.getDurationInMinutes();
                                    } else {
                                        currentTime = userTaskActivityItem.getDurationInMinutes();
                                    }
                                    userTaskActualTimeMap.put(contributer, currentTime);
                                }
                            }
                        }

                    } else if (taskDetails.getContributers()[0] != 0) {
                        userTaskActualTimeMap.put(taskDetails.getContributers()[0], taskDetails.getActualTimeTaken());
                    }
                }
            }
        }
        return taskUserActualTimeMap;
    }

    private CellStyle getUserHeaderStyle(XSSFWorkbook workbook, XSSFColor myColorIndexGray)
    {
        XSSFCellStyle xssfCellUserHeaderStyle = workbook.createCellStyle();
        xssfCellUserHeaderStyle.setFillForegroundColor(myColorIndexGray);
        xssfCellUserHeaderStyle.setFillBackgroundColor(myColorIndexGray);
        xssfCellUserHeaderStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        Font font = workbook.createFont();
        font.setBold(true);
        xssfCellUserHeaderStyle.setFont(font);

        return xssfCellUserHeaderStyle;
    }

    private CellStyle getCellStyle(XSSFWorkbook workbook, XSSFColor myColorIndexGray)
    {
        XSSFCellStyle xssfCellUtilizationStyle = workbook.createCellStyle();
        xssfCellUtilizationStyle.setFillForegroundColor(myColorIndexGray);
        xssfCellUtilizationStyle.setFillBackgroundColor(myColorIndexGray);
        xssfCellUtilizationStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        xssfCellUtilizationStyle.setAlignment(HorizontalAlignment.CENTER);
        Font font = workbook.createFont();
        font.setBold(true);
        xssfCellUtilizationStyle.setFont(font);

        return xssfCellUtilizationStyle;
    }

    private CellStyle getTextWrapStyle(Workbook workbook)
    {
        CellStyle xssfCellTextWrapStyle = workbook.createCellStyle();
        xssfCellTextWrapStyle.setWrapText(true);

        return xssfCellTextWrapStyle;
    }

    private CellStyle getProjectTotalStyle(XSSFWorkbook workbook, XSSFColor myColorIndexLightGray)
    {
        XSSFCellStyle xssfCellProjectTotalStyle = workbook.createCellStyle();
        if (myColorIndexLightGray != null) {
            xssfCellProjectTotalStyle.setFillForegroundColor(myColorIndexLightGray);
            xssfCellProjectTotalStyle.setFillBackgroundColor(myColorIndexLightGray);
            xssfCellProjectTotalStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        }
        xssfCellProjectTotalStyle.setAlignment(HorizontalAlignment.CENTER);
        xssfCellProjectTotalStyle.setWrapText(true);
        Font font = workbook.createFont();
        font.setBold(true);
        xssfCellProjectTotalStyle.setFont(font);

        CreationHelper createHelper = workbook.getCreationHelper();

        xssfCellProjectTotalStyle.setDataFormat(createHelper.createDataFormat().getFormat("[h]:mm"));
        return xssfCellProjectTotalStyle;
    }
    
    private CellStyle getHiddenContentStyle(XSSFWorkbook workbook) {
    	CellStyle hiddenStyle = workbook.createCellStyle(); 
    	hiddenStyle.setDataFormat(workbook.createDataFormat().getFormat(";;;"));
    	return hiddenStyle;
    }

    private XSSFColor getColor(int R, int G, int B, Workbook workbook)
    {

        return new XSSFColor(new java.awt.Color(R, G, B));
    }

    public int getMaxThread()
    {
        return maxThread;
    }

    public void setMaxThread(int maxThread)
    {
        this.maxThread = maxThread;
    }
}
