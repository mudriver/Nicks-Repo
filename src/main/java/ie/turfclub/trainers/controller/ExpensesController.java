package ie.turfclub.trainers.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value="/expenses")
public class ExpensesController {

	/*@Autowired
	private ExpenseService expenseService;
	
	private String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
	
	@RequestMapping(value="/get", method=RequestMethod.GET)
	public String getDataFromExcel(HttpServletRequest request, ModelMap model) {
		
		HashMap<String, Object> data = expenseService.getDataFromExcel();
		model.addAttribute("data", data.get("data"));
		
		return "expenses-manager";
	}
	
	@RequestMapping(value="/edit/{id}", method=RequestMethod.POST)
	public String handleRecord(@PathVariable(value = "id") int id, HttpServletRequest request, ModelMap model,
			Authentication auth) {
		
		String subsistanceChange = request.getParameter("subsistanceChange");
		String subsistanceReason = request.getParameter("subsistanceReason");
	    String additionalMileage = request.getParameter("additionalMileage");
	    String mileageReason = request.getParameter("mileageReason");
		
	    System.out.println(subsistanceChange);
		return "expenses-manager";
	}

	@RequestMapping(value="/search/{year}/{month}", method=RequestMethod.GET)
	public String handleRecord(@PathVariable(value = "year") String year, @PathVariable(value = "month") String month,
			HttpServletRequest request, ModelMap model, Authentication auth) {
		
		HashMap<String, Object> data = expenseService.getSearchableData(month, year);
		model.addAttribute("data", data.get("data"));
		model.addAttribute("month", months[Integer.parseInt(month)-1]+"-"+year+" View Data");
		model.addAttribute("monthVal", month);
		model.addAttribute("yearVal", year);
		return "expenses-manager";
	}*/
}
