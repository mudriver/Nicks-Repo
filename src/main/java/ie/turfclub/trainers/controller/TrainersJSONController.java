package ie.turfclub.trainers.controller;

import ie.turfclub.inspections.model.InspectionSavedSearch;
import ie.turfclub.inspections.model.InspectionsInspections;
import ie.turfclub.main.model.login.User;
import ie.turfclub.main.pojos.directives.DirectiveTypes;
import ie.turfclub.main.pojos.directives.display.ButtonDirective;
import ie.turfclub.main.pojos.directives.display.ContactDetailsDirective;
import ie.turfclub.main.pojos.directives.display.DateDisplayDirective;
import ie.turfclub.main.pojos.directives.display.EnumToImageDirective;
import ie.turfclub.main.pojos.directives.display.MultiButtonDirective;
import ie.turfclub.main.pojos.directives.display.SpanDirective;
import ie.turfclub.main.pojos.directives.display.TrueFalseTickDirective;
import ie.turfclub.main.pojos.directives.display.ButtonDirective.FunctionTypes;
import ie.turfclub.main.pojos.directives.display.TrueFalseWithButtonDirective;
import ie.turfclub.main.pojos.directives.input.CommentBoxDirective;
import ie.turfclub.main.pojos.directives.input.CommentBoxWithEnumToggleDirective;
import ie.turfclub.main.pojos.directives.input.DateDirective;
import ie.turfclub.main.pojos.directives.input.FillFieldsButtonDirective;
import ie.turfclub.main.pojos.directives.input.InputDirective;
import ie.turfclub.main.pojos.directives.input.Select2Directive;
import ie.turfclub.main.pojos.directives.input.SelectBooleanDirective;
import ie.turfclub.main.pojos.directives.input.SelectEnumImageDirective;
import ie.turfclub.main.pojos.directives.modals.UploadModal;
import ie.turfclub.main.pojos.directives.subclasses.ImageUrlWithTextAndKey;
import ie.turfclub.main.pojos.jsonEdit.JsonEdit;
import ie.turfclub.main.pojos.jsonEdit.JsonFormFields;
import ie.turfclub.main.pojos.jsonTable.JsonColumnSearchConfig;
import ie.turfclub.main.pojos.jsonTable.JsonSavedSearchConfig;
import ie.turfclub.main.pojos.jsonTable.JsonTable;
import ie.turfclub.main.pojos.jsonTable.JsonTableColumn;
import ie.turfclub.trainers.model.TeEmployees;
import ie.turfclub.trainers.model.TeFile;
import ie.turfclub.trainers.model.TeFile.UploadType;
import ie.turfclub.trainers.model.TeTrainers;
import ie.turfclub.trainers.model.TeTrainers.VerifiedStatus;
import ie.turfclub.trainers.model.savedSearches.TeAuthRepsSavedSearches;
import ie.turfclub.trainers.model.savedSearches.TeEmployeesPensionSavedSearch;
import ie.turfclub.trainers.model.savedSearches.TeEmployeesSavedSearch;
import ie.turfclub.trainers.model.savedSearches.TeTrainersPensionSavedSearch;
import ie.turfclub.trainers.model.savedSearches.TeTrainersSavedSearch;
import ie.turfclub.trainers.service.FileService;
import ie.turfclub.trainers.service.TrainersService;
import ie.turfclub.utilities.PDFConvertAndMerge;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.util.IOUtils;
import org.imgscalr.Scalr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfWriter;

@RestController
@RequestMapping(value = "/trainers/service")
public class TrainersJSONController {

	@Autowired
	TrainersService trainersService;
	@Autowired
	private FileService fileService;
	private String fileDirectory = "/home/FTP-shared/stableread/ftp/";
	// private String fileDirectory = "C:/files/";
	static Logger logger = LoggerFactory
			.getLogger(TrainersJSONController.class);

	@RequestMapping(value = "/data/trainerList", method = RequestMethod.POST)
	public @ResponseBody
	HashMap<String, Object> getTrainerData(
			@RequestBody(required = false) TeTrainersSavedSearch savedSearch,
			Authentication authentication) {

		logger.info("Data Trainer List");
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

		if (savedSearch == null) {
			savedSearch = new TeTrainersSavedSearch();
		}
		HashMap<String, Object> trainers = trainersService
				.getTrainers(savedSearch);

		return trainers;
		
	}

	@RequestMapping(value = "/data/trainerPensionList", method = RequestMethod.POST)
	public @ResponseBody
	HashMap<String, Object> getTrainerPensionData(
			@RequestBody(required = false) TeTrainersPensionSavedSearch savedSearch,
			Authentication authentication) {

		logger.info("Data Trainer Pension List");
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

		if (savedSearch == null) {
			savedSearch = new TeTrainersPensionSavedSearch();
		}
		HashMap<String, Object> trainers = trainersService
				.getTrainersPension(savedSearch);

		return trainers;
	}

	@RequestMapping(value = "/data/employeeList/{id}", method = RequestMethod.POST)
	public @ResponseBody
	HashMap<String, Object> getEmployeeData(
			@PathVariable(value = "id") Integer trainerId,
			@RequestBody(required = false) TeEmployeesSavedSearch savedSearch,
			Authentication authentication) {

		logger.info("Data Emp List");
		System.out.println(trainerId);
		
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

		if (savedSearch == null) {
			savedSearch = new TeEmployeesSavedSearch();
		}
		HashMap<String, Object> employees = trainersService.getEmployees(
				trainerId, savedSearch);

		return employees;
	}

	@RequestMapping(value = "/data/employeePensionList/{id}", method = RequestMethod.POST)
	public @ResponseBody
	HashMap<String, Object> getEmployeePensionData(
			@PathVariable(value = "id") Integer trainerId,
			@RequestBody(required = false) TeEmployeesPensionSavedSearch savedSearch,
			Authentication authentication) {

		logger.info("Data Emp List Pension");
		System.out.println(trainerId);
		
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

		if (savedSearch == null) {
			savedSearch = new TeEmployeesPensionSavedSearch();
		}
		HashMap<String, Object> employees = trainersService
				.getEmployeesPension(trainerId, savedSearch);

		return employees;
	}

	@RequestMapping(value = "/data/employeeList", method = RequestMethod.POST)
	public @ResponseBody
	HashMap<String, Object> getEmployeeData(
			@RequestBody(required = false) TeEmployeesSavedSearch savedSearch,
			Authentication authentication) {

		logger.info("Data Emp List");
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

		if (savedSearch == null) {
			savedSearch = new TeEmployeesSavedSearch();
		}
		HashMap<String, Object> employees = trainersService.getEmployees(0,
				savedSearch);

		return employees;
	}

	@RequestMapping(value = "/data/authRepList/{id}", method = RequestMethod.POST)
	public @ResponseBody
	HashMap<String, Object> getAuthRepData(
			@PathVariable(value = "id") Integer trainerId,
			@RequestBody(required = false) TeAuthRepsSavedSearches savedSearch,
			Authentication authentication) {

		logger.info("Data Auth Reps");
		System.out.println(trainerId);
		
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

		if (savedSearch != null) {
			savedSearch = new TeAuthRepsSavedSearches();
		}

		HashMap<String, Object> reps = trainersService.getAuthorisedReps(
				trainerId, savedSearch);

		return reps;
	}

	@RequestMapping(value = "/data/authRepList", method = RequestMethod.POST)
	public @ResponseBody
	HashMap<String, Object> getAuthRepData(
			@RequestBody(required = false) TeAuthRepsSavedSearches savedSearch,
			Authentication authentication) {

		logger.info("Data Auth Reps");
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

		if (savedSearch == null) {
			savedSearch = new TeAuthRepsSavedSearches();
		}
		HashMap<String, Object> reps = trainersService.getAuthorisedReps(0,
				savedSearch);

		return reps;
	}

	@RequestMapping(value = "/select/trainers", method = RequestMethod.GET)
	public @ResponseBody
	List<HashMap<String, String>> getTrainers(
			@RequestParam("chars") String chars) {
		// System.out.println(foo);
		logger.info("Select Trainers");
		List<HashMap<String, String>> trainers = trainersService
				.getTrainers(chars.toLowerCase().replaceAll("'", "''"));
		return trainers;
	}

	@RequestMapping(value = "/select/employees", method = RequestMethod.GET)
	public @ResponseBody
	List<HashMap<String, String>> getEmployees(
			@RequestParam("chars") String chars) {
		// System.out.println(foo);
		logger.info("Select Employees");
		List<HashMap<String, String>> employees = trainersService
				.getEmployees(chars.toLowerCase().replaceAll("'", "''"));
		return employees;
	}

	@RequestMapping(value = "/save/pensionTrainersSearch", method = RequestMethod.POST)
	public @ResponseBody
	List<Object> saveSearch(
			@RequestBody(required = false) TeTrainersPensionSavedSearch savedSearch,
			Authentication authentication) {
		logger.info("Save Search");
		System.out.println("SAVE SEARCH:" + (savedSearch == null));
		Object principal = authentication.getPrincipal();
		User user = (User) principal;
		savedSearch.setSavedSearchUserId(user.getId());
		trainersService.saveTrainerPensionSavedSearches(savedSearch);

		// getTrainersPensionS

		return trainersService.getTrainersPensionSavedSearches(user.getId());
	}

	@RequestMapping(value = "/save/comments", method = RequestMethod.POST)
	public @ResponseBody
	HashMap<String, String> saveTrainerComments(
			@RequestBody(required = true) TeTrainers trainerWithCommentsToSave,
			Authentication authentication) {
		logger.info("Save Trainer Comments");
		HashMap<String, String> map = new HashMap<String, String>();
		trainersService.updateTrainerComments(trainerWithCommentsToSave);
		map.put("message", "Updated record successfully");
		return map;
	}

	@RequestMapping(value = "/save/verifiedStatus", method = RequestMethod.POST)
	public @ResponseBody
	TeTrainers saveVerfiedStatus(
			@RequestBody(required = true) TeTrainers trainerWithStatusToSave) {

		logger.info("Save Verified Status");

		return trainersService
				.updateTrainerVerifiedStatus(trainerWithStatusToSave);
	}

	@RequestMapping(value = "/update/employeeVerified/{id}", method = RequestMethod.POST)
	public @ResponseBody
	TeEmployees updateEmployeeVerified(
			@PathVariable(value = "id") Integer trainerId,
			@RequestBody(required = true) TeEmployees employee) {

		logger.info("Change Employee Verified");

		return trainersService.updateEmployeeHistoryVerifiedStatus(employee,
				trainerId);
	}

	@RequestMapping(value = "/update/trainerVerified/{id}", method = RequestMethod.POST)
	public @ResponseBody
	Boolean updateTrainerVerified(
			@PathVariable(value = "id") Integer trainerId,
			@RequestBody(required = false) Boolean verified) throws Exception {

		if (verified != null) {

			logger.info("Change Trainer Verified" + verified);
			TeTrainers trainer = trainersService.getTrainer(trainerId);
			if (verified) {
				trainer.setTrainerVerifiedStatus(VerifiedStatus.VERIFIED);
				trainersService.updateTrainer(trainer);
				return true;
			} else {
				trainer.setTrainerVerifiedStatus(VerifiedStatus.NOTVERIFIED);
				trainersService.updateTrainer(trainer);
				return false;
			}

		} else {
			logger.info("Get Trainer Verified");
			TeTrainers trainer = trainersService.getTrainer(trainerId);
			if (trainer.getTrainerVerifiedStatus() == VerifiedStatus.VERIFIED) {

				return true;
			} else {

				return false;
			}
		}

	}

	@RequestMapping(value = "/trainerPensionList", method = RequestMethod.POST)
	public @ResponseBody
	JsonTable getPensionTrainerList(Authentication authentication) {

		logger.info("Trainer Pension List");
		Object principal = authentication.getPrincipal();
		User user = (User) principal;

		JsonTableColumn column;
		JsonColumnSearchConfig searchConfig;
		Select2Directive select2;

		JsonTable table = new JsonTable();
		table.setTitle("Trainers");
		table.setDataUrl("/turfclubPrograms/trainers/service/data/trainerPensionList");
		// set the pagination options
		table.getPageSizeOptions().add(25);
		table.getPageSizeOptions().add(50);
		table.getPageSizeOptions().add(100);
		table.setSavedSearch(new TeTrainersPensionSavedSearch());

		// set the save search config
		JsonSavedSearchConfig savedSearchConfig = new JsonSavedSearchConfig();

		savedSearchConfig
				.setSaveUrl("/turfclubPrograms/trainers/service/save/pensionTrainersSearch");
		// inspectionsService.getSaveSearches();
		// getTrainersPensionS
		savedSearchConfig.setSavedSearches(trainersService
				.getTrainersPensionSavedSearches(user.getId()));

		table.setSavedSearchConfig(savedSearchConfig);

		// Trainers ID Column
		column = new JsonTableColumn();
		column.setTitle("Id");
		column.setColumnWidth("1");
		SpanDirective spanDirective = new SpanDirective();
		spanDirective.setKey("trainerId");
		spanDirective.setSplitAfter(1);
		column.setDirective(spanDirective);
		table.getColumns().add(column);

		// Trainer Name Column
		column = new JsonTableColumn();
		column.setTitle("Name");
		column.setColumnWidth("20");
		spanDirective = new SpanDirective();
		spanDirective.setKey("trainerFullName");
		spanDirective.setSplitAfter(2);
		spanDirective.setSplitter(", ");
		column.setDirective(spanDirective);
		searchConfig = new JsonColumnSearchConfig();

		select2 = new Select2Directive();
		select2.setIdKey("trainersSearch.trainerId.trainerId");
		select2.setKey("trainersSearch.trainerId.trainerSurname");
		select2.setSearchUrl("/turfclubPrograms/trainers/service/select/trainers");
		select2.setLoadOnInitOnly(false);
		select2.setRequired(false);
		searchConfig.setDirective(select2);
		column.setSearchConfig(searchConfig);
		column.setSortable(true);
		table.getColumns().add(column);

		// Trainers Contact
		column = new JsonTableColumn();
		column.setTitle("Trainer Contact Details");
		column.setColumnWidth("5");
		ContactDetailsDirective contactDetailsDirective = new ContactDetailsDirective();
		contactDetailsDirective.setEmailKey("trainerEmail");
		contactDetailsDirective.setFaxKey("trainerFax");
		contactDetailsDirective.setMobileKey("trainerMobilePhone");
		contactDetailsDirective.setPhoneKey("trainerHomePhone");
		contactDetailsDirective.setExtraTitle("Completed By:");
		contactDetailsDirective
				.setExtraKey("trainerContactName trainerContactPhone");
		contactDetailsDirective.setExtraKeySplitter(" : ");
		column.setDirective(contactDetailsDirective);
		table.getColumns().add(column);

		// Date Complete
		column = new JsonTableColumn();
		column.setTitle("Date Completed");
		column.setColumnWidth("1");
		spanDirective = new SpanDirective();
		spanDirective.setDirectiveType(DirectiveTypes.DATESPAN);
		spanDirective.setKey("trainerDateCompleted");
		
		column.setSortable(true);
		column.setDirective(spanDirective);
		table.getColumns().add(column);

		// Comments
		column = new JsonTableColumn();
		column.setTitle("Comments");
		column.setColumnWidth("12");
		CommentBoxWithEnumToggleDirective commentBoxEnumDir = new CommentBoxWithEnumToggleDirective();
		commentBoxEnumDir.setKey("trainerNotes");
		commentBoxEnumDir
				.setSaveUrl("/turfclubPrograms/trainers/service/save/comments");
		commentBoxEnumDir.setToggleButtonKey("trainerVerifiedStatus");
		commentBoxEnumDir
				.setToggleButtonUrl("/turfclubPrograms/trainers/service/save/verifiedStatus");
		commentBoxEnumDir.setToggleButtonOffValue("NOTVERIFIED");
		commentBoxEnumDir.setToggleButtonOnValue("PENDING");
		commentBoxEnumDir.setToggleButtonTitle("Pending");
		column.setDirective(commentBoxEnumDir);
		table.getColumns().add(column);

		// Trainers
		column = new JsonTableColumn();
		column.setTitle("Verified");
		column.setColumnWidth("1");
		EnumToImageDirective enumToImageDirective = new EnumToImageDirective();
		enumToImageDirective.setKey("trainerVerifiedStatus");

		enumToImageDirective.getEnumToImageMap().put("NOTVERIFIED",
				"../resources/img/redx.png");
		enumToImageDirective.getEnumToImageMap().put("VERIFIED",
				"../resources/img/greentick.png");
		enumToImageDirective.getEnumToImageMap().put("PENDING",
				"../resources/img/question-mark.png");
		column.setDirective(enumToImageDirective);
		searchConfig = new JsonColumnSearchConfig();
		SelectEnumImageDirective emumImageSelectDirective = new SelectEnumImageDirective();
		emumImageSelectDirective.setKey("verified");
		emumImageSelectDirective.setPlaceHolder("Select");

		ImageUrlWithTextAndKey imageUrl = new ImageUrlWithTextAndKey();
		imageUrl.setImageUrl("../resources/img/redx.png");
		imageUrl.setKey("NOTVERIFIED");
		imageUrl.setTitle("Not Verified");
		emumImageSelectDirective.getImageUrls().add(imageUrl);
		imageUrl = new ImageUrlWithTextAndKey();
		imageUrl.setImageUrl("../resources/img/greentick.png");
		imageUrl.setKey("VERIFIED");
		imageUrl.setTitle("Verified");
		emumImageSelectDirective.getImageUrls().add(imageUrl);
		imageUrl = new ImageUrlWithTextAndKey();
		imageUrl.setImageUrl("../resources/img/question-mark.png");
		imageUrl.setKey("PENDING");
		imageUrl.setTitle("Pending");
		emumImageSelectDirective.getImageUrls().add(imageUrl);
		searchConfig.setDirective(emumImageSelectDirective);
		column.setSearchConfig(searchConfig);
		column.setSortable(true);

		table.getColumns().add(column);

		// Trainers Complete
		column = new JsonTableColumn();
		column.setTitle("Return Complete");
		column.setColumnWidth("1");
		TrueFalseTickDirective tickDirective = new TrueFalseTickDirective();
		tickDirective.setKey("trainerReturnComplete");
		searchConfig = new JsonColumnSearchConfig();
		SelectBooleanDirective booleanDirective = new SelectBooleanDirective();
		booleanDirective = new SelectBooleanDirective();
		booleanDirective.setKey("returnCompleteSearch");
		booleanDirective.setPlaceHolder("Select");
		booleanDirective.setTrueText("Complete");
		booleanDirective.setFalseText("Incomplete");
		searchConfig.setDirective(booleanDirective);
		column.setSearchConfig(searchConfig);
		column.setSortable(true);
		column.setDirective(tickDirective);
		table.getColumns().add(column);

		// download docs
		column = new JsonTableColumn();
		column.setTitle("Documents Attached");
		column.setColumnWidth("1");
		searchConfig = new JsonColumnSearchConfig();
		booleanDirective = new SelectBooleanDirective();
		booleanDirective.setKey("documentsAttached");
		booleanDirective.setPlaceHolder("Select");
		booleanDirective.setTrueText("Attached");
		booleanDirective.setFalseText("Not Attached");
		searchConfig.setDirective(booleanDirective);
		column.setSearchConfig(searchConfig);
		column.setSortable(true);
		tickDirective = new TrueFalseTickDirective();
		tickDirective.setKey("hasDocuments");

		column.setDirective(tickDirective);
		table.getColumns().add(column);

		// list employees button
		column = new JsonTableColumn();
		column.setTitle("Actions");
		column.setColumnWidth("1");
		MultiButtonDirective multiButtonDir = new MultiButtonDirective();
		ButtonDirective buttonDir = new ButtonDirective();
		buttonDir = new ButtonDirective();
		buttonDir.setKey("trainerId");
		buttonDir.setText("Attach");
		buttonDir.setButtonClass("btn btn-warning btn-block");
		buttonDir.setButtonDisabledClass("btn btn-default btn-block");
		buttonDir.setButtonGlyphiconClass("glyphicon glyphicon-upload");
		buttonDir.setEnabledDisabledKey("canEdit");
		UploadModal uploadModal = new UploadModal();
		uploadModal.setUploadTitle("Upload A Document for Trainer ");
		uploadModal
				.setUploadUrl("/turfclubPrograms/trainersEmployeesOnline/upload/");
		buttonDir.setFunctionType(FunctionTypes.MODAL);
		buttonDir.setModal(uploadModal);
		multiButtonDir.getButtons().add(buttonDir);
		buttonDir = new ButtonDirective();
		buttonDir.setKey("trainerId");
		buttonDir.setText("Download");
		buttonDir.setButtonClass("btn btn-info btn-block");
		buttonDir.setButtonDisabledClass("btn btn-default btn-block");
		buttonDir.setButtonGlyphiconClass("glyphicon glyphicon-download-alt");
		buttonDir.setUrl("/turfclubPrograms/trainers/service/download/p60s/");
		buttonDir.setEnabledDisabledKey("hasDocuments");
		buttonDir.setFunctionType(FunctionTypes.EXTERNALURLNEWTAB);
		multiButtonDir.getButtons().add(buttonDir);
		// list employees button
		buttonDir = new ButtonDirective();
		buttonDir.setKey("trainerId");
		buttonDir.setText("Show Employees");
		buttonDir.setButtonClass("btn btn-info btn-block");
		buttonDir.setButtonDisabledClass("btn btn-default btn-block");
		buttonDir.setButtonGlyphiconClass("glyphicon glyphicon-th-list");
		buttonDir.setUrl("/employeePensionList/");
		buttonDir.setEnabledDisabledKey("canEdit");
		multiButtonDir.getButtons().add(buttonDir);
		// complete trainer return on behalf
		buttonDir = new ButtonDirective();
		buttonDir.setKey("trainerId");
		buttonDir.setText("Edit Return");
		buttonDir.setButtonClass("btn btn-info btn-block");
		buttonDir.setButtonDisabledClass("btn btn-default btn-block");
		buttonDir.setButtonGlyphiconClass("glyphicon glyphicon-th-list");
		buttonDir.setUrl("/turfclubPrograms/trainersEmployeesOnline/main/");
		buttonDir.setFunctionType(FunctionTypes.EXTERNALURL);
		buttonDir.setEnabledDisabledKey("canEdit");
		multiButtonDir.getButtons().add(buttonDir);
		column.setDirective(multiButtonDir);
		table.getColumns().add(column);
		table.setLastSearchPrefix("trainers-pension");
		return table;
	}

	@RequestMapping(value = "/employeePensionList", method = RequestMethod.POST)
	public @ResponseBody
	JsonTable getEmployeePensionList() {

		logger.info("Employee Pension List");
		JsonTableColumn column;
		JsonColumnSearchConfig searchConfig;
		Select2Directive select2;
		JsonTable table = new JsonTable();
		table.setTitle("Employees");
		table.setDataUrl("/turfclubPrograms/trainers/service/data/employeePensionList");
		// set the pagination options
		table.getPageSizeOptions().add(25);
		table.getPageSizeOptions().add(50);
		table.getPageSizeOptions().add(100);
		table.setSavedSearch(new TeEmployeesPensionSavedSearch());

		// Employee ID Column
		column = new JsonTableColumn();
		column.setTitle("Id");
		column.setColumnWidth("1");
		SpanDirective spanDirective = new SpanDirective();
		spanDirective.setKey("employeesEmployeeId");
		spanDirective.setSplitAfter(1);
		column.setDirective(spanDirective);
		table.getColumns().add(column);

		// Employee Name Column
		column = new JsonTableColumn();
		column.setTitle("Name");
		column.setColumnWidth("20");
		spanDirective = new SpanDirective();
		spanDirective.setKey("employeesFullName");
		spanDirective.setSplitAfter(2);
		spanDirective.setSplitter(", ");
		column.setDirective(spanDirective);
		searchConfig = new JsonColumnSearchConfig();
		select2 = new Select2Directive();
		select2.setIdKey("employeesSearch.employeesEmployeeId.employeesEmployeeId");
		select2.setKey("employeesSearch.employeesEmployeeId.employeesFullName");
		select2.setSearchUrl("/turfclubPrograms/trainers/service/select/employees");
		select2.setLoadOnInitOnly(false);
		select2.setRequired(false);
		searchConfig.setDirective(select2);
		column.setSearchConfig(searchConfig);
		column.setSortable(true);
		table.getColumns().add(column);
		// Employee Address
		column = new JsonTableColumn();
		column.setTitle("Address");
		column.setColumnWidth("5");
		// column.setSortable(true);
		spanDirective = new SpanDirective();
		spanDirective
				.setKey("employeesAddress1 employeesAddress2 employeesAddress3 employeesAddress4 employeesAddress5 employeesPostCode");
		spanDirective.setSplitAfter(1);
		spanDirective.setSplitter(", ");
		column.setDirective(spanDirective);
		table.getColumns().add(column);

		// Employee Contact
		column = new JsonTableColumn();
		column.setTitle("Phone");
		column.setColumnWidth("5");
		ContactDetailsDirective contactDetailsDirective = new ContactDetailsDirective();
		contactDetailsDirective.setEmailKey("employeesEmail");
		contactDetailsDirective.setMobileKey("employeesMobileNo");
		contactDetailsDirective.setPhoneKey("employeesPhoneNo");
		column.setDirective(contactDetailsDirective);
		table.getColumns().add(column);

		// Employee Earnings
		column = new JsonTableColumn();
		Calendar now = Calendar.getInstance();
		// CHANGE EARNINGS YEAR HERE
		// Change here to -1 when earnings for 2015 should be entered
		now.add(Calendar.YEAR, -2);

		column.setTitle("Earnings " + (now.get(Calendar.YEAR)));

		column.setColumnWidth("5");
		// column.setSortable(true);
		spanDirective = new SpanDirective();
		spanDirective.setKey("employeesEarnings");
		spanDirective.setSplitAfter(1);
		spanDirective.setSplitter(", ");
		column.setDirective(spanDirective);
		table.getColumns().add(column);

		// EmployeePPS
		column = new JsonTableColumn();
		column.setTitle("PPS No.");
		column.setColumnWidth("5");
		// column.setSortable(true);
		spanDirective = new SpanDirective();
		spanDirective.setKey("employeesPps");
		spanDirective.setSplitAfter(1);
		spanDirective.setSplitter(", ");
		column.setDirective(spanDirective);
		table.getColumns().add(column);

		column = new JsonTableColumn();
		column.setTitle("Worked With Employer in Tax Year");
		column.setColumnWidth("1");
		TrueFalseTickDirective tickDirective = new TrueFalseTickDirective();
		tickDirective.setKey("employeeWorkedWithTrainerInTaxYear");
		column.setDirective(tickDirective);
		table.getColumns().add(column);

		// edit button
		// list employees button
		column = new JsonTableColumn();
		column.setTitle("Approve");
		column.setColumnWidth("1");
		ButtonDirective buttonDir = new ButtonDirective();
		buttonDir.setKey("employeesEmployeeId");
		buttonDir.setText("Approved");
		buttonDir.setButtonClass("btn btn-success");
		buttonDir.setButtonDisabledClass("btn btn-default");
		buttonDir.setButtonGlyphiconClass("glyphicon glyphicon-ok");
		buttonDir.setButtonOffClass("btn btn-danger");
		buttonDir.setButtonOffGlyphiconClass("glyphicon glyphicon-remove");
		buttonDir.setOffText("Approve");
		buttonDir.setOnOffKey("employeeVerified");
		buttonDir.setEnabledDisabledKey("canEdit");
		buttonDir.setFunctionType(FunctionTypes.ONOFF);
		buttonDir
				.setUrl("/turfclubPrograms/trainers/service/update/employeeVerified");
		buttonDir.setOnOffUseRouteParam(true);
		column.setDirective(buttonDir);
		table.getColumns().add(column);
		table.setNoDataTemplateUrl("/turfclubPrograms/resources/js/angularjs/trainers/templates/no-employees-pension.html");
		// table.setLastSearchPrefix("employees-pension");
		return table;
	}

	@RequestMapping(value = "/trainerList", method = RequestMethod.POST)
	public @ResponseBody
	JsonTable getTrainerList() {

		logger.info("Trainer List");
		JsonTableColumn column;
		JsonColumnSearchConfig searchConfig;
		Select2Directive select2;
		DateDisplayDirective dateDirective;
		JsonTable table = new JsonTable();
		table.setTitle("Trainers");
		table.setDataUrl("/turfclubPrograms/trainers/service/data/trainerList");
		// set the pagination options
		table.getPageSizeOptions().add(25);
		table.getPageSizeOptions().add(50);
		table.getPageSizeOptions().add(100);
		table.setSavedSearch(new TeTrainersSavedSearch());

		// Trainers ID Column
		column = new JsonTableColumn();
		column.setTitle("Id");
		column.setColumnWidth("1");
		SpanDirective spanDirective = new SpanDirective();
		spanDirective.setKey("trainerId");
		spanDirective.setSplitAfter(1);
		column.setDirective(spanDirective);
		table.getColumns().add(column);

		// Trainer Name Column
		column = new JsonTableColumn();
		column.setTitle("Name");
		column.setColumnWidth("20");
		spanDirective = new SpanDirective();
		spanDirective.setKey("trainerFullName");
		spanDirective.setSplitAfter(2);
		spanDirective.setSplitter(", ");
		column.setDirective(spanDirective);
		searchConfig = new JsonColumnSearchConfig();
		select2 = new Select2Directive();
		select2.setIdKey("trainersSearch.trainerId.trainerId");
		select2.setKey("trainersSearch.trainerId.trainerSurname");
		select2.setSearchUrl("/turfclubPrograms/trainers/service/select/trainers");
		select2.setLoadOnInitOnly(false);
		select2.setRequired(false);
		searchConfig.setDirective(select2);
		column.setSearchConfig(searchConfig);
		column.setSortable(true);
		table.getColumns().add(column);

		// Trainers Address
		column = new JsonTableColumn();
		column.setTitle("Address");
		column.setColumnWidth("5");
		spanDirective = new SpanDirective();
		spanDirective.setKey("trainerAddress1 trainerAddress2 trainerAddress3");
		spanDirective.setSplitAfter(1);
		spanDirective.setSplitter(", ");
		column.setDirective(spanDirective);
		table.getColumns().add(column);

		// Trainers Phone Fax
		column = new JsonTableColumn();
		column.setTitle("Phone");
		column.setColumnWidth("5");
		ContactDetailsDirective contactDetailsDirective = new ContactDetailsDirective();
		contactDetailsDirective.setEmailKey("trainerEmail");
		contactDetailsDirective.setFaxKey("trainerFax");
		contactDetailsDirective.setMobileKey("trainerMobilePhone");
		contactDetailsDirective.setPhoneKey("trainerHomePhone");
		column.setDirective(contactDetailsDirective);
		table.getColumns().add(column);

		// list employees button
		column = new JsonTableColumn();
		column.setTitle("Employees");
		column.setColumnWidth("1");
		ButtonDirective buttonDir = new ButtonDirective();
		buttonDir.setKey("trainerId");
		buttonDir.setText("Show Employees");
		buttonDir.setButtonClass("btn btn-info");
		buttonDir.setButtonDisabledClass("btn btn-default");
		buttonDir.setButtonGlyphiconClass("glyphicon glyphicon-th-list");
		buttonDir.setUrl("/employeelist/");
		buttonDir.setEnabledDisabledKey("canEdit");
		column.setDirective(buttonDir);
		table.getColumns().add(column);

		// list reps button
		column = new JsonTableColumn();
		column.setTitle("Authorised Reps");
		column.setColumnWidth("1");
		buttonDir = new ButtonDirective();
		buttonDir.setKey("trainerId");
		buttonDir.setText("Show Auth. Reps.");
		buttonDir.setButtonClass("btn btn-warning");
		buttonDir.setButtonDisabledClass("btn btn-default");
		buttonDir.setButtonGlyphiconClass("glyphicon glyphicon-th-list");
		buttonDir.setUrl("/authreplist/");
		buttonDir.setEnabledDisabledKey("canEdit");
		column.setDirective(buttonDir);
		table.getColumns().add(column);

		// edit button
		column = new JsonTableColumn();
		column.setTitle("Edit");
		column.setColumnWidth("1");
		buttonDir = new ButtonDirective();
		buttonDir.setKey("trainerId");
		buttonDir.setText("Edit");
		buttonDir.setButtonClass("btn btn-success");
		buttonDir.setButtonDisabledClass("btn btn-default");
		buttonDir.setButtonGlyphiconClass("glyphicon glyphicon-pencil");
		buttonDir.setUrl("/trainerEdit/");
		buttonDir.setEnabledDisabledKey("canEdit");
		column.setDirective(buttonDir);
		table.getColumns().add(column);
		table.setLastSearchPrefix("trainers");

		return table;
	}

	@RequestMapping(value = "/authRepList", method = RequestMethod.POST)
	public @ResponseBody
	JsonTable getAuthRepList() {

		logger.info("Auth Rep List");
		JsonTableColumn column;
		JsonColumnSearchConfig searchConfig;
		Select2Directive select2;

		JsonTable table = new JsonTable();
		table.setTitle("Authorised Reps");
		table.setDataUrl("/turfclubPrograms/trainers/service/data/authRepList");
		// set the pagination options
		table.getPageSizeOptions().add(25);
		table.getPageSizeOptions().add(50);
		table.getPageSizeOptions().add(100);
		table.setSavedSearch(new TeAuthRepsSavedSearches());

		// Rep ID Column
		column = new JsonTableColumn();
		column.setTitle("Id");
		column.setColumnWidth("1");
		SpanDirective spanDirective = new SpanDirective();
		spanDirective.setKey("authrepsId");
		spanDirective.setSplitAfter(1);
		column.setDirective(spanDirective);
		table.getColumns().add(column);

		// Rep Name Column
		column = new JsonTableColumn();
		column.setTitle("Authorised Rep. Name");
		column.setColumnWidth("20");
		spanDirective = new SpanDirective();
		spanDirective.setKey("authrepsName");
		spanDirective.setSplitAfter(2);
		spanDirective.setSplitter(", ");
		column.setDirective(spanDirective);
		searchConfig = new JsonColumnSearchConfig();
		select2 = new Select2Directive();
		select2.setIdKey("trainersSearch.trainerId.trainerId");
		select2.setKey("trainersSearch.trainerId.trainerSurname");
		select2.setSearchUrl("/turfclubPrograms/trainers/service/select/trainers");
		select2.setLoadOnInitOnly(false);
		select2.setRequired(false);
		searchConfig.setDirective(select2);
		column.setSearchConfig(searchConfig);
		column.setSortable(true);
		table.getColumns().add(column);

		// Rep Name Column
		column = new JsonTableColumn();
		column.setTitle("Trainer Name");
		column.setColumnWidth("20");
		spanDirective = new SpanDirective();
		spanDirective
				.setKey("authrepsTrainerId.trainerSurname trainerFirstName");
		spanDirective.setSplitAfter(2);
		spanDirective.setSplitter(" ");
		column.setDirective(spanDirective);
		searchConfig = new JsonColumnSearchConfig();
		select2 = new Select2Directive();
		select2.setIdKey("trainersSearch.trainerId.trainerId");
		select2.setKey("trainersSearch.trainerId.trainerSurname");
		select2.setSearchUrl("/turfclubPrograms/trainers/service/select/trainers");
		select2.setLoadOnInitOnly(false);
		select2.setRequired(false);
		searchConfig.setDirective(select2);
		column.setSearchConfig(searchConfig);
		column.setSortable(true);
		table.getColumns().add(column);

		// edit button
		column = new JsonTableColumn();
		column.setTitle("Edit");
		column.setColumnWidth("1");
		ButtonDirective buttonDir = new ButtonDirective();
		buttonDir.setKey("authrepsId");
		buttonDir.setText("Edit");
		buttonDir.setButtonClass("btn btn-success");
		buttonDir.setButtonDisabledClass("btn btn-default");
		buttonDir.setButtonGlyphiconClass("glyphicon glyphicon-pencil");
		buttonDir.setUrl("/authrepEdit/");
		buttonDir.setEnabledDisabledKey("canEdit");
		column.setDirective(buttonDir);
		table.getColumns().add(column);

		// delete button
		column = new JsonTableColumn();
		column.setTitle("Delete");
		column.setColumnWidth("1");
		buttonDir = new ButtonDirective();
		buttonDir.setKey("authrepsId");
		buttonDir.setText("Delete");
		buttonDir.setButtonClass("btn btn-danger");
		buttonDir.setButtonDisabledClass("btn btn-default");
		buttonDir.setButtonGlyphiconClass("glyphicon glyphicon-remove");
		buttonDir.setUrl("/authrepDelete/");
		buttonDir.setEnabledDisabledKey("canEdit");
		column.setDirective(buttonDir);
		table.getColumns().add(column);

		return table;
	}

	@RequestMapping(value = "/employeeList", method = RequestMethod.POST)
	public @ResponseBody
	JsonTable getEmployeeList() {

		logger.info("Employee List");
		JsonTableColumn column;
		JsonColumnSearchConfig searchConfig;
		Select2Directive select2;
		DateDisplayDirective dateDirective;
		JsonTable table = new JsonTable();
		table.setTitle("Employees");
		table.setDataUrl("/turfclubPrograms/trainers/service/data/employeeList");
		// set the pagination options
		table.getPageSizeOptions().add(25);
		table.getPageSizeOptions().add(50);
		table.getPageSizeOptions().add(100);
		table.setSavedSearch(new TeEmployeesSavedSearch());

		// Employee ID Column
		column = new JsonTableColumn();
		column.setTitle("Id");
		column.setColumnWidth("1");
		SpanDirective spanDirective = new SpanDirective();
		spanDirective.setKey("employeesEmployeeId");
		spanDirective.setSplitAfter(1);
		column.setDirective(spanDirective);
		table.getColumns().add(column);

		// Employee Name Column
		column = new JsonTableColumn();
		column.setTitle("Name");
		column.setColumnWidth("20");
		spanDirective = new SpanDirective();
		spanDirective.setKey("employeesFullName");
		spanDirective.setSplitAfter(2);
		spanDirective.setSplitter(", ");
		column.setDirective(spanDirective);
		searchConfig = new JsonColumnSearchConfig();
		select2 = new Select2Directive();
		select2.setIdKey("employeesSearch.employeesEmployeeId.employeesEmployeeId");
		select2.setKey("employeesSearch.employeesEmployeeId.employeesFullName");
		select2.setSearchUrl("/turfclubPrograms/trainers/service/select/employees");
		select2.setLoadOnInitOnly(false);
		select2.setRequired(false);
		searchConfig.setDirective(select2);
		column.setSearchConfig(searchConfig);
		column.setSortable(true);
		table.getColumns().add(column);
		// Trainers Address
		column = new JsonTableColumn();
		column.setTitle("Address");
		column.setColumnWidth("5");
		column.setSortable(true);
		spanDirective = new SpanDirective();
		spanDirective
				.setKey("employeesAddress1 employeesAddress2 employeesAddress3 employeesAddress4 employeesAddress5 employeesPostCode");
		spanDirective.setSplitAfter(1);
		spanDirective.setSplitter(", ");
		column.setDirective(spanDirective);
		table.getColumns().add(column);

		// Trainers Phone Fax
		column = new JsonTableColumn();
		column.setTitle("Phone");
		column.setColumnWidth("5");
		ContactDetailsDirective contactDetailsDirective = new ContactDetailsDirective();
		contactDetailsDirective.setEmailKey("employeesEmail");
		contactDetailsDirective.setMobileKey("employeesMobileNo");
		contactDetailsDirective.setPhoneKey("employeesPhoneNo");
		column.setDirective(contactDetailsDirective);
		table.getColumns().add(column);

		// edit button
		// list employees button
		column = new JsonTableColumn();
		column.setTitle("Edit");
		column.setColumnWidth("1");
		ButtonDirective buttonDir = new ButtonDirective();
		buttonDir.setKey("employeesEmployeeId");
		buttonDir.setText("Edit");
		buttonDir.setButtonClass("btn btn-success");
		buttonDir.setButtonDisabledClass("btn btn-default");
		buttonDir.setButtonGlyphiconClass("glyphicon glyphicon-pencil");
		buttonDir.setUrl("/employeeEdit/");
		buttonDir.setEnabledDisabledKey("canEdit");
		column.setDirective(buttonDir);
		table.getColumns().add(column);

		return table;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public @ResponseBody
	JsonEdit editInspections(
			@RequestParam(value = "id", required = false) Integer id,
			Authentication authentication) {
		logger.info("Edit");
		InspectionsInspections inspection;
		if (id == null) {
			System.out.println("Null");
			inspection = new InspectionsInspections();

		} else {
			// inspection = inspectionsService.getInspection(id);
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
		// edit.setData(inspection);
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
		input.setPlaceHolder("Address Line 1 (Not Required)");
		input.setRequired(false);
		field.setDirective(input);
		edit.getFormFields().add(field);

		field = new JsonFormFields();
		field.setFieldLabel("Address Line 2");
		input = new InputDirective();
		input.setKey("inspectionsAddress2");
		input.setPlaceHolder("Address Line 2");
		input.setRequiredMessage("Address Line 2 required");
		field.setDirective(input);
		edit.getFormFields().add(field);

		field = new JsonFormFields();
		field.setFieldLabel("Address Line 3");
		input = new InputDirective();
		input.setKey("inspectionsAddress3");
		input.setPlaceHolder("Address Line 3");
		input.setRequiredMessage("Address Line 3 required");
		field.setDirective(input);
		edit.getFormFields().add(field);

		field = new JsonFormFields();
		field.setFieldLabel("Address Line 4");
		input = new InputDirective();
		input.setKey("inspectionsAddress4");
		input.setPlaceHolder("Address Line 4");
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

		return edit;
	}

	@RequestMapping(value = "/download/p60s/{id}", method = RequestMethod.GET)
	@ResponseBody
	public FileSystemResource downloadP60s(
			@PathVariable(value = "id") Integer trainerId,
			HttpServletResponse response) {

		List<TeFile> files = trainersService
				.getTrainersPensionFileNames(trainerId);

		//

		boolean merged = false;
		int mergeCount = 0;
		int pdfFileCount = 0;
		TeFile mergedFile = null;

		for (TeFile file : files) {

			System.out.println(file.getNewFilename());
			if (file.getUploadType() == UploadType.UPLOADED
					&& !file.getNewFilename().endsWith("pdf")) {

				System.out.println("FileName" + file.getNewFilename());
				String[] splitFileName = (file.getNewFilename()).split("\\.");

				TeFile pdffile = new TeFile();
				pdffile.setNewFilename(splitFileName[0] + ".pdf");

				System.out.println("Find file: " + pdffile.getNewFilename()
						+ " " + !files.contains(pdffile));
				if (!files.contains(pdffile)) {
					System.out.println("Convert File");
					PDFConvertAndMerge.convertFile(file, fileDirectory);

					// if the file is not a pdf set the thumbnail preview
					String thumbnailFilename = "";
					File thumbnailFile = null;
					File pdfFileNew = null;
					pdfFileNew = new File(fileDirectory
							+ pdffile.getNewFilename());

					thumbnailFilename = "pdfpreview" + "-thumbnail.png";
					thumbnailFile = new File(fileDirectory + "/"
							+ thumbnailFilename);

					pdffile.setName(file.getName());
					pdffile.setThumbnailFilename(thumbnailFilename);

					pdffile.setContentType("application/pdf");
					pdffile.setSize(pdfFileNew.length());
					pdffile.setThumbnailSize(thumbnailFile.length());
					pdffile.setFileUserId(file.getFileUserId());
					pdffile.setMergeCount(1);
					pdffile.setUploadType(UploadType.CONVERTED);
					fileService.create(pdffile);
					pdfFileCount++;

				}

			} else if (file.getUploadType() == UploadType.MERGED) {
				mergeCount = file.getMergeCount();
				mergedFile = file;
			} else if (file.getUploadType() == UploadType.CONVERTED) {
				pdfFileCount++;
			} else {
				pdfFileCount++;
			}

		}

		// if there are more than 1 files and no merged file or mergefile
		// smaller than current merge all pdf files now
		System.out
				.println(files.size() + " " + pdfFileCount + " " + mergeCount);
		if (files.size() > 0 && pdfFileCount != mergeCount) {
			System.out.println("MERGE FILES HERE");
			List<TeFile> filesToMerge = new ArrayList<>();
			files = trainersService.getTrainersPensionFileNames(trainerId);
			for (TeFile file : files) {
				if (file.getContentType().equals("application/pdf")) {
					filesToMerge.add(file);
				}
			}

			TeFile newMergedFile = PDFConvertAndMerge.mergePdfFiles(
					filesToMerge, fileDirectory);
			fileService.create(newMergedFile);
			if (mergedFile != null) {
				fileService.delete(mergedFile);
			}
			response.setContentType("application/pdf");
			response.setHeader("Content-Disposition", "attachment; filename="
					+ newMergedFile.getNewFilename());
			return new FileSystemResource(fileDirectory
					+ newMergedFile.getNewFilename());
		} else if (mergeCount == pdfFileCount && mergedFile != null) {
			response.setContentType("application/pdf");
			response.setHeader("Content-Disposition", "attachment; filename="
					+ mergedFile.getNewFilename());
			return new FileSystemResource(fileDirectory
					+ mergedFile.getNewFilename());
		}

		else {
			response.setContentType("application/pdf");
			response.setHeader("Content-Disposition", "attachment; filename="
					+ files.get(0).getNewFilename());
			return new FileSystemResource(fileDirectory
					+ files.get(0).getNewFilename());
		}

	}

}
