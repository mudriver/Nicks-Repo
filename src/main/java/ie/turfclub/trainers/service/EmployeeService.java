package ie.turfclub.trainers.service;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import ie.turfclub.common.bean.AdvanceSearchRecordBean;
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

	void handleSaveOrUpdate(TeEmployees emp) throws Exception;

	HashMap<String, Object> getAdvanceSearchRecordByType(String type, int start, int length, int draw);

	void handleCopyRecord() throws SQLException;

	HashMap<String, Object> getCSVString(String type, String title);

	void handleEncryptPPSNumber();

}
