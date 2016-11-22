package ie.turfclub.person.service;

import java.sql.SQLException;

import ie.turfclub.person.model.Person;

public interface PersonService {

	public void addPerson(Person person) throws SQLException;
}
