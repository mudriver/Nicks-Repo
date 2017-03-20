package ie.turfclub.trainers.service;

import ie.turfclub.sbs.model.SBSEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.view.document.AbstractPdfView;

import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfWriter;

@Service
public class ReprintPDF  extends AbstractPdfView {

	@Autowired
	private PDFService pdfService;
	
	@Override
	protected void buildPdfDocument(Map<String, Object> model, Document document,
			PdfWriter pdfWrite, HttpServletRequest req, HttpServletResponse res)
			throws Exception {
		
		HashMap<String, Object> map = (HashMap<String, Object>) model.get("map");
		HashMap<String, Object> records = (HashMap<String, Object>) map.get("records");
		
		pdfService.buildReprintPDF(document, records);
	}
}
