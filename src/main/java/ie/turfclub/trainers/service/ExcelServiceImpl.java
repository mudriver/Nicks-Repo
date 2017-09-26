package ie.turfclub.trainers.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Service;

@Service
public class ExcelServiceImpl implements ExcelService {

	@Override
	public void buildExcelDocumentForMercerA(HSSFWorkbook workbook,
			List<HashMap<String, Object>> records) {
		
		HSSFSheet sheet = workbook.createSheet("MercerA");

		HSSFRow header = sheet.createRow(0);
		header.createCell(0).setCellValue("Card Number");
		header.createCell(1).setCellValue("Card Type");
		header.createCell(2).setCellValue("Surname");
		header.createCell(3).setCellValue("Firstname");
		header.createCell(4).setCellValue("Sex");
		header.createCell(5).setCellValue("DOB");
		header.createCell(6).setCellValue("Address1");
		header.createCell(7).setCellValue("Address2");
		header.createCell(8).setCellValue("Address3");
		header.createCell(9).setCellValue("Country");
		header.createCell(10).setCellValue("Current Trainer");
		header.createCell(11).setCellValue("Hours Worked");
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

		int rowNum = 1;
		for (HashMap<String, Object> record : records) {
			//create the row data
			HSSFRow row = sheet.createRow(rowNum++);
			Date dob = (Date) record.get("dateOfBirth");
			String formattedDate = "";
			if(dob != null) formattedDate = formatter.format(dob);
			row.createCell(0).setCellValue(record.get("eCardNumber") != null ? String.valueOf(record.get("eCardNumber")) : "");
			row.createCell(1).setCellValue(record.get("eCardType") != null ? String.valueOf(record.get("eCardType")) : "");
			row.createCell(2).setCellValue(record.get("surname") != null ? String.valueOf(record.get("surname")) : "");
			row.createCell(3).setCellValue(record.get("firstname") != null ? String.valueOf(record.get("firstname")) : "");
			row.createCell(4).setCellValue(record.get("sex") != null ? String.valueOf(record.get("sex")) : "");
			row.createCell(5).setCellValue(formattedDate);
			row.createCell(6).setCellValue(record.get("address1") != null ? String.valueOf(record.get("address1")) : "");
			row.createCell(7).setCellValue(record.get("address2") != null ? String.valueOf(record.get("address2")) : "");
			row.createCell(8).setCellValue(record.get("address3") != null ? String.valueOf(record.get("address3")) : "");
			row.createCell(9).setCellValue(record.get("country") != null ? String.valueOf(record.get("country")) : "");
			row.createCell(10).setCellValue(record.get("trainerName") != null ? String.valueOf(record.get("trainerName")) : "");
			row.createCell(11).setCellValue(record.get("hours") != null ? String.valueOf(record.get("hours")) : "");
        }
	}

	@Override
	public void buildExcelDocumentForMercerB(HSSFWorkbook workbook,
			List<HashMap<String, Object>> records) {
		
		HSSFSheet sheet = workbook.createSheet("MercerB");

		HSSFRow header = sheet.createRow(0);
		header.createCell(0).setCellValue("Card Number");
		header.createCell(1).setCellValue("Card Type");
		header.createCell(2).setCellValue("Surname");
		header.createCell(3).setCellValue("Firstname");
		header.createCell(4).setCellValue("Sex");
		header.createCell(5).setCellValue("DOB");
		header.createCell(6).setCellValue("Address1");
		header.createCell(7).setCellValue("Address2");
		header.createCell(8).setCellValue("Address3");
		header.createCell(9).setCellValue("Country");
		header.createCell(10).setCellValue("Current Trainer");
		header.createCell(11).setCellValue("Hours Worked");
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

		int rowNum = 1;
		for (HashMap<String, Object> record : records) {
			//create the row data
			HSSFRow row = sheet.createRow(rowNum++);
			Date dob = (Date) record.get("dateOfBirth");
			String formattedDate = "";
			if(dob != null) formattedDate = formatter.format(dob);
			row.createCell(0).setCellValue(record.get("eCardNumber") != null ? String.valueOf(record.get("eCardNumber")) : "");
			row.createCell(1).setCellValue(record.get("eCardType") != null ? String.valueOf(record.get("eCardType")) : "");
			row.createCell(2).setCellValue(record.get("surname") != null ? String.valueOf(record.get("surname")) : "");
			row.createCell(3).setCellValue(record.get("firstname") != null ? String.valueOf(record.get("firstname")) : "");
			row.createCell(4).setCellValue(record.get("sex") != null ? String.valueOf(record.get("sex")) : "");
			row.createCell(5).setCellValue(formattedDate);
			row.createCell(6).setCellValue(record.get("address1") != null ? String.valueOf(record.get("address1")) : "");
			row.createCell(7).setCellValue(record.get("address2") != null ? String.valueOf(record.get("address2")) : "");
			row.createCell(8).setCellValue(record.get("address3") != null ? String.valueOf(record.get("address3")) : "");
			row.createCell(9).setCellValue(record.get("country") != null ? String.valueOf(record.get("country")) : "");
			row.createCell(10).setCellValue(record.get("trainerName") != null ? String.valueOf(record.get("trainerName")) : "");
			row.createCell(11).setCellValue(record.get("hours") != null ? String.valueOf(record.get("hours")) : "");
        }
	}

}
