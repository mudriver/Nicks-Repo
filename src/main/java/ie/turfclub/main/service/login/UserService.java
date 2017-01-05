package ie.turfclub.main.service.login;

import java.util.List;

import ie.turfclub.common.bean.SearchUserBean;
import ie.turfclub.main.model.login.User;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
 
public interface UserService extends UserDetailsService {
 
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;

	public String handleUser(User user);

	public boolean isExistsEmail(String username);

	public Object getAllRoles();

	public List<SearchUserBean> getUserBySearch(String q);

	public User findById(Integer id);
	
	public boolean isExistsEmail(String username, Integer id);
}
