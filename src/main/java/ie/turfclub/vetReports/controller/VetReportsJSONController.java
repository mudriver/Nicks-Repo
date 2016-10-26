package ie.turfclub.vetReports.controller;


import ie.turfclub.vetReports.model.VetreportRacecard;
import ie.turfclub.vetReports.model.VetreportReports;
import ie.turfclub.vetReports.model.VetreportVets;
import ie.turfclub.vetReports.service.VetReportsService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/vetReports/service")
public class VetReportsJSONController {
	
 
	private SimpleDateFormat standardDate = new SimpleDateFormat("dd/MM/yyyy");
	private SimpleDateFormat sqlDate = new SimpleDateFormat("yyyy-MM-dd");
	
	
	

	@Autowired
	VetReportsService reportService;
	
	   
	@RequestMapping(value = "/meetings", method = RequestMethod.GET)
	 public @ResponseBody List<String> getMeetings(@RequestParam("date") String date) {
	  //System.out.println(foo);
		if (date.matches("([0-9]{2})/([0-9]{2})/([0-9]{4})")){
		   
		
		try {
		date = sqlDate.format(standardDate.parse(date));
	} catch (ParseException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
		}
		System.out.println("MEeetings " + date);
	  List<String> meetings = reportService.getMeetings(date);
	  if(meetings.isEmpty()){
		  System.out.println("NULL");
	  }else{
		  for(String meeting : meetings){
			  System.out.println(meeting);
		  }
	  }
	  
	  return meetings;
	 }
	
	@RequestMapping(value = "/horses", method = RequestMethod.GET)
	 public @ResponseBody List<Object[]> getHorses(@RequestParam("date") String date ,@RequestParam("meeting") String meeting ) {
	  //System.out.println(foo);
	 
		 try {
				date = sqlDate.format(standardDate.parse(date));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		 List<Object[]> horses = reportService.getHorses(date, meeting);
		 return horses;
	 }
	 
	 @RequestMapping(value = "/horsesAll", method = RequestMethod.GET)
	 public @ResponseBody List<Object[]> getAllHorses(@RequestParam("chars") String chars ) {
	  //System.out.println(foo);
	 
		 List<Object[]> horses = reportService.getHorses(chars);
		 for(Object[] obj : horses){
			 System.out.println(obj[0].toString() + " " + obj[1].toString());
		 }
		 return horses;
	 }
	 
		@RequestMapping(value = "/trainersAll", method = RequestMethod.GET)
		 public @ResponseBody List<Object[]> getTrainers(@RequestParam("chars") String chars) {
		  //System.out.println(foo);
		 
			 List<Object[]> trainers = reportService.getTrainers(chars);
			 return trainers;
		 }
	 
		 @RequestMapping(value = "/reports", method = RequestMethod.GET)
		 public @ResponseBody List<VetreportReports> getReports(@RequestParam(value="type" , required=false) String type, @RequestParam(value="sortBy" , required=false) String sortBy, @RequestParam(value="search", required=false) String search , @RequestParam(value="searchType", required=false) String searchType ) {
		     
			 System.out.println("SORTBY:" + sortBy + " " +  search + " " + searchType + " " + type);
			 
			 
			 List<VetreportReports> reports = reportService.getAllVetReports(0, sortBy, type, search, searchType, "");
			 return reports;
		 }
		 
	 
	 @RequestMapping(value = "/vets", method = RequestMethod.GET)
	 public @ResponseBody List<VetreportVets> getVets( ) {
	  //System.out.println(foo);
	 
		 List<VetreportVets> vets = reportService.getVets();
		 return vets;
	 }
	
	
	@RequestMapping(value = "/racecardDetail/{id}", method = RequestMethod.GET)
	 public @ResponseBody List<VetreportRacecard> getRaceCard(@PathVariable ("id") int raceCardId ) {
	  System.out.println(raceCardId);
	 
	 List<VetreportRacecard> rc = new ArrayList<>();
	 rc.add(reportService.getRacecard(raceCardId));
	  return rc;
	 }
	
	
	
	
	
	 
	 
	 
}