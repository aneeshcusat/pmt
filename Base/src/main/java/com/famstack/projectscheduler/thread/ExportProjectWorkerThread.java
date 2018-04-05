package com.famstack.projectscheduler.thread;

import java.util.List;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.famstack.projectscheduler.employees.bean.EmployeeDetails;
import com.famstack.projectscheduler.employees.bean.ProjectDetails;
import com.famstack.projectscheduler.export.processors.FamstackBaseXLSExportProcessor;

public class ExportProjectWorkerThread implements Runnable
{

    XSSFWorkbook workBook;

    Sheet sheet;

    String teamName;

    List<ProjectDetails> exportDataList;

    String dateString;

    FamstackBaseXLSExportProcessor baseXLSExportProcessor;

    List<EmployeeDetails> employees;

    public ExportProjectWorkerThread(FamstackBaseXLSExportProcessor baseXLSExportProcessor, XSSFWorkbook workBook,
        Sheet sheet, String teamName, List<ProjectDetails> exportDataList, String dateString,
        List<EmployeeDetails> employees)
    {
        this.workBook = workBook;
        this.sheet = sheet;
        this.teamName = teamName;
        this.exportDataList = exportDataList;
        this.dateString = dateString;
        this.baseXLSExportProcessor = baseXLSExportProcessor;
        this.employees = employees;
    }

    @Override
    public void run()
    {
        baseXLSExportProcessor.renderReport(workBook, sheet, teamName, exportDataList, dateString, employees);
    }
}
