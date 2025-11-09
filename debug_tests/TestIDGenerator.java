import utils.IDGenerator;

public class TestIDGenerator {
    public static void main(String[] args) {
        System.out.println("=== Testing ID Generator ===\n");
        
        System.out.println("Generating IDs (should start after max existing IDs):");
        System.out.println("Report ID 1: " + IDGenerator.nextReportID());
        System.out.println("Report ID 2: " + IDGenerator.nextReportID());
        System.out.println("Report ID 3: " + IDGenerator.nextReportID());
        
        System.out.println("\nInternship ID 1: " + IDGenerator.nextInternshipID());
        System.out.println("Internship ID 2: " + IDGenerator.nextInternshipID());
        
        System.out.println("\nApplication ID 1: " + IDGenerator.nextApplicationID());
        System.out.println("Application ID 2: " + IDGenerator.nextApplicationID());
        
        System.out.println("\nWithdrawal ID 1: " + IDGenerator.nextWithdrawalID());
        System.out.println("Withdrawal ID 2: " + IDGenerator.nextWithdrawalID());
    }
}
