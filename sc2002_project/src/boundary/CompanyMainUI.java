package boundary;

import java.util.List;
import java.util.Scanner;

import controller.CompanyController;
import controller.LogoutController;
import controller.SystemController;
import entities.Application;
import entities.CompanyRepresentative;
import entities.Internship;
import enums.InternshipLevel;

public class CompanyMainUI extends MainUI{
	private SystemController sys;
	private CompanyRepresentative companyRep;
	private CompanyController companyCtrl;

	public CompanyMainUI(SystemController sys, CompanyRepresentative companyRep) {
		this.sys = sys;
		this.companyRep = companyRep;
		this.companyCtrl = new CompanyController(sys);
	}

	public void show() {
		Scanner sc = new Scanner(System.in);
		while (true) {
			System.out.println("\n======= Company Representative Menu =======");
			System.out.println("1 Create Internship");
			System.out.println("2 View My Internships");
			System.out.println("3 View Applicants for Internship");
			System.out.println("4 Toggle Internship Visibility");
			System.out.println("5 Change Password");
			System.out.println("6 Logout");
			System.out.print("Select: ");
			String s = sc.nextLine().trim();
			switch (s) {
			case "1":
				System.out.println("\n======= Create Internship =======");
				System.out.print("Title: ");
				String title = sc.nextLine().trim();
				System.out.print("Description: ");
				String desc = sc.nextLine().trim();
				System.out.print("Level (BASIC/INTERMEDIATE/ADVANCED): ");
				InternshipLevel lvl = InternshipLevel.valueOf(sc.nextLine().trim().toUpperCase());
				System.out.print("Preferred Major: ");
				String preferredMajor = sc.nextLine().trim();
				System.out.print("Opening Date (YYYY-MM-DD): ");
				String openDate = sc.nextLine().trim();
				System.out.print("Closing Date (YYYY-MM-DD): ");
				String closeDate = sc.nextLine().trim();
				System.out.print("Slots: ");
				int slots = Integer.parseInt(sc.nextLine().trim());
				companyCtrl.createInternship(companyRep, title, desc, lvl, preferredMajor, openDate, closeDate, slots);
				break;
			case "2":
				List<Internship> l = companyCtrl.getInternships(companyRep);
				if (l.isEmpty()) {
					System.out.println("No internships created");
				} else {
					System.out.println("\n======= Internships =======");
					for (Internship i2 : l) {
						System.out.println(i2.getTitle());
						System.out.println("ID: " + i2.getInternshipID());
						System.out.println("Status: " + i2.getStatus());
						System.out.println("Visibility: " + i2.isVisible());
						System.out.println("---------------------------");
					}
					System.out.println("Press any key to go back");
					sc.next();
				}
				break;
			case "3":
				System.out.print("Internship ID: ");
				String iID = sc.nextLine().trim();
				List<Application> l1 = companyCtrl.getApplicants(iID);
				if (l1.isEmpty()) {
					System.out.println("No applicants found");
				} else {
					System.out.println("\n======= Applicants =======");
					for (Application a : l1) {
						System.out.println("Application ID: " + a.getApplicationID());
						System.out.println("Internship ID:  " + a.getInternshipID());
						System.out.println("Status:         " + a.getStatus());
						System.out.println("--------------------------");
					}
					System.out.print("Press any key to go back");
					sc.next();
				}
				break;
			case "4":
				System.out.println("\n======= Toggle Internship Visibility =======");
				System.out.print("Internship ID: ");
				iID = sc.nextLine().trim();
				if (sys.repository().findInternship(iID).isVisible()) {
					companyCtrl.setVisibility(companyRep, iID, false);
				} else {
					companyCtrl.setVisibility(companyRep, iID, true);
				}
				break;
			case "5":
				new ChangePasswordUI(sys).show(companyRep);
				break;
			case "6":
				LogoutController.logout();
				break;
			default:
				System.out.println("Invalid option. Try again.");
				break;
			}
		}
	}

}