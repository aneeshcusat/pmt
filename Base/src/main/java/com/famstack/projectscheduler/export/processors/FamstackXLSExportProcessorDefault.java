package com.famstack.projectscheduler.export.processors;

import java.util.ArrayList;
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

import scala.actors.threadpool.Arrays;

import com.famstack.projectscheduler.BaseFamstackService;
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
public class FamstackXLSExportProcessorDefault extends BaseFamstackService implements FamstackBaseXLSExportProcessor
{

    @Resource
    FamstackUserActivityManager famstackUserActivityManager;

    private int maxThread;

    @Override
    public void renderReport(XSSFWorkbook workBook, Sheet sheet, String teamName, List<ProjectDetails> exportDataList,
        String dateString, List<EmployeeDetails> employees)

    {
        logDebug("Rendering reoprt for  FamstackXLSExportProcessor11");

        int holidayHours = 8;

        XSSFColor myColorIndexYellow = getColor(255, 192, 0, workBook);
        XSSFColor myColorIndexGray = getColor(217, 217, 217, workBook);
        XSSFColor myColorIndexLightGray = getColor(242, 242, 242, workBook);
        XSSFColor myColorIndexLightGreen = getColor(198, 224, 180, workBook);

        CellStyle xssfCellProjectTaskHrsStyle = getProjectTotalStyle(workBook, null);
        CellStyle xssfCellUserHeaderStyle = getUserHeaderStyle(workBook, myColorIndexGray);
        CellStyle xssfCellUtilizationStyle = getCellStyle(workBook, myColorIndexLightGreen);
        CellStyle xssfCellProjectTotalStyle = getProjectTotalStyle(workBook, myColorIndexLightGray);
        CellStyle xssfCellProjectGrandTotalStyle = getProjectTotalStyle(workBook, myColorIndexYellow);

        int availableWorkingDayHours = getWorkingDaysHours(dateString);
        String sheetName = getSheetNameFromDate(dateString);
        workBook.setSheetName(0, sheetName);

        CellStyle xssfCellTextWrapStyle = getTextWrapStyle(workBook);
        createHeader(workBook, sheet, dateString, teamName, xssfCellUserHeaderStyle, xssfCellProjectTotalStyle,
            employees);
        createBody(workBook, sheet, exportDataList, employees, xssfCellProjectTotalStyle, xssfCellTextWrapStyle,
            xssfCellProjectTaskHrsStyle, xssfCellProjectGrandTotalStyle, xssfCellUtilizationStyle,
            availableWorkingDayHours, holidayHours);

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

    private void createBody(final XSSFWorkbook workBook, final Sheet sheet, final List<ProjectDetails> exportDataList,
        final List<EmployeeDetails> employees, final CellStyle xssfCellProjectTotalStyle,
        final CellStyle xssfCellTextWrapStyle, CellStyle xssfCellProjectTaskHrsStyle,
        CellStyle xssfCellProjectGrandTotalStyle, CellStyle xssfCellUtilizationStyle, int availableWorkingDayHours,
        int holidayHours)
    {
        int projectDetailsRowCount = 6;
        if (exportDataList != null) {
            if (exportDataList.size() > 0) {
                for (ProjectDetails projectDetails : exportDataList) {
                    createProjectDetailsRow(workBook, sheet, projectDetailsRowCount, projectDetails, employees,
                        xssfCellTextWrapStyle, xssfCellProjectTotalStyle, xssfCellProjectTaskHrsStyle);
                    projectDetailsRowCount++;
                }
            }
        }

        Map nonBillableListMap = null;

        Map nonBillableMap = null;
        // foreach
        createNonBillableDetailsRow(workBook, sheet, ++projectDetailsRowCount, nonBillableMap, employees,
            xssfCellTextWrapStyle, xssfCellProjectTotalStyle, xssfCellProjectTaskHrsStyle);
        // foreachend

        createTotalSummaryDetailsRows(workBook, sheet, projectDetailsRowCount, employees, xssfCellTextWrapStyle,
            xssfCellProjectTotalStyle, xssfCellProjectTaskHrsStyle, xssfCellProjectGrandTotalStyle,
            xssfCellUtilizationStyle, availableWorkingDayHours, holidayHours);

    }

    private void createTotalSummaryDetailsRows(XSSFWorkbook workBook, Sheet sheet, int rowCount,
        List<EmployeeDetails> employees, CellStyle xssfCellTextWrapStyle, CellStyle xssfCellProjectTotalStyle,
        CellStyle xssfCellProjectTaskHrsStyle, CellStyle xssfCellProjectGrandTotalStyle,
        CellStyle xssfCellUtilizationStyle, int availableWorkingDayHours, int holidayHours)
    {

        Row totalUserProjectHoursRow = sheet.getRow(rowCount);
        Row holidayRow = sheet.getRow(rowCount + 1);
        Row leaveRow = sheet.getRow(rowCount + 2);
        Row excessWorkingHrsRow = sheet.getRow(rowCount + 3);
        Row utilizationRow = sheet.getRow(rowCount + 4);
        Row availableHrsRow = sheet.getRow(rowCount + 5);
        Row totalHrsRow = sheet.getRow(rowCount + 6);
        int columnNumber = 1;

        for (EmployeeDetails employeeDetails : employees) {
            Cell totalUserProjectHoursCell = getCell(totalUserProjectHoursRow, columnNumber + 9);
            totalUserProjectHoursCell.setCellStyle(xssfCellProjectTotalStyle);

            String cellLetter = CellReference.convertNumToColString(columnNumber + 9);
            String strFormula = "SUM(" + cellLetter + 7 + ":" + cellLetter + rowCount + ")";
            totalUserProjectHoursCell.setCellType(CellType.FORMULA);
            totalUserProjectHoursCell.setCellFormula(strFormula);

            Cell holidayCell = getCell(holidayRow, columnNumber + 9);
            holidayCell.setCellStyle(xssfCellProjectTaskHrsStyle);
            holidayCell.setCellValue(convertToActualTimeString(holidayHours * 60));

            Cell leaveCell = getCell(leaveRow, columnNumber + 9);
            leaveCell.setCellStyle(xssfCellProjectTaskHrsStyle);

            Cell availableHrsCell = getCell(availableHrsRow, columnNumber + 9);
            availableHrsCell.setCellStyle(xssfCellProjectTaskHrsStyle);
            availableHrsCell.setCellValue(convertToActualTimeString(availableWorkingDayHours * 60));

            Cell totalHrsCell = getCell(totalHrsRow, columnNumber + 9);
            totalHrsCell.setCellStyle(xssfCellProjectTaskHrsStyle);
            totalHrsCell.setCellValue(convertToActualTimeString(availableWorkingDayHours * 60));
            strFormula =
                "" + cellLetter + (availableHrsRow.getRowNum() + 1) + "-" + cellLetter + (holidayRow.getRowNum() + 1);
            totalHrsCell.setCellType(CellType.FORMULA);
            totalHrsCell.setCellFormula(strFormula);

            Cell excessWorkingHrsCell = getCell(excessWorkingHrsRow, columnNumber + 9);
            excessWorkingHrsCell.setCellStyle(xssfCellProjectTaskHrsStyle);
            strFormula =
                "IF(" + cellLetter + (totalUserProjectHoursRow.getRowNum() + 1) + "-" + cellLetter
                    + (totalHrsRow.getRowNum() + 1) + ">0," + cellLetter + (totalUserProjectHoursRow.getRowNum() + 1)
                    + "-" + cellLetter + (totalHrsRow.getRowNum() + 1) + ",0)";
            excessWorkingHrsCell.setCellType(CellType.FORMULA);
            excessWorkingHrsCell.setCellFormula(strFormula);

            Cell utilizationCell = getCell(utilizationRow, columnNumber + 9);
            xssfCellUtilizationStyle.setDataFormat(workBook.createDataFormat().getFormat("0.00"));
            utilizationCell.setCellStyle(xssfCellUtilizationStyle);
            strFormula =
                "IFERROR(ROUND(((" + cellLetter + (totalUserProjectHoursRow.getRowNum() + 1) + "*24*60)/(" + cellLetter
                    + (totalHrsRow.getRowNum() + 1) + "*24*60))*100,0), 0)&\" %\"";
            utilizationCell.setCellType(CellType.FORMULA);
            utilizationCell.setCellFormula(strFormula);

            sheet.autoSizeColumn(columnNumber + 9);
            columnNumber++;
        }

        Cell projectGrandTotalCell = getCell(totalUserProjectHoursRow, columnNumber + 10);
        projectGrandTotalCell.setCellStyle(xssfCellProjectGrandTotalStyle);
        String cellLetter = CellReference.convertNumToColString(columnNumber + 10);
        String strFormula = "SUM(" + cellLetter + 7 + ":" + cellLetter + rowCount + ")";
        projectGrandTotalCell.setCellType(CellType.FORMULA);
        projectGrandTotalCell.setCellFormula(strFormula);
    }

    private void createNonBillableDetailsRow(XSSFWorkbook workBook, Sheet sheet, int rowCount, Map nonBillableMap,
        List<EmployeeDetails> employees, CellStyle xssfCellTextWrapStyle, CellStyle xssfCellProjectTotalStyle,
        CellStyle xssfCellProjectTaskHrsStyle)
    {

        if (nonBillableMap != null) {
            int nonBillableDetailsColumnCount = 10;
            sheet.shiftRows(rowCount, sheet.getLastRowNum() + 1, 1, true, true);
            Row nonBillableItemRow = getRow(sheet, rowCount++);

            createProjectDetailsColoumn(sheet, 5, "NON_BILLABLE", nonBillableItemRow, xssfCellTextWrapStyle);
            createProjectDetailsColoumn(sheet, 6, "Category", nonBillableItemRow, xssfCellTextWrapStyle);

            List<Integer> employeeIndexList = getEmployeeIndexList(employees);

            int userCellIndex = employeeIndexList.indexOf(1);

            createTaskTimeCell(sheet, nonBillableDetailsColumnCount + userCellIndex, 1, nonBillableItemRow,
                xssfCellProjectTaskHrsStyle);
        }

    }

    public void createProjectDetailsRow(XSSFWorkbook workBook, Sheet sheet, int projectDetailsRowCount,
        ProjectDetails projectDetails, List<EmployeeDetails> employees, CellStyle textWrapStyle,
        CellStyle xssfCellProjectTotalStyle, CellStyle xssfCellProjectTaskHrsStyle)
    {
        if (projectDetails != null) {
            int projectDetailsUserColumnCount = 10;
            sheet.shiftRows(projectDetailsRowCount, sheet.getLastRowNum() + 1, 1, true, true);
            Row projectDetailsRow = getRow(sheet, projectDetailsRowCount);
            Map<Integer, Map<Integer, Integer>> taskUserActualTimeMap = getProjectTaskDuration(projectDetails);

            logDebug("Task User Actual time map " + taskUserActualTimeMap);

            createProjectDetailsColoumn(sheet, 0, projectDetails.getStartTime(), projectDetailsRow, textWrapStyle);
            createProjectDetailsColoumn(sheet, 1, projectDetails.getCode(), projectDetailsRow, textWrapStyle);

            createProjectDetailsColoumn(sheet, 2, "" + projectDetails.getId(), projectDetailsRow, textWrapStyle);

            createProjectDetailsColoumn(sheet, 3, projectDetails.getPONumber(), projectDetailsRow, textWrapStyle);
            createProjectDetailsColoumn(sheet, 4, projectDetails.getName(), projectDetailsRow, textWrapStyle);
            createProjectDetailsColoumn(sheet, 5, projectDetails.getType().toString(), projectDetailsRow, textWrapStyle);
            createProjectDetailsColoumn(sheet, 6, projectDetails.getCategory(), projectDetailsRow, textWrapStyle);
            createProjectDetailsColoumn(sheet, 7, projectDetails.getTeamName(), projectDetailsRow, textWrapStyle);
            createProjectDetailsColoumn(sheet, 8, projectDetails.getSubTeamName(), projectDetailsRow, textWrapStyle);
            createProjectDetailsColoumn(sheet, 9, projectDetails.getClientName(), projectDetailsRow, textWrapStyle);

            if (!taskUserActualTimeMap.isEmpty()) {

                List<Integer> employeeIndexList = getEmployeeIndexList(employees);

                for (Integer taskId : taskUserActualTimeMap.keySet()) {
                    Map<Integer, Integer> userTaskActualTimeMap = taskUserActualTimeMap.get(taskId);

                    if (!userTaskActualTimeMap.isEmpty()) {
                        for (Integer assigneeId : userTaskActualTimeMap.keySet()) {
                            Integer hours = userTaskActualTimeMap.get(assigneeId);
                            int userCellIndex = employeeIndexList.indexOf(assigneeId);
                            logDebug("Creating user task time");
                            if (userCellIndex > -1) {
                                createTaskTimeCell(sheet, projectDetailsUserColumnCount + userCellIndex, hours,
                                    projectDetailsRow, xssfCellProjectTaskHrsStyle);
                            }
                        }
                    }
                }
            }

            createProjectSumFunctionCell(projectDetailsRow, projectDetailsUserColumnCount + employees.size() + 1,
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

    private void createProjectSumFunctionCell(Row projectDetailsRow, int columnIndex,
        CellStyle xssfCellProjectTotalStyle)
    {
        Cell cell = projectDetailsRow.getCell(columnIndex);
        if (cell == null) {
            cell = projectDetailsRow.createCell(columnIndex);
        }
        cell.setCellStyle(xssfCellProjectTotalStyle);
        String cellLetter = CellReference.convertNumToColString(columnIndex - 2);
        int rowNumber = projectDetailsRow.getRowNum() + 1;
        String strFormula = "SUM(K" + rowNumber + ":" + cellLetter + rowNumber + ")";
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
            // userCell.setCellStyle(cellStyle);
        }
        sheet.autoSizeColumn(projectDetailsColumnCount);
        userCell.setCellValue(value);
    }

    private void createTaskTimeCell(Sheet sheet, int columnIndex, Integer userTaskTime, Row projectDetailsRow,
        CellStyle cellStyle)
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
        sheet.autoSizeColumn(columnIndex);
        Double cellValue = userCell.getNumericCellValue();

        if (cellValue != null) {
            userTaskTime = (int) ((userTaskTime == null ? 0 : userTaskTime) + cellValue);
        }

        double time = convertToActualTimeString(userTaskTime);
        if (time > 0) {
            userCell.setCellValue(time);
        }
    }

    private Integer convertToInt(String cellValue)
    {
        String[] time = cellValue.split("[:]");
        int hour = Integer.parseInt(time[0]);
        int mins = Integer.parseInt(time[1]);
        return (hour * 60) + mins;
    }

    private void createHeader(XSSFWorkbook workBook, Sheet sheet, String dateString, String teamName,
        CellStyle xssfCellUserHeaderStyle, CellStyle xssfCellProjectTotalStyle, List<EmployeeDetails> employees)
    {
        Row teamNameRow = sheet.getRow(0);
        Row monthRow = sheet.getRow(1);
        Row userRow = sheet.getRow(2);
        Row userDetailsHeaderRow = sheet.getRow(5);
        teamNameRow.getCell(0).setCellValue(teamName);
        monthRow.getCell(1).setCellValue(dateString);

        int userDetailsColumnCount = 1;
        if (employees != null) {
            userRow.getCell(1).setCellValue(employees.size());
            for (EmployeeDetails employeeDetails : employees) {
                Cell userHeaderCell = getCell(userDetailsHeaderRow, userDetailsColumnCount + 9);
                userHeaderCell.setCellValue(employeeDetails.getFirstName());
                userHeaderCell.setCellStyle(xssfCellUserHeaderStyle);
                sheet.autoSizeColumn(userDetailsColumnCount + 9);
                userDetailsColumnCount++;
            }

            /*
             * Cell projectTotalAdjustmentCell = getCell(userDetailsHeaderRow, userDetailsRowCount + 9);
             * sheet.autoSizeColumn(userDetailsRowCount + 9);
             * projectTotalAdjustmentCell.setCellStyle(xssfCellUserHeaderStyle);
             * projectTotalAdjustmentCell.setCellValue("Adjustment");
             */
            Cell projectTotalCell = getCell(userDetailsHeaderRow, userDetailsColumnCount + 10);
            sheet.autoSizeColumn(userDetailsColumnCount + 10);
            projectTotalCell.setCellStyle(xssfCellUserHeaderStyle);
            projectTotalCell.setCellValue("Project Total Hrs");
        }
    }

    private Cell getCell(Row row, int rowNumber)
    {
        Cell cell = row.getCell(rowNumber);
        if (cell == null) {
            cell = row.createCell(rowNumber);
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
