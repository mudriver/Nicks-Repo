package ie.turfclub.trainers.service;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;

import ie.turfclub.common.bean.SearchByNameEmployeeBean;
import ie.turfclub.trainers.model.TeEmployees;

public interface EmployeeService {

	void saveOrUpdate(TeEmployees emp);

	String getAllCardType();

	String getPension();

	String getSexEnum();

	String getMaritalStatusEnum();

	String getEmploymentCategoryEnum();

	String getTitlesEnum();

	String getCountiesEnum();

	String getCountriesEnum();

	String getNationalityEnum();

	List<HashMap<String, Object>> getAllCards();

	TeEmployees getEmployeeByCardId(Integer cardId);

	List<SearchByNameEmployeeBean> searchByNameEmployees();

	List<SearchByNameEmployeeBean> findByName(String search);

	TeEmployees getEmployeeById(Integer id) throws IllegalAccessException, InvocationTargetException;

	List<SearchByNameEmployeeBean> findByNumber(String search);

	void deleteRecordById(Integer id) throws IllegalAccessException, InvocationTargetException;

	HashMap<String, Object> getPartFullTimeRecords(String hours);

}
