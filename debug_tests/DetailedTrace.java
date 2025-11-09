import controller.StaffController;
import controller.SystemController;
import entities.*;
import enums.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DetailedTrace {
    public static void main(String[] args) {
        Repository repo = new Repository();
        SystemController sys = new SystemController(repo);
        StaffController staffController = new StaffController(sys);
        
        System.out.println("Before generateReport:");
        List<Report> before = repo.getAllReports();
        System.out.println("  Total reports: " + before.size());
        for (Report r : before) {
            if (r.getID().startsWith("R4")) {
                System.out.println("    Found: " + r.getID());
            }
        }
        
        Map<String, String> filters = new HashMap<>();
        staffController.generateReport(ReportCategory.SUMMARY, filters);
        
        System.out.println("\nAfter generateReport:");
        List<Report> after = repo.getAllReports();
        System.out.println("  Total reports: " + after.size());
        for (Report r : after) {
            if (r.getID().startsWith("R4")) {
                System.out.println("    Found: " + r.getID());
            }
        }
    }
}
