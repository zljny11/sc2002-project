import entities.Repository;
import entities.Report;
import java.util.List;

public class TestSort {
    public static void main(String[] args) {
        Repository repo = new Repository();
        List<Report> reports = repo.getAllReports();
        System.out.println("Total reports: " + reports.size());
        System.out.println("All report IDs in order:");
        for (int i = 0; i < reports.size(); i++) {
            System.out.println("  [" + i + "] " + reports.get(i).getID());
        }
        
        if (reports.size() > 0) {
            System.out.println("\nLast report: " + reports.get(reports.size() - 1).getID());
            if (reports.size() > 1) {
                System.out.println("Second to last: " + reports.get(reports.size() - 2).getID());
            }
        }
    }
}
