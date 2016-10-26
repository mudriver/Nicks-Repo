package ie.turfclub.vetReports.model;
// Generated 12-Nov-2014 16:48:32 by Hibernate Tools 3.4.0.CR1

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;


@Entity
@Table(name = "vetreport_racecard", catalog = "vetreports")
public class VetreportRacecard implements java.io.Serializable {

	private int rowId;
	private String accountNo;
	private String trainerName;
	private String raceNo;
	private String horseName;
	private String riderName;
	private String owner1;
	private String owner2;
	private String owner3;
	private String owner4;
	@Temporal(TemporalType.DATE)
	@DateTimeFormat (pattern="dd/MM/YYYY")
	private Date dateRan;
	private String meeting;
	private Date date;
	private boolean betFairImported;
	private int horseNum;
	private String raceCode;
	private String raceName;
	private String jockeyAccNo;

	public VetreportRacecard() {
	}

	public VetreportRacecard(int rowId, String accountNo, String trainerName,
			String raceNo, String horseName, String riderName, String owner1,
			String owner2, String owner3, String owner4, Date dateRan,
			String meeting, Date date, boolean betFairImported, int horseNum) {
		this.rowId = rowId;
		this.accountNo = accountNo;
		this.trainerName = trainerName;
		this.raceNo = raceNo;
		this.horseName = horseName;
		this.riderName = riderName;
		this.owner1 = owner1;
		this.owner2 = owner2;
		this.owner3 = owner3;
		this.owner4 = owner4;
		this.dateRan = dateRan;
		this.meeting = meeting;
		this.date = date;
		this.betFairImported = betFairImported;
		this.horseNum = horseNum;
	}

	public VetreportRacecard(int rowId, String accountNo, String trainerName,
			String raceNo, String horseName, String riderName, String owner1,
			String owner2, String owner3, String owner4, Date dateRan,
			String meeting, Date date, boolean betFairImported, int horseNum,
			String raceCode, String raceName, String jockeyAccNo) {
		this.rowId = rowId;
		this.accountNo = accountNo;
		this.trainerName = trainerName;
		this.raceNo = raceNo;
		this.horseName = horseName;
		this.riderName = riderName;
		this.owner1 = owner1;
		this.owner2 = owner2;
		this.owner3 = owner3;
		this.owner4 = owner4;
		this.dateRan = dateRan;
		this.meeting = meeting;
		this.date = date;
		this.betFairImported = betFairImported;
		this.horseNum = horseNum;
		this.raceCode = raceCode;
		this.raceName = raceName;
		this.jockeyAccNo = jockeyAccNo;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "row_id", unique = true, nullable = false)
	public int getRowId() {
		return this.rowId;
	}

	public void setRowId(int rowId) {
		this.rowId = rowId;
	}

	@Column(name = "Account_No", nullable = false, length = 10)
	public String getAccountNo() {
		return this.accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	@Column(name = "Trainer_Name", nullable = false, length = 50)
	public String getTrainerName() {
		return this.trainerName;
	}

	public void setTrainerName(String trainerName) {
		this.trainerName = trainerName;
	}

	@Column(name = "Race_No", nullable = false, length = 50)
	public String getRaceNo() {
		return this.raceNo;
	}

	public void setRaceNo(String raceNo) {
		this.raceNo = raceNo;
	}

	@Column(name = "Horse_Name", nullable = false, length = 50)
	public String getHorseName() {
		return this.horseName;
	}

	public void setHorseName(String horseName) {
		this.horseName = horseName;
	}

	@Column(name = "Rider_Name", nullable = false, length = 50)
	public String getRiderName() {
		return this.riderName;
	}

	public void setRiderName(String riderName) {
		this.riderName = riderName;
	}

	@Column(name = "Owner1", nullable = false, length = 50)
	public String getOwner1() {
		return this.owner1;
	}

	public void setOwner1(String owner1) {
		this.owner1 = owner1;
	}

	@Column(name = "Owner2", nullable = false, length = 50)
	public String getOwner2() {
		return this.owner2;
	}

	public void setOwner2(String owner2) {
		this.owner2 = owner2;
	}

	@Column(name = "Owner3", nullable = false, length = 50)
	public String getOwner3() {
		return this.owner3;
	}

	public void setOwner3(String owner3) {
		this.owner3 = owner3;
	}

	@Column(name = "Owner4", nullable = false, length = 50)
	public String getOwner4() {
		return this.owner4;
	}

	public void setOwner4(String owner4) {
		this.owner4 = owner4;
	}

	@Column(name = "DateRan", nullable = false, length = 10)
	public Date getDateRan() {
		return this.dateRan;
	}

	public void setDateRan(Date dateRan) {
		this.dateRan = dateRan;
	}

	@Column(name = "Meeting", nullable = false, length = 50)
	public String getMeeting() {
		return this.meeting;
	}

	public void setMeeting(String meeting) {
		this.meeting = meeting;
	}

	@Column(name = "Date", nullable = false, length = 19)
	public Date getDate() {
		return this.date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	@Column(name = "Bet_Fair_Imported", nullable = false)
	public boolean isBetFairImported() {
		return this.betFairImported;
	}

	public void setBetFairImported(boolean betFairImported) {
		this.betFairImported = betFairImported;
	}

	@Column(name = "Horse_Num", nullable = false)
	public int getHorseNum() {
		return this.horseNum;
	}

	public void setHorseNum(int horseNum) {
		this.horseNum = horseNum;
	}

	@Column(name = "raceCode", length = 10)
	public String getRaceCode() {
		return this.raceCode;
	}

	public void setRaceCode(String raceCode) {
		this.raceCode = raceCode;
	}

	@Column(name = "raceName", length = 200)
	public String getRaceName() {
		return this.raceName;
	}

	public void setRaceName(String raceName) {
		this.raceName = raceName;
	}

	@Column(name = "jockeyAccNo", length = 200)
	public String getJockeyAccNo() {
		return this.jockeyAccNo;
	}

	public void setJockeyAccNo(String jockeyAccNo) {
		this.jockeyAccNo = jockeyAccNo;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof VetreportRacecard))
			return false;
		VetreportRacecard castOther = (VetreportRacecard) other;

		return (this.getRowId() == castOther.getRowId())
				&& ((this.getAccountNo() == castOther.getAccountNo()) || (this
						.getAccountNo() != null
						&& castOther.getAccountNo() != null && this
						.getAccountNo().equals(castOther.getAccountNo())))
				&& ((this.getTrainerName() == castOther.getTrainerName()) || (this
						.getTrainerName() != null
						&& castOther.getTrainerName() != null && this
						.getTrainerName().equals(castOther.getTrainerName())))
				&& ((this.getRaceNo() == castOther.getRaceNo()) || (this
						.getRaceNo() != null && castOther.getRaceNo() != null && this
						.getRaceNo().equals(castOther.getRaceNo())))
				&& ((this.getHorseName() == castOther.getHorseName()) || (this
						.getHorseName() != null
						&& castOther.getHorseName() != null && this
						.getHorseName().equals(castOther.getHorseName())))
				&& ((this.getRiderName() == castOther.getRiderName()) || (this
						.getRiderName() != null
						&& castOther.getRiderName() != null && this
						.getRiderName().equals(castOther.getRiderName())))
				&& ((this.getOwner1() == castOther.getOwner1()) || (this
						.getOwner1() != null && castOther.getOwner1() != null && this
						.getOwner1().equals(castOther.getOwner1())))
				&& ((this.getOwner2() == castOther.getOwner2()) || (this
						.getOwner2() != null && castOther.getOwner2() != null && this
						.getOwner2().equals(castOther.getOwner2())))
				&& ((this.getOwner3() == castOther.getOwner3()) || (this
						.getOwner3() != null && castOther.getOwner3() != null && this
						.getOwner3().equals(castOther.getOwner3())))
				&& ((this.getOwner4() == castOther.getOwner4()) || (this
						.getOwner4() != null && castOther.getOwner4() != null && this
						.getOwner4().equals(castOther.getOwner4())))
				&& ((this.getDateRan() == castOther.getDateRan()) || (this
						.getDateRan() != null && castOther.getDateRan() != null && this
						.getDateRan().equals(castOther.getDateRan())))
				&& ((this.getMeeting() == castOther.getMeeting()) || (this
						.getMeeting() != null && castOther.getMeeting() != null && this
						.getMeeting().equals(castOther.getMeeting())))
				&& ((this.getDate() == castOther.getDate()) || (this.getDate() != null
						&& castOther.getDate() != null && this.getDate()
						.equals(castOther.getDate())))
				&& (this.isBetFairImported() == castOther.isBetFairImported())
				&& (this.getHorseNum() == castOther.getHorseNum())
				&& ((this.getRaceCode() == castOther.getRaceCode()) || (this
						.getRaceCode() != null
						&& castOther.getRaceCode() != null && this
						.getRaceCode().equals(castOther.getRaceCode())))
				&& ((this.getRaceName() == castOther.getRaceName()) || (this
						.getRaceName() != null
						&& castOther.getRaceName() != null && this
						.getRaceName().equals(castOther.getRaceName())))
				&& ((this.getJockeyAccNo() == castOther.getJockeyAccNo()) || (this
						.getJockeyAccNo() != null
						&& castOther.getJockeyAccNo() != null && this
						.getJockeyAccNo().equals(castOther.getJockeyAccNo())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + this.getRowId();
		result = 37 * result
				+ (getAccountNo() == null ? 0 : this.getAccountNo().hashCode());
		result = 37
				* result
				+ (getTrainerName() == null ? 0 : this.getTrainerName()
						.hashCode());
		result = 37 * result
				+ (getRaceNo() == null ? 0 : this.getRaceNo().hashCode());
		result = 37 * result
				+ (getHorseName() == null ? 0 : this.getHorseName().hashCode());
		result = 37 * result
				+ (getRiderName() == null ? 0 : this.getRiderName().hashCode());
		result = 37 * result
				+ (getOwner1() == null ? 0 : this.getOwner1().hashCode());
		result = 37 * result
				+ (getOwner2() == null ? 0 : this.getOwner2().hashCode());
		result = 37 * result
				+ (getOwner3() == null ? 0 : this.getOwner3().hashCode());
		result = 37 * result
				+ (getOwner4() == null ? 0 : this.getOwner4().hashCode());
		result = 37 * result
				+ (getDateRan() == null ? 0 : this.getDateRan().hashCode());
		result = 37 * result
				+ (getMeeting() == null ? 0 : this.getMeeting().hashCode());
		result = 37 * result
				+ (getDate() == null ? 0 : this.getDate().hashCode());
		result = 37 * result + (this.isBetFairImported() ? 1 : 0);
		result = 37 * result + this.getHorseNum();
		result = 37 * result
				+ (getRaceCode() == null ? 0 : this.getRaceCode().hashCode());
		result = 37 * result
				+ (getRaceName() == null ? 0 : this.getRaceName().hashCode());
		result = 37
				* result
				+ (getJockeyAccNo() == null ? 0 : this.getJockeyAccNo()
						.hashCode());
		return result;
	}

}
