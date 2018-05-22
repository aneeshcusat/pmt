package com.famstack.projectscheduler.export.processors;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.famstack.projectscheduler.BaseFamstackService;
import com.famstack.projectscheduler.employees.bean.EmployeeDetails;
import com.famstack.projectscheduler.employees.bean.UserWorkDetails;
import com.famstack.projectscheduler.util.DateTimePeriod;
import com.famstack.projectscheduler.util.DateUtils;
import com.famstack.projectscheduler.util.StringUtils;

/**
 * emp utilisation sheet
 * 
 * @author Aneeshkumar
 */
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class FamstackXLSEmployeeUtilisationProcessor extends BaseFamstackService implements
    FamstackBaseXLSExportProcessor
{

    private static final String EMPTY_STRING = "";

    private static XSSFCellStyle dateHeaderCellStyle = null;

    private static XSSFCellStyle billableUtilCellStyle = null;

    private static XSSFCellStyle nonBillableUtilCellStyle = null;

    private static XSSFCellStyle leaveCellStyle = null;

    private static XSSFCellStyle empHeaderCellStyle = null;

    private static XSSFColor dateHeaderColor = null;

    private static XSSFColor billableColor = null;

    private static XSSFColor nonBillableColor = null;

    private static XSSFColor leaveColor = null;

    private static XSSFColor employeeHeaderColor = null;

    public void renderReport(XSSFWorkbook workbook, Sheet sheet,
        Map<String, Map<Integer, UserWorkDetails>> employeeUtilizationData, String dateString,
        List<EmployeeDetails> employees)
    {

        dateHeaderCellStyle = null;

        billableUtilCellStyle = null;

        nonBillableUtilCellStyle = null;

        leaveCellStyle = null;

        empHeaderCellStyle = null;

        dateHeaderColor = null;

        billableColor = null;

        nonBillableColor = null;

        leaveColor = null;

        employeeHeaderColor = null;

        logDebug("Rendering employee utilisation");
        String[] dateRanges;
        if (StringUtils.isNotBlank(dateString) && employeeUtilizationData != null && !employeeUtilizationData.isEmpty()) {
            logDebug("employeeUtilizationData : " + employeeUtilizationData);
            dateRanges = dateString.split("-");
            if (dateRanges != null && dateRanges.length > 1) {
                Date startDate = DateUtils.tryParse(dateRanges[0].trim(), DateUtils.DATE_FORMAT_DP);
                Date endDate = DateUtils.tryParse(dateRanges[1].trim(), DateUtils.DATE_FORMAT_DP);

                List<String> dateList = new ArrayList<>();
                while (startDate.before(endDate)) {
                    dateList.add(DateUtils.format(startDate, DateUtils.DATE_FORMAT_DP));
                    startDate = DateUtils.getNextPreviousDate(DateTimePeriod.DAY, startDate, 1);
                }

                getBillableCellStyle(workbook);
                getDateHeaderCellStyle(workbook);
                getEmployeeHeaderCellStyle(workbook);
                getLeaveCellStyle(workbook);
                getNonBillableCellStyle(workbook);

                int rowIndex = 4;
                if (employees != null) {
                    for (EmployeeDetails employeeDetails : employees) {
                        int colIndex = 0;
                        createEmpCell(sheet, colIndex++, rowIndex, employeeDetails.getFirstName(), null);

                        for (String reportDateString : dateList) {
                            if (rowIndex == 5) {
                                createDateHeaderCell(sheet, colIndex, rowIndex - 3, reportDateString,
                                    dateHeaderCellStyle);

                                createDateHeaderCell(sheet, colIndex, rowIndex - 2, empHeaderCellStyle);

                            }
                            Map<Integer, UserWorkDetails> userWorkMap = employeeUtilizationData.get(reportDateString);
                            UserWorkDetails userWorkDetails = null;
                            if (userWorkMap != null) {
                                userWorkDetails = userWorkMap.get(employeeDetails.getId());
                            }
                            createUtilizationCells(sheet, colIndex, rowIndex, userWorkDetails, billableUtilCellStyle,
                                nonBillableUtilCellStyle, leaveCellStyle);
                            colIndex += 3;
                        }
                        rowIndex++;
                    }
                }
            }
        }
    }

    private void createDateHeaderCell(Sheet sheet, int colIndex, int rowIndex, XSSFCellStyle cellStyle)
    {
        createCell(sheet, colIndex++, rowIndex, "B", cellStyle);
        createCell(sheet, colIndex++, rowIndex, "Non-B", cellStyle);
        createCell(sheet, colIndex++, rowIndex, "Leave", cellStyle);

    }

    private void createUtilizationCells(Sheet sheet, int colIndex, int rowIndex, UserWorkDetails userWorkDetails,
        CellStyle billableUtilCellStyle, CellStyle nonBillableUtilCellStyle, CellStyle leaveCellStyle)
    {
        String billableHours = EMPTY_STRING;
        String nonBillableHours = EMPTY_STRING;
        String leaveHours = EMPTY_STRING;

        if (userWorkDetails != null) {
            billableHours = convertToHours(userWorkDetails.getBillableMins());
            nonBillableHours = convertToHours(userWorkDetails.getNonBillableMins());
            leaveHours = convertToHours(userWorkDetails.getLeaveMins());
        }

        createCell(sheet, colIndex++, rowIndex, billableHours, billableUtilCellStyle);
        createCell(sheet, colIndex++, rowIndex, nonBillableHours, nonBillableUtilCellStyle);
        createCell(sheet, colIndex++, rowIndex, leaveHours, leaveCellStyle);

    }

    private String convertToHours(Integer timeInMins)
    {
        if (timeInMins == 0) {
            return EMPTY_STRING;
        }

        int hours = timeInMins / 60;
        int mins = timeInMins % 60;

        String timeString = "";
        if (hours < 10) {
            timeString += "0";
        }
        timeString += hours;

        timeString += ":";
        if (mins < 10) {
            timeString += "0";
        }
        timeString += mins;

        return timeString;

    }

    private void createCell(Sheet sheet, int colIndex, int rowIndex, String value, CellStyle cellStyle)
    {
        Row row = sheet.getRow(rowIndex);
        if (row == null) {
            row = sheet.createRow(rowIndex);
        }

        Cell cell = row.getCell(colIndex);

        if (cell == null) {
            cell = row.createCell(colIndex);
        }

        cell.setCellValue(value);

        if (cellStyle != null) {
            cell.setCellStyle(cellStyle);
        }

    }

    private void createDateHeaderCell(Sheet sheet, int colIndex, int rowIndex, String value,
        CellStyle dateHeaderCellStyle)
    {
        CellRangeAddress cellRangeAddress = new CellRangeAddress(rowIndex, rowIndex, colIndex, colIndex + 2);
        sheet.addMergedRegion(cellRangeAddress);

        createCell(sheet, colIndex, rowIndex, value, dateHeaderCellStyle);

    }

    private void createEmpCell(Sheet sheet, int colIndex, int rowIndex, String value, CellStyle empHeaderCellStyle)
    {
        createCell(sheet, colIndex, rowIndex, value, empHeaderCellStyle);
        sheet.autoSizeColumn(colIndex);
    }

    private CellStyle getDateHeaderCellStyle(XSSFWorkbook workbook)
    {
        if (dateHeaderCellStyle == null) {
            dateHeaderCellStyle = workbook.createCellStyle();
            if (dateHeaderColor == null) {
                dateHeaderColor = getColor(117, 113, 113, workbook);
            }
            dateHeaderCellStyle.setFillForegroundColor(dateHeaderColor);
            dateHeaderCellStyle.setFillBackgroundColor(dateHeaderColor);
            dateHeaderCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            dateHeaderCellStyle.setWrapText(true);
            dateHeaderCellStyle.setBorderLeft(BorderStyle.MEDIUM);
            dateHeaderCellStyle.setBorderRight(BorderStyle.MEDIUM);
            dateHeaderCellStyle.setAlignment(HorizontalAlignment.CENTER);
            Font font = workbook.createFont();
            font.setBold(true);
            dateHeaderCellStyle.setFont(font);
        }

        return dateHeaderCellStyle;
    }

    private CellStyle getEmployeeHeaderCellStyle(XSSFWorkbook workbook)
    {
        if (empHeaderCellStyle == null) {
            empHeaderCellStyle = workbook.createCellStyle();
            if (employeeHeaderColor == null) {
                employeeHeaderColor = getColor(166, 166, 166, workbook);
            }
            empHeaderCellStyle.setFillForegroundColor(employeeHeaderColor);
            empHeaderCellStyle.setFillBackgroundColor(employeeHeaderColor);
            empHeaderCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            empHeaderCellStyle.setWrapText(true);
            empHeaderCellStyle.setBorderLeft(BorderStyle.THICK);
            empHeaderCellStyle.setBorderRight(BorderStyle.THICK);
            empHeaderCellStyle.setBorderBottom(BorderStyle.THIN);
            empHeaderCellStyle.setBorderTop(BorderStyle.THIN);
            empHeaderCellStyle.setAlignment(HorizontalAlignment.CENTER);
            Font font = workbook.createFont();
            font.setBold(true);
            empHeaderCellStyle.setFont(font);
        }

        return empHeaderCellStyle;
    }

    private CellStyle getBillableCellStyle(XSSFWorkbook workbook)
    {
        if (billableUtilCellStyle == null) {
            billableUtilCellStyle = workbook.createCellStyle();
            if (billableColor == null) {
                billableColor = getColor(226, 239, 218, workbook);
            }
            billableUtilCellStyle.setFillForegroundColor(billableColor);
            billableUtilCellStyle.setFillBackgroundColor(billableColor);
            billableUtilCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            billableUtilCellStyle.setWrapText(true);
            billableUtilCellStyle.setBorderLeft(BorderStyle.THICK);
            billableUtilCellStyle.setBorderBottom(BorderStyle.THIN);
            // billableUtilCellStyle.setBorderRight(BorderStyle.THICK);
            billableUtilCellStyle.setAlignment(HorizontalAlignment.CENTER);
            // Font font = workbook.createFont();
            // font.setBold(true);
            // billableUtilCellStyle.setFont(font);
        }

        return billableUtilCellStyle;
    }

    private CellStyle getNonBillableCellStyle(XSSFWorkbook workbook)
    {
        if (nonBillableUtilCellStyle == null) {
            nonBillableUtilCellStyle = workbook.createCellStyle();
            if (nonBillableColor == null) {
                nonBillableColor = getColor(198, 224, 180, workbook);
            }
            nonBillableUtilCellStyle.setFillForegroundColor(nonBillableColor);
            nonBillableUtilCellStyle.setFillBackgroundColor(nonBillableColor);
            nonBillableUtilCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            nonBillableUtilCellStyle.setWrapText(true);
            nonBillableUtilCellStyle.setBorderBottom(BorderStyle.THIN);
            // nonBillableUtilCellStyle.setBorderLeft(BorderStyle.THICK);
            // billableUtilCellStyle.setBorderRight(BorderStyle.THICK);
            nonBillableUtilCellStyle.setAlignment(HorizontalAlignment.CENTER);
            // Font font = workbook.createFont();
            // font.setBold(true);
            // nonBillableUtilCellStyle.setFont(font);
        }

        return nonBillableUtilCellStyle;
    }

    private CellStyle getLeaveCellStyle(XSSFWorkbook workbook)
    {
        if (leaveCellStyle == null) {
            leaveCellStyle = workbook.createCellStyle();
            if (leaveColor == null) {
                leaveColor = getColor(252, 228, 214, workbook);
            }
            leaveCellStyle.setFillForegroundColor(leaveColor);
            leaveCellStyle.setFillBackgroundColor(leaveColor);
            leaveCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            leaveCellStyle.setWrapText(true);
            // nonBillableUtilCellStyle.setBorderLeft(BorderStyle.THICK);
            leaveCellStyle.setBorderRight(BorderStyle.THICK);
            leaveCellStyle.setBorderBottom(BorderStyle.THIN);
            leaveCellStyle.setAlignment(HorizontalAlignment.CENTER);
            // Font font = workbook.createFont();
            // font.setBold(true);
            // nonBillableUtilCellStyle.setFont(font);
        }

        return leaveCellStyle;
    }

    private XSSFColor getColor(int R, int G, int B, Workbook workbook)
    {
        return new XSSFColor(new java.awt.Color(R, G, B));
    }

    @Override
    public void renderReport(Map<String, Object> dataMap)
    {
        // TODO Auto-generated method stub

    }
}
