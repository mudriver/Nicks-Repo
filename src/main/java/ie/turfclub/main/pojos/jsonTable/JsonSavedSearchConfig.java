package ie.turfclub.main.pojos.jsonTable;

import java.util.ArrayList;
import java.util.List;

import ie.turfclub.inspections.model.InspectionSavedSearch;
import ie.turfclub.trainers.model.savedSearches.TeSavedSearches;

public class JsonSavedSearchConfig {

	private String saveUrl;
	private List<Object> savedSearches = new ArrayList<Object>();

	
	public String getSaveUrl() {
		return saveUrl;
	}
	public void setSaveUrl(String saveUrl) {
		this.saveUrl = saveUrl;
	}

	public List<Object> getSavedSearches() {
		return savedSearches;
	}
	public void setSavedSearches(List<Object> savedSearches) {
		this.savedSearches = savedSearches;
	}

	
	
	
}
