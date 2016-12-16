package ie.turfclub.person.service;

import ie.turfclub.common.service.JDBCConnection;
import ie.turfclub.person.model.Person;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PersonServiceImpl implements PersonService {

	@Autowired
	private JDBCConnection conn;
	
	@Override
	public void addPerson(Person person) throws SQLException {
		
		PreparedStatement pstmt = conn.getConnection().prepareStatement("select * from person where id = ? and ref_id = ?");
		pstmt.setObject(1, person.getId());
		pstmt.setObject(2, person.getRefId());
		ResultSet personDB = pstmt.executeQuery();
		if(!personDB.next()) {
			String personSQL = "INSERT INTO person (surname, firstname, "
					+ "date_of_birth, address1,address2, address3, phone_no, mobile_no,"
					+ "email, comments, date_entered, request_date, ref_id) VALUES "
					+ "(?,?,?,?,?,?,?,?,?,?,?,?,?)";		
			
			PreparedStatement personSTMT = (PreparedStatement) conn.getConnection().prepareStatement(personSQL);
			personSTMT.setObject(1, person.getSurname());
			personSTMT.setObject(2, person.getFirstname());
			personSTMT.setObject(3, person.getDateOfBirth());
			personSTMT.setObject(4, person.getAddress1());
			personSTMT.setObject(5, person.getAddress2());
			personSTMT.setObject(6, person.getAddress3());
			personSTMT.setObject(7, person.getPhoneNo());
			personSTMT.setObject(8, person.getMobileNo());
			personSTMT.setObject(9, person.getEmail());
			personSTMT.setObject(10, person.getComments());
			personSTMT.setObject(11, person.getDateEntered());
			personSTMT.setObject(12, person.getRequestDate());
			personSTMT.setObject(13, person.getRefId());
			
			personSTMT.executeUpdate();
			
			String personRoleSQL = "INSERT INTO person_role (person_id, role_id) VALUES (?, ?)";
			
			PreparedStatement personRoleSTMT = (PreparedStatement) conn.getConnection().prepareStatement(personRoleSQL);
			personRoleSTMT.setObject(1, person.getRefId());
			personRoleSTMT.setObject(2, person.getRoleId());
			
			personRoleSTMT.executeUpdate();
		} else {
			
			String personSQL = "UPDATE person set surname = ?, firstname = ?, "
					+ "date_of_birth = ?, address1 = ?,address2 = ?, address3= ?, phone_no = ?, mobile_no = ?,"
					+ "email = ?, comments = ?, date_entered = ? , request_date = ? where ref_id = ?";		
			
			PreparedStatement personSTMT = (PreparedStatement) conn.getConnection().prepareStatement(personSQL);
			personSTMT.setObject(1, person.getSurname());
			personSTMT.setObject(2, person.getFirstname());
			personSTMT.setObject(3, person.getDateOfBirth());
			personSTMT.setObject(4, person.getAddress1());
			personSTMT.setObject(5, person.getAddress2());
			personSTMT.setObject(6, person.getAddress3());
			personSTMT.setObject(7, person.getPhoneNo());
			personSTMT.setObject(8, person.getMobileNo());
			personSTMT.setObject(9, person.getEmail());
			personSTMT.setObject(10, person.getComments());
			personSTMT.setObject(11, person.getDateEntered());
			personSTMT.setObject(12, person.getRequestDate());
			personSTMT.setObject(13, person.getRefId());
			personSTMT.executeUpdate();
		}
	}
}
