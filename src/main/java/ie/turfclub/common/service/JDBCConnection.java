package ie.turfclub.common.service;

import java.sql.DriverManager;
import java.sql.SQLException;

import org.springframework.stereotype.Service;

import com.mysql.jdbc.Connection;

@Service
public class JDBCConnection {

	//jdbc driver connection string, db username and password
	private String connectionUrl = "jdbc:mysql://localhost:3306/person_db";
	private String username = "root";
	private String password = "root";
	Connection conn = null;
	
	/**
	 * Get Connection
	 * 
	 * @return
	 * @throws SQLException 
	 */
	public Connection getConnection() throws SQLException {
		
		conn = (conn != null) ? conn : (Connection) DriverManager.getConnection(connectionUrl, username, password);
		return conn;
	}
}
