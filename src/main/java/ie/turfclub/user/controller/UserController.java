package ie.turfclub.user.controller;

import java.util.Locale;

import ie.turfclub.main.model.login.User;
import ie.turfclub.main.service.login.UserService;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "/user")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private MessageSource messageSource;

	@RequestMapping(value="/add", method=RequestMethod.GET)
	public String getAddUserPage(HttpServletRequest request, ModelMap model) {
		
		model.addAttribute("user", new User());
		model.addAttribute("roles", userService.getAllRoles());
		return "user-add";
	}
	
	@RequestMapping(value="/save", method=RequestMethod.POST)
	public String handleUser(User user, HttpServletRequest request, ModelMap model) {
		
		boolean isExistsEmail = userService.isExistsEmail(user.getUsername());
		boolean isPasswordSame = user.getPassword().equals(user.getConfirmPassword());
		if(isExistsEmail)
			model.addAttribute("error", messageSource.getMessage("error.email.already.exists", new String[] {}, Locale.US));
		else if(!isPasswordSame)
			model.addAttribute("error", messageSource.getMessage("error.password.mismatch", new String[] {}, Locale.US));
		else {
			userService.handleUser(user);
			model.addAttribute("success", messageSource.getMessage("success.add.user", new String[] {}, Locale.US));
		}
		model.addAttribute("user", user);
		model.addAttribute("roles", userService.getAllRoles());
		return "user-add";
	}
}
