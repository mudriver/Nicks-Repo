package ie.turfclub.inspections.service;

import ie.turfclub.inspections.model.InspectionSavedSearch;
import ie.turfclub.inspections.model.InspectionsCategories;
import ie.turfclub.inspections.model.InspectionsHandlers;
import ie.turfclub.inspections.model.InspectionsInspections;
import ie.turfclub.inspections.model.InspectionsOfficials;

import ie.turfclub.inspections.model.InspectionsPersonSearch;
import ie.turfclub.inspections.model.InspectionsStatus;
import ie.turfclub.inspections.model.InspectionsSubCategories;
import ie.turfclub.inspections.model.InspectionsTrainers;
import ie.turfclub.inspections.model.InspectionsUnregistered;
import ie.turfclub.inspections.pojos.InspectionsAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class InspectionsServiceImpl implements InspectionsService {

	static Logger logger = LoggerFactory
			.getLogger(InspectionsServiceImpl.class);

	@Autowired
	private SessionFactory sessionFactory;

	private Session getCurrentSession() {
		return sessionFactory.getCurrentSession();
	}

	public HashMap<String, Object> getInspectionsCacheJson(String roleType){
		Query query = getCurrentSession().createQuery("from InspectionsInspections i");

		

		@SuppressWarnings("unchecked")
		List<InspectionsInspections> reports = (List<InspectionsInspections>) query
				.list();

		for (InspectionsInspections report : reports) {

			// System.out.println(report.getInspectionDate() );

			if ((report.getInspectionCategoriesId().getCatId() == 1
					|| report.getInspectionCategoriesId().getCatId() == 2
					|| report.getInspectionCategoriesId().getCatId() == 3 || report
					.getInspectionCategoriesId().getCatId() == 4)
					&& (roleType.equals("INSPECTIONS_ADMIN") || roleType
							.equals("INSPECTIONS_CEO"))) {

				report.setCanEdit(true);

			} else if ((report.getInspectionCategoriesId().getCatId() == 5)
					&& (roleType.equals("INSPECTIONS_LICENCING"))) {
				report.setCanEdit(true);
			} else {
				report.setCanEdit(false);
			}

		}
		
		HashMap<String, Object> data = new HashMap<String, Object>();
		data.put("data", reports);

		
		return data;
		
	}
	
	public HashMap<String, Object> getInspectionsJson(String roleType,
			InspectionSavedSearch savedSearch) {

		String queryStart = "SELECT i FROM InspectionsInspections i";
		String where = "";
		String orderBy = "";

		// Set the WHERE Criteria
		// check if there is a person filter
		if (savedSearch.getPersonSearch().size() > 0) {
			if (!queryStart
					.contains("LEFT JOIN i.inspectionTrainerId t LEFT JOIN i.inspectionHandlerId h LEFT JOIN i.inspectionsUnregisteredId u")) {
				queryStart += " LEFT JOIN i.inspectionTrainerId t LEFT JOIN i.inspectionHandlerId h LEFT JOIN i.inspectionsUnregisteredId u";
			}

			if (where.length() > 0) {
				where += " AND";
			} else {
				where += " WHERE";
			}

			if (savedSearch.getPersonSearch().size() > 1) {
				where += " (";
			}
			int count = 0;
			for (InspectionsPersonSearch personSearch : savedSearch
					.getPersonSearch()) {

				if (count > 0 && savedSearch.getPersonSearch().size() > 1) {
					where += " OR";
				}

				if (personSearch.getId().startsWith("T")) {
					where += " t.trainerId="
							+ personSearch.getId().replaceAll("T", "");

				} else if (personSearch.getId().startsWith("H")) {
					where += " h.ownerId="
							+ personSearch.getId().replaceAll("H", "");
				} else {
					where += " u.unregId="
							+ personSearch.getId().replaceAll("U", "");
				}
				count++;
			}
			if (savedSearch.getPersonSearch().size() > 1) {
				where += " )";
			}

		}
		// check if there is a category filter
		if (savedSearch.getCategorySearch().size() > 0) {
			if (!queryStart.contains("LEFT JOIN i.inspectionCategoriesId c")) {
				queryStart += " LEFT JOIN i.inspectionCategoriesId c";
			}
			if (where.length() > 0) {
				where += " AND";
			} else {
				where += " WHERE";
			}
			if (savedSearch.getCategorySearch().size() > 1) {
				where += " (";
			}
			int count = 0;
			for (InspectionsCategories cat : savedSearch.getCategorySearch()) {

				if (count > 0 && savedSearch.getCategorySearch().size() > 1) {
					where += " OR";
				}

				where += " c.catId=" + cat.getCatId();
				count++;

			}
			if (savedSearch.getCategorySearch().size() > 1) {
				where += " )";
			}

		}

		// check if there is a status filter
		if (savedSearch.getStatusSearch().size() > 0) {
			if (!queryStart.contains("LEFT JOIN i.inspectionStatusId s")) {
				queryStart += " LEFT JOIN i.inspectionStatusId s";
			}
			if (where.length() > 0) {
				where += " AND";
			} else {
				where += " WHERE";
			}
			if (savedSearch.getStatusSearch().size() > 1) {
				where += " (";
			}
			int count = 0;
			for (InspectionsStatus status : savedSearch.getStatusSearch()) {

				if (count > 0 && savedSearch.getStatusSearch().size() > 1) {
					where += " OR";
				}

				where += " s.statusId=" + status.getStatusId();
				count++;

			}

			if (savedSearch.getStatusSearch().size() > 1) {
				where += " )";
			}

			// add criteria to not show confidential pending if the user in not
			// admin
			if (roleType.equals("INSPECTIONS_CEO")) {
				where += " AND (s.statusId != 5)";
			} else if (roleType.equals("INSPECTIONS_LICENCING")) {
				if (!queryStart
						.contains("LEFT JOIN i.inspectionCategoriesId c")) {
					queryStart += " LEFT JOIN i.inspectionCategoriesId c";
				}
				if (where.length() > 0) {
					where += "  AND ((s.statusId != 5 AND (c.catId!=5 AND s.statusId != 1)) OR c.catId=5) ";
				} else {
					where += " WHERE ((s.statusId != 5 AND (c.catId!=5 AND s.statusId != 1)) OR c.catId=5)";
				}

			}

		}
		// otherwise add the filter for scheduled inspection which are
		// confidential
		// if the user is not inspections admin
		// This allows only inspection_admin users to see anything that is
		// pending and confidential
		else if (roleType.equals("INSPECTIONS_CEO")) {
			if (!queryStart.contains("LEFT JOIN i.inspectionStatusId s")) {
				queryStart += " LEFT JOIN i.inspectionStatusId s";
			}

			if (where.length() > 0) {
				where += "  AND (s.statusId != 5)";
			} else {
				where += " WHERE (s.statusId != 5)";
			}

		}
		// otherwise add the filter for scheduled inspection which are not
		// stable inspections
		// if the user is inspections licencing only allow them to see scheduled
		// inspections from stable inspecitons

		else if (roleType.equals("INSPECTIONS_LICENCING")) {
			if (!queryStart.contains("LEFT JOIN i.inspectionCategoriesId c")) {
				queryStart += " LEFT JOIN i.inspectionCategoriesId c";
			}
			if (!queryStart.contains("LEFT JOIN i.inspectionStatusId s")) {
				queryStart += " LEFT JOIN i.inspectionStatusId s";
			}
			if (where.length() > 0) {
				where += "  AND ((s.statusId != 5 AND (c.catId!=5 AND s.statusId != 1)) OR c.catId=5) ";
			} else {
				where += " WHERE ((s.statusId != 5 AND (c.catId!=5 AND s.statusId != 1)) OR c.catId=5)";
			}

		}

		if (where.length() > 0) {
			System.out.println("AND (i.inspectionDeleted != true)");
			where += "  AND (i.inspectionDeleted != true)";
		} else {
			System.out.println("WHERE (i.inspectionDeleted != true)");
			where += " WHERE (i.inspectionDeleted != true)";
		}
		
		// SET ORDER BY Criteria
		if (savedSearch.getOrderByFields().size() > 0) {
			System.out.println("Order By");
			int priority = 0;
			
			if (savedSearch.getOrderByFields().containsKey("Person Inspected")) {
				if (!queryStart
						.contains("LEFT JOIN i.inspectionTrainerId t LEFT JOIN i.inspectionHandlerId h LEFT JOIN i.inspectionsUnregisteredId u")) {
					queryStart += " LEFT JOIN i.inspectionTrainerId t LEFT JOIN i.inspectionHandlerId h LEFT JOIN i.inspectionsUnregisteredId u";
				}
				
				orderBy += " CASE WHEN i.inspectionHandlerId is not null THEN Concat(h.ownerSurname, ' ' ,h.ownerFirstName) WHEN i.inspectionsUnregisteredId is not null THEN Concat(u.unregSurname, ' ' ,u.unregFirstName) ELSE concat(t.trainerSurname, ' ',t.trainerFirstName) END " + savedSearch.getOrderByFields().get("Person Inspected").getFieldOrder();
				
					


				priority = savedSearch.getOrderByFields()
						.get("Person Inspected").getFieldPriority();
				System.out.println("Person: " + orderBy);
			}
			if (savedSearch.getOrderByFields().containsKey("Date")) {
				if (savedSearch.getOrderByFields().get("Date")
						.getFieldPriority() < priority) {

						orderBy = " i.inspectionDate " + savedSearch.getOrderByFields().get("Date").getFieldOrder() + "," + orderBy;
					
					
					

				} else if (savedSearch.getOrderByFields().get("Date")
						.getFieldPriority() > priority) {
					if(orderBy.length() > 0){
						orderBy += " , i.inspectionDate " + savedSearch.getOrderByFields().get("Date").getFieldOrder();
					}
					else{
						orderBy += " i.inspectionDate " + savedSearch.getOrderByFields().get("Date").getFieldOrder();
					}
					
				}

				priority = savedSearch.getOrderByFields().get("Date")
						.getFieldPriority();
			}
			orderBy = " ORDER BY" + orderBy;
			
			

		} else {
			// order by name , date desc
			if (!queryStart
					.contains("LEFT JOIN i.inspectionTrainerId t LEFT JOIN i.inspectionHandlerId h LEFT JOIN i.inspectionsUnregisteredId u")) {
				queryStart += " LEFT JOIN i.inspectionTrainerId t LEFT JOIN i.inspectionHandlerId h LEFT JOIN i.inspectionsUnregisteredId u";
			}
			 orderBy +=
			" ORDER BY CASE WHEN i.inspectionHandlerId is not null THEN Concat(h.ownerSurname, ' ' ,h.ownerFirstName) WHEN i.inspectionsUnregisteredId is not null THEN Concat(u.unregSurname, ' ' ,u.unregFirstName) ELSE concat(t.trainerSurname, ' ',t.trainerFirstName) END ASC, i.inspectionDate DESC";
			//orderBy += " ORDER BY i.inspectionDate DESC";
		}

		System.out.println(queryStart + where + orderBy);
		String queryCount = "SELECT COUNT(*) "
				+ queryStart.replace("SELECT i ", "") + where + orderBy;

		int count = ((Long) getCurrentSession().createQuery(queryCount)
				.uniqueResult()).intValue();

		Query query = getCurrentSession().createQuery(
				queryStart + where + orderBy);

		System.out.println("START: " + savedSearch.getCurrentRecordStart());
		query.setFirstResult(savedSearch.getCurrentRecordStart());
		query.setMaxResults(savedSearch.getMaxToShow());

		@SuppressWarnings("unchecked")
		List<InspectionsInspections> reports = (List<InspectionsInspections>) query
				.list();

		for (InspectionsInspections report : reports) {

			// System.out.println(report.getInspectionDate() );

			if ((report.getInspectionCategoriesId().getCatId() == 1
					|| report.getInspectionCategoriesId().getCatId() == 2
					|| report.getInspectionCategoriesId().getCatId() == 3 || report
					.getInspectionCategoriesId().getCatId() == 4)
					&& (roleType.equals("INSPECTIONS_ADMIN") || roleType
							.equals("INSPECTIONS_CEO"))) {

				report.setCanEdit(true);

			} else if ((report.getInspectionCategoriesId().getCatId() == 5)
					&& (roleType.equals("INSPECTIONS_LICENCING"))) {
				report.setCanEdit(true);
			} else {
				report.setCanEdit(false);
			}

		}

		HashMap<String, Object> data = new HashMap<String, Object>();
		data.put("data", reports);
		data.put("max", count);
		// Configure the table titles and data

		return data;
	}

	@Override
	public List<HashMap<String, String>> getPeople(String chars) {
		chars = chars.toLowerCase();
		System.out.println(chars);
		Query query = getCurrentSession()
				.createQuery(
						"SELECT CONCAT('T',trainerId),  trainerSurname, trainerFirstName, '(Trainer)' from InspectionsTrainers WHERE CONCAT(trainerSurname) Like '%"
								+ chars + "%'");
		List<Object[]> list = (List<Object[]>) query.list();
		query = getCurrentSession()
				.createQuery(
						"SELECT CONCAT('H',ownerId), ownerSurname, ownerFirstName, '(Handler)' from InspectionsHandlers WHERE CONCAT(ownerSurname) Like '%"
								+ chars + "%'");
		list.addAll((List<Object[]>) query.list());
		query = getCurrentSession()
				.createQuery(
						"SELECT CONCAT('U',unregId), unregSurname, unregFirstName, '(Unregistered)' from InspectionsUnregistered WHERE CONCAT(unregSurname) Like '%"
								+ chars + "%'");
		list.addAll((List<Object[]>) query.list());

		Collections.sort(list, SurNameCompartor);

		return convertObjectListToSelect2List(list, false);
	}

	@Override
	public InspectionsTrainers getTrainer(int id) {
		InspectionsTrainers trainer = (InspectionsTrainers) getCurrentSession()
				.get(InspectionsTrainers.class, id);
		return trainer;
	}

	@Override
	public InspectionsHandlers getHandler(int id) {
		InspectionsHandlers handler = (InspectionsHandlers) getCurrentSession()
				.get(InspectionsHandlers.class, id);
		return handler;
	}

	@Override
	public InspectionsUnregistered getUnregisteredPerson(int id) {
		InspectionsUnregistered unreg = (InspectionsUnregistered) getCurrentSession()
				.get(InspectionsUnregistered.class, id);
		return unreg;
	}

	@Override
	public void saveInspection(InspectionsInspections inspection) {
		getCurrentSession().save(convertJSONInspectionToSaveObject(inspection));

	}

	@Override
	public InspectionsStatus getStatus(int id) {
		InspectionsStatus status = (InspectionsStatus) getCurrentSession().get(
				InspectionsStatus.class, id);
		System.out.println(status.getStatusType());
		System.out.println(status.getStatusId());
		return status;
	}

	@Override
	public InspectionsCategories getCategory(int id) {
		InspectionsCategories type = (InspectionsCategories) getCurrentSession()
				.get(InspectionsCategories.class, id);
		return type;
	}

	@Override
	public InspectionsSubCategories getSubCategory(int id) {
		InspectionsSubCategories type = (InspectionsSubCategories) getCurrentSession()
				.get(InspectionsSubCategories.class, id);
		return type;
	}

	@Override
	public InspectionsInspections getInspection(int id) {
		Query query = getCurrentSession().createQuery(
				"FROM InspectionsInspections i WHERE i.inspectionId=" + id);

		@SuppressWarnings("unchecked")
		List<InspectionsInspections> list = (List<InspectionsInspections>) query
				.list();

		return list.get(0);
	}

	@Override
	public void updateInspection(InspectionsInspections inspection) {
		getCurrentSession().update(
				convertJSONInspectionToSaveObject(inspection));

	}

	@Override
	public InspectionsOfficials getInspectionsOfficial(int id) {
		InspectionsOfficials official = (InspectionsOfficials) getCurrentSession()
				.get(InspectionsOfficials.class, id);
		return official;
	}

	@Override
	public InspectionsAddress getAddress(int id, String type) {
		Query query = null;
		InspectionsAddress address = new InspectionsAddress();
		if (type.equals("T")) {
			query = getCurrentSession()
					.createQuery(
							"SELECT trainerAddress1, trainerAddress2,trainerAddress3, '', trainerStableAddress1, trainerStableAddress2, trainerStableAddress3, '' from InspectionsTrainers WHERE trainerId = '"
									+ id + "'");

		} else if (type.equals("H")) {
			query = getCurrentSession()
					.createQuery(
							"SELECT  ownerAddress1, ownerAddress2, ownerAddress3, ownerAddress4, '', '', '', '' from InspectionsHandlers WHERE ownerId = '"
									+ id + "'");

		}
		else if (type.equals("U")) {
			query = getCurrentSession()
					.createQuery(
							"SELECT  unregAddress1, unregAddress2, unregAddress3, unregAddress4, '', '', '', '' from InspectionsUnregistered WHERE unregId = '"
									+ id + "'");

		}
		else {
			address.setRegAddress1("");
			address.setRegAddress2("");
			address.setRegAddress3("");
			address.setRegAddress4("");
			address.setYardAddress1("");
			address.setYardAddress2("");
			address.setYardAddress3("");
			address.setYardAddress4("");

		}
		System.out.println("Get Address");
		if (query != null) {
			@SuppressWarnings("unchecked")
			List<Object[]> list = (List<Object[]>) query.list();
			for (Object[] obj : list) {
				 System.out.println(obj[0].toString());
				 System.out.println(obj[1].toString());
				 System.out.println(obj[2].toString());
				 System.out.println(obj[3].toString());
				 
				address.setRegAddress1(obj[0].toString());
				address.setRegAddress2(obj[1].toString());
				address.setRegAddress3(obj[2].toString());
				address.setRegAddress4(obj[3].toString());
				address.setYardAddress1(obj[4].toString());
				address.setYardAddress2(obj[5].toString());
				address.setYardAddress3(obj[6].toString());
				address.setYardAddress4(obj[7].toString());
			}
		}

		return address;
	}

	public static Comparator<Object[]> SurNameCompartor = new Comparator<Object[]>() {

		public int compare(Object[] s1, Object[] s2) {
			String surName1 = s1[1].toString().toUpperCase() + " "
					+ s1[2].toString().toUpperCase();
			String surName2 = s2[1].toString().toUpperCase() + " "
					+ s2[2].toString().toUpperCase();

			// ascending order
			return surName1.compareTo(surName2);

			// descending order
			// return StudentName2.compareTo(StudentName1);
		}
	};

	// converts a list of objects into a select to list of hashmaps with keys id
	// and value
	private ArrayList<HashMap<String, String>> convertObjectListToSelect2List(
			List<Object[]> objectList, boolean linkedValue) {
		ArrayList<HashMap<String, String>> convertedList = new ArrayList<>();
		HashMap<String, String> map;
		for (Object[] objArray : objectList) {
			String id = "";
			String value = "";
			String linkedId = "";
			map = new HashMap<>();
			for (int i = 0; i < objArray.length; i++) {
				if (i == 0) {
					id = objArray[i].toString();
				} else if (i == (objArray.length - 1) && linkedValue) {
					linkedId = linkedId.concat(objArray[i].toString());
				} else {
					//System.out.println(objArray[i].toString());
					value = value.concat(objArray[i].toString() + " ");

				}

			}
			//System.out.println("Array:" + id + " " + value + " " + "Link"+ linkedId);
			map.put("id", id);
			map.put("value", value);
			map.put("linkedId", linkedId);
			convertedList.add(map);
		}
		return convertedList;
	}

	private InspectionsInspections convertJSONInspectionToSaveObject(
			InspectionsInspections inspection) {

		// check the person selected and convert to trainer, handler or
		// unregistered
		String id = inspection.getInspectionsPersonId().getId();
		if (id.startsWith("T")) {

			inspection.setInspectionTrainerId(this.getTrainer(Integer
					.parseInt(id.replaceAll("T", ""))));

		} else if (id.startsWith("H")) {
			inspection.setInspectionHandlerId(this.getHandler(Integer
					.parseInt(id.replaceAll("H", ""))));
		} else {
			inspection.setInspectionsUnregisteredId(this
					.getUnregisteredPerson(Integer.parseInt(id.replaceAll("U",
							""))));
		}

		// check the status
		inspection.setInspectionStatusId(this.getStatus(inspection
				.getInspectionStatusId().getStatusId()));
		// check the inspection category
		inspection.setInspectionCategoriesId(this.getCategory(inspection
				.getInspectionCategoriesId().getCatId()));
		// check the inspection subcategory
		inspection.setInspectionSubCategoriesId(this.getSubCategory(inspection
				.getInspectionSubCategoriesId().getSubCatId()));
		// check the officials
		HashSet<InspectionsOfficials> officials = new HashSet<InspectionsOfficials>();
		for (InspectionsOfficials official : inspection
				.getInspectionsOfficials()) {
			officials
					.add(this.getInspectionsOfficial(official.getOfficialsId()));
		}
		inspection.setInspectionsOfficials(officials);

		
		return inspection;
	}

	@Override
	public List<HashMap<String, String>> getCategories() {
		String queryString = "select c.catId, c.catName from InspectionsCategories c";
		String where = "";
		String orderBy = " ORDER BY c.catName";

		Query query = getCurrentSession().createQuery(
				queryString + where + orderBy);

		List<Object[]> list = (List<Object[]>) query.list();

		return convertObjectListToSelect2List(list, false);
	}

	@Override
	public List<HashMap<String, String>> getSubCategories() {
		String queryString = "select s.subCatId, s.subCatName, c.catId from InspectionsSubCategories s LEFT JOIN s.subCatCategoryId c";
		String where = "";
		String orderBy = " ORDER BY s.subCatName";

		Query query = getCurrentSession().createQuery(
				queryString + where + orderBy);

		List<Object[]> list = (List<Object[]>) query.list();

		return convertObjectListToSelect2List(list, true);
	}

	@Override
	public List<HashMap<String, String>> getCategories(String roleType) {
		String queryString = "select c.catId, c.catName from InspectionsCategories c";
		String where = "";
		String orderBy = " ORDER BY c.catName";
		if (roleType.equals("INSPECTIONS_ADMIN")
				|| roleType.equals("INSPECTIONS_CEO")) {
			where += " WHERE c.catId != 5";
		} else if (roleType.equals("INSPECTIONS_LICENCING")) {
			where += " WHERE c.catId != 1 AND c.catId != 2 AND c.catId != 3 AND c.catId != 4";
		}

		Query query = getCurrentSession().createQuery(
				queryString + where + orderBy);

		List<Object[]> list = (List<Object[]>) query.list();

		return convertObjectListToSelect2List(list, false);
	}

	@Override
	public List<HashMap<String, String>> getSubCategories(String roleType) {
		String queryString = "select s.subCatId, s.subCatName, c.catId from InspectionsSubCategories s LEFT JOIN s.subCatCategoryId c";
		String where = "";
		String orderBy = " ORDER BY s.subCatName";
		if (roleType.equals("INSPECTIONS_ADMIN")
				|| roleType.equals("INSPECTIONS_CEO")) {
			where += " WHERE c.catId != 5";
		} else if (roleType.equals("INSPECTIONS_LICENCING")) {
			where += " WHERE c.catId != 1 AND c.catId != 2 AND c.catId != 3 AND c.catId != 4";
		}

		Query query = getCurrentSession().createQuery(
				queryString + where + orderBy);

		List<Object[]> list = (List<Object[]>) query.list();

		return convertObjectListToSelect2List(list, true);
	}

	@Override
	public void saveSearch(InspectionSavedSearch savedSearch) {

		for (InspectionsPersonSearch personSearch : savedSearch
				.getPersonSearch()) {
			personSearch.setSSSearchId(savedSearch);
			System.out.println(personSearch.getId());
		}
		HashSet<InspectionsCategories> categories = new HashSet<InspectionsCategories>();
		for (InspectionsCategories category : savedSearch.getCategorySearch()) {
			categories.add(this.getCategory(category.getCatId()));
		}
		savedSearch.setCategorySearch(categories);
		HashSet<InspectionsStatus> statuses = new HashSet<InspectionsStatus>();
		for (InspectionsStatus status : savedSearch.getStatusSearch()) {
			statuses.add(this.getStatus(status.getStatusId()));
		}
		savedSearch.setStatusSearch(statuses);
		getCurrentSession().save(savedSearch);

	}

	@Override
	public List<HashMap<String, String>> getStatus(String roleType) {
		String queryString = "select s.statusId, s.statusType from InspectionsStatus s";
		String where = "";
		String orderBy = " ORDER BY s.statusType";
		if (!roleType.equals("INSPECTIONS_ADMIN")) {
			where += " WHERE s.statusId != 5";
		}

		Query query = getCurrentSession().createQuery(
				queryString + where + orderBy);

		List<Object[]> list = (List<Object[]>) query.list();

		return convertObjectListToSelect2List(list, false);
	}

	@Override
	public HashMap<String, String> getPersonAddress(String id) {
		String queryString = "";
		if (id.startsWith("T")) {
			id = id.replaceAll("T", "");
			queryString = "SELECT trainerAddress1, trainerAddress2, trainerAddress3 from InspectionsTrainers WHERE trainerId="
					+ id;
		} else if (id.startsWith("H")) {
			id = id.replaceAll("H", "");
			queryString = "SELECT ownerAddress1, ownerAddress2, ownerAddress3, ownerAddress4 from InspectionsHandlers WHERE ownerId="
					+ id;
		} else {
			id = id.replaceAll("U", "");
			queryString = "SELECT unregAddress1, unregAddress2, unregAddress3, unregAddress4 from InspectionsUnregistered WHERE unregId="
					+ id;
		}

		Query query = getCurrentSession().createQuery(queryString);
		List<Object[]> list = (List<Object[]>) query.list();
		HashMap<String, String> address = new HashMap<String, String>();
		for (Object[] obj : list) {
			if (obj.length == 3) {
				address.put("address4", "");
				address.put("address1", obj[0].toString());
				address.put("address2", obj[1].toString());
				address.put("address3", obj[2].toString());
			} else {
				address.put("address1", obj[0].toString());
				address.put("address2", obj[1].toString());
				address.put("address3", obj[2].toString());
				address.put("address4", obj[3].toString());
			}

		}
		if (list.isEmpty()) {
			System.out.println("Empty List");
		}

		return address;
	}

	@Override
	public HashMap<String, String> getPersonYardAddress(String id) {
		String queryString = "";
		HashMap<String, String> address;
		if (id.startsWith("T")) {
			id = id.replaceAll("T", "");
			queryString = "SELECT trainerStableAddress1, trainerStableAddress2, trainerStableAddress3 from InspectionsTrainers WHERE trainerId="
					+ id;
			Query query = getCurrentSession().createQuery(queryString);
			List<Object[]> list = (List<Object[]>) query.list();
			address = new HashMap<String, String>();
			for (Object[] obj : list) {
				if (obj.length == 3 && obj[0] != null) {
					address.put("address4", "");
					address.put("address1", obj[0].toString());
					address.put("address2", obj[1].toString());
					address.put("address3", obj[2].toString());
				} else {
					address = new HashMap<String, String>();
					address.put("noData", "No yard address available!");
				}

			}
		}

		else {
			address = new HashMap<String, String>();
			address.put("noData", "No yard address available!");
		}

		return address;
	}

	@Override
	public List<HashMap<String, String>> getOfficials() {
		Query query = getCurrentSession()
				.createQuery(
						"select o.officialsId, o.officialsSurname, o.officialsFirstName from InspectionsOfficials o WHERE o.officialShowOnProgram=true ORDER BY o.officialsSurname, o.officialsFirstName");

		List<Object[]> list = (List<Object[]>) query.list();

		return convertObjectListToSelect2List(list, false);
	}



	@Override
	public List<Object> getSaveSearches(int userId) {
		Query query = getCurrentSession().createQuery(
				"from InspectionSavedSearch s where s.savedSearchUserId="
						+ userId + " OR s.savedSearchUserId=0");

		List<Object> list =  query
				.list();
		return list;
	}

	@Override
	public void deleteInspection(int id) {
		InspectionsInspections inspection = this.getInspection(id);
		inspection.setInspectionDeleted(true);
		getCurrentSession().saveOrUpdate(inspection);
	}

}
