package ie.turfclub.vetReports.model.searches;

import ie.turfclub.trainers.model.savedSearches.TeSavedSearches;

public class VROrderByFields {

	private Integer fieldId;
	private TeSavedSearches fieldSavedSearchId;
	private String fieldOrder;
	private Integer fieldPriority;
	private String fieldTitle;
	public Integer getFieldId() {
		return fieldId;
	}
	public void setFieldId(Integer fieldId) {
		this.fieldId = fieldId;
	}
	public TeSavedSearches getFieldSavedSearchId() {
		return fieldSavedSearchId;
	}
	public void setFieldSavedSearchId(TeSavedSearches fieldSavedSearchId) {
		this.fieldSavedSearchId = fieldSavedSearchId;
	}
	public String getFieldOrder() {
		return fieldOrder;
	}
	public void setFieldOrder(String fieldOrder) {
		this.fieldOrder = fieldOrder;
	}
	public Integer getFieldPriority() {
		return fieldPriority;
	}
	public void setFieldPriority(Integer fieldPriority) {
		this.fieldPriority = fieldPriority;
	}
	public String getFieldTitle() {
		return fieldTitle;
	}
	public void setFieldTitle(String fieldTitle) {
		this.fieldTitle = fieldTitle;
	}
	
	
}
