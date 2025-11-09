import entities.Repository;
import entities.Report;
import java.util.List;

public class TestActualRepo {
    public static void main(String[] args) {
        Repository repo = new Repository();
        List<Report> reports = repo.getAllReports();
        System.out.println("Total reports loaded: " + reports.size());
        System.out.println("Looking for REP040...");
        for (Report r : reports) {
            System.out.println("  - " + r.getID());
            if (r.getID().equals("REP040")) {
                System.out.println("    Found REP040!");
            }
        }
        
        if (reports.size() > 0) {
            Report last = reports.get(reports.size() - 1);
            System.out.println("\nLast report ID: " + last.getID());
        }
    }
}
