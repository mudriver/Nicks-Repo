package ie.turfclub.trainers.service;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.view.document.AbstractPdfView;

import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

public class TrainerPDFView extends AbstractPdfView {
	
	@Autowired
	private TrainersService trainersService;

	protected void buildPdfDocument(Map model,
			   Document document, PdfWriter writer, HttpServletRequest req,
			   HttpServletResponse resp) throws Exception {
	  
		HashMap<String, Object> map = (HashMap<String, Object>) model.get("map");
		Integer id = (Integer) map.get("id");
		String type = (String) map.get("type");
		trainersService.createPDFDocumentWithDetails(document, id, type);//new XWPFDocument();

	}
}
