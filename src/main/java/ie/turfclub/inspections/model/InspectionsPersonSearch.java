package ie.turfclub.inspections.model;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

import ie.turfclub.inspections.model.InspectionSavedSearch;

@Entity
@Table(name = "inspections_saved_searches_person", catalog = "inspections")
public class InspectionsPersonSearch {

	private Integer SSId;
	private InspectionSavedSearch SSSearchId;
	private String id;
	
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "ss_id", unique = true, nullable = false)
	public Integer getSSId() {
		return SSId;
	}
	public void setSSId(Integer sSId) {
		SSId = sSId;
	}
	


	@ManyToOne(optional = false, targetEntity=InspectionSavedSearch.class , cascade = CascadeType.ALL)
	@JoinColumn( name = "ss_search_id", referencedColumnName = "saved_search_id", nullable = true)
	@JsonBackReference
	public InspectionSavedSearch getSSSearchId() {
		return SSSearchId;
	}
	public void setSSSearchId(InspectionSavedSearch sSSearchId) {
		SSSearchId = sSSearchId;
	}
	
	@Column(name = "ss_search_person_id", nullable = true)
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	
	
	

	
	
	
	
}
