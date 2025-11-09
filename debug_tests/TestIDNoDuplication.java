import utils.IDGenerator;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class TestIDNoDuplication {
    public static void main(String[] args) {
        System.out.println("=== Test: ID Generator Avoids Duplication ===\n");
        
        // Check what's the current max Report ID in CSV
        System.out.println("Checking existing Report IDs in data/reports.csv:");
        String maxReportID = findMaxReportID();
        System.out.println("  Current max R-format ID: " + maxReportID);
        
        // Generate new IDs
        System.out.println("\nGenerating new Report IDs:");
        String newID1 = IDGenerator.nextReportID();
        String newID2 = IDGenerator.nextReportID();
        String newID3 = IDGenerator.nextReportID();
        
        System.out.println("  New ID 1: " + newID1);
        System.out.println("  New ID 2: " + newID2);
        System.out.println("  New ID 3: " + newID3);
        
        // Verify no duplication
        int maxNum = extractNumber(maxReportID);
        int newNum1 = extractNumber(newID1);
        int newNum2 = extractNumber(newID2);
        int newNum3 = extractNumber(newID3);
        
        System.out.println("\nVerification:");
        if (newNum1 > maxNum) {
            System.out.println("  ✅ " + newID1 + " > " + maxReportID + " - No duplication!");
        } else {
            System.out.println("  ❌ " + newID1 + " <= " + maxReportID + " - DUPLICATION DETECTED!");
        }
        
        if (newNum2 == newNum1 + 1 && newNum3 == newNum2 + 1) {
            System.out.println("  ✅ IDs are sequential - Correct!");
        } else {
            System.out.println("  ❌ IDs are not sequential - ERROR!");
        }
        
        System.out.println("\n=== Test Result ===");
        if (newNum1 > maxNum && newNum2 == newNum1 + 1 && newNum3 == newNum2 + 1) {
            System.out.println("✅ All checks passed! ID duplication is prevented.");
        } else {
            System.out.println("❌ Some checks failed!");
        }
    }
    
    private static String findMaxReportID() {
        String maxID = "R4000";
        int maxNum = 4000;
        
        try (BufferedReader br = new BufferedReader(new FileReader("../data/reports.csv"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length > 0 && parts[0].matches("^R\\d+$")) {
                    int num = Integer.parseInt(parts[0].substring(1));
                    if (num > maxNum) {
                        maxNum = num;
                        maxID = parts[0];
                    }
                }
            }
        } catch (IOException e) {
            // File not found or error reading
        }
        
        return maxID;
    }
    
    private static int extractNumber(String id) {
        if (id == null || id.length() <= 1) return 0;
        try {
            return Integer.parseInt(id.substring(1));
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
