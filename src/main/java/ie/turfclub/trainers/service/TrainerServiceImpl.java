package ie.turfclub.trainers.service;

import ie.turfclub.common.enums.RoleEnum;
import ie.turfclub.person.model.Person;
import ie.turfclub.person.service.PersonService;
import ie.turfclub.trainers.model.TeAuthorisedReps;
import ie.turfclub.trainers.model.TeEmployeeTrainerVerified;
import ie.turfclub.trainers.model.TeEmployees;
import ie.turfclub.trainers.model.TeEmployentHistory;
import ie.turfclub.trainers.model.TeFile;
import ie.turfclub.trainers.model.TeTrainers;
import ie.turfclub.trainers.model.TeTrainers.VerifiedStatus;
import ie.turfclub.trainers.model.savedSearches.TeAuthRepsSavedSearches;
import ie.turfclub.trainers.model.savedSearches.TeBooleanSavedSearch;
import ie.turfclub.trainers.model.savedSearches.TeEmployeesPensionSavedSearch;
import ie.turfclub.trainers.model.savedSearches.TeEmployeesSavedSearch;
import ie.turfclub.trainers.model.savedSearches.TeEnumSavedSearch;
import ie.turfclub.trainers.model.savedSearches.TeOrderByFields;
import ie.turfclub.trainers.model.savedSearches.TeTrainersPensionSavedSearch;
import ie.turfclub.trainers.model.savedSearches.TeTrainersSavedSearch;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.annotation.Resource;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;
import org.hibernate.sql.JoinType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;

@PropertySource("classpath:ie/turfclub/trainers/resources/config/config.properties")
@Service
@Transactional
public class TrainerServiceImpl implements TrainersService {

	@Autowired
	private SessionFactory sessionFactory;
	@Resource
	private Environment env;
	@Autowired
	private MessageSource messageSource;
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private PersonService personService;
	
	private String PROPERTY_TAX_YEAR;
	private Date minDateFrom;
	private Date maxDateFrom;

	public void initialize() {
		PROPERTY_TAX_YEAR = env.getRequiredProperty("tax_year");
		System.out.println("Create TrainerService");
		System.out.println(PROPERTY_TAX_YEAR);
		try {
			SimpleDateFormat sqlFormat = new SimpleDateFormat("yyyy-MM-dd");

			minDateFrom = sqlFormat.parse((Integer.parseInt(PROPERTY_TAX_YEAR)
					 -1)+ "-12-31");
			
			maxDateFrom = sqlFormat.parse((Integer.parseInt(PROPERTY_TAX_YEAR)
					+1) + "-12-31");
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private Session getCurrentSession() {
		return sessionFactory.getCurrentSession();
	}

	@Override
	public HashMap<String, Object> getTrainers(TeTrainersSavedSearch savedSearch) {
		// TODO Auto-generated method stub

		Criteria criteria = getCurrentSession()
				.createCriteria(TeTrainers.class)
				.addOrder(Order.asc("trainerFullName"))
				.setFirstResult(savedSearch.getCurrentRecordStart())
				.setMaxResults(savedSearch.getMaxToShow());

		Criteria criteriaCount = getCurrentSession().createCriteria(
				TeTrainers.class);
		criteriaCount.setProjection(Projections.rowCount());
		Long count = (Long) criteriaCount.uniqueResult();

		@SuppressWarnings("unchecked")
		List<TeTrainers> trainers = (List<TeTrainers>) criteria.list();

		HashMap<String, Object> data = new HashMap<String, Object>();
		data.put("data", trainers);
		data.put("max", count);
		for (TeTrainers trainer : trainers) {
			trainer.setCanEdit(true);
			System.out.println(trainer.getTrainerAccountNo());
		}

		return data;
	}

	@Override
	public HashMap<String, Object> getTrainersPension(
			TeTrainersPensionSavedSearch savedSearch) {

		DetachedCriteria criteria = DetachedCriteria.forClass(TeTrainers.class);

		// if the search contains trainers add each to the search
		if (savedSearch.getTrainersSearch().size() > 0) {

			Disjunction disjunction = Restrictions.disjunction();
			for (TeTrainers trainer : savedSearch.getTrainersSearch()) {
				System.out.println("Trainer" + trainer.getTrainerId());
				disjunction.add(Restrictions.and(Restrictions.eq("trainerId",
						trainer.getTrainerId())));

			}
			criteria.add(disjunction);
		}

		if (savedSearch.getReturnCompleteSearch() != null
				&& savedSearch.getReturnCompleteSearch().isInUse()) {
			System.out.println("saved search complete"
					+ savedSearch.getReturnCompleteSearch());
			criteria.add(Restrictions.eq("trainerReturnComplete", savedSearch
					.getReturnCompleteSearch().isSsBoolean()));
		}

		if (savedSearch.getVerified() != null
				&& savedSearch.getVerified().isInUse()) {
			System.out.println(savedSearch.getVerified().getSsEnum());
			VerifiedStatus status = null;
			switch (savedSearch.getVerified().getSsEnum()) {
			case "PENDING":
				status = VerifiedStatus.PENDING;
				break;
			case "NOTVERIFIED":
				status = VerifiedStatus.NOTVERIFIED;
				break;
			case "VERIFIED":
				status = VerifiedStatus.VERIFIED;
				break;
			}
			criteria.add(Restrictions.eq("trainerVerifiedStatus", status));
		}

		if (savedSearch.getDocumentsAttached() != null
				&& savedSearch.getDocumentsAttached().isInUse()) {

			if (savedSearch.getDocumentsAttached().isSsBoolean()) {
				criteria.createAlias("trainerFile", "file",
						JoinType.INNER_JOIN);
				criteria.add(Restrictions.isNotNull("file.id"));

			} else {
				criteria.createAlias("trainerFile", "file",
						JoinType.LEFT_OUTER_JOIN);
				criteria.add(Restrictions.isNull("file.id"));
			}

		}

		// Batch No's for currently licenced trainers
		criteria.add(Restrictions.between("trainerBatchNo", 500, 599));
		criteria.setProjection(Projections.distinct(Projections
				.property("trainerId")));

		Criteria outer = getCurrentSession().createCriteria(TeTrainers.class,
				"trainer");
		// inner join

		outer.add(Subqueries.propertyIn("trainer.trainerId", criteria));
		// by
		// default

		outer.setProjection(Projections.rowCount());
		Long count = (Long) outer.uniqueResult();
		outer.setProjection(null);
		// SET ORDER BY Criteria
		if (savedSearch.getOrderByFields().size() > 0) {
			// using saved search

			SortedSet<Map.Entry<String, TeOrderByFields>> sortedEntries = orderSorted(savedSearch
					.getOrderByFields());

			Iterator<Map.Entry<String, TeOrderByFields>> sortedItr = sortedEntries
					.iterator();
			while (sortedItr.hasNext()) {
				Map.Entry<String, TeOrderByFields> orderBy = sortedItr.next();
				System.out.println("Order by:" + orderBy.getKey() + " "
						+ orderBy.getValue().getFieldOrder());
				switch (orderBy.getKey()) {
				case "Return Complete":
					if (orderBy.getValue().getFieldOrder().equals("DESC")) {
						outer.addOrder(Order.desc("trainerReturnComplete"));
					} else {

						outer.addOrder(Order.asc("trainerReturnComplete"));
					}
					break;
				case "Name":
					System.out.println("NAME SORT");
					if (orderBy.getValue().getFieldOrder().equals("DESC")) {
						outer.addOrder(Order.desc("trainerFullName"));
					} else {
						outer.addOrder(Order.asc("trainerFullName"));
					}
					break;
				case "Verfied":
					if (orderBy.getValue().getFieldOrder().equals("DESC")) {
						outer.addOrder(Order.desc("trainerVerified"));
					} else {
						outer.addOrder(Order.asc("trainerVerified"));
					}
					break;
				case "Date Completed":
					if (orderBy.getValue().getFieldOrder().equals("DESC")) {
						outer.addOrder(Order.desc("trainerDateCompleted"));
					} else {
						outer.addOrder(Order.asc("trainerDateCompleted"));
					}
					break;
				}

			}

		}
		else{
			outer.addOrder(Order.asc("trainerFullName"));
		}
		outer.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		outer.setFirstResult(savedSearch.getCurrentRecordStart())

		.setMaxResults(savedSearch.getMaxToShow());
		System.out.println(savedSearch.getCurrentRecordStart() + " "
				+ savedSearch.getMaxToShow());
		@SuppressWarnings("unchecked")
		List<TeTrainers> trainers = (List<TeTrainers>) outer.list();

		for (TeTrainers trainer : trainers) {

			trainer.setCanEdit(true);

			if (trainer.getTrainerFile().size() > 0) {
				trainer.setHasDocuments(true);
			}

		}

		System.out.println(count);
		HashMap<String, Object> data = new HashMap<String, Object>();

		data.put("data", trainers);
		data.put("max", count);

		return data;
	}

	@Override
	public List<HashMap<String, String>> getTrainers(String chars) {
		chars = chars.toLowerCase();
		System.out.println(chars);
		Query query = getCurrentSession()
				.createQuery(
						"SELECT trainerId,trainerFullName from TeTrainers WHERE trainerFullName Like '%"
								+ chars + "%'");
		List<Object[]> list = (List<Object[]>) query.list();

		return convertObjectListToSelect2List(list, false);
	}

	@Override
	public TeTrainers getTrainer(Integer id) {
		TeTrainers trainer = (TeTrainers) getCurrentSession().get(
				TeTrainers.class, id);
		return trainer;
	}

	@SuppressWarnings("unchecked")
	@Override
	public HashMap<String, Object> getEmployees(Integer trainerId,
			TeEmployeesSavedSearch savedSearch) {
		List<TeEmployees> employees = null;
		DetachedCriteria c = DetachedCriteria.forClass(TeEmployees.class,
				"employee");

		// if the search is specific to a single trainer only get employees
		// associated
		if (trainerId != 0) {

			c.createAlias("employee.teEmployentHistories",
					"teEmployentHistories"); // inner join
			// by
			// default
			c.createAlias("teEmployentHistories.teTrainers", "teTrainers");

			c.add(Restrictions.eq("teTrainers.trainerId", trainerId));

		}

		if (savedSearch.getEmployeesSearch().size() > 0) {

			Disjunction disjunction = Restrictions.disjunction();
			for (TeEmployees employee : savedSearch.getEmployeesSearch()) {
				System.out.println(employee.getEmployeesEmployeeId());
				disjunction.add(Restrictions.and(Restrictions.eq(
						"employee.employeesEmployeeId",
						employee.getEmployeesEmployeeId())));

			}
			c.add(disjunction);
		}

		// SET ORDER BY Criteria
		if (savedSearch.getOrderByFields().size() > 0) {
			// using saved search

			SortedSet<Map.Entry<String, TeOrderByFields>> sortedEntries = orderSorted(savedSearch
					.getOrderByFields());

			Iterator<Map.Entry<String, TeOrderByFields>> sortedItr = sortedEntries
					.iterator();
			while (sortedItr.hasNext()) {
				Map.Entry<String, TeOrderByFields> orderBy = sortedItr.next();
				switch (orderBy.getKey()) {
				case "Name":
					if (orderBy.getValue().getFieldOrder().equals("DESC")) {
						c.addOrder(Order.desc("employee.employeesFullName"));
					} else {
						c.addOrder(Order.asc("employee.employeesFullName"));
					}
					break;
				case "Address":
					if (orderBy.getValue().getFieldOrder().equals("DESC")) {
						c.addOrder(Order.desc("employee.employeesAddress1"));
					} else {
						c.addOrder(Order.asc("employee.employeesAddress1"));
					}
					break;
				}

			}

		} else {
			// otherwise order by name asc
			c.addOrder(Order.asc("employee.employeesFullName"));
		}

		// c.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		c.setProjection(Projections.distinct(Projections.id()));

		Criteria outer = getCurrentSession().createCriteria(TeEmployees.class,
				"employ");
		outer.add(Subqueries.propertyIn("employ.employeesEmployeeId", c));
		outer.setFirstResult(savedSearch.getCurrentRecordStart());
		outer.setMaxResults(savedSearch.getMaxToShow());

		employees = (List<TeEmployees>) outer.list();
		for (TeEmployees employ : employees) {
			System.out.println(employ.getEmployeesFullName());
		}
		outer.setProjection(Projections
				.countDistinct("employ.employeesEmployeeId"));
		Long count = (Long) outer.uniqueResult();

		HashMap<String, Object> data = new HashMap<String, Object>();
		data.put("data", employees);
		data.put("max", count);
		/*
		 * for(TeEmployees employee : employees){ employee.setCanEdit(true);
		 * 
		 * }
		 */

		return data;
	}

	@Override
	public List<HashMap<String, String>> getEmployees(String chars) {
		chars = chars.toLowerCase();
		System.out.println(chars);
		Query query = getCurrentSession()
				.createQuery(
						"SELECT employeesEmployeeId, employeesFullName from TeEmployees WHERE employeesFullName Like '%"
								+ chars + "%'");
		List<Object[]> list = (List<Object[]>) query.list();

		return convertObjectListToSelect2List(list, false);
	}

	@Override
	public TeEmployees getEmployee(Integer id) {
		return (TeEmployees) getCurrentSession().get(TeEmployees.class, id);
	}

	@Override
	public HashMap<String, Object> getAuthorisedReps(Integer trainerId,
			TeAuthRepsSavedSearches savedSearch) {

		System.out.println("Get Reps" + trainerId);
		Criteria c = getCurrentSession().createCriteria(TeAuthorisedReps.class,
				"reps");
		c.createAlias("reps.authrepsTrainerId", "teTrainers");
		if (trainerId != 0) {

			c.add(Restrictions.eq("teTrainers.trainerId", trainerId));

		}

		// SET ORDER BY Criteria
		if (savedSearch.getOrderByFields().size() > 0) {
			// using saved search

			SortedSet<Map.Entry<String, TeOrderByFields>> sortedEntries = orderSorted(savedSearch
					.getOrderByFields());

			Iterator<Map.Entry<String, TeOrderByFields>> sortedItr = sortedEntries
					.iterator();
			while (sortedItr.hasNext()) {
				Map.Entry<String, TeOrderByFields> orderBy = sortedItr.next();
				switch (orderBy.getKey()) {
				case "Name":
					if (orderBy.getValue().getFieldOrder().equals("DESC")) {
						c.addOrder(Order.desc("employee.employeesFullName"));
					} else {
						c.addOrder(Order.asc("employee.employeesFullName"));
					}
					break;
				case "Address":
					if (orderBy.getValue().getFieldOrder().equals("DESC")) {
						c.addOrder(Order.desc("employee.employeesAddress1"));
					} else {
						c.addOrder(Order.asc("employee.employeesAddress1"));
					}
					break;
				}

			}

		} else {
			// otherwise order by name asc
			c.addOrder(Order.asc("reps.authrepsName"));
		}

		c.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		c.setFirstResult(savedSearch.getCurrentRecordStart());
		c.setMaxResults(savedSearch.getMaxToShow());

		List<TeAuthorisedReps> reps = (List<TeAuthorisedReps>) c.list();

		c.setProjection(Projections.countDistinct("reps.authrepsId"));
		Long count = (Long) c.uniqueResult();

		HashMap<String, Object> data = new HashMap<String, Object>();
		data.put("data", reps);
		data.put("max", count);
		/*
		 * for(TeEmployees employee : employees){ employee.setCanEdit(true);
		 * 
		 * }
		 */

		return data;
	}

	@Override
	public List<HashMap<String, String>> getAuthorisedReps(String chars) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TeAuthorisedReps getAuthorisedRep(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Object> getAuthRepSavedSearches(int userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void saveAuthRepSavedSearches(TeAuthRepsSavedSearches savedsearch) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<Object> getEmployeesSavedSearches(int userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void saveEmployeeSavedSearches(TeEmployeesSavedSearch savedsearch) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<Object> getTrainersSavedSearches(int userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void saveTrainerSavedSearches(TeTrainersSavedSearch savedsearch) {

		HashSet<TeTrainers> trainers = new HashSet<TeTrainers>();
		for (TeTrainers trainer : savedsearch.getTrainersSearch()) {
			this.getTrainer(trainer.getTrainerId());
		}
		savedsearch.setTrainersSearch(trainers);

		getCurrentSession().save(savedsearch);

	}

	@Override
	@Transactional(readOnly = true)
	public List<Object> getTrainersPensionSavedSearches(int userId) {
		Session session = getCurrentSession();
		System.out.println("Savedsearch" + userId);
		Query query = session
				.createQuery("from TeTrainersPensionSavedSearch s where s.savedSearchUserId="
						+ userId);

		List<Object> list = query.list();
		
		
		System.out.println("Savedsearch" + list.size());
		return list;
	}

	@Override
	public void saveTrainerPensionSavedSearches(
			TeTrainersPensionSavedSearch savedsearch) {
		HashSet<TeTrainers> trainers = new HashSet<TeTrainers>();
		for (TeTrainers trainer : savedsearch.getTrainersSearch()) {
			this.getTrainer(trainer.getTrainerId());
		}

		if (savedsearch.getVerified().isInUse()) {
			TeEnumSavedSearch enumSearch = savedsearch.getVerified();
			enumSearch.setSavedSearch(savedsearch);

		} else {
			savedsearch.setVerified(null);
		}

		if (savedsearch.getReturnCompleteSearch().isInUse()) {
			TeBooleanSavedSearch savedBoolean = savedsearch
					.getReturnCompleteSearch();
			savedBoolean.setSavedSearch(savedsearch);

		} else {
			savedsearch.setReturnCompleteSearch(null);
		}

		if (savedsearch.getDocumentsAttached().isInUse()) {
			TeBooleanSavedSearch savedBoolean = savedsearch
					.getDocumentsAttached();
			savedBoolean.setSavedSearch(savedsearch);

		} else {
			savedsearch.setDocumentsAttached(null);
		}

		savedsearch.setTrainersSearch(trainers);
		getCurrentSession().save(savedsearch);

	}

	@Override
	public List<TeFile> getTrainersPensionFileNames(Integer trainerId) {
		Criteria c = getCurrentSession().createCriteria(TeFile.class, "file");
		c.createAlias("file.fileUserId", "trainer"); // inner join by default
		c.add(Restrictions.eq("trainer.trainerId", trainerId));
		List<TeFile> files = c.list();

		return files;
	}

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
					System.out.println(objArray[i].toString());
					value = value.concat(objArray[i].toString() + " ");

				}

			}
			System.out.println("Array:" + id + " " + value + " " + "Link"
					+ linkedId);
			map.put("id", id);
			map.put("value", value);
			map.put("linkedId", linkedId);
			convertedList.add(map);
		}
		return convertedList;
	}

	private static <K, V extends Comparable<? super V>> SortedSet<Map.Entry<String, TeOrderByFields>> orderSorted(
			Map<String, TeOrderByFields> map) {
		SortedSet<Map.Entry<String, TeOrderByFields>> sortedEntries = new TreeSet<Map.Entry<String, TeOrderByFields>>(
				new Comparator<Map.Entry<String, TeOrderByFields>>() {
					@Override
					public int compare(Map.Entry<String, TeOrderByFields> e1,
							Map.Entry<String, TeOrderByFields> e2) {

						return e1.getValue().getFieldPriority()
								.compareTo(e2.getValue().getFieldPriority());
					}
				});
		sortedEntries.addAll(map.entrySet());
		return sortedEntries;
	}

	@Override
	@Transactional(readOnly = true)
	public HashMap<String, Object> getEmployeesPension(Integer trainerId,
			TeEmployeesPensionSavedSearch savedSearch) {
		List<TeEmployees> employees = null;
		// only get earnings and pps numbers from previous year
		Date date = new Date(); // your date
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		//CHANGE EARNINGS YEAR HERE
		//comment out line below to change to current year  
		cal.add(Calendar.YEAR, -1);
		int year = cal.get(Calendar.YEAR);
		Date startDate = null;
		Date endDate = null;
		try {
			startDate = new SimpleDateFormat("yyyy-MM-dd").parse((year-2)
					+ "-12-31");
			endDate = new SimpleDateFormat("yyyy-MM-dd").parse(year + "-01-01");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		DetachedCriteria c = DetachedCriteria.forClass(TeEmployees.class,
				"employee");
		

		// if the search is specific to a single trainer only get employees
		// associated
		if (trainerId != 0) {

			c.createAlias("employee.teEmployentHistories",
					"teEmployentHistories", JoinType.LEFT_OUTER_JOIN); // inner
																		// join
			// by
			// default
			c.createAlias("teEmployentHistories.teTrainers", "teTrainers");

			c.add(Restrictions.eq("teTrainers.trainerId", trainerId));
			c.add(Restrictions.ge("teEmployentHistories.ehDateFrom", startDate));
		}

		/*
		 * if (savedSearch.getEmployeesSearch().size() > 0) {
		 * 
		 * Disjunction disjunction = Restrictions.disjunction(); for
		 * (TeEmployees employee : savedSearch.getEmployeesSearch()) {
		 * System.out.println(employee.getEmployeesEmployeeId());
		 * disjunction.add(Restrictions.and(Restrictions.eq(
		 * "employee.employeesEmployeeId", employee.getEmployeesEmployeeId())));
		 * 
		 * } c.add(disjunction); }
		 */
		// SET ORDER BY Criteria
		if (savedSearch.getOrderByFields().size() > 0) {
			// using saved search

			SortedSet<Map.Entry<String, TeOrderByFields>> sortedEntries = orderSorted(savedSearch
					.getOrderByFields());

			Iterator<Map.Entry<String, TeOrderByFields>> sortedItr = sortedEntries
					.iterator();
			while (sortedItr.hasNext()) {
				Map.Entry<String, TeOrderByFields> orderBy = sortedItr.next();
				switch (orderBy.getKey()) {
				case "Name":
					if (orderBy.getValue().getFieldOrder().equals("DESC")) {
						c.addOrder(Order.desc("employee.employeesFullName"));
					} else {
						c.addOrder(Order.asc("employee.employeesFullName"));
					}
					break;

				}

			}

		} else {
			// otherwise order by name asc
			c.addOrder(Order.asc("employee.employeesFullName"));
		}

		
		c.setProjection(Projections.distinct(Projections.id()));

		Criteria outer = getCurrentSession().createCriteria(TeEmployees.class,
				"employ" );
		
		outer.add(Subqueries.propertyIn("employ.employeesEmployeeId", c));
		
		outer.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		
		
		
		
		outer.setProjection(Projections
				.countDistinct("employ.employeesEmployeeId"));
		Long count = (Long) outer.uniqueResult();
		outer.setProjection(null);
		outer.setFirstResult(savedSearch.getCurrentRecordStart());
		outer.setMaxResults(savedSearch.getMaxToShow());
		outer.addOrder(Order.asc("employ.employeesFullName"));
		employees = (List<TeEmployees>) outer.list();
		
		System.out.println("SEARCH START " + savedSearch.getCurrentRecordStart());
		System.out.println("MAX " + count);
		// c.setMaxResults(savedSearch.getMaxToShow());



		
		for (TeEmployees employ : employees) {

			//System.out.println("GET HISTORIES " + minDateFrom + " " + maxDateFrom);
			List<TeEmployentHistory> histories  = this.getEmploymentHistories(trainerId, employ.getEmployeesEmployeeId(), minDateFrom, maxDateFrom);
			if(histories.size() > 0){
				//System.out.println(histories.size());
				
				for (TeEmployentHistory history : histories) {

					//System.out.println(history.getEhDateFrom());
						if (history.getEhEarnings() != null) {
							employ.setEmployeesEarnings("�"
									+ String.valueOf(history.getEhEarnings()) );
							if(employ.getEmployeesEarnings() .endsWith(".0")){
								employ.setEmployeesEarnings(employ.getEmployeesEarnings().replaceAll("\\.0", "\\.00"));
							}
							else if(employ.getEmployeesEarnings() .endsWith(".*")){
								employ.setEmployeesEarnings(employ.getEmployeesEarnings() + "0");
							}
							if(history.getEhPpsNumber() != null){
								employ.setEmployeesPps(history.getEhPpsNumber());
							}
							
							
						} else {
							if(employ.getEmployeesEarnings() == null){
								employ.setEmployeesEarnings("�" + "0.00");
							}
							
							if(history.getEhPpsNumber() != null && employ.getEmployeesPps() == null){
								employ.setEmployeesPps(history.getEhPpsNumber());
							}
							else if(employ.getEmployeesPps() == null){
								employ.setEmployeesPps("N/A");
							}
							
							
						}

						//System.out.println(history.getEhDateFrom() + "    "+ startDate + "   " + endDate);
						if(history.getEhDateFrom().after(startDate) && history.getEhDateFrom().before(endDate)){
							employ.setEmployeeWorkedWithTrainerInTaxYear(true);
						}
						if(history.isEhVerified()){
							employ.setEmployeeVerified(true);
						}
						
					

				}
			}
			else{

				outer = getCurrentSession().createCriteria(
						TeEmployeeTrainerVerified.class, "verified");
				outer.createAlias("verified.trainerId", "trainer");
				outer.createAlias("verified.employeeId", "employee");
				outer.add(Restrictions.eq("trainer.trainerId", trainerId));
				outer.add(Restrictions.eq("employee.employeesEmployeeId",
						employ.getEmployeesEmployeeId()));
				List<TeEmployeeTrainerVerified> verifiedEmployees = (List<TeEmployeeTrainerVerified>) outer
						.list();
				if (verifiedEmployees.size() > 0) {
					for (TeEmployeeTrainerVerified verified : verifiedEmployees) {
						if (verified.isVerified()) {
							employ.setEmployeeVerified(verified.isVerified());
						}

					}
				} else {
					employ.setEmployeeVerified(false);
				}

				employ.setEmployeeWorkedWithTrainerInTaxYear(false);
				employ.setEmployeesEarnings("N/A");
				employ.setEmployeesPps("N/A");
			}

		}

		/*
		if(employees.isEmpty() || employees == null){
			TeEmployees employee = new TeEmployees();
			System.out.println("This trainer has no employees");
			employee.setEmployeesFirstname("This trainer has no employees");
			employee.setEmployeeVerified(false);
			employees.add(employee);
		}*/
		
		
		System.out.println("Return Employees");
		//Long count = (long) employees.size();

		HashMap<String, Object> data = new HashMap<String, Object>();
		data.put("data", employees);
		data.put("max", count);
		/*
		 * for(TeEmployees employee : employees){ employee.setCanEdit(true);
		 * 
		 * }
		 */
		System.out.println("Return Employees");
		return data;
	}

	@Override
	public void updateTrainerComments(TeTrainers trainerWithComments) {
		TeTrainers trainer = this
				.getTrainer(trainerWithComments.getTrainerId());

		trainer.setTrainerNotes(trainerWithComments.getTrainerNotes());

		getCurrentSession().update(trainer);
	}


	
	
	@Override
	public TeTrainers updateTrainerVerifiedStatus(
			TeTrainers trainerWithVerifiedStatus) {
		TeTrainers trainer = this.getTrainer(trainerWithVerifiedStatus
				.getTrainerId());
		System.out
				.println("Verified:" + trainerWithVerifiedStatus.getTrainerVerifiedStatus());
		if (trainerWithVerifiedStatus.getTrainerVerifiedStatus() == VerifiedStatus.PENDING) {

			List<TeEmployees> employees = this
					.getEmployeesPension(trainerWithVerifiedStatus
							.getTrainerId());
			boolean employeesNotVerfied = false;
			for (TeEmployees employee : employees) {
				if (!employee.isEmployeeVerified()) {
					employeesNotVerfied = true;
				}

			}
			if (employeesNotVerfied) {
				// System.out.println("Set Not Verified");
				trainer.setTrainerVerifiedStatus(VerifiedStatus.NOTVERIFIED);
			} else {
				// System.out.println("Set Verified");
				trainer.setTrainerVerifiedStatus(VerifiedStatus.VERIFIED);
			}

		} else {
			// System.out.println("Set Pending");
			trainer.setTrainerVerifiedStatus(VerifiedStatus.PENDING);
		}

		getCurrentSession().update(trainer);
		// System.out.println(trainer.getTrainerVerifiedStatus());
		return trainer;
	}

	@Override
	@Transactional(readOnly = true)
	public List<TeEmployees> getEmployeesPension(Integer trainerId) {
		List<TeEmployees> employees = null;
		// only get earnings and pps numbers from previous year
		Date date = new Date(); // your date
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		
		
		int year = cal.get(Calendar.YEAR);
		//CHANGE EARNINGS YEAR HERE
				//change line below to year = year -2 for current year  
				year = year-3;
		Date startDate = null;
		Date endDate = null;
		try {
			startDate = new SimpleDateFormat("yyyy-MM-dd").parse(year
					+ "-12-31");
			endDate = new SimpleDateFormat("yyyy-MM-dd").parse(year + "-12-31");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("GET EMPLOYEES " + startDate);
		DetachedCriteria c = DetachedCriteria.forClass(TeEmployees.class, "employee");
		

		// if the search is specific to a single trainer only get employees
		// associated
		if (trainerId != 0) {

			c.createAlias("employee.teEmployentHistories",
					"teEmployentHistories"); // inner join
			// by
			// default
			c.createAlias("teEmployentHistories.teTrainers", "teTrainers");

			c.add(Restrictions.eq("teTrainers.trainerId", trainerId));
			c.add(Restrictions.ge("teEmployentHistories.ehDateFrom", startDate));
			//c.addOrder(Order.desc("teEmployentHistories.ehDateFrom"));
		}

	    
		c.addOrder(Order.asc("employee.employeesFullName"));

		//c.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		
		c.setProjection(Projections.distinct(Projections.id()));

		Criteria outer = getCurrentSession().createCriteria(TeEmployees.class,
				"employ");
		outer.add(Subqueries.propertyIn("employ.employeesEmployeeId", c));
		
		employees = (List<TeEmployees>) outer.list();
		return employees;
	}

	@Override
	public TeEmployees updateEmployeeHistoryVerifiedStatus(
			TeEmployees employee, Integer trainerId) {
		System.out.println(employee.getEmployeesEmployeeId() + " "
				+ employee.isEmployeeVerified());
		TeEmployees employeeToUpdate = this.getEmployee(employee
				.getEmployeesEmployeeId());
		System.out.println(employeeToUpdate.getEmployeesFirstname());
		Criteria c = getCurrentSession().createCriteria(
				TeEmployentHistory.class, "histories");
		c.createAlias("histories.teTrainers", "trainer");
		c.createAlias("histories.teEmployees", "employee");
		c.add(Restrictions.eq("trainer.trainerId", trainerId));
		c.add(Restrictions.eq("employee.employeesEmployeeId",
				employee.getEmployeesEmployeeId()));

		for (TeEmployentHistory history : (List<TeEmployentHistory>) c.list()) {
			if (history.getEhDateFrom().after(minDateFrom)
					&& history.getEhDateFrom().before(maxDateFrom)) {

				history.setEhVerified(employee.isEmployeeVerified());
			}

		}
		c = getCurrentSession().createCriteria(TeEmployeeTrainerVerified.class,
				"verified");
		c.createAlias("verified.trainerId", "trainer");
		c.createAlias("verified.employeeId", "employee");
		c.add(Restrictions.eq("trainer.trainerId", trainerId));
		c.add(Restrictions.eq("employee.employeesEmployeeId",
				employee.getEmployeesEmployeeId()));
		List<TeEmployeeTrainerVerified> verifiedEmployees = (List<TeEmployeeTrainerVerified>) c
				.list();
		if (verifiedEmployees.size() > 0) {
			for (TeEmployeeTrainerVerified verified : verifiedEmployees) {
				System.out.println("Set Verified "
						+ employee.isEmployeeVerified());
				verified.setVerified(employee.isEmployeeVerified());
				getCurrentSession().saveOrUpdate(verified);
			}
		} else {
			TeEmployeeTrainerVerified newVerified = new TeEmployeeTrainerVerified();
			newVerified.setTrainerId(this.getTrainer(trainerId));
			newVerified.setEmployeeId(employeeToUpdate);
			newVerified.setVerified(employee.isEmployeeVerified());
			System.out.println("SET VER FALSE " + employeeToUpdate.getEmployeesEmployeeId());
			getCurrentSession().save(newVerified);
		}
		// set the trainer status if all current employees are verified
		List<TeEmployees> currentEmployees = this
				.getEmployeesPension(trainerId);
		boolean setTrainerVerified = true;
		// if the trainer has current employees
		if (currentEmployees.size() > 0) {
			System.out.println(currentEmployees.size());
			for (TeEmployees currentEmployee : currentEmployees) {
				// check if all employees for the subject trainer are verified
				// and if so set the trainer as verified
				System.out.println("GET HISTORIES " + minDateFrom + " " + maxDateFrom + " " + trainerId + " " + currentEmployee.getEmployeesEmployeeId());
				for (TeEmployentHistory history : this.getEmploymentHistories(
						trainerId, currentEmployee.getEmployeesEmployeeId(),
						minDateFrom, maxDateFrom)) {

					if (!history.isEhVerified()) {
						System.out.println(history.getEhEmploymentId() + " False set ver");
						setTrainerVerified = false;
					}
				}

				for (TeEmployeeTrainerVerified verified : this
						.getEmployeeTrainerVerified(trainerId,
								currentEmployee.getEmployeesEmployeeId())) {
					if (!verified.isVerified()) {
						System.out.println(verified.getVerifiedId() +  " False set ver");
						setTrainerVerified = false;
					}
				}

			}
			if (setTrainerVerified) {
				System.out.println("verified");
				this.getTrainer(trainerId).setTrainerVerifiedStatus(
						VerifiedStatus.VERIFIED);
			} else {
				System.out.println("Not verified");
				this.getTrainer(trainerId).setTrainerVerifiedStatus(
						VerifiedStatus.NOTVERIFIED);
			}
		}
		// otherwise set the trainer as approved
		else {

			this.getTrainer(trainerId).setTrainerVerifiedStatus(
					VerifiedStatus.VERIFIED);
		}

		return employee;
	}

	@Override
	public List<TeEmployentHistory> getEmploymentHistories(Integer trainerId,
			Integer employeeId, Date yearFrom, Date yearTo) {
		Criteria c = getCurrentSession().createCriteria(
				TeEmployentHistory.class, "histories");
		c.createAlias("histories.teTrainers", "trainer");
		c.createAlias("histories.teEmployees", "employee");
		c.add(Restrictions.eq("employee.employeesEmployeeId", employeeId));
		c.add(Restrictions.eq("trainer.trainerId", trainerId));
		System.out.println(yearFrom + " " + yearTo);
		c.add(Restrictions.gt("ehDateFrom", yearFrom));
		c.add(Restrictions.lt("ehDateFrom", yearTo));
		return c.list();
	}

	@Override
	public List<TeEmployeeTrainerVerified> getEmployeeTrainerVerified(
			Integer trainerId, Integer employeeId) {
		Criteria c = getCurrentSession().createCriteria(
				TeEmployeeTrainerVerified.class, "verified");
		c.createAlias("verified.trainerId", "trainer");
		c.createAlias("verified.employeeId", "employee");
		c.add(Restrictions.eq("trainer.trainerId", trainerId));
		c.add(Restrictions.eq("employee.employeesEmployeeId", employeeId));

		return c.list();
	}

	@Override
	public VerifiedStatus getTrainerVerifiedStatus(Integer trainerId) {
		TeTrainers trainer = this.getTrainer(trainerId);
		return trainer.getTrainerVerifiedStatus();
	}

	@Override
	public void updateTrainer(TeTrainers trainer) {
		getCurrentSession().saveOrUpdate(trainer);
		
	}

	@Override
	public List<HashMap<String, Object>> getAllTrainers() {
		
		Criteria criteria = getCurrentSession().createCriteria(TeTrainers.class);
		List<TeTrainers> records = criteria.list();
		List<HashMap<String, Object>> results = new ArrayList<HashMap<String,Object>>();
		if(records != null) {
			for (TeTrainers trainer : records) {
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("name", trainer.getTrainerFullName());
				map.put("id", trainer.getTrainerId());
				results.add(map);
			}
		}
		return results;
	}
	
	@Override
	public Object getVerifiedStatus() {
		
		Gson gson = new Gson();
		List<Map<String,Object>> statuses = new ArrayList<Map<String,Object>>();
		VerifiedStatus verifiedStatuses = new TeTrainers().getTrainerVerifiedStatus();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", verifiedStatuses.NOTVERIFIED);
		map.put("text", verifiedStatuses.NOTVERIFIED);
		statuses.add(map);
		
		map = new HashMap<String, Object>();
		map.put("id", verifiedStatuses.PENDING);
		map.put("text", verifiedStatuses.PENDING);
		statuses.add(map);
		
		map = new HashMap<String, Object>();
		map.put("id", verifiedStatuses.RESET);
		map.put("text", verifiedStatuses.RESET);
		statuses.add(map);
		
		map = new HashMap<String, Object>();
		map.put("id", verifiedStatuses.VERIFIED);
		map.put("text", verifiedStatuses.VERIFIED);
		statuses.add(map);
		
		String json = gson.toJson(statuses);
		return json;
	}
	
	@Override
	public String saveOrUpdate(TeTrainers trainer) throws SQLException {
		
		trainer.setTrainerDateRequested(new Date());
		trainer.setPwd(passwordEncoder.encode("test"));
		getCurrentSession().saveOrUpdate(trainer);
		Person person = createPerson(trainer);
		personService.addPerson(person);
		
		return null;
	}

	private Person createPerson(TeTrainers trainer) {
		
		Person person = new Person();
		person.setRefId(trainer.getTrainerId());
		person.setSurname(trainer.getTrainerSurname());
		person.setFirstname(trainer.getTrainerFirstName());
		person.setDateOfBirth(trainer.getTrainerDateOfBirth());
		person.setRequestDate(trainer.getTrainerDateRequested());
		person.setDateEntered(trainer.getTrainerTimeEntered());
		person.setAddress1(trainer.getTrainerAddress1());
		person.setAddress2(trainer.getTrainerAddress2());
		person.setAddress3(trainer.getTrainerAddress3());
		person.setPhoneNo(trainer.getTrainerHomePhone());
		person.setMobileNo(trainer.getTrainerMobilePhone());
		person.setEmail(trainer.getTrainerEmail());
		person.setComments(trainer.getTrainerNotes());
		person.setRoleId(RoleEnum.TRAINER.getId());
		person.setTitle(trainer.getTitle());
		person.setSex(trainer.getSex());
		person.setNationality(trainer.getNationality());
		person.setMaritalStatus(trainer.getMaritalStatus());
		person.setSpouseName(trainer.getSpouseName());
		person.setCounty(trainer.getCounty());
		person.setCountry(trainer.getCountry());
		person.setPostCode(trainer.getPostCode());
		return person;
	}
	
	@Override
	public void handleCopyRecord() throws SQLException {
		
		List<TeTrainers> trainers = getCurrentSession().createCriteria(TeTrainers.class).list();
		
		if(trainers != null && trainers.size() > 0) {
			
			for (TeTrainers trainer : trainers) {
				Person person = createPerson(trainer);
				personService.addPerson(person);
			}
		}
	}
}
