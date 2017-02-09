package ie.turfclub.sbs.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping(value = "/sbscheme")
public class StableBonusSchemeController {

	static Logger logger = LoggerFactory.getLogger(StableBonusSchemeController.class);
	
	@RequestMapping(value="/get", method=RequestMethod.GET)
	public String getPage(HttpServletRequest req, ModelMap model) {
		
		return "sbs-page";
	}
	
	
}
