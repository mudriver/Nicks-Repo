package ie.turfclub.inspections.service;



import ie.turfclub.inspections.model.InspectionSavedSearch;
import ie.turfclub.inspections.model.InspectionsCategories;
import ie.turfclub.inspections.model.InspectionsHandlers;
import ie.turfclub.inspections.model.InspectionsInspections;
import ie.turfclub.inspections.model.InspectionsOfficials;
import ie.turfclub.inspections.model.InspectionsStatus;
import ie.turfclub.inspections.model.InspectionsSubCategories;
import ie.turfclub.inspections.model.InspectionsTrainers;







import ie.turfclub.inspections.model.InspectionsUnregistered;
import ie.turfclub.inspections.pojos.InspectionsAddress;


import java.util.HashMap;
import java.util.List;

public interface InspectionsService {

	public HashMap<String, Object> getInspectionsCacheJson(String roleType);
	public HashMap<String, Object> getInspectionsJson(String roleType , InspectionSavedSearch savedSearch);
	public InspectionsInspections getInspection(int id);
	public List<HashMap<String, String>>  getPeople(String chars);
	public HashMap<String, String>  getPersonAddress(String id);
	public HashMap<String, String> getPersonYardAddress(String id);
	public InspectionsStatus getStatus(int id);
	public InspectionsCategories getCategory(int id);
	public List<HashMap<String, String>> getCategories();
	public List<HashMap<String, String>> getSubCategories();
	public List<HashMap<String, String>> getCategories(String roleType);
	public List<HashMap<String, String>> getSubCategories(String roleType);
	public List<HashMap<String, String>> getStatus(String roleType);
	public List<HashMap<String, String>> getOfficials();
	public InspectionsTrainers getTrainer(int id);
	public InspectionsHandlers getHandler(int id);
	public InspectionsUnregistered getUnregisteredPerson(int id); 
	public InspectionsSubCategories getSubCategory(int id);
	public InspectionsOfficials getInspectionsOfficial(int id);
	public InspectionsAddress getAddress(int id, String type);
	public void saveSearch(InspectionSavedSearch savedSearch);
	public List<Object> getSaveSearches(int userId);
	public void saveInspection(InspectionsInspections inspection);
	public void updateInspection(InspectionsInspections inspection);
	public void deleteInspection(int id);
}
