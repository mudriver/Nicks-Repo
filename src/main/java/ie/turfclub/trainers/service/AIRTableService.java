package ie.turfclub.trainers.service;

import ie.turfclub.trainers.model.AIRTable;

import java.util.List;

public interface AIRTableService {

	List<AIRTable> findAll();

	Object loadDataByPagination(int start, int length, int draw);

	String getCSVString();

}
