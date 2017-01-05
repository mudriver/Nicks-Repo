package ie.turfclub.main.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LinkController {

	@RequestMapping(value = "/home")
	public String home(Model model) {

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    

		for(GrantedAuthority authorities :  auth.getAuthorities()){
			System.out.println(authorities.getAuthority());
			switch (authorities.getAuthority()) {
	         case "VET_USER":
	             model.addAttribute("VET_REPORTS", "VET_REPORTS");
	             break;
	         case "VET_ADMIN":
	        	 model.addAttribute("VET_REPORTS", "VET_REPORTS");
	        	 break;
	         case "INSPECTIONS_ADMIN":
	             model.addAttribute("INSPECTIONS", "INSPECTIONS");
	             break;
	         case "INSPECTIONS_CEO":
	        	 model.addAttribute("INSPECTIONS", "INSPECTIONS");
	        	 break;
	         case "INSPECTIONS_LICENCING":
	        	 model.addAttribute("INSPECTIONS", "INSPECTIONS");
	        	 break;	
	         case "STABLESTAFF":
	        	// model.addAttribute("TRAINERS","TRAINERS");
	        	 model.addAttribute("STABLESTAFF","STABLESTAFF");
	        	 break;	
	        	
	         case "STABLESTAFF_PENSION":
	        	 model.addAttribute("TRAINERS","TRAINERS");
	        	 model.addAttribute("STABLESTAFF","STABLESTAFF");
	        	 break;	
	         case "TRAINERS_USER":
	        	 model.addAttribute("TRAINERS","TRAINERS");
	        	 break;	
	         case "TRAINERS_ADMIN":
	        	 model.addAttribute("TRAINERS","TRAINERS");
	        	 break;	
	         case "ACCOUNTS":
	        	 model.addAttribute("ACCOUNTS","ACCOUNTS");
	        	 break;	
	         case "SUPER_USER":
	        	 model.addAttribute("SUPERUSER", "SUPERUSER");
	        	 break;
	        
	         default:
	        	 
		}
		}
		
		return "home";
	}

	@RequestMapping(value = "/logout")
	public String logout(Model model,
			@RequestParam(value = "logout") String logout) {

		//System.out.println(logout.equals("null"));
		if(!logout.equals("null")){
			model.addAttribute("logout", logout);
		}
		else{
			model.addAttribute("logout", "You are now sucessfully logged out");
		}
		
		return "login";
	}

	@RequestMapping(value = "/notAuthorised")
	public String notAuthorised() {

		return "notAuthorised";
	}

}