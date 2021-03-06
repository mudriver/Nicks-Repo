package ie.turfclub.main.config;




import java.util.Properties;

import ie.turfclub.trainers.service.AintreePDFView;
import ie.turfclub.trainers.service.CheltenhamPDFView;
import ie.turfclub.trainers.service.InitialLetterPDF;
import ie.turfclub.trainers.service.InitialLetterSBSPDF;
import ie.turfclub.trainers.service.LicenseTrainerListPDFView;
import ie.turfclub.trainers.service.ReminderLetterPDF;
import ie.turfclub.trainers.service.ReminderLetterSBSPDF;
import ie.turfclub.trainers.service.ReprintPDF;
import ie.turfclub.trainers.service.TrainerPDFView;
import ie.turfclub.utilities.AccountReporting;
import ie.turfclub.utilities.EmployeeHistoryUtils;
import ie.turfclub.utilities.MailUtility;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.BeanNameViewResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.jasperreports.JasperReportsHtmlView;
import org.springframework.web.servlet.view.jasperreports.JasperReportsPdfView;
import org.springframework.web.servlet.view.jasperreports.JasperReportsXlsView;

@Configuration
@PropertySource("classpath:ie/turfclub/main/resources/properties/config.properties")
@EnableScheduling
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
		 /*InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
		 viewResolver.setOrder(1);
		 Properties properties = new Properties();
		 properties.setProperty("order", "1");
		 properties.setProperty("basename", "views");
		 viewResolver.setAttributes(properties);
		 return viewResolver;*/
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
	 
	 @Bean
	 public TrainerPDFView trainerPDFView() {
		 return new TrainerPDFView();
	 }
	 
	 @Bean
	 public InitialLetterPDF initialLetterPDF() {
		 return new InitialLetterPDF();
	 }
	 
	 @Bean
	 public AintreePDFView aintreePDFView() {
		 return new AintreePDFView();
	 }
	 
	 @Bean LicenseTrainerListPDFView licenseTrainerListPDFView() {
		 return new LicenseTrainerListPDFView();
	 }
	 
	 @Bean
	 public CheltenhamPDFView cheltenhamPDFView() {
		 return new CheltenhamPDFView();
	 }
	 
	 @Bean
	 public InitialLetterSBSPDF initialLetterSBSPDF() {
		 return new InitialLetterSBSPDF();
	 }
	 
	 @Bean
	 public ReminderLetterPDF reminderLetterPDF() {
		 return new ReminderLetterPDF();
	 }
	 
	 @Bean
	 public ReminderLetterSBSPDF remiderLetterSBSPDF() {
		 return new ReminderLetterSBSPDF();
	 }
	 
	 @Bean
	 public ReprintPDF reprintPDF() {
		 return new ReprintPDF();
	 }
}
