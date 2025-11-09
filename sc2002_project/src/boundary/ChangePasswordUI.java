package boundary;

import java.util.Scanner;

import controller.PasswordController;
import controller.SystemController;
import entities.User;
import enums.UserRole;

/**
 * UI for password reset functionality.
 * Allows users to reset forgotten passwords by providing ID and name.
 */
public class ChangePasswordUI {
	private SystemController sys;
    public ChangePasswordUI(SystemController sys) { this.sys = sys; }
    
    // forgot password
    public void show() {
    	Scanner sc = new Scanner(System.in);
    	while (true) {
	    	System.out.println("\n======= Change Password =======");
			System.out.println("Domains:");
			System.out.println("1 Student");
			System.out.println("2 Company Representative");
			System.out.println("3 Career Center Staff");
			System.out.print("Select: ");
			String r = sc.nextLine().trim();
			UserRole role;
			switch(r) {
			case "1" -> role = UserRole.STUDENT;
			case "2" -> role = UserRole.COMPANY_REP;
			case "3" -> role = UserRole.STAFF;
			default -> role = null;
			}
			System.out.print("User ID: ");
			String id = sc.nextLine().trim();
			System.out.print("Name: ");
			String name = sc.nextLine().trim();
			System.out.print("New password: ");
			String newPW = sc.nextLine().trim();
			boolean ok = new PasswordController(sys.accounts()).changePassword(id, name, role, newPW);
			if (ok) break;
			else {
				String s = sc.nextLine().trim();
				if (!s.equals("1")) break;
			}
    	}
    }
    
    // user logged in
    public void show(User user) {
        Scanner sc = new Scanner(System.in);
        while(true) {
	        System.out.print("Old password: ");
	        String oldPW = sc.nextLine().trim();
	        System.out.print("New password: ");
	        String newPW = sc.nextLine().trim();
	        boolean ok = new PasswordController(sys.accounts()).changePassword(user, oldPW, newPW);
	        if (ok) break;
			else {
				String s = sc.nextLine().trim();
				if (!s.equals("1")) break;
			}
        }
    }
}
