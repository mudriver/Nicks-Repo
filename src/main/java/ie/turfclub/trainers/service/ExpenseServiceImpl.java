package ie.turfclub.trainers.service;

import java.io.File;
import java.io.FileInputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.DateUtils;

@Service
@Transactional
public class ExpenseServiceImpl implements ExpenseService {

	@Autowired
	private ResourceLoader resourceLoader;
	
	List<HashMap<String, Object>> records = null;
	
	/*@Override
	public HashMap<String, Object> getDataFromExcel() {
	
		try {
			HashMap<String, Object> result = new HashMap<String, Object>();
			Resource resource = new ClassPathResource("SampleRoster.xlsx");
			File file = new File(resource.getURI());
			FileInputStream input = new FileInputStream(file);
			
			// Finds the workbook instance for XLSX file 
			XSSFWorkbook myWorkBook = new XSSFWorkbook (input); 
			
			// Return first sheet from the XLSX workbook 
			XSSFSheet mySheet = myWorkBook.getSheetAt(0); 
			
			// Get iterator to all the rows in current sheet 
			Iterator<Row> rowIterator = mySheet.iterator(); 
			
			rowIterator.next();
			rowIterator.next();
			rowIterator.next();
			rowIterator.next();
			rowIterator.next();
			records = new ArrayList<HashMap<String,Object>>();
			
			int id = 1;
			// Traversing over each row of XLSX file 
			while (rowIterator.hasNext()) { 
				
				Row row = rowIterator.next(); 
				
				// For each row, iterate through each columns 
				Iterator<Cell> cellIterator = row.cellIterator(); 
				HashMap<String, Object> record = new HashMap<String, Object>();
				record.put("id", id);
				int count = 0;
				boolean isBreak = false;
				while (cellIterator.hasNext()) {
					
					Cell cell = cellIterator.next(); 
					Object colValue = getStringCellValue(cell);
					if(count == 0 && (colValue == null || (colValue != null && ((String)colValue).length() <= 0))) {
						isBreak = true;
						break;
					}
					switch(count) {
					case 0:
						record.put("date", colValue);
						record.put("orgDate", cell.getDateCellValue());
						break;
					case 1:
						record.put("day", colValue);
						break;
					case 2:
						record.put("evening", colValue);
						break;
					case 3:
						record.put("venue", colValue);
						break;
					case 4:
						record.put("dutyCode", colValue);
						break;
					case 5:
						record.put("dutyDescription", colValue);
						break;
					case 6:
						record.put("costCentre", colValue);
						break;
					case 7:
						record.put("dutyType", colValue);
						break;
					case 8:
						record.put("noOfRaces", colValue);
						break;
					case 9:
						record.put("subsistanceTypeExpected", colValue);
						break;
					case 10:
						record.put("subsistanceTypeChange", colValue);
						break;
					case 11:
						record.put("subsistanceType", colValue);
						break;
					case 12:
						record.put("subsistanceReasonForChange", colValue);
						break;
					case 13:
						record.put("expectedMileage", colValue);
						break;
					case 14:
						record.put("additionalMileage", colValue);
						break;
					case 15:
						record.put("mileageType", colValue);
						break;
					case 16:
						record.put("mileageReasonForChange", colValue);
						break;
					}
					++count;
				} 
				if(!isBreak) {
					records.add(record);
					++id;
				}
			}

			result.put("data", records);
			return result;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	@Override
	public List<HashMap<String, Object>> getXLSXData() {
		
		return records;
	}
	
	private Object getStringCellValue(Cell cell) {
		
		String cellValue = null;
		switch(cell.getCellType()) {
		case Cell.CELL_TYPE_STRING:
		    cellValue = cell.getStringCellValue();
		    break;

		case Cell.CELL_TYPE_FORMULA:
		    cellValue = cell.getCellFormula();
		    break;

		case Cell.CELL_TYPE_NUMERIC:
		    if (DateUtil.isCellDateFormatted(cell)) {
		        cellValue = cell.getDateCellValue().toString();
		        cellValue = convertDateToString(cellValue);
		    } else {
		        cellValue = Double.toString(cell.getNumericCellValue());
		    }
		    break;

		case Cell.CELL_TYPE_BLANK:
		    cellValue = "";
		    break;

		case Cell.CELL_TYPE_BOOLEAN:
		    cellValue = Boolean.toString(cell.getBooleanCellValue());
		    break;
		}
		return cellValue;
	}
	
	private String convertDateToString(String dateStr) {
		DateFormat formatter = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy");
		Date date;
		try {
			date = (Date)formatter.parse(dateStr);
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			
			String month = ""+(cal.get(Calendar.MONTH) + 1);
			String day = ""+(cal.get(Calendar.DATE));
			if((cal.get(Calendar.MONTH) + 1) < 10)
				month = "0"+(cal.get(Calendar.MONTH) + 1);
			if(cal.get(Calendar.DATE) < 10)
				day = "0"+cal.get(Calendar.DATE);
			
			String formatedDate = day + "/" + month + "/" +         cal.get(Calendar.YEAR);
			return formatedDate;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	@Override
	public HashMap<String, Object> getSearchableData(String month, String year) {
		
		DateTime datetime = new DateTime(Integer.parseInt(year), Integer.parseInt(month), 1, 0, 0);
		final Date startDate = datetime.dayOfMonth().withMinimumValue().toDate();
		final Date endDate = datetime.dayOfMonth().withMaximumValue().toDate();
		
		HashMap<String, Object> data = new HashMap<String, Object>();
		List<HashMap<String, Object>> results = new ArrayList<HashMap<String,Object>>();
		if(records != null) {
			for (HashMap<String, Object> map : records) {
				
				Date date = (Date) map.get("orgDate");
				if(startDate.compareTo(date) * date.compareTo(endDate) > 0)
					results.add(map);
			}
		}
		data.put("data", results);
		return data;
	}*/
}
