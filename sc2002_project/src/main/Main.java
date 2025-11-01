package main;

import boundary.WelcomeUI;
import controller.SystemController;
import entities.Repository;

public class Main {
	public static void main(String[] args) {
		Repository repo = new Repository();
		SystemController sys = new SystemController(repo);
		new WelcomeUI(sys).start();
	}
}