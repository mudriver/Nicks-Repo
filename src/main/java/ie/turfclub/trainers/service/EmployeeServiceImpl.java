package ie.turfclub.trainers.service;

import ie.turfclub.common.bean.SearchByNameEmployeeBean;
import ie.turfclub.trainers.model.TeCards;
import ie.turfclub.trainers.model.TeEmployees;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
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
		
		Criteria criteria = getCurrentSession().createCriteria(TeEmployees.class);
		criteria.add(Restrictions.ilike("employeesFullName", search, MatchMode.ANYWHERE));
		List<TeEmployees> employees = criteria.list();
		return convertEmployeesToBean(employees);
	}
	
	@Override
	public TeEmployees getEmployeeById(Integer id) {
		
		Criteria criteria = getCurrentSession().createCriteria(TeEmployees.class);
		criteria.add(Restrictions.eq("employeesEmployeeId", id));
		List<TeEmployees> employees = criteria.list();
		return (employees != null && employees.size() > 0) ? employees.get(0) : null;
	}
}
