package boundary;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import controller.StaffController;
import controller.SystemController;
import entities.Staff;
import enums.ReportCategory;

public class StaffMainUI extends MainUI {
	private SystemController sys;
    private Staff staff;
    private StaffController staffCtrl;
    
    public StaffMainUI(SystemController sys, Staff staff) {
        this.sys = sys;
        this.staff = staff;
        this.staffCtrl = new StaffController(sys);
    }
    
    public void show() {
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("\n======= Staff Menu =======");
            System.out.println("1 Approve Company Registrations");
            System.out.println("2 Approve Internships");
            System.out.println("3 Approve Withdrawals");
            System.out.println("4 Generate Report");
            System.out.println("5 Change Password");
            System.out.println("6 Logout");
            System.out.print("Select: ");
            String s = sc.nextLine().trim();
            switch(s) {
            case "1":
            	System.out.println("\n======= Approve Company Registrations =======");
            	staffCtrl.getPendingCompanies();
            	System.out.print("Company Representative's email: ");
            	String email = sc.nextLine().trim();
            	staffCtrl.decideCompany(email, true);
            	break;
            case "2":
            	System.out.println("\n======= Approve Internships =======");
            	staffCtrl.getPendingInternships();
            	System.out.print("Internship ID: ");
            	String iID = sc.nextLine().trim();
            	staffCtrl.decideInternship(iID, true);
            	break;
            case "3":
            	System.out.println("\n======= Approve Withdrawals =======");
            	staffCtrl.getWithdrawals();
            	System.out.print("Withdrawal ID: ");
            	String wID = sc.nextLine().trim();
            	staffCtrl.decideWithdrawal(wID, true);
            	break;
            case "4":
            	System.out.println("\n======= Generate Report =======");
            	Map<String,String> filters = new HashMap<>();
            	staffCtrl.generateReport(ReportCategory.SUMMARY, filters);
            	break;
            case "5":
            	new ChangePasswordUI(sys).show(staff);
            	break;
            case "6":
            	new LogoutUI().confirm();
            	return;
            default:
            	System.out.println("Invalid option. Try again.");
				break;
            }
        }
    }
}
