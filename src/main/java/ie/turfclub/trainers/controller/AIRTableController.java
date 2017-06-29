package ie.turfclub.trainers.controller;

import java.util.List;

import ie.turfclub.trainers.model.AIRTable;
import ie.turfclub.trainers.service.AIRTableService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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
}
