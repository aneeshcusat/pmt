package com.famstack.projectscheduler.manager;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class EmployeeDetailsImportProcessor {
	public static void main(String[] args) {
		try {
			try {
				generateSqlFromXls("C://temp/1_MAA.xlsx");
				//generateSqlFromXls("C://temp/2_DAA.xlsx");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private static void generateSqlFromXls(String fileName) throws IOException {
		
		File myFile = new File(fileName);
	    FileInputStream fis = new FileInputStream(myFile);
	
	    // Finds the workbook instance for XLSX file
	    XSSFWorkbook myWorkBook = new XSSFWorkbook (fis);
	   
	    // Return first sheet from the XLSX workbook
	    XSSFSheet mySheet = myWorkBook.getSheetAt(0);
	   
	    // Get iterator to all the rows in current sheet
	    Iterator<Row> rowIterator = mySheet.iterator();
	   
	    // Traversing over each row of XLSX file
	    while (rowIterator.hasNext()) {
	        Row row = rowIterator.next();
	        if(row.getRowNum() == 0) {
	        	continue;
	        }
	        String empCode = getCell(row,0);
	        String empName = getCell(row,1);
	        String DOJ = getCell(row,2);
	        String DOL = getCell(row,3);
	        String status = getCell(row,4);
	        String location = getCell(row,5);
	        String level = getCell(row,6);
	        String empDesignation = getCell(row,7);
	        String department = getCell(row,8);
	        String userEmailId = getCell(row,9);
	        String reportingManagerName = getCell(row,10);
	        String reportingManagerEmaild = getCell(row,11);
	        String functionalHead = getCell(row,12);
	        String businessHead = getCell(row,13);
	        String empType = getCell(row,14);
	        String LWD = getCell(row,15);
	        
	        if("".equalsIgnoreCase(userEmailId)) {
	        	continue;
	        }
	        
	        String DOJString = StringUtils.isBlank(DOJ) ?"" :  "date_of_join='"+DOJ+"',";
	        String DOLString = StringUtils.isBlank(DOL) ?"" :  "date_of_leave='"+DOL+"',";
	        String LWDString = StringUtils.isBlank(LWD) ?"" :  "last_work_day='"+LWD+"'";
	        
	        System.out.println("update user_info set emp_code='"+empCode+"', first_name='"+empName+"',last_name='', " + DOJString + DOLString + "location='"+location+"',level='"+level+"',designation='"+empDesignation+"', department='"+department+"', user_id='"+userEmailId.toLowerCase()+"', rept_mgr_email_id='"+reportingManagerEmaild.toLowerCase()+"', functional_lead='"+functionalHead+"', business_head='"+businessHead+"',is_temp=false, "+LWDString+" where lower(user_id)='"+userEmailId.toLowerCase()+"';");
	            
	    }
	}

	private static String getCell(Row row, int index) {
		Cell cell =  row.getCell(index);
		String returnValue = "";
		if (cell != null) {
			if (cell.getCellTypeEnum() == CellType.STRING) {
				returnValue =  cell.getStringCellValue();
			} else if (cell.getCellTypeEnum() == CellType.NUMERIC) {
				cell.setCellType(CellType.STRING);
				returnValue = "" + cell.getStringCellValue();
			} else {
				cell.setCellType(CellType.STRING);
				returnValue = cell.getStringCellValue();
			}
		}
		return "-".equalsIgnoreCase(returnValue) || "-".equalsIgnoreCase("")? "" : returnValue;
	}

}