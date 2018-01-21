/**
 * 
 */
package com.excel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.vo.EmailServiceVO;




/**
 * @author User
 *
 */
public class ExcelHelper {


	public List<EmailServiceVO> parseFile(HttpServletRequest request){
		InputStream inputStream = null;
		List<EmailServiceVO> emailServiceList = null;
		try {
			List<FileItem> items = new ServletFileUpload(new DiskFileItemFactory()).parseRequest(request);            
			for (FileItem item : items) {
				if (item.isFormField()) {
					inputStream = item.getInputStream();
					String fileName = new File(item.getName()).getName(); 
					if(fileName.split("\\.")[1].trim().equals("xlsx")) {
						emailServiceList = readXLSXFile(inputStream);
					}else {
						emailServiceList = readXLSFile(inputStream);
					}
				} else {
					String fileName = item.getName();
					inputStream = item.getInputStream();
					if(fileName.split("\\.")[1].trim().equals("xlsx")) {
						emailServiceList = readXLSXFile(inputStream);
					}else {
						emailServiceList = readXLSFile(inputStream);
					}
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return emailServiceList;
	}

	public void writeToExcelFile(Map<String, List<String>> validEmails){
		SimpleDateFormat sdf = new SimpleDateFormat("ddMMMyyyy_HHmmss");
		String excelFile = "C:\\temp\\"+sdf.format(new Date())+"EmailsData.xls";
		// Blank workbook
		HSSFWorkbook workbook = new HSSFWorkbook();

		// Create a blank sheet
		HSSFSheet sheet = null;
		CellStyle style = workbook.createCellStyle();
		Set<String> keyset = validEmails.keySet();
		for(String key : keyset) {
			if(validEmails.get(key) != null && validEmails.get(key).size() > 0)
				if(key.contains("invalid")) {
					sheet = workbook.createSheet("Invalid Emails");
					writeDataToSheet(validEmails.get(key),sheet,style);
				}else {
					sheet = workbook.createSheet("Valid Emails");
					writeDataToSheet(validEmails.get(key),sheet,style);
				}
		}

		try {
			// this Writes the workbook gfgcontribute
			FileOutputStream out = new FileOutputStream(new File(excelFile));
			workbook.write(out);
			out.close();
			System.out.println("file written successfully on disk.");
		}
		catch (Exception e) {
			e.printStackTrace();
		}

	}


	public void writeDataToSheet(List<String> emailsList, HSSFSheet sheet,CellStyle style) {
		int rownum = 0;
		// this creates a new row in the sheet
		int cellnum = 0;
		Row row = sheet.createRow(rownum++);
		Cell cell = row.createCell(cellnum);
		cell.setCellValue("Email");
		for(String email: emailsList) {
			row = sheet.createRow(rownum++);
			cell = row.createCell(cellnum);
			cell.setCellValue(email);
		}
		sheet.autoSizeColumn(0);
	}



	/****
	 * reads XLS file format 97-2003 
	 */
	public static List<EmailServiceVO> readXLSFile(InputStream ExcelFileToRead) throws IOException
	{
		//InputStream ExcelFileToRead = new FileInputStream("C:/temp/input.xls");
		HSSFWorkbook wb = new HSSFWorkbook(ExcelFileToRead);

		HSSFSheet sheet=wb.getSheetAt(0);
		HSSFRow row; 
		HSSFCell cell;
		List<EmailServiceVO> emailServiceList = new LinkedList<EmailServiceVO>();
		EmailServiceVO emailServiceVO = null;
		Iterator rows = sheet.rowIterator();

		while (rows.hasNext())
		{
			row=(HSSFRow) rows.next();
			Iterator cells = row.cellIterator();

			while (cells.hasNext())
			{
				cell=(HSSFCell) cells.next();

				if(null != cell && !cell.getStringCellValue().equals("")) {
					if(cell.getStringCellValue().equals("Email"))
						break;
					else {
						emailServiceVO = new EmailServiceVO();
						emailServiceVO.setTo(cell.getStringCellValue());
						if(cells.hasNext()) {
							cell = (HSSFCell) cells.next();
							emailServiceVO.setFirstName(null != cell && null != cell.getStringCellValue() && !cell.getStringCellValue().equals("")?cell.getStringCellValue():"");
						}
						emailServiceList.add(emailServiceVO);
						break;
					}
				}else {
					break;
				}
			}
		}
		return emailServiceList;

	}

	/****
	 * reads XLS file format 97-2003 
	 */
	public static List<EmailServiceVO> readXLSXFile(InputStream ExcelFileToRead) throws IOException
	{
		//InputStream ExcelFileToRead = new FileInputStream("C:/temp/input.xlsx");
		List<EmailServiceVO> emailServiceList = new LinkedList<EmailServiceVO>();
		EmailServiceVO emailServiceVO = null;
		XSSFWorkbook  wb = new XSSFWorkbook(ExcelFileToRead);

		XSSFWorkbook test = new XSSFWorkbook(); 

		XSSFSheet sheet = wb.getSheetAt(0);
		XSSFRow row; 
		XSSFCell cell;

		Iterator rows = sheet.rowIterator();

		while (rows.hasNext())
		{
			row=(XSSFRow) rows.next();
			Iterator cells = row.cellIterator();
			while (cells.hasNext())
			{
				cell=(XSSFCell) cells.next();

				if(null != cell && !cell.getStringCellValue().equals("")) {
					if(cell.getStringCellValue().equals("Email"))
						break;
					else {
						emailServiceVO = new EmailServiceVO();
						emailServiceVO.setTo(cell.getStringCellValue());
						if(cells.hasNext()) {
							cell = (XSSFCell) cells.next();
							emailServiceVO.setFirstName(null != cell && null != cell.getStringCellValue() && !cell.getStringCellValue().equals("")?cell.getStringCellValue():"");
						}
						emailServiceList.add(emailServiceVO);
						break;
					}
				}else {
					break;
				}
			}
		}
		return emailServiceList;

	}


	public static void writeXLSFile() throws IOException {

		String excelFileName = "C:/temp/output.xls";//name of excel file

		String sheetName = "Sheet1";//name of sheet

		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet(sheetName) ;

		//iterating r number of rows
		for (int r=0;r < 5; r++ )
		{
			HSSFRow row = sheet.createRow(r);

			//iterating c number of columns
			for (int c=0;c < 5; c++ )
			{
				HSSFCell cell = row.createCell(c);

				cell.setCellValue("Cell "+r+" "+c);
			}
		}

		FileOutputStream fileOut = new FileOutputStream(excelFileName);

		//write this workbook to an Outputstream.
		wb.write(fileOut);
		fileOut.flush();
		fileOut.close();
	}

	public static void writeXLSXFile() throws IOException {

		String excelFileName = "C:/temp/output.xlsx";//name of excel file

		String sheetName = "Sheet1";//name of sheet

		XSSFWorkbook wb = new XSSFWorkbook();
		XSSFSheet sheet = wb.createSheet(sheetName) ;

		//iterating r number of rows
		for (int r=0;r < 5; r++ )
		{
			XSSFRow row = sheet.createRow(r);

			//iterating c number of columns
			for (int c=0;c < 5; c++ )
			{
				XSSFCell cell = row.createCell(c);

				cell.setCellValue("Cell "+r+" "+c);
			}
		}

		FileOutputStream fileOut = new FileOutputStream(excelFileName);

		//write this workbook to an Outputstream.
		wb.write(fileOut);
		fileOut.flush();
		fileOut.close();
	}

	public static void main(String[] args) throws IOException {

		/*readXLSFile();
		writeXLSFile();	*/	

		/*readXLSXFile();
		writeXLSXFile();*/

	}


}
