import controller.StaffController;
import controller.SystemController;
import entities.*;
import enums.*;

import java.util.HashMap;
import java.util.Map;

public class ReportGenerationTest {

    private static int testsPassed = 0;
    private static int testsFailed = 0;

    public static void main(String[] args) {
        System.out.println("=== Report Generation Test ===\n");

        // Run tests
        testReportGenerationWithoutFilters();
        testReportGenerationWithStatusFilter();
        testReportGenerationWithMajorFilter();
        testReportGenerationWithCompanyFilter();
        testReportGenerationWithLevelFilter();
        testReportGenerationWithMultipleFilters();

        // Print summary
        System.out.println("\n=== Test Summary ===");
        System.out.println("Tests Passed: " + testsPassed);
        System.out.println("Tests Failed: " + testsFailed);
        System.out.println("Total Tests: " + (testsPassed + testsFailed));

        if (testsFailed == 0) {
            System.out.println("\nPASSED: All tests passed!");
        } else {
            System.out.println("\nFAILED: Some tests failed!");
        }
    }

    // Test 1: Report generation without filters
    private static void testReportGenerationWithoutFilters() {
        System.out.println("Test 1: Report generation without filters");
        try {
            Repository repo = new Repository();
            SystemController sys = new SystemController(repo);
            StaffController staffController = new StaffController(sys);

            // Create test data
            setupTestData(repo);

            // Generate report without filters
            Map<String, String> filters = new HashMap<>();
            staffController.generateReport(ReportCategory.SUMMARY, filters);

            // Create a new repository to reload data from file
            Repository newRepo = new Repository();
            // Find the newly generated report by ID (R4000)
            Report latestReport = newRepo.findReport("R4000");

            if (latestReport == null) {
                System.out.println("FAILED: Report R4000 not found in repository\n");
                testsFailed++;
                return;
            }

            System.out.println("   Report ID: " + latestReport.getID());
            System.out.println("   Report content length: " + latestReport.getContent().length());
            System.out.println("   Report content:\n" + latestReport.getContent());

            if (latestReport.getContent().length() > 50) { // Increased threshold
                System.out.println("PASSED: Report generated without filters\n");
                testsPassed++;
            } else {
                System.out.println("FAILED: Report content is too short or empty\n");
                testsFailed++;
            }

        } catch (Exception e) {
            System.out.println("FAILED: " + e.getMessage() + "\n");
            e.printStackTrace();
            testsFailed++;
        }
    }

    // Test 2: Report generation with placement status filter
    private static void testReportGenerationWithStatusFilter() {
        System.out.println("Test 2: Report generation with placement status filter");
        try {
            Repository repo = new Repository();
            SystemController sys = new SystemController(repo);
            StaffController staffController = new StaffController(sys);

            // Create test data
            setupTestData(repo);

            // Generate report with status filter
            Map<String, String> filters = new HashMap<>();
            filters.put("status", "APPROVED");
            staffController.generateReport(ReportCategory.SUMMARY, filters);

            // Create a new repository to reload data from file
            Repository newRepo = new Repository();
            // Find the newly generated report by ID (R4001)
            Report latestReport = newRepo.findReport("R4001");

            if (latestReport == null) {
                System.out.println("FAILED: Report R4001 not found in repository\n");
                testsFailed++;
                return;
            }

            System.out.println("   Report ID: " + latestReport.getID());
            System.out.println("   Report content length: " + latestReport.getContent().length());
            System.out.println("   Report content:\n" + latestReport.getContent());

            // Check if the report contains filter criteria section
            boolean hasFilterCriteria = latestReport.getContent().contains("=== Filter Criteria ===");
            boolean hasStatusFilter = latestReport.getContent().contains("status: APPROVED");

            if (latestReport.getContent().length() > 50 && hasFilterCriteria && hasStatusFilter) {
                System.out.println("PASSED: Report generated with placement status filter\n");
                testsPassed++;
            } else {
                System.out.println("FAILED: Report does not contain status filter information\n");
                System.out.println("   Content length: " + latestReport.getContent().length());
                System.out.println("   Has filter criteria: " + hasFilterCriteria);
                System.out.println("   Has status filter: " + hasStatusFilter);
                testsFailed++;
            }

        } catch (Exception e) {
            System.out.println("FAILED: " + e.getMessage() + "\n");
            e.printStackTrace();
            testsFailed++;
        }
    }

    // Test 3: Report generation with major filter
    private static void testReportGenerationWithMajorFilter() {
        System.out.println("Test 3: Report generation with major filter");
        try {
            Repository repo = new Repository();
            SystemController sys = new SystemController(repo);
            StaffController staffController = new StaffController(sys);

            // Create test data
            setupTestData(repo);

            // Generate report with major filter (preferredMajor)
            Map<String, String> filters = new HashMap<>();
            filters.put("preferredMajor", "Computer Science");
            staffController.generateReport(ReportCategory.SUMMARY, filters);

            // Create a new repository to reload data from file
            Repository newRepo = new Repository();
            // Find the newly generated report by ID (R4002)
            Report latestReport = newRepo.findReport("R4002");

            if (latestReport == null) {
                System.out.println("FAILED: Report R4002 not found in repository\n");
                testsFailed++;
                return;
            }

            System.out.println("   Report ID: " + latestReport.getID());
            System.out.println("   Report content length: " + latestReport.getContent().length());
            System.out.println("   Report content:\n" + latestReport.getContent());

            // Check if the report contains filter criteria section
            boolean hasFilterCriteria = latestReport.getContent().contains("=== Filter Criteria ===");
            boolean hasMajorFilter = latestReport.getContent().contains("preferredMajor: Computer Science");

            if (latestReport.getContent().length() > 50 && hasFilterCriteria && hasMajorFilter) {
                System.out.println("PASSED: Report generated with major filter\n");
                testsPassed++;
            } else {
                System.out.println("FAILED: Report does not contain major filter information\n");
                System.out.println("   Content length: " + latestReport.getContent().length());
                System.out.println("   Has filter criteria: " + hasFilterCriteria);
                System.out.println("   Has major filter: " + hasMajorFilter);
                testsFailed++;
            }

        } catch (Exception e) {
            System.out.println("FAILED: " + e.getMessage() + "\n");
            e.printStackTrace();
            testsFailed++;
        }
    }

    // Test 4: Report generation with company filter
    private static void testReportGenerationWithCompanyFilter() {
        System.out.println("Test 4: Report generation with company filter");
        try {
            Repository repo = new Repository();
            SystemController sys = new SystemController(repo);
            StaffController staffController = new StaffController(sys);

            // Create test data
            setupTestData(repo);

            // Generate report with company name filter
            Map<String, String> filters = new HashMap<>();
            filters.put("companyName", "TechCorp");
            staffController.generateReport(ReportCategory.SUMMARY, filters);

            // Create a new repository to reload data from file
            Repository newRepo = new Repository();
            // Find the newly generated report by ID (R4003)
            Report latestReport = newRepo.findReport("R4003");

            if (latestReport == null) {
                System.out.println("FAILED: Report R4003 not found in repository\n");
                testsFailed++;
                return;
            }

            System.out.println("   Report ID: " + latestReport.getID());
            System.out.println("   Report content length: " + latestReport.getContent().length());
            System.out.println("   Report content:\n" + latestReport.getContent());

            // Check if the report contains filter criteria section
            boolean hasFilterCriteria = latestReport.getContent().contains("=== Filter Criteria ===");
            boolean hasCompanyFilter = latestReport.getContent().contains("companyName: TechCorp");

            if (latestReport.getContent().length() > 50 && hasFilterCriteria && hasCompanyFilter) {
                System.out.println("PASSED: Report generated with company filter\n");
                testsPassed++;
            } else {
                System.out.println("FAILED: Report does not contain company filter information\n");
                System.out.println("   Content length: " + latestReport.getContent().length());
                System.out.println("   Has filter criteria: " + hasFilterCriteria);
                System.out.println("   Has company filter: " + hasCompanyFilter);
                testsFailed++;
            }

        } catch (Exception e) {
            System.out.println("FAILED: " + e.getMessage() + "\n");
            e.printStackTrace();
            testsFailed++;
        }
    }

    // Test 5: Report generation with level filter
    private static void testReportGenerationWithLevelFilter() {
        System.out.println("Test 5: Report generation with level filter");
        try {
            Repository repo = new Repository();
            SystemController sys = new SystemController(repo);
            StaffController staffController = new StaffController(sys);

            // Create test data
            setupTestData(repo);

            // Generate report with level filter
            Map<String, String> filters = new HashMap<>();
            filters.put("level", "INTERMEDIATE");
            staffController.generateReport(ReportCategory.SUMMARY, filters);

            // Create a new repository to reload data from file
            Repository newRepo = new Repository();
            // Find the newly generated report by ID (R4004)
            Report latestReport = newRepo.findReport("R4004");

            if (latestReport == null) {
                System.out.println("FAILED: Report R4004 not found in repository\n");
                testsFailed++;
                return;
            }

            System.out.println("   Report ID: " + latestReport.getID());
            System.out.println("   Report content length: " + latestReport.getContent().length());
            System.out.println("   Report content:\n" + latestReport.getContent());

            // Check if the report contains filter criteria section
            boolean hasFilterCriteria = latestReport.getContent().contains("=== Filter Criteria ===");
            boolean hasLevelFilter = latestReport.getContent().contains("level: INTERMEDIATE");

            if (latestReport.getContent().length() > 50 && hasFilterCriteria && hasLevelFilter) {
                System.out.println("PASSED: Report generated with level filter\n");
                testsPassed++;
            } else {
                System.out.println("FAILED: Report does not contain level filter information\n");
                System.out.println("   Content length: " + latestReport.getContent().length());
                System.out.println("   Has filter criteria: " + hasFilterCriteria);
                System.out.println("   Has level filter: " + hasLevelFilter);
                testsFailed++;
            }

        } catch (Exception e) {
            System.out.println("FAILED: " + e.getMessage() + "\n");
            e.printStackTrace();
            testsFailed++;
        }
    }

    // Test 6: Report generation with multiple filters
    private static void testReportGenerationWithMultipleFilters() {
        System.out.println("Test 6: Report generation with multiple filters");
        try {
            Repository repo = new Repository();
            SystemController sys = new SystemController(repo);
            StaffController staffController = new StaffController(sys);

            // Create test data
            setupTestData(repo);

            // Generate report with multiple filters
            Map<String, String> filters = new HashMap<>();
            filters.put("status", "APPROVED");
            filters.put("level", "BASIC");
            filters.put("companyName", "StartupInc");
            staffController.generateReport(ReportCategory.SUMMARY, filters);

            // Create a new repository to reload data from file
            Repository newRepo = new Repository();
            // Find the newly generated report by ID (R4005)
            Report latestReport = newRepo.findReport("R4005");

            if (latestReport == null) {
                System.out.println("FAILED: Report R4005 not found in repository\n");
                testsFailed++;
                return;
            }

            System.out.println("   Report ID: " + latestReport.getID());
            System.out.println("   Report content length: " + latestReport.getContent().length());
            System.out.println("   Report content:\n" + latestReport.getContent());

            // Check if the report contains filter criteria section
            boolean hasFilterCriteria = latestReport.getContent().contains("=== Filter Criteria ===");
            boolean hasStatusFilter = latestReport.getContent().contains("status: APPROVED");
            boolean hasLevelFilter = latestReport.getContent().contains("level: BASIC");
            boolean hasCompanyFilter = latestReport.getContent().contains("companyName: StartupInc");

            boolean hasAllFilters = hasFilterCriteria && hasStatusFilter &&
                    hasLevelFilter && hasCompanyFilter;

            if (latestReport.getContent().length() > 50 && hasAllFilters) {
                System.out.println("PASSED: Report generated with multiple filters\n");
                testsPassed++;
            } else {
                System.out.println("FAILED: Report does not contain all filter information\n");
                System.out.println("   Content length: " + latestReport.getContent().length());
                System.out.println("   Has filter criteria: " + hasFilterCriteria);
                System.out.println("   Has status filter: " + hasStatusFilter);
                System.out.println("   Has level filter: " + hasLevelFilter);
                System.out.println("   Has company filter: " + hasCompanyFilter);
                testsFailed++;
            }

        } catch (Exception e) {
            System.out.println("FAILED: " + e.getMessage() + "\n");
            e.printStackTrace();
            testsFailed++;
        }
    }

    // Helper method to set up test data
    private static void setupTestData(Repository repo) {
        // Create company representatives
        CompanyRepresentative rep1 = new CompanyRepresentative(
                "tech@techcorp.com",
                "Tech Rep",
                "password",
                "TechCorp",
                "IT",
                "Manager",
                true
        );
        repo.updateCompanyRep(rep1);

        CompanyRepresentative rep2 = new CompanyRepresentative(
                "startup@startupinc.com",
                "Startup Rep",
                "password",
                "StartupInc",
                "Engineering",
                "Director",
                true
        );
        repo.updateCompanyRep(rep2);

        // Create students
        Student student1 = new Student(
                "U1234567A",
                "Student One",
                "password",
                "student1@e.ntu.edu.sg",
                3,
                "Computer Science"
        );
        repo.updateStudent(student1);

        Student student2 = new Student(
                "U7654321B",
                "Student Two",
                "password",
                "student2@e.ntu.edu.sg",
                2,
                "Business Administration"
        );
        repo.updateStudent(student2);

        // Create internships
        Internship internship1 = new Internship(
                "I001",
                "Software Engineering Intern",
                "Work on software projects",
                InternshipLevel.BASIC,
                "Computer Science",
                "2025-06-01",
                "2025-12-01",
                InternshipStatus.APPROVED,
                "TechCorp",
                rep1.getID(),
                5,
                true
        );
        repo.updateInternship(internship1);

        Internship internship2 = new Internship(
                "I002",
                "Data Analysis Intern",
                "Analyze business data",
                InternshipLevel.INTERMEDIATE,
                "Business Administration",
                "2025-07-01",
                "2025-12-31",
                InternshipStatus.PENDING,
                "TechCorp",
                rep1.getID(),
                3,
                false
        );
        repo.updateInternship(internship2);

        Internship internship3 = new Internship(
                "I003",
                "Web Development Intern",
                "Build web applications",
                InternshipLevel.BASIC,
                "Computer Science",
                "2025-08-01",
                "2026-01-31",
                InternshipStatus.APPROVED,
                "StartupInc",
                rep2.getID(),
                2,
                true
        );
        repo.updateInternship(internship3);

        // Create applications
        Application app1 = new Application(
                "A001",
                "I001",
                "U1234567A",
                ApplicationStatus.SUCCESSFUL,
                "2025-05-01",
                true
        );
        repo.updateApplication(app1);

        Application app2 = new Application(
                "A002",
                "I002",
                "U7654321B",
                ApplicationStatus.PENDING,
                "2025-06-15",
                false
        );
        repo.updateApplication(app2);

        // Create withdrawal requests
        WithdrawalRequest wr1 = new WithdrawalRequest(
                "W001",
                "A001",
                "U1234567A",
                ApprovalStatus.PENDING,
                "2025-05-20"
        );
        repo.updateWithdrawal(wr1);
    }
}