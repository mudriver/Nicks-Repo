package ie.turfclub.main.service.login;

import ie.turfclub.common.bean.SearchUserBean;
import ie.turfclub.main.model.login.Roles;
import ie.turfclub.main.model.login.User;
import ie.turfclub.main.model.login.UserRoles;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
 
@Service
@Transactional
public class UserServiceImpl implements UserService {
    static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
     
    @Autowired
    private SessionFactory sessionFactory;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
 
    private Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }
    
    //TODO Dummy role added temporarily 
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    	Query query = getCurrentSession().createQuery("from User where user_login = :usersName ");
        query.setString("usersName", username);
         
        logger.info(query.toString());
        if (query.list().size() == 0 ) {
            throw new UsernameNotFoundException("User [" + username.toString() + "] not found");
        } else {
        	 @SuppressWarnings("unchecked")
			List<User> list = (List<User>)query.list();
        	 query = getCurrentSession().createQuery("from UserRole where user_name = :usersName ");
             query.setString("usersName", username); 
             
             User user = (User) list.get(0);
             @SuppressWarnings("unchecked")
			 Collection<GrantedAuthority> authorities = query.list();
             user.setAuthorities(authorities);
             
             
             logger.info(user.toString());
             return user;
        }
    }
    
    @Override
    public boolean isExistsEmail(String username) {
    	
    	Criteria criteria = getCurrentSession().createCriteria(User.class);
    	criteria.add(Restrictions.eq("user_login", username));
    	List<User> users = criteria.list();
    	User userFromDB = (users != null && users.size() > 0) ? users.get(0) : null;
    	if(userFromDB != null)
    		return true;
    	else
    		return false;
    }
    
    @Override
    public boolean isExistsEmail(String username, Integer id) {
    	
    	Criteria criteria = getCurrentSession().createCriteria(User.class);
    	criteria.add(Restrictions.eq("user_login", username));
    	criteria.add(Restrictions.ne("user_id", id));
    	List<User> users = criteria.list();
    	User userFromDB = (users != null && users.size() > 0) ? users.get(0) : null;
    	if(userFromDB != null)
    		return true;
    	else
    		return false;
    }
    
    @Override
    public String handleUser(User user) {
    	
    	user.setPassword(passwordEncoder.encode(user.getPassword()));
    	getCurrentSession().saveOrUpdate(user);
    	
    	Query query = getCurrentSession().createQuery("from UserRoles where roletypeId = ? and userId = ?");
    	query.setInteger(0, user.getRoleId());
    	query.setInteger(1, user.getId());
    	List<UserRoles> records = query.list();
    	if(records == null || (records != null && records.size() == 0)) {
    		UserRoles roles = new UserRoles();
    		roles.setUserId(user.getId());
    		roles.setRoletypeId(user.getRoleId());
    		getCurrentSession().saveOrUpdate(roles);
    	}
    	return null;
    }
    
    @Override
    public List<Roles> getAllRoles() {
    	
    	Criteria criteria = getCurrentSession().createCriteria(Roles.class);
    	List<Roles> records = criteria.list();
    	return records;
    }
    
    @Override
    public List<SearchUserBean> getUserBySearch(String q) {
    	Criteria criteria = getCurrentSession().createCriteria(User.class);
    	criteria.add(Restrictions.ilike("fullName", q, MatchMode.ANYWHERE));
    	List<User> records = criteria.list();
    	return convertEntityToBean(records);
    }

	private List<SearchUserBean> convertEntityToBean(List<User> records) {
		
		List<SearchUserBean> results = new ArrayList<SearchUserBean>();
		if(records != null && records.size() > 0) {
			for (User user : records) {
				SearchUserBean bean = new SearchUserBean();
				bean.setId(user.getUser_id());
				bean.setFullName(user.getFullName());
				results.add(bean);
			}
		}
		return results;
	}
	
	@Override
	public User findById(Integer id) {
		
		Criteria criteria = getCurrentSession().createCriteria(User.class);
		criteria.add(Restrictions.eq("user_id", id));
		List<User> records = criteria.list();
		return (records != null && records.size() > 0) ? records.get(0) : null;
	}
}