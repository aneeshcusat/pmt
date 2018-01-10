package com.famstack.projectscheduler.export.processors;

import java.util.List;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.famstack.projectscheduler.employees.bean.ProjectDetails;

public interface FamstackBaseXLSExportProcessor
{

    public void renderReport(XSSFWorkbook workbook, Sheet sheet, int rowCount, List<ProjectDetails> exportDataList,
        String dateString);

}
