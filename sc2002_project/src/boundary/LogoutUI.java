package boundary;

import java.util.Scanner;

import controller.LogoutController;

public class LogoutUI {

	public LogoutUI() {}
	
	public void confirm() {
		Scanner sc = new Scanner(System.in);
		System.out.print("Log out? (Y/N)");
		String choice = sc.next();
		if (choice.toLowerCase().equals("y")) {
			LogoutController.logout();
		} else {
			return;
		}
	}
}
