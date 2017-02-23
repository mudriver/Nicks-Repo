package ie.turfclub.main.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;







import ie.turfclub.main.config.DatabaseConfig;
import ie.turfclub.main.config.ThymeleafConfig;
import ie.turfclub.main.config.WebAppConfig;
 

@Configuration
public class Initializer extends
  AbstractAnnotationConfigDispatcherServletInitializer{
 
 @Override
 protected Class<?>[] getRootConfigClasses() {
	  return new Class[] { BeanConfig.class, DatabaseConfig.class, SecurityConfig.class };
  
 }
 
 @Override
 protected Class<?>[] getServletConfigClasses() {
  return new Class<?>[] { WebAppConfig.class , ThymeleafConfig.class};
 }
 
 @Override
 protected String[] getServletMappings() {
  return new String[] { "/" };
 }
 
}	
 
