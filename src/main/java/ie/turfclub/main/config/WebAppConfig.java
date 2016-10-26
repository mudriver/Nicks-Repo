package ie.turfclub.main.config;

import ie.turfclub.formatters.DateFormatter;
import ie.turfclub.formatters.InspectionsCategoriesFormatter;
import ie.turfclub.formatters.InspectionsOfficialsFormatter;




import ie.turfclub.formatters.TrainerFormatter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.Ordered;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;


@Configuration
@EnableWebMvc
@ComponentScan( {"ie.turfclub.*.controller"})
public class WebAppConfig extends WebMvcConfigurerAdapter {


	@Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/login").setViewName("login");
        registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
    }
	
	
 
 
 // Maps resources path to webapp/resources
 public void addResourceHandlers(ResourceHandlerRegistry registry) {
  registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
  registry.addResourceHandler("/favicon.ico").addResourceLocations("/favicon.ico");
 }
 
 // Provides internationalization of messages
 @Bean
 public ResourceBundleMessageSource messageSource() {
  ResourceBundleMessageSource source = new ResourceBundleMessageSource();
  source.setBasename("ie/turfclub/main/resources/messages/messages");
  return source;
 }
 
 @Override
 public void addFormatters(FormatterRegistry registry) {
     
     registry.addFormatter(new DateFormatter());
     registry.addFormatter(new InspectionsOfficialsFormatter());
     registry.addFormatter(new InspectionsCategoriesFormatter());
     registry.addFormatter(new TrainerFormatter());
     super.addFormatters(registry);
 }
  
}