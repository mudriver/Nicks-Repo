package ie.turfclub.vetReports.controller;


	import java.util.HashMap;
import java.util.Map;

	import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;



	@Controller
	@RequestMapping("/reports")
	public class VetReportsReportController {
		
		public static final String REPORTCONTROLLER_REPORT_TYPE_BEAN_NAME_XLS_REPORT = "xlsReport";
		public static final String REPORTCONTROLLER_REPORT_TYPE_BEAN_NAME_PDF_REPORT = "pdfReport";
		private static Logger logger = LoggerFactory.getLogger(VetReportController.class);
		
		@RequestMapping(value = "/pdf", method = RequestMethod.GET)
		public ModelAndView generatePdfReport(ModelAndView modelAndView) {

		Map<String, Object> parameterMap = new HashMap<String, Object>();
		logger.info("rendering the pdf view");
		modelAndView = new ModelAndView(REPORTCONTROLLER_REPORT_TYPE_BEAN_NAME_PDF_REPORT, parameterMap);
		return modelAndView;
		}
	}
