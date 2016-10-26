package ie.turfclub.utilities;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
 
public class Hashing {
	 
	  public static void main(String[] args) {
	 
		
			String password = "EllaDylan1";
			BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
			String hashedPassword = passwordEncoder.encode(password);
	 
			System.out.println("Password:" + hashedPassword);
			
		
	 
	  }
	}