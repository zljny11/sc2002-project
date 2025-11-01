package boundary;

import java.util.Scanner;

import controller.RegistrationController;
import controller.SystemController;

public class RegistrationUI {
	private SystemController sys;

	public RegistrationUI(SystemController sys) { this.sys = sys; }

	public void show() {
		Scanner sc = new Scanner(System.in);
		while (true) {
			System.out.println("\n======= Register =======");
			System.out.print("Email: ");
			String email = sc.nextLine().trim();
			System.out.print("Name: ");
			String name = sc.nextLine().trim();
			System.out.print("Company Name: ");
			String companyName = sc.nextLine().trim();
			System.out.print("Department: ");
			String dept = sc.nextLine().trim();
			System.out.print("Position: ");
			String pos = sc.nextLine().trim();
			boolean ok = new RegistrationController(sys.accounts()).registerCompanyRep(email,name,"password",companyName,dept,pos);
			if (ok) break;
			else {
				String s = sc.nextLine().trim();
				if (!s.equals("1")) break;
			}
		}
	}
}