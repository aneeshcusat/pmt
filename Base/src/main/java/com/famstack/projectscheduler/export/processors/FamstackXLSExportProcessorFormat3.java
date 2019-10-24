package com.famstack.projectscheduler.export.processors;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.famstack.projectscheduler.BaseFamstackService;
import com.famstack.projectscheduler.contants.ProjectType;
import com.famstack.projectscheduler.dashboard.bean.ProjectTaskActivityDetails;
import com.famstack.projectscheduler.employees.bean.EmployeeDetails;
import com.famstack.projectscheduler.manager.FamstackUserActivityManager;
import com.famstack.projectscheduler.util.DateTimePeriod;
import com.famstack.projectscheduler.util.DateUtils;
import com.famstack.projectscheduler.util.StringUtils;

/**
 * visual service
 * 
 * @author Aneeshkumar
 */
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class FamstackXLSExportProcessorFormat3 extends BaseFamstackService implements FamstackBaseXLSExportProcessor
{

    @Resource
    FamstackUserActivityManager famstackUserActivityManager;

    private int maxThread;

    @Override
    public void renderReport(Map<String, Object> dataMap)

    {

        XSSFWorkbook workBook = (XSSFWorkbook) dataMap.get("workBook");
        Sheet sheet = (Sheet) dataMap.get("sheet");
        List<ProjectTaskActivityDetails> exportDataList =
            (List<ProjectTaskActivityDetails>) dataMap.get("exportDataList");
        String dateString = (String) dataMap.get("dateString");
        Map<String, Map<Integer, Integer>> nonBillableTaskActivities =
            (Map<String, Map<Integer, Integer>>) dataMap.get("nonBillableTaskActivities");
        logDebug("Rendering reoprt for  FamstackXLSExportProcessorFormat3");
        String[] dateRanges;
        CellStyle xssfCellProjectTaskHrsStyle = getProjectTotalStyle(workBook, null);
        if (StringUtils.isNotBlank(dateString) && exportDataList != null && !exportDataList.isEmpty()) {
            dateRanges = dateString.split("-");
            if (dateRanges != null && dateRanges.length > 1) {
                Date startDate = DateUtils.tryParse(dateRanges[0].trim(), DateUtils.DATE_FORMAT_DP);
                Date endDate = DateUtils.tryParse(dateRanges[1].trim(), DateUtils.DATE_FORMAT_DP);
                endDate = DateUtils.getNextPreviousDate(DateTimePeriod.DAY, endDate, 1);
                List<String> dateList = new ArrayList<>();
                List<String> dayList = new ArrayList<>();
                boolean isLastWeekNumber = false;
                while (startDate.before(endDate)) {
                	String dayString = DateUtils.getDayString(startDate);
                	dayList.add(dayString);
                    dateList.add(DateUtils.format(startDate, DateUtils.DATE_MONTH_YEAR));
                   
                    if ("Sunday".equalsIgnoreCase(dayString)) {
                    	dayList.add("Week " + DateUtils.getWeekNumber(DateUtils.getNextPreviousDate(DateTimePeriod.DAY, startDate, -1)));
                    	dateList.add("SKIP");
                    	isLastWeekNumber= true;
                    } else {
                    	isLastWeekNumber = false;
                    }
                    startDate = DateUtils.getNextPreviousDate(DateTimePeriod.DAY, startDate, 1);
                }
                
                if (!isLastWeekNumber) {
                	dayList.add("Week " + (DateUtils.getWeekNumber(startDate) + 1));
                	dateList.add("SKIP");
                }
                
                createHeader(sheet, dateList, dayList);
                createBody(workBook, sheet, exportDataList, nonBillableTaskActivities, dateList, xssfCellProjectTaskHrsStyle);
            }
        }
       
    }

    private void createHeader(Sheet sheet, List<String> dateList, List<String> dayList)
    {
        int colIndex = 9;
        Row rowDate = getRow(sheet, 1);
        Row rowDay = getRow(sheet, 2);
        int dayIndexCount = 0;
        for (String headerDate : dateList) {
        	Cell cell = getCell(sheet, rowDate, colIndex);
        	if (!"SKIP".equalsIgnoreCase(headerDate)) {
        		cell.setCellValue(headerDate);
        		sheet.autoSizeColumn(colIndex);
        	}
            
            cell = getCell(sheet, rowDay, colIndex++);
            cell.setCellValue(dayList.get(dayIndexCount++));
        }
    }
    

    private Cell getCell(Sheet sheet, Row row, int cellNumber)
    {
        Cell cell = row.getCell(cellNumber);
        if (cell == null) {
            cell = row.createCell(cellNumber);
        }
        if (cellNumber == 0 || cellNumber == 1 || row.getRowNum() == 0) {
            sheet.autoSizeColumn(cellNumber);
        }
        return cell;
    }

    private void createBody(final XSSFWorkbook workBook, final Sheet sheet,
        final List<ProjectTaskActivityDetails> exportDataList,
        Map<String, Map<Integer, Integer>> nonBillableTaskActivities, List<String> dateList, CellStyle xssfCellProjectTaskHrsStyle)
    {
    	Map<Integer, EmployeeDetails> allEmployeesMap = getFamstackApplicationConfiguration().getAllUsersMap();
        int projectDetailsRowCount = 3;
        if (exportDataList != null) {
            if (exportDataList.size() > 0) {
                for (ProjectTaskActivityDetails projectDetails : exportDataList) {
                    createProjectDetailsRow(workBook, sheet, projectDetailsRowCount, projectDetails, allEmployeesMap, dateList, xssfCellProjectTaskHrsStyle);
                    createWeekSumCells(sheet,dateList.indexOf("SKIP"),dateList.lastIndexOf("SKIP"), 9, projectDetailsRowCount, xssfCellProjectTaskHrsStyle);
                    projectDetailsRowCount++;
                }
            }
        }

        if (nonBillableTaskActivities != null) {
        }

    }

    private void createWeekSumCells(Sheet sheet, int startWeekIndex, int endWeekIndex, int colStartIndex,
			int rowIndex, CellStyle cellStyle) {
    	int weekIndex = startWeekIndex + colStartIndex;
    	int columnStartIndex = colStartIndex;
    	int columnEndIndex= columnStartIndex + startWeekIndex - 1;
    	do {
    		createWeekSumCell(columnStartIndex,columnEndIndex, rowIndex,weekIndex ,sheet, cellStyle);
    		columnStartIndex= weekIndex + 1;
        	weekIndex+=8;
        	columnEndIndex= columnStartIndex + 6;
    	} while(weekIndex <= (endWeekIndex + colStartIndex));
    	
    	if (weekIndex > (endWeekIndex + colStartIndex)) {
    		createWeekSumCell(columnStartIndex, endWeekIndex + colStartIndex - 1, rowIndex, endWeekIndex + colStartIndex ,sheet, cellStyle);
    	}
    	
        	
    }
    
    private void createWeekSumCell(int startCellIndex, int endCellIndex, int rowIndex,int weekCellIndex, Sheet sheet, CellStyle cellStyle){
    	String startCellLetter = CellReference.convertNumToColString(startCellIndex);
    	String endCellLetter = CellReference.convertNumToColString(endCellIndex);
    	String strFormula = "SUM(" + startCellLetter + (rowIndex+1) + ":" + endCellLetter + (rowIndex+1) + ")";
    	Cell weekSumCell = getCell(sheet, getRow(sheet, rowIndex), weekCellIndex);
    	weekSumCell.setCellType(CellType.FORMULA);
    	weekSumCell.setCellFormula(strFormula);
    	
    	if(cellStyle != null) {
    		weekSumCell.setCellStyle(cellStyle);
    	}
    }

	public void createProjectDetailsRow(XSSFWorkbook workBook, Sheet sheet, int projectDetailsRowCount,
        ProjectTaskActivityDetails projectDetails, Map<Integer, EmployeeDetails> allEmployeesMap, List<String> dateList, CellStyle xssfCellProjectTaskHrsStyle)
    {
        if (projectDetails != null) {
            Row projectDetailsRow = getRow(sheet, projectDetailsRowCount);

            String projectDateString = DateUtils.format(projectDetails.getTaskActivityStartTime(), DateUtils.DATE_MONTH_YEAR);
            createProjectDetailsColoumn(sheet, 0, "" + (projectDetailsRowCount - 2) , projectDetailsRow);
            createProjectDetailsColoumn(sheet, 1, allEmployeesMap.get(projectDetails.getUserId()).getFirstName(), projectDetailsRow);
            createProjectDetailsColoumn(sheet, 2, allEmployeesMap.get(projectDetails.getUserId()).getDesignation(), projectDetailsRow);
            createProjectDetailsColoumn(sheet, 3, projectDetails.getAccountName(), projectDetailsRow);
            createProjectDetailsColoumn(sheet, 4, projectDetails.getProjectName(), projectDetailsRow);
            createProjectDetailsColoumn(sheet, 5, projectDetails.getClientName(), projectDetailsRow);
            createProjectDetailsColoumn(sheet, 6, getProjectType(projectDetails.getProjectType()), projectDetailsRow);
            createProjectDetailsColoumn(sheet, 7, projectDetails.getTaskName(), projectDetailsRow);
            createProjectDetailsColoumn(sheet, 8, projectDetails.getTaskCompletionComments(), projectDetailsRow);

            int dateCellIndex = dateList.indexOf(projectDateString);
            
            createTaskTimeCell(sheet, 9 + dateCellIndex,
                projectDetails.getTaskActivityDuration(), projectDetailsRow, xssfCellProjectTaskHrsStyle);
            
            if (projectDetails.getSubItems().size() > 0) {
            	for (ProjectTaskActivityDetails subActivityDetails : projectDetails.getSubItems()) {
            		 projectDateString = DateUtils.format(subActivityDetails.getTaskActivityStartTime(), DateUtils.DATE_MONTH_YEAR);
            		 dateCellIndex = dateList.indexOf(projectDateString);
                     createTaskTimeCell(sheet, 9 + dateCellIndex,
                    		 subActivityDetails.getTaskActivityDuration(), projectDetailsRow, xssfCellProjectTaskHrsStyle);
            	}
            }
        }
    }

    private String getProjectType(ProjectType projectType) {
		if (projectType != null) {
			if (projectType == ProjectType.BILLABLE){
				return "Billable";
			} else {
				return "Non Billable";
			}
		}
		return "";
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
        Row projectDetailsRow)
    {
        Cell userCell = projectDetailsRow.getCell(projectDetailsColumnCount);
        if (userCell == null) {
            userCell = projectDetailsRow.createCell(projectDetailsColumnCount);
        }
        userCell.setCellValue(value);
    }

    private void createTaskTimeCell(Sheet sheet, int columnIndex, Integer userTaskTime, Row projectDetailsRow, CellStyle cellStyle)
    {

        if (userTaskTime == null) {
            userTaskTime = 0;
        }
        logDebug("User task time cell index " + columnIndex);
        Cell userCell = projectDetailsRow.getCell(columnIndex);
        if (userCell == null) {
            userCell = projectDetailsRow.createCell(columnIndex);
        }
        if (cellStyle != null) {
            userCell.setCellStyle(cellStyle);
        }
        double time = convertToActualTimeString(userTaskTime);
        if (time > 0) {
            userCell.setCellValue(time);
        }
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

    public int getMaxThread()
    {
        return maxThread;
    }

    public void setMaxThread(int maxThread)
    {
        this.maxThread = maxThread;
    }
}
