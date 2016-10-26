package ie.turfclub.vetReports.service;

import ie.turfclub.vetReports.model.VetreportAlert;
import java.util.List;

public interface VetReportsAlertService {


	public List<VetreportAlert> getAlert();
	public void putOnAlert(VetreportAlert onAlert);
	public void offAlert(VetreportAlert offAlert);
}
