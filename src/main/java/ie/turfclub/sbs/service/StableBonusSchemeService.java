package ie.turfclub.sbs.service;

import ie.turfclub.main.model.login.User;
import ie.turfclub.sbs.model.SBSEntity;

import java.util.HashMap;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public interface StableBonusSchemeService {

	/**
	 * handle uploaded file 
	 * get record and save into table
	 * 
	 * @param file
	 * @return
	 */
	HashMap<String, Object> handleUploadedFile(MultipartFile file);

	/**
	 * GET SBS Initial Records
	 * 
	 * @return
	 */
	List<HashMap<String, Object>> getSBSInitialRecords(String date, String quarter);

	/**
	 * Get All records
	 * 
	 * @return
	 */
	List<SBSEntity> getAll();

	/**
	 * is Exists TrainerId
	 * 
	 * @param tId
	 * @return
	 */
	HashMap<String, Object> isExistsTrainerId(String tId);

	/**
	 * Get SBS Reprint
	 * 
	 * @param date
	 * @param quarter
	 * @param tId
	 * @return
	 */
	HashMap<String, Object> getSBSReprint(String date, String quarter,
			String tId);

	/**
	 * Get All Records Order by name asc
	 * 
	 * @return
	 */
	List<SBSEntity> getAllOrderByNameAsc();

	/**
	 * Get All records find by name
	 * 
	 * @param search
	 * @return
	 */
	List<SBSEntity> findByName(String search);

	/**
	 * handle returned
	 * 
	 * @param id
	 */
	void handleReturned(String id);

	/**
	 * Handle Message Reminder
	 */
	void handleMsgReminder(String path);

	/**
	 * Get SBS Final Reminder
	 * 
	 * @param date
	 * @param quarter
	 * @return
	 */
	List<HashMap<String, Object>> getSBSFinalReminder(String date,
			String quarter);

	void sendMailToAdmin(String filePath, User user, String emails);

}
