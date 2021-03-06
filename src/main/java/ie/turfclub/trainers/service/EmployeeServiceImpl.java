package ie.turfclub.trainers.service;

import ie.turfclub.common.bean.AdvanceSearchRecordBean;
import ie.turfclub.common.bean.EmployeePPSBean;
import ie.turfclub.common.bean.InapproveEmployeeBean;
import ie.turfclub.common.bean.SearchByNameEmployeeBean;
import ie.turfclub.common.enums.RoleEnum;
import ie.turfclub.common.enums.TrainerLicenseEnum;
import ie.turfclub.common.service.NullAwareBeanUtilsBean;
import ie.turfclub.person.model.Person;
import ie.turfclub.person.service.PersonService;
import ie.turfclub.trainers.model.TeCards;
import ie.turfclub.trainers.model.TeCardsApproved;
import ie.turfclub.trainers.model.TeEmployees;
import ie.turfclub.trainers.model.TeEmployeesApproved;
import ie.turfclub.trainers.model.TeEmployentHistory;
import ie.turfclub.trainers.model.TeEmploymentApprovedHistory;
import ie.turfclub.trainers.model.TePension;
import ie.turfclub.trainers.model.TePensionApproved;
import ie.turfclub.trainers.model.TeTrainers;
import ie.turfclub.utilities.EncryptDecryptUtils;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
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
import org.thymeleaf.util.StringUtils;

import com.fasterxml.jackson.databind.deser.DataFormatReaders.Match;
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
		/*if(emp.getTeCard() != null && emp.getTeCard().getCardsCardId() != null && emp.getTeCard().getCardsCardId() > 0) {
			List<TeCards> cards = sessionFactory.getCurrentSession().createCriteria(TeCards.class)
			.add(Restrictions.eq("cardsCardId", emp.getTeCard().getCardsCardId())).list();
			if(cards != null && cards.size() > 0 ) {
				sessionFactory.getCurrentSession().delete(cards.get(0));
			}
			emp.getTeCard().setCardsCardId(null);
		}*/
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
		if(records != null) {
			for(SearchByNameEmployeeBean bean : records) {
				String hql1 = "select new map(cardsCardType as type, cardsCardNumber as number) from TeCards"
						+ " where teEmployees.employeesEmployeeId = "+bean.getId();
				List<HashMap<String, Object>> maps = getCurrentSession().createQuery(hql1).list();
				HashMap<String, Object> mapRecord = (maps != null && maps.size() > 0) ? maps.get(0) : null;
				if(mapRecord != null && mapRecord.get("type") != null) {
					bean.setCardNumber(Integer.parseInt(String.valueOf(mapRecord.get("number"))));
					bean.setCardType((String)mapRecord.get("type"));
				}
			}
		}
		return records;
	}
	
	@Override
	public TeEmployeesApproved getEmployeeApprovedById(Integer id)
			throws IllegalAccessException, InvocationTargetException {
	
		Criteria criteria = getCurrentSession().createCriteria(TeEmployeesApproved.class);
		criteria.add(Restrictions.eq("employeesEmployeeId", id));
		List<TeEmployeesApproved> employees = criteria.list();
		TeEmployeesApproved emp =  (employees != null && employees.size() > 0) ? employees.get(0) : null;
		
		criteria = getCurrentSession().createCriteria(TeEmploymentApprovedHistory.class);
		criteria.add(Restrictions.eq("teEmployees.employeesEmployeeId", id));
		List<TeEmploymentApprovedHistory> currHistories = criteria.list();
		if(currHistories != null && currHistories.size() > 0) {
			List<TeEmploymentApprovedHistory> histories = new ArrayList<TeEmploymentApprovedHistory>();
			for (TeEmploymentApprovedHistory teEmployentHistory : currHistories) {
				TeEmploymentApprovedHistory history = new TeEmploymentApprovedHistory();
				BeanUtilsBean notNull=new NullAwareBeanUtilsBean();
				notNull.copyProperties(history, teEmployentHistory);
				if(history.getTeTrainers() != null)
					history.setTrainerName(history.getTeTrainers().getTrainerFullName());
				histories.add(history);
			}
			emp.setHistories(histories);
		}
		
		criteria = getCurrentSession().createCriteria(TePensionApproved.class);
		criteria.add(Restrictions.eq("teEmployees.employeesEmployeeId", id));
		List<TePensionApproved> currPensions = criteria.list();
		
		if(emp.getTePensions() != null && !emp.getTePensions().isEmpty()) {
			List<TePensionApproved> pensions = new ArrayList<TePensionApproved>();
			for (TePensionApproved tePension : currPensions) {
				TePensionApproved pension = new TePensionApproved();
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
		//emp.setEmployeesPpsNumber(EncryptDecryptUtils.decrypt(emp.getEmployeesPpsNumber()));
 		return emp;
	}
	
	@Override
	public TeEmployees getEmployeeById(Integer id) throws IllegalAccessException, InvocationTargetException {
		
		Criteria criteria = getCurrentSession().createCriteria(TeEmployees.class);
		criteria.add(Restrictions.eq("employeesEmployeeId", id));
		List<TeEmployees> employees = criteria.list();
		List<TeEmployentHistory> groupHistories = new ArrayList<TeEmployentHistory>();
		TeEmployees emp =  (employees != null && employees.size() > 0) ? employees.get(0) : null;
		
		criteria = getCurrentSession().createCriteria(TeEmployentHistory.class);
		criteria.add(Restrictions.eq("teEmployees.employeesEmployeeId", id));
		List<TeEmployentHistory> currHistories = criteria.list();
		if(currHistories != null && currHistories.size() > 0) {
			List<TeEmployentHistory> histories = new ArrayList<TeEmployentHistory>();
			for (TeEmployentHistory teEmployentHistory : currHistories) {
				TeEmployentHistory history = new TeEmployentHistory();
				BeanUtilsBean notNull=new NullAwareBeanUtilsBean();
				notNull.copyProperties(history, teEmployentHistory);
				if(history.getTeTrainers() != null)
					history.setTrainerName(history.getTeTrainers().getTrainerFullName());
				histories.add(history);
			}
			
			Collections.sort(histories, new Comparator<TeEmployentHistory>() {
				public int compare(TeEmployentHistory h1, TeEmployentHistory h2) {
					return h1.getEhDateFrom().compareTo(h2.getEhDateFrom());
				}
			});
			
			Date startDate = null;
			Date endDate = null;
			String fullname = null;
			for (int i=0; i<histories.size();i++) {
				TeEmployentHistory teEmployentHistory = histories.get(i);
				String currEmpFullname = teEmployentHistory.getTeTrainers().getTrainerFullName();
				Date teEmpDateTo = teEmployentHistory.getEhDateTo();
				Date teEmpDateFrom = teEmployentHistory.getEhDateFrom();
				if(i == 0) {
					startDate = teEmployentHistory.getEhDateFrom();
					endDate = teEmployentHistory.getEhDateTo();
					fullname = teEmployentHistory.getTeTrainers().getTrainerFullName();
					continue;
				}
				Calendar cal1 = Calendar.getInstance();
				Calendar cal2 = Calendar.getInstance();
				if(endDate != null) cal1.setTime(endDate);
				if(teEmpDateTo != null) {
					cal2.setTime(teEmpDateFrom);
					cal2.add(Calendar.DATE, -1);
				}
				if(fullname != null && i != (histories.size()-1) &&
						((!fullname.equalsIgnoreCase(currEmpFullname))
						|| (teEmpDateTo == null)
						|| (teEmpDateTo != null && endDate != null && cal1.get(Calendar.YEAR) != cal2.get(Calendar.YEAR) && 
						cal1.get(Calendar.DAY_OF_YEAR) != cal2.get(Calendar.DAY_OF_YEAR)))) {
					TeEmployentHistory groupHistory = histories.get(i-1);
					groupHistory.setEhDateFrom(startDate);
					groupHistory.setHriAccNum(groupHistory.getTeTrainers().getTrainerAccountNo());
					groupHistories.add(groupHistory);
					startDate = teEmployentHistory.getEhDateFrom();
					fullname = teEmployentHistory.getTeTrainers().getTrainerFullName();
				}
				endDate = teEmployentHistory.getEhDateTo();
				if(i == (histories.size() - 1)) {
					TeEmployentHistory groupHistory = histories.get(i);
					groupHistory.setEhDateFrom(startDate);
					groupHistory.setHriAccNum(groupHistory.getTeTrainers().getTrainerAccountNo());
					groupHistories.add(groupHistory);
				}
			}
			
			emp.setHistories(histories);
		}
		
		String hql = "select new map(teTrainers.trainerId as tid, min(ehDateFrom) as from, max(ehDateTo) as to, "
				+ "avg(employeeNumHourWorked) as worked) from TeEmployentHistory where "
				+ "teEmployees.employeesEmployeeId="+id+" group by teTrainers.trainerId";
		List<HashMap<String, Object>> records = getCurrentSession().createQuery(hql).list();
		
		criteria = getCurrentSession().createCriteria(TePension.class);
		criteria.add(Restrictions.eq("teEmployees.employeesEmployeeId", id));
		List<TePension> currPensions = criteria.list();
		
		if(emp.getTePensions() != null && !emp.getTePensions().isEmpty()) {
			List<TePension> pensions = new ArrayList<TePension>();
			for (TePension tePension : currPensions) {
				TePension pension = new TePension();
				BeanUtilsBean notNull=new NullAwareBeanUtilsBean();
				notNull.copyProperties(pension, tePension);
				if(pension.getPensionTrainer() != null)
					pension.setPensionTrainerName(pension.getPensionTrainer().getTrainerFullName());
				pensions.add(pension);
			}
			emp.setPensions(pensions);
		}
		LocalDate ld = LocalDate.fromDateFields(emp.getEmployeesDateOfBirth());

		//Handle Phone and Mobile Number
		if(emp.getEmployeesPhoneNo() != null) emp.setEmployeesPhoneNo(emp.getEmployeesPhoneNo().replaceAll("\\D+", ""));
		if(emp.getEmployeesMobileNo() != null) emp.setEmployeesMobileNo(emp.getEmployeesMobileNo().replaceAll("\\D+", ""));
		
		Period p = Period.fieldDifference(ld, LocalDate.now());
		emp.setAge(p.getYears());
		if(emp.getHistories().size() == 1)
			emp.setGroupHistories(emp.getHistories());
		else
			emp.setGroupHistories(groupHistories);
		if(emp.getEmployeesPpsNumber() != null && emp.getEmployeesPpsNumber().length() > 10)
			emp.setEmployeesPpsNumber(EncryptDecryptUtils.decrypt(emp.getEmployeesPpsNumber()));
 		return emp;
	}
	
	@Override
	public List<SearchByNameEmployeeBean> findByNumber(String search) {
		
		String sql = "select teEmployees.employeesEmployeeId from TeCards  "
				+ " where lower(cardsCardNumber) like lower('%"+search+"%')";
		
		List<Integer> empIds = getCurrentSession().createQuery(sql).list();
		String ids = StringUtils.join(empIds, "','");
		ids = "'"+ids+"'";
		
		List<SearchByNameEmployeeBean> records = personService.getEmployeeByCardNumber(ids);
		if(records != null) {
			for(SearchByNameEmployeeBean bean : records) {
				String hql1 = "select new map(cardsCardType as type, cardsCardNumber as number) from TeCards"
						+ " where teEmployees.employeesEmployeeId = "+bean.getId();
				List<HashMap<String, Object>> maps = getCurrentSession().createQuery(hql1).list();
				HashMap<String, Object> mapRecord = (maps != null && maps.size() > 0) ? maps.get(0) : null;
				if(mapRecord != null && mapRecord.get("type") != null) {
					bean.setCardNumber(Integer.parseInt(String.valueOf(mapRecord.get("number"))));
					bean.setCardType((String)mapRecord.get("type"));
				}
			}
		}
		return records;
	}
	
	@Override
	public void deleteRecordById(Integer id) throws IllegalAccessException, InvocationTargetException {
	
		TeEmployees emp = getEmployeeById(id);
		getCurrentSession().delete(emp);
	}
	
	@Override
	public void deleteEmployeeApprovedById(Integer id) throws IllegalAccessException, InvocationTargetException {
		
		TeEmployeesApproved emp = getEmployeeApprovedById(id);
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
		if(emp.getEmployeesEmployeeId() != null && emp.getEmployeesEmployeeId() > 0) {
			Criteria cardcriteria = getCurrentSession().createCriteria(TeCards.class);
			cardcriteria.add(Restrictions.eq("teEmployees.employeesEmployeeId", emp.getEmployeesEmployeeId()));
			List<TeCards> cards = cardcriteria.list();
			if(cards != null && cards.size() > 0) {
				//emp.getTeCard().setCardsCardId(cards.get(0).getCardsCardId());
				sessionFactory.getCurrentSession().delete(cards.get(0));
				//sessionFactory.getCurrentSession().flush();
			}
		}
		emp.getTeCard().setCardsCardStatus("Applied");
		emp.getTeCard().setTeEmployees(emp);
		
		
		/*if(emp.getEmployeesEmployeeId() == null || emp.getEmployeesEmployeeId() == 0) {*/
			TeTrainers ptrainer = null;
			Date startDate = null;
			if(emp.getHistories() != null && emp.getHistories().size() > 0) {
				for (TeEmployentHistory history : emp.getHistories()) {
					history.setTeEmployees(emp);
					history.setCardType(emp.getTeCard().getCardsCardType());
					if(history.getTeTrainers().getTrainerId() != null) {
						TeTrainers trainer = trainersService.getTrainer(history.getTeTrainers().getTrainerId());
						history.setTeTrainers(trainer);
						if(ptrainer == null) {
							ptrainer = trainer;
							startDate = history.getEhDateFrom();
						}
					}
					if(history.getEhEmploymentCategory() != null && history.getEhEmploymentCategory().length() == 0)
						history.setEhEmploymentCategory(null);
				}
				emp.setTeEmployentHistories(new HashSet<TeEmployentHistory>(emp.getHistories()));
				if(emp.getHistories().get(emp.getHistories().size()-1).getEmployeeNumHourWorked() > 0)
					emp.setEmployeeNumHourWorked((double)emp.getHistories().get(emp.getHistories().size()-1).getEmployeeNumHourWorked());
			}
			
			if(emp.getPensions() != null && emp.getPensions().size() > 0) {
				for (TePension pension : emp.getPensions()) {
					
					pension.setTeEmployees(emp);
					
					/*if(pension.getPensionTrainer().getTrainerId() != null) {
						TeTrainers trainer = trainersService.getTrainer(pension.getPensionTrainer().getTrainerId());
						pension.setPensionTrainer(trainer);
					}*/
					pension.setPensionTrainer(ptrainer);
					pension.setPensionDateJoinedScheme(startDate);
				}
				emp.setTePensions(new HashSet<TePension>(emp.getPensions()));
			}
		/*} else {
			emp.setTeEmployentHistories(new HashSet<TeEmployentHistory>(emp.getHistories()));
			emp.setTePensions(new HashSet<TePension>(emp.getPensions()));
		}*/
		
		saveOrUpdate(emp);
		
		if(emp.getEmployeeEndDate() != null && emp.getEmployeeTrainer() != null && emp.getEmployeeTrainer() > 0) {
			Criteria criteria = getCurrentSession().createCriteria(TeEmployentHistory.class);
			criteria.add(Restrictions.eq("teEmployees.employeesEmployeeId", emp.getEmployeesEmployeeId()));
			criteria.add(Restrictions.eq("teTrainers.trainerId", emp.getEmployeeTrainer()));
			criteria.add(Restrictions.isNull("ehDateTo"));
			List<TeEmployentHistory> records = criteria.list();
			List<TeTrainers> trainers = new ArrayList<TeTrainers>();
			if(records != null && records.size() > 0) {
				for(TeEmployentHistory history : records) {
					history.setEhDateTo(emp.getEmployeeEndDate());
					getCurrentSession().save(history);
				}
			}
		}
		
		Person person = createPerson(emp);
		personService.addPerson(person);
	}
	
	@Override
	public void handleApproveEmployee(Integer id) throws Exception {
		
		TeEmployees emp = getEmployeeFromEmployeeApprovedRecord(id);
		//emp.setEmployeesPpsNumber(EncryptDecryptUtils.encrypt(emp.getEmployeesPpsNumber()));
		getCurrentSession().saveOrUpdate(emp);
		
		Person person = createPerson(emp);
		personService.addPerson(person);
		
		this.deleteEmployeeApprovedById(id);
	}
	
	private TeEmployees getEmployeeFromEmployeeApprovedRecord(Integer id) throws Exception {
		
		//Set TeEmployeesApproved value to TeEmployees
		TeEmployeesApproved empApproved = getEmployeeApprovedById(id);
		TeEmployees emp = new TeEmployees();
		emp.setEmployeesPpsNumber(empApproved.getEmployeesPpsNumber());
		emp.setEmployeesTitle(empApproved.getEmployeesTitle());
		emp.setEmployeesSurname(empApproved.getEmployeesSurname());
		emp.setEmployeesFirstname(empApproved.getEmployeesFirstname());
		emp.setEmployeesFullName(empApproved.getEmployeesFullName());
		emp.setEmployeesDateOfBirth(empApproved.getEmployeesDateOfBirth());
		emp.setEmployeesNationality(empApproved.getEmployeesNationality());
		emp.setEmployeesMaritalStatus(empApproved.getEmployeesMaritalStatus());
		emp.setEmployeesSpouseName(empApproved.getEmployeesSpouseName());
		emp.setEmployeesSex(empApproved.getEmployeesSex());
		emp.setEmployeesAddress1(empApproved.getEmployeesAddress1());
		emp.setEmployeesAddress2(empApproved.getEmployeesAddress2());
		emp.setEmployeesAddress3(empApproved.getEmployeesAddress3());
		emp.setEmployeesAddress4(empApproved.getEmployeesAddress4());
		emp.setEmployeesAddress5(empApproved.getEmployeesAddress5());
		emp.setEmployeesPostCode(empApproved.getEmployeesPostCode());
		emp.setEmployeesPhoneNo(empApproved.getEmployeesPhoneNo());
		emp.setEmployeesMobileNo(empApproved.getEmployeesMobileNo());
		emp.setEmployeesEmail(empApproved.getEmployeesEmail());
		emp.setEmployeesComments(empApproved.getEmployeesComments());
		emp.setEmployeesHriAccountNo(empApproved.getEmployeesHriAccountNo());
		emp.setEmployeesIsNew(empApproved.getEmployeesIsNew());
		emp.setEmployeesHasTaxableEarnings(empApproved.getEmployeesHasTaxableEarnings());
		emp.setEmployeesDateEntered(empApproved.getEmployeesDateEntered());
		emp.setEmployeesLastUpdated(empApproved.getEmployeesLastUpdated());
		
		emp.setEmployeeRequestDate(empApproved.getEmployeeRequestDate());
		emp.setEmployeeExistingAIRCardHolder(empApproved.getEmployeeExistingAIRCardHolder());
		emp.setEmployeeOldEmployeeCardNumber(empApproved.getEmployeeOldEmployeeCardNumber());
		emp.setEmployeeCategoryOfEmployment(empApproved.getEmployeeCategoryOfEmployment());
		emp.setEmployeeLastYearPaid(empApproved.getEmployeeLastYearPaid());
		emp.setEmployeeNumHourWorked(empApproved.getEmployeeNumHourWorked());
		emp.setEmployeeVerified(empApproved.isEmployeeVerified());
		
		//Set TeCardsApproved value to TeCards value
		TeCards cards = new TeCards();
		TeCardsApproved cardsApproved = empApproved.getTeCard();
		cards.setTeEmployees(emp);
		cards.setCardsCardNumber(cardsApproved.getCardsCardNumber());
		cards.setCardsCardReturnedToOffice(cardsApproved.getCardsCardReturnedToOffice());
		cards.setCardsCardStatus(cardsApproved.getCardsCardStatus());
		cards.setCardsCardType(cardsApproved.getCardsCardType());
		cards.setCardsIssueDate(cardsApproved.getCardsIssueDate());
		cards.setCardsPreviousAirCardHolder(cardsApproved.getCardsPreviousAirCardHolder());
		emp.setTeCard(cards);
		
		List<TeEmployentHistory> empHistories = new ArrayList<TeEmployentHistory>();
		Criteria criteria = getCurrentSession().createCriteria(TeEmploymentApprovedHistory.class);
		criteria.add(Restrictions.eq("teEmployees.employeesEmployeeId", id));
		List<TeEmploymentApprovedHistory> empApprovedHistories = criteria.list();
		if(empApprovedHistories != null && empApprovedHistories.size() > 0) {
			for (TeEmploymentApprovedHistory empApprovedHistory : empApprovedHistories) {
				TeEmployentHistory history = new TeEmployentHistory();
				history.setTeEmployees(emp);
				history.setEhDateFrom(empApprovedHistory.getEhDateFrom());
				history.setEhDateTo(empApprovedHistory.getEhDateTo());
				history.setEhEarnings(empApprovedHistory.getEhEarnings());
				//history.setEhEmploymentCategory(empApprovedHistory.getEhEmploymentCategory());
				history.setEhHoursWorked(empApprovedHistory.getEhHoursWorked());
				history.setEhPpsNumber(empApprovedHistory.getEhPpsNumber());
				history.setEhTempCategory(empApprovedHistory.getEhTempCategory());
				history.setEhVerified(empApprovedHistory.isEhVerified());
				history.setEhYear(empApprovedHistory.getEhYear());
				history.setEmployeeNumHourWorked(empApprovedHistory.getEmployeeNumHourWorked());
				history.setStartDate(empApprovedHistory.getStartDate());
				history.setTeTrainers(empApprovedHistory.getTeTrainers());
				history.setTrainerName(empApprovedHistory.getTrainerName());
				history.setCardType(empApprovedHistory.getCardType());
				history.setPensionType(empApprovedHistory.getPensionType());
				history.setHriAccNum(empApprovedHistory.getHriAccNum());
				empHistories.add(history);
			}
		}
		emp.setHistories(empHistories);
		emp.setTeEmployentHistories(new HashSet<TeEmployentHistory>(emp.getHistories()));
		
		List<TePension> pensions = new ArrayList<TePension>();
		criteria = getCurrentSession().createCriteria(TePensionApproved.class);
		criteria.add(Restrictions.eq("teEmployees.employeesEmployeeId", id));
		List<TePensionApproved> pensionApprovedHistories = criteria.list();
		if(pensionApprovedHistories != null && pensionApprovedHistories.size() > 0) {
			for (TePensionApproved tePensionApproved : pensionApprovedHistories) {
				TePension pension = new TePension();
				//pension.setPensionCardType(tePensionApproved.getPensionCardType());
				pension.setPensionDateJoinedScheme(tePensionApproved.getPensionDateJoinedScheme());
				pension.setPensionDateLeftScheme(tePensionApproved.getPensionDateLeftScheme());
				pension.setPensionDateRange(tePensionApproved.getPensionDateRange());
				//pension.setPensionHRIAccNum(tePensionApproved.getPensionHRIAccNum());
				pension.setPensionTrainer(tePensionApproved.getPensionTrainer());
				pension.setPensionTrainerName(tePensionApproved.getPensionTrainerName());
				pension.setEmploymentCategory(tePensionApproved.getEmploymentCategory());
				//pension.setPensionType(tePensionApproved.getPensionType());
				pension.setTeEmployees(emp);
				pensions.add(pension);
			}
		}
		emp.setPensions(pensions);
		emp.setTePensions(new HashSet<TePension>(emp.getPensions()));
		
		return emp; 
	}

	@Override
	public HashMap<String, Object> getAdvanceSearchRecordByType(
			String type, int start, int length, int draw) {
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		int count = 0;
		List<AdvanceSearchRecordBean> records = new ArrayList<AdvanceSearchRecordBean>();
		switch(type) {
			case "1":
				String hql = "select teEmployees.employeesEmployeeId from TeCards where cardsCardType = 'A'";
				List<Integer> empIds = getCurrentSession().createQuery(hql).list();
				String ids = StringUtils.join(empIds, "','");
				ids = "'"+ids+"'";
				count = personService.getAdvanceSearchRecordForAllACardCount(ids);
				records = personService.getAdvanceSearchRecordForAllACardByLimit(start, length, ids);
				if(records != null) {
					for(AdvanceSearchRecordBean record: records) {
						String hql1 = "select new map(cardsCardType as type, cardsCardNumber as number) from TeCards"
								+ " where teEmployees.employeesEmployeeId = "+record.getId();
						List<HashMap<String, Object>> maps = getCurrentSession().createQuery(hql1).list();
						HashMap<String, Object> mapRecord = (maps != null && maps.size() > 0) ? maps.get(0) : null;
						if(mapRecord != null && mapRecord.get("type") != null) 
							record.setCardNumber(mapRecord.get("type")+" "+mapRecord.get("number"));
					}
				}
				break;
			case "2":
				hql = "select teEmployees.employeesEmployeeId from TeCards where cardsCardType = 'B'";
				empIds = getCurrentSession().createQuery(hql).list();
				ids = StringUtils.join(empIds, "','");
				ids = "'"+ids+"'";
				count = personService.getAdvanceSearchRecordForAllBCardCount(ids);
				records = personService.getAdvanceSearchRecordForAllBCardByLimit(start, length, ids);
				if(records != null) {
					for(AdvanceSearchRecordBean record: records) {
						String hql1 = "select new map(cardsCardType as type, cardsCardNumber as number) from TeCards"
								+ " where teEmployees.employeesEmployeeId = "+record.getId();
						List<HashMap<String, Object>> maps = getCurrentSession().createQuery(hql1).list();
						HashMap<String, Object> mapRecord = (maps != null && maps.size() > 0) ? maps.get(0) : null;
						if(mapRecord != null && mapRecord.get("type") != null) 
							record.setCardNumber(mapRecord.get("type")+" "+mapRecord.get("number"));
					}
				}
				break;
			case "3":
				hql = "select c.teEmployees.employeesEmployeeId from TeCards c, "
						+ " TeEmployentHistory  eh where c.teEmployees.employeesEmployeeId = eh.teEmployees.employeesEmployeeId"
						+ " and c.cardsCardType = 'A' and eh.ehDateTo is null ";
				empIds = getCurrentSession().createQuery(hql).list();
				ids = StringUtils.join(empIds, "','");
				ids = "'"+ids+"'";
				count = personService.getAdvanceSearchRecordForAllACardCount(ids);
				records = personService.getAdvanceSearchRecordForAllACardByLimit(start, length, ids);
				if(records != null) {
					for(AdvanceSearchRecordBean record: records) {
						String hql1 = "select new map(cardsCardType as type, cardsCardNumber as number) from TeCards"
								+ " where teEmployees.employeesEmployeeId = "+record.getId();
						List<HashMap<String, Object>> maps = getCurrentSession().createQuery(hql1).list();
						HashMap<String, Object> mapRecord = (maps != null && maps.size() > 0) ? maps.get(0) : null;
						if(mapRecord != null && mapRecord.get("type") != null) 
							record.setCardNumber(mapRecord.get("type")+" "+mapRecord.get("number"));
					}
				}
				break;
			case "4":
				hql = "select c.teEmployees.employeesEmployeeId from TeCards c, "
						+ " TeEmployentHistory  eh where c.teEmployees.employeesEmployeeId = eh.teEmployees.employeesEmployeeId"
						+ " and c.cardsCardType = 'B' and eh.ehDateTo is null ";
				empIds = getCurrentSession().createQuery(hql).list();
				ids = StringUtils.join(empIds, "','");
				ids = "'"+ids+"'";
				count = personService.getAdvanceSearchRecordForAllBCardCount(ids);
				records = personService.getAdvanceSearchRecordForAllBCardByLimit(start, length, ids);
				if(records != null) {
					for(AdvanceSearchRecordBean record: records) {
						String hql1 = "select new map(cardsCardType as type, cardsCardNumber as number) from TeCards"
								+ " where teEmployees.employeesEmployeeId = "+record.getId();
						List<HashMap<String, Object>> maps = getCurrentSession().createQuery(hql1).list();
						HashMap<String, Object> mapRecord = (maps != null && maps.size() > 0) ? maps.get(0) : null;
						if(mapRecord != null && mapRecord.get("type") != null) 
							record.setCardNumber(mapRecord.get("type")+" "+mapRecord.get("number"));
					}
				}
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
		person.setLicensed(TrainerLicenseEnum.UNLICENSED.getId());
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
				String hql = "select teEmployees.employeesEmployeeId from TeCards where cardsCardType = 'A'";
				List<Integer> empIds = getCurrentSession().createQuery(hql).list();
				String ids = StringUtils.join(empIds, "','");
				ids = "'"+ids+"'";
				records = personService.getAdvanceSearchRecordForAllACard(ids);
				if(records != null) {
					for(AdvanceSearchRecordBean record: records) {
						String hql1 = "select new map(cardsCardType as type, cardsCardNumber as number) from TeCards"
								+ " where teEmployees.employeesEmployeeId = "+record.getId();
						List<HashMap<String, Object>> maps = getCurrentSession().createQuery(hql1).list();
						HashMap<String, Object> mapRecord = (maps != null && maps.size() > 0) ? maps.get(0) : null;
						if(mapRecord != null && mapRecord.get("type") != null) 
							record.setCardNumber(mapRecord.get("type")+" "+mapRecord.get("number"));
					}
				}
				break;
			case "2":
				hql = "select teEmployees.employeesEmployeeId from TeCards where cardsCardType = 'B'";
				empIds = getCurrentSession().createQuery(hql).list();
				ids = StringUtils.join(empIds, "','");
				ids = "'"+ids+"'";
				records = personService.getAdvanceSearchRecordForAllBCard(ids);
				if(records != null) {
					for(AdvanceSearchRecordBean record: records) {
						String hql1 = "select new map(cardsCardType as type, cardsCardNumber as number) from TeCards"
								+ " where teEmployees.employeesEmployeeId = "+record.getId();
						List<HashMap<String, Object>> maps = getCurrentSession().createQuery(hql1).list();
						HashMap<String, Object> mapRecord = (maps != null && maps.size() > 0) ? maps.get(0) : null;
						if(mapRecord != null && mapRecord.get("type") != null) 
							record.setCardNumber(mapRecord.get("type")+" "+mapRecord.get("number"));
					}
				}
				break;
			case "3":
				hql = "select c.teEmployees.employeesEmployeeId from TeCards c, "
						+ " TeEmployentHistory  eh where c.teEmployees.employeesEmployeeId = eh.teEmployees.employeesEmployeeId"
						+ " and c.cardsCardType = 'A' and eh.ehDateTo is null ";
				empIds = getCurrentSession().createQuery(hql).list();
				ids = StringUtils.join(empIds, "','");
				ids = "'"+ids+"'";
				records = personService.getAdvanceSearchRecordForAllACard(ids);
				if(records != null) {
					for(AdvanceSearchRecordBean record: records) {
						String hql1 = "select new map(cardsCardType as type, cardsCardNumber as number) from TeCards"
								+ " where teEmployees.employeesEmployeeId = "+record.getId();
						List<HashMap<String, Object>> maps = getCurrentSession().createQuery(hql1).list();
						HashMap<String, Object> mapRecord = (maps != null && maps.size() > 0) ? maps.get(0) : null;
						if(mapRecord != null && mapRecord.get("type") != null) 
							record.setCardNumber(mapRecord.get("type")+" "+mapRecord.get("number"));
					}
				}
				break;
			case "4":
				hql = "select c.teEmployees.employeesEmployeeId from TeCards c, "
						+ " TeEmployentHistory  eh where c.teEmployees.employeesEmployeeId = eh.teEmployees.employeesEmployeeId"
						+ " and c.cardsCardType = 'B' and eh.ehDateTo is null ";
				empIds = getCurrentSession().createQuery(hql).list();
				ids = StringUtils.join(empIds, "','");
				ids = "'"+ids+"'";
				records = personService.getAdvanceSearchRecordForAllBCard(ids);
				if(records != null) {
					for(AdvanceSearchRecordBean record: records) {
						String hql1 = "select new map(cardsCardType as type, cardsCardNumber as number) from TeCards"
								+ " where teEmployees.employeesEmployeeId = "+record.getId();
						List<HashMap<String, Object>> maps = getCurrentSession().createQuery(hql1).list();
						HashMap<String, Object> mapRecord = (maps != null && maps.size() > 0) ? maps.get(0) : null;
						if(mapRecord != null && mapRecord.get("type") != null) 
							record.setCardNumber(mapRecord.get("type")+" "+mapRecord.get("number"));
					}
				}
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
				if(teEmployees.getEmployeesPpsNumber() != null && teEmployees.getEmployeesPpsNumber().length() <= 9) {
					
					teEmployees.setEmployeesPpsNumber(EncryptDecryptUtils.encrypt(teEmployees.getEmployeesPpsNumber()));
					getCurrentSession().saveOrUpdate(teEmployees);
				}
			}
		}
	}
	
	@Override
	public List<TeEmployentHistory> getListOfTrainersEmployees(Integer id, String type) {
		
		List<TeEmployentHistory> records = new ArrayList<TeEmployentHistory>();
		if(type.equalsIgnoreCase("all")) {
			Criteria criteria = sessionFactory.getCurrentSession().createCriteria(TeEmployentHistory.class);
			criteria.add(Restrictions.eq("teTrainers.trainerId", id));
			//criteria.add(Restrictions.eq("ehDateTo", null));
			/*criteria.add(Restrictions.isNull("ehDateTo"));*/
			records = criteria.list();
			
		} else {
			Criteria criteria = sessionFactory.getCurrentSession().createCriteria(TeEmployentHistory.class);
			criteria.add(Restrictions.eq("teTrainers.trainerId", id));
			criteria.add(Restrictions.isNull("ehDateTo"));
			DateTime date = new DateTime();
			Date today = new Date();
			Date firstDay = date.dayOfYear().withMinimumValue().toDate();
			criteria.add(Restrictions.between("ehDateFrom", firstDay, today));
			records = criteria.list();
		}
		
		if(records != null && records.size() > 0) {
			List<Integer> ids = new ArrayList<Integer>();
			List<TeEmployentHistory> uniqueEmployeeHistories = new ArrayList<TeEmployentHistory>();
			for (TeEmployentHistory history : records) {
				if(ids.indexOf(history.getTeEmployees().getEmployeesEmployeeId()) < 0) {
					Criteria criteria = sessionFactory.getCurrentSession().createCriteria(TeEmployentHistory.class);
					criteria.add(Restrictions.eq("teTrainers.trainerId", id));
					criteria.add(Restrictions.eq("teEmployees.employeesEmployeeId", history.getTeEmployees().getEmployeesEmployeeId()));
					criteria.addOrder(Order.asc("ehDateFrom"));
					List<TeEmployentHistory> historyRecords = criteria.list();
					history.setStartDate(historyRecords.get(0).getEhDateFrom());
					if(historyRecords.get(historyRecords.size()-1) != null)
						history.setEndDate(historyRecords.get(historyRecords.size()-1).getEhDateTo());
					
					ids.add(history.getTeEmployees().getEmployeesEmployeeId());
					uniqueEmployeeHistories.add(history);
				}
			}
			records = uniqueEmployeeHistories;
		}
		
		Collections.sort(records, new Comparator<TeEmployentHistory>() {
		    @Override
		    public int compare(final TeEmployentHistory o1, final TeEmployentHistory o2) {
		        int compare = o1.getTeEmployees().getEmployeesSurname().compareToIgnoreCase(o2.getTeEmployees().getEmployeesSurname());
		        if (compare != 0) {
		            return compare;
		        }
		        return o1.getTeEmployees().getEmployeesFirstname().compareToIgnoreCase(o2.getTeEmployees().getEmployeesFirstname());
		    }
		});
		
		return records;
	}
	
	@Override
	public List<InapproveEmployeeBean> getInapproveEmployeeList() {
		
		List<InapproveEmployeeBean> results = new ArrayList<InapproveEmployeeBean>();
		Criteria criteria = getCurrentSession().createCriteria(TeEmployeesApproved.class);
		List<TeEmployeesApproved> employees = criteria.list();
		if(employees != null && employees.size() > 0) {
			for (TeEmployeesApproved emp : employees) {
				InapproveEmployeeBean bean = new InapproveEmployeeBean();
				bean.setId(emp.getEmployeesEmployeeId());
				Criteria criteria1 = getCurrentSession().createCriteria(TeEmploymentApprovedHistory.class);
				criteria1.add(Restrictions.eq("teEmployees.employeesEmployeeId", emp.getEmployeesEmployeeId()));
				criteria1.addOrder(Order.asc("ehEmploymentId"));
				List<TeEmploymentApprovedHistory> histories = criteria1.list();
				if(histories != null && histories.size() > 0) {
					bean.setTrainerAccountNumber(histories.get(0).getTeTrainers().getTrainerAccountNo());
					bean.setTrainerName(histories.get(0).getTeTrainers().getTrainerFullName());
				}
				bean.setEmployeeName(emp.getEmployeesFullName());
				results.add(bean);
			}
		}
		return results;
	}
	
	@Override
	public Object getAutoGeneatedCardNumber() {
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		//Criteria criteria = getCurrentSession().createCriteria(TeCards.class).setProjection(Projections.max("cardsCardNumber"));
		return map;
	}
	
	@Override
	public Object deleteHistoryRecord(Integer id) {
		
		Criteria criteria = getCurrentSession().createCriteria(TeEmployentHistory.class);
		criteria.add(Restrictions.eq("ehEmploymentId", id));
		List<TeEmployentHistory> records = criteria.list();
		if(records != null && records.size() > 0) {
			getCurrentSession().delete(records.get(0));
		}
		return new HashMap<String, Object>();
	}
	
	@Override
	public int getAutoIncreamentCardNumber() {
		
		Criteria criteria = getCurrentSession()
		    .createCriteria(TeCards.class)
		    .setProjection(Projections.max("cardsCardNumber"));
		Integer maxNumber = (Integer)criteria.uniqueResult();
		return (maxNumber+1);
	}
	
	@Override
	public boolean isExistsPPSNumberAndDOB(String employeesPpsNumber,
			Date employeesDateOfBirth, Integer empId) {
		
		Criteria criteria = getCurrentSession().createCriteria(TeEmployees.class);
		criteria.add(Restrictions.eq("employeesPpsNumber", employeesPpsNumber));
		criteria.add(Restrictions.eq("employeesDateOfBirth", employeesDateOfBirth));
		if(empId != null && empId > 0) criteria.add(Restrictions.ne("employeesEmployeeId", empId));
		List<TeEmployees> employees = criteria.list();
		return (employees.size() > 0);
	}
	
	@Override
	public Object isExistsDOB(Integer id, Date dob, String fname, String sname) throws ParseException {
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		Criteria criteria = getCurrentSession().createCriteria(TeEmployees.class);
		criteria.add(Restrictions.eq("employeesDateOfBirth", dob));
		if(id > 0) criteria.add(Restrictions.ne("employeesEmployeeId", id));
		List<TeEmployees> employees = criteria.list();
		/*Criteria sfCriteria = getCurrentSession().createCriteria(TeEmployees.class);
		sfCriteria.add(Restrictions.eq("employeesDateOfBirth", dob));
		sfCriteria.add(Restrictions.or(
				Restrictions.or(Restrictions.like("employeesFirstname", fname, MatchMode.ANYWHERE), Restrictions.like("employeesSurname", sname, MatchMode.ANYWHERE)), 
				Restrictions.or(Restrictions.like("employeesSurname", fname, MatchMode.ANYWHERE), Restrictions.like("employeesFirstname", sname, MatchMode.ANYWHERE))));
		if(id > 0) sfCriteria.add(Restrictions.ne("employeesEmployeeId", id));
		List<TeEmployees> sfEmployees = sfCriteria.list();*/
		map.put("exists", (employees != null && employees.size() > 0));
		map.put("emps", null);
		if(employees != null && employees.size() > 0) {
			List<HashMap<String, Object>> records = new ArrayList<HashMap<String,Object>>();
			for (TeEmployees teEmployees : employees) {
				HashMap<String, Object> empMap = new HashMap<String, Object>();
				empMap.put("eId", teEmployees.getEmployeesEmployeeId());
				empMap.put("name", teEmployees.getEmployeesFullName());
				empMap.put("dob", formatter.format(teEmployees.getEmployeesDateOfBirth()));
				empMap.put("address", teEmployees.getEmployeesAddress1()+" "+teEmployees.getEmployeesAddress2()+" "+teEmployees.getEmployeesAddress3()+
						" "+teEmployees.getEmployeesAddress4()+" "+teEmployees.getEmployeesAddress5()+" "+teEmployees.getEmployeesPostCode());
				records.add(empMap);
			}
			map.put("emps", records);
		}
		return map;
	}
	
	@Override
	public Object isExistsPPS(Integer id, String pps) {
		
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		HashMap<String, Object> map = new HashMap<String, Object>();
		Criteria criteria = getCurrentSession().createCriteria(TeEmployees.class);
		criteria.add(Restrictions.eq("employeesPpsNumber", EncryptDecryptUtils.encrypt(pps)));
		if(id > 0) criteria.add(Restrictions.ne("employeesEmployeeId", id));
		List<TeEmployees> employees = criteria.list();
		map.put("exists", (employees != null && employees.size() > 0));
		map.put("emps", null);
		if(employees != null && employees.size() > 0) {
			List<HashMap<String, Object>> records = new ArrayList<HashMap<String,Object>>();
			for (TeEmployees teEmployees : employees) {
				HashMap<String, Object> empMap = new HashMap<String, Object>();
				empMap.put("eId", teEmployees.getEmployeesEmployeeId());
				empMap.put("name", teEmployees.getEmployeesFullName());
				empMap.put("dob", formatter.format(teEmployees.getEmployeesDateOfBirth()));
				empMap.put("address", teEmployees.getEmployeesAddress1()+" "+teEmployees.getEmployeesAddress2()+" "+teEmployees.getEmployeesAddress3()+
						" "+teEmployees.getEmployeesAddress4()+" "+teEmployees.getEmployeesAddress5()+" "+teEmployees.getEmployeesPostCode());
				records.add(empMap);
			}
			map.put("emps", records);
		}
		return map;
	}
	
	@Override
	public Object getPPSRecordForEmployee(int start, int length, int draw, String search) {

		HashMap<String, Object> map = new HashMap<String, Object>();
		int count = 0;
		Criteria criteria = getCurrentSession().createCriteria(TeEmployees.class);
		if(search != null && search.length() > 0) {
			criteria.add(Restrictions.or(
					Restrictions.like("employeesFirstname", search, MatchMode.ANYWHERE), 
					Restrictions.like("employeesSurname", search, MatchMode.ANYWHERE)));
		}
		criteria.setFirstResult(start);
		criteria.setMaxResults(length);
		criteria.addOrder(Order.asc("employeesSurname"));
		criteria.addOrder(Order.asc("employeesFirstname"));
		List<TeEmployees> records = criteria.list();
		if(search != null && search.length() > 0) {
			Criteria countCriteria = getCurrentSession().createCriteria(TeEmployees.class);
			countCriteria.add(Restrictions.or(
					Restrictions.like("employeesFirstname", search, MatchMode.ANYWHERE), 
					Restrictions.like("employeesSurname", search, MatchMode.ANYWHERE)));
			count = ((Long)countCriteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();
		} else {
			count = ((Long)getCurrentSession().createCriteria(TeEmployees.class).setProjection(Projections.rowCount()).uniqueResult()).intValue();
		}
		List<EmployeePPSBean> results = new ArrayList<EmployeePPSBean>();
		if(records != null && records.size() > 0) {
			for (TeEmployees emp : records) {
				EmployeePPSBean bean = new EmployeePPSBean();
				bean.setName(emp.getEmployeesFullName());
				if(emp.getEmployeesPpsNumber() != null) {
					if(emp.getEmployeesPpsNumber().length() > 9)
						bean.setPps(EncryptDecryptUtils.decrypt(emp.getEmployeesPpsNumber()));
					else
						bean.setPps(emp.getEmployeesPpsNumber());
				} else bean.setPps("");
				bean.setId(emp.getEmployeesEmployeeId());
				results.add(bean);
			}
		}
		EmployeePPSBean[] beanArr = new EmployeePPSBean[results.size()];
		beanArr = results.toArray(beanArr);
		
		map.put("data", beanArr);
		map.put("draw", draw);
		map.put("recordsTotal", count);
		map.put("recordsFiltered", count);
		
		return map;
	}
	
	@Override
	public Object getEmployeesWithPPSNumber() {
		
		Criteria criteria = getCurrentSession().createCriteria(TeEmployees.class);
		List<TeEmployees> records = criteria.list();
		List<EmployeePPSBean> results = new ArrayList<EmployeePPSBean>();
		if(records != null && records.size() > 0) {
			for (TeEmployees emp : records) {
				EmployeePPSBean bean = new EmployeePPSBean();
				bean.setName(emp.getEmployeesFullName());
				if(emp.getEmployeesPpsNumber() != null) {
					if(emp.getEmployeesPpsNumber().length() > 9)
						bean.setPps(EncryptDecryptUtils.decrypt(emp.getEmployeesPpsNumber()));
					else
						bean.setPps(emp.getEmployeesPpsNumber());
				} else bean.setPps("");
				bean.setId(emp.getEmployeesEmployeeId());
				results.add(bean);
			}
		}
		return results;
	}
	
	@Override
	public List<TeTrainers> getTrainersByEmpId(Integer employeesEmployeeId) {
		
		Criteria criteria = getCurrentSession().createCriteria(TeEmployentHistory.class);
		criteria.add(Restrictions.eq("teEmployees.employeesEmployeeId", employeesEmployeeId));
		criteria.add(Restrictions.isNull("ehDateTo"));
		List<TeEmployentHistory> records = criteria.list();
		List<TeTrainers> trainers = new ArrayList<TeTrainers>();
		if(records != null && records.size() > 0) {
			for(TeEmployentHistory history : records) {
				trainers.add(history.getTeTrainers());
			}
		}
		return trainers;
	}
}
