package controller;

import entities.User;

public class SessionController {
	private static User currentUser;
	
	public static void setCurrentUser(User user) { currentUser = user; }
	public static User getCurrentUser() { return currentUser; }
	public static void clear() { currentUser = null; }
}