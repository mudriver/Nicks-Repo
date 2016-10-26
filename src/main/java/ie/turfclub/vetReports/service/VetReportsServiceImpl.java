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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class VetReportsServiceImpl implements VetReportsService {

	static Logger logger = LoggerFactory.getLogger(VetReportsServiceImpl.class);
	
	   @Autowired
	    private SessionFactory sessionFactory;
	 
	    private Session getCurrentSession() {
	        return sessionFactory.getCurrentSession();
	    }
	
	@Override
	public List<VetreportReports> getAllVetReports(int startPoint, String sortBy, String reportType, String search, String searchType, String ascDesc) {
		
		System.out.println("from VetreportReports");
		 ascDesc = "asc";
		
		 
		 
		//Query query = getCurrentSession().createQuery("from VetreportReports v left join v.reportRacecardId r left join v.reportRandomId a ORDER By CASE WHEN r is null THEN a.randomInpsectionDate else r.dateRan END " + ascDesc);
		Query query;
		List<VetreportReports> reports = new ArrayList<>();
		
		String where = "";
	
		if(reportType.equals("NonRunner")){
			where = " WHERE v.reportType = 'NonRunner' ";
		}
		else if(reportType.equals("RandomStableInspection")){
			where = " WHERE v.reportType = 'RandomStableInspection' ";
		}
		else if(reportType.equals("PreRace")){
			where = " WHERE v.reportType = 'PreRace' ";
		}
		else if(reportType.equals("Track")){
			where = " WHERE v.reportType = 'Track' ";
		}
		
		if(searchType.equals("horse")){
			if(where.isEmpty()){
				where = " WHERE CASE WHEN r is null THEN (CASE WHEN h is null THEN 'ZNull' ELSE h.horseName END) else r.horseName END Like '%" + search +"%'";
				
			}
			else{
				where += " AND CASE WHEN r is null THEN (CASE WHEN h is null THEN 'ZNull' ELSE h.horseName END) else r.horseName END  Like '%" + search +"%'";
			}
		}
		
		
		if(sortBy.equals("horse")){
			System.out.println("from VetreportReports HORSE SORT");
			query = getCurrentSession().createQuery("from VetreportReports v left join v.reportRacecardId r left join v.reportRandomId a LEFT JOIN a.randomHorseId h" + where  + "ORDER By CASE WHEN r is null THEN CASE WHEN h is null THEN 'NONE ' ELSE h.horseName END else r.horseName END " + ascDesc);
			query.setMaxResults(30);
			List<Object[]> list = (List<Object[]>)query.list();
			  System.out.println(list.size());
      	
      	for(Object obj[]: list){
      		VetreportReports report = (VetreportReports) obj[0];
      		System.out.println(report.getReportType() + (report.getReportRacecardId() != null )+ " - " + (report.getReportRandomId() != null) );
      		
      		reports.add((VetreportReports) obj[0]);
      		}
		}
		else if(sortBy.equals("trainer")){
			query = getCurrentSession().createQuery("SELECT r.trainerName,CASE WHEN r IS NULL THEN 'NULL' ELSE CASE WHEN r.trainerName LIKE '%in GB%' THEN SUBSTRING(TRIM(SUBSTRING(r.trainerName, 1 ,LENGTH(r.trainerName) - LOCATE('(', REVERSE(r.trainerName)))), LENGTH(TRIM(SUBSTRING(r.trainerName, 1 ,LENGTH(r.trainerName) - LOCATE('(', REVERSE(r.trainerName))))) - LOCATE(' ', REVERSE(TRIM(SUBSTRING(r.trainerName, 1 ,LENGTH(r.trainerName) - LOCATE('(', REVERSE(r.trainerName)))))) + 2)  WHEN r.trainerName LIKE '%Jr.%' THEN SUBSTRING(TRIM(SUBSTRING(r.trainerName, 1 ,LENGTH(r.trainerName) - LOCATE('Jr.', REVERSE(r.trainerName)))), LENGTH(TRIM(SUBSTRING(r.trainerName, 1 ,LENGTH(r.trainerName) - LOCATE('Jr.', REVERSE(r.trainerName))))) - LOCATE(' ', REVERSE(TRIM(SUBSTRING(r.trainerName, 1 ,LENGTH(r.trainerName) - LOCATE('Jr.', REVERSE(r.trainerName)))))) + 2)   WHEN r.trainerName LIKE '% %'THEN SUBSTRING(r.trainerName, LENGTH(r.trainerName) - LOCATE(' ', REVERSE(r.trainerName)) + 2) WHEN r.trainerName LIKE '%((GB))%'THEN SUBSTRING(r.trainerName, LENGTH(r.trainerName) - LOCATE('.', REVERSE(r.trainerName), LENGTH(r.trainerName))) ELSE SUBSTRING(r.trainerName, LENGTH(r.trainerName) - LOCATE('.', REVERSE(r.trainerName), LENGTH(r.trainerName)))END END FROM VetreportReports v LEFT JOIN v.reportRacecardId r ORDER BY CASE WHEN r IS NULL THEN 'NULL' ELSE CASE WHEN r.trainerName LIKE '% %'THEN SUBSTRING(r.trainerName, LENGTH(r.trainerName) - LOCATE(' ', REVERSE(r.trainerName), LENGTH(r.trainerName)))ELSE SUBSTRING(r.trainerName, LENGTH(r.trainerName) - LOCATE('.', REVERSE(r.trainerName), LENGTH(r.trainerName)))END END ASC");
			for(Object[] obj : (List<Object[]>) query.list()){
				System.out.println(obj[0] + " --- " + obj[1] + "---" );
			}
			
			Criteria criteria = getCurrentSession().createCriteria(VetreportReports.class);
			
			criteria.add(Restrictions.not(Restrictions.like("reportType", "RandomStableInspection")));
			criteria.createAlias("reportRacecardId", "rc");
			criteria.addOrder(Order.desc(("rc.trainerName")));
			//criteria.addOrder(Property.forName("trainerName").asc());
			reports =  criteria.list();
			System.out.println("Sort by trainer" + reports.size());
		}
		//CASE WHEN r is null THEN 'NULL' ELSE CASE WHEN r.trainerName LIKE '% %' THEN SUBSTRING(r.trainerName, LENGTH(r.trainerName)-LOCATE(' ', REVERSE( r.trainerName), LENGTH(r.trainerName))) WHEN r.trainerName LIKE '%((GB))%' THEN SUBSTRING(r.trainerName, LENGTH(r.trainerName)-LOCATE('.', REVERSE( r.trainerName), LENGTH(r.trainerName)))  ELSE SUBSTRING(r.trainerName, LENGTH(r.trainerName)-LOCATE('.', REVERSE( r.trainerName), LENGTH(r.trainerName))) END END
		else{
			
			query = getCurrentSession().createQuery("from VetreportReports v left join v.reportRacecardId r left join v.reportRandomId a LEFT JOIN a.randomHorseId h" + where + " ORDER By CASE WHEN r is null THEN a.randomInpsectionDate else r.dateRan END desc");
			query.setMaxResults(100);
			List<Object[]> list = (List<Object[]>)query.list();
			  System.out.println(list.size());
      	
      	for(Object obj[]: list){
      		VetreportReports report = (VetreportReports) obj[0];
      		System.out.println(report.getReportType() + (report.getReportRacecardId() != null )+ " - " + (report.getReportRandomId() != null) );
      		
      		reports.add((VetreportReports) obj[0]);
      		}
			
			
			
			
		}
		
		
	    
	         
	    
	        
	        
	        /* System.out.println();
	        	 @SuppressWarnings("unchecked")
				List<Object[]> list = (List<Object[]>)query.list();
				  System.out.println(list.size());
	        	List<VetreportReports> reports = new ArrayList<>();
	        	for(Object obj[]: list){
	        		VetreportReports report = (VetreportReports) obj[0];
	        		System.out.println(report.getReportType() + (report.getReportRacecardId() != null )+ " - " + (report.getReportRandomId() != null) );
	        		
	        		reports.add((VetreportReports) obj[0]);
	        	}*/
	       /*      
	     if(sortBy.equals("horse")){
	    	 list = sortByHorseName(list, ascDesc);
	     }
	     else if(sortBy.equals("trainer")){
	    	 list = sortByTrainerName(list, ascDesc);
	     }
	     else if(sortBy.equals("date")){
	    	 list = sortByDate(list, ascDesc);
	     }
	     else{
	    	 list = sortByDate(list, ascDesc);
	     }
	        */
	     return reports;
	}
	
	
	
	@Override
	public List<VetreportReports> getVetReports(VetReportsSearch search) {
		
		
	     return null;
	}
	


	@Override
	public VetreportReports getVetReport(int reportNo) {
		VetreportReports report = (VetreportReports) getCurrentSession().get(VetreportReports.class, reportNo);
		return report;
	}

	@Override
	public void updateVetReport(VetreportReports report) {
		getCurrentSession().saveOrUpdate(report);
		
	}

	@Override
	public List<Object[]> getHorses(String date, String meeting) {
		
		Query query = getCurrentSession().createQuery("SELECT  rowId ,horseName from VetreportRacecard WHERE dateRan='" + date + "' AND meeting Like '%" + meeting + "%'");
		@SuppressWarnings("unchecked")
		List<Object[]> list = (List<Object[]>)query.list();
		
		
		
		if(list.isEmpty()){
			System.out.println("No Horses");
			return null;
		}
		else{
			return list;
		}
	}
	
	@Override
	public List<Object[]> getHorses(String chars) {
		Query query = getCurrentSession().createQuery("select horseName, CONCAT('H', horseId)  from  VetreportHorses WHERE horseName like '%" + chars.replaceAll("'", "''")  + "%' group by horseName");
		@SuppressWarnings("unchecked")
		List<Object[]> list = (List<Object[]>)query.list();
		query = getCurrentSession().createQuery("select notRanHorseName, CONCAT('N', notRanId) from  VetreportHorsesNotRan WHERE notRanHorseName like '%" + chars.replaceAll("'", "''")  + "%'  AND notRanHorseName NOT like '%All Horses%'");
		list.addAll((List<Object[]>)query.list());
		Collections.sort(list, HorseComparator);
		if(list.isEmpty()){
			//System.out.println("EMPTY");
			return new ArrayList<>();
		}
		else{
			return list;
		}
	}
	
	@Override
	public List<Object[]> getTrainers(String chars) {
		Query query = getCurrentSession().createQuery("select trainerName, trainerId from  VetreportTrainers WHERE trainerName like '%" + chars  + "%'");
		@SuppressWarnings("unchecked")
		List<Object[]> list = (List<Object[]>)query.list();
		if(list.isEmpty()){
			//System.out.println("EMPTY");
			return new ArrayList<>();
		}
		else{
			return list;
		}
	}

	@Override
	public List<String> getMeetings(String date) {
		Query query = getCurrentSession().createQuery("SELECT distinct meeting from VetreportRacecard WHERE dateRan='" + date  + "'");
		@SuppressWarnings("unchecked")
		List<String> list = (List<String>)query.list();
		if(list.isEmpty()){
			//System.out.println("EMPTY");
			return new ArrayList<>();
		}
		else{
			return list;
		}
	}

	@Override
	public VetreportRacecard getRacecard(int id) {
		
		VetreportRacecard card = (VetreportRacecard) getCurrentSession().get(VetreportRacecard.class, id);
		System.out.println(card.getTrainerName());
		return card;
	}

	@Override
	public List<VetreportVets> getVets() {
		Query query = getCurrentSession().createQuery("from VetreportVets");
		@SuppressWarnings("unchecked")
		List<VetreportVets> list = (List<VetreportVets>)query.list();
		
		if(list.isEmpty()){
			System.out.println("No VETS");
			return null;
		}
		else{
			return list;
		}
	}

	@Override
	public List<VetreportReportedbyVets> getVetsReportBy(int reportNo) {
		Query query = getCurrentSession().createQuery("from VetreportReportedbyVets WHERE reportedbyReportId='" + reportNo  + "'");
		@SuppressWarnings("unchecked")
		List<VetreportReportedbyVets> list = (List<VetreportReportedbyVets>)query.list();
		if(list.isEmpty()){
			//System.out.println("EMPTY");
			return new ArrayList<>();
		}
		else{
			return list;
		}
	}

	@Override
	public List<VetreportVets> getVetsWithSelectedSetForReportNo(int reportNo) {
		List<VetreportReportedbyVets> reportedBy = this.getVetsReportBy(reportNo);
		List<VetreportVets> vets = this.getVets();
		
		for(VetreportVets vet : vets){
			for(VetreportReportedbyVets reportBy : reportedBy){
				if(reportBy.getReportedbyVetId() == vet.getVetId()){
					vet.setSelected("selected");
				}
			}
			
			
		}
		
		return vets;
	}
	
	public VetreportHorses getHorse(int id){
	
		Query query = getCurrentSession().createQuery("from  VetreportHorses WHERE horseId='" + id + "'");
		@SuppressWarnings("unchecked")
		List<VetreportHorses> list = (List<VetreportHorses>)query.list();
		if(list.isEmpty()){
			//System.out.println("EMPTY");
			return null;
		}
		else{
			return list.get(0);
		}
	}
	
	public VetreportHorsesNotRan getHorseNotRan(int id){
		
		Query query = getCurrentSession().createQuery("from  VetreportHorsesNotRan WHERE notRanId='" + id + "'");
		@SuppressWarnings("unchecked")
		List<VetreportHorsesNotRan> list = (List<VetreportHorsesNotRan>)query.list();
		if(list.isEmpty()){
			//System.out.println("EMPTY");
			return null;
		}
		else{
			return list.get(0);
		}
	}

	@Override
	public VetreportAlert getAlert(int alertId) {

		Query query = getCurrentSession().createQuery("from  VetreportAlert WHERE alertId='" + alertId + "'");
		@SuppressWarnings("unchecked")
		List<VetreportAlert> list = (List<VetreportAlert>)query.list();
		if(list.isEmpty()){
			//System.out.println("EMPTY");
			return null;
		}
		else{
			return list.get(0);
		}
	}

	@Override
	public VetreportTrainers getTrainer(int trainerid) {
		Query query = getCurrentSession().createQuery("from  VetreportTrainers WHERE trainerId='" + trainerid + "'");
		@SuppressWarnings("unchecked")
		List<VetreportTrainers> list = (List<VetreportTrainers>)query.list();
		if(list.isEmpty()){
			//System.out.println("EMPTY");
			return null;
		}
		else{
			return list.get(0);
		}
	}
	

	
	

	@Override
	public int saveVetReport(VetreportReports report) {
		if(report.getReportId() == null){
			return (Integer) getCurrentSession().save(report);
		}
		else{
			getCurrentSession().saveOrUpdate(report);
			return report.getReportId();
		}
		
		
	}
	
	
	public static Comparator<Object[]> HorseComparator = new Comparator<Object[]>() {

		@Override
		public int compare(Object[] o1, Object[] o2) {
			
			return o1[0].toString().compareTo(o2[0].toString());
		}
	
	
	};

	@Override
	public VetreportHorsesNotRan getNotRanYetHorse(int id) {
		Query query = getCurrentSession().createQuery("from  VetreportHorsesNotRan WHERE notRanId='" + id + "'");
		@SuppressWarnings("unchecked")
		List<VetreportHorsesNotRan> list = (List<VetreportHorsesNotRan>)query.list();
		if(list.isEmpty()){
			//System.out.println("EMPTY");
			return null;
		}
		else{
			return list.get(0);
		}
	}

	@Override
	public void saveVetReportedBy(VetreportReportedbyVets reportedBy) {
		getCurrentSession().saveOrUpdate(reportedBy);
		
	}

	@Override
	public void deleteVetReportedBy(VetreportReportedbyVets reportedBy) {
		getCurrentSession().delete(reportedBy);
		
	}

	@Override
	public boolean checkReportedBy(int reportNo, int vetId) {
		Query query = getCurrentSession().createQuery("from  VetreportReportedbyVets v WHERE v.reportedbyVetId='" + vetId + "' AND v.reportedbyReportId='" + reportNo + "'");
		@SuppressWarnings("unchecked")
	
		List<VetreportReportedbyVets> list = (List<VetreportReportedbyVets>)query.list();
		if(list.isEmpty()){
			//System.out.println("EMPTY");
			return false;
		}
		else{
			return true;
		}
	} 
	
}
