package boundary;

import java.util.Scanner;

import controller.LoginController;
import controller.SessionController;
import controller.SystemController;
import entities.CompanyRepresentative;
import entities.Staff;
import entities.Student;
import entities.User;
import enums.UserRole;

public class LoginUI {
	private SystemController sys;
	
	public LoginUI(SystemController sys) { this.sys = sys; }
	
	public void show() {
		Scanner sc = new Scanner(System.in);
		System.out.println("======= Login =======");
		System.out.println("Domains:");
		System.out.println("1 Student");
		System.out.println("2 Company Representative");
		System.out.println("3 Career Center Staff");
		System.out.print("Select: ");
		String r = sc.next();
		UserRole role;
		switch(r) {
		case "1" -> role = UserRole.STUDENT;
		case "2" -> role = UserRole.COMPANY_REP;
		case "3" -> role = UserRole.STAFF;
		default -> role = null;
		}
		System.out.print("User ID: ");
		String id = sc.next();
		System.out.print("Password: ");
		String pw = sc.next();
		
		User u = new LoginController(sys.accounts()).login(id, pw, role);
		if (u == null) return;
		
		SessionController.setCurrentUser(u);
		
		// route to respective UIs
		switch(role) {
		case STUDENT -> new StudentMainUI(sys, (Student)u).show();
		case COMPANY_REP -> new CompanyMainUI(sys, (CompanyRepresentative)u).show();
		case STAFF -> new StaffMainUI(sys, (Staff)u).show();
		}
	}
}