package ie.turfclub.main.config;

import java.util.Properties;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.aspectj.runtime.internal.PerObjectMap;
import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@Configuration
@EnableTransactionManagement
//@ComponentScan(basePackages = "ie.turfclub.config")
@PropertySource("classpath:ie/turfclub/main/resources/properties/database.properties")
public class DatabaseConfig {
 
    private static final String PROPERTY_NAME_DATABASE_DRIVER   = "db.driver";
    private static final String PROPERTY_NAME_DATABASE_PASSWORD = "db.password";
    private static final String PROPERTY_NAME_DATABASE_URL      = "db.url";
    private static final String PROPERTY_NAME_DATABASE_USERNAME = "db.username";
  
    private static final String PROPERTY_NAME_HIBERNATE_DIALECT = "hibernate.dialect";
    private static final String PROPERTY_NAME_HIBERNATE_SHOW_SQL = "hibernate.show_sql";
    private static final String PROPERTY_NAME_HIBERNATE_ENABLE_LAZY_LOAD_NO_TRANS = "hibernate.enable_lazy_load_no_trans";
    private static final String[] ENTITYMANAGER_PACKAGES_TO_SCAN = new String[] { "ie.turfclub.*.model", "ie.turfclub.person.model" };
     
 @Resource
 private Environment env;
  
 @Bean
 public DataSource dataSource() {
  DriverManagerDataSource dataSource = new DriverManagerDataSource();
  dataSource.setDriverClassName(env.getRequiredProperty(PROPERTY_NAME_DATABASE_DRIVER));
  dataSource.setUrl(env.getRequiredProperty(PROPERTY_NAME_DATABASE_URL));
  dataSource.setUsername(env.getRequiredProperty(PROPERTY_NAME_DATABASE_USERNAME));
  dataSource.setPassword(env.getRequiredProperty(PROPERTY_NAME_DATABASE_PASSWORD));
  return dataSource;
 }
  
 private Properties hibProperties() {
  Properties properties = new Properties();
  properties.put(PROPERTY_NAME_HIBERNATE_DIALECT, env.getRequiredProperty(PROPERTY_NAME_HIBERNATE_DIALECT));
  properties.put(PROPERTY_NAME_HIBERNATE_SHOW_SQL, env.getRequiredProperty(PROPERTY_NAME_HIBERNATE_SHOW_SQL));
  properties.put(PROPERTY_NAME_HIBERNATE_ENABLE_LAZY_LOAD_NO_TRANS, env.getRequiredProperty(PROPERTY_NAME_HIBERNATE_ENABLE_LAZY_LOAD_NO_TRANS));
  return properties; 
 }
  
 @Bean
 public HibernateTransactionManager transactionManager() {
  HibernateTransactionManager transactionManager = new HibernateTransactionManager();
  transactionManager.setSessionFactory(sessionFactory().getObject());
  return transactionManager;
 }
  
 @Bean
 public LocalSessionFactoryBean sessionFactory() {
	 LocalSessionFactoryBean sessionFactoryBean = new LocalSessionFactoryBean();
  sessionFactoryBean.setDataSource(dataSource());
  sessionFactoryBean.setPackagesToScan(ENTITYMANAGER_PACKAGES_TO_SCAN);
  sessionFactoryBean.setHibernateProperties(hibProperties());
  return sessionFactoryBean;
 }
}