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
            System.out.println("5 View Reports");
            System.out.println("6 Change Password");
            System.out.println("7 Logout");
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
            	viewReports(sc);
            	break;
            case "6":
            	new ChangePasswordUI(sys).show(staff);
            	break;
            case "7":
            	if (new LogoutUI().confirm()) {
            		return;
            	}
            	break;
            default:
            	System.out.println("Invalid option. Try again.");
				break;
            }
        }
    }

    private void viewReports(Scanner sc) {
        System.out.println("\n======= View Reports =======");
        System.out.println("Filter by category:");
        System.out.println("1 All Reports");
        System.out.println("2 SUMMARY Reports");
        System.out.println("3 INTERNSHIP Reports");
        System.out.println("4 APPLICATION Reports");
        System.out.println("5 WITHDRAWAL Reports");
        System.out.print("Select: ");

        String choice = sc.nextLine().trim();
        ReportCategory filterCategory = null;

        switch(choice) {
        case "1":
            filterCategory = null; // Show all
            break;
        case "2":
            filterCategory = ReportCategory.SUMMARY;
            break;
        case "3":
            filterCategory = ReportCategory.INTERNSHIP;
            break;
        case "4":
            filterCategory = ReportCategory.APPLICATION;
            break;
        case "5":
            filterCategory = ReportCategory.WITHDRAWAL;
            break;
        default:
            System.out.println("Invalid option. Showing all reports.");
            break;
        }

        java.util.List<entities.Report> reports = sys.repository().getAllReports();

        // Create a final copy of the filter category for use in lambda
        final ReportCategory finalFilterCategory = filterCategory;

        if (finalFilterCategory != null) {
            reports = reports.stream()
                .filter(r -> r.getCategory() == finalFilterCategory)
                .collect(java.util.stream.Collectors.toList());
        }

        if (reports.isEmpty()) {
            System.out.println("\nNo reports found.");
        } else {
            System.out.println("\n======= Reports " +
                (filterCategory != null ? "(" + filterCategory + ")" : "(All)") + " =======");
            System.out.println("Found " + reports.size() + " report(s)\n");

            for (entities.Report report : reports) {
                System.out.println("----------------------------------------");
                System.out.println("Report ID:   " + report.getID());
                System.out.println("Category:    " + report.getCategory());
                System.out.println("Generated:   " + report.getGeneratedDate());
                System.out.println("\nContent:");
                System.out.println(report.getContent());
                System.out.println("----------------------------------------\n");
            }
        }

        System.out.print("Press Enter to continue...");
        sc.nextLine();
    }
}
