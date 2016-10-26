package ie.turfclub.trainers.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.gson.annotations.Expose;

public class PpsValidJsonObject {

	@Expose
	private boolean validPPs = false;
	@Expose
	private List<String> multiplePpsList = new ArrayList<>();
	
	public boolean isValidPPs() {
		return validPPs;
	}
	public void setValidPPs(boolean validPPs) {
		this.validPPs = validPPs;
	}
	
	public List<String> getMultiplePpsList() {
		return multiplePpsList;
	}
	public void setMultiplePpsList(List<TeEmployentHistory> histories) {
		Set<String> ppsNumbers = new HashSet<>();
		for(TeEmployentHistory history : histories){
			System.out.println(history.getEhPpsNumber());
			if(history.getEhPpsNumber() != null){
				ppsNumbers.add(history.getEhPpsNumber());
			}
			
		}
		System.out.println(ppsNumbers.size());
		
		this.multiplePpsList.addAll(ppsNumbers);
		
		
		
		if(this.multiplePpsList.size() > 1){
			this.validPPs = false;
		}
		else{
			this.validPPs = true;
		}
		
		
		
		
		
	}
	
	
	
}
