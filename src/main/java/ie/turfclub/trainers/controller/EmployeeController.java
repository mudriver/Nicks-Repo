package ie.turfclub.trainers.controller;

import ie.turfclub.common.bean.SearchByNameEmployeeBean;
import ie.turfclub.person.model.Person;
import ie.turfclub.person.service.PersonService;
import ie.turfclub.trainers.model.TeEmployees;
import ie.turfclub.trainers.model.TeEmployentHistory;
import ie.turfclub.trainers.model.TePension;
import ie.turfclub.trainers.model.TeTrainers;
import ie.turfclub.trainers.service.EmployeeService;
import ie.turfclub.trainers.service.StableStaffService;
import ie.turfclub.trainers.service.TrainersService;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/employees")
public class EmployeeController {
	
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
	
	@RequestMapping(value="/findByName/{search}", method=RequestMethod.GET)
	@ResponseBody
	public Object getFindByName(@PathVariable(value="search") String search, HttpServletRequest request, ModelMap model) {
		
		List<SearchByNameEmployeeBean> records = employeeService.findByName(search);
		return records;
	}
	
	@RequestMapping(value="/getByCardId/{cardId}", method=RequestMethod.GET)
	public String getEmployeeByCardId(@PathVariable("cardId") Integer cardId, 
			HttpServletRequest request, ModelMap model) {
		
		TeEmployees employee = employeeService.getEmployeeByCardId(cardId);
		model.addAttribute("emp", employee);
		model.addAttribute("backUrl", "/turfclubPrograms/employees/manageStaff");
		return "employee-detail";
	}
	
	@RequestMapping(value="/detail/{id}", method=RequestMethod.GET)
	public String getEmployeeById(@PathVariable("id") Integer id, 
			HttpServletRequest request, ModelMap model) {
		
		TeEmployees employee = employeeService.getEmployeeById(id);
		model.addAttribute("emp", employee);
		model.addAttribute("backUrl", "/turfclubPrograms/employees/searchByName");
		return "employee-detail";
	}
	
	@RequestMapping(value="/add", method=RequestMethod.GET)
	public String getEmployeePage(HttpServletRequest request, ModelMap model) {
		
		model.addAttribute("emp", new TeEmployees());
		
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
		return "emp-add";
	}
	
	@RequestMapping(value="/save", method=RequestMethod.POST)
	public String handleEmployee(TeEmployees emp, HttpServletRequest request, ModelMap model) throws ParseException, SQLException {
		
		emp.setEmployeesDateEntered(new Date());
		emp.setEmployeesLastUpdated(new Date());
		emp.getTeCard().setCardsCardStatus("Applied");
		emp.getTeCard().setTeEmployees(emp);
		
		if(emp.getHistories() != null && emp.getHistories().size() > 0) {
			for (TeEmployentHistory history : emp.getHistories()) {
				history.setTeEmployees(emp);
				if(history.getTeTrainers().getTrainerId() != null) {
					TeTrainers trainer = trainersService.getTrainer(history.getTeTrainers().getTrainerId());
					history.setTeTrainers(trainer);
				}
			}
			emp.setTeEmployentHistories(new HashSet<TeEmployentHistory>(emp.getHistories()));
		}
		
		if(emp.getPensions() != null && emp.getPensions().size() > 0) {
			for (TePension pension : emp.getPensions()) {
				
				if(pension != null && pension.getPensionCardType() != null && pension.getPensionType() != null &&
						pension.getPensionDateJoinedScheme() != null) {
					pension.setTeEmployees(emp);
					
					if(pension.getPensionTrainer().getTrainerId() != null) {
						TeTrainers trainer = trainersService.getTrainer(pension.getPensionTrainer().getTrainerId());
						pension.setPensionTrainer(trainer);
					}
				}
			}
			emp.setTePensions(new HashSet<TePension>(emp.getPensions()));
		}
		
		employeeService.saveOrUpdate(emp);
		
		//Person person = createPerson(emp);
		//personService.addPerson(person);
		model.addAttribute("emp", new TeEmployees());
		model.addAttribute("success", messageSource.getMessage("success.added.employee", new String[] {}, Locale.US));
		return "emp-add";
	}

	//set all value from employee to person object
	private Person createPerson(TeEmployees emp) {
		
		Person person = new Person();
		person.setPpsNumber(emp.getEmployeesPpsNumber());
		person.setTitle(emp.getEmployeesTitle());
		person.setSurname(emp.getEmployeesSurname());
		person.setFirstname(emp.getEmployeesFirstname());
		person.setDateOfBirth(emp.getEmployeesDateOfBirth());
		person.setNationality(emp.getEmployeesNationality());
		person.setSex(emp.getEmployeesSex());
		person.setMaritalStatus(emp.getEmployeesMaritalStatus());
		person.setSpouseName(emp.getEmployeesSpouseName());
		person.setAddress1(emp.getEmployeesAddress1());
		person.setAddress2(emp.getEmployeesAddress2());
		person.setAddress3(emp.getEmployeesAddress3());
		person.setAddress4(emp.getEmployeesAddress4());
		person.setAddress5(emp.getEmployeesAddress5());
		person.setPostCode(emp.getEmployeesPostCode());
		person.setPhoneNo(emp.getEmployeesPhoneNo());
		person.setMobileNo(emp.getEmployeesMobileNo());
		person.setEmail(emp.getEmployeesEmail());
		person.setComments(emp.getEmployeesComments());
		person.setHriAccountNo(emp.getEmployeesHriAccountNo());
		person.setLastUpdated(emp.getEmployeesLastUpdated());
		person.setDateEntered(emp.getEmployeesDateEntered());
		person.setNew(emp.getEmployeesIsNew());
		person.setHasTaxableEarnings(emp.getEmployeesHasTaxableEarnings() != null ? emp.getEmployeesHasTaxableEarnings() : false);
		person.setEmployeeVerified(emp.isEmployeeVerified());
		person.setRequestDate(emp.getEmployeeRequestDate());
		person.setExistingAIRCardHolder(emp.getEmployeeExistingAIRCardHolder());
		person.setOldEmployeeCardNumber(emp.getEmployeeOldEmployeeCardNumber());
		person.setCategoryOfEmployment(emp.getEmployeeCategoryOfEmployment());
		person.setLastYearPaid(emp.getEmployeeLastYearPaid());
		person.setNumHourWorked(emp.getEmployeeNumHourWorked());
		return person;
	}
	
}
