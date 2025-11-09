package main;

import boundary.WelcomeUI;
import controller.SystemController;
import entities.Repository;

/**
 * Main entry point for the Internship Placement Management System.
 */
public class Main {
	/**
	 * Starts the application by initializing the repository and system controller.
	 * @param args command line arguments (not used)
	 */
	public static void main(String[] args) {
		Repository repo = new Repository();
		SystemController sys = new SystemController(repo);
		new WelcomeUI(sys).start();
	}
}