package ie.turfclub.main.config;

import ie.turfclub.trainers.service.StableStaffService;
import ie.turfclub.trainers.service.TrainersService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.annotation.Order;
import org.springframework.web.multipart.MultipartResolver;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;
import org.thymeleaf.templateresolver.TemplateResolver;

@Order(1)
@Configuration
public class ThymeleafConfig {
 
	@Autowired
	StableStaffService stableStaffService;
	@Autowired
	TrainersService trainerService;
	
	
	 @Bean
	    public TemplateResolver manifestResolver(){
	        ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver();
	        templateResolver.setPrefix("/WEB-INF/inspections/view/manifest/");
	        templateResolver.setSuffix(".manifest");
	        templateResolver.setCacheTTLMs((long) 5000);
	        
	        return templateResolver;
	    }
	
    @Bean
    public TemplateResolver templateResolver(){
        ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver();
        templateResolver.setPrefix("/WEB-INF/main/view/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode("HTML5");
        stableStaffService.initialize();
        return templateResolver;
    }
 
    @Bean
    //view resolver for accounts reports
    public TemplateResolver accountsViewResolver(){
        ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver();
        templateResolver.setPrefix("/WEB-INF/accountsReports/view/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode("HTML5");

        return templateResolver;
    }
    
    
    @Bean
    //view resolver for inspections
    public TemplateResolver inspectionsViewResolver(){
        ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver();
        templateResolver.setPrefix("/WEB-INF/inspections/view/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode("HTML5");
        return templateResolver;
    }
    
    @Bean
    //view resolver for vet reports
    public TemplateResolver vetReportsViewResolver(){
        ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver();
        templateResolver.setPrefix("/WEB-INF/vetReports/view/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode("HTML5");
        return templateResolver;
    }
    
    
    @Bean
    //view resolver for vet reports
    public TemplateResolver trainersEmployeesViewResolver(){
        ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver();
        templateResolver.setPrefix("/WEB-INF/trainers/view/");
        
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode("HTML5");
        trainerService.initialize();
        return templateResolver;
    }
    
    @Bean
    public MultipartResolver multipartResolver() {
        org.springframework.web.multipart.commons.CommonsMultipartResolver multipartResolver = new org.springframework.web.multipart.commons.CommonsMultipartResolver();
        multipartResolver.setMaxUploadSize(10000000);
        return multipartResolver;
    }
  
    
    @Bean
    public SpringTemplateEngine templateEngine(){
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
      
        templateEngine.addTemplateResolver(inspectionsViewResolver());
        templateEngine.addTemplateResolver(manifestResolver());
        templateEngine.addTemplateResolver(accountsViewResolver());
        templateEngine.addTemplateResolver(vetReportsViewResolver());
        templateEngine.addTemplateResolver(trainersEmployeesViewResolver());
        templateEngine.addTemplateResolver(templateResolver());
        return templateEngine;
    }
 
 @Bean
 public ThymeleafViewResolver thymeleafViewResolver() {
  ThymeleafViewResolver resolver = new ThymeleafViewResolver();
  resolver.setTemplateEngine(templateEngine());
  return resolver;
 }
}
