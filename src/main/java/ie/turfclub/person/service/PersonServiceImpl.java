package ie.turfclub.person.service;

import ie.turfclub.common.bean.AdvanceSearchRecordBean;
import ie.turfclub.common.bean.SearchByNameEmployeeBean;
import ie.turfclub.common.bean.SearchByNameTrainerBean;
import ie.turfclub.common.bean.TrainerUserBean;
import ie.turfclub.common.enums.RoleEnum;
import ie.turfclub.common.service.JDBCConnection;
import ie.turfclub.person.model.Person;
import ie.turfclub.trainers.model.TeTrainers;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@PropertySource("classpath:ie/turfclub/trainers/resources/config/config.properties")
@Service
@Transactional
public class PersonServiceImpl implements PersonService {

	@Autowired
	private JDBCConnection conn;
	@Autowired
	private SessionFactory sessionFactory;
	@Resource
	private Environment env;
	
	private Session getCurrentSession() {
		return sessionFactory.getCurrentSession();
	}
	
	@Override
	public void addPerson(Person person) throws SQLException {
		
		/*Query query = getCurrentSession().createSQLQuery("select p.* from person as p join person_role as pr "
				+ "on p.id = pr.person_id where pr.role_id = ? and p.ref_id = ?");
		query.setLong(0, person.getRoleId());
		query.setLong(1, person.getRefId());
		List<Person> persons = query.list();
		if(persons != null && persons.size() > 0) {
			
			getCurrentSession().saveOrUpdate(person);
			
			PersonRole personRole = new PersonRole();
			personRole.setPersonId(person.getId());
			personRole.setRoleId(person.getRoleId());
			getCurrentSession().saveOrUpdate(personRole);
		} else {
			
			getCurrentSession().saveOrUpdate(person);
		}*/
		
		PreparedStatement pstmt = conn.getConnection().prepareStatement("select p.* from person as p join person_role as pr "
				+ "on p.id = pr.person_id where pr.role_id = ? and p.ref_id = ?");
		pstmt.setObject(1, person.getRoleId());
		pstmt.setObject(2, person.getRefId());
		ResultSet personDB = pstmt.executeQuery();
		if(!personDB.first()) {
			String personSQL = "INSERT INTO person (surname, firstname, "
					+ "date_of_birth, address1,address2, address3, phone_no, mobile_no,"
					+ "email, comments, date_entered, request_date, ref_id, title, sex, nationality,"
					+ " marital_status, spouse_name, county, country, post_code, "
					+ " trainer_name,account_number) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";		
			
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
			personSTMT.setObject(17, (person.getMaritalStatus() != null && person.getMaritalStatus().length() > 0) ? person.getMaritalStatus() : null);
			personSTMT.setObject(18, person.getSpouseName());
			personSTMT.setObject(19, person.getCounty());
			personSTMT.setObject(20, person.getCountry());
			personSTMT.setObject(21, person.getPostCode());
			/*personSTMT.setObject(22, person.getCardType());
			personSTMT.setObject(23, person.getCardNumber());*/
			personSTMT.setObject(22, person.getTrainerName());
			personSTMT.setObject(23, person.getAccountNumber());
			
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
					+ " county = ?, country = ? , post_code = ?,  "
					+ " trainer_name = ?, account_number = ? where ref_id = ?";		
			
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
			personSTMT.setObject(16, (person.getMaritalStatus() != null && person.getMaritalStatus().length() > 0) ? person.getMaritalStatus() : null);
			personSTMT.setObject(17, person.getSpouseName());
			personSTMT.setObject(18, person.getCounty());
			personSTMT.setObject(19, person.getCountry());
			personSTMT.setObject(20, person.getPostCode());
			/*personSTMT.setObject(21, person.getCardType());
			personSTMT.setObject(22, person.getCardNumber());*/
			personSTMT.setObject(21, person.getTrainerName());
			personSTMT.setObject(22, person.getAccountNumber());
			personSTMT.setObject(23, person.getRefId());
			personSTMT.executeUpdate();
		}
	}
	
	@Override
	public List<SearchByNameTrainerBean> findByNameTrainer(String search) throws Exception {
		
		/*Query query = getCurrentSession().createSQLQuery("select p.surname as surname, "
				+ "p.firstname as firstname,  p.ref_id as refId from person as p join person_role as pr "
				+ "on p.id = pr.person_id where pr.role_id = ? and (p.surname like ? or p.firstname like ?) ");
		query.setLong(0, RoleEnum.TRAINER.getId());
		query.setString(1, "%"+search+"%");
		query.setString(2, "%"+search+"%");
		List<Object[]> records = query.list();
		List<SearchByNameTrainerBean> results = new ArrayList<SearchByNameTrainerBean>();
		if(records != null && records.size() > 0) {
			for (Object[] objects : records) {
				SearchByNameTrainerBean bean = new SearchByNameTrainerBean();
				bean.setId(Integer.parseInt(String.valueOf(objects[2])));
				bean.setName(String.valueOf(objects[0])+" "+String.valueOf(objects[1]));
				results.add(bean);
			}
		}
		return results;*/
		PreparedStatement pstmt = conn.getConnection().prepareStatement("select p.surname as surname, "
				+ "p.firstname as firstname,  p.ref_id as refId, p.account_number as accountNumber from person as p join person_role as pr "
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
			bean.setAccountNumber(set.getString("accountNumber"));
			records.add(bean);
		}
		return records;
	}
	
	@Override
	public TeTrainers setSomeFieldInTrainer(TeTrainers trainer) {
		
		try {
			
			/*Query query = getCurrentSession().createSQLQuery("select p.* from person as p join person_role as pr "
					+ "on p.id = pr.person_id where pr.role_id = ? and p.ref_id = ?");
			query.setLong(0, RoleEnum.TRAINER.getId());
			query.setLong(1, trainer.getTrainerId());
			List<Person> persons = query.list();
			if(persons != null && persons.size() > 0) {
				for (Person person : persons) {
					trainer.setTitle(person.getTitle());
					trainer.setSex(person.getSex());
					trainer.setNationality(person.getNationality());
					trainer.setMaritalStatus(person.getMaritalStatus());
					trainer.setSpouseName(person.getSpouseName());
					trainer.setCounty(person.getCounty());
					trainer.setCountry(person.getCountry());
					trainer.setPostCode(person.getPostCode());
				}
			}
			return trainer;*/
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
	
	@Override
	public List<SearchByNameEmployeeBean> getEmployeeByName(String search) {
		try {
			
			/*Query query = getCurrentSession().createSQLQuery("select p.surname as surname, "
					+ "p.firstname as firstname,  p.ref_id as refId, p.card_type as cardType, p.card_number as cardNumber"
					+ " from person as p join person_role as pr "
					+ "on p.id = pr.person_id where pr.role_id = ? and (p.surname like ? or p.firstname like ?) ");
			query.setLong(0, RoleEnum.EMPLOYEE.getId());
			query.setString(1, "%"+search+"%");
			query.setString(2, "%"+search+"%");	
			List<Object[]> records = query.list();
			List<SearchByNameEmployeeBean> results = new ArrayList<SearchByNameEmployeeBean>();
			if(records != null && records.size() > 0) {
				for (Object[] objects : records) {
					SearchByNameEmployeeBean bean = new SearchByNameEmployeeBean();
					bean.setId(Integer.parseInt(String.valueOf(objects[2])));
					bean.setFullName(String.valueOf(objects[0])+" "+String.valueOf(objects[1]));
					bean.setCardType(String.valueOf(objects[3]));
					bean.setCardNumber(String.valueOf(objects[4]));
					results.add(bean);
				}
			}
			return results;*/
			/*, p.card_type as cardType, p.card_number as cardNumber*/
			PreparedStatement pstmt = conn.getConnection().prepareStatement("select p.surname as surname, "
					+ "p.firstname as firstname,  p.ref_id as refId"
					+ " from person as p join person_role as pr "
					+ "on p.id = pr.person_id where pr.role_id = ? and (p.surname like ? or p.firstname like ?) ");
			pstmt.setObject(1, RoleEnum.EMPLOYEE.getId());
			pstmt.setObject(2, "%"+search+"%");
			pstmt.setObject(3, "%"+search+"%");
			ResultSet set = pstmt.executeQuery();
			List<SearchByNameEmployeeBean> records = new ArrayList<SearchByNameEmployeeBean>();
			while(set.next()) {
				SearchByNameEmployeeBean bean = new SearchByNameEmployeeBean();
				bean.setId(Integer.parseInt(set.getString("refId")));
				bean.setFullName(set.getString("surname")+" "+set.getString("firstname"));
				/*bean.setCardType(set.getString("cardType"));
				bean.setCardNumber(set.getString("cardNumber"));*/
				records.add(bean);
			}
			return records;
		} catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public List<SearchByNameEmployeeBean> getEmployeeByCardNumber(String ids) {
		
		try {
			/*Query query = getCurrentSession().createSQLQuery("select p.* "
					+ " from Person p, PersonRole pr "
					+ "where p.id = pr.personId and pr.roleId = ? and p.cardNumber like ? ");
			query.setLong(0, RoleEnum.EMPLOYEE.getId());
			query.setString(1, "%"+search+"%");
			List<Person> records = query.list();
			List<SearchByNameEmployeeBean> results = new ArrayList<SearchByNameEmployeeBean>();
			if(records != null && records.size() > 0) {
				for (Person person : records) {
					
					SearchByNameEmployeeBean bean = new SearchByNameEmployeeBean();
					bean.setId(person.getRefId());
					bean.setFullName(person.getFullName());
					bean.setCardType(person.getCardType());
					bean.setCardNumber(person.getCardNumber());
					results.add(bean);
				}
			}
			
			return results;*/
			/*, p.card_type as cardType, p.card_number as cardNumber*/
			PreparedStatement pstmt = conn.getConnection().prepareStatement("select p.surname as surname, "
					+ "p.firstname as firstname,  p.ref_id as refId"
					+ " from person as p join person_role as pr "
					+ "on p.id = pr.person_id where pr.role_id = ? and p.ref_id IN ("+ids+") ");
			pstmt.setObject(1, RoleEnum.EMPLOYEE.getId());
			ResultSet set = pstmt.executeQuery();
			List<SearchByNameEmployeeBean> records = new ArrayList<SearchByNameEmployeeBean>();
			while(set.next()) {
				SearchByNameEmployeeBean bean = new SearchByNameEmployeeBean();
				bean.setId(Integer.parseInt(set.getString("refId")));
				bean.setFullName(set.getString("surname")+" "+set.getString("firstname"));
				/*bean.setCardType(set.getString("cardType"));
				bean.setCardNumber(set.getString("cardNumber"));*/
				records.add(bean);
			}
			return records;
		} catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public int getAdvanceSearchRecordForAllACardCount(String ids) {
		
		try {
			/*Query query = getCurrentSession().createSQLQuery("select count(*) "
					+ " from person as p join person_role as pr "
					+ "on p.id = pr.person_id where pr.role_id = ? and p.card_type = ? ");
			query.setLong(0, RoleEnum.EMPLOYEE.getId());
			query.setString(1, "A");
			List<Object[]> records = query.list();
			if(records != null && records.size() > 0) {
				return Integer.parseInt(String.valueOf(records.get(0)[0]));
			}*/
			
			PreparedStatement pstmt = conn.getConnection().prepareStatement("select count(*) "
					+ " from person as p join person_role as pr "
					+ "on p.id = pr.person_id where pr.role_id = ? and p.ref_id IN ("+ids+") ");
			pstmt.setObject(1, RoleEnum.EMPLOYEE.getId());
			/*pstmt.setObject(2, "A");*/
			ResultSet set = pstmt.executeQuery();
			if(set.next()) {
				int count = set.getInt(1);
				return count;
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	@Override
	public int getAdvanceSearchRecordForAllBCardCount(String ids) {
		
		try {
			/*Query query = getCurrentSession().createSQLQuery("select count(*) "
					+ " from person as p join person_role as pr "
					+ "on p.id = pr.person_id where pr.role_id = ? and p.card_type = ? ");
			query.setLong(0, RoleEnum.EMPLOYEE.getId());
			query.setString(1, "B");
			List<Object[]> records = query.list();
			if(records != null && records.size() > 0) {
				return Integer.parseInt(String.valueOf(records.get(0)[0]));
			}*/
			PreparedStatement pstmt = conn.getConnection().prepareStatement("select count(*) "
					+ " from person as p join person_role as pr "
					+ "on p.id = pr.person_id where pr.role_id = ? and p.ref_id IN ("+ids+") ");
			pstmt.setObject(1, RoleEnum.EMPLOYEE.getId());
			/*pstmt.setObject(2, "B");*/
			ResultSet set = pstmt.executeQuery();
			if(set.next()) {
				int count = set.getInt(1);
				return count;
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	
	private List<AdvanceSearchRecordBean> getAdvanceSearchRecords(
			List<Object[]> records) {
		List<AdvanceSearchRecordBean> results = new ArrayList<AdvanceSearchRecordBean>();
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd"); 
		for (Object[] objects : records) {
			try {
				AdvanceSearchRecordBean bean = new AdvanceSearchRecordBean();
				bean.setName(String.valueOf(objects[0])+" "+String.valueOf(objects[1]));
				bean.setCardNumber(String.valueOf(objects[4])+" "+String.valueOf(objects[5]));
				bean.setDateOfBirth(formatter.parse(String.valueOf(objects[2])));
				bean.setTrainerName(String.valueOf(objects[3]));
				results.add(bean);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return results;
	}
	
	@Override
	public List<AdvanceSearchRecordBean> getAdvanceSearchRecordForAllACardByLimit(int start, int length,
			String ids) {
		
		try {
			
			/*Query query = getCurrentSession().createQuery("select p.surname as surname, "
					+ "p.firstname as firstname,  p.date_of_birth as dateOfBirth, p.trainer_name as trainerName, "
					+ "p.card_type as cardType, p.card_number as cardNumber"
					+ " from person as p join person_role as pr "
					+ "on p.id = pr.person_id where pr.role_id = ? and p.card_type = ? LIMIT ?, ? ");
			query.setLong(0, RoleEnum.EMPLOYEE.getId());
			query.setString(1, "A");
			query.setInteger(2, start);
			query.setInteger(3, length);
			List<Object[]> records = query.list();
			return getAdvanceSearchRecords(records);*/
			
			PreparedStatement pstmt = conn.getConnection().prepareStatement("select p.surname as surname, "
					+ "p.firstname as firstname,  p.date_of_birth as dateOfBirth, p.trainer_name as trainerName, "
					/*+ "p.card_type as cardType, p.card_number as cardNumber"*/
					+" p.ref_id as id "
					+ " from person as p join person_role as pr "
					+ "on p.id = pr.person_id where pr.role_id = ? and p.ref_id IN ("+ids+") LIMIT ?, ? ");
			pstmt.setObject(1, RoleEnum.EMPLOYEE.getId());
			/*pstmt.setObject(2, "A");*/
			pstmt.setInt(2, start);
			pstmt.setInt(3, length);
			System.out.println();
			ResultSet set = pstmt.executeQuery();
			return getAdvanceSearchRecords(set);
		} catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<AdvanceSearchRecordBean> getAdvanceSearchRecordForAllBCardByLimit(int start, int length,
			String ids) {
		
		try {
			
			/*Query query = getCurrentSession().createQuery("select p.surname as surname, "
					+ "p.firstname as firstname,  p.date_of_birth as dateOfBirth, p.trainer_name as trainerName, "
					+ "p.card_type as cardType, p.card_number as cardNumber"
					+ " from person as p join person_role as pr "
					+ "on p.id = pr.person_id where pr.role_id = ? and p.card_type = ? LIMIT ?, ? ");
			query.setLong(0, RoleEnum.EMPLOYEE.getId());
			query.setString(1, "B");
			query.setInteger(2, start);
			query.setInteger(3, length);
			List<Object[]> records = query.list();
			return getAdvanceSearchRecords(records);*/
			
			PreparedStatement pstmt = conn.getConnection().prepareStatement("select p.surname as surname, "
					+ "p.firstname as firstname,  p.date_of_birth as dateOfBirth, p.trainer_name as trainerName, "
					/*+ "p.card_type as cardType, p.card_number as cardNumber"*/
					+" p.ref_id as id "
					+ " from person as p join person_role as pr "
					+ "on p.id = pr.person_id where pr.role_id = ? and p.ref_id IN ("+ids+") LIMIT ?, ?");
			pstmt.setObject(1, RoleEnum.EMPLOYEE.getId());
			/*pstmt.setObject(2, "B");*/
			pstmt.setInt(2, start);
			pstmt.setInt(3, length);
			ResultSet set = pstmt.executeQuery();
			return getAdvanceSearchRecords(set);
		} catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public List<AdvanceSearchRecordBean> getAdvanceSearchRecordForAllACard(String ids) {
		
		try {
			
			/*Query query = getCurrentSession().createQuery("select p.surname as surname, "
					+ "p.firstname as firstname,  p.date_of_birth as dateOfBirth, p.trainer_name as trainerName, "
					+ "p.card_type as cardType, p.card_number as cardNumber"
					+ " from person as p join person_role as pr "
					+ "on p.id = pr.person_id where pr.role_id = ? and p.card_type = ? ");
			query.setLong(0, RoleEnum.EMPLOYEE.getId());
			query.setString(1, "A");
			List<Object[]> records = query.list();
			return getAdvanceSearchRecords(records);*/
			
			PreparedStatement pstmt = conn.getConnection().prepareStatement("select p.surname as surname, "
					+ "p.firstname as firstname,  p.date_of_birth as dateOfBirth, p.trainer_name as trainerName, "
					+" p.ref_id as id "
					/*+ "p.card_type as cardType, p.card_number as cardNumber"*/
					+ " from person as p join person_role as pr "
					+ "on p.id = pr.person_id where pr.role_id = ? and p.ref_id IN ("+ids+") ");
			pstmt.setObject(1, RoleEnum.EMPLOYEE.getId());
			/*pstmt.setObject(2, "A");*/
			ResultSet set = pstmt.executeQuery();
			return getAdvanceSearchRecords(set);
		} catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public List<AdvanceSearchRecordBean> getAdvanceSearchRecordForAllBCard(String ids) {
		
		try {
			/*Query query = getCurrentSession().createQuery("select p.surname as surname, "
					+ "p.firstname as firstname,  p.date_of_birth as dateOfBirth, p.trainer_name as trainerName, "
					+ "p.card_type as cardType, p.card_number as cardNumber"
					+ " from person as p join person_role as pr "
					+ "on p.id = pr.person_id where pr.role_id = ? and p.card_type = ? ");
			query.setLong(0, RoleEnum.EMPLOYEE.getId());
			query.setString(1, "B");
			List<Object[]> records = query.list();
			return getAdvanceSearchRecords(records);*/
			
			PreparedStatement pstmt = conn.getConnection().prepareStatement("select p.surname as surname, "
					+ "p.firstname as firstname,  p.date_of_birth as dateOfBirth, p.trainer_name as trainerName, "
					/*+ "p.card_type as cardType, p.card_number as cardNumber"*/
					+" p.ref_id as id "
					+ " from person as p join person_role as pr "
					+ "on p.id = pr.person_id where pr.role_id = ? and p.ref_id IN ("+ids+") ");
			pstmt.setObject(1, RoleEnum.EMPLOYEE.getId());
			/*pstmt.setObject(2, "B");*/
			ResultSet set = pstmt.executeQuery();
			return getAdvanceSearchRecords(set);
		} catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	//Convert set object to list of bean records
	private List<AdvanceSearchRecordBean> getAdvanceSearchRecords(ResultSet set) throws Exception {
		
		List<AdvanceSearchRecordBean> records = new ArrayList<AdvanceSearchRecordBean>();
		while(set.next()) {
			AdvanceSearchRecordBean bean = new AdvanceSearchRecordBean();
			bean.setName(set.getString("surname")+" "+set.getString("firstname"));
			/*bean.setCardNumber(set.getString("cardType")+" "+set.getString("cardNumber"));*/
			bean.setDateOfBirth(set.getDate("dateOfBirth"));
			bean.setTrainerName(set.getString("trainerName"));
			bean.setId(set.getInt("id"));
			records.add(bean);
		}
		return records;
	}
	
	@Override
	public List<TrainerUserBean> getTrainerUserBean() {
		
		try {
			PreparedStatement pstmt = conn.getConnection().prepareStatement("select p.surname as surname, "
					+ "p.firstname as firstname,  p.ref_id as id, p.account_number as accountNumber "
					+ " from person as p join person_role as pr "
					+ "on p.id = pr.person_id where pr.role_id = ? order by p.surname ");
			pstmt.setObject(1, RoleEnum.TRAINER.getId());
			ResultSet set = pstmt.executeQuery();
			return convertEntityToTrainerUserBean(set);
		} catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private List<TrainerUserBean> convertEntityToTrainerUserBean(ResultSet set) throws SQLException {
		
		List<TrainerUserBean> records = new ArrayList<TrainerUserBean>();
		while(set.next()) {
			TrainerUserBean trainerUserBean = new TrainerUserBean();
			trainerUserBean.setId(set.getInt("id"));
			trainerUserBean.setName(set.getString("firstname")+" "+set.getString("surname"));
			trainerUserBean.setAccNumber(set.getString("accountNumber"));
			records.add(trainerUserBean);
		}
		return records;
	}
	
	@Override
	public HashMap<String, Object> getEmpNameAndNumberById(Integer empId) {
		
		try {
			/*,  p.card_number as cardNumber*/
			PreparedStatement pstmt = conn.getConnection().prepareStatement("select p.surname as surname, "
					+ "p.firstname as firstname "
					+ " from person as p join person_role as pr "
					+ "on p.id = pr.person_id where pr.role_id = ? and p.ref_id  = ? order by p.surname ");
			pstmt.setObject(1, RoleEnum.EMPLOYEE.getId());
			pstmt.setObject(2, empId);
			ResultSet set = pstmt.executeQuery();
			return convertEntityToEmpMap(set);
		} catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private HashMap<String, Object> convertEntityToEmpMap(ResultSet set) throws SQLException {
		
		HashMap<String, Object> record = null;
		while(set.next()) {
			record = new HashMap<String, Object>();
			record.put("name", set.getString("firstname")+" "+set.getString("surname"));
			/*record.put("cardNumber", set.getString("cardNumber"));*/
		}
		return record;
	}
	
	@Override
	public String getTrainerMobileNumbers(String cmsIds) {
		try {
			PreparedStatement pstmt = conn.getConnection().prepareStatement("select p.mobile_no as mobileNo "
					+ " from person as p join person_role as pr "
					+ " on p.id = pr.person_id where pr.role_id = "+RoleEnum.TRAINER.getId()+" and p.ref_id  "
							+ "IN ( "+cmsIds+" ) ");
			ResultSet set = pstmt.executeQuery();
			return convertEntityToCMSMobileString(set);
		} catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private String convertEntityToCMSMobileString(ResultSet set) throws SQLException {
		
		String csmMobile = "";
		while(set.next()) {
			String mobile = set.getString("mobileNo");
			mobile = mobile.replaceAll("[^\\d0-9]", "");
			csmMobile += mobile+"\n";
		}
		return csmMobile;
	}
}
