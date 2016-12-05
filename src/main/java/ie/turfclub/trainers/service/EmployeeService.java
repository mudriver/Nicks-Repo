package ie.turfclub.trainers.service;

import java.util.HashMap;
import java.util.List;

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

}
