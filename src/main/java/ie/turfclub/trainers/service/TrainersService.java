package ie.turfclub.trainers.service;



import ie.turfclub.common.bean.SearchByNameTrainerBean;
import ie.turfclub.trainers.model.SentEmail;
import ie.turfclub.trainers.model.TeAuthorisedReps;
import ie.turfclub.trainers.model.TeEmployeeTrainerVerified;
import ie.turfclub.trainers.model.TeEmployees;
import ie.turfclub.trainers.model.TeEmployentHistory;
import ie.turfclub.trainers.model.TeFile;
import ie.turfclub.trainers.model.TeTrainers;
import ie.turfclub.trainers.model.TeTrainers.VerifiedStatus;
import ie.turfclub.trainers.model.savedSearches.TeAuthRepsSavedSearches;
import ie.turfclub.trainers.model.savedSearches.TeEmployeesPensionSavedSearch;
import ie.turfclub.trainers.model.savedSearches.TeEmployeesSavedSearch;
import ie.turfclub.trainers.model.savedSearches.TeTrainersPensionSavedSearch;
import ie.turfclub.trainers.model.savedSearches.TeTrainersSavedSearch;

import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpSession;

import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfPTable;

public interface TrainersService {

	public void initialize();
	public HashMap<String, Object> getTrainers(TeTrainersSavedSearch savedSearch);
	public HashMap<String, Object> getTrainersPension(TeTrainersPensionSavedSearch savedSearch);
	public List<TeEmployees> getEmployeesPension(Integer trainerId);
	public HashMap<String, Object> getEmployeesPension(Integer trainerId, TeEmployeesPensionSavedSearch savedSearch);
	public List<TeFile> getTrainersPensionFileNames(Integer trainerId);
	public List<TeEmployentHistory> getEmploymentHistories(Integer trainerId,Integer employeeId, Date yearFrom , Date yearTo);
	public List<TeEmployeeTrainerVerified> getEmployeeTrainerVerified(Integer trainerId, Integer employeeId);
	public List<HashMap<String, String>> getTrainers(String chars);
	public TeTrainers getTrainer(Integer id) throws Exception;
	public HashMap<String, Object> getEmployees(Integer trainerId, TeEmployeesSavedSearch savedSearch);
	public List<HashMap<String, String>> getEmployees(String chars);
	public TeEmployees getEmployee(Integer id);
	public HashMap<String, Object> getAuthorisedReps(Integer trainerId, TeAuthRepsSavedSearches savedSearch);
	public List<HashMap<String, String>> getAuthorisedReps(String chars);
	public TeAuthorisedReps getAuthorisedRep(Integer id);
	public List<Object> getAuthRepSavedSearches(int userId);
	public void saveAuthRepSavedSearches(TeAuthRepsSavedSearches savedsearch);
	public List<Object> getEmployeesSavedSearches(int userId);
	public void saveEmployeeSavedSearches(TeEmployeesSavedSearch savedsearch);
	public List<Object> getTrainersSavedSearches(int userId);
	public void saveTrainerSavedSearches(TeTrainersSavedSearch savedsearch) throws Exception;
	public List<Object> getTrainersPensionSavedSearches(int userId);
	public void saveTrainerPensionSavedSearches(TeTrainersPensionSavedSearch savedsearch);
	public void updateTrainer(TeTrainers trainer);
	public void updateTrainerComments(TeTrainers trainerWithComments);
	public VerifiedStatus getTrainerVerifiedStatus(Integer trainerId);
	public TeTrainers updateTrainerVerifiedStatus(TeTrainers trainerWithVerifiedStatus);
	public TeEmployees updateEmployeeHistoryVerifiedStatus(TeEmployees employee, Integer trainerId);
	public List<HashMap<String, Object>> getAllTrainers();
	public Object getVerifiedStatus();
	public String saveOrUpdate(TeTrainers trainer) throws SQLException;
	
	/**
	 * Save trainer record into person DB
	 * @throws SQLException 
	 */
	public void handleCopyRecord() throws SQLException;
	public List<SearchByNameTrainerBean> findByName(String search) throws Exception;
	public String getCSVStringForTrainerEmployees(Integer id, String type);
	public void createPDFDocumentWithDetails(Document document, Integer id, String type);
	public List<HashMap<String, Object>> getAintreeRecord(int start, int end, HttpSession session);
	public List<HashMap<String, Object>> getAintreeRecord();
	public void handleStableListAdministrationReturYearPage(String year);
	public void handleTrainerEmployeeOnlineReturYearPage(String year);
	public String getYearForTrainerEmployeeOnline();
	public String getYearForStaffListAdministrator();
	public List<SearchByNameTrainerBean> getRenewalTrainerFindByName(
			String search) throws Exception;
	public void markEmployeesLeftForTrainer(Integer id);
	public List<HashMap<String, Object>> getLicensedTrainerWithCurrentEmployees(
			int start, int end, HttpSession session);
	public List<HashMap<String, Object>> getPrintRenewalRecords();
	public void buildDocumentForLicenseTrainerList(Document document);
	public String getListOfSentEmail(String smsTxt);
	
}
