package ie.turfclub.trainers.service;

import ie.turfclub.sbs.model.SBSEntity;

import java.awt.Color;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Image;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;

@PropertySource("classpath:ie/turfclub/main/resources/properties/config.properties")
@Service
@Transactional
public class PDFService {
	
	@Autowired
	private SessionFactory sessionFactory;
	@Resource
	private Environment env;
	@Autowired
	private MessageSource messageSource;
	
	private Session getCurrentSession() {
		return sessionFactory.getCurrentSession();
	}
	
	private PdfPTable getTable(int noOfColumns) {
		PdfPTable table = new PdfPTable(noOfColumns);
		table.setWidthPercentage(100);
		return table;
	}

	public void buildInitialLetterPDF(Document document,
			List<HashMap<String, Object>> records, List<SBSEntity> sbsRecords, String date) throws Exception {

		String logoPath = env.getRequiredProperty("tclogo.path");
		//document = new Document(PageSize.A4);
		
		PdfPTable table = getTable(3);
		float[] tableWidth = new float[] {25f,25f,25f};
		Font boldWithUnderline = new Font(Font.getFamilyIndex("TIMES_ROMAN"), 12, Font.BOLD|Font.UNDERLINE);
		Font bold = new Font(Font.getFamilyIndex("TIMES_ROMAN"), 12, Font.BOLD);
		Font font = new Font(Font.getFamilyIndex("TIMES_ROMAN"), 12, Font.NORMAL);
		
		table.setWidths(tableWidth);
		PdfPCell cell = new PdfPCell();
		if(records != null) {
			for(int i=0; i<records.size(); i++) {
				HashMap<String, Object> record = records.get(i);
				table = getTable(3);
				tableWidth = new float[] {25f,25f,25f};
				table.setWidths(tableWidth);
				//--------------------------------------------------------
				// First Page
				//--------------------------------------------------------
				Image logoImage = Image.getInstance(logoPath);
				logoImage.setAbsolutePosition(50, 50);
				logoImage.setAlignment(Image.MIDDLE);
				cell = new PdfPCell(new Phrase(new Chunk("")));
				cell.setBorder(0);
				table.addCell(cell);
				cell = new PdfPCell(logoImage);
				cell.setBorder(0);
				table.addCell(cell);
				cell = new PdfPCell(new Phrase(new Chunk("")));
				cell.setBorder(0);
				table.addCell(cell);
				document.add(table);
				
				document.add(new Phrase("\n"));
				
				table = getTable(2);
				Chunk chunk = new Chunk((String)record.get("name"), font);
				cell = new PdfPCell(new Phrase(chunk));
				cell.setBorder(0);
				cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
				table.addCell(cell);
				
				chunk = new Chunk((String)record.get("date"), font);
				cell = new PdfPCell(new Phrase(chunk));
				cell.setBorder(0);
				cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
				table.addCell(cell);
				
				chunk = new Chunk((String)record.get("address1"), font);
				cell = new PdfPCell(new Phrase(chunk));
				cell.setBorder(0);
				cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
				table.addCell(cell);
				
				cell = new PdfPCell(new Phrase(new Chunk("")));
				cell.setBorder(0);
				cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
				table.addCell(cell);
				
				chunk = new Chunk((String)record.get("address2"), font);
				cell = new PdfPCell(new Phrase(chunk));
				cell.setBorder(0);
				cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
				table.addCell(cell);
				
				cell = new PdfPCell(new Phrase(new Chunk("")));
				cell.setBorder(0);
				cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
				table.addCell(cell);
				
				chunk = new Chunk((String)record.get("address3"), font);
				cell = new PdfPCell(new Phrase(chunk));
				cell.setBorder(0);
				cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
				table.addCell(cell);
				
				cell = new PdfPCell(new Phrase(new Chunk("")));
				cell.setBorder(0);
				cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
				table.addCell(cell);
				
				chunk = new Chunk((String)record.get("address4"), font);
				cell = new PdfPCell(new Phrase(chunk));
				cell.setBorder(0);
				cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
				table.addCell(cell);
				
				cell = new PdfPCell(new Phrase(new Chunk("")));
				cell.setBorder(0);
				cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
				table.addCell(cell);
				document.add(table);
				
				table = getTable(1);
				cell = new PdfPCell(new Phrase(new Chunk("Stable Employee Bonus Scheme", boldWithUnderline)));
				cell.setBorder(0);
				cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
				table.addCell(cell);
				document.add(table);
				document.add(new Phrase("\n"));
				
				table = getTable(1);
				Phrase phrase = new Phrase();
				String text = "Dear "+record.get("title")+" "+record.get("surname")+", \n\n";
				text += "Please find attached documentation in relation to the Stable Employee Bonus Scheme.\n\n";
				text += "As per the provisions of Rule 298 you are required to complete the attached Trainer's Receipt form. You are "+ 
			    		"also required to insert the gross amount payable to each registered stable employee on the attached listing of "+ 
			    		"your registered employee's. Please note this must be divided according to the most recently lodged Staff "+
			    		"Agreement.\n\n";
				text += "Please return the completed Trainer's Receipt along with one copy of your registered employee's detailing the "+
			    		"gross amount paid to each employee to the Licensing Department of the Turf Club. This documentation "+
			    		"should be returned by : ";
				phrase.add(new Chunk(text, font));
				phrase.add(new Chunk(record.get("returnDate")+"\n\n", boldWithUnderline));
				text = "The other copy of your registered employee's detailing the gross amount paid to each employee must be "+
			    		"displayed in a Staff Working area at your premises where it can be inspected by all your registered "+
			    		"employee's. Payments may only be made to persons whose names are included in the Register of Stable "+
			    		"Employees as being either currently employed by you or employed during the period covered by the return.\n\n";
				text += "Please note the Bonus payment is fully taxable and should be processed through your wages system, all "+
					   "appropriate Tax deductions should be deducted on all amounts paid to employees.\n\n";
				text += "Failure to comply with the Terms and Conditions of this Scheme shall constitute a breach of Rule 298 and "+
					   "will be referred to the Referrals Committee.\n\n ";
				text += "Yours Sincerely.\n\n\n\n\n";
				phrase.add(new Chunk(text, font));
				text = "Sally Wilson\n";
				phrase.add(new Chunk(text, bold));
				text = "LICENSING DEPARTMENT";
				phrase.add(new Chunk(text, boldWithUnderline));
				
				document.add(new Paragraph(phrase));
				
				//----------------------------------------------------
				//Second Page
				//----------------------------------------------------
				table = getTable(3);
				table.setWidths(tableWidth);
				cell = new PdfPCell(new Phrase(new Chunk("")));
				cell.setBorder(0);
				table.addCell(cell);
				cell = new PdfPCell(logoImage);
				cell.setBorder(0);
				table.addCell(cell);
				cell = new PdfPCell(new Phrase(new Chunk("")));
				cell.setBorder(0);
				table.addCell(cell);
				document.add(table);
				
				document.add(new Phrase("\n"));
				document.add(new Phrase("\n"));
				
				table = getTable(2);
				chunk = new Chunk((String)record.get("name"), font);
				cell = new PdfPCell(new Phrase(chunk));
				cell.setBorder(0);
				cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
				table.addCell(cell);
				
				chunk = new Chunk((String)record.get("date"), font);
				cell = new PdfPCell(new Phrase(chunk));
				cell.setBorder(0);
				cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
				table.addCell(cell);
				
				chunk = new Chunk((String)record.get("address1"), font);
				cell = new PdfPCell(new Phrase(chunk));
				cell.setBorder(0);
				cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
				table.addCell(cell);
				
				cell = new PdfPCell(new Phrase(new Chunk("")));
				cell.setBorder(0);
				cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
				table.addCell(cell);
				
				chunk = new Chunk((String)record.get("address2"), font);
				cell = new PdfPCell(new Phrase(chunk));
				cell.setBorder(0);
				cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
				table.addCell(cell);
				
				cell = new PdfPCell(new Phrase(new Chunk("")));
				cell.setBorder(0);
				cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
				table.addCell(cell);
				
				chunk = new Chunk((String)record.get("address3"), font);
				cell = new PdfPCell(new Phrase(chunk));
				cell.setBorder(0);
				cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
				table.addCell(cell);
				
				cell = new PdfPCell(new Phrase(new Chunk("")));
				cell.setBorder(0);
				cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
				table.addCell(cell);
				
				chunk = new Chunk((String)record.get("address4"), font);
				cell = new PdfPCell(new Phrase(chunk));
				cell.setBorder(0);
				cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
				table.addCell(cell);
				
				cell = new PdfPCell(new Phrase(new Chunk("")));
				cell.setBorder(0);
				cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
				table.addCell(cell);
				document.add(table);
				
				table = getTable(1);
				cell = new PdfPCell(new Phrase(new Chunk("Trainer Receipt\n\n("+record.get("quarter")+")\n\n", boldWithUnderline)));
				cell.setBorder(0);
				cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
				table.addCell(cell);
				document.add(table);
				
				document.add(new Phrase("\n"));
				
				text = "I confirm that the amount paid to me of €"+record.get("amount")+" under the Stable Employees Bonus will be distributed to my "+
			    		"staff on (   /   /     ) in accordance with the distribution agreement agreed amongst themselves. I am "+
			    		"attaching a completed duplicate sheet which sets out the gross payment to each employee.\n\n\n "+
			    		"Yours truly, \n\n\n";
				document.add(new Paragraph(new Chunk(text, font)));
				
				table = getTable(1);
				text = "\n\n\nSigned : _______________________________________\n"
						+ "                     (Trainer's Signature only)\n\n"
						+ "Name   : _______________________________________\n"
						+ "                     (BLOCK CAPITALS)\n\n\n"
						+ "This form must be returned to the Turf Club no later than "+date+". Failure to do so "
						+ "will result in this matter being referred to the Referrals Committee.";
				document.add(new Paragraph(new Phrase(new Chunk(text, bold))));
				
				table = getTable(1);
				cell = new PdfPCell(new Phrase(new Chunk("\n\nPLEASE RETURN TO THE TURF CLUB\n\n\n\n\n\n", font)));
				cell.setBorder(0);
				cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
				table.addCell(cell);
				document.add(table);
				
				//---------------------------------------------------
				//Third Page
				//---------------------------------------------------
				
				int noOfPages = (int) record.get("noOfPages");
				for(int j=1; j<= noOfPages; j++) {
					table = getTable(3);
					table.setWidths(tableWidth);
					cell = new PdfPCell(new Phrase(new Chunk("")));
					cell.setBorder(0);
					table.addCell(cell);
					cell = new PdfPCell(logoImage);
					cell.setBorder(0);
					table.addCell(cell);
					cell = new PdfPCell(new Phrase(new Chunk("")));
					cell.setBorder(0);
					table.addCell(cell);
					document.add(table);
					
					document.add(new Phrase("\n"));
					document.add(new Phrase("\n"));	
					
					table = getTable(1);
					text = "Quarterly Bonus Payment ("+record.get("quarter")+")\n\n";
					cell = new PdfPCell(new Phrase(new Chunk(text, boldWithUnderline)));
					cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
					cell.setBorder(0);
					table.addCell(cell);
					document.add(table);
					
					table = getTable(1);
					text = "The employees for Trainer "+record.get("trainerName")+" ("+record.get("accNo")+") are listed below :\n\n";
					cell = new PdfPCell(new Phrase(new Chunk(text, font)));
					cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
					cell.setBorder(0);
					table.addCell(cell);
					document.add(table);
					
					PdfPTable childTable = getTable(3);
					cell = new PdfPCell(new Phrase(new Chunk("Number", boldWithUnderline)));
					cell.setBorder(0);
					cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
					childTable.addCell(cell);
					cell = new PdfPCell(new Phrase(new Chunk("Employee Name", boldWithUnderline)));
					cell.setBorder(0);
					cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
					childTable.addCell(cell);
					cell = new PdfPCell(new Phrase(new Chunk("Amount", boldWithUnderline)));
					cell.setBorder(0);
					cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
					childTable.addCell(cell);
					
					cell = new PdfPCell(new Phrase(new Chunk("A Card Holders", boldWithUnderline)));
					cell.setBorder(0);
					cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
					childTable.addCell(cell);
					cell = new PdfPCell(new Phrase(new Chunk("")));
					cell.setBorder(0);
					childTable.addCell(cell);
					cell = new PdfPCell(new Phrase(new Chunk("")));
					cell.setBorder(0);
					childTable.addCell(cell);
					
					cell = new PdfPCell(new Phrase(new Chunk(" ")));
					cell.setBorder(0);
					childTable.addCell(cell);
					cell = new PdfPCell(new Phrase(new Chunk(" ")));
					cell.setBorder(0);
					childTable.addCell(cell);
					cell = new PdfPCell(new Phrase(new Chunk(" ")));
					cell.setBorder(0);
					childTable.addCell(cell);
					
					if(record.get("emp"+j) != null) {
						List<HashMap<String, Object>> emps = (List<HashMap<String, Object>>) record.get("emp"+j);
						if(emps != null) {
							for (HashMap<String, Object> hashMap : emps) {
								String cardNumber = String.valueOf(hashMap.get("cardNumber") != null ? hashMap.get("cardNumber") : "");
								cell = new PdfPCell(new Phrase(new Chunk(cardNumber, font)));
								cell.setBorderWidth(1);
								cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
								childTable.addCell(cell);
								String name = (String)hashMap.get("name");
								if(name.length() > 20)
									name = name.substring(0, 20)+"...";
								cell = new PdfPCell(new Phrase(new Chunk(name, font)));
								cell.setBorderWidth(1);
								cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
								childTable.addCell(cell);
								cell = new PdfPCell(new Phrase(new Chunk("")));
								cell.setBorderWidth(1);
								cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
								childTable.addCell(cell);
							}
						}
					}
					
					cell = new PdfPCell(new Phrase(new Chunk(" ")));
					cell.setBorder(0);
					childTable.addCell(cell);
					cell = new PdfPCell(new Phrase(new Chunk(" ")));
					cell.setBorder(0);
					childTable.addCell(cell);
					cell = new PdfPCell(new Phrase(new Chunk(" ")));
					cell.setBorder(0);
					childTable.addCell(cell);
					
					cell = new PdfPCell(new Phrase(new Chunk("")));
					cell.setBorder(0);
					childTable.addCell(cell);
					cell = new PdfPCell(new Phrase(new Chunk("Total = € ", bold)));
					cell.setBorder(0);
					cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
					childTable.addCell(cell);
					cell = new PdfPCell(new Phrase(new Chunk(""+record.get("amount"), font)));
					cell.setBorderWidth(1);
					childTable.addCell(cell);
					
					cell = new PdfPCell(new Phrase(new Chunk("")));
					cell.setBorder(0);
					childTable.addCell(cell);
					cell = new PdfPCell(new Phrase(new Chunk("")));
					cell.setBorder(0);
					childTable.addCell(cell);
					cell = new PdfPCell(new Phrase(new Chunk("* Gross Amount", font)));
					cell.setBorder(0);
					childTable.addCell(cell);
					
					document.add(childTable);
					
					table = getTable(1);
					text = "\n\nPLEASE RETURN TO THE TURF CLUB\n\n";
					cell = new PdfPCell(new Phrase(new Chunk(text, font)));
					cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
					cell.setBorder(0);
					table.addCell(cell);
					document.add(table);
					
					table = getTable(1);
					text = "* Please note the Bonus payment is liable for tax and should be processed through your wages system."+
					    		"All appropriate Tax deductions should be deducted on all amounts paid to employees.";
					cell = new PdfPCell(new Phrase(new Chunk(text, font)));
					cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
					cell.setBorder(0);
					table.addCell(cell);
					document.add(table);

					int noOfSpace = (int) record.get("space"+j);
					for(int l=0;l <noOfSpace; l++) {
						table = getTable(1);
						cell = new PdfPCell(new Phrase(new Chunk(" ")));
						cell.setBorder(0);
						table.addCell(cell);
						document.add(table);
					}
				}
				
				//---------------------------------------------------
				//Forth Page
				//---------------------------------------------------
				
				noOfPages = (int) record.get("noOfPages");
				for(int j=1; j<= noOfPages; j++) {
					table = getTable(3);
					table.setWidths(tableWidth);
					cell = new PdfPCell(new Phrase(new Chunk("")));
					cell.setBorder(0);
					table.addCell(cell);
					cell = new PdfPCell(logoImage);
					cell.setBorder(0);
					table.addCell(cell);
					cell = new PdfPCell(new Phrase(new Chunk("")));
					cell.setBorder(0);
					table.addCell(cell);
					document.add(table);
					
					document.add(new Phrase("\n"));
					document.add(new Phrase("\n"));	
					
					table = getTable(1);
					text = "Quarterly Bonus Payment ("+record.get("quarter")+")\n\n";
					cell = new PdfPCell(new Phrase(new Chunk(text, boldWithUnderline)));
					cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
					cell.setBorder(0);
					table.addCell(cell);
					document.add(table);
					
					table = getTable(1);
					text = "The employees for Trainer "+record.get("trainerName")+" ("+record.get("accNo")+") are listed below :\n\n";
					cell = new PdfPCell(new Phrase(new Chunk(text, font)));
					cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
					cell.setBorder(0);
					table.addCell(cell);
					document.add(table);
					
					PdfPTable childTable = getTable(3);
					cell = new PdfPCell(new Phrase(new Chunk("Number", boldWithUnderline)));
					cell.setBorder(0);
					cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
					childTable.addCell(cell);
					cell = new PdfPCell(new Phrase(new Chunk("Employee Name", boldWithUnderline)));
					cell.setBorder(0);
					cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
					childTable.addCell(cell);
					cell = new PdfPCell(new Phrase(new Chunk("Amount", boldWithUnderline)));
					cell.setBorder(0);
					cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
					childTable.addCell(cell);
					
					cell = new PdfPCell(new Phrase(new Chunk("A Card Holders", boldWithUnderline)));
					cell.setBorder(0);
					cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
					childTable.addCell(cell);
					cell = new PdfPCell(new Phrase(new Chunk("")));
					cell.setBorder(0);
					childTable.addCell(cell);
					cell = new PdfPCell(new Phrase(new Chunk("")));
					cell.setBorder(0);
					childTable.addCell(cell);
					
					cell = new PdfPCell(new Phrase(new Chunk(" ")));
					cell.setBorder(0);
					childTable.addCell(cell);
					cell = new PdfPCell(new Phrase(new Chunk(" ")));
					cell.setBorder(0);
					childTable.addCell(cell);
					cell = new PdfPCell(new Phrase(new Chunk(" ")));
					cell.setBorder(0);
					childTable.addCell(cell);
					
					if(record.get("emp"+j) != null) {
						List<HashMap<String, Object>> emps = (List<HashMap<String, Object>>) record.get("emp"+j);
						if(emps != null) {
							for (HashMap<String, Object> hashMap : emps) {
								String cardNumber = String.valueOf(hashMap.get("cardNumber") != null ? hashMap.get("cardNumber") : "");
								cell = new PdfPCell(new Phrase(new Chunk(cardNumber, font)));
								cell.setBorderWidth(1);
								cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
								childTable.addCell(cell);
								cell = new PdfPCell(new Phrase(new Chunk((String)hashMap.get("name"), font)));
								cell.setBorderWidth(1);
								cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
								childTable.addCell(cell);
								cell = new PdfPCell(new Phrase(new Chunk("")));
								cell.setBorderWidth(1);
								cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
								childTable.addCell(cell);
							}
						}
					}
					
					cell = new PdfPCell(new Phrase(new Chunk(" ")));
					cell.setBorder(0);
					childTable.addCell(cell);
					cell = new PdfPCell(new Phrase(new Chunk(" ")));
					cell.setBorder(0);
					childTable.addCell(cell);
					cell = new PdfPCell(new Phrase(new Chunk(" ")));
					cell.setBorder(0);
					childTable.addCell(cell);
					
					cell = new PdfPCell(new Phrase(new Chunk("")));
					cell.setBorder(0);
					childTable.addCell(cell);
					cell = new PdfPCell(new Phrase(new Chunk("Total = € ", bold)));
					cell.setBorder(0);
					cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
					childTable.addCell(cell);
					cell = new PdfPCell(new Phrase(new Chunk(""+record.get("amount"), font)));
					cell.setBorderWidth(1);
					childTable.addCell(cell);
					
					cell = new PdfPCell(new Phrase(new Chunk("")));
					cell.setBorder(0);
					childTable.addCell(cell);
					cell = new PdfPCell(new Phrase(new Chunk("")));
					cell.setBorder(0);
					childTable.addCell(cell);
					cell = new PdfPCell(new Phrase(new Chunk("* Gross Amount", font)));
					cell.setBorder(0);
					childTable.addCell(cell);
					
					document.add(childTable);
					
					table = getTable(1);
					text = "\n\nPLEASE Display in a Staff Working Area.\n\n";
					cell = new PdfPCell(new Phrase(new Chunk(text, font)));
					cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
					cell.setBorder(0);
					table.addCell(cell);
					document.add(table);
					
					table = getTable(1);
					text = "* Please note the Bonus payment is liable for tax and should be processed through your wages system."+
					    		"All appropriate Tax deductions should be deducted on all amounts paid to employees.";
					cell = new PdfPCell(new Phrase(new Chunk(text, font)));
					cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
					cell.setBorder(0);
					table.addCell(cell);
					document.add(table);

					int noOfSpace = (int) record.get("space"+j);
					for(int l=0;l <noOfSpace; l++) {
						table = getTable(1);
						cell = new PdfPCell(new Phrase(new Chunk(" ")));
						cell.setBorder(0);
						table.addCell(cell);
						document.add(table);
					}
				}
			}
		}
		
		if(sbsRecords != null) {
			table = getTable(2);
			for(int i=0; i<sbsRecords.size(); i++) {
				SBSEntity record = sbsRecords.get(i);
				i++;
				if(i < sbsRecords.size()) {
					SBSEntity newrecord = sbsRecords.get(i);
					
					cell = new PdfPCell(new Phrase(new Chunk(record.getSbsName(), font)));
					cell.setBorder(0);
					table.addCell(cell);
					
					cell = new PdfPCell(new Phrase(new Chunk(newrecord.getSbsName(), font)));
					cell.setBorder(0);
					table.addCell(cell);
					
					String address1 = record.getAddress1() != null ? record.getAddress1() : " ";
					cell = new PdfPCell(new Phrase(new Chunk(address1, font)));
					cell.setBorder(0);
					table.addCell(cell);
					
					cell = new PdfPCell(new Phrase(new Chunk(newrecord.getAddress1(), font)));
					cell.setBorder(0);
					table.addCell(cell);
					
					String address2 = record.getAddress2() != null ? record.getAddress2() : " ";
					cell = new PdfPCell(new Phrase(new Chunk(address2, font)));
					cell.setBorder(0);
					table.addCell(cell);
					
					cell = new PdfPCell(new Phrase(new Chunk(newrecord.getAddress2(), font)));
					cell.setBorder(0);
					table.addCell(cell);
					
					String address3 = record.getAddress3() != null ? record.getAddress3() : " ";
					cell = new PdfPCell(new Phrase(new Chunk(address3, font)));
					cell.setBorder(0);
					table.addCell(cell);
					
					cell = new PdfPCell(new Phrase(new Chunk(newrecord.getAddress3(), font)));
					cell.setBorder(0);
					table.addCell(cell);
					
					String address4 = record.getAddress4() != null ? record.getAddress4() : " ";
					cell = new PdfPCell(new Phrase(new Chunk(address4, font)));
					cell.setBorder(0);
					table.addCell(cell);
					
					cell = new PdfPCell(new Phrase(new Chunk(newrecord.getAddress4(), font)));
					cell.setBorder(0);
					table.addCell(cell);
					
					cell = new PdfPCell(new Phrase(new Chunk(" ", font)));
					cell.setBorder(0);
					table.addCell(cell);
					
					cell = new PdfPCell(new Phrase(new Chunk(" ", font)));
					cell.setBorder(0);
					table.addCell(cell);
					
					cell = new PdfPCell(new Phrase(new Chunk(" ", font)));
					cell.setBorder(0);
					table.addCell(cell);
					
					cell = new PdfPCell(new Phrase(new Chunk(" ", font)));
					cell.setBorder(0);
					table.addCell(cell);
				}
			}
			document.add(table);
		}
	}

	public void buildReminderLetterPDF(Document document,
			List<HashMap<String, Object>> records, List<SBSEntity> sbsRecords) throws Exception {

		String logoPath = env.getRequiredProperty("tclogo.path");
		//document = new Document(PageSize.A4);
		
		PdfPTable table = getTable(3);
		float[] tableWidth = new float[] {25f,25f,25f};
		Font headerBoldWithUnderline = new Font(Font.getFamilyIndex("TIMES_ROMAN"), 14, Font.BOLD|Font.UNDERLINE);
		Font boldWithUnderline = new Font(Font.getFamilyIndex("TIMES_ROMAN"), 12, Font.BOLD|Font.UNDERLINE);
		Font bold = new Font(Font.getFamilyIndex("TIMES_ROMAN"), 12, Font.BOLD);
		Font font = new Font(Font.getFamilyIndex("TIMES_ROMAN"), 12, Font.NORMAL);
		
		table.setWidths(tableWidth);
		PdfPCell cell = new PdfPCell();
		if(records != null) {
			for(int i=0; i<records.size(); i++) {
				HashMap<String, Object> record = records.get(i);
				table = getTable(3);
				tableWidth = new float[] {25f,25f,25f};
				table.setWidths(tableWidth);
				//--------------------------------------------------------
				// First Page
				//--------------------------------------------------------
				Image logoImage = Image.getInstance(logoPath);
				logoImage.setAbsolutePosition(50, 50);
				logoImage.setAlignment(Image.MIDDLE);
				cell = new PdfPCell(new Phrase(new Chunk("")));
				cell.setBorder(0);
				table.addCell(cell);
				cell = new PdfPCell(logoImage);
				cell.setBorder(0);
				table.addCell(cell);
				cell = new PdfPCell(new Phrase(new Chunk("")));
				cell.setBorder(0);
				table.addCell(cell);
				document.add(table);
				
				document.add(new Phrase("\n"));
				table = getTable(1);
				cell = new PdfPCell(new Phrase(new Chunk("*** FINAL REMINDER NOTICE ***", headerBoldWithUnderline)));
				cell.setBorder(0);
				cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
				table.addCell(cell);
				document.add(table);
				
				document.add(new Phrase("\n"));
				
				table = getTable(2);
				Chunk chunk = new Chunk((String)record.get("name"), font);
				cell = new PdfPCell(new Phrase(chunk));
				cell.setBorder(0);
				cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
				table.addCell(cell);
				
				chunk = new Chunk((String)record.get("date"), font);
				cell = new PdfPCell(new Phrase(chunk));
				cell.setBorder(0);
				cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
				table.addCell(cell);
				
				chunk = new Chunk((String)record.get("address1"), font);
				cell = new PdfPCell(new Phrase(chunk));
				cell.setBorder(0);
				cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
				table.addCell(cell);
				
				cell = new PdfPCell(new Phrase(new Chunk("")));
				cell.setBorder(0);
				cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
				table.addCell(cell);
				
				chunk = new Chunk((String)record.get("address2"), font);
				cell = new PdfPCell(new Phrase(chunk));
				cell.setBorder(0);
				cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
				table.addCell(cell);
				
				cell = new PdfPCell(new Phrase(new Chunk("")));
				cell.setBorder(0);
				cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
				table.addCell(cell);
				
				chunk = new Chunk((String)record.get("address3"), font);
				cell = new PdfPCell(new Phrase(chunk));
				cell.setBorder(0);
				cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
				table.addCell(cell);
				
				cell = new PdfPCell(new Phrase(new Chunk("")));
				cell.setBorder(0);
				cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
				table.addCell(cell);
				
				chunk = new Chunk((String)record.get("address4"), font);
				cell = new PdfPCell(new Phrase(chunk));
				cell.setBorder(0);
				cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
				table.addCell(cell);
				
				cell = new PdfPCell(new Phrase(new Chunk("")));
				cell.setBorder(0);
				cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
				table.addCell(cell);
				document.add(table);
				
				table = getTable(1);
				cell = new PdfPCell(new Phrase(new Chunk("Stable Employee Bonus Scheme", boldWithUnderline)));
				cell.setBorder(0);
				cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
				table.addCell(cell);
				document.add(table);
				document.add(new Phrase("\n"));
				
				table = getTable(1);
				Phrase phrase = new Phrase();
				String text = "Dear "+record.get("title")+" "+record.get("surname")+", \n\n";
				text += "I refer to the most recent Stable Employees Bonus Scheme payment you received (see attached). I note "+
						"from our records you have failed to return the necessary documentation in accordance with Rule 298.\n\n";
				text += "Please find attached duplicate documentation which you are required to complete and return to this office "+
			    		"within 7 days of the date of this letter. I must put you on notice that failure to do so will mean that "+
			    		"this matter will be referred to the Referrals Committee in accordance with Rule 298.\n\n";
				text += "Should you have any query in relation to this matter, please do not hesitate to contact me.\n\n ";
				phrase.add(new Chunk(text, font));
				text = "Yours Sincerely.\n\n\n\n\n";
				phrase.add(new Chunk(text, font));
				text = "Seamus Gibson\n";
				phrase.add(new Chunk(text, bold));
				text = "SECRETARY TO THE LICENSING COMMITTEE";
				phrase.add(new Chunk(text, boldWithUnderline));
				document.add(new Paragraph(phrase));
				document.add(new Phrase("\n\n\n\n\n\n\n"));
				
				//----------------------------------------------------
				//Second Page
				//----------------------------------------------------
				table = getTable(3);
				table.setWidths(tableWidth);
				cell = new PdfPCell(new Phrase(new Chunk("")));
				cell.setBorder(0);
				table.addCell(cell);
				cell = new PdfPCell(logoImage);
				cell.setBorder(0);
				table.addCell(cell);
				cell = new PdfPCell(new Phrase(new Chunk("")));
				cell.setBorder(0);
				table.addCell(cell);
				document.add(table);
				
				document.add(new Phrase("\n"));
				document.add(new Phrase("\n"));
				
				table = getTable(2);
				chunk = new Chunk((String)record.get("name"), font);
				cell = new PdfPCell(new Phrase(chunk));
				cell.setBorder(0);
				cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
				table.addCell(cell);
				
				chunk = new Chunk((String)record.get("date"), font);
				cell = new PdfPCell(new Phrase(chunk));
				cell.setBorder(0);
				cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
				table.addCell(cell);
				
				chunk = new Chunk((String)record.get("address1"), font);
				cell = new PdfPCell(new Phrase(chunk));
				cell.setBorder(0);
				cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
				table.addCell(cell);
				
				cell = new PdfPCell(new Phrase(new Chunk("")));
				cell.setBorder(0);
				cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
				table.addCell(cell);
				
				chunk = new Chunk((String)record.get("address2"), font);
				cell = new PdfPCell(new Phrase(chunk));
				cell.setBorder(0);
				cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
				table.addCell(cell);
				
				cell = new PdfPCell(new Phrase(new Chunk("")));
				cell.setBorder(0);
				cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
				table.addCell(cell);
				
				chunk = new Chunk((String)record.get("address3"), font);
				cell = new PdfPCell(new Phrase(chunk));
				cell.setBorder(0);
				cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
				table.addCell(cell);
				
				cell = new PdfPCell(new Phrase(new Chunk("")));
				cell.setBorder(0);
				cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
				table.addCell(cell);
				
				chunk = new Chunk((String)record.get("address4"), font);
				cell = new PdfPCell(new Phrase(chunk));
				cell.setBorder(0);
				cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
				table.addCell(cell);
				
				cell = new PdfPCell(new Phrase(new Chunk("")));
				cell.setBorder(0);
				cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
				table.addCell(cell);
				document.add(table);
				
				table = getTable(1);
				cell = new PdfPCell(new Phrase(new Chunk("Trainer Receipt\n\n("+record.get("quarter")+")\n\n", boldWithUnderline)));
				cell.setBorder(0);
				cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
				table.addCell(cell);
				document.add(table);
				
				document.add(new Phrase("\n"));
				
				text = "I confirm that the amount paid to me of €"+record.get("amount")+" under the Stable Employees Bonus will be distributed to my "+
			    		"staff on (   /   /     ) in accordance with the distribution agreement agreed amongst themselves. I am "+
			    		"attaching a completed duplicate sheet which sets out the gross payment to each employee.\n\n\n "+
			    		"Yours truly, \n\n\n";
				document.add(new Paragraph(new Chunk(text, font)));
				
				table = getTable(1);
				text = "\n\nSigned : _______________________________________\n"
						+ "                     (Trainer's Signature only)\n\n"
						+ "Name   : _______________________________________\n"
						+ "                     (BLOCK CAPITALS)\n\n\n"
						+ "This form must be returned to the Turf Club no later than ghf. Failure to do so "
						+ "will result in this matter being referred to the Referrals Committee.";
				document.add(new Paragraph(new Phrase(new Chunk(text, bold))));
				
				table = getTable(1);
				cell = new PdfPCell(new Phrase(new Chunk("\n\nPLEASE RETURN TO THE TURF CLUB\n\n\n\n\n", font)));
				cell.setBorder(0);
				cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
				table.addCell(cell);
				document.add(table);
				
				//---------------------------------------------------
				//Third Page
				//---------------------------------------------------
				
				int noOfPages = (int) record.get("noOfPages");
				for(int j=1; j<= noOfPages; j++) {
					table = getTable(3);
					table.setWidths(tableWidth);
					cell = new PdfPCell(new Phrase(new Chunk("")));
					cell.setBorder(0);
					table.addCell(cell);
					cell = new PdfPCell(logoImage);
					cell.setBorder(0);
					table.addCell(cell);
					cell = new PdfPCell(new Phrase(new Chunk("")));
					cell.setBorder(0);
					table.addCell(cell);
					document.add(table);
					
					document.add(new Phrase("\n"));
					document.add(new Phrase("\n"));	
					
					table = getTable(1);
					text = "Quarterly Bonus Payment ("+record.get("quarter")+")\n\n";
					cell = new PdfPCell(new Phrase(new Chunk(text, boldWithUnderline)));
					cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
					cell.setBorder(0);
					table.addCell(cell);
					document.add(table);
					
					table = getTable(1);
					text = "The employees for Trainer "+record.get("trainerName")+" ("+record.get("accNo")+") are listed below :\n\n";
					cell = new PdfPCell(new Phrase(new Chunk(text, font)));
					cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
					cell.setBorder(0);
					table.addCell(cell);
					document.add(table);
					
					PdfPTable childTable = getTable(3);
					cell = new PdfPCell(new Phrase(new Chunk("Number", boldWithUnderline)));
					cell.setBorder(0);
					cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
					childTable.addCell(cell);
					cell = new PdfPCell(new Phrase(new Chunk("Employee Name", boldWithUnderline)));
					cell.setBorder(0);
					cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
					childTable.addCell(cell);
					cell = new PdfPCell(new Phrase(new Chunk("Amount", boldWithUnderline)));
					cell.setBorder(0);
					cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
					childTable.addCell(cell);
					
					cell = new PdfPCell(new Phrase(new Chunk("A Card Holders", boldWithUnderline)));
					cell.setBorder(0);
					cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
					childTable.addCell(cell);
					cell = new PdfPCell(new Phrase(new Chunk("")));
					cell.setBorder(0);
					childTable.addCell(cell);
					cell = new PdfPCell(new Phrase(new Chunk("")));
					cell.setBorder(0);
					childTable.addCell(cell);
					
					cell = new PdfPCell(new Phrase(new Chunk(" ")));
					cell.setBorder(0);
					childTable.addCell(cell);
					cell = new PdfPCell(new Phrase(new Chunk(" ")));
					cell.setBorder(0);
					childTable.addCell(cell);
					cell = new PdfPCell(new Phrase(new Chunk(" ")));
					cell.setBorder(0);
					childTable.addCell(cell);
					
					if(record.get("emp"+j) != null) {
						List<HashMap<String, Object>> emps = (List<HashMap<String, Object>>) record.get("emp"+j);
						if(emps != null) {
							for (HashMap<String, Object> hashMap : emps) {
								String cardNumber = String.valueOf(hashMap.get("cardNumber") != null ? hashMap.get("cardNumber") : "");
								cell = new PdfPCell(new Phrase(new Chunk(cardNumber, font)));
								cell.setBorderWidth(1);
								cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
								childTable.addCell(cell);
								cell = new PdfPCell(new Phrase(new Chunk((String)hashMap.get("name"), font)));
								cell.setBorderWidth(1);
								cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
								childTable.addCell(cell);
								cell = new PdfPCell(new Phrase(new Chunk("")));
								cell.setBorderWidth(1);
								cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
								childTable.addCell(cell);
							}
						}
					}
					
					cell = new PdfPCell(new Phrase(new Chunk(" ")));
					cell.setBorder(0);
					childTable.addCell(cell);
					cell = new PdfPCell(new Phrase(new Chunk(" ")));
					cell.setBorder(0);
					childTable.addCell(cell);
					cell = new PdfPCell(new Phrase(new Chunk(" ")));
					cell.setBorder(0);
					childTable.addCell(cell);
					
					cell = new PdfPCell(new Phrase(new Chunk("")));
					cell.setBorder(0);
					childTable.addCell(cell);
					cell = new PdfPCell(new Phrase(new Chunk("Total = € ", bold)));
					cell.setBorder(0);
					cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
					childTable.addCell(cell);
					cell = new PdfPCell(new Phrase(new Chunk(""+record.get("amount"), font)));
					cell.setBorderWidth(1);
					childTable.addCell(cell);
					
					cell = new PdfPCell(new Phrase(new Chunk("")));
					cell.setBorder(0);
					childTable.addCell(cell);
					cell = new PdfPCell(new Phrase(new Chunk("")));
					cell.setBorder(0);
					childTable.addCell(cell);
					cell = new PdfPCell(new Phrase(new Chunk("* Gross Amount", font)));
					cell.setBorder(0);
					childTable.addCell(cell);
					
					document.add(childTable);
					
					table = getTable(1);
					text = "\n\nPLEASE RETURN TO THE TURF CLUB\n\n";
					cell = new PdfPCell(new Phrase(new Chunk(text, font)));
					cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
					cell.setBorder(0);
					table.addCell(cell);
					document.add(table);
					
					table = getTable(1);
					text = "* Please note the Bonus payment is liable for tax and should be processed through your wages system."+
					    		"All appropriate Tax deductions should be deducted on all amounts paid to employees.";
					cell = new PdfPCell(new Phrase(new Chunk(text, font)));
					cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
					cell.setBorder(0);
					table.addCell(cell);
					document.add(table);

					int noOfSpace = (int) record.get("space"+j);
					for(int l=0;l <noOfSpace; l++) {
						table = getTable(1);
						cell = new PdfPCell(new Phrase(new Chunk(" ")));
						cell.setBorder(0);
						table.addCell(cell);
						document.add(table);
					}
				}
				
				//---------------------------------------------------
				//Forth Page
				//---------------------------------------------------
				
				noOfPages = (int) record.get("noOfPages");
				for(int j=1; j<= noOfPages; j++) {
					table = getTable(3);
					table.setWidths(tableWidth);
					cell = new PdfPCell(new Phrase(new Chunk("")));
					cell.setBorder(0);
					table.addCell(cell);
					cell = new PdfPCell(logoImage);
					cell.setBorder(0);
					table.addCell(cell);
					cell = new PdfPCell(new Phrase(new Chunk("")));
					cell.setBorder(0);
					table.addCell(cell);
					document.add(table);
					
					document.add(new Phrase("\n"));
					document.add(new Phrase("\n"));	
					
					table = getTable(1);
					text = "Quarterly Bonus Payment ("+record.get("quarter")+")\n\n";
					cell = new PdfPCell(new Phrase(new Chunk(text, boldWithUnderline)));
					cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
					cell.setBorder(0);
					table.addCell(cell);
					document.add(table);
					
					table = getTable(1);
					text = "The employees for Trainer "+record.get("trainerName")+" ("+record.get("accNo")+") are listed below :\n\n";
					cell = new PdfPCell(new Phrase(new Chunk(text, font)));
					cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
					cell.setBorder(0);
					table.addCell(cell);
					document.add(table);
					
					PdfPTable childTable = getTable(3);
					cell = new PdfPCell(new Phrase(new Chunk("Number", boldWithUnderline)));
					cell.setBorder(0);
					cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
					childTable.addCell(cell);
					cell = new PdfPCell(new Phrase(new Chunk("Employee Name", boldWithUnderline)));
					cell.setBorder(0);
					cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
					childTable.addCell(cell);
					cell = new PdfPCell(new Phrase(new Chunk("Amount", boldWithUnderline)));
					cell.setBorder(0);
					cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
					childTable.addCell(cell);
					
					cell = new PdfPCell(new Phrase(new Chunk("A Card Holders", boldWithUnderline)));
					cell.setBorder(0);
					cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
					childTable.addCell(cell);
					cell = new PdfPCell(new Phrase(new Chunk("")));
					cell.setBorder(0);
					childTable.addCell(cell);
					cell = new PdfPCell(new Phrase(new Chunk("")));
					cell.setBorder(0);
					childTable.addCell(cell);
					
					cell = new PdfPCell(new Phrase(new Chunk(" ")));
					cell.setBorder(0);
					childTable.addCell(cell);
					cell = new PdfPCell(new Phrase(new Chunk(" ")));
					cell.setBorder(0);
					childTable.addCell(cell);
					cell = new PdfPCell(new Phrase(new Chunk(" ")));
					cell.setBorder(0);
					childTable.addCell(cell);
					
					if(record.get("emp"+j) != null) {
						List<HashMap<String, Object>> emps = (List<HashMap<String, Object>>) record.get("emp"+j);
						if(emps != null) {
							for (HashMap<String, Object> hashMap : emps) {
								String cardNumber = String.valueOf(hashMap.get("cardNumber") != null ? hashMap.get("cardNumber") : "");
								cell = new PdfPCell(new Phrase(new Chunk(cardNumber, font)));
								cell.setBorderWidth(1);
								cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
								childTable.addCell(cell);
								String name = (String)hashMap.get("name");
								if(name.length() > 20)
									name = name.substring(0, 20)+"...";
								cell = new PdfPCell(new Phrase(new Chunk(name, font)));
								cell.setBorderWidth(1);
								cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
								childTable.addCell(cell);
								cell = new PdfPCell(new Phrase(new Chunk("")));
								cell.setBorderWidth(1);
								cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
								childTable.addCell(cell);
							}
						}
					}
					
					cell = new PdfPCell(new Phrase(new Chunk(" ")));
					cell.setBorder(0);
					childTable.addCell(cell);
					cell = new PdfPCell(new Phrase(new Chunk(" ")));
					cell.setBorder(0);
					childTable.addCell(cell);
					cell = new PdfPCell(new Phrase(new Chunk(" ")));
					cell.setBorder(0);
					childTable.addCell(cell);
					
					cell = new PdfPCell(new Phrase(new Chunk("")));
					cell.setBorder(0);
					childTable.addCell(cell);
					cell = new PdfPCell(new Phrase(new Chunk("Total = € ", bold)));
					cell.setBorder(0);
					cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
					childTable.addCell(cell);
					cell = new PdfPCell(new Phrase(new Chunk(""+record.get("amount"), font)));
					cell.setBorderWidth(1);
					childTable.addCell(cell);
					
					cell = new PdfPCell(new Phrase(new Chunk("")));
					cell.setBorder(0);
					childTable.addCell(cell);
					cell = new PdfPCell(new Phrase(new Chunk("")));
					cell.setBorder(0);
					childTable.addCell(cell);
					cell = new PdfPCell(new Phrase(new Chunk("* Gross Amount", font)));
					cell.setBorder(0);
					childTable.addCell(cell);
					
					document.add(childTable);
					
					table = getTable(1);
					text = "\n\nPLEASE Display in a Staff Working Area.\n\n";
					cell = new PdfPCell(new Phrase(new Chunk(text, font)));
					cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
					cell.setBorder(0);
					table.addCell(cell);
					document.add(table);
					
					table = getTable(1);
					text = "* Please note the Bonus payment is liable for tax and should be processed through your wages system."+
					    		"All appropriate Tax deductions should be deducted on all amounts paid to employees.";
					cell = new PdfPCell(new Phrase(new Chunk(text, font)));
					cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
					cell.setBorder(0);
					table.addCell(cell);
					document.add(table);

					int noOfSpace = (int) record.get("space"+j);
					for(int l=0;l <noOfSpace; l++) {
						table = getTable(1);
						cell = new PdfPCell(new Phrase(new Chunk(" ")));
						cell.setBorder(0);
						table.addCell(cell);
						document.add(table);
					}
				}
			}
		}
		
		if(sbsRecords != null) {
			table = getTable(2);
			for(int i=0; i<sbsRecords.size()-1; i++) {
				SBSEntity record = sbsRecords.get(i);
				i++;
				SBSEntity newrecord = sbsRecords.get(i);
				
				cell = new PdfPCell(new Phrase(new Chunk(record.getSbsName(), font)));
				cell.setBorder(0);
				table.addCell(cell);
				
				cell = new PdfPCell(new Phrase(new Chunk(newrecord.getSbsName(), font)));
				cell.setBorder(0);
				table.addCell(cell);
				
				String address1 = record.getAddress1() != null ? record.getAddress1() : " ";
				cell = new PdfPCell(new Phrase(new Chunk(address1, font)));
				cell.setBorder(0);
				table.addCell(cell);
				
				cell = new PdfPCell(new Phrase(new Chunk(newrecord.getAddress1(), font)));
				cell.setBorder(0);
				table.addCell(cell);
				
				String address2 = record.getAddress2() != null ? record.getAddress2() : " ";
				cell = new PdfPCell(new Phrase(new Chunk(address2, font)));
				cell.setBorder(0);
				table.addCell(cell);
				
				cell = new PdfPCell(new Phrase(new Chunk(newrecord.getAddress2(), font)));
				cell.setBorder(0);
				table.addCell(cell);
				
				String address3 = record.getAddress3() != null ? record.getAddress3() : " ";
				cell = new PdfPCell(new Phrase(new Chunk(address3, font)));
				cell.setBorder(0);
				table.addCell(cell);
				
				cell = new PdfPCell(new Phrase(new Chunk(newrecord.getAddress3(), font)));
				cell.setBorder(0);
				table.addCell(cell);
				
				String address4 = record.getAddress4() != null ? record.getAddress4() : " ";
				cell = new PdfPCell(new Phrase(new Chunk(address4, font)));
				cell.setBorder(0);
				table.addCell(cell);
				
				cell = new PdfPCell(new Phrase(new Chunk(newrecord.getAddress4(), font)));
				cell.setBorder(0);
				table.addCell(cell);
				
				cell = new PdfPCell(new Phrase(new Chunk(" ", font)));
				cell.setBorder(0);
				table.addCell(cell);
				
				cell = new PdfPCell(new Phrase(new Chunk(" ", font)));
				cell.setBorder(0);
				table.addCell(cell);
				
				cell = new PdfPCell(new Phrase(new Chunk(" ", font)));
				cell.setBorder(0);
				table.addCell(cell);
				
				cell = new PdfPCell(new Phrase(new Chunk(" ", font)));
				cell.setBorder(0);
				table.addCell(cell);
			}
			document.add(table);
		}
	}

	public void buildReprintPDF(Document document,
			HashMap<String, Object> record) throws Exception {

		String logoPath = env.getRequiredProperty("tclogo.path");
		//document = new Document(PageSize.A4);
		
		PdfPTable table = getTable(3);
		float[] tableWidth = new float[] {25f,25f,25f};
		Font boldWithUnderline = new Font(Font.getFamilyIndex("TIMES_ROMAN"), 12, Font.BOLD|Font.UNDERLINE);
		Font bold = new Font(Font.getFamilyIndex("TIMES_ROMAN"), 12, Font.BOLD);
		Font font = new Font(Font.getFamilyIndex("TIMES_ROMAN"), 12, Font.NORMAL);
		String date = (String) record.get("rdate");
		table.setWidths(tableWidth);
		PdfPCell cell = new PdfPCell();
		if(record != null) {
			table = getTable(3);
			tableWidth = new float[] {25f,25f,25f};
			table.setWidths(tableWidth);
			//--------------------------------------------------------
			// First Page
			//--------------------------------------------------------
			Image logoImage = Image.getInstance(logoPath);
			logoImage.setAbsolutePosition(50, 50);
			logoImage.setAlignment(Image.MIDDLE);
			cell = new PdfPCell(new Phrase(new Chunk("")));
			cell.setBorder(0);
			table.addCell(cell);
			cell = new PdfPCell(logoImage);
			cell.setBorder(0);
			table.addCell(cell);
			cell = new PdfPCell(new Phrase(new Chunk("")));
			cell.setBorder(0);
			table.addCell(cell);
			document.add(table);
			
			document.add(new Phrase("\n"));
			
			table = getTable(2);
			Chunk chunk = new Chunk((String)record.get("name"), font);
			cell = new PdfPCell(new Phrase(chunk));
			cell.setBorder(0);
			cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
			table.addCell(cell);
			
			chunk = new Chunk((String)record.get("date"), font);
			cell = new PdfPCell(new Phrase(chunk));
			cell.setBorder(0);
			cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
			table.addCell(cell);
			
			chunk = new Chunk((String)record.get("address1"), font);
			cell = new PdfPCell(new Phrase(chunk));
			cell.setBorder(0);
			cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
			table.addCell(cell);
			
			cell = new PdfPCell(new Phrase(new Chunk("")));
			cell.setBorder(0);
			cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
			table.addCell(cell);
			
			chunk = new Chunk((String)record.get("address2"), font);
			cell = new PdfPCell(new Phrase(chunk));
			cell.setBorder(0);
			cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
			table.addCell(cell);
			
			cell = new PdfPCell(new Phrase(new Chunk("")));
			cell.setBorder(0);
			cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
			table.addCell(cell);
			
			chunk = new Chunk((String)record.get("address3"), font);
			cell = new PdfPCell(new Phrase(chunk));
			cell.setBorder(0);
			cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
			table.addCell(cell);
			
			cell = new PdfPCell(new Phrase(new Chunk("")));
			cell.setBorder(0);
			cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
			table.addCell(cell);
			
			chunk = new Chunk((String)record.get("address4"), font);
			cell = new PdfPCell(new Phrase(chunk));
			cell.setBorder(0);
			cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
			table.addCell(cell);
			
			cell = new PdfPCell(new Phrase(new Chunk("")));
			cell.setBorder(0);
			cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
			table.addCell(cell);
			document.add(table);
			
			table = getTable(1);
			cell = new PdfPCell(new Phrase(new Chunk("Stable Employee Bonus Scheme", boldWithUnderline)));
			cell.setBorder(0);
			cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
			table.addCell(cell);
			document.add(table);
			document.add(new Phrase("\n"));
			
			table = getTable(1);
			Phrase phrase = new Phrase();
			String text = "Dear "+record.get("title")+" "+record.get("surname")+", \n\n";
			text += "Please find attached documentation in relation to the Stable Employee Bonus Scheme.\n\n";
			text += "As per the provisions of Rule 298 you are required to complete the attached Trainer's Receipt form. You are "+ 
		    		"also required to insert the gross amount payable to each registered stable employee on the attached listing of "+ 
		    		"your registered employee's. Please note this must be divided according to the most recently lodged Staff "+
		    		"Agreement.\n\n";
			text += "Please return the completed Trainer's Receipt along with one copy of your registered employee's detailing the "+
		    		"gross amount paid to each employee to the Licensing Department of the Turf Club. This documentation "+
		    		"should be returned by : ";
			phrase.add(new Chunk(text, font));
			phrase.add(new Chunk(record.get("returnDate")+"\n\n", boldWithUnderline));
			text = "The other copy of your registered employee's detailing the gross amount paid to each employee must be "+
		    		"displayed in a Staff Working area at your premises where it can be inspected by all your registered "+
		    		"employee's. Payments may only be made to persons whose names are included in the Register of Stable "+
		    		"Employees as being either currently employed by you or employed during the period covered by the return.\n\n";
			text += "Please note the Bonus payment is fully taxable and should be processed through your wages system, all "+
				   "appropriate Tax deductions should be deducted on all amounts paid to employees.\n\n";
			text += "Failure to comply with the Terms and Conditions of this Scheme shall constitute a breach of Rule 298 and "+
				   "will be referred to the Referrals Committee.\n\n ";
			text += "Yours Sincerely.\n\n\n\n\n";
			phrase.add(new Chunk(text, font));
			text = "Sally Wilson\n";
			phrase.add(new Chunk(text, bold));
			text = "LICENSING DEPARTMENT";
			phrase.add(new Chunk(text, boldWithUnderline));
			
			document.add(new Paragraph(phrase));
			
			//----------------------------------------------------
			//Second Page
			//----------------------------------------------------
			table = getTable(3);
			table.setWidths(tableWidth);
			cell = new PdfPCell(new Phrase(new Chunk("")));
			cell.setBorder(0);
			table.addCell(cell);
			cell = new PdfPCell(logoImage);
			cell.setBorder(0);
			table.addCell(cell);
			cell = new PdfPCell(new Phrase(new Chunk("")));
			cell.setBorder(0);
			table.addCell(cell);
			document.add(table);
			
			document.add(new Phrase("\n"));
			document.add(new Phrase("\n"));
			
			table = getTable(2);
			chunk = new Chunk((String)record.get("name"), font);
			cell = new PdfPCell(new Phrase(chunk));
			cell.setBorder(0);
			cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
			table.addCell(cell);
			
			chunk = new Chunk((String)record.get("date"), font);
			cell = new PdfPCell(new Phrase(chunk));
			cell.setBorder(0);
			cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
			table.addCell(cell);
			
			chunk = new Chunk((String)record.get("address1"), font);
			cell = new PdfPCell(new Phrase(chunk));
			cell.setBorder(0);
			cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
			table.addCell(cell);
			
			cell = new PdfPCell(new Phrase(new Chunk("")));
			cell.setBorder(0);
			cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
			table.addCell(cell);
			
			chunk = new Chunk((String)record.get("address2"), font);
			cell = new PdfPCell(new Phrase(chunk));
			cell.setBorder(0);
			cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
			table.addCell(cell);
			
			cell = new PdfPCell(new Phrase(new Chunk("")));
			cell.setBorder(0);
			cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
			table.addCell(cell);
			
			chunk = new Chunk((String)record.get("address3"), font);
			cell = new PdfPCell(new Phrase(chunk));
			cell.setBorder(0);
			cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
			table.addCell(cell);
			
			cell = new PdfPCell(new Phrase(new Chunk("")));
			cell.setBorder(0);
			cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
			table.addCell(cell);
			
			chunk = new Chunk((String)record.get("address4"), font);
			cell = new PdfPCell(new Phrase(chunk));
			cell.setBorder(0);
			cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
			table.addCell(cell);
			
			cell = new PdfPCell(new Phrase(new Chunk("")));
			cell.setBorder(0);
			cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
			table.addCell(cell);
			document.add(table);
			
			table = getTable(1);
			cell = new PdfPCell(new Phrase(new Chunk("Trainer Receipt\n\n("+record.get("quarter")+")\n\n", boldWithUnderline)));
			cell.setBorder(0);
			cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
			table.addCell(cell);
			document.add(table);
			
			document.add(new Phrase("\n"));
			
			text = "I confirm that the amount paid to me of €"+record.get("amount")+" under the Stable Employees Bonus will be distributed to my "+
		    		"staff on (   /   /     ) in accordance with the distribution agreement agreed amongst themselves. I am "+
		    		"attaching a completed duplicate sheet which sets out the gross payment to each employee.\n\n\n "+
		    		"Yours truly, \n\n\n";
			document.add(new Paragraph(new Chunk(text, font)));
			
			table = getTable(1);
			text = "\n\n\nSigned : _______________________________________\n"
					+ "                     (Trainer's Signature only)\n\n"
					+ "Name   : _______________________________________\n"
					+ "                     (BLOCK CAPITALS)\n\n\n"
					+ "This form must be returned to the Turf Club no later than "+date+". Failure to do so "
					+ "will result in this matter being referred to the Referrals Committee.";
			document.add(new Paragraph(new Phrase(new Chunk(text, bold))));
			
			table = getTable(1);
			cell = new PdfPCell(new Phrase(new Chunk("\n\nPLEASE RETURN TO THE TURF CLUB\n\n\n\n\n\n", font)));
			cell.setBorder(0);
			cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
			table.addCell(cell);
			document.add(table);
			
			//---------------------------------------------------
			//Third Page
			//---------------------------------------------------
			
			int noOfPages = (int) record.get("noOfPages");
			for(int j=1; j<= noOfPages; j++) {
				table = getTable(3);
				table.setWidths(tableWidth);
				cell = new PdfPCell(new Phrase(new Chunk("")));
				cell.setBorder(0);
				table.addCell(cell);
				cell = new PdfPCell(logoImage);
				cell.setBorder(0);
				table.addCell(cell);
				cell = new PdfPCell(new Phrase(new Chunk("")));
				cell.setBorder(0);
				table.addCell(cell);
				document.add(table);
				
				document.add(new Phrase("\n"));
				document.add(new Phrase("\n"));	
				
				table = getTable(1);
				text = "Quarterly Bonus Payment ("+record.get("quarter")+")\n\n";
				cell = new PdfPCell(new Phrase(new Chunk(text, boldWithUnderline)));
				cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
				cell.setBorder(0);
				table.addCell(cell);
				document.add(table);
				
				table = getTable(1);
				text = "The employees for Trainer "+record.get("trainerName")+" ("+record.get("accNo")+") are listed below :\n\n";
				cell = new PdfPCell(new Phrase(new Chunk(text, font)));
				cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
				cell.setBorder(0);
				table.addCell(cell);
				document.add(table);
				
				PdfPTable childTable = getTable(3);
				cell = new PdfPCell(new Phrase(new Chunk("Number", boldWithUnderline)));
				cell.setBorder(0);
				cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
				childTable.addCell(cell);
				cell = new PdfPCell(new Phrase(new Chunk("Employee Name", boldWithUnderline)));
				cell.setBorder(0);
				cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
				childTable.addCell(cell);
				cell = new PdfPCell(new Phrase(new Chunk("Amount", boldWithUnderline)));
				cell.setBorder(0);
				cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
				childTable.addCell(cell);
				
				cell = new PdfPCell(new Phrase(new Chunk("A Card Holders", boldWithUnderline)));
				cell.setBorder(0);
				cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
				childTable.addCell(cell);
				cell = new PdfPCell(new Phrase(new Chunk("")));
				cell.setBorder(0);
				childTable.addCell(cell);
				cell = new PdfPCell(new Phrase(new Chunk("")));
				cell.setBorder(0);
				childTable.addCell(cell);
				
				cell = new PdfPCell(new Phrase(new Chunk(" ")));
				cell.setBorder(0);
				childTable.addCell(cell);
				cell = new PdfPCell(new Phrase(new Chunk(" ")));
				cell.setBorder(0);
				childTable.addCell(cell);
				cell = new PdfPCell(new Phrase(new Chunk(" ")));
				cell.setBorder(0);
				childTable.addCell(cell);
				
				if(record.get("emp"+j) != null) {
					List<HashMap<String, Object>> emps = (List<HashMap<String, Object>>) record.get("emp"+j);
					if(emps != null) {
						for (HashMap<String, Object> hashMap : emps) {
							String cardNumber = String.valueOf(hashMap.get("cardNumber") != null ? hashMap.get("cardNumber") : "");
							cell = new PdfPCell(new Phrase(new Chunk(cardNumber, font)));
							cell.setBorderWidth(1);
							cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
							childTable.addCell(cell);
							String name = (String)hashMap.get("name");
							if(name.length() > 20)
								name = name.substring(0, 20)+"...";
							cell = new PdfPCell(new Phrase(new Chunk(name, font)));
							cell.setBorderWidth(1);
							cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
							childTable.addCell(cell);
							cell = new PdfPCell(new Phrase(new Chunk("")));
							cell.setBorderWidth(1);
							cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
							childTable.addCell(cell);
						}
					}
				}
				
				cell = new PdfPCell(new Phrase(new Chunk(" ")));
				cell.setBorder(0);
				childTable.addCell(cell);
				cell = new PdfPCell(new Phrase(new Chunk(" ")));
				cell.setBorder(0);
				childTable.addCell(cell);
				cell = new PdfPCell(new Phrase(new Chunk(" ")));
				cell.setBorder(0);
				childTable.addCell(cell);
				
				cell = new PdfPCell(new Phrase(new Chunk("")));
				cell.setBorder(0);
				childTable.addCell(cell);
				cell = new PdfPCell(new Phrase(new Chunk("Total = € ", bold)));
				cell.setBorder(0);
				cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
				childTable.addCell(cell);
				cell = new PdfPCell(new Phrase(new Chunk(""+record.get("amount"), font)));
				cell.setBorderWidth(1);
				childTable.addCell(cell);
				
				cell = new PdfPCell(new Phrase(new Chunk("")));
				cell.setBorder(0);
				childTable.addCell(cell);
				cell = new PdfPCell(new Phrase(new Chunk("")));
				cell.setBorder(0);
				childTable.addCell(cell);
				cell = new PdfPCell(new Phrase(new Chunk("* Gross Amount", font)));
				cell.setBorder(0);
				childTable.addCell(cell);
				
				document.add(childTable);
				
				table = getTable(1);
				text = "\n\nPLEASE RETURN TO THE TURF CLUB\n\n";
				cell = new PdfPCell(new Phrase(new Chunk(text, font)));
				cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
				cell.setBorder(0);
				table.addCell(cell);
				document.add(table);
				
				table = getTable(1);
				text = "* Please note the Bonus payment is liable for tax and should be processed through your wages system."+
				    		"All appropriate Tax deductions should be deducted on all amounts paid to employees.";
				cell = new PdfPCell(new Phrase(new Chunk(text, font)));
				cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
				cell.setBorder(0);
				table.addCell(cell);
				document.add(table);

				int noOfSpace = (int) record.get("space"+j);
				for(int l=0;l <noOfSpace; l++) {
					table = getTable(1);
					cell = new PdfPCell(new Phrase(new Chunk(" ")));
					cell.setBorder(0);
					table.addCell(cell);
					document.add(table);
				}
			}
			
			//---------------------------------------------------
			//Forth Page
			//---------------------------------------------------
			
			noOfPages = (int) record.get("noOfPages");
			for(int j=1; j<= noOfPages; j++) {
				table = getTable(3);
				table.setWidths(tableWidth);
				cell = new PdfPCell(new Phrase(new Chunk("")));
				cell.setBorder(0);
				table.addCell(cell);
				cell = new PdfPCell(logoImage);
				cell.setBorder(0);
				table.addCell(cell);
				cell = new PdfPCell(new Phrase(new Chunk("")));
				cell.setBorder(0);
				table.addCell(cell);
				document.add(table);
				
				document.add(new Phrase("\n"));
				document.add(new Phrase("\n"));	
				
				table = getTable(1);
				text = "Quarterly Bonus Payment ("+record.get("quarter")+")\n\n";
				cell = new PdfPCell(new Phrase(new Chunk(text, boldWithUnderline)));
				cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
				cell.setBorder(0);
				table.addCell(cell);
				document.add(table);
				
				table = getTable(1);
				text = "The employees for Trainer "+record.get("trainerName")+" ("+record.get("accNo")+") are listed below :\n\n";
				cell = new PdfPCell(new Phrase(new Chunk(text, font)));
				cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
				cell.setBorder(0);
				table.addCell(cell);
				document.add(table);
				
				PdfPTable childTable = getTable(3);
				cell = new PdfPCell(new Phrase(new Chunk("Number", boldWithUnderline)));
				cell.setBorder(0);
				cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
				childTable.addCell(cell);
				cell = new PdfPCell(new Phrase(new Chunk("Employee Name", boldWithUnderline)));
				cell.setBorder(0);
				cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
				childTable.addCell(cell);
				cell = new PdfPCell(new Phrase(new Chunk("Amount", boldWithUnderline)));
				cell.setBorder(0);
				cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
				childTable.addCell(cell);
				
				cell = new PdfPCell(new Phrase(new Chunk("A Card Holders", boldWithUnderline)));
				cell.setBorder(0);
				cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
				childTable.addCell(cell);
				cell = new PdfPCell(new Phrase(new Chunk("")));
				cell.setBorder(0);
				childTable.addCell(cell);
				cell = new PdfPCell(new Phrase(new Chunk("")));
				cell.setBorder(0);
				childTable.addCell(cell);
				
				cell = new PdfPCell(new Phrase(new Chunk(" ")));
				cell.setBorder(0);
				childTable.addCell(cell);
				cell = new PdfPCell(new Phrase(new Chunk(" ")));
				cell.setBorder(0);
				childTable.addCell(cell);
				cell = new PdfPCell(new Phrase(new Chunk(" ")));
				cell.setBorder(0);
				childTable.addCell(cell);
				
				if(record.get("emp"+j) != null) {
					List<HashMap<String, Object>> emps = (List<HashMap<String, Object>>) record.get("emp"+j);
					if(emps != null) {
						for (HashMap<String, Object> hashMap : emps) {
							String cardNumber = String.valueOf(hashMap.get("cardNumber") != null ? hashMap.get("cardNumber") : "");
							cell = new PdfPCell(new Phrase(new Chunk(cardNumber, font)));
							cell.setBorderWidth(1);
							cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
							childTable.addCell(cell);
							cell = new PdfPCell(new Phrase(new Chunk((String)hashMap.get("name"), font)));
							cell.setBorderWidth(1);
							cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
							childTable.addCell(cell);
							cell = new PdfPCell(new Phrase(new Chunk("")));
							cell.setBorderWidth(1);
							cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
							childTable.addCell(cell);
						}
					}
				}
				
				cell = new PdfPCell(new Phrase(new Chunk(" ")));
				cell.setBorder(0);
				childTable.addCell(cell);
				cell = new PdfPCell(new Phrase(new Chunk(" ")));
				cell.setBorder(0);
				childTable.addCell(cell);
				cell = new PdfPCell(new Phrase(new Chunk(" ")));
				cell.setBorder(0);
				childTable.addCell(cell);
				
				cell = new PdfPCell(new Phrase(new Chunk("")));
				cell.setBorder(0);
				childTable.addCell(cell);
				cell = new PdfPCell(new Phrase(new Chunk("Total = € ", bold)));
				cell.setBorder(0);
				cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
				childTable.addCell(cell);
				cell = new PdfPCell(new Phrase(new Chunk(""+record.get("amount"), font)));
				cell.setBorderWidth(1);
				childTable.addCell(cell);
				
				cell = new PdfPCell(new Phrase(new Chunk("")));
				cell.setBorder(0);
				childTable.addCell(cell);
				cell = new PdfPCell(new Phrase(new Chunk("")));
				cell.setBorder(0);
				childTable.addCell(cell);
				cell = new PdfPCell(new Phrase(new Chunk("* Gross Amount", font)));
				cell.setBorder(0);
				childTable.addCell(cell);
				
				document.add(childTable);
				
				table = getTable(1);
				text = "\n\nPLEASE Display in a Staff Working Area.\n\n";
				cell = new PdfPCell(new Phrase(new Chunk(text, font)));
				cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
				cell.setBorder(0);
				table.addCell(cell);
				document.add(table);
				
				table = getTable(1);
				text = "* Please note the Bonus payment is liable for tax and should be processed through your wages system."+
				    		"All appropriate Tax deductions should be deducted on all amounts paid to employees.";
				cell = new PdfPCell(new Phrase(new Chunk(text, font)));
				cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
				cell.setBorder(0);
				table.addCell(cell);
				document.add(table);

				int noOfSpace = (int) record.get("space"+j);
				for(int l=0;l <noOfSpace; l++) {
					table = getTable(1);
					cell = new PdfPCell(new Phrase(new Chunk(" ")));
					cell.setBorder(0);
					table.addCell(cell);
					document.add(table);
				}
			}
		}
	}
}
