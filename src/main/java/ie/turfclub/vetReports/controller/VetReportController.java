package ie.turfclub.vetReports.controller;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRHtmlExporter;
import net.sf.jasperreports.engine.export.JRHtmlExporterParameter;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import ie.turfclub.main.pojos.StatusResponse;
import ie.turfclub.main.service.downloads.DownloadService;
import ie.turfclub.main.service.downloads.TokenService;
import ie.turfclub.vetReports.model.VetReportRandomStableInspectionDetail;
import ie.turfclub.vetReports.model.VetreportAlert;
import ie.turfclub.vetReports.model.VetreportHorses;
import ie.turfclub.vetReports.model.VetreportHorsesNotRan;
import ie.turfclub.vetReports.model.VetreportNonrunnerDetail;
import ie.turfclub.vetReports.model.VetreportReportedbyVets;
import ie.turfclub.vetReports.model.VetreportReports;
import ie.turfclub.vetReports.model.VetreportTrainers;
import ie.turfclub.vetReports.service.VetReportsAlertService;
import ie.turfclub.vetReports.service.VetReportsService;

@Controller
@ComponentScan("ie.turfclub.pojos")
@RequestMapping(value = "/vetReports")
public class VetReportController {

	static Logger logger = LoggerFactory.getLogger(VetReportController.class);

	@Autowired
	VetReportsService vetReportService;
	@Autowired
	VetReportsAlertService vetReportAlertService;
	
	@Autowired
	private DownloadService downloadService;
	
	@Autowired
	private TokenService tokenService;
	

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String listOfVetReports(
			Model model,
			@RequestParam(value = "startNo", required = false) Integer startNo,
			@RequestParam(value = "nextPrev", required = false) String nextPrev,
			@RequestParam(value = "type", required = false) String type,
			@RequestParam(value = "sortBy", required = false) String sortBy,
			@ModelAttribute String ascDesc) {

		System.out.println("SortBy:" + sortBy);
		System.out.println("ascDesc:" + ascDesc);
		System.out.println("type:" + type);
		if (sortBy == null) {
			sortBy = "";
		}
		if (ascDesc.equals("")) {
			ascDesc = "asc";
		}
		if(type == null){
			type = "";
		}
		System.out.println("ascDesc:" + ascDesc);
		model.addAttribute("sortBy", sortBy);
		model.addAttribute("ascDesc", ascDesc);
		model.addAttribute("type", type);
		if (startNo == null) {
			startNo = 0;
		} else {
			if (nextPrev.equals("next")) {
				startNo += 30;
			}
			if (nextPrev.equals("prev")) {
				startNo -= 30;
			}
		}
		List<VetreportReports> vetReports;
	
		System.out.println(type);	
		
			logger.info("IN: Vet Reports List" + type + " " + startNo);
			switch (type) {
			case "random":
				
				model.addAttribute("typeTitle", "Random Vet Reports");
				model.addAttribute("type", "RandomStableInspection");
				break;
			case "nonrun":

				model.addAttribute("typeTitle", "Non-Runner Reports");
			
				model.addAttribute("type", "NonRunner");
				break;
			case "track":

				model.addAttribute("typeTitle", "Track Vet Reports");
			
				model.addAttribute("type", "Track");
				break;
			case "prerace":

				
				model.addAttribute("typeTitle", "Pre-Race Vet Reports");
				model.addAttribute("type", "PreRace");
				break;
			default:
				
				model.addAttribute("typeTitle", "All Reports");
				model.addAttribute("type", "");
				break;
			}
		

		model.addAttribute("startNo", startNo);
		model.addAttribute("sortBy", sortBy);

		return "vetreport-list";
	}

	@RequestMapping(value = "/new", method = RequestMethod.GET)
	public String newReport(Model model) {
		logger.info("IN: Vet Reports NEW");
		return "vetreport-new";
	}
	
	@RequestMapping(value = "/reports", method = RequestMethod.GET)
	public String generateReports(Model model) {
		logger.info("IN: Vet Reports report");
		return "vetreport-reports";
	}

	@RequestMapping(value = "/alert", method = RequestMethod.GET)
	public ModelAndView horsesOnAlert() {
		logger.info("IN: Vet Reports horse alert page");
		ModelAndView modelAndView = new ModelAndView("vetreport-alert");
		List<VetreportAlert> alert = vetReportAlertService.getAlert();
		modelAndView.addObject("vetreportAlert", alert);
		modelAndView.addObject("onAlert", new VetreportAlert());
		return modelAndView;
	}

	@RequestMapping(value = "/alert", method = RequestMethod.POST)
	public String putOnAlert(@ModelAttribute VetreportAlert onAlert) {

		VetreportAlert alert = new VetreportAlert();
		if (!onAlert.getAlertTempHorseId().isEmpty()) {
			if (onAlert.getAlertTempHorseId().startsWith("N")) {
				VetreportHorsesNotRan horseNotRan = vetReportService
						.getNotRanYetHorse(Integer.parseInt(onAlert
								.getAlertTempHorseId().replaceAll("N", "")));
				alert.setAlertHorseHasNotRanId(horseNotRan);
			} else if (onAlert.getAlertTempHorseId().startsWith("H")) {
				VetreportHorses horse = vetReportService.getHorse(Integer
						.parseInt(onAlert.getAlertTempHorseId().replaceAll("H",
								"")));
				alert.setAlertHorseId(horse);
			}
		} else {
			VetreportTrainers trainer = vetReportService.getTrainer(onAlert
					.getAlertTrainerId().getTrainerId());
			alert.setAlertTrainerId(trainer);
		}

		vetReportAlertService.putOnAlert(alert);
		return "redirect:/vetReports/alert";

	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView editVetReport(

	@RequestParam(value = "id", required = false) Integer reportNo) {

		logger.info("IN: Vet Reports Edit" + reportNo + (reportNo != null));
		ModelAndView modelAndView = new ModelAndView("vetreport-edit");
		if (reportNo != null) {
			logger.info("IN: Vet Reports Edit:" + reportNo);
			VetreportReports report = vetReportService.getVetReport(reportNo);
			logger.info("IN: Vet Reports:" + report.getReportContent());
			modelAndView.addObject("title", "Edit");
			modelAndView.addObject("vetReport", report);
			modelAndView.addObject("allVets", vetReportService
					.getVetsWithSelectedSetForReportNo(reportNo));
			System.out.println(report.getReportRacecardId().getMeeting());
		} else {
			logger.info("IN: Vet Reports New Type:");
			modelAndView.addObject("title", "New");
			VetreportReports report = new VetreportReports();
			System.out.println(report.getReportRacecardId().getRowId() + " "
					+ report.getReportRandomId().getRandomId());
			modelAndView.addObject("vetReport", report);
			modelAndView.addObject("allVets",
					vetReportService.getVetsWithSelectedSetForReportNo(0));
		}

		return modelAndView;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public ModelAndView saveEditVetReport(
			@ModelAttribute VetreportReports vetReport,
			@RequestParam(value = "selectedVets", required = false) List<Integer> vets) {
		System.out.println("Saving Report Using POST");
		System.out.println(vetReport.getReportRacecardId().getMeeting());
		System.out.println(vetReport.getReportRacecardId().getDateRan());
		System.out.println(vetReport.getReportContent());
		VetreportReports reportToSave;
		// check if new or existing report
		if (vetReport.getReportId() == null) {
			// new report
			logger.info("IN: Saving New Report");
			reportToSave = new VetreportReports();

		} else {
			logger.info("IN: Saving Edit to Report:" + vetReport.getReportId());
			reportToSave = vetReportService.getVetReport(vetReport
					.getReportId());

		}

		// check the racecard detail
		if (!vetReport.getReportType().equals("RandomStableInspection")) {
			logger.info("IN: Saving Racecard INfo");
			reportToSave.setReportRacecardId(vetReportService
					.getRacecard(vetReport.getReportRacecardId().getRowId()));
			reportToSave.setReportRandomId(null);
			if (vetReport.getReportType().equals("NonRunner")) {
				logger.info("IN: Saving Non Run INfo");
				if(reportToSave.getReportNonRunnerId() == null){
					reportToSave.setReportNonRunnerId(new VetreportNonrunnerDetail());
				}
				reportToSave.getReportNonRunnerId().setNonrunFine(vetReport.getReportNonRunnerId().isNonrunFine());
				reportToSave.getReportNonRunnerId().setNonrunLate(vetReport.getReportNonRunnerId().isNonrunLate());
				reportToSave.getReportNonRunnerId().setNonrunRefunded(vetReport.getReportNonRunnerId().isNonrunRefunded());
			}
			else{
				reportToSave.setReportNonRunnerId(null);
			}
			
		} else {
			logger.info("IN: Saving Non Random Info");
			reportToSave.setReportRacecardId(null);
			reportToSave.setReportNonRunnerId(null);
			if (reportToSave.getReportRandomId() == null) {
				reportToSave
						.setReportRandomId(new VetReportRandomStableInspectionDetail());
			}
			if (vetReport.getReportRandomId().getRandomNotRanYetTempId()
					.contains("N")) {
				int id = Integer.parseInt(vetReport.getReportRandomId()
						.getRandomNotRanYetTempId().replace("N", ""));
				reportToSave.getReportRandomId().setRandomNotRanYetId(
						vetReportService.getNotRanYetHorse(id));
			} else if (vetReport.getReportRandomId().getRandomNotRanYetTempId()
					.contains("H")) {
				int id = Integer.parseInt(vetReport.getReportRandomId()
						.getRandomNotRanYetTempId().replace("H", ""));
				reportToSave.getReportRandomId().setRandomHorseId(
						vetReportService.getHorse(id));
			}
			reportToSave.getReportRandomId().setRandomInpsectionDate(vetReport.getHtmlDateFormatted());
			reportToSave.getReportRandomId().setRandomTrainerId(
					vetReportService.getTrainer(vetReport.getReportRandomId()
							.getRandomTrainerId().getTrainerId()));

		}
		
		
		
		reportToSave.setReportType(vetReport.getReportType());
		reportToSave.setReportContent(vetReport.getReportContent());
		reportToSave.setReportMisc(vetReport.getReportMisc());
		
	   
		
		
		int reportId = vetReportService.saveVetReport(reportToSave);
		// check the vet detail and save
		if (!vetReport.getReportType().equals("NonRunner")) {

			if (vetReport.getReportId() != null) {
				List<VetreportReportedbyVets> reportedByVets = vetReportService
						.getVetsReportBy(vetReport.getReportId());

				for (VetreportReportedbyVets reportedByVet : reportedByVets) {

					vetReportService.deleteVetReportedBy(reportedByVet);
				}
			}

			VetreportReportedbyVets reportedByVet;
			for (int vet : vets) {

				System.out.println(vet);
				reportedByVet = new VetreportReportedbyVets();
				reportedByVet.setReportedbyReportId(reportId);
				reportedByVet.setReportedbyVetId(vet);
				vetReportService.saveVetReportedBy(reportedByVet);

			}

		}
		

		ModelAndView modelAndView = new ModelAndView("redirect:/vetReports/");

		return modelAndView;
	}

	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView deleteHunterCert(
			@RequestParam(value = "id", required = true) Integer id,

			@RequestParam(value = "phase", required = true) String phase) {

		// get the report here using the service
		ModelAndView modelAndView = null;

		if (phase.equals("stage")) {
			modelAndView = new ModelAndView("huntercert-delete");
			// get the report and return it here
			// String message = "Hunter Cert " + hunterCert.getId()
			// + " queued for display.";
			// modelAndView.addObject("hunterCertDTO", hunterCertDTO);
			// modelAndView.addObject("message", message);
		}

		if (phase.equals("confirm")) {
			modelAndView = new ModelAndView("redirect:/hunterCert/list");
			// delete report here
			// String message = "Hunter Cert " + hunterCert.getId() +
			// " was successfully deleted";
			// modelAndView.addObject("message", message);
		}

		if (phase.equals("cancel")) {
			modelAndView = new ModelAndView("redirect:/hunterCert/list");
			String message = "HunterCert delete was cancelled.";
			modelAndView.addObject("message", message);
		}

		return modelAndView;

	}

	@RequestMapping(value = "/offAlert", method = RequestMethod.GET)
	public ModelAndView alertRemove(
			@RequestParam(value = "id", required = true) Integer id,
			@RequestParam(value = "alertName", required = true) String alertName,
			@RequestParam(value = "phase", required = true) String phase) {

		ModelAndView modelAndView = new ModelAndView("vetreport-alert");

		if (phase.equals("stage")) {
			logger.info("Vet Reports OFF Alert: " + alertName + id);
			modelAndView.addObject("message",
					"Are you sure you want to remove " + alertName
							+ " from the alert!");
			modelAndView.addObject("stage", "stage");
			modelAndView.addObject("id", id);
			modelAndView.addObject("alertName", alertName);
		}
		if (phase.equals("confirm")) {

			logger.info("Vet Reports OFF Alert: " + id);
			vetReportAlertService.offAlert(vetReportService.getAlert(id));
			modelAndView.addObject("message", "You have successfully removed "
					+ alertName + " from the alert!");

		}
		if (phase.equals("cancel")) {

			modelAndView.addObject("message", "Remove " + alertName
					+ " cancelled!");
			modelAndView.addObject("stage", "cancel");
			modelAndView.addObject("alertName", alertName);
		}

		List<VetreportAlert> alert = vetReportAlertService.getAlert();
		modelAndView.addObject("vetreportAlert", alert);
		modelAndView.addObject("onAlert", new VetreportAlert());
		return modelAndView;

	}

	
	@RequestMapping(value="/getpdf", method=RequestMethod.POST)
	public ResponseEntity<byte[]> getPDF(@RequestParam(value = "meeting", required = false) String meeting,
	        @RequestParam(value = "date", required = false) String date) {
	    // json => emp


	
	

	    // retrieve contents of "C:/tmp/report.pdf"
	    byte[] contents = null;

	    HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.parseMediaType("application/pdf"));
	    String filename = "output.pdf";
	    headers.setContentDispositionFormData(filename, filename);
	    headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
	    ResponseEntity<byte[]> response = new ResponseEntity<byte[]>(contents, headers, HttpStatus.OK);
	    return response;
	}
	
	

	
	

	@RequestMapping(value = "/svlDownload", method = RequestMethod.GET)
	public void generatePdfDownload(@RequestParam String type,
			@RequestParam String token, 
			HttpServletResponse response, @RequestParam(value = "meeting", required = false) String meeting,
	        @RequestParam(value = "date", required = false) String date) {
		SimpleDateFormat dateSQL = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat dateNorm = new SimpleDateFormat("dd/MM/yyyy");
		System.out.println(date + meeting);
		
		try {
			date = dateSQL.format(dateNorm.parse(date));
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		// Setup my data connection
    	try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		java.sql.Connection conn = null;
		try {
			conn = DriverManager
					.getConnection("jdbc:mysql://127.0.0.1:3000/vetreports?user=root&password=password");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        

    	BufferedImage image = null;
		try {
			image = ImageIO.read(this.getClass().getResourceAsStream("/images/tclogo.jpg"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("image error");
			e.printStackTrace();
		}
		HashMap<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.put("meeting", meeting);
		parameterMap.put("date", date);
		parameterMap.put("tclogo", image);
		parameterMap.put("REPORT_CONNECTION", conn);
		logger.info("rendering the pdf view");
		downloadService.download("/jasper/VetReports.jasper", parameterMap, null, conn, token, response);

	}
	
	
	
	
	@RequestMapping(value = "/svlPdf", method = RequestMethod.GET)
	public ModelAndView generatePdfSVLReport(ModelAndView modelAndView, @RequestParam(value = "meeting", required = false) String meeting,
	        @RequestParam(value = "date", required = false) String date) {
		SimpleDateFormat dateSQL = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat dateNorm = new SimpleDateFormat("dd/MM/yyyy");
		try {
			date = dateSQL.format(dateNorm.parse(date));
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		// Setup my data connection
    	try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		java.sql.Connection conn = null;
		try {
			conn = DriverManager
					.getConnection("jdbc:mysql://127.0.0.1:3000/vetreports?user=root&password=password");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        

    	BufferedImage image = null;
		try {
			image = ImageIO.read(this.getClass().getResourceAsStream("/images/tclogo.jpg"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("image error");
			e.printStackTrace();
		}
		
	Map<String, Object> parameterMap = new HashMap<String, Object>();
	parameterMap.put("meeting", meeting);
	parameterMap.put("date", date);
	parameterMap.put("tclogo", image);
	parameterMap.put("REPORT_CONNECTION", conn);
	logger.info("rendering the pdf view");
	modelAndView = new ModelAndView("svlListPdf", parameterMap);
	return modelAndView;
	}
	
	@RequestMapping(value = "/svlExcel", method = RequestMethod.GET)
	public ModelAndView generateExcelSVLReport(ModelAndView modelAndView) {

	Map<String, Object> parameterMap = new HashMap<String, Object>();
	logger.info("rendering the excel view");
	modelAndView = new ModelAndView("svlListXls", parameterMap);
	return modelAndView;
	}
	
	@RequestMapping(value = "/svlHtml", method = RequestMethod.GET)
	public ModelAndView generateHTMLSVLReport(ModelAndView modelAndView) {

	Map<String, Object> parameterMap = new HashMap<String, Object>();
	logger.info("rendering the excel view");
	modelAndView = new ModelAndView("svlListHtml", parameterMap);
	return modelAndView;
	}
	
	@RequestMapping(value = "/svlList", method = RequestMethod.GET)
	public ModelAndView generateSVLReport(ModelAndView modelAndView) {

	Map<String, Object> parameterMap = new HashMap<String, Object>();
	logger.info("rendering the excel view");
	modelAndView = new ModelAndView("svlListHtml", parameterMap);
	return modelAndView;
	}
	
	
	
	
	
	
	@RequestMapping(value = "/svlreport", method = RequestMethod.POST)
	public ModelAndView htmlReport(@RequestParam(value = "meeting", required = false) String meeting,
	        @RequestParam(value = "date", required = false) String date) {

		System.out.println(date);
		SimpleDateFormat dateSQL = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat dateNorm = new SimpleDateFormat("dd/MM/yyyy");
		try {
			date = dateSQL.format(dateNorm.parse(date));
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		ModelAndView model = new ModelAndView();
	    try {

	        // Setup my data connection
	    	try {
				Class.forName("com.mysql.jdbc.Driver");
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			java.sql.Connection conn = null;
			try {
				conn = DriverManager
						.getConnection("jdbc:mysql://127.0.0.1:3000/vetreports?user=root&password=password");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        

	    	BufferedImage image = null;
			try {
				image = ImageIO.read(this.getClass().getResourceAsStream("/images/tclogo.jpg"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("image error");
				e.printStackTrace();
			}
	    	Map<String, Object> parameters = new HashMap<String, Object>();
			
			System.out.println(meeting + date);
			parameters.put("meeting", meeting);
			parameters.put("date", date);
			parameters.put("tclogo", image);
			JasperPrint print = null;
			try {
				print = JasperFillManager.fillReport(
						this.getClass().getResourceAsStream("/jasper/VetReports.jasper"), parameters, conn);
				
			} catch (JRException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

	        

	        // Create report exporter to be in Html
	        JRExporter exporter = new JRHtmlExporter();

	        // Create string buffer to store completed report
	        StringBuffer sb = new StringBuffer();

	        // Setup report, no header, no footer, no images for layout
	        exporter.setParameter(JRHtmlExporterParameter.HTML_HEADER, "");
	        exporter.setParameter(JRHtmlExporterParameter.HTML_FOOTER, "");
	        exporter.setParameter(JRHtmlExporterParameter.IS_USING_IMAGES_TO_ALIGN, Boolean.FALSE);

	        // When report is exported send to string buffer
	        exporter.setParameter(JRExporterParameter.OUTPUT_STRING_BUFFER, sb);
	        exporter.setParameter(JRExporterParameter.JASPER_PRINT, print);

	        // Export the report, store to sb
	        exporter.exportReport();

	        // Use Jsoup to clean the report table html to output to browser
	        Whitelist allowedHtml = new Whitelist();
	        allowedHtml.addTags("table", "tr", "td", "span");
	        allowedHtml.addTags("table", "style", "cellpadding", "cellspacing", "border", "bgcolor");
	        allowedHtml.addAttributes("tr", "valign");
	        allowedHtml.addAttributes("td", "colspan", "style");
	        allowedHtml.addAttributes("span", "style");
	        String html = Jsoup.clean(sb.toString(), allowedHtml);

	        System.out.println(html);
	        // Add report to map
	        model.addObject("report", html);

	    } catch (Exception e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
	    }
	    model.setViewName("vetreport-reportPreview");
	    return model;
	}

	@RequestMapping(value="/download/progress")
	public @ResponseBody StatusResponse checkDownloadProgress(@RequestParam String token) {
		return new StatusResponse(true, tokenService.check(token));
	}
	
	@RequestMapping(value="/download/token")
	public @ResponseBody StatusResponse getDownloadToken() {
		return new StatusResponse(true, tokenService.generate());
	}
	

	
	/*
	 * 
	 * // service to get huntercert objects from the database
	 * 
	 * @Autowired private HunterCertService hunterCertService; // service to get
	 * huntercert objects from the database
	 * 
	 * @Autowired private OwnerHandlerService ownerHandlerService; // service to
	 * get hunt objects from the database
	 * 
	 * @Autowired private HuntService huntService; // store the huntercert
	 * objects as they are completed
	 * 
	 * @Autowired
	 * 
	 * @Transient private HunterCertsBasket hCertBasket; // store the huntercert
	 * objects as they are completed
	 * 
	 * @Autowired private VaccinationService vaccinationService; // get messages
	 * from properties file
	 * 
	 * @Autowired private MessageSource messageSource; private Authentication
	 * auth;
	 * 
	 * 
	 * 
	 * // STEP 1a - Confirm the handler details
	 * 
	 * @RequestMapping(value = "/handlerDetail", method = RequestMethod.GET)
	 * public String handlerDetails(Model model) {
	 * 
	 * logger.info("HANDLER : Getting handler info & initialize cart");
	 * 
	 * String url = "huntercert-handlerDetail"; String redirectUrl =
	 * checkCompletion(url); if (!url.equals(redirectUrl)) { return redirectUrl;
	 * } else{ HunterCertItem item = new HunterCertItem();
	 * hCertBasket.clearUnfinishedItems(); hCertBasket.setCurrentItem(item);
	 * 
	 * auth = SecurityContextHolder.getContext().getAuthentication(); //
	 * model.addAttribute("username", auth.getName()); OwnerHandler handler =
	 * ownerHandlerService.getOwnerHandler(auth .getName());
	 * 
	 * hCertBasket.getCurrentItem().setHandler(handler);
	 * 
	 * model.addAttribute("handler", handler); //
	 * System.out.println(checkCompletion("huntercert-handlerDetail")); return
	 * "huntercert-handlerDetail"; }
	 * 
	 * 
	 * 
	 * }
	 * 
	 * // STEP 2A - Select A Horse
	 * 
	 * @RequestMapping(value = "/horseDetail", method = RequestMethod.GET)
	 * public String selectHorse(Model model,
	 * 
	 * @RequestParam(value = "add", required = false) String add) {
	 * 
	 * 
	 * 
	 * logger.info("IN: Hunter Cert/horsedetail-GET"); // if the user has
	 * requested to add another item to the basket // create a new item and set
	 * it as the current item //if the add parameter is set add a new huntercert
	 * to the basket if (add != null) {
	 * logger.info("IN: Hunter Cert/ SETUP new Handler & item in basket"); auth
	 * = SecurityContextHolder.getContext().getAuthentication(); //
	 * model.addAttribute("username", auth.getName()); OwnerHandler handler =
	 * ownerHandlerService.getOwnerHandler(auth .getName()); HunterCertItem item
	 * = new HunterCertItem(); item.setHandler(handler);
	 * hCertBasket.setCurrentItem(item); }
	 * 
	 * // check that correct path to url has been taken otherwise redirect to //
	 * proper step String url = "huntercert-horseDetail"; String redirectUrl =
	 * checkCompletion("huntercert-horseDetail"); if (!url.equals(redirectUrl))
	 * { return redirectUrl; }
	 * 
	 * else {
	 * 
	 * 
	 * 
	 * // initialize the model if it doesn't exist // ****** It will exist when
	 * error checking occurs only!! if (!model.containsAttribute("horseWithID"))
	 * { // logger.info("Adding Horse object to model"); HorseWithID horse = new
	 * HorseWithID(); model.addAttribute("horseWithID", horse); }
	 * 
	 * model.addAttribute("username", auth.getName()); return
	 * "huntercert-horseDetail"; }
	 * 
	 * }
	 * 
	 * // STEP 2B Validated Horse
	 * 
	 * @RequestMapping(value = "/horseDetail", method = RequestMethod.POST)
	 * public String getHorseDetail(@ModelAttribute HunterCert hunterCert,
	 * 
	 * @Valid @ModelAttribute HorseWithID horseWithID, BindingResult result,
	 * RedirectAttributes redirectAttrs) {
	 * 
	 * if (result.hasErrors()) { logger.info("hunterCert-add error: ");
	 * redirectAttrs.addFlashAttribute(
	 * "org.springframework.validation.BindingResult.horseWithID", result);
	 * redirectAttrs.addFlashAttribute("horseWithID", horseWithID); return
	 * "redirect:/hunterCert/horseDetail"; } else
	 * if(basketContainsHCert(horseWithID.getHorse_name())){
	 * redirectAttrs.addFlashAttribute("basketError",
	 * "Selected Horse Already in Basket!");
	 * redirectAttrs.addFlashAttribute("horseWithID", horseWithID); return
	 * "redirect:/hunterCert/horseDetail"; } else {
	 * 
	 * hunterCert = hunterCertService.getHunterCert(Integer
	 * .parseInt(horseWithID.getId()));
	 * redirectAttrs.addFlashAttribute("hunterCert", hunterCert);
	 * redirectAttrs.addFlashAttribute("horseWithID", horseWithID); return
	 * "redirect:/hunterCert/horseDetail";
	 * 
	 * } }
	 * 
	 * // STEP 2C Confirm & Save Horse Details
	 * 
	 * @RequestMapping(value = "/horseDetailConfirm", method =
	 * RequestMethod.POST) public String saveHunterCert(@ModelAttribute
	 * HunterCert hunterCert) { logger.info("IN: horse details confirm"); //
	 * Save the selected huntercert object to the basket hunterCert =
	 * hunterCertService.getHunterCert(hunterCert.getId());
	 * hCertBasket.getCurrentItem().setCert(hunterCert);
	 * 
	 * return "redirect:/hunterCert/ownerDetail"; }
	 * 
	 * // STEP 3a Owner Details
	 * 
	 * @RequestMapping(value = "/ownerDetail", method = RequestMethod.GET)
	 * public String ownerDetails(Model model) { logger.info("IN: Owner Get");
	 * 
	 * if (!model.containsAttribute("owner")) {
	 * 
	 * OwnerHandler owner = new OwnerHandler(); model.addAttribute("owner",
	 * owner); }
	 * 
	 * return checkCompletion("huntercert-ownerDetail"); }
	 * 
	 * // STEP 3B Validate OwnerAccount
	 * 
	 * @RequestMapping(value = "/ownerDetail", method = RequestMethod.POST)
	 * public String getOwnerDetail(
	 * 
	 * @Valid @ModelAttribute OwnerHandler ownerHandler, BindingResult result,
	 * RedirectAttributes redirectAttrs) {
	 * 
	 * // logger.info("Basket" + //
	 * hCertBasket.getCurrentItem().getHandler().getOwner_handler_account_no());
	 * 
	 * if (result.hasErrors()) { logger.info("hunter owner-add error: ");
	 * redirectAttrs.addFlashAttribute(
	 * "org.springframework.validation.BindingResult.owner", result);
	 * redirectAttrs.addFlashAttribute("owner", ownerHandler); return
	 * "redirect:/hunterCert/ownerDetail"; } else {
	 * 
	 * ownerHandler = ownerHandlerService.getOwner(ownerHandler
	 * .getOwner_handler_account_no());
	 * System.out.println(ownerHandler.getOwner_handler_name());
	 * redirectAttrs.addFlashAttribute("owner", ownerHandler);
	 * redirectAttrs.addFlashAttribute("ownerDetail", ownerHandler); return
	 * "redirect:/hunterCert/ownerDetail";
	 * 
	 * } }
	 * 
	 * // STEP 3C Confirm & Save Owner Details
	 * 
	 * @RequestMapping(value = "/ownerDetailConfirm", method =
	 * RequestMethod.POST) public String saveOwner(@ModelAttribute OwnerHandler
	 * owner) { logger.info("IN:owner details confirm"); // Save the selected
	 * owner to the basket
	 * 
	 * owner = ownerHandlerService.getOwner(owner
	 * .getOwner_handler_account_no());
	 * hCertBasket.getCurrentItem().setOwner(owner);
	 * 
	 * return "redirect:/hunterCert/huntDetail"; }
	 * 
	 * // STEP 4a Hunt Detail
	 * 
	 * @RequestMapping(value = "/huntDetail", method = RequestMethod.GET) public
	 * String huntDetails(Model model) {
	 * logger.info("IN: Hunter Cert/huntDetail GET");
	 * 
	 * if (!model.containsAttribute("hunt")) { Hunt hunt = new Hunt();
	 * model.addAttribute("hunt", hunt); } return
	 * checkCompletion("huntercert-huntDetail"); }
	 * 
	 * // STEP 4b Validated and Save Hunt Detail
	 * 
	 * @RequestMapping(value = "/huntDetail", method = RequestMethod.POST)
	 * public String saveHuntDetails(@Valid @ModelAttribute Hunt hunt,
	 * BindingResult result, RedirectAttributes redirectAttrs) {
	 * logger.info("IN: Hunter Cert/huntDetail POST"); // validate the hunt
	 * object if (result.hasErrors()) { logger.info("hunterCert-add error: ");
	 * redirectAttrs .addFlashAttribute(
	 * "org.springframework.validation.BindingResult.hunt", result);
	 * redirectAttrs.addFlashAttribute("hunt", hunt); return
	 * "redirect:/hunterCert/huntDetail"; } else { hunt =
	 * huntService.getHunt(hunt.getId()); // Save the selected hunt object to
	 * the basket hCertBasket.getCurrentItem().setHunt(hunt);
	 * 
	 * return "redirect:/hunterCert/vaccinations"; } }
	 * 
	 * // Step 5A - Show Vaccinations And Enter New
	 * 
	 * @RequestMapping(value = "/vaccinations", method = RequestMethod.GET)
	 * public String vaccinationDetails(Model model) {
	 * logger.info("IN: Hunter Cert/ Vaccinations GET");
	 * 
	 * String url = "huntercert-vaccinations"; String redirectUrl =
	 * checkCompletion(url); if (!url.equals(redirectUrl)) { return redirectUrl;
	 * } else { List<Vaccination> vaccinations = vaccinationService
	 * .getVaccinations(hCertBasket.getCurrentItem().getCert() .getId()); // add
	 * existing horsename to the model model.addAttribute("horsename",
	 * hCertBasket.getCurrentItem() .getCert().getHorse_name()); // add existing
	 * vaccinations to the model model.addAttribute("vaccinations",
	 * vaccinations); // add three blank vaccinations to the model with hcert id
	 * 
	 * if (!model.containsAttribute("inputForm")) { VaccinationForm form = new
	 * VaccinationForm(); String hcertId =
	 * hCertBasket.getCurrentItem().getCert().getId() .toString(); for
	 * (Vaccination vac : form.getVaccinations()) {
	 * vac.setVac_hcert_id(hcertId); } model.addAttribute("inputForm", form); }
	 * 
	 * return "huntercert-vaccinations"; }
	 * 
	 * }
	 * 
	 * // Step 5B - Save Vaccinations
	 * 
	 * @RequestMapping(value = "/addVaccinations", method = RequestMethod.POST)
	 * public String saveVaccinationDetails(
	 * 
	 * @Valid @ModelAttribute VaccinationForm inputForm, BindingResult result,
	 * RedirectAttributes redirectAttrs) {
	 * 
	 * // update the basket item to reflect that the condition were accepted on
	 * // this item
	 * 
	 * if (result.hasErrors()) {
	 * 
	 * logger.info("Vaccination Errors" + result.getObjectName());
	 * List<ObjectError> errors = result.getAllErrors(); for (int i = 0; i <
	 * result.getErrorCount(); i++) {
	 * 
	 * // if there are too many attempts log the person out if
	 * (errors.get(i).toString() .contains("Too many vaccination attempts!!")) {
	 * return "redirect:/hunterCert/review?ERROR=" +
	 * "Too many vaccination attempts!! The horse " + StringToTitleCase
	 * .convertString(hCertBasket.getCurrentItem() .getCert().getHorse_name()) +
	 * " is now locked - Please contact the Turf Club to unblock"; } }
	 * redirectAttrs.addFlashAttribute(
	 * "org.springframework.validation.BindingResult.inputForm", result);
	 * redirectAttrs.addFlashAttribute("inputForm", inputForm); return
	 * "redirect:/hunterCert/vaccinations"; }
	 * 
	 * // save the vaccinations to the basket else {
	 * hCertBasket.getCurrentItem().setVaccinations(
	 * inputForm.getVaccinations()); return "redirect:/hunterCert/confirm"; }
	 * 
	 * }
	 * 
	 * // Step 6 Confirm Conditions for Current Hunter Cert
	 * 
	 * @RequestMapping(value = "/confirm", method = RequestMethod.GET) public
	 * String listOfCondtions(Model model) {
	 * logger.info("IN: Hunter Cert/conditions GET");
	 * 
	 * // add logic here to get hunt price if needed from huntService
	 * model.addAttribute("huntPrice", "60"); if
	 * (!model.containsAttribute("hunterCert")) {
	 * logger.info("Adding HunterCert object to model"); HunterCert hunterCert =
	 * new HunterCert(); model.addAttribute("hunterCert", hunterCert); } return
	 * checkCompletion("huntercert-conditions"); }
	 * 
	 * // Step 6B - Save conditions
	 * 
	 * @RequestMapping(value = "/conditionsAccept", method = RequestMethod.POST)
	 * public String saveConditions(@RequestParam(value = "no", required =
	 * false) String no,
	 * 
	 * @RequestParam(value = "yes", required = false) String yes,
	 * 
	 * @RequestParam(value = "agree", required = false) String accept) {
	 * 
	 * // update the basket item to reflect that the condition were accepted on
	 * // this item
	 * 
	 * // System.out.println(accept); if (accept!=null &&
	 * accept.equals("agree")) {
	 * hCertBasket.getCurrentItem().setConditionsAccepted(true);
	 * 
	 * if(no!=null && no.equals("no")){
	 * hCertBasket.getCurrentItem().setHasRanOutsideIreland(false); }
	 * if(yes!=null && yes.equals("yes")){
	 * hCertBasket.getCurrentItem().setHasRanOutsideIreland(true); }
	 * 
	 * return "redirect:/hunterCert/review"; } else { return
	 * "redirect:/hunterCert/conditions"; } // Save the selected hunt object to
	 * the basket
	 * 
	 * }
	 * 
	 * // Step 7 Review Hunter Certs in Basket
	 * 
	 * @RequestMapping(value = "/review", method = RequestMethod.GET) public
	 * String listOfHunterCerts(Model model, @RequestParam(value = "ERROR",
	 * required = false) String error) {
	 * logger.info("Adding Current Item to Basket and Display Basket"); String
	 * url = "huntercert-review-payment"; String redirectUrl =
	 * checkCompletion(url); if (!url.equals(redirectUrl)) { return redirectUrl;
	 * }
	 * 
	 * else { // if the current item is not empty add to basket and empty
	 * current item if (hCertBasket.getCurrentItem() != null &&
	 * !hCertBasket.getCurrentItem().isUnFinished()) {
	 * hCertBasket.addCurrentItemToBasket(); hCertBasket.setCurrentItem(null); }
	 * 
	 * List<HunterCertItem> items = hCertBasket.getHunterCerts();
	 * System.out.println(error); if(error != null){ model.addAttribute("error",
	 * error); } if (!items.isEmpty() && !hCertBasket.hasUnfinishedItems()) {
	 * 
	 * PaymentObj obj = BasketToPaymentObjConvertor
	 * .convertBasketToPaymentObj(hCertBasket); model.addAttribute("payObj",
	 * obj); model.addAttribute("huntercerts", items);
	 * model.addAttribute("totalPrice", (50 * items.size())); } else {
	 * model.addAttribute("payObj", null); model.addAttribute("huntercerts",
	 * null); model.addAttribute("totalPrice", null); }
	 * 
	 * return "huntercert-review-payment"; }
	 * 
	 * 
	 * 
	 * }
	 * 
	 * // Step 8 Payment
	 * 
	 * @RequestMapping(value = "/receipt", method = RequestMethod.GET) public
	 * String payment(@RequestParam(value = "TERMINALID", required = true)
	 * String terminalId ,@RequestParam(value = "ORDERID", required = true)
	 * String orderId ,@RequestParam(value = "AMOUNT", required = true) String
	 * amount ,@RequestParam(value = "DATETIME", required = true) String
	 * dateTime ,@RequestParam(value = "RESPONSECODE", required = true) String
	 * responseCode ,@RequestParam(value = "RESPONSETEXT", required = true)
	 * String responseText ,@RequestParam(value = "HASH", required = true)
	 * String hash, Model model){ logger.info("Processing Paid Items Basket");
	 * 
	 * 
	 * 
	 * 
	 * 
	 * String returnHash; String original = terminalId + orderId + amount +
	 * dateTime + responseCode + responseText + "1790testpasswordlished" ;
	 * MessageDigest md = null; try { md = MessageDigest.getInstance("MD5"); }
	 * catch (NoSuchAlgorithmException e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); } md.update(original.getBytes()); byte[] digest =
	 * md.digest(); StringBuffer sb = new StringBuffer(); for (byte b : digest)
	 * { sb.append(String.format("%02x", b & 0xff)); }
	 * 
	 * //System.out.println("original:" + original);
	 * //.out.println("digested(hex):" + sb.toString()); returnHash =
	 * sb.toString();
	 * 
	 * 
	 * 
	 * //if the payment is successful by verifying the hash then //set each item
	 * in the cart to payed and update the details SimpleDateFormat sql = new
	 * SimpleDateFormat("yyyy-MM-dd"); SimpleDateFormat normal = new
	 * SimpleDateFormat("dd/MM/yyyy"); if(responseCode.equals("A") &&
	 * returnHash.equals(hash)){ //process cart items List<HunterCert>
	 * updatedHunterCerts = new ArrayList<>(); for(HunterCertItem item :
	 * hCertBasket.getHunterCerts()){ HunterCert cert =
	 * item.getUpdatedHunterCert(); HunterCert updatecert =
	 * hunterCertService.getHunterCert(cert.getId());
	 * updatecert.setHunt_id(huntService.getHunt(cert.getHunt_id().getId()));
	 * updatecert.setOwner_id(ownerHandlerService.getOwner(cert.getOwner_id().
	 * getOwner_handler_account_no()));
	 * updatecert.setHandler_id(ownerHandlerService
	 * .getOwnerHandler(cert.getHandler_id().getOwner_handler_account_no()));
	 * updatecert.setRan_out_of_ireland(cert.isRan_out_of_ireland());
	 * updatecert.setStatus("PAID"); updatecert.setHcert_order_id(orderId);
	 * hunterCertService.updateHunterCert(updatecert);
	 * updatedHunterCerts.add(item.getCert()); List<Vaccination> vacs =
	 * item.getVaccinations(); for(Vaccination vac : vacs){
	 * System.out.println(vac.getVac_date());
	 * System.out.println(vac.getVac_type());
	 * 
	 * if(vac.getVac_type() != null){ try {
	 * vac.setVac_date(sql.format(normal.parse(vac.getVac_date()))); } catch
	 * (ParseException e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); } vac.setVac_hcert_id(cert.getId().toString());
	 * vaccinationService.saveVaccination(vac); }
	 * 
	 * } }
	 * 
	 * //return huntercerts from cart items to the receipt //and the unique
	 * transaction orderId model.addAttribute("huntercerts",
	 * updatedHunterCerts); model.addAttribute("receiptNo", orderId);
	 * 
	 * 
	 * }
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * return "huntercert-payment-receipt"; }
	 * 
	 * // show unfinshed item if one exists
	 * 
	 * @RequestMapping(value = "/unFinishedItem", method = RequestMethod.GET)
	 * public String unFinishedHunterCert(
	 * 
	 * @RequestParam(value = "continue", required = false) String cont) {
	 * 
	 * if (cont == null) { return "huntercert-unFinished"; } else if
	 * (cont.equals("continue")) { return
	 * hCertBasket.getCurrentItem().getCurrentStep(); } else if
	 * (cont.equals("start")) { hCertBasket.setCurrentItem(null); return
	 * "redirect:/hunterCert/handlerDetail"; } else { return
	 * "redirect:/hunterCert/handlerDetail"; }
	 * 
	 * }
	 * 
	 * // DELETE Function for HunterCert Basket
	 * 
	 * @RequestMapping(value = "/delete", method = RequestMethod.GET) public
	 * ModelAndView deleteHunterCert(
	 * 
	 * @RequestParam(value = "horseName", required = true) String name,
	 * 
	 * @RequestParam(value = "yob", required = true) String yob,
	 * 
	 * @RequestParam(value = "phase", required = true) String phase) {
	 * 
	 * ModelAndView modelAndView = null;
	 * 
	 * if (phase.equals("stage")) { modelAndView = new
	 * ModelAndView("huntercert-delete");
	 * 
	 * modelAndView.addObject("horse_name", name); modelAndView.addObject("yob",
	 * yob);
	 * 
	 * }
	 * 
	 * if (phase.equals("confirm")) { modelAndView = new
	 * ModelAndView("redirect:/hunterCert/review");
	 * 
	 * 
	 * List<HunterCertItem> items = hCertBasket.getHunterCerts();
	 * hCertBasket.clearBasket(); for (HunterCertItem item : items) {
	 * System.out.println(name + item.getCert().getHorse_name()); if
	 * (!item.getCert().getHorse_name().toLowerCase()
	 * .equals(name.toLowerCase())) { System.out.println("Adding" +
	 * item.getCert().getHorse_name()); hCertBasket.setCurrentItem(item);
	 * hCertBasket.addCurrentItemToBasket(); hCertBasket.setCurrentItem(null);
	 * 
	 * }
	 * 
	 * }
	 * 
	 * 
	 * }
	 * 
	 * if (phase.equals("cancel")) { modelAndView = new
	 * ModelAndView("redirect:/hunterCert/review");
	 * 
	 * }
	 * 
	 * return modelAndView;
	 * 
	 * }
	 * 
	 * 
	 * 
	 * 
	 * @RequestMapping(value = "/", method = RequestMethod.GET) public String
	 * checkCompletion(String url) {
	 * 
	 * // if there is no huntercert basket initialized return to step 1 if ((url
	 * == null) || url.contains("huntercert-handlerDetail") ||
	 * url.contains("huntercert-review-payment")) {
	 * 
	 * System.out.println(hCertBasket.hasUnfinishedItems()); if
	 * (hCertBasket.hasUnfinishedItems()) { hCertBasket.clearBasket(); if(url ==
	 * null){ return "redirect:/hunterCert/handlerDetail"; } else{ return url; }
	 * 
	 * 
	 * } else { if(url == null){ return "redirect:/hunterCert/handlerDetail"; }
	 * else{ return url; } }
	 * 
	 * } else if (hCertBasket.getCurrentItem() != null &&
	 * (url.contains("huntercert-handlerDetail") ||
	 * url.contains("huntercert-review-payment"))) { if
	 * (hCertBasket.getCurrentItem().isUnFinished()) { return
	 * "redirect:/hunterCert/unFinishedItem"; } else { return url; }
	 * 
	 * }
	 * 
	 * // if the handler has not been confirmed redirect to step 1 else if
	 * (!hCertBasket.getCurrentItem().hasHandler() &&
	 * !url.contains("huntercert-handlerDetail")) { return
	 * "redirect:/hunterCert/handlerDetail"; } // if the huntercert for the
	 * currentItem has not been initialized return // to horse select else if
	 * (!hCertBasket.getCurrentItem().hasCert() &&
	 * !url.contains("huntercert-horseDetail") &&
	 * !url.contains("huntercert-handlerDetail")) { return
	 * "redirect:/hunterCert/horseDetail"; } // if the current item has no owner
	 * return to the owner page else if
	 * (!hCertBasket.getCurrentItem().hasOwner() &&
	 * !url.contains("huntercert-horseDetail") &&
	 * !url.contains("huntercert-handlerDetail") &&
	 * !url.contains("huntercert-ownerDetail")) { //
	 * System.out.println(!hCertBasket.getCurrentItem().hasOwner() + // url);
	 * return "redirect:/hunterCert/ownerDetail"; } else if
	 * (!hCertBasket.getCurrentItem().hasHunt() &&
	 * !url.contains("huntercert-horseDetail") &&
	 * !url.contains("huntercert-handlerDetail") &&
	 * !url.contains("huntercert-ownerDetail") &&
	 * !url.contains("huntercert-huntDetail")) { //
	 * System.out.println(!hCertBasket.getCurrentItem().hasOwner() + // url);
	 * return "redirect:/hunterCert/huntDetail"; } else if
	 * (!hCertBasket.getCurrentItem().hasVaccinations() &&
	 * !url.contains("huntercert-horseDetail") &&
	 * !url.contains("huntercert-handlerDetail") &&
	 * !url.contains("huntercert-ownerDetail") &&
	 * !url.contains("huntercert-huntDetail") &&
	 * !url.contains("huntercert-vaccinations")) { //
	 * System.out.println(!hCertBasket.getCurrentItem().hasOwner() + // url);
	 * return "redirect:/hunterCert/vaccinations"; } else if
	 * (!hCertBasket.getCurrentItem().isConditionsAccepted() &&
	 * !url.contains("huntercert-horseDetail") &&
	 * !url.contains("huntercert-handlerDetail") &&
	 * !url.contains("huntercert-ownerDetail") &&
	 * !url.contains("huntercert-huntDetail") &&
	 * !url.contains("huntercert-vaccinations") &&
	 * !url.contains("huntercert-conditions")) { //
	 * System.out.println(!hCertBasket.getCurrentItem().hasOwner() + // url);
	 * return "redirect:/hunterCert/confirm"; }
	 * 
	 * else { return url; }
	 * 
	 * }
	 * 
	 * 
	 * public boolean basketContainsHCert(String horseName){ for(HunterCertItem
	 * item :hCertBasket.getHunterCerts()){
	 * if(item.getCert().getHorse_name().toLowerCase
	 * ().equals(horseName.toLowerCase())){ return true; }
	 * 
	 * } return false;
	 * 
	 * }
	 * 
	 * 
	 * 
	 * // Example code for other controller types below needs to be commented
	 * out
	 * 
	 * 
	 * 
	 * @RequestMapping(value = "/list", method = RequestMethod.GET) public
	 * String listOfHunterCertsB(Model model) {
	 * logger.info("IN: Hunter Cert/list-GET");
	 * 
	 * List<HunterCert> hunterCerts = hunterCertService.getHunterCerts();
	 * model.addAttribute("huntercerts", hunterCerts); if
	 * (!model.containsAttribute("hunterCert")) {
	 * logger.info("Adding HunterCert object to model"); HunterCert hunterCert =
	 * new HunterCert(); model.addAttribute("hunterCert", hunterCert); } return
	 * "huntercert-list"; }
	 * 
	 * @RequestMapping(value = "/add", method = RequestMethod.POST) public
	 * String addingStrategy(@Valid @ModelAttribute HunterCert hunterCert,
	 * BindingResult result, RedirectAttributes redirectAttrs) {
	 * 
	 * logger.info("IN: HunterCert/add-POST");
	 * 
	 * if (result.hasErrors()) { logger.info("hunterCert-add error: " +
	 * result.toString()); redirectAttrs.addFlashAttribute(
	 * "org.springframework.validation.BindingResult.hunterCert", result);
	 * redirectAttrs.addFlashAttribute("hunterCert", hunterCert); return
	 * "redirect:/hunterCert/list"; } else {
	 * hunterCertService.addHunterCert(hunterCert); String message =
	 * "Hunter Cert was successfully added.";
	 * redirectAttrs.addFlashAttribute("message", message); return
	 * "redirect:/hunterCert/list"; } }
	 * 
	 * @RequestMapping(value = "/edit", method = RequestMethod.GET) public
	 * String editHunterCertPage(Model model,
	 * 
	 * @RequestParam(value = "id", required = true) Integer id) {
	 * logger.info("IN: Huntercert/edit-GET:  ID to query = " + id);
	 * 
	 * if (!model.containsAttribute("hunterCert")) {
	 * logger.info("Adding huntercert object to model"); HunterCert hunterCert =
	 * hunterCertService.getHunterCert(id); logger.info("HunterCert/edit-GET:  "
	 * + hunterCert.toString()); model.addAttribute("hunterCert", hunterCert); }
	 * 
	 * return "huntercert-edit"; }
	 * 
	 * @RequestMapping(value = "/edit", method = RequestMethod.POST) public
	 * String editingHunterCert(
	 * 
	 * @Valid @ModelAttribute HunterCert hunterCert, BindingResult result, Model
	 * model, RedirectAttributes redirectAttrs,
	 * 
	 * @RequestParam(value = "action", required = true) String action) {
	 * 
	 * logger.info("IN: Strategy/edit-POST: " + action);
	 * 
	 * System.out.println(action); if
	 * (action.equals(messageSource.getMessage("button.action.cancel", null,
	 * Locale.US))) { String message = "Hunter Cert " + hunterCert.getId() +
	 * " edit cancelled"; redirectAttrs.addFlashAttribute("message", message); }
	 * else if (result.hasErrors()) { logger.info("Strategy-edit error: " +
	 * result.toString()); redirectAttrs.addFlashAttribute(
	 * "org.springframework.validation.BindingResult.hunterCert", result);
	 * redirectAttrs.addFlashAttribute("hunterCert", hunterCert); return
	 * "redirect:/hunterCert/edit?id=" + hunterCert.getId(); } else if
	 * (action.equals(messageSource.getMessage("button.action.save", null,
	 * Locale.US))) { logger.info("hunterCert/edit-POST:  " +
	 * hunterCert.toString()); hunterCertService.updateHunterCert(hunterCert);
	 * String message = "Hunter Cert " + hunterCert.getId() +
	 * " was successfully edited"; redirectAttrs.addFlashAttribute("message",
	 * message); }
	 * 
	 * return "redirect:/hunterCert/list"; }
	 */

	/*
	 * @RequestMapping(value = "/add", method = RequestMethod.POST) public
	 * ModelAndView addingHunterCert(
	 * 
	 * @ModelAttribute HunterCertDTO hunterCertDTO) { ModelAndView modelAndView
	 * = new ModelAndView( "redirect:/hunterCert/list"); HunterCert hunterCert =
	 * HunterCertMapper.getHunterCert(hunterCertDTO);
	 * hunterCertService.addHunterCert(hunterCert); String message =
	 * "Hunter Cert was successfully added."; modelAndView.addObject("message",
	 * message); return modelAndView; }
	 * 
	 * 
	 * @RequestMapping(value="/edit", method=RequestMethod.GET) public
	 * ModelAndView editHunterCertPage(
	 * 
	 * @RequestParam(value = "id", required = true) Integer id) { ModelAndView
	 * modelAndView = new ModelAndView("huntercert-edit"); HunterCert hunterCert
	 * = hunterCertService.getHunterCert(id); HunterCertDTO hunterCertDTO =
	 * HunterCertMapper.getDTO(hunterCert);
	 * modelAndView.addObject("hunterCertDTO", hunterCertDTO); return
	 * modelAndView; }
	 * 
	 * @RequestMapping(value="/edit", method=RequestMethod.POST) public
	 * ModelAndView editingHunterCert(
	 * 
	 * @ModelAttribute HunterCertDTO hunterCertDTO,
	 * 
	 * @RequestParam(value = "action", required = true) String action) {
	 * System.out.println("Using POST"); ModelAndView modelAndView = new
	 * ModelAndView( "redirect:/hunterCert/list"); String message = null; if
	 * (action.equals("save")) { HunterCert hunterCert =
	 * HunterCertMapper.getHunterCert(hunterCertDTO);
	 * hunterCertService.updateHunterCert(hunterCert); message = "Hunter Cert "
	 * + hunterCert.getId() +" was successfully edited.";
	 * modelAndView.addObject("message", message); }
	 * 
	 * if (action.equals("cancel")) { message = "Hunter Cert " +
	 * hunterCertDTO.getId() +" edit cancelled."; } return modelAndView; }
	 * 
	 * @RequestMapping(value="/delete", method=RequestMethod.GET) public
	 * ModelAndView deleteHunterCert(@RequestParam(value="id", required=true)
	 * Integer id,
	 * 
	 * @RequestParam(value="phase", required=true) String phase) {
	 * 
	 * 
	 * HunterCert hunterCert = hunterCertService.getHunterCert(id); ModelAndView
	 * modelAndView = null;
	 * 
	 * if (phase.equals("stage")) { modelAndView = new
	 * ModelAndView("huntercert-delete"); HunterCertDTO hunterCertDTO =
	 * HunterCertMapper.getDTO(hunterCert); String message = "Hunter Cert " +
	 * hunterCert.getId() + " queued for display.";
	 * modelAndView.addObject("hunterCertDTO",hunterCertDTO);
	 * modelAndView.addObject("message", message); }
	 * 
	 * if (phase.equals("confirm")) { modelAndView = new
	 * ModelAndView("redirect:/hunterCert/list");
	 * hunterCertService.deleteHunterCert(id); String message = "Hunter Cert " +
	 * hunterCert.getId() + " was successfully deleted";
	 * modelAndView.addObject("message", message); }
	 * 
	 * if (phase.equals("cancel")) { modelAndView = new
	 * ModelAndView("redirect:/hunterCert/list"); String message =
	 * "HunterCert delete was cancelled."; modelAndView.addObject("message",
	 * message); }
	 * 
	 * return modelAndView;
	 * 
	 * 
	 * }
	 */
}