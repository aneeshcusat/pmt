package com.famstack.projectscheduler.export.processors;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import com.famstack.projectscheduler.BaseFamstackService;
import com.famstack.projectscheduler.contants.SkillsUtils;
import com.famstack.projectscheduler.dashboard.bean.ProjectDetailsBySkillsResponse;
import com.famstack.projectscheduler.dashboard.bean.ProjectDetailsBySkillsResponse.Resources;
import com.famstack.projectscheduler.dashboard.bean.ProjectDetailsBySkillsResponse.SkillSetResponse;
import com.famstack.projectscheduler.util.StringUtils;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class FamstackProjectDetailsBySkillsProcessor extends BaseFamstackService
    implements FamstackBaseXLSExportProcessor {
  private XSSFColor colorIndexGreen = null;
  private XSSFCellStyle boldCenterValueCellStyle = null;
  private XSSFCellStyle headerValueCellStyle = null;
  private XSSFCellStyle valueCellStyle = null;
  private XSSFCellStyle valueLeftCellStyle = null;

  @Override
  public void renderReport(Map<String, Object> dataMap) {
    XSSFSheet sheet = (XSSFSheet) dataMap.get("sheet");
    XSSFWorkbook workBook = (XSSFWorkbook) dataMap.get("workbook");
    if (sheet != null) {
      boldCenterValueCellStyle = initializeCellStyle(workBook, null, true, boldCenterValueCellStyle,
          HorizontalAlignment.CENTER, true);
      
      headerValueCellStyle = initializeCellStyle(workBook, getColor(0, 176, 80, colorIndexGreen), true, headerValueCellStyle,
              HorizontalAlignment.LEFT, false);
      valueCellStyle = initializeCellStyle(workBook, null, true, valueCellStyle,
              HorizontalAlignment.CENTER, false);
      valueLeftCellStyle = initializeCellStyle(workBook, null, true, valueLeftCellStyle,
              HorizontalAlignment.LEFT, false);
      fillProjectDetailsBySkillReportData(dataMap, sheet, workBook);
    }
  }

  private void setCellValue(Sheet sheet, int rowIndex, int colIndex, String value,
      CellStyle cellStyle) {
    Row row = getRow(sheet, rowIndex);
    Cell cell = getCell(sheet, row, colIndex);
    cell.setCellValue(sanitizeCellValue(value));
    if (cellStyle != null) {
      cell.setCellStyle(cellStyle);
    } else {
      cell.setCellStyle(boldCenterValueCellStyle);
    }
    if (rowIndex < 2) {
      sheet.autoSizeColumn(colIndex);
    }
  }

  private Cell getCell(Sheet sheet, Row row, int cellNumber) {
    Cell cell = row.getCell(cellNumber);
    if (cell == null) {
      cell = row.createCell(cellNumber);
    }
    return cell;
  }

  private Row getRow(Sheet sheet, int rowIndex) {
    Row projectDetailsRow = sheet.getRow(rowIndex);
    if (projectDetailsRow == null) {
      projectDetailsRow = sheet.createRow(rowIndex);
    }
    return projectDetailsRow;
  }

  private void fillProjectDetailsBySkillReportData(Map<String, Object> dataMap, Sheet sheet,
      XSSFWorkbook workBook) {

    List<ProjectDetailsBySkillsResponse> projectDetailsBySkillsList =
        (List<ProjectDetailsBySkillsResponse>) dataMap.get("DATA");

    int rowIndex = 2;
    int estSkillHoursCellIndex = 29;
	int actualSkillHoursCellIndex = 72;
	int resouceSkillHoursCellIndex = 105;
    if (projectDetailsBySkillsList != null) {
    	List<String> allSkills = new ArrayList<>(SkillsUtils.getUserSkillList());
	    for (ProjectDetailsBySkillsResponse projectDetailsBySkill : projectDetailsBySkillsList) {
	      setCellValue(sheet, rowIndex, 2, projectDetailsBySkill.getAccount(), null);
	      setCellValue(sheet, rowIndex, 3, projectDetailsBySkill.getTeam(), null);
	      setCellValue(sheet, rowIndex, 4, projectDetailsBySkill.getClientPartner(), null);
	      setCellValue(sheet, rowIndex, 5, projectDetailsBySkill.getMonthYear(), null);
	      setCellValue(sheet, rowIndex, 6, projectDetailsBySkill.getPoNumber(), null);
	      setCellValue(sheet, rowIndex, 7, projectDetailsBySkill.getProposalNumber(), null);
	      setCellValue(sheet, rowIndex, 8, projectDetailsBySkill.getOrderBookId(), null);
	      setCellValue(sheet, rowIndex, 9, projectDetailsBySkill.getProjectId(), null);
	      setCellValue(sheet, rowIndex, 10, projectDetailsBySkill.getProjectName(), null);
	      setCellValue(sheet, rowIndex, 11, projectDetailsBySkill.getLocation(), null);
	      setCellValue(sheet, rowIndex, 12, projectDetailsBySkill.getClientName(), null);
	      
	      Map<String, SkillSetResponse> skills = projectDetailsBySkill.getSkills();
	      
	      if (skills != null) {
	    	 
	    	  
	    	  Double totalOtherHours = 0d;
	    	  int estOtherHours = 0;
	    	  
	    	  for (String skill : skills.keySet()) {
	    		  int skillIndex =  SkillsUtils.getUserSkillList().indexOf(skill);
	    		  if (skillIndex < 0) {
	    			  skillIndex = SkillsUtils.getUserSkillList().size();
	    			  totalOtherHours+=skills.get(skill).getTotalHours();
	    			  estOtherHours+= skills.get(skill).getEstimatedHours();
	    		  } else {
	    			  if( skills.get(skill).getEstimatedHours() > 0) {
		    			  setCellValue(sheet, rowIndex, estSkillHoursCellIndex + skillIndex, "" + skills.get(skill).getEstimatedHours(), valueCellStyle);
		    		  }
		    		  if( skills.get(skill).getTotalHours() > 0) {
		    			  setCellValue(sheet, rowIndex, actualSkillHoursCellIndex + skillIndex, "" + skills.get(skill).getTotalHours(), valueCellStyle);
		    		  }
	    		  }
	    		  
	    		  if (allSkills.indexOf(skill) == -1) {
	    			  setCellValue(sheet, 1, resouceSkillHoursCellIndex + allSkills.size(), StringUtils.isNotBlank(skill) ? skill : "Others", headerValueCellStyle);
	    			  allSkills.add(skill);
	    		  }
	    			  if (skills.get(skill).getResources() != null) {
	    			  String resourceHours = "";
	    			  for (Resources resource : skills.get(skill).getResources()) {
	    				  if (resource.getHoursSpent() > 0) {
	    					  resourceHours+= resource.getName() + " (" +resource.getHoursSpent()+")\n";
	    				  }
	    			  }
	    			  setCellValue(sheet, rowIndex, resouceSkillHoursCellIndex + allSkills.indexOf(skill), resourceHours, valueLeftCellStyle);
	    		  }
    		  
	    	  }
	    	  if(totalOtherHours > 0) {
	    		  setCellValue(sheet, rowIndex, actualSkillHoursCellIndex + SkillsUtils.getUserSkillList().size(), "" + totalOtherHours, valueCellStyle);
	    	  }
	    	  if (estOtherHours > 0) {
	    		  setCellValue(sheet, rowIndex, estSkillHoursCellIndex + SkillsUtils.getUserSkillList().size(), "" + estOtherHours, valueCellStyle);
	    	  }
	      }
	      
	      rowIndex++;
	    }
	    
	    for (String allSkill : allSkills) {
	    	sheet.autoSizeColumn(resouceSkillHoursCellIndex + allSkills.indexOf(allSkill)); 
	    }
    }
  }


  private XSSFCellStyle initializeCellStyle(XSSFWorkbook workbook, XSSFColor colorIndex,
      boolean isBold, XSSFCellStyle cellStyle, HorizontalAlignment horizontalAlignment, boolean border) {
    if (cellStyle == null) {
      cellStyle = workbook.createCellStyle();
      if (colorIndex != null) {
        cellStyle.setFillForegroundColor(colorIndex);
        cellStyle.setFillBackgroundColor(colorIndex);
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
      }
      Font font = workbook.createFont();
      font.setBold(isBold);
      if (horizontalAlignment != null) {
        cellStyle.setAlignment(horizontalAlignment);
      }
      cellStyle.setFont(font);
      if (border) {
	      cellStyle.setBorderBottom(BorderStyle.THIN);
	      cellStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
	
	      cellStyle.setBorderRight(BorderStyle.THIN);
	      cellStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());
	
	      cellStyle.setBorderLeft(BorderStyle.THIN);
	      cellStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());
	
	      cellStyle.setBorderTop(BorderStyle.THIN);
	      cellStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());
      }
      cellStyle.setWrapText(true);
    }
    return cellStyle;
  }
  private XSSFColor getColor(int R, int G, int B, XSSFColor color) {
	    if (color == null) {
	      color = new XSSFColor(new java.awt.Color(R, G, B));
	    }
	    return color;
	  }
}
