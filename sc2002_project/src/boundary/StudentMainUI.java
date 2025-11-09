package boundary;

import java.util.List;
import java.util.Scanner;

import controller.StudentController;
import controller.SystemController;
import entities.Application;
import entities.Internship;
import entities.Student;

public class StudentMainUI extends MainUI {
	private SystemController sys;
	private Student student;
	private StudentController studentCtrl;
	
	
	public StudentMainUI(SystemController sys, Student student) {
		this.sys = sys;
		this.student = student;
		this.studentCtrl = new StudentController(sys);
	}
	
	public void show() {
		Scanner sc = new Scanner(System.in);
		while (true) {
			System.out.println("\n======= Student Menu =======");
			System.out.println("1 View Available Internships");
			System.out.println("2 Apply for Internship");
			System.out.println("3 View My Applications");
			System.out.println("4 Withdraw Application");
			System.out.println("5 Accept Offer");
			System.out.println("6 Change Password");
			System.out.println("7 Logout");
			System.out.print("Select: ");

			String s = sc.next();
			sc.nextLine(); // Consume leftover newline
			switch(s) {
			case "1":
				List<Internship> l = studentCtrl.viewAvailable(student);
				if (l.isEmpty()) {
					System.out.println("No internships available");
				} else {
					System.out.println("\n======= Available Internships =======");
					for (Internship i : l) {
						System.out.println("\n" + i.getTitle());
						System.out.println("Internship ID:    " + i.getInternshipID());
						System.out.println("Company:          " + i.getCompanyName());
						System.out.println("Level:            " + i.getLevel());
						System.out.println("Preferred Major:  " + i.getPreferredMajor());
						System.out.println("Available Slots:  " + i.getSlots());
						System.out.println("Closing Date:     " + i.getClosingDate());
						System.out.println("Description:      " + i.getDescription());
						System.out.println("---------------------------");
						}
					System.out.print("Press Enter to go back...");
					sc.nextLine();
				}
				break;
			case "2":
				System.out.println("\n======= Internship Application =======");
				System.out.print("Internship ID: ");
				String iID = sc.next();
				boolean ok = studentCtrl.apply(student, iID);
				if (!ok) { System.out.println("Application failed"); }
				break;
			case "3":
				List<Application> i = studentCtrl.myApplications(student);
				if (i.isEmpty()) {
					System.out.println("No applications found");
				} else {
					System.out.println("\n======= Applications =======");
					for (Application a : i) {
						System.out.println("Application ID: " + a.getApplicationID());
						System.out.println("Internship ID:  " + a.getInternshipID());
						System.out.println("Status:         " + a.getStatus());
						System.out.println("----------------------------");
					}
					System.out.print("Press Enter to go back...");
					sc.nextLine();
				}
				break;
			case "4":
				System.out.println("\n======= Wtihdraw Application =======");
				System.out.print("Application ID: ");
				String aID = sc.next();
				studentCtrl.requestWithdrawal(student, aID);
				break;
			case "5":
				System.out.println("\n======= Accept Offer =======");
				System.out.print("Application ID: ");
				aID = sc.next();
				ok = studentCtrl.acceptOffer(student, aID);
				if (!ok) { System.out.println("Accept failed"); }
				break;
			case "6":
				new ChangePasswordUI(sys).show(student);
				break;
			case "7":
				new LogoutUI().confirm();
				return;
			default:
				System.out.println("Invalid option. Try again.");
				break;
			}
		}
	}

}
