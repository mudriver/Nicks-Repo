package ie.turfclub.trainers.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.view.document.AbstractExcelView;

@Service
public class MercerAExcelView extends AbstractExcelView {

	@Autowired
	private TrainersService trainerService;
	
	@Autowired
	private ExcelService excelService;
	
	@Override
	protected void buildExcelDocument(Map<String, Object> model,
			HSSFWorkbook workbook, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		List<HashMap<String, Object>> records = trainerService.getMercerARecord();
		excelService.buildExcelDocumentForMercerA(workbook, records);
		response.setHeader("Content-Disposition", "attachment;filename=\"MercerA.xls\"");
	}

}
