import controller.StaffController;
import controller.SystemController;
import entities.*;
import enums.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SingleTest {
    public static void main(String[] args) {
        System.out.println("Before generating report:");
        Repository repo1 = new Repository();
        System.out.println("  Reports in repo: " + repo1.getAllReports().size());
        
        // Setup and generate
        SystemController sys = new SystemController(repo1);
        StaffController staffController = new StaffController(sys);
        
        Map<String, String> filters = new HashMap<>();
        staffController.generateReport(ReportCategory.SUMMARY, filters);
        System.out.println("  Reports after generate: " + repo1.getAllReports().size());
        
        // Reload
        System.out.println("\nAfter reloading:");
        Repository repo2 = new Repository();
        System.out.println("  Reports in new repo: " + repo2.getAllReports().size());
        
        List<Report> reports = repo2.getAllReports();
        if (reports.size() > 0) {
            System.out.println("  First report: " + reports.get(0).getID());
            System.out.println("  Last report: " + reports.get(reports.size() - 1).getID());
        }
    }
}
