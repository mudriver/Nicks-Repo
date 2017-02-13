package ie.turfclub.person.service;

import ie.turfclub.common.bean.AdvanceSearchRecordBean;
import ie.turfclub.common.bean.SearchByNameEmployeeBean;
import ie.turfclub.common.bean.SearchByNameTrainerBean;
import ie.turfclub.common.bean.TrainerUserBean;
import ie.turfclub.person.model.Person;
import ie.turfclub.trainers.model.TeTrainers;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

public interface PersonService {

	public void addPerson(Person person) throws SQLException;

	public List<SearchByNameTrainerBean> findByNameTrainer(String search) throws Exception;

	public TeTrainers setSomeFieldInTrainer(TeTrainers trainer);

	public List<SearchByNameEmployeeBean> getEmployeeByName(String search);

	public List<SearchByNameEmployeeBean> getEmployeeByCardNumber(String search);

	public int getAdvanceSearchRecordForAllACardCount();

	public int getAdvanceSearchRecordForAllBCardCount();

	public List<AdvanceSearchRecordBean> getAdvanceSearchRecordForAllACardByLimit(
			int start, int length);

	public List<AdvanceSearchRecordBean> getAdvanceSearchRecordForAllBCardByLimit(
			int start, int length);

	public List<AdvanceSearchRecordBean> getAdvanceSearchRecordForAllACard();
	
	public List<AdvanceSearchRecordBean> getAdvanceSearchRecordForAllBCard();

	public List<TrainerUserBean> getTrainerUserBean();

	public HashMap<String, Object> getEmpNameAndNumberById(Integer empId);
}
