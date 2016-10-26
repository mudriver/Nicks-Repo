package ie.turfclub.main.config;



import ie.turfclub.main.filters.CsrfFilterExtend;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import  org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;

@Order(2)
@Configuration
@EnableWebMvcSecurity
@ComponentScan({"ie.turfclub.*.service"})
public class SecurityConfig extends WebSecurityConfigurerAdapter {
     
     
	
    @Override
    
    protected void configure(HttpSecurity http) throws Exception {
    	//http.csrf().disable();
    	
    	http 
    	.authorizeRequests()
                .antMatchers("/resources/**").permitAll()
                .antMatchers("/home/**").access("hasAnyRole('INSPECTIONS_CEO','INSPECTIONS_LICENCING','INSPECTIONS_ADMIN','VET_USER','VET_ADMIN','ACCOUNTS','TRAINERS_USER','TRAINERS_ADMIN','STABLESTAFF','STABLESTAFF_PENSION')")
                .antMatchers("/vetReports/**").access("hasAnyRole('VET_USER','VET_ADMIN')")
                .antMatchers("/inspections/**").access("hasAnyRole('INSPECTIONS_ADMIN','INSPECTIONS_LICENCING','INSPECTIONS_CEO')")
                .antMatchers("/accountsReports/**").access("hasAnyRole('ACCOUNTS')")
                .antMatchers("/trainersEmployees/**").access("hasAnyRole('TRAINERS_USER','TRAINERS_ADMIN','STABLESTAFF','STABLESTAFF_PENSION')")
                .antMatchers("/trainersEmployeesOnline/**").access("hasAnyRole('STABLESTAFF_PENSION')")
                .anyRequest().authenticated()
                
                .and()
               
            .formLogin()
                .loginPage("/login")
          
                .successHandler(new MyAuthSuccessHandler())
         
                //.defaultSuccessUrl("/hunterCert/handlerDetail", true)
                .permitAll()
            
                .and()
                
                
            .logout().logoutSuccessUrl("/logout").logoutSuccessHandler(new LogHandler())
           
            .permitAll()
            .and()
		.exceptionHandling().accessDeniedPage("/logout?logout=Please login to access this page").and()
		.csrf()
                .csrfTokenRepository(csrfTokenRepository()).and()
                .addFilterAfter(csrfFilter(), org.springframework.security.web.csrf.CsrfFilter.class);
		//.and()
		//.headers().frameOptions().disable();
        //.and().httpBasic();
       
    }
 
    

    private CsrfTokenRepository csrfTokenRepository() {
    	HttpSessionCsrfTokenRepository token = new HttpSessionCsrfTokenRepository();
    	token.setHeaderName("X-XSRF-TOKEN");
    	return token;
    }



    private CsrfFilterExtend csrfFilter() {
    	CsrfFilterExtend filter = new CsrfFilterExtend();
    	return filter;
    }
    
    @Autowired
    public void configureGlobal(UserDetailsService userService, AuthenticationManagerBuilder auth) throws Exception {
    	
    	
    	//userService.loadUserByUsername("02810P");
    
    	
    	auth.userDetailsService(userService).passwordEncoder(passwordEncoder());
    
    	//System.out.println(hand.getOwner_handler_password());
    	/*auth.inMemoryAuthentication().
        withUser("user").password("password").roles("USER").and().
        withUser("trader").password("password").roles("USER","TRADER").and().
        withUser("admin").password("password").roles("USER", "TRADER", "ADMIN");*/
    }
    
    
    @Bean
	public PasswordEncoder passwordEncoder(){
		PasswordEncoder encoder = new BCryptPasswordEncoder();
		return encoder;
	}
    
  
    
 
	public class LogHandler extends SimpleUrlLogoutSuccessHandler {
		
		 @Override
		    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response,
		            Authentication authentication) throws IOException, ServletException {

			
		        setDefaultTargetUrl("/logout?logout=" + request.getParameter("logout"));
		       
		        
		        super.onLogoutSuccess(request, response, authentication);       
		    }
		
	}
	

		
	public class MyAuthSuccessHandler implements AuthenticationSuccessHandler {


@Override
public void onAuthenticationSuccess(HttpServletRequest request,
		HttpServletResponse response, Authentication authentication)
		throws IOException, ServletException {

	//response.sendRedirect( "/turfclubPrograms/inspections/");
		response.sendRedirect( "/turfclubPrograms/home");
//TEST URL FOR TRAINERS EMPLOYEES
	//response.sendRedirect( "/turfclubPrograms/trainersEmployees/edit?employeeId=5872");
}




}
	
	

	
	
    
}