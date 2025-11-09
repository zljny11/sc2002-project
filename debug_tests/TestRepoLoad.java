import entities.Repository;
import entities.Report;
import java.util.List;

public class TestRepoLoad {
    public static void main(String[] args) {
        Repository repo = new Repository();
        List<Report> reports = repo.getAllReports();
        System.out.println("Total reports loaded: " + reports.size());
        for (Report r : reports) {
            System.out.println("  - " + r.getID());
        }
    }
}
