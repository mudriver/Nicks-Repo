package ie.turfclub.person.service;

import java.sql.SQLException;
import java.util.List;

import ie.turfclub.common.bean.SearchByNameTrainerBean;
import ie.turfclub.person.model.Person;
import ie.turfclub.trainers.model.TeTrainers;

public interface PersonService {

	public void addPerson(Person person) throws SQLException;

	public List<SearchByNameTrainerBean> findByNameTrainer(String search) throws Exception;

	public TeTrainers setSomeFieldInTrainer(TeTrainers trainer);
}
