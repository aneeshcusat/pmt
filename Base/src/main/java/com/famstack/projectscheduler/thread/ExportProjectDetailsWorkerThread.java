package com.famstack.projectscheduler.thread;

import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.famstack.projectscheduler.employees.bean.EmployeeDetails;
import com.famstack.projectscheduler.employees.bean.ProjectDetails;
import com.famstack.projectscheduler.export.processors.FamstackXLSExportProcessor11;

public class ExportProjectDetailsWorkerThread implements Runnable
{

    XSSFWorkbook workBook;

    Sheet sheet;

    ProjectDetails projectDetails;

    int rowIndex;

    FamstackXLSExportProcessor11 famstackXLSExportProcessor;

    CellStyle xssfCellTextWrapStyle;

    List<EmployeeDetails> employees;

    Map<Integer, Integer> userProjectTotalHoursMap;

    public ExportProjectDetailsWorkerThread(FamstackXLSExportProcessor11 famstackXLSExportProcessor,
        XSSFWorkbook workBook, Sheet sheet, int rowIndex, ProjectDetails projectDetails,
        Map<Integer, Integer> userProjectTotalHoursMap, CellStyle xssfCellTextWrapStyle, List<EmployeeDetails> employees)
    {
        this.workBook = workBook;
        this.sheet = sheet;
        this.projectDetails = projectDetails;
        this.rowIndex = rowIndex;
        this.famstackXLSExportProcessor = famstackXLSExportProcessor;
        this.userProjectTotalHoursMap = userProjectTotalHoursMap;
        this.xssfCellTextWrapStyle = xssfCellTextWrapStyle;
        this.employees = employees;
    }

    @Override
    public void run()
    {
        famstackXLSExportProcessor.createProjectDetailsRow(workBook, sheet, rowIndex, projectDetails,
            userProjectTotalHoursMap, employees, xssfCellTextWrapStyle);
    }

}
