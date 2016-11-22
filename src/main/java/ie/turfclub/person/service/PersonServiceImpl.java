package ie.turfclub.person.service;

import java.sql.SQLException;

import ie.turfclub.common.service.JDBCConnection;
import ie.turfclub.person.model.Person;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.PreparedStatement;

@Service
public class PersonServiceImpl implements PersonService {

	@Autowired
	private JDBCConnection conn;
	
	@Override
	public void addPerson(Person person) throws SQLException {
		
		String sql = "INSERT INTO person (pps_number, title, surname, firstname, "
				+ "date_of_birth, nationality, sex, marital_status, spouse_name, address1,"
				+ "address2, address3, address4, address5, postcode, phone_no, mobile_no,"
				+ "email, comments, hri_account_no, last_updated, date_entered, is_new,"
				+ "has_taxable_earnings, employee_verified, request_date, existing_air_card_holder,"
				+ "old_employee_card_holder, category_of_employment, last_year_paid, num_hour_worked,"
				+ "batch_no, acc_no, stable_address1, stable_address2, stable_address3, fax,"
				+ "restricted, hunter_chase, curragh, insurance_expiry, last_racecard_id, "
				+ "return_completed, contact_name, contact_number, verified_status, notes,"
				+ "role) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,"
				+ "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";		
		
		PreparedStatement stmt = (PreparedStatement) conn.getConnection().prepareStatement(sql);
		stmt.setObject(1, person.getPpsNumber());
		stmt.setObject(2, person.getTitle());
		stmt.setObject(3, person.getTitle());
		stmt.setObject(4, person.getFirstname());
		stmt.setObject(5, person.getDateOfBirth());
		stmt.setObject(6, person.getNationality());
		stmt.setObject(7, person.getSex());
		stmt.setObject(8, person.getMaritalStatus());
		stmt.setObject(9, person.getSpouseName());
		stmt.setObject(10, person.getAddress1());
		stmt.setObject(11, person.getAddress2());
		stmt.setObject(12, person.getAddress3());
		stmt.setObject(13, person.getAddress4());
		stmt.setObject(14, person.getAddress5());
		stmt.setObject(15, person.getPostCode());
		stmt.setObject(16, person.getPhoneNo());
		stmt.setObject(17, person.getMobileNo());
		stmt.setObject(18, person.getEmail());
		stmt.setObject(19, person.getComments());
		stmt.setObject(20, person.getHriAccountNo());
		stmt.setObject(21, person.getLastUpdated());
		stmt.setObject(22, person.getDateEntered());
		stmt.setObject(23, person.isNew());
		stmt.setObject(24, person.isHasTaxableEarnings());
		stmt.setObject(25, person.isEmployeeVerified());
		stmt.setObject(26, person.getRequestDate());
		stmt.setObject(27, person.getExistingAIRCardHolder());
		stmt.setObject(28, person.getOldEmployeeCardNumber());
		stmt.setObject(29, person.getCategoryOfEmployment());
		stmt.setObject(30, person.getLastYearPaid());
		stmt.setObject(31, person.getNumHourWorked());
		stmt.setObject(32, person.getBatchNo());
		stmt.setObject(33, person.getAccNo());
		stmt.setObject(34, person.getStableAddress1());
		stmt.setObject(35, person.getStableAddress2());
		stmt.setObject(36, person.getStableAddress3());
		stmt.setObject(37, person.getFax());
		stmt.setObject(38, person.getRestricted());
		stmt.setObject(39, person.getHunterChase());
		stmt.setObject(40, person.getCurragh());
		stmt.setObject(41, person.getInsuranceExpiry());
		stmt.setObject(42, person.getLastRacecardId());
		stmt.setObject(43, person.isReturnCompleted());
		stmt.setObject(44, person.getContactName());
		stmt.setObject(45, person.getContactNumber());
		stmt.setObject(46, person.getVerifiedStatus());
		stmt.setObject(47, person.getNotes());
		stmt.setObject(48, person.getRole());
		
		stmt.executeUpdate();
	}
}
