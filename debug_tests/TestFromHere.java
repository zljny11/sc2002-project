import entities.Repository;
import entities.Report;
import java.util.List;
import java.io.File;

public class TestFromHere {
    public static void main(String[] args) {
        System.out.println("Working directory: " + System.getProperty("user.dir"));
        System.out.println("Looking for project root...");
        
        File current = new File(System.getProperty("user.dir"));
        System.out.println("  Current: " + current.getAbsolutePath());
        System.out.println("  Has .idea: " + new File(current, ".idea").exists());
        System.out.println("  Has .git: " + new File(current, ".git").exists());
        System.out.println("  Has data: " + new File(current, "data").exists());
        
        File parent = current.getParentFile();
        if (parent != null) {
            System.out.println("  Parent: " + parent.getAbsolutePath());
            System.out.println("  Parent has .idea: " + new File(parent, ".idea").exists());
            System.out.println("  Parent has .git: " + new File(parent, ".git").exists());
            System.out.println("  Parent has data: " + new File(parent, "data").exists());
        }
        
        System.out.println("\nLoading repository...");
        Repository repo = new Repository();
        List<Report> reports = repo.getAllReports();
        System.out.println("Total reports loaded: " + reports.size());
    }
}
