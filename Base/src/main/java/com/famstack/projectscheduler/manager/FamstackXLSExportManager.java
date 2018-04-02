package com.famstack.projectscheduler.manager;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.famstack.projectscheduler.employees.bean.ProjectDetails;
import com.famstack.projectscheduler.employees.bean.UserWorkDetails;
import com.famstack.projectscheduler.export.processors.FamstackBaseXLSExportProcessor;
import com.famstack.projectscheduler.export.processors.FamstackXLSEmployeeUtilisationProcessor;

/**
 * The Class FamstackXLSExportManager.
 */
public class FamstackXLSExportManager extends BaseFamstackManager
{

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
    public void exportXLS(String processorName, String dateString, List<ProjectDetails> exportDataList,
        Map<String, Map<Integer, UserWorkDetails>> employeeUtilizationData, HttpServletRequest request,
        HttpServletResponse response)
    {
        String fullPath =
            request.getServletContext().getRealPath("/WEB-INF/classes/templates/" + processorName + ".xlsx");
        try {
            FileInputStream inputStream = new FileInputStream(new File(fullPath));
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
            Sheet sheet = workbook.getSheetAt(0);

            String teamName = "Project Report";
            try {
                teamName =
                    getFamstackApplicationConfiguration().getUserGroupMap()
                        .get(getFamstackApplicationConfiguration().getCurrentUserGroupId()).getName();
            } catch (Exception e) {

            }

            if (exportDataList != null) {
                FamstackBaseXLSExportProcessor baseXLSExportProcessor = exportProcessorMap.get(processorName);
                if (baseXLSExportProcessor != null) {
                    baseXLSExportProcessor.renderReport(workbook, sheet, teamName, exportDataList, dateString);
                } else {
                    logError("Unable to get the report template");
                }
            }

            if (!employeeUtilizationData.isEmpty()) {
                logDebug("Getting employee utilization export processor");
                FamstackXLSEmployeeUtilisationProcessor employeeUtilisationProcessor =
                    (FamstackXLSEmployeeUtilisationProcessor) exportProcessorMap.get("famstackEmpUtilisation");

                if (employeeUtilisationProcessor != null) {
                    sheet = workbook.getSheetAt(1);
                    logDebug("Processing employee utilization export report");
                    employeeUtilisationProcessor.renderReport(workbook, sheet, employeeUtilizationData, dateString);
                }
            }

            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment; filename=" + teamName + "_" + dateString + ".xlsx");

            inputStream.close();
            workbook.write(response.getOutputStream());
            workbook.close();
            response.getOutputStream().close();

        } catch (IOException | EncryptedDocumentException ex) {
            ex.printStackTrace();
        }
    }

    public Map<String, FamstackBaseXLSExportProcessor> getExportProcessorMap()
    {
        return exportProcessorMap;
    }

    public void setExportProcessorMap(Map<String, FamstackBaseXLSExportProcessor> exportProcessorMap)
    {
        this.exportProcessorMap = exportProcessorMap;
    }

}
