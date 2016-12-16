package ie.turfclub.person.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Table
@Entity(name="person")
public class Person {

	@Id
	@GeneratedValue
	@Column(name="id")
	private long id;
	
	@Column(name="ref_id")
	private Integer refId;
	
	/*@Column(name="pps_number")
	private String ppsNumber;
	
	@Column(name="title")
	private String title;*/
	
	@Column(name="surname")
	private String surname;
	
	@Column(name="firstname")
	private String firstname;
	
	@Transient
	private String fullname;
	
	@Column(name="date_of_birth")
	private Date dateOfBirth;
	
	/*@Column(name="nationality")
	private String nationality;
	
	@Column(name="sex")
	private String sex;
	
	@Column(name="marital_status")
	private String maritalStatus;
	
	@Column(name="spouse_name")
	private String spouseName;*/
	
	@Column(name="address1")
	private String address1;
	
	@Column(name="address2")
	private String address2;
	
	@Column(name="address3")
	private String address3;
	
	/*@Column(name="address4")
	private String address4;
	
	@Column(name="address5")
	private String address5;
	
	@Column(name="postcode")
	private String postCode;*/
	
	@Column(name="phone_no")
	private String phoneNo;
	
	@Column(name="mobile_no")
	private String mobileNo;

	@Column(name="email")
	private String email;
	
	@Column(name="comments")
	private String comments;
	
	/*@Column(name="hri_account_no")
	private String hriAccountNo;
	
	@Column(name="last_updated")
	private Date lastUpdated;*/
	
	@Column(name="date_entered")
	private Date dateEntered;
	
	/*@Column(name="is_new")
	private boolean isNew;
	
	@Column(name="has_taxable_earnings")
	private boolean hasTaxableEarnings;
	
	@Column(name="employee_verified")
	private boolean employeeVerified;*/
	
	@Column(name="request_date")
	private Date requestDate;
	
	/*@Column(name="existing_air_card_holder")
	private String existingAIRCardHolder;
	
	@Column(name="old_employee_card_holder")
	private String oldEmployeeCardNumber;
	
	@Column(name="category_of_employment")
	private String categoryOfEmployment;
	
	@Column(name="last_year_paid")
	private Double lastYearPaid;
	
	@Column(name="num_hour_worked")
	private Double numHourWorked;
	
	@Column(name="batch_no")
	private int batchNo;
	
	@Column(name="acc_no")
	private String accNo;
	
	@Column(name="stable_address1")
	private String stableAddress1;
	
	@Column(name="stable_address2")
	private String stableAddress2;
	
	@Column(name="stable_address3")
	private String stableAddress3;
	
	@Column(name="fax")
	private String fax;
	
	@Column(name="restricted")
	private String restricted;
	
	@Column(name="hunter_chase")
	private String hunterChase;
	
	@Column(name="curragh")
	private String curragh;
	
	@Column(name="insurance_expiry")
	private Date insuranceExpiry;
	
	@Column(name="last_racecard_id")
	private int lastRacecardId;
	
	@Column(name="return_completed")
	private boolean returnCompleted;
	
	@Column(name="contact_name")
	private String contactName;
	
	@Column(name="contact_number")
	private String contactNumber;
	
	@Column(name="verified_status")
	private VerifiedStatus verifiedStatus;
	
	@Column(name="notes")
	private String notes;
	
	@Column(name="role")
	private String role;*/

	@Transient
	private long roleId;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getFullname() {
		return fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getAddress1() {
		return address1;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	public String getAddress2() {
		return address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	public String getAddress3() {
		return address3;
	}

	public void setAddress3(String address3) {
		this.address3 = address3;
	}

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public Date getDateEntered() {
		return dateEntered;
	}

	public void setDateEntered(Date dateEntered) {
		this.dateEntered = dateEntered;
	}

	public Date getRequestDate() {
		return requestDate;
	}

	public void setRequestDate(Date requestDate) {
		this.requestDate = requestDate;
	}

	public long getRoleId() {
		return roleId;
	}

	public void setRoleId(long roleId) {
		this.roleId = roleId;
	}

	public Integer getRefId() {
		return refId;
	}

	public void setRefId(Integer refId) {
		this.refId = refId;
	}
}