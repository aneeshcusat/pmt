package com.famstack.projectscheduler.export.processors;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Component;

import com.famstack.projectscheduler.BaseFamstackService;
import com.famstack.projectscheduler.employees.bean.EmployeeDetails;
import com.famstack.projectscheduler.employees.bean.ProjectDetails;
import com.famstack.projectscheduler.employees.bean.TaskDetails;

/**
 * visual service
 * 
 * @author Aneeshkumar
 */
@Component
public class FamstackXLSExportProcessor1 extends BaseFamstackService implements FamstackBaseXLSExportProcessor
{

    private static CellStyle hssfCellUserHeaderStyle;

    private static CellStyle hssfCellProjectTotalStyle;

    private static CellStyle hssfCellTextWrapStyle;

    private Sheet sheet;

    @Override
    public void renderReport(Workbook workBook, Sheet sheet, int rowCount, List<ProjectDetails> exportDataList,
        String dateString)

    {
        this.sheet = sheet;
        createHeader(workBook, sheet, dateString);
        createBody(workBook, sheet, exportDataList);

    }

    private void createBody(Workbook workBook, Sheet sheet, List<ProjectDetails> exportDataList)
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

                createProjectDetailsColoumn(0, projectDetails.getStartTime(), projectDetailsRow,
                    getTextWrapStyle(workBook));
                createProjectDetailsColoumn(1, projectDetails.getCode(), projectDetailsRow, getTextWrapStyle(workBook));

                createProjectDetailsColoumn(2, projectDetails.getPONumber(), projectDetailsRow,
                    getTextWrapStyle(workBook));
                createProjectDetailsColoumn(3, projectDetails.getName(), projectDetailsRow, getTextWrapStyle(workBook));
                createProjectDetailsColoumn(4, projectDetails.getType().toString(), projectDetailsRow,
                    getTextWrapStyle(workBook));
                createProjectDetailsColoumn(5, projectDetails.getCategory(), projectDetailsRow,
                    getTextWrapStyle(workBook));
                createProjectDetailsColoumn(6, projectDetails.getTeamName(), projectDetailsRow,
                    getTextWrapStyle(workBook));
                createProjectDetailsColoumn(7, projectDetails.getSubTeamName(), projectDetailsRow,
                    getTextWrapStyle(workBook));
                createProjectDetailsColoumn(8, projectDetails.getClientName(), projectDetailsRow,
                    getTextWrapStyle(workBook));
                createProjectDetailsColoumn(9, projectDetails.getStatus().toString(), projectDetailsRow,
                    getTextWrapStyle(workBook));

                List<EmployeeDetails> employees = getFamstackApplicationConfiguration().getUserList();
                if (employees != null) {
                    for (EmployeeDetails employeeDetails : employees) {
                        Cell userCell = projectDetailsRow.getCell(projectDetailsUserColumnCount);
                        if (userCell == null) {
                            userCell = projectDetailsRow.createCell(projectDetailsUserColumnCount);
                        }
                        String value = "";
                        if (projectDetails.getProjectTaskDeatils() != null) {
                            for (TaskDetails taskDetails : projectDetails.getProjectActualTaskDeatils()) {
                                if (taskDetails.getAssignee() == employeeDetails.getId()) {
                                    value = taskDetails.getActualTimeTakenInHrs();
                                    if (userProjectHoursMap.get(employeeDetails.getId()) == null) {
                                        userProjectHoursMap.put(employeeDetails.getId(),
                                            taskDetails.getActualTimeTaken());
                                    } else {
                                        Integer userProjectTotal = userProjectHoursMap.get(employeeDetails.getId());
                                        userProjectTotal += taskDetails.getActualTimeTaken();
                                        userProjectHoursMap.put(employeeDetails.getId(), userProjectTotal);
                                    }
                                }
                            }
                        }
                        createProjectDetailsColoumn(projectDetailsUserColumnCount, value, projectDetailsRow,
                            getTextWrapStyle(workBook));
                        projectDetailsUserColumnCount++;
                    }
                }
                projectDetailsRowCount++;
            }

            Row projectTotalHoursRow = sheet.getRow(projectDetailsRowCount);
            if (projectTotalHoursRow == null) {
                projectTotalHoursRow = sheet.createRow(projectDetailsRowCount);
            }
            List<EmployeeDetails> employees = getFamstackApplicationConfiguration().getUserList();
            if (employees != null) {
                int projectDetailsUserColumnCount = 9;
                createProjectDetailsColoumn(projectDetailsUserColumnCount++, "Project Hours", projectTotalHoursRow,
                    getProjectTotalStyle(workBook));
                for (EmployeeDetails employeeDetails : employees) {
                    Integer projectHours = userProjectHoursMap.get(employeeDetails.getId());
                    String value = projectHours == null ? "" : String.valueOf(projectHours / 60);
                    createProjectDetailsColoumn(projectDetailsUserColumnCount, value, projectTotalHoursRow,
                        getProjectTotalStyle(workBook));
                    projectDetailsUserColumnCount++;
                }
            }

        }

    }

    private void createProjectDetailsColoumn(int projectDetailsColumnCount, String value, Row projectDetailsRow,
        CellStyle cellStyle)
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

    private void createHeader(Workbook workBook, Sheet sheet, String dateString)
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

    private CellStyle getUserHeaderStyle(Workbook workbook)
    {
        if (hssfCellUserHeaderStyle == null) {
            hssfCellUserHeaderStyle = workbook.createCellStyle();
            hssfCellUserHeaderStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            hssfCellUserHeaderStyle.setWrapText(true);
            hssfCellUserHeaderStyle.setFillBackgroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            hssfCellUserHeaderStyle.setFillPattern(FillPatternType.BIG_SPOTS);
            Font font = workbook.createFont();
            font.setBold(true);
            hssfCellUserHeaderStyle.setFont(font);
        }

        return hssfCellUserHeaderStyle;
    }

    private CellStyle getTextWrapStyle(Workbook workbook)
    {
        if (hssfCellTextWrapStyle == null) {
            hssfCellTextWrapStyle = workbook.createCellStyle();
            hssfCellTextWrapStyle.setWrapText(true);
        }

        return hssfCellTextWrapStyle;
    }

    private CellStyle getProjectTotalStyle(Workbook workbook)
    {
        if (hssfCellProjectTotalStyle == null) {
            hssfCellProjectTotalStyle = workbook.createCellStyle();
            hssfCellProjectTotalStyle.setFillForegroundColor(IndexedColors.LIGHT_ORANGE.getIndex());
            hssfCellProjectTotalStyle.setFillBackgroundColor(IndexedColors.LIGHT_ORANGE.getIndex());
            hssfCellProjectTotalStyle.setFillPattern(FillPatternType.BIG_SPOTS);
            hssfCellProjectTotalStyle.setWrapText(true);
            Font font = workbook.createFont();
            font.setBold(true);
            hssfCellProjectTotalStyle.setFont(font);
        }

        return hssfCellProjectTotalStyle;
    }
}
