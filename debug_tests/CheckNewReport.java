import entities.Repository;
import entities.Report;

public class CheckNewReport {
    public static void main(String[] args) {
        Repository repo = new Repository();
        Report r = repo.findReport("R4000");
        if (r != null) {
            System.out.println("Found R4000:");
            System.out.println("  Content length: " + r.getContent().length());
            System.out.println("  Content:\n" + r.getContent());
        } else {
            System.out.println("R4000 not found!");
        }
    }
}
