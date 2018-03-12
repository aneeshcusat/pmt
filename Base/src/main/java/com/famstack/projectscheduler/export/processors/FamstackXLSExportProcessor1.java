package com.famstack.projectscheduler.export.processors;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import com.famstack.projectscheduler.util.StringUtils;

/**
 * visual service
 * 
 * @author Aneeshkumar
 */
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class FamstackXLSExportProcessor1 extends BaseFamstackService implements FamstackBaseXLSExportProcessor
{

    @Resource
    FamstackUserActivityManager famstackUserActivityManager;

    private static XSSFCellStyle xssfCellUserHeaderStyle;

    private static XSSFCellStyle xssfCellProjectTotalStyle;

    private static CellStyle xssfCellTextWrapStyle;

    private static XSSFColor myColorIndexPink;

    private static XSSFColor myColorIndexGray;

    @Override
    public void renderReport(XSSFWorkbook workBook, Sheet sheet, int rowCount, List<ProjectDetails> exportDataList,
        String dateString)

    {
        xssfCellUserHeaderStyle = null;
        xssfCellProjectTotalStyle = null;
        xssfCellTextWrapStyle = null;
        myColorIndexPink = null;
        myColorIndexGray = null;
        createHeader(workBook, sheet, dateString);
        createBody(workBook, sheet, exportDataList);

    }

    private void createBody(XSSFWorkbook workBook, Sheet sheet, List<ProjectDetails> exportDataList)
    {

        if (exportDataList != null) {
            int projectDetailsRowCount = 7;
            Map<Integer, Integer> userProjectHoursMap = new HashMap<>();
            for (ProjectDetails projectDetails : exportDataList) {
                int projectDetailsUserColumnCount = 10;
                Row projectDetailsRow = sheet.getRow(projectDetailsRowCount);
                if (projectDetailsRow == null) {
                    projectDetailsRow = sheet.createRow(projectDetailsRowCount);
                }

                createProjectDetailsColoumn(sheet, 0, projectDetails.getStartTime(), projectDetailsRow,
                    getTextWrapStyle(workBook));
                createProjectDetailsColoumn(sheet, 1, projectDetails.getCode(), projectDetailsRow,
                    getTextWrapStyle(workBook));

                createProjectDetailsColoumn(sheet, 2, "" + projectDetails.getId(), projectDetailsRow,
                    getTextWrapStyle(workBook));

                createProjectDetailsColoumn(sheet, 3, projectDetails.getPONumber(), projectDetailsRow,
                    getTextWrapStyle(workBook));
                createProjectDetailsColoumn(sheet, 4, projectDetails.getName(), projectDetailsRow,
                    getTextWrapStyle(workBook));
                createProjectDetailsColoumn(sheet, 5, projectDetails.getType().toString(), projectDetailsRow,
                    getTextWrapStyle(workBook));
                createProjectDetailsColoumn(sheet, 6, projectDetails.getCategory(), projectDetailsRow,
                    getTextWrapStyle(workBook));
                createProjectDetailsColoumn(sheet, 7, projectDetails.getTeamName(), projectDetailsRow,
                    getTextWrapStyle(workBook));
                createProjectDetailsColoumn(sheet, 8, projectDetails.getSubTeamName(), projectDetailsRow,
                    getTextWrapStyle(workBook));
                createProjectDetailsColoumn(sheet, 9, projectDetails.getClientName(), projectDetailsRow,
                    getTextWrapStyle(workBook));

                List<EmployeeDetails> employees = getFamstackApplicationConfiguration().getUserList();
                if (employees != null) {
                    Map<Integer, Map<Integer, Integer>> taskUserActualTimeMap = new HashMap<>();

                    for (EmployeeDetails employeeDetails : employees) {
                        logDebug("userId : " + employeeDetails.getId() + " name : " + employeeDetails.getFirstName());
                        Cell userCell = projectDetailsRow.getCell(projectDetailsUserColumnCount);
                        if (userCell == null) {
                            userCell = projectDetailsRow.createCell(projectDetailsUserColumnCount);
                        }
                        if (projectDetails.getProjectTaskDeatils() != null) {
                            for (TaskDetails taskDetails : projectDetails.getProjectTaskDeatils()) {

                                Map<Integer, Integer> userTaskActualTimeMap =
                                    taskUserActualTimeMap.get(taskDetails.getTaskId());

                                if (userTaskActualTimeMap == null) {
                                    userTaskActualTimeMap = new HashMap<>();
                                    taskUserActualTimeMap.put(taskDetails.getTaskId(), userTaskActualTimeMap);

                                    Set<Integer> contributers =
                                        new HashSet<>(Arrays.asList(taskDetails.getContributers()));
                                    logDebug("Task id :" + taskDetails.getTaskId() + " Contributers :" + contributers);
                                    if (contributers.size() > 1) {
                                        List<UserTaskActivityItem> userTaskActivityItems =
                                            (List<UserTaskActivityItem>) famstackUserActivityManager
                                                .getUserTaskActivityItemByTaskId(taskDetails.getTaskId());
                                        for (Integer contributer : contributers) {
                                            for (UserTaskActivityItem userTaskActivityItem : userTaskActivityItems) {
                                                if (userTaskActivityItem.getUserActivityItem().getUserItem().getId() == contributer) {
                                                    Integer currenTime = userTaskActualTimeMap.get(contributer);
                                                    if (currenTime != null) {
                                                        currenTime += userTaskActivityItem.getDurationInMinutes();
                                                    } else {
                                                        currenTime = userTaskActivityItem.getDurationInMinutes();
                                                    }
                                                    userTaskActualTimeMap.put(contributer, currenTime);
                                                }
                                            }
                                        }

                                    } else if (taskDetails.getContributers()[0] != 0) {
                                        userTaskActualTimeMap.put(taskDetails.getContributers()[0],
                                            taskDetails.getActualTimeTaken());
                                    }
                                }

                                logDebug("taskUserActualTimeMap : " + taskUserActualTimeMap);

                                Integer userTaskTime =
                                    taskUserActualTimeMap.get(taskDetails.getTaskId()).get(employeeDetails.getId());

                                createTaskTimeCell(sheet, projectDetailsUserColumnCount, userTaskTime,
                                    projectDetailsRow, getTextWrapStyle(workBook));

                                if (userProjectHoursMap.get(employeeDetails.getId()) == null) {
                                    userProjectHoursMap.put(employeeDetails.getId(), userTaskTime);
                                } else {
                                    Integer userProjectTotal = userProjectHoursMap.get(employeeDetails.getId());
                                    userProjectTotal += (userTaskTime == null ? 0 : userTaskTime);
                                    userProjectHoursMap.put(employeeDetails.getId(), userProjectTotal);
                                }
                            }
                        }

                        projectDetailsUserColumnCount++;
                    }
                }
                projectDetailsRowCount++;
            }

            Row projectTotalHoursRow = sheet.getRow(++projectDetailsRowCount);
            if (projectTotalHoursRow == null) {
                projectTotalHoursRow = sheet.createRow(projectDetailsRowCount);
            }
            projectTotalHoursRow.setHeight((short) 350);
            List<EmployeeDetails> employees = getFamstackApplicationConfiguration().getUserList();
            if (employees != null) {
                int projectDetailsUserColumnCount = 9;
                createProjectDetailsColoumn(sheet, projectDetailsUserColumnCount++, "Total Hours",
                    projectTotalHoursRow, getProjectTotalStyle(workBook));
                sheet.autoSizeColumn(projectDetailsUserColumnCount - 1);
                for (EmployeeDetails employeeDetails : employees) {
                    Integer projectHours = userProjectHoursMap.get(employeeDetails.getId());
                    String value = convertToActualTimeString(projectHours);
                    createProjectDetailsColoumn(sheet, projectDetailsUserColumnCount, value, projectTotalHoursRow,
                        getProjectTotalStyle(workBook));
                    projectDetailsUserColumnCount++;
                }
            }

        }

    }

    private String convertToActualTimeString(Integer timeInMins)
    {

        return timeInMins == null ? "" : String.valueOf((timeInMins / 60 < 10 ? "0" + (timeInMins / 60)
            : timeInMins / 60))
            + ":"
            + ((timeInMins % 60 > 0) ? (timeInMins % 60 < 10 ? "0" + (timeInMins % 60) : timeInMins % 60) : "00");
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
        sheet.autoSizeColumn(projectDetailsColumnCount);
        userCell.setCellValue(value);
    }

    private void createTaskTimeCell(Sheet sheet, int projectDetailsColumnCount, Integer userTaskTime,
        Row projectDetailsRow, CellStyle cellStyle)
    {
        Cell userCell = projectDetailsRow.getCell(projectDetailsColumnCount);
        if (userCell == null) {
            userCell = projectDetailsRow.createCell(projectDetailsColumnCount);
        }
        if (cellStyle != null) {
            userCell.setCellStyle(cellStyle);
        }
        sheet.autoSizeColumn(projectDetailsColumnCount);
        String cellValue = userCell.getStringCellValue();

        if (StringUtils.isNotBlank(cellValue)) {
            userTaskTime = (userTaskTime == null ? 0 : userTaskTime) + convertToInt(cellValue);
        }
        userCell.setCellValue(convertToActualTimeString(userTaskTime));
    }

    private Integer convertToInt(String cellValue)
    {
        String[] time = cellValue.split(":");
        int hour = Integer.parseInt(time[0]);
        int mins = Integer.parseInt(time[1]);
        return (hour * 60) + mins;
    }

    private void createHeader(XSSFWorkbook workBook, Sheet sheet, String dateString)
    {
        Row monthRow = sheet.getRow(1);
        Row userRow = sheet.getRow(2);
        Row userDetailsRow = sheet.getRow(3);
        Row userDetailsHeaderRow = sheet.getRow(6);

        monthRow.getCell(1).setCellValue(dateString);

        List<EmployeeDetails> employees = getFamstackApplicationConfiguration().getUserList();
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
                userHeaderCell.setCellStyle(getUserHeaderStyle(workBook));
                userCell.setCellStyle(getTextWrapStyle(workBook));
                sheet.autoSizeColumn(userDetailsRowCount);
                userDetailsRowCount++;
            }
        }
    }

    private CellStyle getUserHeaderStyle(XSSFWorkbook workbook)
    {
        if (xssfCellUserHeaderStyle == null) {
            xssfCellUserHeaderStyle = workbook.createCellStyle();
            if (myColorIndexGray == null) {
                myColorIndexGray = getColor(217, 217, 217, workbook);
            }
            xssfCellUserHeaderStyle.setFillForegroundColor(myColorIndexGray);
            xssfCellUserHeaderStyle.setWrapText(true);
            xssfCellUserHeaderStyle.setFillBackgroundColor(myColorIndexGray);
            xssfCellUserHeaderStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            Font font = workbook.createFont();
            font.setBold(true);
            xssfCellUserHeaderStyle.setFont(font);
        }

        return xssfCellUserHeaderStyle;
    }

    private CellStyle getTextWrapStyle(Workbook workbook)
    {
        if (xssfCellTextWrapStyle == null) {
            xssfCellTextWrapStyle = workbook.createCellStyle();
            xssfCellTextWrapStyle.setWrapText(true);
        }

        return xssfCellTextWrapStyle;
    }

    private CellStyle getProjectTotalStyle(XSSFWorkbook workbook)
    {
        if (xssfCellProjectTotalStyle == null) {
            xssfCellProjectTotalStyle = workbook.createCellStyle();
            if (myColorIndexPink == null) {
                myColorIndexPink = getColor(248, 203, 173, workbook);
            }
            xssfCellProjectTotalStyle.setFillForegroundColor(myColorIndexPink);
            xssfCellProjectTotalStyle.setFillBackgroundColor(myColorIndexPink);
            xssfCellProjectTotalStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            xssfCellProjectTotalStyle.setWrapText(true);
            Font font = workbook.createFont();
            font.setBold(true);
            xssfCellProjectTotalStyle.setFont(font);
        }

        return xssfCellProjectTotalStyle;
    }

    private XSSFColor getColor(int R, int G, int B, Workbook workbook)
    {

        /*
         * XSSFWorkbook hwb = (XSSFWorkbook) workbook; HSSFPalette palette = hwb.getC(); HSSFColor myColor =
         * palette.findSimilarColor(R, G, B); short palIndex = myColor.getIndex();
         */
        return new XSSFColor(new java.awt.Color(R, G, B));
    }
}
