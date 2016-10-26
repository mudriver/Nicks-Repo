package ie.turfclub.inspections.controller;

import ie.turfclub.inspections.model.InspectionSavedSearch;
import ie.turfclub.inspections.model.InspectionsInspections;
import ie.turfclub.inspections.model.InspectionsOfficials;
import ie.turfclub.inspections.pojos.InspectionsAddress;
import ie.turfclub.inspections.service.InspectionsService;
import ie.turfclub.main.model.login.User;
import ie.turfclub.main.pojos.directives.DirectiveTypes;
import ie.turfclub.main.pojos.directives.display.ButtonDirective;
import ie.turfclub.main.pojos.directives.display.DateDisplayDirective;
import ie.turfclub.main.pojos.directives.display.SpanDirective;
import ie.turfclub.main.pojos.directives.display.ButtonDirective.FunctionTypes;
import ie.turfclub.main.pojos.directives.input.DateDirective;
import ie.turfclub.main.pojos.directives.input.FillFieldsButtonDirective;
import ie.turfclub.main.pojos.directives.input.InputDirective;
import ie.turfclub.main.pojos.directives.input.Select2Directive;
import ie.turfclub.main.pojos.directives.modals.DeleteModal;
import ie.turfclub.main.pojos.jsonEdit.JsonEdit;
import ie.turfclub.main.pojos.jsonEdit.JsonFormFields;
import ie.turfclub.main.pojos.jsonTable.JsonColumnSearchConfig;
import ie.turfclub.main.pojos.jsonTable.JsonSavedSearchConfig;
import ie.turfclub.main.pojos.jsonTable.JsonTable;
import ie.turfclub.main.pojos.jsonTable.JsonTableColumn;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/inspections/service")
public class InspectionsJSONController {

	@Autowired
	InspectionsService inspectionsService;

	@RequestMapping(value = "/data/inspections", method = RequestMethod.POST)
	public @ResponseBody
	HashMap<String, Object> getInspectionsData(
			@RequestBody(required = false) InspectionSavedSearch search,
			Authentication authentication) {

		if (search == null) {
			System.out.println("NO SEARCH");
		}

		Object principal = authentication.getPrincipal();
		User user = (User) principal;
		String role = "";
		for (GrantedAuthority roles : user.getAuthorities()) {
			System.out.println(roles.getAuthority());
			if (roles.getAuthority().equals("INSPECTIONS_ADMIN")
					|| roles.getAuthority().equals("INSPECTIONS_LICENCING")
					|| roles.getAuthority().equals("INSPECTIONS_CEO")) {
				role = roles.getAuthority();
			}
		}

		return inspectionsService.getInspectionsJson(role, search);
	}

	@RequestMapping(value = "/data/inspectionsCached", method = RequestMethod.POST)
	public @ResponseBody
	HashMap<String, Object> getInspectionsCache(Authentication authentication) {

		Object principal = authentication.getPrincipal();
		User user = (User) principal;
		String role = "";
		for (GrantedAuthority roles : user.getAuthorities()) {
			System.out.println(roles.getAuthority());
			if (roles.getAuthority().equals("INSPECTIONS_ADMIN")
					|| roles.getAuthority().equals("INSPECTIONS_LICENCING")
					|| roles.getAuthority().equals("INSPECTIONS_CEO")) {
				role = roles.getAuthority();
			}
		}

		return inspectionsService.getInspectionsCacheJson(role);
	}

	@RequestMapping(value = "/data/address", method = RequestMethod.GET)
	public @ResponseBody
	HashMap<String, String> getAddress(@RequestParam("id") String id) {
		// System.out.println(foo);

		return inspectionsService.getPersonAddress(id.replaceAll("'", "''"));
	}

	@RequestMapping(value = "/data/yardAddress", method = RequestMethod.GET)
	public @ResponseBody
	HashMap<String, String> getYardAddress(@RequestParam("id") String id) {
		// System.out.println(foo);

		return inspectionsService
				.getPersonYardAddress(id.replaceAll("'", "''"));
	}

	@RequestMapping(value = "/select/persons", method = RequestMethod.GET)
	public @ResponseBody
	List<HashMap<String, String>> getPersons(@RequestParam("chars") String chars) {
		// System.out.println(foo);

		List<HashMap<String, String>> persons = inspectionsService
				.getPeople(chars.replaceAll("'", "''"));
		return persons;
	}

	@RequestMapping(value = "/select/categories", method = RequestMethod.GET)
	public @ResponseBody
	List<HashMap<String, String>> getCategories(Authentication authentication) {
		// System.out.println(foo);

		Object principal = authentication.getPrincipal();
		User user = (User) principal;
		String role = "";
		for (GrantedAuthority roles : user.getAuthorities()) {
			System.out.println(roles.getAuthority());
			if (roles.getAuthority().equals("INSPECTIONS_ADMIN")
					|| roles.getAuthority().equals("INSPECTIONS_LICENCING")
					|| roles.getAuthority().equals("INSPECTIONS_CEO")) {
				role = roles.getAuthority();
			}
		}

		List<HashMap<String, String>> categories = inspectionsService
				.getCategories(role);
		return categories;
	}

	@RequestMapping(value = "/select/categoriesAll", method = RequestMethod.GET)
	public @ResponseBody
	List<HashMap<String, String>> getCategories() {
		// System.out.println(foo);

		List<HashMap<String, String>> categories = inspectionsService
				.getCategories();
		return categories;
	}

	@RequestMapping(value = "/select/subcategories", method = RequestMethod.GET)
	public @ResponseBody
	List<HashMap<String, String>> getSubCategories(Authentication authentication) {
		// System.out.println(foo);
		Object principal = authentication.getPrincipal();
		User user = (User) principal;
		String role = "";
		for (GrantedAuthority roles : user.getAuthorities()) {
			System.out.println(roles.getAuthority());
			if (roles.getAuthority().equals("INSPECTIONS_ADMIN")
					|| roles.getAuthority().equals("INSPECTIONS_LICENCING")
					|| roles.getAuthority().equals("INSPECTIONS_CEO")) {
				role = roles.getAuthority();
			}
		}
		List<HashMap<String, String>> subcategories = inspectionsService
				.getSubCategories(role);
		return subcategories;
	}

	@RequestMapping(value = "/select/status", method = RequestMethod.GET)
	public @ResponseBody
	List<HashMap<String, String>> getStatuses(Authentication authentication) {

		Object principal = authentication.getPrincipal();
		User user = (User) principal;
		String role = "";
		for (GrantedAuthority roles : user.getAuthorities()) {
			System.out.println(roles.getAuthority());
			if (roles.getAuthority().equals("INSPECTIONS_ADMIN")
					|| roles.getAuthority().equals("INSPECTIONS_LICENCING")
					|| roles.getAuthority().equals("INSPECTIONS_CEO")) {
				role = roles.getAuthority();
			}
		}
		List<HashMap<String, String>> status = inspectionsService
				.getStatus(role);
		return status;
	}

	@RequestMapping(value = "/select/officials", method = RequestMethod.GET)
	public @ResponseBody
	List<HashMap<String, String>> getOfficials() {
		// System.out.println(foo);

		List<HashMap<String, String>> officials = inspectionsService
				.getOfficials();
		return officials;
	}

	@RequestMapping(value = "/select/savedSearches", method = RequestMethod.GET)
	public @ResponseBody
	List<Object> getSavedSearches(Authentication authentication) {
		// System.out.println(foo);

		Object principal = authentication.getPrincipal();
		User user = (User) principal;

		return inspectionsService.getSaveSearches(user.getId());
	}

	
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public @ResponseBody HashMap<String, String> deleteInspection(
			@RequestBody(required = true) Integer inspectionId,
			Authentication authentication) {

		System.out.println("Delete " + inspectionId);

		Object principal = authentication.getPrincipal();
		User user = (User) principal;
		String role = "";
		for (GrantedAuthority roles : user.getAuthorities()) {
			System.out.println(roles.getAuthority());
			if (roles.getAuthority().equals("INSPECTIONS_ADMIN")
					|| roles.getAuthority().equals("INSPECTIONS_LICENCING")
					|| roles.getAuthority().equals("INSPECTIONS_CEO")) {
				role = roles.getAuthority();
			}
		}
		inspectionsService.deleteInspection(inspectionId);
		HashMap<String, String> map = new HashMap<>();
		map.put("message","Deleted" );
		return map;
	}
	
	@RequestMapping(value = "/save/inspection", method = RequestMethod.POST)
	public @ResponseBody
	HashMap<String, String> saveInspection(
			@RequestBody(required = true) InspectionsInspections inspection,
			Authentication authentication) {

		Object principal = authentication.getPrincipal();
		User user = (User) principal;

		HashMap<String, String> map = new HashMap<String, String>();
		for (InspectionsOfficials official : inspection
				.getInspectionsOfficials()) {
			System.out.println(official.getOfficialsId());
		}
		if (inspection.getInspectionId() == null) {
			inspection.setInspectionUserId(user.getId());
			inspectionsService.saveInspection(inspection);

			map.put("message", "Saved record successfully");
		} else {
			inspectionsService.updateInspection(inspection);
			map.put("message", "Updated record successfully");
		}

		return map;
	}

	@RequestMapping(value = "/save/search", method = RequestMethod.POST)
	public @ResponseBody
	List<Object> saveSearch(
			@RequestBody(required = false) InspectionSavedSearch savedSearch,
			Authentication authentication) {
		System.out.println("SAVE SEARCH:" + (savedSearch == null));
		Object principal = authentication.getPrincipal();
		User user = (User) principal;
		savedSearch.setSavedSearchUserId(user.getId());
		inspectionsService.saveSearch(savedSearch);

		return inspectionsService.getSaveSearches(user.getId());
	}

	@RequestMapping(value = "/address", method = RequestMethod.GET)
	public @ResponseBody
	InspectionsAddress getAddress(@RequestParam("id") Integer id,
			@RequestParam("type") String type) {
		// System.out.println(foo);

		InspectionsAddress address = inspectionsService.getAddress(id, type);
		return address;
	}

	/*
	 * @RequestMapping(value = "/inspections", method = RequestMethod.POST)
	 * public @ResponseBody List<InspectionsInspections>
	 * getInspections(@ModelAttribute InspectionsSavedSearches search,
	 * Authentication authentication) {
	 * 
	 * System.out.println(search == null); if(search != null){
	 * 
	 * if(search.getSavedSearchDates() != null){
	 * System.out.println("Start Date:" +
	 * search.getSavedSearchDates().getSavedSearchStartDate()); }
	 * if(search.getSavedSearchMaxResults() != null){ System.out.println("Max:"
	 * + search.getSavedSearchMaxResults()); }
	 * 
	 * }
	 * 
	 * Object principal = authentication.getPrincipal(); User user = (User)
	 * principal; user.getAuthorities(); String role = ""; for(GrantedAuthority
	 * roles: user.getAuthorities()){ System.out.println(roles.getAuthority());
	 * if(roles.getAuthority().equals("INSPECTIONS_ADMIN_CEO") ||
	 * roles.getAuthority().equals("INSPECTIONS_ADMIN_LICENCING")){ role =
	 * roles.getAuthority(); } }
	 * 
	 * List<InspectionsInspections> inspections =
	 * inspectionsService.getInspections(search, role);
	 * 
	 * return inspections; }
	 */
	@RequestMapping(value = "/list", method = RequestMethod.POST)
	public @ResponseBody
	JsonTable getInspections(Authentication authentication) {

		Object principal = authentication.getPrincipal();
		User user = (User) principal;
		user.getAuthorities();
		String role = "";
		for (GrantedAuthority roles : user.getAuthorities()) {
			System.out.println(roles.getAuthority());
			if (roles.getAuthority().equals("INSPECTIONS_ADMIN_CEO")
					|| roles.getAuthority().equals(
							"INSPECTIONS_ADMIN_LICENCING")) {
				role = roles.getAuthority();
			}
		}

		JsonTableColumn column;
		JsonColumnSearchConfig searchConfig;
		Select2Directive select2;
		DateDisplayDirective dateDirective;
		JsonTable table = new JsonTable();
		table.setTitle("Inspections");
		table.setDataUrl("/turfclubPrograms/inspections/service/data/inspections");
		table.setCachedDataUrl("/turfclubPrograms/inspections/service/data/inspectionsCached");
		table.setCachedDataPrefix("inspections-list");
		table.setCachedDataUserId(user.getId().toString());
		// set the pagination options
		table.getPageSizeOptions().add(25);
		table.getPageSizeOptions().add(50);
		table.getPageSizeOptions().add(100);
		table.setSavedSearch(new InspectionSavedSearch());

		// set the save search config
		JsonSavedSearchConfig savedSearchConfig = new JsonSavedSearchConfig();

		savedSearchConfig
				.setSaveUrl("/turfclubPrograms/inspections/service/save/search");
		// inspectionsService.getSaveSearches();
		savedSearchConfig.setSavedSearches(inspectionsService
				.getSaveSearches(user.getId()));

		table.setSavedSearchConfig(savedSearchConfig);
		column = new JsonTableColumn();
		column.setTitle("Id");
		column.setColumnWidth("1");
		SpanDirective spanDirective = new SpanDirective();
		spanDirective.setKey("inspectionId");
		spanDirective.setSplitAfter(1);
		column.setDirective(spanDirective);
		table.getColumns().add(column);
		// Person Inspected Column
		column = new JsonTableColumn();

		column.setTitle("Person Inspected");
		column.setColumnWidth("20");
		// column.setTextKey("inspectionDate");
		// searchConfig = new JsonColumnSearchConfig();
		// searchConfig.setSearchDirective(FormDirectiveTypes.DATEPICKERDIR);
		spanDirective = new SpanDirective();
		spanDirective.setKey("inspectionsPersonId.name");
		spanDirective.setSplitAfter(2);
		spanDirective.setSplitter(", ");
		column.setDirective(spanDirective);
		// searchConfig.setDateDirective(dateDirective);
		searchConfig = new JsonColumnSearchConfig();
		select2 = new Select2Directive();
		select2.setIdKey("personSearch.id");
		select2.setKey("personSearch.name");
		select2.setSearchUrl("/turfclubPrograms/inspections/service/select/persons");
		select2.setLoadOnInitOnly(false);
		select2.setRequired(false);
		searchConfig.setDirective(select2);
		column.setSearchConfig(searchConfig);
		column.setSortable(true);

		table.getColumns().add(column);

		// Category
		column = new JsonTableColumn();
		column.setTitle("Category");
		spanDirective = new SpanDirective();
		spanDirective.setKey("inspectionCategoriesId.catName");
		spanDirective.setSplitAfter(2);
		spanDirective.setSplitter(", ");
		column.setDirective(spanDirective);
		searchConfig = new JsonColumnSearchConfig();
		select2 = new Select2Directive();
		select2.setIdKey("categorySearch.inspectionSubCategoriesId.catId");
		select2.setKey("categorySearch.inspectionSubCategoriesId.catName");
		select2.setSearchUrl("/turfclubPrograms/inspections/service/select/categoriesAll");
		select2.setLoadOnInitOnly(true);
		select2.setRequired(false);
		searchConfig.setDirective(select2);
		column.setSearchConfig(searchConfig);
		table.getColumns().add(column);

		// SubCategory
		column = new JsonTableColumn();
		column.setTitle("Subcategory");
		spanDirective = new SpanDirective();
		spanDirective.setKey("inspectionSubCategoriesId.subCatName");
		spanDirective.setSplitAfter(2);
		spanDirective.setSplitter(", ");
		column.setDirective(spanDirective);
		table.getColumns().add(column);

		// Status
		column = new JsonTableColumn();
		column.setTitle("Status");
		searchConfig = new JsonColumnSearchConfig();
		select2 = new Select2Directive();
		select2.setIdKey("statusSearch.inspectionStatusId.statusId");
		select2.setKey("statusSearch.inspectionStatusId.statusType");
		select2.setSearchUrl("/turfclubPrograms/inspections/service/select/status");
		select2.setLoadOnInitOnly(true);
		select2.setRequired(false);
		searchConfig.setDirective(select2);
		column.setSearchConfig(searchConfig);
		spanDirective = new SpanDirective();
		spanDirective.setKey("inspectionStatusId.statusType");
		spanDirective.setSplitAfter(2);
		spanDirective.setSplitter(", ");
		column.setDirective(spanDirective);
		// searchConfig.setDateDirective(dateDirective);
		// column.setSearchConfig(searchConfig);
		table.getColumns().add(column);

		// Officials on Duty
		column = new JsonTableColumn();
		column.setTitle("Officials On Duty");
		// column.setTextKey("inspectionDate");
		// searchConfig = new JsonColumnSearchConfig();
		// searchConfig.setSearchDirective(FormDirectiveTypes.DATEPICKERDIR);
		spanDirective = new SpanDirective();
		spanDirective
				.setKey("inspectionsOfficials.officialsSurname officialsFirstName");
		spanDirective.setSplitAfter(2);
		spanDirective.setSplitter(", ");
		column.setDirective(spanDirective);
		// searchConfig.setDateDirective(dateDirective);
		// column.setSearchConfig(searchConfig);
		table.getColumns().add(column);

		// Dept Officials
		column = new JsonTableColumn();
		column.setTitle("Dept. Officials");
		// column.setTextKey("inspectionDate");
		// searchConfig = new JsonColumnSearchConfig();
		// searchConfig.setSearchDirective(FormDirectiveTypes.DATEPICKERDIR);
		spanDirective = new SpanDirective();
		spanDirective.setKey("inspectionDeptOfficials");
		spanDirective.setSplitAfter(2);
		spanDirective.setSplitter(", ");
		column.setDirective(spanDirective);
		// searchConfig.setDateDirective(dateDirective);
		// column.setSearchConfig(searchConfig);
		table.getColumns().add(column);

		//Comments
		column = new JsonTableColumn();
		column.setTitle("Comments");
		// column.setTextKey("inspectionDate");
		// searchConfig = new JsonColumnSearchConfig();
		// searchConfig.setSearchDirective(FormDirectiveTypes.DATEPICKERDIR);
		spanDirective = new SpanDirective();
		spanDirective.setKey("inspectionsComments");
		column.setDirective(spanDirective);
		// searchConfig.setDateDirective(dateDirective);
		// column.setSearchConfig(searchConfig);
		table.getColumns().add(column);

		column = new JsonTableColumn();
		column.setTitle("Date");
		column.setSortable(true);
		// column.setTextKey("inspectionDate");
		// searchConfig = new JsonColumnSearchConfig();
		// searchConfig.setSearchDirective(FormDirectiveTypes.DATEPICKERDIR);
		dateDirective = new DateDisplayDirective();
		dateDirective.setKey("inspectionDate");
		dateDirective.setPreLabel("Select a date");
		column.setDirective(dateDirective);
		// searchConfig.setDateDirective(dateDirective);
		// column.setSearchConfig(searchConfig);
		table.getColumns().add(column);

		// edit button column
		column = new JsonTableColumn();
		column.setTitle("Edit");
		column.setColumnWidth("2");
		ButtonDirective buttonDir = new ButtonDirective();
		buttonDir.setKey("inspectionId");
		buttonDir.setText("Edit");
		buttonDir.setButtonClass("btn btn-success");
		buttonDir.setButtonDisabledClass("btn btn-default");
		buttonDir.setButtonGlyphiconClass("glyphicon glyphicon-pencil");
		buttonDir.setUrl("/edit/");
		buttonDir.setEnabledDisabledKey("canEdit");
		column.setDirective(buttonDir);
		table.getColumns().add(column);
		
		// delete button column
		column = new JsonTableColumn();
		column.setTitle("Delete");
		column.setColumnWidth("2");
		buttonDir = new ButtonDirective();
		buttonDir.setKey("inspectionId");
		buttonDir.setText("Delete");
		buttonDir.setButtonClass("btn btn-danger");
		buttonDir.setButtonDisabledClass("btn btn-default");
		buttonDir.setButtonGlyphiconClass("glyphicon glyphicon-trash");
		buttonDir.setUrl("/delete/");
		buttonDir.setEnabledDisabledKey("canEdit");
		DeleteModal deleteModal = new DeleteModal();
		deleteModal.setDeleteUrl("/turfclubPrograms/inspections/service/delete/");
		deleteModal.setTitle("Delete Record ");
		deleteModal.setWarningText("Are you sure you want to delete record no. ");
		buttonDir.setModal(deleteModal);
		buttonDir.setFunctionType(FunctionTypes.MODAL);
		column.setDirective(buttonDir);
		table.getColumns().add(column);

		return table;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public @ResponseBody
	JsonEdit editInspections(
			@RequestParam(value = "id", required = false) Integer id,
			Authentication authentication) {
		InspectionsInspections inspection;
		if (id == null) {
			System.out.println("Null");
			inspection = new InspectionsInspections();

		} else {
			inspection = inspectionsService.getInspection(id);
		}

		Object principal = authentication.getPrincipal();
		User user = (User) principal;
		user.getAuthorities();
		String role = "";
		for (GrantedAuthority roles : user.getAuthorities()) {
			System.out.println(roles.getAuthority());
			if (roles.getAuthority().equals("INSPECTIONS_ADMIN_CEO")
					|| roles.getAuthority().equals(
							"INSPECTIONS_ADMIN_LICENCING")) {
				role = roles.getAuthority();
			}
		}
		// inspection = inspectionsService.getInspection(528);

		// inspection.setInspectionDate(new Date());
		JsonEdit edit = new JsonEdit();
		JsonFormFields field;
		edit.setTitle("Edit Inspection");
		edit.setSaveUrl("/turfclubPrograms/inspections/service/save/inspection");
		edit.setAfterSaveUrl("/list");

		// edit.setData(new InspectionsInspections());
		edit.setData(inspection);
		// field = new JsonFormFields();
		// field.setFieldkey("inspectionTrainerId.trainerId");
		// field.setFieldLabel("Trainer:");
		// field.setFormDirective(FormDirectiveTypes.INPUT);
		// edit.getFormFields().add(field);
		// field to edit the inspected person
		field = new JsonFormFields();
		field.setFieldLabel("Person Inspected:");
		Select2Directive select2 = new Select2Directive();
		select2.setPlaceHolder("Enter a person name");
		select2.setSearchUrl("/turfclubPrograms/inspections/service/select/persons");
		select2.setKey("inspectionsPersonId.name");
		select2.setIdKey("inspectionsPersonId.id");
		select2.setDirectiveType(DirectiveTypes.SELECT2);
		field.setDirective(select2);
		edit.getFormFields().add(field);

		field = new JsonFormFields();
		FillFieldsButtonDirective fillFieldsButtonDirective = new FillFieldsButtonDirective();
		fillFieldsButtonDirective.setButtonText("Set as current address");
		fillFieldsButtonDirective
				.setDataUrl("/turfclubPrograms/inspections/service/data/address");
		fillFieldsButtonDirective.getParamFields()
				.add("inspectionsPersonId.id");
		fillFieldsButtonDirective.getParamNames().add("id");
		fillFieldsButtonDirective.getDataKeys().add("address1");
		fillFieldsButtonDirective.getFieldsToFill().add("inspectionsAddress1");
		fillFieldsButtonDirective.getDataKeys().add("address2");
		fillFieldsButtonDirective.getFieldsToFill().add("inspectionsAddress2");
		fillFieldsButtonDirective.getDataKeys().add("address3");
		fillFieldsButtonDirective.getFieldsToFill().add("inspectionsAddress3");
		fillFieldsButtonDirective.getDataKeys().add("address4");
		fillFieldsButtonDirective.getFieldsToFill().add("inspectionsAddress4");
		field.setDirective(fillFieldsButtonDirective);
		edit.getFormFields().add(field);

		field = new JsonFormFields();

		fillFieldsButtonDirective = new FillFieldsButtonDirective();
		fillFieldsButtonDirective.setButtonText("Set as current yard address");
		fillFieldsButtonDirective
				.setDataUrl("/turfclubPrograms/inspections/service/data/yardAddress");
		fillFieldsButtonDirective.getParamFields()
				.add("inspectionsPersonId.id");
		fillFieldsButtonDirective.getParamNames().add("id");
		fillFieldsButtonDirective.getDataKeys().add("address1");
		fillFieldsButtonDirective.getFieldsToFill().add("inspectionsAddress1");
		fillFieldsButtonDirective.getDataKeys().add("address2");
		fillFieldsButtonDirective.getFieldsToFill().add("inspectionsAddress2");
		fillFieldsButtonDirective.getDataKeys().add("address3");
		fillFieldsButtonDirective.getFieldsToFill().add("inspectionsAddress3");
		fillFieldsButtonDirective.getDataKeys().add("address4");
		fillFieldsButtonDirective.getFieldsToFill().add("inspectionsAddress4");
		field.setDirective(fillFieldsButtonDirective);
		edit.getFormFields().add(field);

		field = new JsonFormFields();
		field.setFieldLabel("Address Line 1");

		InputDirective input = new InputDirective();
		input.setKey("inspectionsAddress1");
		input.setRequiredMessage("Address Line 1 required");
		input.setPlaceHolder("Address Line 1");
		input.setRequired(true);
		field.setDirective(input);
		edit.getFormFields().add(field);

		field = new JsonFormFields();
		field.setFieldLabel("Address Line 2");

		input = new InputDirective();
		input.setKey("inspectionsAddress2");
		input.setPlaceHolder("Address Line 2");
		input.setRequiredMessage("Address Line 2 required");
		input.setRequired(true);
		field.setDirective(input);
		edit.getFormFields().add(field);

		field = new JsonFormFields();
		field.setFieldLabel("Address Line 3");

		input = new InputDirective();
		input.setKey("inspectionsAddress3");
		input.setPlaceHolder("Address Line 3");
		input.setRequired(true);
		input.setRequiredMessage("Address Line 3 required");
		field.setDirective(input);
		edit.getFormFields().add(field);

		field = new JsonFormFields();
		field.setFieldLabel("Address Line 4");
		input = new InputDirective();
		input.setRequired(false);
		input.setKey("inspectionsAddress4");
		input.setPlaceHolder("Address Line 4 (not required)");
		input.setRequiredMessage("Address Line 4 required");
		field.setDirective(input);
		edit.getFormFields().add(field);

		field = new JsonFormFields();
		field.setFieldLabel("Date:");
		DateDirective date = new DateDirective();
		date.setKey("inspectionDate");
		date.setName("inspectionDate");
		field.setDirective(date);
		edit.getFormFields().add(field);

		field = new JsonFormFields();
		field.setFieldLabel("Status:");
		select2 = new Select2Directive();
		select2.setPlaceHolder("Enter a status");
		select2.setSearchUrl("/turfclubPrograms/inspections/service/select/status");
		select2.setLoadOnInitOnly(true);
		select2.setKey("inspectionStatusId.statusType");
		select2.setIdKey("inspectionStatusId.statusId");
		select2.setDirectiveType(DirectiveTypes.SELECT2);
		field.setDirective(select2);

		edit.getFormFields().add(field);

		field = new JsonFormFields();
		field.setFieldLabel("Category:");
		select2 = new Select2Directive();
		select2.setPlaceHolder("Enter a category");
		select2.setSearchUrl("/turfclubPrograms/inspections/service/select/categories");
		select2.setLoadOnInitOnly(true);
		select2.setKey("inspectionCategoriesId.catName");
		select2.setIdKey("inspectionCategoriesId.catId");
		select2.setDirectiveType(DirectiveTypes.SELECT2);
		field.setDirective(select2);
		edit.getFormFields().add(field);

		field = new JsonFormFields();
		field.setFieldLabel("Subcategory:");
		select2 = new Select2Directive();
		select2.setKey("inspectionSubCategoriesId.subCatName");
		select2.setIdKey("inspectionSubCategoriesId.subCatId");
		select2.setPlaceHolder("Enter a subcategory");
		select2.setSearchUrl("/turfclubPrograms/inspections/service/select/subcategories");
		select2.setLoadOnInitOnly(true);
		select2.setLinkedFormFieldModel("inspectionCategoriesId.catId");
		select2.setDirectiveType(DirectiveTypes.SELECT2LINKED);
		field.setDirective(select2);
		edit.getFormFields().add(field);

		field = new JsonFormFields();
		field.setFieldLabel("Officals on Duty:");
		select2 = new Select2Directive();
		select2.setRequired(true);
		select2.setKey("inspectionsOfficials.officialsFirstName officialsSurname");
		select2.setIdKey("inspectionsOfficials.officialsId");
		select2.setPlaceHolder("Select official(s)");
		select2.setSearchUrl("/turfclubPrograms/inspections/service/select/officials");
		select2.setLoadOnInitOnly(true);
		field.setDirective(select2);
		edit.getFormFields().add(field);

		field = new JsonFormFields();
		field.setFieldLabel("Dept. Officals:");

		input = new InputDirective();
		input.setKey("inspectionDeptOfficials");
		input.setPlaceHolder("Department officials (if present)");
		input.setRequired(false);
		field.setDirective(input);
		edit.getFormFields().add(field);
		
		field = new JsonFormFields();
		field.setFieldLabel("Comments:");

		input = new InputDirective();
		input.setKey("inspectionsComments");
		input.setPlaceHolder("Comments");
		input.setRequired(false);
		field.setDirective(input);
		edit.getFormFields().add(field);

		return edit;
	}

}