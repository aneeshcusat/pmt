package com.famstack.projectscheduler.export.processors;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.Resource;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import scala.actors.threadpool.Arrays;

import com.famstack.projectscheduler.BaseFamstackService;
import com.famstack.projectscheduler.datatransferobject.UserTaskActivityItem;
import com.famstack.projectscheduler.employees.bean.EmployeeDetails;
import com.famstack.projectscheduler.employees.bean.ProjectDetails;
import com.famstack.projectscheduler.employees.bean.TaskDetails;
import com.famstack.projectscheduler.manager.FamstackUserActivityManager;
import com.famstack.projectscheduler.thread.ExportProjectDetailsWorkerThread;
import com.famstack.projectscheduler.util.StringUtils;

/**
 * visual service
 * 
 * @author Aneeshkumar
 */
public class FamstackXLSExportProcessor11 extends BaseFamstackService implements FamstackBaseXLSExportProcessor
{

    @Resource
    FamstackUserActivityManager famstackUserActivityManager;

    private int maxThread;

    @Override
    public void renderReport(XSSFWorkbook workBook, Sheet sheet, String teamName, List<ProjectDetails> exportDataList,
        String dateString, List<EmployeeDetails> employees)

    {
        logDebug("Rendering reoprt for  FamstackXLSExportProcessor11");

        XSSFColor myColorIndexPink = getColor(248, 203, 173, workBook);
        XSSFColor myColorIndexGray = getColor(217, 217, 217, workBook);

        CellStyle xssfCellUserHeaderStyle = getUserHeaderStyle(workBook, myColorIndexGray);
        CellStyle xssfCellProjectTotalStyle = getProjectTotalStyle(workBook, myColorIndexPink);
        CellStyle xssfCellTextWrapStyle = getTextWrapStyle(workBook);

        createHeader(workBook, sheet, dateString, teamName, xssfCellTextWrapStyle, xssfCellUserHeaderStyle, employees);
        createBody(workBook, sheet, exportDataList, employees, xssfCellProjectTotalStyle, xssfCellTextWrapStyle);

    }

    private void createBody(final XSSFWorkbook workBook, final Sheet sheet, final List<ProjectDetails> exportDataList,
        final List<EmployeeDetails> employees, final CellStyle xssfCellProjectTotalStyle,
        final CellStyle xssfCellTextWrapStyle)
    {
        if (exportDataList != null) {
            int projectDetailsRowCount = 7;
            Map<Integer, Integer> userProjectTotalHoursMap = new ConcurrentHashMap();

            if (exportDataList.size() > 0) {
                ExecutorService executorService =
                    Executors.newFixedThreadPool(exportDataList.size() > maxThread ? maxThread : exportDataList.size());
                // List<Future> futures = Collections.synchronizedList(new ArrayList<Future>());

                List<ExportProjectDetailsWorkerThread> futures =
                    Collections.synchronizedList(new ArrayList<ExportProjectDetailsWorkerThread>());

                for (ProjectDetails projectDetails : exportDataList) {
                    ExportProjectDetailsWorkerThread projectDetailsWorkerThred =
                        new ExportProjectDetailsWorkerThread(this, workBook, sheet, projectDetailsRowCount,
                            projectDetails, userProjectTotalHoursMap, xssfCellTextWrapStyle, employees);
                    // Future future = executorService.submit(projectDetailsWorkerThred);
                    // futures.add(future);
                    futures.add(projectDetailsWorkerThred);
                    projectDetailsRowCount++;
                }

                logDebug("Starting project reporting threads");
                for (ExportProjectDetailsWorkerThread future : futures) {
                    future.run();
                }
                logDebug("Completed project reporting threads");
                executorService.shutdown();
            }

            Row projectTotalHoursRow = sheet.getRow(++projectDetailsRowCount);
            if (projectTotalHoursRow == null) {
                projectTotalHoursRow = sheet.createRow(projectDetailsRowCount);
            }
            projectTotalHoursRow.setHeight((short) 350);

            logDebug("User Project total hours map " + userProjectTotalHoursMap);

            if (employees != null) {
                int projectDetailsUserColumnCount = 9;
                createProjectDetailsColoumn(sheet, projectDetailsUserColumnCount++, "Total Hours",
                    projectTotalHoursRow, xssfCellProjectTotalStyle);
                sheet.autoSizeColumn(projectDetailsUserColumnCount - 1);
                for (EmployeeDetails employeeDetails : employees) {
                    Integer projectHours = userProjectTotalHoursMap.get(employeeDetails.getId());
                    createTaskTimeCell(sheet, projectDetailsUserColumnCount, projectHours, projectTotalHoursRow,
                        xssfCellProjectTotalStyle);
                    projectDetailsUserColumnCount++;
                }
            }

        }

    }

    public void createProjectDetailsRow(XSSFWorkbook workBook, Sheet sheet, int projectDetailsRowCount,
        ProjectDetails projectDetails, Map<Integer, Integer> userProjectTotalHoursMap, List<EmployeeDetails> employees,
        CellStyle textWrapStyle)
    {
        if (projectDetails != null) {
            int projectDetailsUserColumnCount = 10;
            Row projectDetailsRow = getRow(sheet, projectDetailsRowCount);
            Map<Integer, Map<Integer, Integer>> taskUserActualTimeMap =
                getProjectTaskDuration(projectDetails, userProjectTotalHoursMap);

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

                List<Integer> employeeIndexList = new ArrayList<>();

                for (EmployeeDetails employeeDetails : employees) {
                    employeeIndexList.add(employeeDetails.getId());
                }

                for (Integer taskId : taskUserActualTimeMap.keySet()) {
                    Map<Integer, Integer> userTaskActualTimeMap = taskUserActualTimeMap.get(taskId);

                    if (!userTaskActualTimeMap.isEmpty()) {
                        for (Integer assigneeId : userTaskActualTimeMap.keySet()) {
                            Integer hours = userTaskActualTimeMap.get(assigneeId);
                            int userCellIndex = employeeIndexList.indexOf(assigneeId);
                            logDebug("Creating user task time");
                            if (userCellIndex > -1) {
                                createTaskTimeCell(sheet, projectDetailsUserColumnCount + userCellIndex, hours,
                                    projectDetailsRow, null);
                            }
                        }
                    }
                }
            }

            createTaskTimeCell(sheet, projectDetailsUserColumnCount + employees.size() + 1,
                projectDetails.getActualDuration(), projectDetailsRow, null);
        }
    }

    private Row getRow(Sheet sheet, int rowIndex)
    {
        Row projectDetailsRow = sheet.getRow(rowIndex);
        if (projectDetailsRow == null) {
            projectDetailsRow = sheet.createRow(rowIndex);
        }
        return projectDetailsRow;
    }

    private String convertToActualTimeString(Integer timeInMins)
    {

        if (timeInMins != null) {

            int hour = timeInMins / 60;
            int minutes = timeInMins % 60;

            return hour + "." + minutes;
        }

        return "";

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
        String cellValue = userCell.getStringCellValue();

        if (StringUtils.isNotBlank(cellValue)) {
            userTaskTime = (userTaskTime == null ? 0 : userTaskTime) + convertToInt(cellValue);
        }
        userCell.setCellValue(convertToActualTimeString(userTaskTime));
    }

    private Integer convertToInt(String cellValue)
    {
        String[] time = cellValue.split("\\.");
        int hour = Integer.parseInt(time[0]);
        int mins = Integer.parseInt(time[1]);
        return (hour * 60) + mins;
    }

    private void createHeader(XSSFWorkbook workBook, Sheet sheet, String dateString, String teamName,
        CellStyle xssfCellUserHeaderStyle, CellStyle xssfCellTextWrapStyle, List<EmployeeDetails> employees)
    {
        Row teamNameRow = sheet.getRow(0);
        Row monthRow = sheet.getRow(1);
        Row userRow = sheet.getRow(2);
        Row userDetailsRow = sheet.getRow(3);
        Row userDetailsHeaderRow = sheet.getRow(6);
        teamNameRow.getCell(0).setCellValue(teamName);
        monthRow.getCell(1).setCellValue(dateString);

        int userDetailsRowCount = 1;
        if (employees != null) {
            userRow.getCell(1).setCellValue(employees.size());
            for (EmployeeDetails employeeDetails : employees) {
                Cell userCell = userDetailsRow.getCell(userDetailsRowCount);

                if (userCell == null) {
                    userCell = userDetailsRow.createCell(userDetailsRowCount);
                }
                Cell userHeaderCell = userDetailsHeaderRow.getCell(userDetailsRowCount + 9);
                if (userHeaderCell == null) {
                    userHeaderCell = userDetailsHeaderRow.createCell(userDetailsRowCount + 9);
                }

                userCell.setCellValue(employeeDetails.getFirstName());
                userHeaderCell.setCellValue(employeeDetails.getFirstName());
                userHeaderCell.setCellStyle(xssfCellUserHeaderStyle);
                userCell.setCellStyle(xssfCellTextWrapStyle);
                sheet.autoSizeColumn(userDetailsRowCount);
                userDetailsRowCount++;
            }

            Cell projectTotalCell = userDetailsHeaderRow.getCell(userDetailsRowCount + 10);

            if (projectTotalCell == null) {
                projectTotalCell = userDetailsHeaderRow.createCell(userDetailsRowCount + 10);
            }

            projectTotalCell.setCellValue("Project Total Hrs");
        }
    }

    private CellStyle getUserHeaderStyle(XSSFWorkbook workbook, XSSFColor myColorIndexGray)
    {
        XSSFCellStyle xssfCellUserHeaderStyle = workbook.createCellStyle();
        xssfCellUserHeaderStyle.setFillForegroundColor(myColorIndexGray);
        xssfCellUserHeaderStyle.setWrapText(true);
        xssfCellUserHeaderStyle.setFillBackgroundColor(myColorIndexGray);
        xssfCellUserHeaderStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        Font font = workbook.createFont();
        font.setBold(true);
        xssfCellUserHeaderStyle.setFont(font);

        return xssfCellUserHeaderStyle;
    }

    private CellStyle getTextWrapStyle(Workbook workbook)
    {
        CellStyle xssfCellTextWrapStyle = workbook.createCellStyle();
        xssfCellTextWrapStyle.setWrapText(true);

        return xssfCellTextWrapStyle;
    }

    private CellStyle getProjectTotalStyle(XSSFWorkbook workbook, XSSFColor myColorIndexPink)
    {
        XSSFCellStyle xssfCellProjectTotalStyle = workbook.createCellStyle();
        xssfCellProjectTotalStyle.setFillForegroundColor(myColorIndexPink);
        xssfCellProjectTotalStyle.setFillBackgroundColor(myColorIndexPink);
        xssfCellProjectTotalStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        xssfCellProjectTotalStyle.setWrapText(true);
        Font font = workbook.createFont();
        font.setBold(true);
        xssfCellProjectTotalStyle.setFont(font);

        return xssfCellProjectTotalStyle;
    }

    private XSSFColor getColor(int R, int G, int B, Workbook workbook)
    {

        return new XSSFColor(new java.awt.Color(R, G, B));
    }

    public Map<Integer, Map<Integer, Integer>> getProjectTaskDuration(ProjectDetails projectDetails,
        Map<Integer, Integer> userProjectTotalHoursMap)
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

                            calculateUsersTotalTaskTime(userProjectTotalHoursMap, contributer, currentTime);
                        }

                    } else if (taskDetails.getContributers()[0] != 0) {
                        userTaskActualTimeMap.put(taskDetails.getContributers()[0], taskDetails.getActualTimeTaken());
                        calculateUsersTotalTaskTime(userProjectTotalHoursMap, taskDetails.getContributers()[0],
                            taskDetails.getActualTimeTaken());
                    }
                }
            }
        }
        return taskUserActualTimeMap;
    }

    private void calculateUsersTotalTaskTime(Map<Integer, Integer> userProjectTotalHoursMap, Integer contributer,
        Integer currenTime)
    {
        Integer totalUserTimeInMinutes = userProjectTotalHoursMap.get(contributer);
        if (totalUserTimeInMinutes != null) {
            totalUserTimeInMinutes += currenTime;
        } else {
            totalUserTimeInMinutes = currenTime;
        }
        userProjectTotalHoursMap.put(contributer, totalUserTimeInMinutes);
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
