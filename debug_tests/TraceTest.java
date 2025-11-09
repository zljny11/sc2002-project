import controller.StaffController;
import controller.SystemController;
import entities.*;
import enums.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TraceTest {
    public static void main(String[] args) {
        System.out.println("=== Step 1: Create first repo ===");
        Repository repo = new Repository();
        System.out.println("Reports loaded: " + repo.getAllReports().size());
        
        System.out.println("\n=== Step 2: Add test data ===");
        CompanyRepresentative rep1 = new CompanyRepresentative("tech@techcorp.com", "Tech Rep", "password", "TechCorp", "IT", "Manager", true);
        repo.updateCompanyRep(rep1);
        System.out.println("After adding company rep, reports in memory: " + repo.getAllReports().size());
        
        System.out.println("\n=== Step 3: Generate report ===");
        SystemController sys = new SystemController(repo);
        StaffController staffController = new StaffController(sys);
        Map<String, String> filters = new HashMap<>();
        staffController.generateReport(ReportCategory.SUMMARY, filters);
        System.out.println("After generating report, reports in memory: " + repo.getAllReports().size());
        List<Report> reportsInMem = repo.getAllReports();
        System.out.println("Last report in memory: " + reportsInMem.get(reportsInMem.size() - 1).getID());
        
        System.out.println("\n=== Step 4: Create new repo to reload ===");
        Repository newRepo = new Repository();
        System.out.println("Reports loaded in new repo: " + newRepo.getAllReports().size());
        List<Report> newReports = newRepo.getAllReports();
        if (newReports.size() > 0) {
            System.out.println("Last report in new repo: " + newReports.get(newReports.size() - 1).getID());
        }
    }
}
