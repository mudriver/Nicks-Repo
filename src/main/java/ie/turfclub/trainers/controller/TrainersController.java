package ie.turfclub.trainers.controller;

import ie.turfclub.common.bean.SearchByNameTrainerBean;
import ie.turfclub.common.bean.TrainerUserBean;
import ie.turfclub.common.thread.PrintAintreeThread;
import ie.turfclub.common.thread.PrintCheltenhamThread;
import ie.turfclub.common.thread.PrintRenewalThread;
import ie.turfclub.main.model.login.User;
import ie.turfclub.main.service.downloads.DownloadService;
import ie.turfclub.main.service.downloads.TokenService;
import ie.turfclub.person.service.PersonService;
import ie.turfclub.sbs.model.SBSEntity;
import ie.turfclub.sbs.service.StableBonusSchemeService;
import ie.turfclub.trainers.model.TeEmployentHistory;
import ie.turfclub.trainers.model.TeTrainers;
import ie.turfclub.trainers.service.EmployeeService;
import ie.turfclub.trainers.service.StableStaffService;
import ie.turfclub.trainers.service.TrainersService;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.lowagie.text.Document;

@Controller
@RequestMapping(value = "/trainers")
@PropertySource("classpath:ie/turfclub/main/resources/properties/config.properties")
public class TrainersController {

	static Logger logger = LoggerFactory.getLogger(TrainersController.class);

	@Resource
	private Environment env;
	
	@Autowired
	StableStaffService stableStaffService;
	
	@Autowired
	private DownloadService downloadService;

	@Autowired
	private TokenService tokenService;

	@Autowired
	private ServletContext context;
	
	@Autowired
	private EmployeeService employeeService;

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private TrainersService trainersService;

	@Autowired
	private PersonService personService;
	
	@Autowired
	private StableBonusSchemeService sbsService;
	
	@RequestMapping(value="/admin/slayear", method=RequestMethod.GET)
	public String getStableListAdministrationReturYearPage(HttpServletRequest req, ModelMap model) {
		return "admin-slayear";
	}
	
	@RequestMapping(value="/admin/teoyear", method=RequestMethod.GET)
	public String getTrainerEmployeeOnlineReturYearPage(HttpServletRequest req, ModelMap model) {
		String year = trainersService.getYearForTrainerEmployeeOnline();
		model.addAttribute("year", year);
		return "admin-teoyear";
	}
	
	@RequestMapping(value="/admin/slayear", method=RequestMethod.POST)
	public String handleStableListAdministrationReturYearPage(HttpServletRequest req, ModelMap model) {
		String year = req.getParameter("year");
		trainersService.handleStableListAdministrationReturYearPage(year);
		model.addAttribute("success", "true");
		return "admin-slayear";
	}
	
	@RequestMapping(value="/admin/teoyear", method=RequestMethod.POST)
	public String handleTrainerEmployeeOnlineReturYearPage(HttpServletRequest req, ModelMap model) {
		String year = req.getParameter("year");
		trainersService.handleTrainerEmployeeOnlineReturYearPage(year);
		model.addAttribute("success", "true");
		return "admin-teoyear";
	}
	
	@RequestMapping(value="/sbs/get", method=RequestMethod.GET)
	public String getPage(HttpServletRequest req, ModelMap model) {
		return "sbs-page";
	}
	
	@RequestMapping(value="/sbs/existsTrainerId", method=RequestMethod.GET)
	@ResponseBody
	public Object isExistsTrainerId(HttpServletRequest req, ModelMap model) {
		String tId = req.getParameter("tId");
		return sbsService.isExistsTrainerId(tId);
	}
	
	@RequestMapping(value="/json/{id}", method=RequestMethod.GET)
	@ResponseBody
	public Object getTrainerById(@PathVariable("id") Integer id, HttpServletRequest req, ModelMap model) throws Exception {
		
		return trainersService.getTrainer(id);
	}
	
	@RequestMapping(value="/sbs/uploadPage", method=RequestMethod.GET)
	public String getUploadPage(HttpServletRequest request, ModelMap model) {
		return "sbs-upload-page";
	}
	
	@RequestMapping(value="/sbs/upload", method=RequestMethod.POST)
	@ResponseBody
	public Object handleUploadFile(@RequestParam("files") MultipartFile file, 
			MultipartHttpServletRequest request, ModelMap model) {
		HashMap<String, Object> map = sbsService.handleUploadedFile(file);
		return map;
	}
	
	@RequestMapping(value="/sbs/initialLetter", method=RequestMethod.GET)
	public ModelAndView getSBSInitialLetter(HttpServletRequest request, ModelMap model) {
		String date = request.getParameter("r");
		String quarter = request.getParameter("q");
		List<HashMap<String, Object>> records = sbsService.getSBSInitialRecords(date, quarter);
		List<SBSEntity> sbsRecords = sbsService.getAll();
		model.addAttribute("sbsRecords", sbsRecords);
		model.addAttribute("records", records);
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("sbsRecords", sbsRecords);
		map.put("records", records);
		map.put("date", date);
		ModelAndView modelAndView = new ModelAndView("initialLetterPDF", "map",map);
		  
		return modelAndView;
		/*return "sbs-initial-letter";*/
	}
	
	@RequestMapping(value="/sbs/returned", method=RequestMethod.GET)
	public String getSBSReturned1(HttpServletRequest request, ModelMap model) {
		List<SBSEntity> sbsRecords = sbsService.getAllOrderByNameAsc();
		model.addAttribute("records", sbsRecords);
		return "sbs-returned";
	}
	
	@RequestMapping(value="/sbs/handleReturned", method=RequestMethod.GET)
	@ResponseBody
	public String handleReturned(HttpServletRequest request, ModelMap model) {
		String id = request.getParameter("id");
		sbsService.handleReturned(id);
		return "sbs-returned";
	}
	
	@RequestMapping(value="/sbs/msgReminder", method=RequestMethod.GET)
	@ResponseBody
	public String handleMsgReminder(HttpServletRequest request, ModelMap model, Authentication authentication) throws IOException {
		
		Object principal = authentication.getPrincipal();
		User user = (User) principal;
		sbsService.handleMsgReminder(env.getRequiredProperty("upload.txt.filepath"),env.getRequiredProperty("upload.dir.path"), user);
		return "sbs-returned";
	}
	
	@RequestMapping(value="/sbs/send/smsReminder", method=RequestMethod.GET)
	public String sendSMSReminder(HttpServletRequest request, ModelMap model) throws IOException {
		
		return "send-sms-reminder";
	}
	
	@RequestMapping(value="/sbs/send/smsReminder/mail", method=RequestMethod.GET)
	@ResponseBody
	public Object sendSMSReminderMail(HttpServletRequest request, ModelMap model, 
			Authentication authentication) throws IOException {
		
		Object principal = authentication.getPrincipal();
		User user = (User) principal;
		String emails = request.getParameter("email");
		sbsService.saveEmailIntoConfigTable(emails);
		//sbsService.sendMailToAdmin(env.getRequiredProperty("upload.dir.path"), user, emails);
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("message", messageSource.getMessage("success.save.email", new String[] {}, Locale.US));
		return map;
	}
	
	@RequestMapping(value="/sbs/finalReminder", method=RequestMethod.GET)
	public ModelAndView getSBSFinalReminder(HttpServletRequest request, ModelMap model) {
		String date = request.getParameter("r");
		String quarter = request.getParameter("q");
		List<HashMap<String, Object>> records = sbsService.getSBSFinalReminder(date, quarter);
		List<SBSEntity> sbsRecords = sbsService.getAll();
		model.addAttribute("sbsRecords", sbsRecords);
		model.addAttribute("records", records);
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("sbsRecords", sbsRecords);
		map.put("records", records);
		map.put("date", date);
		ModelAndView modelAndView = new ModelAndView("reminderLetterPDF", "map",map);
		  
		return modelAndView;
		/*return "sbs-final-reminder";*/
	}
	
	@RequestMapping(value = "/sbs/findByName", method = RequestMethod.GET)
	@ResponseBody
	public Object getSBSFindByName(HttpServletRequest request, ModelMap model)
			throws Exception {

		String search = request.getParameter("q");
		List<SBSEntity> records = sbsService.findByName(search);
		return records;
	}
	
	@RequestMapping(value="/sbs/reprint", method=RequestMethod.GET)
	public ModelAndView getSBSReprint(HttpServletRequest request, ModelMap model) {
		String date = request.getParameter("r");
		String quarter = request.getParameter("q");
		String tId = request.getParameter("t");
		HashMap<String, Object> record = sbsService.getSBSReprint(date, quarter, tId);
		record.put("rdate", date);
		model.addAttribute("rec", record);
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("records", record);
		map.put("date", date);
		ModelAndView modelAndView = new ModelAndView("reprintPDF", "map",map);
		  
		return modelAndView;
		/*return "sbs-reprint";*/
	}
	
	@RequestMapping(value = "/home", method = RequestMethod.GET)
	public String trainers(Model model, Authentication authentication) {

		Object principal = authentication.getPrincipal();
		User user = (User) principal;
		String role = "";
		for (GrantedAuthority roles : user.getAuthorities()) {

			if (roles.getAuthority().equals("STABLESTAFF_PENSION")) {
				model.addAttribute("USERMENUTYPE", "STABLESTAFF_PENSION");
			}
		}

		return "trainers";
	}

	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String getAddTrainerPage(Model model, Authentication authentication) {

		model.addAttribute("trainer", new TeTrainers());
		model.addAttribute("verifiedStatusEnum",
				trainersService.getVerifiedStatus());
		model.addAttribute("sexEnum", employeeService.getSexEnum());
		model.addAttribute("maritalEnum",
				employeeService.getMaritalStatusEnum());
		model.addAttribute("employmentCatEnum",
				employeeService.getEmploymentCategoryEnum());
		model.addAttribute("titlesEnum", employeeService.getTitlesEnum());
		model.addAttribute("countiesEnum", employeeService.getCountiesEnum());
		model.addAttribute("countriesEnum", employeeService.getCountriesEnum());
		model.addAttribute("cardTypeEnum", employeeService.getAllCardType());
		model.addAttribute("pensionEnum", employeeService.getPension());
		model.addAttribute("nationalityEnum",
				employeeService.getNationalityEnum());
		return "trainer-add";
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String handleTrainerPage(TeTrainers trainer, Model model,
			RedirectAttributes redirectAttributes) throws SQLException {

		if (trainer.getTrainerId() != null && trainer.getTrainerId() > 0)
			redirectAttributes.addFlashAttribute("success", messageSource.getMessage(
					"success.updated.trainer", new String[] {}, Locale.US));
		else
			redirectAttributes.addFlashAttribute("success", messageSource.getMessage(
					"success.added.trainer", new String[] {}, Locale.US));

		trainersService.saveOrUpdate(trainer);
		/*model.addAttribute("trainer", trainer);
		model.addAttribute("verifiedStatusEnum",
				trainersService.getVerifiedStatus());
		model.addAttribute("sexEnum", employeeService.getSexEnum());
		model.addAttribute("maritalEnum",
				employeeService.getMaritalStatusEnum());
		model.addAttribute("employmentCatEnum",
				employeeService.getEmploymentCategoryEnum());
		model.addAttribute("titlesEnum", employeeService.getTitlesEnum());
		model.addAttribute("countiesEnum", employeeService.getCountiesEnum());
		model.addAttribute("countriesEnum", employeeService.getCountriesEnum());
		model.addAttribute("cardTypeEnum", employeeService.getAllCardType());
		model.addAttribute("pensionEnum", employeeService.getPension());
		model.addAttribute("nationalityEnum",
				employeeService.getNationalityEnum());*/
		return "redirect:/trainers/add";
	}

	@RequestMapping(value = "/person/copyRecord", method = RequestMethod.GET)
	@ResponseBody
	public String handleCopyRecord(HttpServletRequest req, Model model)
			throws SQLException {

		trainersService.handleCopyRecord();
		String success = messageSource.getMessage(
				"success.trainer.save.record.to.person", new String[] {},
				Locale.US);
		return success;
	}

	@RequestMapping(value = "/searchPage", method = RequestMethod.GET)
	public String getSearchPage(HttpServletRequest req, Model model)
			throws SQLException {

		return "trainer-search-page";
	}

	@RequestMapping(value = "/findByName", method = RequestMethod.GET)
	@ResponseBody
	public Object getFindByName(HttpServletRequest request, ModelMap model)
			throws Exception {

		String search = request.getParameter("q");
		List<SearchByNameTrainerBean> records = trainersService
				.findByName(search);
		return records;
	}
	
	@RequestMapping(value = "/renewal/findByName", method = RequestMethod.GET)
	@ResponseBody
	public Object getRenewalTrainerFindByName(HttpServletRequest request, ModelMap model)
			throws Exception {

		String search = request.getParameter("q");
		List<SearchByNameTrainerBean> records = trainersService
				.getRenewalTrainerFindByName(search);
		return records;
	}
	
	

	@RequestMapping(value = "/detail/{id}", method = RequestMethod.GET)
	public String getEmployeeById(@PathVariable("id") Integer id,
			HttpServletRequest request, ModelMap model) throws Exception {

		TeTrainers trainer = trainersService.getTrainer(id);
		model.addAttribute("trainer", trainer);
		model.addAttribute("verifiedStatusEnum",
				trainersService.getVerifiedStatus());
		model.addAttribute("sexEnum", employeeService.getSexEnum());
		model.addAttribute("maritalEnum",
				employeeService.getMaritalStatusEnum());
		model.addAttribute("employmentCatEnum",
				employeeService.getEmploymentCategoryEnum());
		model.addAttribute("titlesEnum", employeeService.getTitlesEnum());
		model.addAttribute("countiesEnum", employeeService.getCountiesEnum());
		model.addAttribute("countriesEnum", employeeService.getCountriesEnum());
		model.addAttribute("cardTypeEnum", employeeService.getAllCardType());
		model.addAttribute("pensionEnum", employeeService.getPension());
		model.addAttribute("nationalityEnum",
				employeeService.getNationalityEnum());

		return "trainer-edit";
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String getListOfTrainers(Model model, Authentication authentication) {

		List<TrainerUserBean> records = personService.getTrainerUserBean();
		model.addAttribute("records", records);
		return "trainer-list";
	}
	
	@RequestMapping(value = "/aintree", method = RequestMethod.GET)
	public String getAintreePage(Model model, Authentication authentication,
			HttpServletRequest request) {

		int start = 0;
		int end = 20;
		HttpSession session = request.getSession();
		session.setAttribute("lastTrainerId", null);
		List<HashMap<String, Object>> records = trainersService.getAintreeRecord(start, end, session);
		model.addAttribute("records", records);
		return "sbs-aintree";
	}
	
	@RequestMapping(value = "/print/renewal", method = RequestMethod.GET)
	public String getListOfLicenseTrainers(HttpServletRequest request, Model model, Authentication authentication) {

		int start = 0;
		int end = 20;
		HttpSession session = request.getSession();
		session.setAttribute("lastTrainerId", null);
		List<HashMap<String, Object>> records = trainersService.getLicensedTrainerWithCurrentEmployees(start, end, session);
		model.addAttribute("records", records);
		return "licensed-trainer-list";
	}
	
	@RequestMapping(value = "/print/renewal/page", method = RequestMethod.GET)
	public String getPrintRenewalPageByPage(Model model, Authentication authentication, 
			HttpServletRequest req) {

		int page = Integer.parseInt(req.getParameter("page"));
		int start = page * 20;
		int end = (page+1) * 20;
		HttpSession session = req.getSession();
		List<HashMap<String, Object>> records = trainersService.getLicensedTrainerWithCurrentEmployees(start, end, session);
		model.addAttribute("records", records);
		return "licensed-trainer-list-page";
	}
	
	@RequestMapping(value = "/print/renewal/print", method = RequestMethod.GET)
	@ResponseBody
	public String getPrintRenewalPrint(Model model, Authentication authentication,
			HttpServletRequest request) {
		
		request.getSession().setAttribute("printRenewalStatus", "working");
		PrintRenewalThread printCheltenhamThread = new PrintRenewalThread(trainersService, env.getRequiredProperty("upload.pdf.print.renewal"), request.getSession());
		Thread thread = new Thread(printCheltenhamThread);
		thread.start();
		return "Success";
	}
	
	@RequestMapping(value = "/print/renewal/print/download", method = RequestMethod.GET)
	public void getPrintRenewalPrintDownload(Model model, Authentication authentication,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		FileInputStream baos = new FileInputStream(env.getRequiredProperty("upload.pdf.print.renewal"));
		request.getSession().setAttribute("printRenewalStatus", "null");
        response.setHeader("Expires", "0");
        response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
        response.setHeader("Pragma", "public");
        response.setContentType("application/pdf");
        response.addHeader("Content-Disposition", "attachment; filename=All_Trainers_Report.pdf");

        OutputStream os = response.getOutputStream();

        byte buffer[] = new byte[8192];
        int bytesRead;

        while ((bytesRead = baos.read(buffer)) != -1) {
            os.write(buffer, 0, bytesRead);
        }

        os.flush();
        os.close();
	}
	
	@RequestMapping(value = "/print/renewal/printStatus", method = RequestMethod.GET)
	@ResponseBody
	public String getPrintRenewalPrintStatus(Model model, Authentication authentication,
			HttpServletRequest request) {
		
		String status = (String) request.getSession().getAttribute("printRenewalStatus");
		if(status != null && !status.equalsIgnoreCase("undefined") && status.equalsIgnoreCase("done")) {
			//request.getSession().setAttribute("aintreeStatus", null);
			return "done";
		} else if(status != null && !status.equalsIgnoreCase("undefined") && status.equalsIgnoreCase("working")) {
			return "working";
		} else {
			return "null";
		}
	}
	
	@RequestMapping(value = "/cheltenham", method = RequestMethod.GET)
	public String getCheltenhamPage(Model model, Authentication authentication,
			HttpServletRequest request) {

		int start = 0;
		int end = 20;
		HttpSession session = request.getSession();
		session.setAttribute("lastTrainerId", null);
		List<HashMap<String, Object>> records = trainersService.getAintreeRecord(start, end, session);
		model.addAttribute("records", records);
		return "sbs-cheltenham";
	}
	
	@RequestMapping(value = "/cheltenham/page", method = RequestMethod.GET)
	public String getCheltenhamPageByPage(Model model, Authentication authentication, 
			HttpServletRequest req) {

		int page = Integer.parseInt(req.getParameter("page"));
		int start = page * 20;
		int end = (page+1) * 20;
		HttpSession session = req.getSession();
		List<HashMap<String, Object>> records = trainersService.getAintreeRecord(start, end, session);
		model.addAttribute("records", records);
		return "sbs-cheltenham-page";
	}
	
	@RequestMapping(value = "/cheltenham/print", method = RequestMethod.GET)
	@ResponseBody
	public String getCheltenhamPrint(Model model, Authentication authentication,
			HttpServletRequest request) {
		
		request.getSession().setAttribute("cheltenhamStatus", "working");
		PrintCheltenhamThread printCheltenhamThread = new PrintCheltenhamThread(trainersService, env.getRequiredProperty("upload.pdf.cheltenham"), request.getSession());
		Thread thread = new Thread(printCheltenhamThread);
		thread.start();
		return "Success";
	}
	
	@RequestMapping(value = "/cheltenham/print/download", method = RequestMethod.GET)
	public void getCheltenhamPrintDownload(Model model, Authentication authentication,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		FileInputStream baos = new FileInputStream(env.getRequiredProperty("upload.pdf.cheltenham"));
		request.getSession().setAttribute("cheltenhamStatus", "null");
        response.setHeader("Expires", "0");
        response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
        response.setHeader("Pragma", "public");
        response.setContentType("application/pdf");
        response.addHeader("Content-Disposition", "attachment; filename=cheltenham.pdf");

        OutputStream os = response.getOutputStream();

        byte buffer[] = new byte[8192];
        int bytesRead;

        while ((bytesRead = baos.read(buffer)) != -1) {
            os.write(buffer, 0, bytesRead);
        }

        os.flush();
        os.close();
	}
	
	@RequestMapping(value = "/cheltenham/printStatus", method = RequestMethod.GET)
	@ResponseBody
	public String getCheltenhamPrintStatus(Model model, Authentication authentication,
			HttpServletRequest request) {
		
		String status = (String) request.getSession().getAttribute("cheltenhamStatus");
		if(status != null && !status.equalsIgnoreCase("undefined") && status.equalsIgnoreCase("done")) {
			//request.getSession().setAttribute("aintreeStatus", null);
			return "done";
		} else if(status != null && !status.equalsIgnoreCase("undefined") && status.equalsIgnoreCase("working")) {
			return "working";
		} else {
			return "null";
		}
	}
	
	@RequestMapping(value = "/aintree/print", method = RequestMethod.GET)
	@ResponseBody
	public String getAintreePrint(Model model, Authentication authentication,
			HttpServletRequest request) {
		
		request.getSession().setAttribute("aintreeStatus", "working");
		PrintAintreeThread printAintreeThread = new PrintAintreeThread(trainersService, env.getRequiredProperty("upload.pdf.aintree"), request.getSession());
		Thread thread = new Thread(printAintreeThread);
		thread.start();
		return "Success";
	}
	
	@RequestMapping(value = "/aintree/print/download", method = RequestMethod.GET)
	public void getAintreePrintDownload(Model model, Authentication authentication,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		FileInputStream baos = new FileInputStream(env.getRequiredProperty("upload.pdf.aintree"));
		request.getSession().setAttribute("aintreeStatus", "null");
        response.setHeader("Expires", "0");
        response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
        response.setHeader("Pragma", "public");
        response.setContentType("application/pdf");
        response.addHeader("Content-Disposition", "attachment; filename=aintree.pdf");

        OutputStream os = response.getOutputStream();

        byte buffer[] = new byte[8192];
        int bytesRead;

        while ((bytesRead = baos.read(buffer)) != -1) {
            os.write(buffer, 0, bytesRead);
        }

        os.flush();
        os.close();
	}
	
	@RequestMapping(value = "/aintree/printStatus", method = RequestMethod.GET)
	@ResponseBody
	public String getAintreePrintStatus(Model model, Authentication authentication,
			HttpServletRequest request) {
		
		String status = (String) request.getSession().getAttribute("aintreeStatus");
		if(status != null && !status.equalsIgnoreCase("undefined") && status.equalsIgnoreCase("done")) {
			//request.getSession().setAttribute("aintreeStatus", null);
			return "done";
		} else if(status != null && !status.equalsIgnoreCase("undefined") && status.equalsIgnoreCase("working")) {
			return "working";
		} else {
			return "null";
		}
	}
	
	@RequestMapping(value = "/aintree/page", method = RequestMethod.GET)
	public String getAintreePageByPage(Model model, Authentication authentication, 
			HttpServletRequest req) {

		int page = Integer.parseInt(req.getParameter("page"));
		int start = page * 20;
		int end = (page+1) * 20;
		HttpSession session = req.getSession();
		List<HashMap<String, Object>> records = trainersService.getAintreeRecord(start, end, session);
		model.addAttribute("records", records);
		return "sbs-aintree-page";
	}

	@RequestMapping(value = "/{id}/employees/{type}", method = RequestMethod.GET)
	public String getListOfTrainers(@PathVariable("id") Integer id,
			@PathVariable("type") String type, Model model,
			Authentication authentication) throws Exception {

		List<TeEmployentHistory> records = employeeService
				.getListOfTrainersEmployees(id, type);
		model.addAttribute("trainer", trainersService.getTrainer(id));
		model.addAttribute("records", records);
		model.addAttribute("type", type);
		return "trainer-employee-list";
	}
	
	@RequestMapping(value = "/employees/mark/{id}/{type}", method = RequestMethod.GET)
	public String getListOfTrainers(@PathVariable("id") Integer id,
			@PathVariable("type") String type, Model model,
			Authentication authentication, RedirectAttributes redirectAttributes) throws Exception {

		trainersService.markEmployeesLeftForTrainer(id);
		/*model.addAttribute("trainer", trainersService.getTrainer(id));
		model.addAttribute("records", records);
		model.addAttribute("type", type);*/
		redirectAttributes.addFlashAttribute("success", messageSource.getMessage("success.trainer.employees.left", new String[] {}, Locale.US));
		return "redirect:/trainers/"+id+"/employees/"+type;
	}

	@RequestMapping(value = "/export/{id}/{type}", method = RequestMethod.GET)
	public void handleExportTrainerEmployeeRecords(
			@PathVariable("id") Integer id, @PathVariable("type") String type,
			HttpServletRequest request, HttpServletResponse response,
			ModelMap model) throws Exception {

		BufferedWriter writer = new BufferedWriter(response.getWriter());
		String csvString = trainersService.getCSVStringForTrainerEmployees(id,
				type);
		TeTrainers trainer = trainersService.getTrainer(id);
		response.setContentType("text/csv");
		String headerValue = String.format("attachment; filename=\"%s\"",
				trainer.getTrainerFullName() + "'s Employees.csv");
		response.setHeader("Content-Disposition", headerValue);

		writer.write(csvString);

		writer.flush();
		writer.close();
	}

	@RequestMapping(value = "/pdfDoc/{id}/{type}", method = RequestMethod.GET)
	public ModelAndView handleExportWordDocForTrainerEmployeeRecords(
			@PathVariable("id") Integer id, @PathVariable("type") String type,
			HttpServletRequest request, HttpServletResponse response,
			ModelMap model) throws Exception {

		Document document = new Document();
		try {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("id", id);
			map.put("type", type);
			ModelAndView modelAndView = new ModelAndView("trainerPDFView", "map",map);
			return modelAndView;
		} catch (Exception ioe) {
			throw new ServletException(ioe.getMessage());
		} finally {
		}
	}
}
