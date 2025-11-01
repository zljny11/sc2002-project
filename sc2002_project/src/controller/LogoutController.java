package controller;

public class LogoutController {
	
	public static void logout() {
		SessionController.clear();
		System.out.println("Logged out");
	}
	
}
