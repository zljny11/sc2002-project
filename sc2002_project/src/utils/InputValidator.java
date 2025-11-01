package utils;

public class InputValidator {
	
	public static boolean validStudentID(String id) { return id != null && id.matches("U\\d{7}[A-Za-z]"); }
	
	public static boolean validEmail(String email) { return email != null && email.contains("@"); }

}
