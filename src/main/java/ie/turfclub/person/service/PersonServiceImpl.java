package ie.turfclub.person.service;

import ie.turfclub.common.bean.SearchByNameTrainerBean;
import ie.turfclub.common.enums.RoleEnum;
import ie.turfclub.common.service.JDBCConnection;
import ie.turfclub.person.model.Person;
import ie.turfclub.trainers.model.TeTrainers;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mysql.jdbc.Statement;

@Service
public class PersonServiceImpl implements PersonService {

	@Autowired
	private JDBCConnection conn;
	
	@Override
	public void addPerson(Person person) throws SQLException {
		
		PreparedStatement pstmt = conn.getConnection().prepareStatement("select p.* from person as p join person_role as pr "
				+ "on p.id = pr.person_id where pr.role_id = ? and p.ref_id = ?");
		pstmt.setObject(1, person.getRoleId());
		pstmt.setObject(2, person.getRefId());
		ResultSet personDB = pstmt.executeQuery();
		if(!personDB.next()) {
			String personSQL = "INSERT INTO person (surname, firstname, "
					+ "date_of_birth, address1,address2, address3, phone_no, mobile_no,"
					+ "email, comments, date_entered, request_date, ref_id, title, sex, nationality,"
					+ " marital_status, spouse_name, county, country, post_code) VALUES "
					+ "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";		
			
			PreparedStatement personSTMT = (PreparedStatement) conn.getConnection().prepareStatement(personSQL, Statement.RETURN_GENERATED_KEYS);
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
			personSTMT.setObject(14, person.getTitle());
			personSTMT.setObject(15, person.getSex());
			personSTMT.setObject(16, person.getNationality());
			personSTMT.setObject(17, person.getMaritalStatus());
			personSTMT.setObject(18, person.getSpouseName());
			personSTMT.setObject(19, person.getCounty());
			personSTMT.setObject(20, person.getCountry());
			personSTMT.setObject(21, person.getPostCode());
			
			personSTMT.executeUpdate();
			
			try (ResultSet generatedKeys = personSTMT.getGeneratedKeys()) {
	            if (generatedKeys.next()) {
	                person.setId(generatedKeys.getLong(1));
	            }
	            else {
	                throw new SQLException("Creating user failed, no ID obtained.");
	            }
	        }
			
			String personRoleSQL = "INSERT INTO person_role (person_id, role_id) VALUES (?, ?)";
			
			PreparedStatement personRoleSTMT = (PreparedStatement) conn.getConnection().prepareStatement(personRoleSQL);
			personRoleSTMT.setObject(1, person.getId());
			personRoleSTMT.setObject(2, person.getRoleId());
			
			personRoleSTMT.executeUpdate();
		} else {
			
			String personSQL = "UPDATE person set surname = ?, firstname = ?, "
					+ "date_of_birth = ?, address1 = ?,address2 = ?, address3= ?, phone_no = ?, mobile_no = ?,"
					+ "email = ?, comments = ?, date_entered = ? , request_date = ?,"
					+ " title = ? , sex = ? , nationality = ? , marital_status = ? , spouse_name = ? ,"
					+ " county = ?, country = ? , post_code = ? where ref_id = ?";		
			
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
			personSTMT.setObject(13, person.getTitle());
			personSTMT.setObject(14, person.getSex());
			personSTMT.setObject(15, person.getNationality());
			personSTMT.setObject(16, person.getMaritalStatus());
			personSTMT.setObject(17, person.getSpouseName());
			personSTMT.setObject(18, person.getCounty());
			personSTMT.setObject(19, person.getCountry());
			personSTMT.setObject(20, person.getPostCode());
			personSTMT.setObject(21, person.getRefId());
			personSTMT.executeUpdate();
		}
	}
	
	@Override
	public List<SearchByNameTrainerBean> findByNameTrainer(String search) throws Exception {
		
		PreparedStatement pstmt = conn.getConnection().prepareStatement("select p.surname as surname, "
				+ "p.firstname as firstname,  p.ref_id as refId from person as p join person_role as pr "
				+ "on p.id = pr.person_id where pr.role_id = ? and (p.surname like ? or p.firstname like ?) ");
		pstmt.setObject(1, RoleEnum.TRAINER.getId());
		pstmt.setObject(2, "%"+search+"%");
		pstmt.setObject(3, "%"+search+"%");
		ResultSet set = pstmt.executeQuery();
		List<SearchByNameTrainerBean> records = new ArrayList<SearchByNameTrainerBean>();
		while(set.next()) {
			SearchByNameTrainerBean bean = new SearchByNameTrainerBean();
			bean.setId(Integer.parseInt(set.getString("refId")));
			bean.setName(set.getString("surname")+" "+set.getString("firstname"));
			records.add(bean);
		}
		return records;
	}
	
	@Override
	public TeTrainers setSomeFieldInTrainer(TeTrainers trainer) {
		
		try {
			PreparedStatement pstmt = conn.getConnection().prepareStatement("select p.* from person as p join person_role as pr "
					+ "on p.id = pr.person_id where pr.role_id = ? and p.ref_id = ?");
			pstmt.setObject(1, RoleEnum.TRAINER.getId());
			pstmt.setObject(2, trainer.getTrainerId());
			ResultSet set = pstmt.executeQuery();
			if(set.next()) {
				trainer.setTitle(set.getString("title"));
				trainer.setSex(set.getString("sex"));
				trainer.setNationality(set.getString("nationality"));
				trainer.setMaritalStatus(set.getString("marital_status"));
				trainer.setSpouseName(set.getString("spouse_name"));
				trainer.setCounty(set.getString("county"));
				trainer.setCountry(set.getString("country"));
				trainer.setPostCode(set.getString("post_code"));
			}
			return trainer;
		} catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
