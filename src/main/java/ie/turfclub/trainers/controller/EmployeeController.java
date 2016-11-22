package ie.turfclub.trainers.controller;

import ie.turfclub.person.model.Person;
import ie.turfclub.person.service.PersonService;
import ie.turfclub.trainers.model.TeEmployees;
import ie.turfclub.trainers.model.TePension;
import ie.turfclub.trainers.model.TeTrainers;
import ie.turfclub.trainers.service.EmployeeService;
import ie.turfclub.trainers.service.TrainersService;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "/employees")
public class EmployeeController {
	
	@Autowired
	private TrainersService trainersService;
	
	@Autowired
	private EmployeeService employeeService;
	
	@Autowired
	private PersonService personService;

	@RequestMapping(value="/manageStaff", method=RequestMethod.GET)
	public String getManageStaffPage(HttpServletRequest request) {
		
		return "manage-employee-staff";
	}
	
	@RequestMapping(value="/add", method=RequestMethod.GET)
	public String getEmployeePage(HttpServletRequest request, ModelMap model) {
		
		model.addAttribute("emp", new TeEmployees());
		
		model.addAttribute("trainers", trainersService.getAllTrainers());
		return "emp-add";
	}
	
	@RequestMapping(value="/save", method=RequestMethod.POST)
	public String handleEmployee(TeEmployees emp, HttpServletRequest request, ModelMap model) throws ParseException, SQLException {
		
		emp.setEmployeesDateEntered(new Date());
		emp.setEmployeesLastUpdated(new Date());
		emp.getTeCard().setCardsCardStatus("Applied");
		emp.getTeCard().setTeEmployees(emp);
		
		for (TePension pension : emp.getPensions()) {
			
			if(pension != null && pension.getPensionCardType() != null && pension.getPensionType() != null &&
					pension.getPensionDateRange() != null) {
				pension.setTeEmployees(emp);
				
				String dateRange = pension.getPensionDateRange();
				String dates[] = dateRange.split(" - ");
				DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
				
				Date fromDate = formatter.parse(dates[0]);
				Date toDate = formatter.parse(dates[1]);
				pension.setPensionDateJoinedScheme(fromDate);
				pension.setPensionDateLeftScheme(toDate);
				
				if(pension.getPensionTrainer().getTrainerId() != null) {
					TeTrainers trainer = trainersService.getTrainer(pension.getPensionTrainer().getTrainerId());
					pension.setPensionTrainer(trainer);
				}
			}
		}
		emp.setTePensions(new HashSet<TePension>(emp.getPensions()));
		employeeService.saveOrUpdate(emp);
		
		Person person = createPerson(emp);
		personService.addPerson(person);
		model.addAttribute("emp", new TeEmployees());
		
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
