package ie.turfclub.trainers.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.gson.annotations.Expose;

public class HistoriesMultiPpsJsonObject {

	@Expose
	private List<TeEmployentHistory> histories;
	@Expose
	private List<String> multiplePPsNumbers = new ArrayList<String>();
	@Expose
	private boolean multiplePPs;
	public List<TeEmployentHistory> getHistories() {
		return histories;
	}
	public void setHistories(List<TeEmployentHistory> histories) {
		this.histories = histories;
	}
	public List<String> getMultiplePPsNumbers() {
		return multiplePPsNumbers;
	}
	public void setMultiplePPsNumbers(List<String> multiplePPsNumbers) {
		this.multiplePPsNumbers = multiplePPsNumbers;
	}
	public boolean hasMultiplePPs() {
		return multiplePPs;
	}
	public void setMultiplePPs(boolean multiplePPs) {
		this.multiplePPs = multiplePPs;
	}
	
	//gets a list of multiple pps numbers
	public void setMultiplePPSNumbers(List<TeEmployentHistory> histories){

		Set<String> ppsNumbers = new HashSet<>();
		for(TeEmployentHistory history : histories){
			System.out.println(history.getEhPpsNumber());
			if(history.getEhPpsNumber() != null){
				ppsNumbers.add(history.getEhPpsNumber());
			}
			
		}
		System.out.println(ppsNumbers.size());
		
		this.multiplePPsNumbers.addAll(ppsNumbers);
		
		
		
		if(this.multiplePPsNumbers.size() > 1){
			this.multiplePPs = true;
		}
		else{
			this.multiplePPs = false;
		}
	}
	
	
}
