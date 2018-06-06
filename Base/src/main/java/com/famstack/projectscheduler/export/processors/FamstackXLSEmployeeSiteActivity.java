package com.famstack.projectscheduler.export.processors;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.famstack.projectscheduler.BaseFamstackService;
import com.famstack.projectscheduler.employees.bean.EmployeeDetails;
import com.famstack.projectscheduler.employees.bean.UserGroupDetails;
import com.famstack.projectscheduler.util.DateTimePeriod;
import com.famstack.projectscheduler.util.DateUtils;
import com.famstack.projectscheduler.util.StringUtils;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class FamstackXLSEmployeeSiteActivity extends BaseFamstackService implements FamstackBaseXLSExportProcessor
{

    @Override
    public void renderReport(Map<String, Object> dataMap)
    {

        XSSFWorkbook workBook = (XSSFWorkbook) dataMap.get("workBook");
        Sheet sheet = (Sheet) dataMap.get("sheet");
        Map<Integer, Map<String, String>> exportDataList =
            (Map<Integer, Map<String, String>>) dataMap.get("exportDataList");
        String dateString = (String) dataMap.get("dateString");
        Map<Integer, EmployeeDetails> employees = (Map<Integer, EmployeeDetails>) dataMap.get("allEmployeeData");
        Map<String, UserGroupDetails> userGroupMap = (Map<String, UserGroupDetails>) dataMap.get("userGroupMap");
        logDebug("Rendering employee utilisation");
        String[] dateRanges;

        if (StringUtils.isNotBlank(dateString) && exportDataList != null && !exportDataList.isEmpty()) {
            dateRanges = dateString.split("-");
            if (dateRanges != null && dateRanges.length > 1) {
                Date startDate = DateUtils.tryParse(dateRanges[0].trim(), DateUtils.DATE_FORMAT_DP);
                Date endDate = DateUtils.tryParse(dateRanges[1].trim(), DateUtils.DATE_FORMAT_DP);
                endDate = DateUtils.getNextPreviousDate(DateTimePeriod.DAY, endDate, 1);
                List<String> dateList = new ArrayList<>();
                while (startDate.before(endDate)) {
                    dateList.add(DateUtils.format(startDate, DateUtils.DATE_FORMAT));
                    startDate = DateUtils.getNextPreviousDate(DateTimePeriod.DAY, startDate, 1);
                }

                int rowIndex = 0;
                createHeader(sheet, dateList);
                rowIndex++;
                if (employees != null) {
                    for (Integer userId : employees.keySet()) {
                        int colIndex = 0;
                        Row row = getRow(sheet, rowIndex);
                        Cell cell = getCell(sheet, row, colIndex++);

                        cell.setCellValue(employees.get(userId).getFirstName());
                        cell = getCell(sheet, row, colIndex++);

                        UserGroupDetails userGroupDetails = userGroupMap.get(employees.get(userId).getUserGroupId());
                        cell.setCellValue(userGroupDetails == null ? "" : userGroupDetails.getName());
                        Map<String, String> userActivity = exportDataList.get(userId);

                        if (userActivity != null) {

                            for (String userActiveDate : userActivity.keySet()) {
                                colIndex = dateList.indexOf(userActiveDate) + 2;
                                cell = getCell(sheet, row, colIndex);
                                cell.setCellValue("Active");
                            }
                        }
                        rowIndex++;
                    }
                }
            }
        }

    }

    private void createHeader(Sheet sheet, List<String> dateList)
    {
        int colIndex = 0;
        Row row = getRow(sheet, 0);
        Cell cell = getCell(sheet, row, colIndex++);
        cell.setCellValue("Employee Names");
        cell = getCell(sheet, row, colIndex++);
        cell.setCellValue("Team Name");
        for (String headerDate : dateList) {
            cell = getCell(sheet, row, colIndex++);
            cell.setCellValue(headerDate);
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

    private Row getRow(Sheet sheet, int rowIndex)
    {
        Row projectDetailsRow = sheet.getRow(rowIndex);
        if (projectDetailsRow == null) {
            projectDetailsRow = sheet.createRow(rowIndex);
        }
        return projectDetailsRow;
    }
}
