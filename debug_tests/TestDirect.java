import java.io.File;

public class TestDirect {
    public static void main(String[] args) {
        String userDir = System.getProperty("user.dir");
        System.out.println("Current directory: " + userDir);
        
        File dataDir = new File(userDir, "data");
        System.out.println("Data directory exists: " + dataDir.exists());
        System.out.println("Data directory path: " + dataDir.getAbsolutePath());
        
        File reportsFile = new File(dataDir, "reports.csv");
        System.out.println("Reports file exists: " + reportsFile.exists());
        System.out.println("Reports file path: " + reportsFile.getAbsolutePath());
    }
}
