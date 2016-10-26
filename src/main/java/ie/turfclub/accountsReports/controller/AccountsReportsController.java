package ie.turfclub.accountsReports.controller;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

import ie.turfclub.utilities.AccountReporting;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@ComponentScan("ie.turfclub.pojos")
@RequestMapping(value = "/accountsReports")
public class AccountsReportsController {
	static Logger logger = LoggerFactory.getLogger(AccountsReportsController.class);


	@Autowired
	AccountReporting reporting;
	
	@RequestMapping(value = "/generate", method = RequestMethod.GET)
	public String newInspection(Model model) {
		logger.info("IN: Accounts Reports");
		
		return "accountsReports-generate";
	}
	
	@RequestMapping(value = "/generate", method = RequestMethod.POST)
	public String saveInspection(Model model,  @RequestParam(value = "print", required = true) String print,  @RequestParam(value = "email", required = false) String email) {
		logger.info("IN: Generate Report:" + print);
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if(print.equals("yes")){
			try {
				Class.forName("com.mysql.jdbc.Driver");
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			java.sql.Connection conn = null;
			try {
				conn = DriverManager
						.getConnection("jdbc:mysql://127.0.0.1:3000/turf201_disciplinaries?user=root&password=password");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			ArrayList<String> emails = new ArrayList<>();
			emails.add(auth.getName());
			if(!email.isEmpty()){
				emails.add(email);
				reporting.makeWeeklyDisciplinaryFinesReport(conn, emails);
				model.addAttribute("message", "Accounts report sent successfully to:" + auth.getName() + ", " + email + " successfully.");
			}
			else{
				reporting.makeWeeklyDisciplinaryFinesReport(conn, emails);
				model.addAttribute("message", "Accounts report sent successfully to:" + auth.getName() + " successfully.");
			}
			
			
			//generate report
		}
		return  "accountsReports-generate";	
		
	}
	
}
