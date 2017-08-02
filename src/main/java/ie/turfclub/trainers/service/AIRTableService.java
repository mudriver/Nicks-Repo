package ie.turfclub.trainers.service;

import ie.turfclub.main.model.login.User;
import ie.turfclub.trainers.model.AIRTable;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public interface AIRTableService {

	List<AIRTable> findAll();

	Object loadDataByPagination(int start, int length, int draw);

	String getCSVString();

	HashMap<String, Object> sendMailToAdmin(String requiredProperty, User user) throws IOException;

}
