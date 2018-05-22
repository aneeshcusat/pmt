package com.famstack.projectscheduler.export.processors;

import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.famstack.projectscheduler.BaseFamstackService;
import com.famstack.projectscheduler.employees.bean.EmployeeDetails;
import com.famstack.projectscheduler.employees.bean.ProjectDetails;

/**
 * team2
 * 
 * @author Aneeshkumar
 */
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class FamstackXLSExportProcessor1 extends BaseFamstackService implements FamstackBaseXLSExportProcessor
{

    private static CellStyle xssfCellTextStyle;

    @Override
    public void renderReport(Map<String, Object> dataMap)

    {
        XSSFWorkbook workBook = (XSSFWorkbook) dataMap.get("workBook");
        Sheet sheet = (Sheet) dataMap.get("sheet");
        List<ProjectDetails> exportDataList = (List<ProjectDetails>) dataMap.get("exportDataList");

        xssfCellTextStyle = null;
        createBody(workBook, sheet, exportDataList);

    }

    private void createBody(XSSFWorkbook workBook, Sheet sheet, List<ProjectDetails> exportDataList)
    {

        if (exportDataList != null) {
            int projectDetailsRowCount = 3;
            for (ProjectDetails projectDetails : exportDataList) {
                Row projectDetailsRow = sheet.getRow(projectDetailsRowCount);
                if (projectDetailsRow == null) {
                    projectDetailsRow = sheet.createRow(projectDetailsRowCount);
                }
                createProjectDetailsColoumn(sheet, 1, projectDetails.getStartTime(), projectDetailsRow,
                    getTextStyle(workBook));
                createProjectDetailsColoumn(sheet, 2, projectDetails.getAssigneeNames(), projectDetailsRow,
                    getTextStyle(workBook));

                EmployeeDetails employeeDetails =
                    getFamstackApplicationConfiguration().getUserMap().get(projectDetails.getProjectLead());
                createProjectDetailsColoumn(sheet, 3, employeeDetails != null ? employeeDetails.getFirstName() : "",
                    projectDetailsRow, getTextStyle(workBook));

                createProjectDetailsColoumn(sheet, 4, projectDetails.getType().toString(), projectDetailsRow,
                    getTextStyle(workBook));
                createProjectDetailsColoumn(sheet, 5, projectDetails.getCategory(), projectDetailsRow,
                    getTextStyle(workBook));
                createProjectDetailsColoumn(sheet, 6, projectDetails.getProjectSubType() != null ? projectDetails
                    .getProjectSubType().toString() : "", projectDetailsRow, getTextStyle(workBook));
                createProjectDetailsColoumn(sheet, 7, projectDetails.getTeamName(), projectDetailsRow,
                    getTextStyle(workBook));
                createProjectDetailsColoumn(sheet, 8, projectDetails.getClientName(), projectDetailsRow,
                    getTextStyle(workBook));
                createProjectDetailsColoumn(sheet, 9, projectDetails.getReporterName(), projectDetailsRow,
                    getTextStyle(workBook));
                createProjectDetailsColoumn(sheet, 10, projectDetails.getName(), projectDetailsRow,
                    getTextStyle(workBook));

                createProjectDetailsColoumn(sheet, 11, projectDetails.getActualDurationInHrs(), projectDetailsRow,
                    getTextStyle(workBook));

                projectDetailsRowCount++;
            }

        }

    }

    private CellStyle getTextStyle(XSSFWorkbook workBook)
    {
        return xssfCellTextStyle;
    }

    private void createProjectDetailsColoumn(Sheet sheet, int projectDetailsColumnCount, String value,
        Row projectDetailsRow, CellStyle cellStyle)
    {
        Cell projectDataCell = projectDetailsRow.getCell(projectDetailsColumnCount);
        if (projectDataCell == null) {
            projectDataCell = projectDetailsRow.createCell(projectDetailsColumnCount);
        }
        if (cellStyle != null) {
            projectDataCell.setCellStyle(cellStyle);
        }
        sheet.autoSizeColumn(projectDetailsColumnCount);
        projectDataCell.setCellValue(value);
    }

}
