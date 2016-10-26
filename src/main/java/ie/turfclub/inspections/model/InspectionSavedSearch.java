package ie.turfclub.inspections.model;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.fasterxml.jackson.annotation.JsonManagedReference;

/**
 * @author chrisk
 *
 */
@Entity
@Table(name = "inspections_saved_searches", catalog = "inspections")
public class InspectionSavedSearch {

	private Integer savedSearchId;
	private String savedSearchName;
	private Integer savedSearchUserId;
	private Integer maxToShow = 25;
	private Integer currentRecordStart = 0;
	private Set<InspectionsPersonSearch> personSearch = new HashSet<InspectionsPersonSearch>();
	private Set<InspectionsCategories> categorySearch = new HashSet<InspectionsCategories>();
	private Set<InspectionsStatus> statusSearch = new HashSet<InspectionsStatus>();
	private Map<String, InspectionsOrderByFields> orderByFields = new HashMap<String, InspectionsOrderByFields>();

	
	
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "saved_search_id", unique = true, nullable = false)
	public Integer getSavedSearchId() {
		return savedSearchId;
	}
	public void setSavedSearchId(Integer savedSearchId) {
		this.savedSearchId = savedSearchId;
	}
	
	
	
	@Column(name = "saved_search_name", nullable = false)
	public String getSavedSearchName() {
		return savedSearchName;
	}
	public void setSavedSearchName(String savedSearchName) {
		this.savedSearchName = savedSearchName;
	}
	
	
	@Column(name = "saved_search_user_id", nullable = false)
	public Integer getSavedSearchUserId() {
		return savedSearchUserId;
	}
	public void setSavedSearchUserId(Integer savedSearchUserId) {
		this.savedSearchUserId = savedSearchUserId;
	}
	@Column(name = "saved_search_max_show", nullable = false)
	public Integer getMaxToShow() {
		return maxToShow;
	}
	
	public void setMaxToShow(Integer maxToShow) {
		this.maxToShow = maxToShow;
	}
	
	@Column(name = "saved_search_record_start", nullable = false)
	public Integer getCurrentRecordStart() {
		return currentRecordStart;
	}
	public void setCurrentRecordStart(Integer currentRecordStart) {
		this.currentRecordStart = currentRecordStart;
	}
	
	
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "SSSearchId")
	@JsonManagedReference
	public Set<InspectionsPersonSearch> getPersonSearch() {
		return personSearch;
	}
	public void setPersonSearch(Set<InspectionsPersonSearch> personSearch) {
		this.personSearch = personSearch;
	}
	

	
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy="fieldSavedSearchId")
    @MapKey(name="fieldTitle")   
	@JsonManagedReference
	public Map<String, InspectionsOrderByFields> getOrderByFields() {
		return orderByFields;
	}
	public void setOrderByFields(Map<String, InspectionsOrderByFields> orderByFields) {
		this.orderByFields = orderByFields;
	}
	

	@NotFound(action=NotFoundAction.IGNORE)  
	@ManyToMany(fetch=FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinTable(name="inspections_saved_searches_categories", catalog="inspections", joinColumns={
			@JoinColumn(name="saved_search_id", nullable=false, updatable=false)},
			inverseJoinColumns={@JoinColumn(name="saved_search_cat_id", nullable=false, updatable=false)})
	public Set<InspectionsCategories> getCategorySearch() {
		return categorySearch;
	}
	public void setCategorySearch(Set<InspectionsCategories> categorySearch) {
		this.categorySearch = categorySearch;
	}
	@NotFound(action=NotFoundAction.IGNORE)  
	@ManyToMany(fetch=FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinTable(name="inspections_saved_searches_status", catalog="inspections", joinColumns={
			@JoinColumn(name="saved_search_id", nullable=false, updatable=false)},
			inverseJoinColumns={@JoinColumn(name="saved_status_id", nullable=false, updatable=false)})
	public Set<InspectionsStatus> getStatusSearch() {
		return statusSearch;
	}
	public void setStatusSearch(Set<InspectionsStatus> statusSearch) {
		this.statusSearch = statusSearch;
	}
	
	
	
	
	
}
