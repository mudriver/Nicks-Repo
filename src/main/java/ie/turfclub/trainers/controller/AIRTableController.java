package ie.turfclub.trainers.controller;

import ie.turfclub.main.model.login.User;
import ie.turfclub.trainers.service.AIRTableService;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.HashMap;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mysql.jdbc.Constants;

@Controller
@RequestMapping(value = "/air")
@PropertySource("classpath:ie/turfclub/main/resources/properties/config.properties")
public class AIRTableController {

	static Logger logger = LoggerFactory.getLogger(AIRTableController.class);
	
	public static int noOfRecords = 10;

	@Resource
	private Environment env;
	
	@Autowired
	private AIRTableService airTableService;
	
	@RequestMapping(value="/all",method=RequestMethod.GET)
	public String getAllAirTable(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		
		/*List<AIRTable> records = airTableService.findAll();
		model.addAttribute("records", records);*/
		return "air-all";
	}
	
	@RequestMapping(value="/loadData",method=RequestMethod.GET)
	@ResponseBody
	public Object loadDataByPagination(HttpServletRequest request, HttpServletResponse response, 
			@RequestParam(value="start") int start, @RequestParam(value="length") int length,
			@RequestParam(value="draw") int draw, ModelMap model) {
		
		length = (length == -1) ? noOfRecords : length;
		return airTableService.loadDataByPagination(start, length, draw);
	}
	
	@RequestMapping(value="/export/csv",method=RequestMethod.GET)
	public void exportCSV(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		
		/*List<AIRTable> records = airTableService.findAll();
		model.addAttribute("records", records);*/
		/*return "air-all";*/
		try {
			BufferedWriter writer = new BufferedWriter(response.getWriter());
			
			response.setContentType("text/csv");
			// creates mock data
			String headerValue = String.format("attachment; filename=\"%s\"", "AIR_Table.csv");
			response.setHeader("Content-Disposition", headerValue);
			writer.write(airTableService.getCSVString());
			writer.flush();
			writer.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@RequestMapping(value="/send/mail",method=RequestMethod.GET)
	@ResponseBody
	public Object sendMailToAdmin(HttpServletRequest request, HttpServletResponse response, ModelMap model,
			Authentication authentication) throws IllegalStateException, IOException {
	
		Object principal = authentication.getPrincipal();
		User user = (User) principal;
		return airTableService.sendMailToAdmin(env.getRequiredProperty("upload.pdf.air"), user);
	}
}
