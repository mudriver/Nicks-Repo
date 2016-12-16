package ie.turfclub.trainers.controller;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ie.turfclub.main.model.login.User;
import ie.turfclub.main.pojos.StatusResponse;
import ie.turfclub.main.service.downloads.DownloadService;
import ie.turfclub.main.service.downloads.TokenService;
import ie.turfclub.trainers.model.TeChangesLog;
import ie.turfclub.trainers.model.TeEmployees;
import ie.turfclub.trainers.model.TeEmployeesJsonObject;
import ie.turfclub.trainers.model.TeEmployentHistory;
import ie.turfclub.trainers.model.TeTrainers;
import ie.turfclub.trainers.model.TeTrainersJsonObject;
import ie.turfclub.trainers.pojos.Employee;
import ie.turfclub.trainers.pojos.EmployeeHistoryRequest;
import ie.turfclub.trainers.service.StableStaffService;
import ie.turfclub.trainers.service.TrainersService;
import ie.turfclub.utilities.EmployeeHistoryUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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
	private MessageSource messageSource;
	
	@Autowired
	private TrainersService trainersService;

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
		model.addAttribute("verifiedStatusEnum", trainersService.getVerifiedStatus());
		return "trainer-add";
	}
	
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String handleTrainerPage(TeTrainers trainer, Model model) throws SQLException {

		trainersService.saveOrUpdate(trainer);
		model.addAttribute("trainer", new TeTrainers());
		model.addAttribute("success", messageSource.getMessage("success.added.trainer", new String[] {}, Locale.US));
		return "trainer-add";
	}
}
