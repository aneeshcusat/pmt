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
import com.famstack.projectscheduler.export.processors.FamstackBaseXLSExportProcessor;

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
     * @param request the request
     */
    public void exportXLS(String processorName, String dateString, List<ProjectDetails> exportDataList,
        HttpServletRequest request, HttpServletResponse response)
    {
        String fullPath =
            request.getServletContext().getRealPath("/WEB-INF/classes/templates/" + processorName + ".xlsx");
        try {
            FileInputStream inputStream = new FileInputStream(new File(fullPath));

            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);

            Sheet sheet = workbook.getSheetAt(0);

            int rowCount = sheet.getLastRowNum();
            if (exportDataList != null) {
                FamstackBaseXLSExportProcessor baseXLSExportProcessor = exportProcessorMap.get(processorName);
                if (baseXLSExportProcessor != null) {
                    baseXLSExportProcessor.renderReport(workbook, sheet, rowCount, exportDataList, dateString);
                } else {
                    logError("Unable to get the report template");
                }
            }

            String mimeType = request.getServletContext().getMimeType(fullPath);
            if (mimeType == null) {
                mimeType = "application/octet-stream";
            }

            response.setContentType(mimeType);
            response.setContentLength((int) new File(fullPath).length());
            String headerKey = "Content-Disposition";
            String headerValue =
                String.format("attachment; filename=\"%s\"", processorName + "_" + dateString + ".xlsx");
            response.setHeader(headerKey, headerValue);
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
