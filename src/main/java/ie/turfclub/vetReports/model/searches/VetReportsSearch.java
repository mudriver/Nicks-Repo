package ie.turfclub.vetReports.model.searches;



import java.util.HashMap;
import java.util.Map;

public class VetReportsSearch {

	private Integer savedSearchId;
	private String savedSearchName;
	private Integer savedSearchUserId;
	private Integer maxToShow = 25;
	private Integer currentRecordStart = 0;
	private Map<String, VROrderByFields> orderByFields = new HashMap<String, VROrderByFields>();
	
	public Integer getSavedSearchId() {
		return savedSearchId;
	}
	public void setSavedSearchId(Integer savedSearchId) {
		this.savedSearchId = savedSearchId;
	}
	public String getSavedSearchName() {
		return savedSearchName;
	}
	public void setSavedSearchName(String savedSearchName) {
		this.savedSearchName = savedSearchName;
	}
	public Integer getSavedSearchUserId() {
		return savedSearchUserId;
	}
	public void setSavedSearchUserId(Integer savedSearchUserId) {
		this.savedSearchUserId = savedSearchUserId;
	}
	public Integer getMaxToShow() {
		return maxToShow;
	}
	public void setMaxToShow(Integer maxToShow) {
		this.maxToShow = maxToShow;
	}
	public Integer getCurrentRecordStart() {
		return currentRecordStart;
	}
	public void setCurrentRecordStart(Integer currentRecordStart) {
		this.currentRecordStart = currentRecordStart;
	}
	public Map<String, VROrderByFields> getOrderByFields() {
		return orderByFields;
	}
	public void setOrderByFields(Map<String, VROrderByFields> orderByFields) {
		this.orderByFields = orderByFields;
	}
	
	
	
}
