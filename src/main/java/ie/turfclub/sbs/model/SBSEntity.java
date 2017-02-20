package ie.turfclub.sbs.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Entity
@Table(name = "sbs", catalog = "trainers")
public class SBSEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	static Logger logger = LoggerFactory.getLogger(SBSEntity.class);

	@Id
	@GeneratedValue
	@Column(name="id")
	private long id;
	
	@Column(name="mail_to")
	private String mailTo;
	
	@Column(name="sbs_name")
	private String sbsName;
	
	@Column(name="address1")
	private String address1;
	
	@Column(name="address2")
	private String address2;
	
	@Column(name="address3")
	private String address3;
	
	@Column(name="address4")
	private String address4;
	
	@Column(name="title")
	private String title;
	
	@Column(name="surname")
	private String surname;
	
	@Column(name="amount")
	private double amount;
	
	@Column(name="eft")
	private boolean eft;
	
	@Column(name="trainer_id")
	private String trainerId;
	
	@Column(name="sbs_ac")
	private String sbsAc;
	
	@Column(name="sur_n")
	private String surN;
	
	@Column(name="first_n")
	private String firstN;
	
	@Column(name="created_date")
	private Date createdDate;
	
	@Column(name="is_returned")
	private boolean returned;

	public boolean isReturned() {
		return returned;
	}

	public void setReturned(boolean returned) {
		this.returned = returned;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getMailTo() {
		return mailTo;
	}

	public void setMailTo(String mailTo) {
		this.mailTo = mailTo;
	}

	public String getSbsName() {
		return sbsName;
	}

	public void setSbsName(String sbsName) {
		this.sbsName = sbsName;
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

	public String getAddress4() {
		return address4;
	}

	public void setAddress4(String address4) {
		this.address4 = address4;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public boolean isEft() {
		return eft;
	}

	public void setEft(boolean eft) {
		this.eft = eft;
	}

	public String getTrainerId() {
		return trainerId;
	}

	public void setTrainerId(String trainerId) {
		this.trainerId = trainerId;
	}

	public String getSbsAc() {
		return sbsAc;
	}

	public void setSbsAc(String sbsAc) {
		this.sbsAc = sbsAc;
	}

	public String getSurN() {
		return surN;
	}

	public void setSurN(String surN) {
		this.surN = surN;
	}

	public String getFirstN() {
		return firstN;
	}

	public void setFirstN(String firstN) {
		this.firstN = firstN;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	
}
