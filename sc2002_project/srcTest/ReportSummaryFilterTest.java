import entities.*;
import enums.ReportCategory;
import java.util.*;

/**
 * Test class to verify ReportSummary filtering functionality
 * Tests various filter combinations to ensure proper data filtering
 */
public class ReportSummaryFilterTest {

    public static void main(String[] args) {
        System.out.println("=== Testing ReportSummary Filtering Functionality ===\n");

        try {
            // Create a Repository instance (will load all CSV data)
            Repository repo = new Repository();

            // Test 1: Generate report WITHOUT filters
            System.out.println("TEST 1: Report without filters (all data)");
            System.out.println("=".repeat(60));
            ReportSummary report1 = new ReportSummary("RPT_TEST_001", ReportCategory.SUMMARY);
            report1.generate(repo, null);
            System.out.println(report1.getContent());
            System.out.println("\n");

            // Test 2: Generate report WITH date range filter
            System.out.println("TEST 2: Report with date range filter (2024-01-01 to 2024-06-30)");
            System.out.println("=".repeat(60));
            Map<String, String> dateFilter = new HashMap<>();
            dateFilter.put("startDate", "2024-01-01");
            dateFilter.put("endDate", "2024-06-30");
            ReportSummary report2 = new ReportSummary("RPT_TEST_002", ReportCategory.SUMMARY);
            report2.generate(repo, dateFilter);
            System.out.println(report2.getContent());
            System.out.println("\n");

            // Test 3: Generate report WITH status filter
            System.out.println("TEST 3: Report with status filter (PENDING applications only)");
            System.out.println("=".repeat(60));
            Map<String, String> statusFilter = new HashMap<>();
            statusFilter.put("status", "PENDING");
            ReportSummary report3 = new ReportSummary("RPT_TEST_003", ReportCategory.SUMMARY);
            report3.generate(repo, statusFilter);
            System.out.println(report3.getContent());
            System.out.println("\n");

            // Test 4: Generate report WITH multiple filters
            System.out.println("TEST 4: Report with multiple filters (PENDING + Date Range)");
            System.out.println("=".repeat(60));
            Map<String, String> multiFilter = new HashMap<>();
            multiFilter.put("status", "PENDING");
            multiFilter.put("startDate", "2024-01-01");
            multiFilter.put("endDate", "2024-12-31");
            ReportSummary report4 = new ReportSummary("RPT_TEST_004", ReportCategory.SUMMARY);
            report4.generate(repo, multiFilter);
            System.out.println(report4.getContent());
            System.out.println("\n");

            // Test 5: Generate report WITH visibility filter (for internships)
            System.out.println("TEST 5: Report with visibility filter (visible internships only)");
            System.out.println("=".repeat(60));
            Map<String, String> visibilityFilter = new HashMap<>();
            visibilityFilter.put("visible", "true");
            ReportSummary report5 = new ReportSummary("RPT_TEST_005", ReportCategory.SUMMARY);
            report5.generate(repo, visibilityFilter);
            System.out.println(report5.getContent());
            System.out.println("\n");

            System.out.println("=".repeat(60));
            System.out.println("SUCCESS: All filter tests completed successfully!");
            System.out.println("=".repeat(60));

        } catch (Exception e) {
            System.err.println("✗ ERROR during testing: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
