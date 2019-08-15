package com.famstack.projectscheduler.export.processors;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Comment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFComment;
import org.apache.poi.xssf.usermodel.XSSFCreationHelper;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.famstack.projectscheduler.BaseFamstackService;
import com.famstack.projectscheduler.contants.FamstackConstants;
import com.famstack.projectscheduler.datatransferobject.UserTaskActivityItem;
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
        XSSFSheet sheet = (XSSFSheet) dataMap.get("sheet");
        Map<Integer, Map<String, String>> exportDataList =
            (Map<Integer, Map<String, String>>) dataMap.get("exportDataList");
        Map<Integer, Map<String, UserTaskActivityItem>> nonBillativityMap =
            (Map<Integer, Map<String, UserTaskActivityItem>>) dataMap.get("nonBillativityMap");
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
                        Map<String, UserTaskActivityItem> nonBillableTaskItem = nonBillativityMap.get(userId);
                        if (userActivity != null) {

                            for (String userActiveDate : userActivity.keySet()) {
                                colIndex = dateList.indexOf(userActiveDate) + 2;
                                cell = getCell(sheet, row, colIndex);
                                cell.setCellValue("Active");
                            }
                        }

                        if (nonBillableTaskItem != null) {
                            for (String userNonBillableDate : nonBillableTaskItem.keySet()) {
                                colIndex = dateList.indexOf(userNonBillableDate) + 2;
                                cell = getCell(sheet, row, colIndex);
                                UserTaskActivityItem userTaskActivityItem =
                                    nonBillableTaskItem.get(userNonBillableDate);

                                if (userTaskActivityItem != null
                                    && (FamstackConstants.LEAVE.equalsIgnoreCase(userTaskActivityItem.getTaskActCategory()) || 
                                    		FamstackConstants.HOLIDAY.equalsIgnoreCase(userTaskActivityItem.getTaskActCategory()) ||
                                    		FamstackConstants.LEAVE_OR_HOLIDAY.equalsIgnoreCase(userTaskActivityItem.getTaskActCategory()))) {
                                    cell.setCellValue(userTaskActivityItem.getTaskActCategory());
                                    String startTimeString =
                                        DateUtils.format(new Date(userTaskActivityItem.getActualStartTime().getTime()),
                                            DateUtils.DATE_TIME_FORMAT);
                                    String endTimeString =
                                        DateUtils.format(new Date(userTaskActivityItem.getActualEndTime().getTime()),
                                            DateUtils.DATE_TIME_FORMAT);
                                    Comment comment =
                                        getComment(workBook, sheet, userTaskActivityItem.getTaskName() + ", Time : "
                                            + startTimeString + " - " + endTimeString);
                                    logDebug("Creating cell c-" + colIndex + " r-" + rowIndex + " comment : " + comment);
                                    cell.setCellComment(comment);
                                }
                            }
                        }
                        rowIndex++;
                    }
                }
            }
        }

    }

    private Comment getComment(XSSFWorkbook workBook, XSSFSheet sheet, String commentString)
    {
        XSSFCreationHelper richTextFactory = workBook.getCreationHelper();
        XSSFDrawing drawing = sheet.createDrawingPatriarch();
        XSSFClientAnchor anchor = drawing.createAnchor(0, 0, 0, 0, 0, 5, 10, 15);
        XSSFComment comment1 = drawing.createCellComment(anchor);
        XSSFRichTextString rtf1 = richTextFactory.createRichTextString(commentString);
        comment1.setString(rtf1);
        return comment1;
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
