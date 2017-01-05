package ie.turfclub.user.controller;

import java.util.Locale;

import ie.turfclub.main.model.login.User;
import ie.turfclub.main.service.login.UserService;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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
	
	@RequestMapping(value="/searchPage", method=RequestMethod.GET)
	public String getSearchUserPage(HttpServletRequest request, ModelMap model) {
		
		return "user-search";
	}
	
	@RequestMapping(value="/edit/{id}", method=RequestMethod.GET)
	public String getEditUser(@PathVariable(value="id") Integer id, HttpServletRequest request, ModelMap model) {
		
		User user = userService.findById(id);
		model.addAttribute("user", user);
		model.addAttribute("roles", userService.getAllRoles());
		return "user-edit";
	}
	
	@RequestMapping(value="/search", method=RequestMethod.GET)
	@ResponseBody
	public Object getSearchUser(HttpServletRequest request, ModelMap model) {
		
		String q  = request.getParameter("q");
		return userService.getUserBySearch(q);
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
	
	@RequestMapping(value="/update", method=RequestMethod.POST)
	public String handleUpdateUser(User user, HttpServletRequest request, ModelMap model) {
		
		boolean isExistsEmail = userService.isExistsEmail(user.getUsername(), user.getUser_id());
		boolean isPasswordSame = user.getPassword().equals(user.getConfirmPassword());
		if(isExistsEmail)
			model.addAttribute("error", messageSource.getMessage("error.email.already.exists", new String[] {}, Locale.US));
		else if(!isPasswordSame)
			model.addAttribute("error", messageSource.getMessage("error.password.mismatch", new String[] {}, Locale.US));
		else {
			userService.handleUser(user);
			model.addAttribute("success", messageSource.getMessage("success.update.user", new String[] {}, Locale.US));
		}
		model.addAttribute("user", user);
		model.addAttribute("roles", userService.getAllRoles());
		return "user-edit";
	}
}
