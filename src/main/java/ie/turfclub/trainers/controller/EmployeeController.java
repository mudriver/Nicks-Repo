package ie.turfclub.trainers.controller;

import ie.turfclub.common.bean.InapproveEmployeeBean;
import ie.turfclub.common.bean.SearchByNameEmployeeBean;
import ie.turfclub.common.enums.AdvanceSearchEnum;
import ie.turfclub.person.service.PersonService;
import ie.turfclub.trainers.model.TeCards;
import ie.turfclub.trainers.model.TeEmployees;
import ie.turfclub.trainers.service.EmployeeService;
import ie.turfclub.trainers.service.StableStaffService;
import ie.turfclub.trainers.service.TrainersService;

import java.io.BufferedWriter;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping(value = "/employees")
public class EmployeeController {
	
	public static int noOfRecords = 10;
	
	@Autowired
	private TrainersService trainersService;
	
	@Autowired
	private EmployeeService employeeService;
	
	@Autowired
	private PersonService personService;

	@Autowired
	private StableStaffService stableStaffService;
	
	@Autowired
	private MessageSource messageSource;
	
	@RequestMapping(value="/admin", method=RequestMethod.GET)
	public String getEmployeesAdminPage(HttpServletRequest request, ModelMap model) {
		
		List<HashMap<String, Object>> cards = employeeService.getAllCards();
		model.addAttribute("cards", cards);
		return "employees-admin";
	}
	
	@RequestMapping(value="/manageStaff", method=RequestMethod.GET)
	public String getManageStaffPage(HttpServletRequest request, ModelMap model) {
		
		List<HashMap<String, Object>> cards = employeeService.getAllCards();
		model.addAttribute("cards", cards);
		return "manage-employee-staff";
	}
	
	@RequestMapping(value="/searchByName", method=RequestMethod.GET)
	public String getSearchByName(HttpServletRequest request, ModelMap model) {
		
		/*List<SearchByNameEmployeeBean> records = employeeService.searchByNameEmployees();
		model.addAttribute("records", records);*/
		return "search-by-name-employees";
	}
	
	@RequestMapping(value="/findByName", method=RequestMethod.GET)
	@ResponseBody
	public Object getFindByName(HttpServletRequest request, ModelMap model) {
		
		String search = request.getParameter("q");
		List<SearchByNameEmployeeBean> records = employeeService.findByName(search);
		return records;
	}
	
	@RequestMapping(value="/findByNumber", method=RequestMethod.GET)
	@ResponseBody
	public Object getFindByNumber( HttpServletRequest request, ModelMap model) {
		
		String search = request.getParameter("q");
		List<SearchByNameEmployeeBean> records = employeeService.findByNumber(search);
		return records;
	}
	
	@RequestMapping(value="/partFullTimeStat", method=RequestMethod.GET)
	public String getPartFullTimeStat( HttpServletRequest request, ModelMap model) {
		
		String search = request.getParameter("q");
		List<SearchByNameEmployeeBean> records = employeeService.findByNumber(search);
		return "part-full-time-stat";
	}
	
	@RequestMapping(value="/partFullTimeResult", method=RequestMethod.GET)
	@ResponseBody
	public Object getPartFullTimeResult( HttpServletRequest request, ModelMap model) {
		
		String hours = request.getParameter("hours");
		HashMap<String, Object> records = employeeService.getPartFullTimeRecords(hours);
		return records;
	}
	
	@RequestMapping(value="/delete/{id}", method=RequestMethod.GET)
	public String deleteRecordById(@PathVariable(value="id") Integer id, HttpServletRequest request, ModelMap model) throws IllegalAccessException, InvocationTargetException {
		
		employeeService.deleteRecordById(id);
		model.addAttribute("success", messageSource.getMessage("success.deleted.employee", new String[] {}, Locale.US));
		return "search-by-name-employees";
	}
	
	@RequestMapping(value="/preview/{id}", method=RequestMethod.GET)
	public String previewRecordById(@PathVariable(value="id") Integer id, HttpServletRequest request, ModelMap model) throws IllegalAccessException, InvocationTargetException {
		
		TeEmployees employee = employeeService.getEmployeeById(id);
		model.addAttribute("emp", employee);
		model.addAttribute("backUrl", "/turfclubPrograms/employees/detail/"+id);
		
		return "employee-preview";
	}
	
	@RequestMapping(value="/getByCardId/{cardId}", method=RequestMethod.GET)
	public String getEmployeeByCardId(@PathVariable("cardId") Integer cardId, 
			HttpServletRequest request, ModelMap model) {
		
		TeEmployees employee = employeeService.getEmployeeByCardId(cardId);
		model.addAttribute("emp", employee);
		model.addAttribute("backUrl", "/turfclubPrograms/employees/manageStaff");
		return "employee-detail";
	}
	
	@RequestMapping(value="/inapprove/list", method=RequestMethod.GET)
	public String getInapproveEmployeeList(HttpServletRequest request, ModelMap model) {
		
		List<InapproveEmployeeBean> employees = employeeService.getInapproveEmployeeList();
		model.addAttribute("employees", employees);
		return "employee-inapprove-list";
	}
	
	@RequestMapping(value="/inapprove/{id}", method=RequestMethod.GET)
	public String handleInapproveEmployee(@PathVariable("id") Integer id, HttpServletRequest request, ModelMap model, RedirectAttributes attributes) throws IllegalAccessException, InvocationTargetException {

		employeeService.deleteEmployeeApprovedById(id);
		attributes.addFlashAttribute("success", messageSource.getMessage("approve.employees.inapprove", new String[] {}, Locale.US));
		return "redirect:/employees/inapprove/list";
	}
	
	@RequestMapping(value="/approve/delete/{id}", method=RequestMethod.GET)
	public String handleDeleteInapproveEmployee(@PathVariable("id") Integer id, HttpServletRequest request, ModelMap model, RedirectAttributes attributes) throws IllegalAccessException, InvocationTargetException {

		employeeService.deleteEmployeeApprovedById(id);
		attributes.addFlashAttribute("success", messageSource.getMessage("success.deleted.employee", new String[] {}, Locale.US));
		return "redirect:/employees/inapprove/list";
	}
	
	@RequestMapping(value="/approve/{id}", method=RequestMethod.GET)
	public String handleApproveEmployee(@PathVariable("id") Integer id, HttpServletRequest request, ModelMap model, RedirectAttributes attributes) throws Exception {

		employeeService.handleApproveEmployee(id);
		attributes.addFlashAttribute("success", messageSource.getMessage("approve.employees.approve", new String[] {}, Locale.US));
		return "redirect:/employees/inapprove/list";
	}
	
	@RequestMapping(value="/detail/{id}", method=RequestMethod.GET)
	public String getEmployeeById(@PathVariable("id") Integer id, 
			HttpServletRequest request, ModelMap model) throws IllegalAccessException, InvocationTargetException {
		
		TeEmployees employee = employeeService.getEmployeeById(id);
		model.addAttribute("emp", employee);
		model.addAttribute("backUrl", "/turfclubPrograms/employees/searchByName");
		
		model.addAttribute("trainers", trainersService.getAllTrainers());
		model.addAttribute("sexEnum", employeeService.getSexEnum());
		model.addAttribute("maritalEnum",
				employeeService.getMaritalStatusEnum());
		model.addAttribute("employmentCatEnum",
				employeeService.getEmploymentCategoryEnum());
		model.addAttribute("titlesEnum", employeeService.getTitlesEnum());
		model.addAttribute("countiesEnum", employeeService.getCountiesEnum());
		model.addAttribute("countriesEnum",
				employeeService.getCountriesEnum());
		model.addAttribute("cardTypeEnum", employeeService.getAllCardType());
		model.addAttribute("pensionEnum", employeeService.getPension());
		model.addAttribute("nationalityEnum", employeeService.getNationalityEnum());
		model.addAttribute("year", trainersService.getYearForTrainerEmployeeOnline());
		model.addAttribute("prevYear", Integer.parseInt(trainersService.getYearForTrainerEmployeeOnline())-1);
		
		return "employee-edit";
	}
	
	@RequestMapping(value="/add", method=RequestMethod.GET)
	public String getEmployeePage(HttpServletRequest request, ModelMap model) {
		
		TeEmployees emp = new TeEmployees();
		TeCards card = new TeCards();
		int autoCardNumber = employeeService.getAutoIncreamentCardNumber();
		card.setCardsCardNumber(autoCardNumber);
		emp.setTeCard(card);
		model.addAttribute("emp", emp);
		
		model.addAttribute("trainers", trainersService.getAllTrainers());
		model.addAttribute("sexEnum", employeeService.getSexEnum());
		model.addAttribute("maritalEnum",
				employeeService.getMaritalStatusEnum());
		model.addAttribute("employmentCatEnum",
				employeeService.getEmploymentCategoryEnum());
		model.addAttribute("titlesEnum", employeeService.getTitlesEnum());
		model.addAttribute("countiesEnum", employeeService.getCountiesEnum());
		model.addAttribute("countriesEnum",
				employeeService.getCountriesEnum());
		model.addAttribute("cardTypeEnum", employeeService.getAllCardType());
		model.addAttribute("pensionEnum", employeeService.getPension());
		model.addAttribute("nationalityEnum", employeeService.getNationalityEnum());
		model.addAttribute("year", trainersService.getYearForTrainerEmployeeOnline());
		model.addAttribute("prevYear", Integer.parseInt(trainersService.getYearForTrainerEmployeeOnline())-1);
		return "emp-add";
	}
	
	@RequestMapping(value="/save", method=RequestMethod.POST)
	public String handleEmployee(TeEmployees emp, HttpServletRequest request, ModelMap model,
			RedirectAttributes redirectAttributes) throws Exception {
		
		if(emp.getEmployeesEmployeeId() != null && emp.getEmployeesEmployeeId() > 0)
			redirectAttributes.addFlashAttribute("success", messageSource.getMessage("success.updated.employee", new String[] {}, Locale.US));
		else
			redirectAttributes.addFlashAttribute("success", messageSource.getMessage("success.added.employee", new String[] {}, Locale.US));
	
		employeeService.handleSaveOrUpdate(emp);
		
		/*if(emp.getEmployeesPpsNumber() != null && emp.getEmployeesPpsNumber().length() > 10)
			emp.setEmployeesPpsNumber(EncryptDecryptUtils.decrypt(emp.getEmployeesPpsNumber()));
		model.addAttribute("emp", emp);
		model.addAttribute("trainers", trainersService.getAllTrainers());
		model.addAttribute("sexEnum", employeeService.getSexEnum());
		model.addAttribute("maritalEnum",
				employeeService.getMaritalStatusEnum());
		model.addAttribute("employmentCatEnum",
				employeeService.getEmploymentCategoryEnum());
		model.addAttribute("titlesEnum", employeeService.getTitlesEnum());
		model.addAttribute("countiesEnum", employeeService.getCountiesEnum());
		model.addAttribute("countriesEnum",
				employeeService.getCountriesEnum());
		model.addAttribute("cardTypeEnum", employeeService.getAllCardType());
		model.addAttribute("pensionEnum", employeeService.getPension());
		model.addAttribute("nationalityEnum", employeeService.getNationalityEnum());*/
		return "redirect:/employees/manageStaff";
	}

	@RequestMapping(value="/advSearch", method=RequestMethod.GET)
	public String getAdvanceSearchPage( HttpServletRequest request, ModelMap model) {
		
		return "advance-search";
	}
	
	@RequestMapping(value="/encryptPPS", method=RequestMethod.GET)
	@ResponseBody
	public String handleEncryptPPSNumber( HttpServletRequest request, ModelMap model) {
		
		employeeService.handleEncryptPPSNumber();
		return messageSource.getMessage("success.encrypt.pps.number", new String[] {}, Locale.US);
	}
	
	@RequestMapping(value="/advSearchType", method=RequestMethod.GET)
	public String getAdvanceSearchType(@RequestParam(value="type") String type, HttpServletRequest request, ModelMap model) {
	
		model.addAttribute("type", type);
		model.addAttribute("recordFor", AdvanceSearchEnum.getNameById(type));
		return "advance-search-type";
	}
	
	@RequestMapping(value="/advSearchRec", method=RequestMethod.GET)
	@ResponseBody
	public Object getAdvanceSearchRecordByType(@RequestParam(value="type") String type, 
			@RequestParam(value="start") int start, @RequestParam(value="length") int length,  
			@RequestParam(value="draw") int draw, HttpServletRequest request, ModelMap model) {
		
		length = (length == -1) ? noOfRecords : length;
		return employeeService.getAdvanceSearchRecordByType(type, start, length, draw);
	}
	
	@RequestMapping(value = "/person/copyRecord", method = RequestMethod.GET)
	@ResponseBody
	public String handleCopyRecord(HttpServletRequest req, Model model) throws SQLException {

		employeeService.handleCopyRecord();
		String success = messageSource.getMessage("success.employee.save.record.to.person", new String[] {}, Locale.US);
		return success;
	}
	
	@RequestMapping(value = "/exportCSV", method=RequestMethod.GET)
	public void exportCSV(@RequestParam( value = "type") String type, HttpServletRequest request,
			HttpServletResponse response) {
		
		try {
			
			BufferedWriter writer = new BufferedWriter(response.getWriter());
			
			response.setContentType("text/csv");
			// creates mock data
			HashMap<String, Object> map = employeeService.getCSVString(type, getTitleForExportCSV(type));
			String headerValue = String.format("attachment; filename=\"%s\"", getFileName(type));
			response.setHeader("Content-Disposition", headerValue);
			writer.write(((String) map.get("csvstring")));
			writer.flush();
			writer.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Get Title for export CSV
	 * 
	 * @param type
	 * @return
	 */
	private String getTitleForExportCSV(String type) {
		
		String title = "";
		switch(type) {
			case "1":
				title = messageSource.getMessage("link.all.a.card.holders", new String[] {}, Locale.US);
				break;
			case "2":
				title = messageSource.getMessage("link.all.b.card.holders", new String[] {}, Locale.US);
				break;
			case "3":
				break;
			case "4":
				break;
			case "5":
				break;
			case "6":
				break;
			case "7":
				break;
		}
		return title;
	}

	/**
	 * get Export CSV filename
	 * 
	 * @param type
	 * @return
	 */
	private String getFileName(String type) {
		
		String filename = "Demo";
		switch(type) {
			case "1":
				filename = messageSource.getMessage("filename.all.a.card.holders", new String[] {}, Locale.US);
				break;
			case "2":
				filename = messageSource.getMessage("filename.all.b.card.holders", new String[] {}, Locale.US);
				break;
			case "3":
				break;
			case "4":
				break;
			case "5":
				break;
			case "6":
				break;
			case "7":
				break;
		}
		return filename+".csv";
	}
	
	@RequestMapping(value="/cardnumber/generate", method=RequestMethod.GET)
	public @ResponseBody Object getAutoGeneratedCardNumber(HttpServletRequest request, ModelMap model) {
		
		return employeeService.getAutoGeneatedCardNumber();
	}
	
	@RequestMapping(value="/history/delete/{id}", method=RequestMethod.GET)
	public @ResponseBody Object delteHistoryRecord(@PathVariable("id") Integer id, HttpServletRequest request) {
		
		return employeeService.deleteHistoryRecord(id);
	}
	
	@RequestMapping(value="/existsdob/{id}", method=RequestMethod.GET)
	public @ResponseBody Object isExistsDOB(@RequestParam("dob") Date dob, @PathVariable("id") Integer id, 
			HttpServletRequest request) throws ParseException {
		
		return employeeService.isExistsDOB(id, dob);
	}
	
	@RequestMapping(value="/existspps/{id}", method=RequestMethod.GET)
	public @ResponseBody Object isExistsPPS(@RequestParam("pps") String pps, @PathVariable("id") Integer id, 
			HttpServletRequest request) {
		
		return employeeService.isExistsPPS(id, pps);
	}
}
