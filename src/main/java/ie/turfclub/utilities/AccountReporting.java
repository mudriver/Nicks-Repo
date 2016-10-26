package ie.turfclub.utilities;


import ie.turfclub.accountsReports.service.DisciplinariesService;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JTextField;

import org.springframework.beans.factory.annotation.Autowired;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;

public class AccountReporting {
	
	@Autowired
	DisciplinariesService discipService;
	@Autowired
	MailUtility sendMail;
	

	public static void main(String[] args) throws JRException{
		AccountReporting acc = new AccountReporting();
		
		acc.makeHunterCertsAccountsReport(null, null);
	}
	
	public void makeHunterCertsAccountsReport(Connection conn, String date)
			throws JRException {

		
		Map<String, Object> parameters = new HashMap<String, Object>();

		parameters.put("date", date);
		String[] datePiece = date.split("-");
		parameters.put("dateCreated", datePiece[2] + "-" + datePiece[1] + "-" + datePiece[0]);
	
		//String filename = new File(".").get;

		//filename = filename.substring(0, filename.length() - 2);
		//System.out.println(filename );
		String workingDir = System.getProperty("user.dir");
		   System.out.println("Current working directory : " + workingDir);
		
		JasperPrint print = JasperFillManager.fillReport(
				AccountReporting.class.getResourceAsStream("ie/turfclub/vetReports/resources/jasper/AccountsReportHCerts.jasper"), parameters, conn);

		JasperExportManager.exportReportToPdfFile(print, "accountsReport.pdf");
	}

	public void makeWeeklyDisciplinaryFinesReport(Connection conn, ArrayList<String> emails) {

		Date date = new Date();
		java.text.SimpleDateFormat day = new java.text.SimpleDateFormat("EEE");
		String weekDay = day.format(date);
		int daysToGoBack = 0;
		int calendarTitleDays = 0;
		switch (weekDay) {
		case "Mon":
			daysToGoBack = -1;
			calendarTitleDays = +3;
			break;
		case "Tue":
			daysToGoBack = -2;
			calendarTitleDays = +2;
			break;
		case "Wed":
			daysToGoBack = -3;
			calendarTitleDays = +1;
			break;
		case "Thu":
			daysToGoBack = -4;

			break;
		case "Fri":
			daysToGoBack = -5;
			calendarTitleDays = -1;
			break;
		}

		Calendar cal = GregorianCalendar.getInstance();
		cal.add(Calendar.DAY_OF_YEAR, daysToGoBack);
		Date endDate = cal.getTime();
		int days = 6;
		JTextField daystoGo = new JTextField();
		daystoGo.setText("6");

		cal.add(Calendar.DAY_OF_YEAR, -daysToGoBack); // ALAN CHANGE TO TEN FOR
														// NEW YEAR
		Date startDate = cal.getTime();
		Calendar calendar = GregorianCalendar.getInstance();
		calendar.add(Calendar.DAY_OF_YEAR, calendarTitleDays);
		Date calendarDate = calendar.getTime();

		calendar.add(Calendar.DAY_OF_YEAR, 2);
		Date datePrepared = calendar.getTime();

		System.out.println("Day is" + startDate + " " + endDate);
		java.text.SimpleDateFormat sqlf = new java.text.SimpleDateFormat(
				"yyyy-MM-dd");
		OrdinalNumber ord = new OrdinalNumber();
		String startSqldate = sqlf.format(startDate);
		String endSqldate = sqlf.format(endDate);

		// if a reprint of accounts report is needed put the date range in here.
		// dateFrom = "2014-06-09";
		// dateTo = "2014-06-15";

		@SuppressWarnings("rawtypes")
		Map<String, Object> parameters = new HashMap<String, Object>();
		

		parameters.put("dateFrom", startSqldate);
		parameters.put("dateTo", endSqldate);
		parameters.put("calendarDate", ord.ordinalNo(calendarDate));
		parameters.put("calendarDateSmall", calendarDate);
		parameters.put("datePrepared", datePrepared);
		try {

			JasperPrint jasperPrint = JasperFillManager.fillReport(AccountReporting.class
					.getResourceAsStream("../accountsReports/resources/jasper/AccountsDiscipFinesReport.jasper"),
					parameters, conn);
			JRXlsExporter exporter = new JRXlsExporter();

			exporter.setParameter(JRXlsExporterParameter.JASPER_PRINT,
					jasperPrint);
			exporter.setParameter(
					JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, false);

			exporter.setParameter(JRXlsExporterParameter.IS_IGNORE_CELL_BORDER,
					false);
			exporter.setParameter(
					JRXlsExporterParameter.IS_IMAGE_BORDER_FIX_ENABLED, true);

			exporter.setParameter(JRXlsExporterParameter.IS_IGNORE_GRAPHICS,
					Boolean.FALSE);
			exporter.setParameter(JRXlsExporterParameter.OUTPUT_FILE_NAME,
					"accounts_report.xls");

			exporter.exportReport();

		}

		catch (JRException ex) {

			System.err.println(ex);
			// Logger.getLogger(Enquires.class.getName()).log(Level.SEVERE,
			// null, ex);

		}

		updateAccountsFlagOnRecords(endSqldate);
		
		sendMail.sendEmailWithToRecipients("Accounts Report", "Please find attached Calendar Fines Report", "accounts_report.xls", emails);


		
	}
	
	

	private void updateAccountsFlagOnRecords(String maxDate){
		  discipService.updateAccountsProcessedOnDisciplinaries(maxDate);
	}
	
	
	private class OrdinalNumber {

		public String ordinalNo(Date d) {
			java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(
					"dd MMMM yyyy");

			int value = Integer.parseInt(sdf.format(d).substring(0, 2));
			int hunRem = value % 100;
			int tenRem = value % 10;
			if (hunRem - tenRem == 10) {
				String date = sdf.format(d).substring(0, 2) + "th"
						+ sdf.format(d).substring(2);
				return date;
			}
			switch (tenRem) {
			case 1:
				String date = sdf.format(d).substring(0, 2) + "st"
						+ sdf.format(d).substring(2);
				return date;
			case 2:
				String date1 = sdf.format(d).substring(0, 2) + "nd"
						+ sdf.format(d).substring(2);
				return date1;
			case 3:
				String date2 = sdf.format(d).substring(0, 2) + "rd"
						+ sdf.format(d).substring(2);
				return date2;
			default:
				String date3 = sdf.format(d).substring(0, 2) + "th"
						+ sdf.format(d).substring(2);
				return date3;
			}
		}
	}
	

}
