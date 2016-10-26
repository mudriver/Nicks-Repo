package ie.turfclub.vetReports.service;

import ie.turfclub.vetReports.model.VetreportAlert;
import ie.turfclub.vetReports.model.VetreportHorses;
import ie.turfclub.vetReports.model.VetreportHorsesNotRan;
import ie.turfclub.vetReports.model.VetreportRacecard;
import ie.turfclub.vetReports.model.VetreportReportedbyVets;
import ie.turfclub.vetReports.model.VetreportReports;
import ie.turfclub.vetReports.model.VetreportTrainers;
import ie.turfclub.vetReports.model.VetreportVets;
import ie.turfclub.vetReports.model.searches.VetReportsSearch;

import java.util.List;

public interface VetReportsService {

	public List<VetreportReports> getAllVetReports(int startPoint, String sortBy, String reportType, String search, String searchType, String ascDesc);
	public VetreportReports getVetReport(int reportNo);
	public int saveVetReport(VetreportReports report);
	public void saveVetReportedBy(VetreportReportedbyVets reportedBy);
	public void deleteVetReportedBy(VetreportReportedbyVets reportedBy);
	public List<VetreportVets> getVets();
	public List<VetreportVets> getVetsWithSelectedSetForReportNo(int reportNo);
	public void updateVetReport(VetreportReports report);
	public List<Object[]> getHorses(String date, String meeting);
	public VetreportRacecard getRacecard(int id);
	public List<String> getMeetings(String date);
	public List<VetreportReportedbyVets> getVetsReportBy(int reportNo);
	public boolean checkReportedBy(int reportNo, int vetId);
	public List<Object[]> getTrainers(String chars);
	public List<Object[]> getHorses(String chars);
	public VetreportHorses getHorse(int id);
	public VetreportHorsesNotRan getNotRanYetHorse(int id);
	public VetreportAlert getAlert(int alertId);
	public VetreportTrainers getTrainer(int trainerid);
	public VetreportHorsesNotRan getHorseNotRan(int id);
	public List<VetreportReports> getVetReports(VetReportsSearch search);
}
