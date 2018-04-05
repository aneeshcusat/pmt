package com.famstack.projectscheduler.thread;

import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.famstack.projectscheduler.employees.bean.EmployeeDetails;
import com.famstack.projectscheduler.employees.bean.UserWorkDetails;
import com.famstack.projectscheduler.export.processors.FamstackXLSEmployeeUtilisationProcessor;

public class ExportEmpUtilWorkerThread implements Runnable
{

    XSSFWorkbook workBook;

    Sheet sheet;

    Map<String, Map<Integer, UserWorkDetails>> employeeUtilizationData;

    String dateString;

    FamstackXLSEmployeeUtilisationProcessor employeeUtilisationProcessor;

    List<EmployeeDetails> employees;

    public ExportEmpUtilWorkerThread(FamstackXLSEmployeeUtilisationProcessor employeeUtilisationProcessor,
        XSSFWorkbook workBook, Sheet sheet, Map<String, Map<Integer, UserWorkDetails>> employeeUtilizationData,
        String dateString, List<EmployeeDetails> employees)
    {
        this.workBook = workBook;
        this.sheet = sheet;
        this.employeeUtilizationData = employeeUtilizationData;
        this.dateString = dateString;
        this.employeeUtilisationProcessor = employeeUtilisationProcessor;
        this.employees = employees;
    }

    @Override
    public void run()
    {
        employeeUtilisationProcessor.renderReport(workBook, sheet, employeeUtilizationData, dateString, employees);
    }
}
