package ie.turfclub.vetReports.model;

// default package
// Generated 11-Dec-2014 16:08:13 by Hibernate Tools 3.4.0.CR1

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * VetreportAlertHorsesWithTrainerId generated by hbm2java
 */
@Entity
@Table(name = "vetreport_alert_horses_with_trainer", catalog = "vetreports")
public class VetreportAlertHorsesWithTrainer implements java.io.Serializable {

	private String alertId;
	private String trainerName;
	private String horseName;

	public VetreportAlertHorsesWithTrainer() {
	}

	public VetreportAlertHorsesWithTrainer(String trainerName,
			String horseName) {
		this.trainerName = trainerName;
		this.horseName = horseName;
	}

	
	@Id
	@Column(name = "alert_id", unique = true, nullable = false)
	public String getAlertId() {
		return alertId;
	}

	public void setAlertId(String alertId) {
		this.alertId = alertId;
	}

	@Column(name = "alert_trainer_name", nullable = false, length = 100)
	public String getTrainerName() {
		return this.trainerName;
	}

	public void setTrainerName(String trainerName) {
		this.trainerName = trainerName;
	}

	@Column(name = "alert_horse_name", nullable = false, length = 100)
	public String getHorseName() {
		return this.horseName;
	}

	public void setHorseName(String horseName) {
		this.horseName = horseName;
	}




}
