package ie.turfclub.sbs.service;

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

}
