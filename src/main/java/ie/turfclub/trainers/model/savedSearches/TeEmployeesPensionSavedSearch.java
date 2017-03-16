package ie.turfclub.trainers.model.savedSearches;

import ie.turfclub.trainers.model.TeEmployees;
import ie.turfclub.trainers.model.TeTrainers;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.FilterDefs;
import org.hibernate.annotations.FilterJoinTable;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;


@Entity
@DiscriminatorValue(value = "EMPLOYEE_PENSION_LIST")
public class TeEmployeesPensionSavedSearch extends TeSavedSearches{
	
	private Set<TeEmployees> employeesSearch = new HashSet<TeEmployees>();

	
	@NotFound(action=NotFoundAction.IGNORE)  
	@ManyToMany(fetch=FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinTable(name="te_saved_searches_employees", catalog="trainers", joinColumns={
			@JoinColumn(name="ss_search_id", nullable=false, updatable=false)},
			inverseJoinColumns={@JoinColumn(name="ss_employee_id", nullable=false, updatable=false)})
	public Set<TeEmployees> getEmployeesSearch() {
		return employeesSearch;
	}

	public void setEmployeesSearch(Set<TeEmployees> employeesSearch) {
		this.employeesSearch = employeesSearch;
	}
	
	
}
