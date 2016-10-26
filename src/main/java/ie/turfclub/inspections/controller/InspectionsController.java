package ie.turfclub.inspections.controller;




import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;




	@Controller
	@RequestMapping(value = "/inspections")
	public class InspectionsController {



		
		@RequestMapping(value = "/home", method = RequestMethod.GET)
		public String inspections(Model model) {


			return "inspections";
		}
		
		@RequestMapping(value = "/inspectionsManifest", method = RequestMethod.GET)
		public String inspectionsManifest(Model model) {


			return "inspectionsManifest";
		}
		
	
	}
