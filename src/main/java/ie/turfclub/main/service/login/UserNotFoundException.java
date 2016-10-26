package ie.turfclub.main.service.login;

public class UserNotFoundException extends Exception {

	private String message;

	public UserNotFoundException(String message) {
		super();
		this.message = message;
	}
	
	
}
