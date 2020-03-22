package com.famstack.projectscheduler.manager;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.famstack.projectscheduler.util.DateUtils;

public class EmployeeDetailsImportProcessor {
	public static void main(String[] args) {
		try {
			try {
				generateSqlFromXls("C://temp/Employee_Master_31-Dec-2019.xlsx");
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

	    File fout = new File("C://temp/Employee_Master_31-Dec-2019.sql");
    	FileOutputStream fos = new FileOutputStream(fout);
    	BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
    	
    	 File fout1 = new File("C://temp/EmpNotAvailableInFamstack.sql");
     	FileOutputStream fos1 = new FileOutputStream(fout1);
     	BufferedWriter bw1 = new BufferedWriter(new OutputStreamWriter(fos1));
	    
    	List<String> existingEmps = getExistingEmpList("C://temp/existingemp.txt");
    	System.out.println("Total famstackemps " + existingEmps.size());
	    // Finds the workbook instance for XLSX file
	    XSSFWorkbook myWorkBook = new XSSFWorkbook (fis);
	   
	    // Return first sheet from the XLSX workbook
	    XSSFSheet mySheet = myWorkBook.getSheetAt(0);
	   
	    // Get iterator to all the rows in current sheet
	    Iterator<Row> rowIterator = mySheet.iterator();
	   
	    // Traversing over each row of XLSX file
	    int availableCount = 0;
	    int unAvailbleCount = 0;
	    while (rowIterator.hasNext()) {
	        Row row = rowIterator.next();
	        if(row.getRowNum() == 0) {
	        	continue;
	        }
	        String empCode = getCell(row,1);
	        String status = getCell(row,2);
	        String empName = getCell(row,3);
	        String gender = getCell(row,4);
	        String DOJ = getDate(row,5);
	        String DOB = getDate(row,6);
	        String location = getCell(row,7);
	        String country = getCell(row,8);
	        String division = getCell(row,9);
	        String department = getCell(row,10);
	        String subDepartment = getCell(row,11);
	        String band = getCell(row,12);
	        String grade = getCell(row,13);
	        String designation= getCell(row,14);
	        String empType = getCell(row,15);
	        String repEmailId = getCell(row,17);
	        String depEmailId = getCell(row,19);
	        String lobEmailId = getCell(row,21);
	        String userEmailId = getCell(row,22).trim();
	        String DOL = getDate(row,23);
	        
	        if("".equalsIgnoreCase(userEmailId)) {
	        	continue;
	        }
	        String DOBString = StringUtils.isBlank(DOB) ?"" :  "dob='"+DOB+"',";
	        
	        String DOJString = StringUtils.isBlank(DOJ) ?"" :  "date_of_join='"+DOJ+"',";
	        String DOLString = StringUtils.isBlank(DOL) ?"" :  "exit_date='"+DOL+"',";
	        
	        String updateItemSql = "update user_info set gender='"+gender.toLowerCase()+"',grade='"+grade+"',grade='"+grade+"',emp_type='"+empType+"',country='"+country+"',division='"+division+"',sub_department='"+subDepartment+"',band='"+band+"',emp_code='"+empCode+"', first_name='"+empName+"',last_name='', " + DOJString + DOLString + DOBString + "location='"+location+"',designation='"+designation+"', department='"+department+"', rept_mgr_email_id='"+repEmailId.toLowerCase()+"', dept_lead_email_id='"+depEmailId+"', lob_head_email_id='"+lobEmailId+"', user_id='"+userEmailId.toLowerCase()+"' where lower(user_id)='"+userEmailId.toLowerCase()+"';";

	        if(existingEmps.contains(userEmailId.toLowerCase())) {
	        	availableCount++;
	        	bw.write(availableCount +", "+ updateItemSql);
	        	bw.newLine();
	        	System.out.println(updateItemSql);
	        } else {
	        	unAvailbleCount++;
	        	bw1.write(unAvailbleCount +", "+ updateItemSql);
	        	bw1.newLine();
	        }
	    }
	    fos.close();
	    fos1.close();
	    System.out.println("Availble " +  availableCount);
	    System.out.println("unAvailbleCount " +  unAvailbleCount);
	}
	
	private static String getDate(Row row, int index) {
		try{
		Cell cell =  row.getCell(index);
		if (cell != null) {
			java.util.Date date = cell.getDateCellValue();
			if (date !=null) {
				return DateUtils.format(date, DateUtils.DATE_FORMAT_CALENDER);
			}
		}
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
		return null;
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
	
	public static List<String> getExistingEmpList(String fileName) {
		 List<String> existingEmp = new ArrayList<>();
		try {
	      File myObj = new File(fileName);
	      Scanner myReader = new Scanner(myObj);
	     
	      while (myReader.hasNextLine()) {
	        String data = myReader.nextLine();
	        existingEmp.add(data.trim().toLowerCase());
	      }
	      myReader.close();
	    } catch (FileNotFoundException e) {
	      System.out.println("An error occurred.");
	      e.printStackTrace();
	    }
	    return existingEmp;
	  }

}