package ie.turfclub.inspections.model;

// default package
// Generated 11-Feb-2015 16:52:50 by Hibernate Tools 3.4.0.CR1

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * InspectionsStatus generated by hbm2java
 */
@Entity
@Table(name = "inspections_status", catalog = "inspections")
public class InspectionsStatus implements java.io.Serializable {

	private Integer statusId;
	private String statusType;

	public InspectionsStatus() {
	}

	public InspectionsStatus(String statusType) {
		this.statusType = statusType;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "status_id", unique = true, nullable = false)
	public Integer getStatusId() {
		return this.statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}

	@Column(name = "status_type", nullable = false, length = 200)
	public String getStatusType() {
		return this.statusType;
	}

	public void setStatusType(String statusType) {
		this.statusType = statusType;
	}

}
