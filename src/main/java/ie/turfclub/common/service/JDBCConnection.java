package ie.turfclub.common.service;

import java.sql.DriverManager;
import java.sql.SQLException;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.mysql.jdbc.Connection;

@Service
@PropertySource("classpath:ie/turfclub/main/resources/properties/database.properties")
@Transactional
public class JDBCConnection {

	@Resource
	private Environment env;
	
	public JDBCConnection() {}
	Connection conn = null;
	
	/**
	 * Get Connection
	 * 
	 * @return
	 * @throws SQLException 
	 */
	public Connection getConnection() throws SQLException {
		
		conn = (conn != null) ? conn : (Connection) DriverManager.getConnection(env.getRequiredProperty("person.db.url"), env.getRequiredProperty("person.db.username"), env.getRequiredProperty("person.db.password"));
		return conn;
	}
}
