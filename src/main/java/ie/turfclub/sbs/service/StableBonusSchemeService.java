package ie.turfclub.sbs.service;

import java.util.HashMap;

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

}
