package ie.turfclub.inspections.model;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;

// default package
// Generated 26-Apr-2015 14:26:55 by Hibernate Tools 3.4.0.CR1


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * InspectionsSavedSearchesDates generated by hbm2java
 */
@Entity
@Table(name = "inspections_saved_searches_dates", catalog = "inspections")
public class InspectionsSavedSearchesDates implements java.io.Serializable {

	

	private int savedSearchId;
	private Date savedSearchStartDate;
	private Date savedSearchEndDate;



	public InspectionsSavedSearchesDates(int savedSearchId) {
		this.savedSearchId = savedSearchId;
	}

	public InspectionsSavedSearchesDates(int savedSearchId,
			Date savedSearchStartDate, Date savedSearchEndDate) {
		this.savedSearchId = savedSearchId;
		this.savedSearchStartDate = savedSearchStartDate;
		this.savedSearchEndDate = savedSearchEndDate;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "saved_search_id", unique = true, nullable = false)
	public int getSavedSearchId() {
		return this.savedSearchId;
	}

	public void setSavedSearchId(int savedSearchId) {
		this.savedSearchId = savedSearchId;
	}

	@Column(name = "saved_search_start_date", length = 10)
	public Date getSavedSearchStartDate() {
		return this.savedSearchStartDate;
	}

	public void setSavedSearchStartDate(Date savedSearchStartDate) {
		this.savedSearchStartDate = savedSearchStartDate;
	}

	@Column(name = "saved_search_end_date", length = 10)
	public Date getSavedSearchEndDate() {
		return this.savedSearchEndDate;
	}

	public void setSavedSearchEndDate(Date savedSearchEndDate) {
		this.savedSearchEndDate = savedSearchEndDate;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof InspectionsSavedSearchesDates))
			return false;
		InspectionsSavedSearchesDates castOther = (InspectionsSavedSearchesDates) other;

		return (this.getSavedSearchId() == castOther.getSavedSearchId())
				&& ((this.getSavedSearchStartDate() == castOther
						.getSavedSearchStartDate()) || (this
						.getSavedSearchStartDate() != null
						&& castOther.getSavedSearchStartDate() != null && this
						.getSavedSearchStartDate().equals(
								castOther.getSavedSearchStartDate())))
				&& ((this.getSavedSearchEndDate() == castOther
						.getSavedSearchEndDate()) || (this
						.getSavedSearchEndDate() != null
						&& castOther.getSavedSearchEndDate() != null && this
						.getSavedSearchEndDate().equals(
								castOther.getSavedSearchEndDate())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + this.getSavedSearchId();
		result = 37
				* result
				+ (getSavedSearchStartDate() == null ? 0 : this
						.getSavedSearchStartDate().hashCode());
		result = 37
				* result
				+ (getSavedSearchEndDate() == null ? 0 : this
						.getSavedSearchEndDate().hashCode());
		return result;
	}

}
