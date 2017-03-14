package ie.turfclub.common.thread;

import ie.turfclub.trainers.service.TrainersService;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpSession;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class PrintAintreeThread implements Runnable {

	private TrainersService trainerService;
	private String aintreePDFPath;
	private HttpSession session;
	
	public PrintAintreeThread(TrainersService trainerService, String aintreePDFPath, HttpSession session) {
		this.trainerService = trainerService;
		this.aintreePDFPath = aintreePDFPath;
		this.session = session;
	}
	
	@Override
	public void run() {
		
		List<HashMap<String, Object>> records = trainerService.getAintreeRecord();
		File file = new File(this.aintreePDFPath);
		if(file.exists())
			file.delete();
		
		try {
			file.createNewFile();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		Document document = new Document();
		try {
			PdfWriter.getInstance(document, new FileOutputStream(this.aintreePDFPath));
			document.open();
			
			PdfPTable table = addContentIntoPDF(records);
			document.add(table);
			document.close();
			
		} catch (FileNotFoundException | DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private PdfPTable addContentIntoPDF(List<HashMap<String, Object>> records) {
		
		Font titleFont = FontFactory.getFont(FontFactory.TIMES_BOLD, 16, BaseColor.BLUE);
		Font headerFont = FontFactory.getFont(FontFactory.TIMES_BOLD, 14, BaseColor.BLUE);
		Font trainerTextFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, BaseColor.BLUE);
		Font employeeTextFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, BaseColor.BLACK);
		PdfPTable table = new PdfPTable(3);
		table.setWidthPercentage(100);
		
		PdfPCell cell = new PdfPCell();
		cell = new PdfPCell(new Phrase(new Chunk("Aintree", titleFont)));
		cell.setBorder(0);
		table.addCell(cell);
		cell = new PdfPCell(new Phrase(new Chunk("")));
		cell.setBorder(0);
		table.addCell(cell);
		cell = new PdfPCell(new Phrase(new Chunk("")));
		cell.setBorder(0);
		table.addCell(cell);
		
		cell = new PdfPCell(new Phrase(new Chunk("")));
		cell.setBorder(0);
		table.addCell(cell);
		cell = new PdfPCell(new Phrase(new Chunk("")));
		cell.setBorder(0);
		table.addCell(cell);
		cell = new PdfPCell(new Phrase(new Chunk("")));
		cell.setBorder(0);
		table.addCell(cell);
		
		cell = new PdfPCell(new Phrase(new Chunk("Current Trainers", headerFont)));
		cell.setBorder(PdfPCell.TOP | PdfPCell.BOTTOM);
		cell.setBorderColor(BaseColor.BLUE);
		table.addCell(cell);
		cell = new PdfPCell(new Phrase(new Chunk("Surname", headerFont)));
		cell.setBorder(PdfPCell.TOP | PdfPCell.BOTTOM);
		cell.setBorderColor(BaseColor.BLUE);
		table.addCell(cell);
		cell = new PdfPCell(new Phrase(new Chunk("Firstname", headerFont)));
		cell.setBorder(PdfPCell.TOP | PdfPCell.BOTTOM);
		cell.setBorderColor(BaseColor.BLUE);
		table.addCell(cell);
		
		if(records != null && records.size() > 0) {
			for (HashMap<String, Object> map : records) {
				if((boolean)map.get("isTrainer")) {
					cell = new PdfPCell(new Phrase(new Chunk((String)map.get("name"), trainerTextFont)));
					cell.setBorder(0);
					table.addCell(cell);
					cell = new PdfPCell(new Phrase(new Chunk("")));
					cell.setBorder(0);
					table.addCell(cell);
					cell = new PdfPCell(new Phrase(new Chunk("")));
					cell.setBorder(0);
					table.addCell(cell);
				} else {
					cell = new PdfPCell(new Phrase(new Chunk("")));
					cell.setBorder(0);
					table.addCell(cell);
					cell = new PdfPCell(new Phrase(new Chunk((String)map.get("surname"), employeeTextFont)));
					cell.setBorder(0);
					table.addCell(cell);
					cell = new PdfPCell(new Phrase(new Chunk((String)map.get("firstname"), employeeTextFont)));
					cell.setBorder(0);
					table.addCell(cell);
				}
			}
		}
		
		session.setAttribute("aintreeStatus", "done");
		return table;
	}
}
