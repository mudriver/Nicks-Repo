package ie.turfclub.trainers.model;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.google.gson.annotations.Expose;

@Entity
@Table(name = "air_table", catalog = "trainers")
public class AIRTable {

	@Expose
	private Integer airTableId;
	@Expose
	private String account;
	@Expose
	private String surname;
	@Expose
	private String firstname;
	@Expose
	private String categoryCode;
	@Expose
	private String address1;
	@Expose
	private String address2;
	@Expose
	private String address3;
	@Expose
	private String currentTrainerNum;
	@Expose
	private boolean active;
	@Expose
	private boolean postDirect;
	@Expose
	private boolean hriAccountHolder;
	@Expose
	private String hriAccNum;
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "air_table_id", unique = true, nullable = false)
	public Integer getAirTableId() {
		return airTableId;
	}
	
	public void setAirTableId(Integer airTableId) {
		this.airTableId = airTableId;
	}
	
	@Column(name="account", length=6)
	public String getAccount() {
		return account;
	}
	
	public void setAccount(String account) {
		this.account = account;
	}
	
	@Column(name="surname", length=30)
	public String getSurname() {
		return surname;
	}
	
	public void setSurname(String surname) {
		this.surname = surname;
	}
	
	@Column(name="firstname", length=30)
	public String getFirstname() {
		return firstname;
	}
	
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	
	@Column(name="category_code", length=2)
	public String getCategoryCode() {
		return categoryCode;
	}
	
	public void setCategoryCode(String categoryCode) {
		this.categoryCode = categoryCode;
	}
	
	@Column(name="address1", length=50)
	public String getAddress1() {
		return address1;
	}
	
	public void setAddress1(String address1) {
		this.address1 = address1;
	}
	
	@Column(name="address2", length=50)
	public String getAddress2() {
		return address2;
	}
	
	public void setAddress2(String address2) {
		this.address2 = address2;
	}
	
	@Column(name="address3", length=50)
	public String getAddress3() {
		return address3;
	}
	
	public void setAddress3(String address3) {
		this.address3 = address3;
	}
	
	@Column(name="current_trainer_num", length=10)
	public String getCurrentTrainerNum() {
		return currentTrainerNum;
	}
	
	public void setCurrentTrainerNum(String currentTrainerNum) {
		this.currentTrainerNum = currentTrainerNum;
	}
	
	@Column(name="active")
	public boolean isActive() {
		return active;
	}
	
	public void setActive(boolean active) {
		this.active = active;
	}
	
	@Column(name="post_direct")
	public boolean isPostDirect() {
		return postDirect;
	}
	
	public void setPostDirect(boolean postDirect) {
		this.postDirect = postDirect;
	}
	
	@Column(name="hri_account_holder")
	public boolean isHriAccountHolder() {
		return hriAccountHolder;
	}
	
	public void setHriAccountHolder(boolean hriAccountHolder) {
		this.hriAccountHolder = hriAccountHolder;
	}
	
	@Column(name="hri_acc_num", length=50)
	public String getHriAccNum() {
		return hriAccNum;
	}
	public void setHriAccNum(String hriAccNum) {
		this.hriAccNum = hriAccNum;
	}
	
	
}
