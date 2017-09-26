package ie.turfclub.trainers.service;

import java.util.HashMap;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public interface ExcelService {

	void buildExcelDocumentForMercerA(HSSFWorkbook workbook,
			List<HashMap<String, Object>> records);

	void buildExcelDocumentForMercerB(HSSFWorkbook workbook,
			List<HashMap<String, Object>> records);

}
