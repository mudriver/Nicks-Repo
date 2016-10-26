package ie.turfclub.main.config;




import ie.turfclub.utilities.AccountReporting;
import ie.turfclub.utilities.EmployeeHistoryUtils;
import ie.turfclub.utilities.MailUtility;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.BeanNameViewResolver;
import org.springframework.web.servlet.view.jasperreports.JasperReportsHtmlView;
import org.springframework.web.servlet.view.jasperreports.JasperReportsPdfView;
import org.springframework.web.servlet.view.jasperreports.JasperReportsXlsView;

@Configuration
@PropertySource("classpath:ie/turfclub/main/resources/properties/config.properties")

public class BeanConfig {

	@Autowired
	private Environment env;
	
	
	 @Bean
	 public static PropertySourcesPlaceholderConfigurer properties() {
	     PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer = new PropertySourcesPlaceholderConfigurer();
	     return propertySourcesPlaceholderConfigurer;
	 }
	
	@Bean 
	   public MailUtility sendMail(){
		return new MailUtility(env);
	   }
	 

	 
	 @Bean
	 public AccountReporting accountsReports(){
		 return new AccountReporting();
	 }
	 
	 
	 @Bean
	 public ViewResolver beanViewResolver(){
	 BeanNameViewResolver viewResolver = new BeanNameViewResolver();
	 viewResolver.setOrder(1);
	 return viewResolver;
	 }

	
	 @Bean
	 public View svlListPdf() {
	
	 JasperReportsPdfView userReportPdfView = new JasperReportsPdfView();
	 userReportPdfView.setUrl("classpath:ie/turfclub/vetReports/resources/jasper/VetReports.jrxml");

	 return userReportPdfView;
	 }

	 @Bean
	 public View svlListXls() {

	 JasperReportsXlsView userReportXlsView = new JasperReportsXlsView();
	 userReportXlsView.setUrl("classpath:ie/turfclub/vetReports/resources/jasper/VetReports.jrxml");
	 return userReportXlsView;
	 }
	 
	 @Bean
	 public View svlListHtml() {
	
	 JasperReportsHtmlView userReportHtmlView = new JasperReportsHtmlView();
	 userReportHtmlView.setUrl("classpath:ie/turfclub/vetReports/resources/jasper/VetReports.jrxml");
	 return userReportHtmlView;
	 }


	
	 @Bean
	 public EmployeeHistoryUtils employeeUtils(){
		 return new EmployeeHistoryUtils();
	 }
}
