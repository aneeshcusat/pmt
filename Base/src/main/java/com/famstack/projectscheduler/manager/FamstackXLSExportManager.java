package com.famstack.projectscheduler.manager;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import com.famstack.projectscheduler.contants.ReportType;
import com.famstack.projectscheduler.employees.bean.EmployeeDetails;
import com.famstack.projectscheduler.employees.bean.UserWorkDetails;
import com.famstack.projectscheduler.export.processors.FamstackBaseXLSExportProcessor;
import com.famstack.projectscheduler.export.processors.FamstackXLSEmployeeUtilisationProcessor;
import com.famstack.projectscheduler.thread.ExportEmpUtilWorkerThread;
import com.famstack.projectscheduler.thread.ExportProjectWorkerThread;

/**
 * The Class FamstackXLSExportManager.
 */
public class FamstackXLSExportManager extends BaseFamstackManager {

  private Map<String, FamstackBaseXLSExportProcessor> exportProcessorMap;

  /**
   * Export XLS.
   * 
   * @param templateName the template name
   * @param outputStream the output stream
   * @param exportDataList the export data list
   * @param employeeUtilizationData
   * @param request the request
   */
  public void exportXLS(String processorName, Map<String, Object> dataMap,
      HttpServletRequest request, HttpServletResponse response) {
    String fullPath = request.getServletContext()
        .getRealPath("/WEB-INF/classes/templates/" + processorName + ".xlsx");
    try {
      FileInputStream inputStream = new FileInputStream(new File(fullPath));
      XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
      Sheet sheet = workbook.getSheetAt(0);

      String teamName = "Project Report";
      try {
        teamName = getFamstackApplicationConfiguration().getUserGroupMap()
            .get(getFamstackApplicationConfiguration().getCurrentUserGroupId()).getName();
      } catch (Exception e) {

      }
      List<EmployeeDetails> employees = getFamstackApplicationConfiguration().getUserList();
      ExecutorService executorService = Executors.newFixedThreadPool(2);
      List<Future> futures = Collections.synchronizedList(new ArrayList<Future>());
      Object exportDataList = dataMap.get("exportDataList");
      String dateString = (String) dataMap.get("dateString");
      Map<String, Map<Integer, UserWorkDetails>> employeeUtilizationData =
          (Map<String, Map<Integer, UserWorkDetails>>) dataMap.get("employeeUtilizationData");

      if ("useractivity".equalsIgnoreCase(processorName)) {
        Map<Integer, EmployeeDetails> allEmployeeData = null;

        if ("1012"
            .equalsIgnoreCase(getFamstackApplicationConfiguration().getCurrentUserGroupId())) {
          allEmployeeData = getFamstackApplicationConfiguration().getUserMap();
        } else {
          allEmployeeData = getFamstackApplicationConfiguration().getCurrentGroupUserMap();
        }
        dataMap.put("allEmployeeData", allEmployeeData);
        dataMap.put("userGroupMap", getFamstackApplicationConfiguration().getUserGroupMap());
        teamName = "EmployeeSiteActivity";
      }

      if (exportDataList != null) {

        FamstackBaseXLSExportProcessor baseXLSExportProcessor =
            exportProcessorMap.get(processorName);
        if (baseXLSExportProcessor != null) {

          dataMap.put("workBook", workbook);
          dataMap.put("sheet", sheet);
          dataMap.put("teamName", teamName);
          dataMap.put("employees", employees);

          ExportProjectWorkerThread projectWorkerThred =
              new ExportProjectWorkerThread(dataMap, baseXLSExportProcessor);
          // workbook.setSheetName(0, dateString);
          Future future = executorService.submit(projectWorkerThred);
          futures.add(future);

          // baseXLSExportProcessor.renderReport(workbook, sheet, teamName, exportDataList,
          // dateString,
          // employees);
        } else {
          logError("Unable to get the report template");
        }

      }

      if (employeeUtilizationData != null && !employeeUtilizationData.isEmpty()
          && !"default".equalsIgnoreCase(processorName)) {
        logDebug("Getting employee utilization export processor");
        FamstackXLSEmployeeUtilisationProcessor employeeUtilisationProcessor =
            (FamstackXLSEmployeeUtilisationProcessor) exportProcessorMap
                .get("famstackEmpUtilisation");

        if (employeeUtilisationProcessor != null) {
          Sheet employeeSheet = workbook.getSheetAt(1);
          logDebug("Processing employee utilization export report");

          ExportEmpUtilWorkerThread empUtilsWorkerThred =
              new ExportEmpUtilWorkerThread(employeeUtilisationProcessor, workbook, employeeSheet,
                  employeeUtilizationData, dateString, employees);
          Future future = executorService.submit(empUtilsWorkerThred);
          futures.add(future);
          // employeeUtilisationProcessor.renderReport(workbook, employeeSheet,
          // employeeUtilizationData,
          // dateString);
        }
      }
      logDebug("Starting reporting threads");
      for (Future future : futures) {
        try {
          future.get();
        } catch (InterruptedException | ExecutionException e) {
          e.printStackTrace();
          logError("Unable to generate report", e);
        }
      }

      logDebug("Completing reporting threads");
      executorService.shutdown();
      response.setContentType("application/vnd.ms-excel");
      response.setHeader("Content-Disposition",
          "attachment; filename=" + teamName + "_" + dateString + ".xlsx");

      inputStream.close();
      workbook.write(response.getOutputStream());
      workbook.close();
      response.getOutputStream().close();

    } catch (IOException | EncryptedDocumentException ex) {
      ex.printStackTrace();
    }
  }

  public void downloadXLSReport(ReportType reportType, String fileName, Map<String, Object> dataMap,
      HttpServletRequest request, HttpServletResponse response) {
    try {

      FamstackBaseXLSExportProcessor baseXLSExportProcessor =
          exportProcessorMap.get("reportProcessor");
      if (baseXLSExportProcessor != null && dataMap != null && !dataMap.isEmpty()) {
        XSSFWorkbook workbook = new XSSFWorkbook();
        String dateString = (String) dataMap.get("REPORT_DATE");
        Sheet sheet = workbook.createSheet(dateString.replace("/", "-"));

        dataMap.put("workbook", workbook);
        dataMap.put("sheet", sheet);
        dataMap.put("reportType", reportType);
        baseXLSExportProcessor.renderReport(dataMap);
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition",
            "attachment; filename=" + fileName + "_" + dateString + ".xlsx");
        workbook.write(response.getOutputStream());
        workbook.close();
        response.getOutputStream().close();
      }
    } catch (Exception e) {
      logError(e.getMessage(), e);
    }
  }

  public void downloadProjectsBySkills(Map<String, Object> dataMap, String fileName,
      HttpServletRequest request, HttpServletResponse response) {

    String fullPath = request.getServletContext()
        .getRealPath("/WEB-INF/classes/templates/projectDetailsBySkills.xlsx");
    try {
      FamstackBaseXLSExportProcessor baseXLSExportProcessor =
          exportProcessorMap.get("projectDetailsBySkillsProcessor");

      FileInputStream inputStream = new FileInputStream(new File(fullPath));
      XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
      Sheet sheet = workbook.getSheetAt(1);

      dataMap.put("workbook", workbook);
      dataMap.put("sheet", sheet);

      baseXLSExportProcessor.renderReport(dataMap);
      response.setContentType("application/vnd.ms-excel");
      response.setHeader("Content-Disposition", "attachment; filename=" + fileName + ".xlsx");
      workbook.write(response.getOutputStream());
      workbook.close();
      response.getOutputStream().close();
    } catch (Exception e) {
      logError(e.getMessage(), e);
    }
  }

  public Map<String, FamstackBaseXLSExportProcessor> getExportProcessorMap() {
    return exportProcessorMap;
  }

  public void setExportProcessorMap(
      Map<String, FamstackBaseXLSExportProcessor> exportProcessorMap) {
    this.exportProcessorMap = exportProcessorMap;
  }

}
