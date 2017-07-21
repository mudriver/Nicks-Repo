package ie.turfclub.common.bean;

import java.io.Serializable;

public class InapproveEmployeeBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private String trainerAccountNumber;
	private String trainerName;
	private String employeeName;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getTrainerAccountNumber() {
		return trainerAccountNumber;
	}
	public void setTrainerAccountNumber(String trainerAccountNumber) {
		this.trainerAccountNumber = trainerAccountNumber;
	}
	public String getTrainerName() {
		return trainerName;
	}
	public void setTrainerName(String trainerName) {
		this.trainerName = trainerName;
	}
	public String getEmployeeName() {
		return employeeName;
	}
	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}
}
