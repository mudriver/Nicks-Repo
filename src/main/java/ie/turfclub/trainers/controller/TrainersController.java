package ie.turfclub.trainers.controller;

import ie.turfclub.common.bean.SearchByNameTrainerBean;
import ie.turfclub.common.bean.TrainerUserBean;
import ie.turfclub.main.model.login.User;
import ie.turfclub.main.service.downloads.DownloadService;
import ie.turfclub.main.service.downloads.TokenService;
import ie.turfclub.person.service.PersonService;
import ie.turfclub.trainers.model.TeEmployentHistory;
import ie.turfclub.trainers.model.TeTrainers;
import ie.turfclub.trainers.service.EmployeeService;
import ie.turfclub.trainers.service.StableStaffService;
import ie.turfclub.trainers.service.TrainersService;

import java.io.BufferedWriter;
import java.sql.SQLException;
import java.util.List;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lowagie.text.Document;
import com.lowagie.text.ImgTemplate;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

@Controller
@RequestMapping(value = "/trainers")
public class TrainersController {

	static Logger logger = LoggerFactory.getLogger(TrainersController.class);

	@Autowired
	StableStaffService stableStaffService;
	@Autowired
	private DownloadService downloadService;

	@Autowired
	private TokenService tokenService;

	@Autowired
	private EmployeeService employeeService;

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private TrainersService trainersService;

	@Autowired
	private PersonService personService;

	@RequestMapping(value = "/home", method = RequestMethod.GET)
	public String trainers(Model model, Authentication authentication) {

		Object principal = authentication.getPrincipal();
		User user = (User) principal;
		String role = "";
		for (GrantedAuthority roles : user.getAuthorities()) {

			if (roles.getAuthority().equals("STABLESTAFF_PENSION")) {
				model.addAttribute("USERMENUTYPE", "STABLESTAFF_PENSION");
			}
		}

		return "trainers";
	}

	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String getAddTrainerPage(Model model, Authentication authentication) {

		model.addAttribute("trainer", new TeTrainers());
		model.addAttribute("verifiedStatusEnum",
				trainersService.getVerifiedStatus());
		model.addAttribute("sexEnum", employeeService.getSexEnum());
		model.addAttribute("maritalEnum",
				employeeService.getMaritalStatusEnum());
		model.addAttribute("employmentCatEnum",
				employeeService.getEmploymentCategoryEnum());
		model.addAttribute("titlesEnum", employeeService.getTitlesEnum());
		model.addAttribute("countiesEnum", employeeService.getCountiesEnum());
		model.addAttribute("countriesEnum", employeeService.getCountriesEnum());
		model.addAttribute("cardTypeEnum", employeeService.getAllCardType());
		model.addAttribute("pensionEnum", employeeService.getPension());
		model.addAttribute("nationalityEnum",
				employeeService.getNationalityEnum());
		return "trainer-add";
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String handleTrainerPage(TeTrainers trainer, Model model)
			throws SQLException {

		if (trainer.getTrainerId() != null && trainer.getTrainerId() > 0)
			model.addAttribute("success", messageSource.getMessage(
					"success.updated.trainer", new String[] {}, Locale.US));
		else
			model.addAttribute("success", messageSource.getMessage(
					"success.added.trainer", new String[] {}, Locale.US));

		trainersService.saveOrUpdate(trainer);
		model.addAttribute("trainer", trainer);
		model.addAttribute("verifiedStatusEnum",
				trainersService.getVerifiedStatus());
		model.addAttribute("sexEnum", employeeService.getSexEnum());
		model.addAttribute("maritalEnum",
				employeeService.getMaritalStatusEnum());
		model.addAttribute("employmentCatEnum",
				employeeService.getEmploymentCategoryEnum());
		model.addAttribute("titlesEnum", employeeService.getTitlesEnum());
		model.addAttribute("countiesEnum", employeeService.getCountiesEnum());
		model.addAttribute("countriesEnum", employeeService.getCountriesEnum());
		model.addAttribute("cardTypeEnum", employeeService.getAllCardType());
		model.addAttribute("pensionEnum", employeeService.getPension());
		model.addAttribute("nationalityEnum",
				employeeService.getNationalityEnum());
		return "trainer-add";
	}

	@RequestMapping(value = "/person/copyRecord", method = RequestMethod.GET)
	@ResponseBody
	public String handleCopyRecord(HttpServletRequest req, Model model)
			throws SQLException {

		trainersService.handleCopyRecord();
		String success = messageSource.getMessage(
				"success.trainer.save.record.to.person", new String[] {},
				Locale.US);
		return success;
	}

	@RequestMapping(value = "/searchPage", method = RequestMethod.GET)
	public String getSearchPage(HttpServletRequest req, Model model)
			throws SQLException {

		return "trainer-search-page";
	}

	@RequestMapping(value = "/findByName", method = RequestMethod.GET)
	@ResponseBody
	public Object getFindByName(HttpServletRequest request, ModelMap model)
			throws Exception {

		String search = request.getParameter("q");
		List<SearchByNameTrainerBean> records = trainersService
				.findByName(search);
		return records;
	}

	@RequestMapping(value = "/detail/{id}", method = RequestMethod.GET)
	public String getEmployeeById(@PathVariable("id") Integer id,
			HttpServletRequest request, ModelMap model) throws Exception {

		TeTrainers trainer = trainersService.getTrainer(id);
		model.addAttribute("trainer", trainer);
		model.addAttribute("verifiedStatusEnum",
				trainersService.getVerifiedStatus());
		model.addAttribute("sexEnum", employeeService.getSexEnum());
		model.addAttribute("maritalEnum",
				employeeService.getMaritalStatusEnum());
		model.addAttribute("employmentCatEnum",
				employeeService.getEmploymentCategoryEnum());
		model.addAttribute("titlesEnum", employeeService.getTitlesEnum());
		model.addAttribute("countiesEnum", employeeService.getCountiesEnum());
		model.addAttribute("countriesEnum", employeeService.getCountriesEnum());
		model.addAttribute("cardTypeEnum", employeeService.getAllCardType());
		model.addAttribute("pensionEnum", employeeService.getPension());
		model.addAttribute("nationalityEnum",
				employeeService.getNationalityEnum());

		return "trainer-edit";
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String getListOfTrainers(Model model, Authentication authentication) {

		List<TrainerUserBean> records = personService.getTrainerUserBean();
		model.addAttribute("records", records);
		return "trainer-list";
	}

	@RequestMapping(value = "/{id}/employees/{type}", method = RequestMethod.GET)
	public String getListOfTrainers(@PathVariable("id") Integer id,
			@PathVariable("type") String type, Model model,
			Authentication authentication) throws Exception {

		List<TeEmployentHistory> records = employeeService
				.getListOfTrainersEmployees(id, type);
		model.addAttribute("trainer", trainersService.getTrainer(id));
		model.addAttribute("records", records);
		model.addAttribute("type", type);
		return "trainer-employee-list";
	}

	@RequestMapping(value = "/export/{id}/{type}", method = RequestMethod.GET)
	public void handleExportTrainerEmployeeRecords(
			@PathVariable("id") Integer id, @PathVariable("type") String type,
			HttpServletRequest request, HttpServletResponse response,
			ModelMap model) throws Exception {

		BufferedWriter writer = new BufferedWriter(response.getWriter());
		String csvString = trainersService.getCSVStringForTrainerEmployees(id,
				type);
		TeTrainers trainer = trainersService.getTrainer(id);
		response.setContentType("text/csv");
		String headerValue = String.format("attachment; filename=\"%s\"",
				trainer.getTrainerFullName() + "'s Employees.csv");
		response.setHeader("Content-Disposition", headerValue);

		writer.write(csvString);

		writer.flush();
		writer.close();
	}

	@RequestMapping(value = "/pdfDoc/{id}/{type}", method = RequestMethod.GET)
	public void handleExportWordDocForTrainerEmployeeRecords(
			@PathVariable("id") Integer id, @PathVariable("type") String type,
			HttpServletRequest request, HttpServletResponse response,
			ModelMap model) throws Exception {

		Document document = new Document();
		try {
			TeTrainers trainer = trainersService.getTrainer(id);
			PdfPTable table = trainersService.createPDFDocumentWithDetails(id, type);//new XWPFDocument();
			PdfWriter.getInstance(document, response.getOutputStream());
			response.setContentType("application/pdf");
			String headerValue = String.format("attachment; filename=\"%s\"", trainer.getTrainerFullName()+"'s Employees.pdf");
			response.setHeader("Content-Disposition", headerValue);

			document.open();
			document.add(table);
			document.close();
		} catch (Exception ioe) {
			throw new ServletException(ioe.getMessage());
		} finally {
		}
	}
}
