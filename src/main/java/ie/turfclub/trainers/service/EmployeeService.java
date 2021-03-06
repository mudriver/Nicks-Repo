package ie.turfclub.trainers.service;

import ie.turfclub.common.bean.InapproveEmployeeBean;
import ie.turfclub.common.bean.SearchByNameEmployeeBean;
import ie.turfclub.trainers.model.TeEmployees;
import ie.turfclub.trainers.model.TeEmployeesApproved;
import ie.turfclub.trainers.model.TeEmployentHistory;
import ie.turfclub.trainers.model.TeTrainers;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

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
	
	TeEmployeesApproved getEmployeeApprovedById(Integer id) throws IllegalAccessException, InvocationTargetException;

	List<SearchByNameEmployeeBean> findByNumber(String search);

	void deleteRecordById(Integer id) throws IllegalAccessException, InvocationTargetException;

	HashMap<String, Object> getPartFullTimeRecords(String hours);

	void handleSaveOrUpdate(TeEmployees emp) throws Exception;

	HashMap<String, Object> getAdvanceSearchRecordByType(String type, int start, int length, int draw);

	void handleCopyRecord() throws SQLException;

	HashMap<String, Object> getCSVString(String type, String title);

	void handleEncryptPPSNumber();

	List<TeEmployentHistory> getListOfTrainersEmployees(Integer id, String type);

	List<InapproveEmployeeBean> getInapproveEmployeeList();

	void handleApproveEmployee(Integer id) throws IllegalAccessException, InvocationTargetException, Exception;

	void deleteEmployeeApprovedById(Integer id) throws IllegalAccessException, InvocationTargetException;

	Object getAutoGeneatedCardNumber();

	Object deleteHistoryRecord(Integer id);

	int getAutoIncreamentCardNumber();

	boolean isExistsPPSNumberAndDOB(String employeesPpsNumber,
			Date employeesDateOfBirth, Integer empId);

	Object isExistsDOB(Integer id, Date dob, String fname, String sname) throws ParseException;

	Object isExistsPPS(Integer id, String pps);

	Object getPPSRecordForEmployee(int start, int length, int draw, String search);

	Object getEmployeesWithPPSNumber();

	List<TeTrainers> getTrainersByEmpId(Integer employeesEmployeeId);

}
