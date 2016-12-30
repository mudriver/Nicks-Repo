package ie.turfclub.trainers.service;

import ie.turfclub.common.bean.AdvanceSearchRecordBean;
import ie.turfclub.common.bean.SearchByNameEmployeeBean;
import ie.turfclub.common.enums.RoleEnum;
import ie.turfclub.common.service.NullAwareBeanUtilsBean;
import ie.turfclub.person.model.Person;
import ie.turfclub.person.service.PersonService;
import ie.turfclub.trainers.model.TeCards;
import ie.turfclub.trainers.model.TeEmployees;
import ie.turfclub.trainers.model.TeEmployentHistory;
import ie.turfclub.trainers.model.TePension;
import ie.turfclub.trainers.model.TeTrainers;
import ie.turfclub.utilities.EncryptDecryptUtils;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.Period;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

@PropertySource("classpath:ie/turfclub/trainers/resources/config/config.properties")
@Service
@Transactional
public class EmployeeServiceImpl implements EmployeeService {

	@Autowired
	private SessionFactory sessionFactory;
	@Resource
	private Environment env;
	@Autowired
	private StableStaffService stableStaffService;
	@Autowired
	private TrainersService trainersService;
	@Autowired
	private PersonService personService;
	@Autowired
	private MessageSource messageSource;
	
	List<String> sexEnum;
	List<String> maritalEnum;
	List<String> employmentCategoryEnum;
	List<String> cardTypeEnum;
	List<String> nationalityEnum;
	List<String> hoursWorkedEnum;
	List<String> titlesEnum;
	List<String> countiesEnum;
	List<String> countriesEnum;
	List<String> pensionEnum; 
	
	private Session getCurrentSession() {
		return sessionFactory.getCurrentSession();
	}
	
	@Override
	public void saveOrUpdate(TeEmployees emp) {
	
		emp.setEmployeesPpsNumber(EncryptDecryptUtils.encrypt(emp.getEmployeesPpsNumber()));
		getCurrentSession().saveOrUpdate(emp);
	}
	
	@Override
	public String getAllCardType() {
		
		cardTypeEnum = new ArrayList<String>();
		cardTypeEnum.add("A");
		cardTypeEnum.add("B");
		
		return getEnumCommonResponse(cardTypeEnum);
	}
	
	public String getEnumCommonResponse(List<String> records) {
		
		Gson gson = new Gson();
		List<Map<String, String>> titlesList = new ArrayList<>();
		for (String title : records) {
			Map<String, String> map = new HashMap<>();
			map.put("id", title);
			map.put("text", title);
			titlesList.add(map);
		}
		// convert java object to JSON format,
		// and returned as JSON formatted string
		String json = gson.toJson(titlesList);

		return json;
	}
	
	@Override
	public String getPension() {
		
		pensionEnum = new ArrayList<String>();
		pensionEnum.add("F");
		pensionEnum.add("P");
		pensionEnum.add("N");
		
		return getEnumCommonResponse(pensionEnum);
	}

	@Override
	public String getSexEnum() {
		
		sexEnum = stableStaffService.getSexEnum();
		
		return getEnumCommonResponse(sexEnum);
	}

	@Override
	public String getMaritalStatusEnum() {
		
		maritalEnum = stableStaffService.getMaritalStatusEnum();
		return getEnumCommonResponse(maritalEnum);
	}

	@Override
	public String getEmploymentCategoryEnum() {
		
		employmentCategoryEnum = stableStaffService.getEmploymentCategoryEnum();
		return getEnumCommonResponse(employmentCategoryEnum);
	}

	@Override
	public String getTitlesEnum() {
		
		return stableStaffService.getTitlesEnum();
	}

	@Override
	public String getCountiesEnum() {
		
		return stableStaffService.getCountiesEnum();
	}

	@Override
	public String getCountriesEnum() {
		
		return stableStaffService.getCountriesEnum();
	}
	
	@Override
	public String getNationalityEnum() {
		
		nationalityEnum = stableStaffService.getNationalityEnum();
		return getEnumCommonResponse(nationalityEnum);
	}
	
	@Override
	public List<HashMap<String, Object>> getAllCards() {
		
		Criteria criteria = getCurrentSession().createCriteria(TeCards.class);
		List<TeCards> cards = criteria.list();
		List<HashMap<String, Object>> records = new ArrayList<HashMap<String,Object>>();
		if(cards != null && cards.size() > 0) {
			for (TeCards teCards : cards) {
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("name", teCards.getCardsCardNumber());
				map.put("id", teCards.getCardsCardId());
				records.add(map);
			}
		}
		return records;
	}
	
	@Override
	public TeEmployees getEmployeeByCardId(Integer cardId) {
		
		Criteria criteria = getCurrentSession().createCriteria(TeCards.class);
		criteria.add(Restrictions.eq("cardsCardId", cardId));
		List<TeCards> cards = criteria.list();
		TeEmployees employee = null;
		if(cards != null && cards.size() > 0) {
			if(cards.get(0).getTeEmployees().getEmployeesEmployeeId() != null) {
				String sql = "From TeEmployees where employeesEmployeeId = "+cards.get(0).getTeEmployees().getEmployeesEmployeeId();
				List<TeEmployees> employees = getCurrentSession().createQuery(sql).list();
				if(employees != null && employees.size() > 0) {
					employees.get(0).setEmployeesPpsNumber(EncryptDecryptUtils.encrypt(employees.get(0).getEmployeesPpsNumber()));
					return employees.get(0);
				}
			}
		}
		return employee;
	}
	
	@Override
	public List<SearchByNameEmployeeBean> searchByNameEmployees() {
		
		Criteria criteria = getCurrentSession().createCriteria(TeEmployees.class);
		List<TeEmployees> employees = criteria.list();
		return convertEmployeesToBean(employees);
	}

	private List<SearchByNameEmployeeBean> convertEmployeesToBean(
			List<TeEmployees> employees) {
		
		List<SearchByNameEmployeeBean> records = new ArrayList<SearchByNameEmployeeBean>();
		for (TeEmployees teEmployees : employees) {
			SearchByNameEmployeeBean bean = new SearchByNameEmployeeBean();
			bean.setId(teEmployees.getEmployeesEmployeeId());
			if(teEmployees.getTeCard() != null) {
				bean.setCardType(teEmployees.getTeCard().getCardsCardType());
				bean.setCardNumber(teEmployees.getTeCard().getCardsCardNumber());
			}
			bean.setFullName(teEmployees.getEmployeesFullName());
			records.add(bean);
		}
		return records;
	}
	
	@Override
	public List<SearchByNameEmployeeBean> findByName(String search) {
		
		/*Criteria criteria = getCurrentSession().createCriteria(TeEmployees.class);
		criteria.add(Restrictions.ilike("employeesFullName", search, MatchMode.ANYWHERE));
		List<TeEmployees> employees = criteria.list();
		return convertEmployeesToBean(employees);*/
		List<SearchByNameEmployeeBean> records = personService.getEmployeeByName(search);
		return records;
	}
	
	@Override
	public TeEmployees getEmployeeById(Integer id) throws IllegalAccessException, InvocationTargetException {
		
		Criteria criteria = getCurrentSession().createCriteria(TeEmployees.class);
		criteria.add(Restrictions.eq("employeesEmployeeId", id));
		List<TeEmployees> employees = criteria.list();
		TeEmployees emp =  (employees != null && employees.size() > 0) ? employees.get(0) : null;
		
		if(emp.getTeEmployentHistories() != null && !emp.getTeEmployentHistories().isEmpty()) {
			List<TeEmployentHistory> histories = new ArrayList<TeEmployentHistory>();
			List<TeEmployentHistory> currHistories = new ArrayList<TeEmployentHistory>();
			currHistories.addAll(emp.getTeEmployentHistories());
			for (TeEmployentHistory teEmployentHistory : currHistories) {
				TeEmployentHistory history = new TeEmployentHistory();
				BeanUtilsBean notNull=new NullAwareBeanUtilsBean();
				notNull.copyProperties(history, teEmployentHistory);
				history.setTrainerName(history.getTeTrainers().getTrainerFullName());
				histories.add(history);
			}
			emp.setHistories(histories);
		}
		
		if(emp.getTePensions() != null && !emp.getTePensions().isEmpty()) {
			List<TePension> pensions = new ArrayList<TePension>();
			List<TePension> currPensions = new ArrayList<TePension>();
			currPensions.addAll(emp.getTePensions());
			for (TePension tePension : currPensions) {
				TePension pension = new TePension();
				BeanUtilsBean notNull=new NullAwareBeanUtilsBean();
				notNull.copyProperties(pension, tePension);
				pension.setPensionTrainerName(pension.getPensionTrainer().getTrainerFullName());
				pensions.add(pension);
			}
			emp.setPensions(pensions);
		}
		LocalDate ld = LocalDate.fromDateFields(emp.getEmployeesDateOfBirth());

		Period p = Period.fieldDifference(ld, LocalDate.now());
		emp.setAge(p.getYears());
		emp.setEmployeesPpsNumber(EncryptDecryptUtils.decrypt(emp.getEmployeesPpsNumber()));
 		return emp;
	}
	
	@Override
	public List<SearchByNameEmployeeBean> findByNumber(String search) {
		
		/*String sql = "select e from TeEmployees e, TeCards c where c.cardsCardId = e.teCard.cardsCardId"
				+ " and lower(c.cardsCardNumber) like lower('%"+search+"%')";
		
		List<TeEmployees> employees = getCurrentSession().createQuery(sql).list();
		return convertEmployeesToBean(employees);*/
		
		List<SearchByNameEmployeeBean> records = personService.getEmployeeByCardNumber(search);
		return records;
	}
	
	@Override
	public void deleteRecordById(Integer id) throws IllegalAccessException, InvocationTargetException {
	
		TeEmployees emp = getEmployeeById(id);
		getCurrentSession().delete(emp);
	}
	
	@Override
	public HashMap<String, Object> getPartFullTimeRecords(String hours) {
		
		HashMap<String, Object> records = new HashMap<String, Object>();
		
		Criteria partTimeCriteria = getCurrentSession().createCriteria(TeEmployentHistory.class);
		Date startDate = new DateTime().dayOfYear().withMinimumValue().toDate();
		Date endDate = new DateTime().dayOfYear().withMaximumValue().toDate();
		partTimeCriteria.setProjection(Projections.rowCount());
		partTimeCriteria.add(
				Restrictions.or(
						Restrictions.between("ehDateFrom", startDate, endDate), 
						Restrictions.between("ehDateTo", startDate, endDate)));
		partTimeCriteria.add(Restrictions.lt("employeeNumHourWorked", Integer.parseInt(hours)));
		Long partTimeCount = (Long) partTimeCriteria.uniqueResult();
		Criteria fullTimeCriteria = getCurrentSession().createCriteria(TeEmployentHistory.class);
		fullTimeCriteria.setProjection(Projections.rowCount());
		fullTimeCriteria.add(
				Restrictions.or(
						Restrictions.between("ehDateFrom", startDate, endDate), 
						Restrictions.between("ehDateTo", startDate, endDate)));
		fullTimeCriteria.add(Restrictions.ge("employeeNumHourWorked", Integer.parseInt(hours)));
		Long fullTimeCount = (Long) fullTimeCriteria.uniqueResult();
		records.put("partTime", partTimeCount);
		records.put("fullTime", fullTimeCount);
		return records;
	}
	
	@Override
	public void handleSaveOrUpdate(TeEmployees emp) throws Exception {
		
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
		
		saveOrUpdate(emp);
		
		Person person = createPerson(emp);
		personService.addPerson(person);
	}
	
	@Override
	public HashMap<String, Object> getAdvanceSearchRecordByType(
			String type, int start, int length, int draw) {
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		int count = 0;
		List<AdvanceSearchRecordBean> records = new ArrayList<AdvanceSearchRecordBean>();
		switch(type) {
			case "1":
				
				count = personService.getAdvanceSearchRecordForAllACardCount();
				records = personService.getAdvanceSearchRecordForAllACardByLimit(start, length);
				break;
			case "2":
				count = personService.getAdvanceSearchRecordForAllBCardCount();
				records = personService.getAdvanceSearchRecordForAllBCardByLimit(start, length);
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
		
		AdvanceSearchRecordBean[] beanArr = new AdvanceSearchRecordBean[records.size()];
		beanArr = records.toArray(beanArr);
		
		map.put("data", beanArr);
		map.put("draw", draw);
		map.put("recordsTotal", count);
		map.put("recordsFiltered", count);
		
		return map;
	}
	
	//set all value from employee to person object
	private Person createPerson(TeEmployees emp) {
		
		Person person = new Person();
		person.setRefId(emp.getEmployeesEmployeeId());
		person.setSurname(emp.getEmployeesSurname());
		person.setFirstname(emp.getEmployeesFirstname());
		person.setDateOfBirth(emp.getEmployeesDateOfBirth());
		person.setRequestDate(emp.getEmployeeRequestDate());
		person.setDateEntered(emp.getEmployeesDateEntered());
		person.setAddress1(emp.getEmployeesAddress1());
		person.setAddress2(emp.getEmployeesAddress2());
		person.setAddress3(emp.getEmployeesAddress3());
		person.setPhoneNo(emp.getEmployeesPhoneNo());
		person.setMobileNo(emp.getEmployeesMobileNo());
		person.setEmail(emp.getEmployeesEmail());
		person.setComments(emp.getEmployeesComments());
		person.setRoleId(RoleEnum.EMPLOYEE.getId());
		person.setTitle(emp.getEmployeesTitle());
		person.setSex(emp.getEmployeesSex());
		person.setNationality(emp.getEmployeesNationality());
		person.setMaritalStatus(emp.getEmployeesMaritalStatus());
		person.setSpouseName(emp.getEmployeesSpouseName());
		person.setCounty(emp.getEmployeesAddress4());
		person.setCountry(emp.getEmployeesAddress5());
		person.setPostCode(emp.getEmployeesPostCode());
		person.setCardType(emp.getTeCard() != null ? emp.getTeCard().getCardsCardType() : null);
		person.setCardNumber(emp.getTeCard() != null ? emp.getTeCard().getCardsCardNumber() : null);
		if(emp.getTeEmployentHistories() != null && !emp.getTeEmployentHistories().isEmpty()) {
			List<TeEmployentHistory> histories = new ArrayList<TeEmployentHistory>();
			histories.addAll(emp.getTeEmployentHistories());
			
			try {
				Collections.sort(histories, new Comparator<TeEmployentHistory>() {
				    @Override
				    public int compare(TeEmployentHistory o1, TeEmployentHistory o2) {
				        return -1 * o1.getEhDateFrom().compareTo(o2.getEhDateFrom());
				    }
				});
				if(histories != null && histories.size() > 0 && histories.get(0).getTeTrainers() != null) {
					person.setTrainerName(histories.get(0).getTeTrainers().getTrainerFullName());
				}
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		return person;
	}
	
	@Override
	public void handleCopyRecord() throws SQLException {
		
		Criteria criteria = getCurrentSession().createCriteria(TeEmployees.class);
		List<TeEmployees> employees = criteria.list();
		if(employees != null) {
			for (TeEmployees employee : employees) {
				
				Person person = createPerson(employee);
				personService.addPerson(person);
			}
		}
	}
	
	@Override
	public HashMap<String, Object> getCSVString(String type, String title) {
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		List<AdvanceSearchRecordBean> records = new ArrayList<AdvanceSearchRecordBean>();
		switch(type) {
			case "1":
				records = personService.getAdvanceSearchRecordForAllACard();
				break;
			case "2":
				records = personService.getAdvanceSearchRecordForAllBCard();
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
		
		map.put("csvstring", getCSVStringByData(records, title));
		return map;
	}

	/**
	 * Get CSV String for exportCSV file
	 * 
	 * @param records
	 * @return
	 */
	private String getCSVStringByData(List<AdvanceSearchRecordBean> records, String title) {
		
		String csvString = ",,";
		csvString += title+",";
		csvString += "\n\n";
		csvString += "Card Number,Name,Date Of Birth,Current/Last Trainer\n";
		if(records != null && records.size() > 0) {
			for (AdvanceSearchRecordBean bean : records) {
				csvString += bean.getCardNumber()+","+bean.getName()+","+bean.getDateOfBirth()+","+bean.getTrainerName()+"\n";
			}
		}
		return csvString;
	}
	
	@Override
	public void handleEncryptPPSNumber() {
		
		Criteria criteria = getCurrentSession().createCriteria(TeEmployees.class);
		List<TeEmployees> employees = criteria.list();
		if(employees != null && employees.size() > 0) {
			for (TeEmployees teEmployees : employees) {
				if(teEmployees.getEmployeesPpsNumber() != null && teEmployees.getEmployeesPpsNumber().length() <= 8) {
					
					teEmployees.setEmployeesPpsNumber(EncryptDecryptUtils.encrypt(teEmployees.getEmployeesPpsNumber()));
					getCurrentSession().saveOrUpdate(teEmployees);
				}
			}
		}
	}
}
