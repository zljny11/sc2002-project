import controller.CompanyController;
import controller.SystemController;
import entities.CompanyRepresentative;
import entities.Internship;
import entities.Repository;
import enums.InternshipLevel;

import java.util.List;

public class DateValidationTest {

    private static int testsPassed = 0;
    private static int testsFailed = 0;

    public static void main(String[] args) {
        System.out.println("=== Date Validation Test ===\n");
        System.out.println("Testing: Opening date must be before closing date\n");

        // Run tests
        testValidDateOrder();
        testInvalidDateOrder_SameDate();
        testInvalidDateOrder_OpenAfterClose();
        testModifyWithInvalidDates();
        testValidDateModification();

        // Print summary
        System.out.println("\n=== Test Summary ===");
        System.out.println("Tests Passed: " + testsPassed);
        System.out.println("Tests Failed: " + testsFailed);
        System.out.println("Total Tests: " + (testsPassed + testsFailed));

        if (testsFailed == 0) {
            System.out.println("\nPASSED: All tests passed!");
            System.out.println("Date validation is working correctly.");
        } else {
            System.out.println("\nFAILED: Some tests failed!");
        }
    }

    // Test 1: Valid date order (open before close)
    private static void testValidDateOrder() {
        System.out.println("Test 1: Valid date order - opening before closing");
        try {
            Repository repo = new Repository();
            SystemController sys = new SystemController(repo);

            CompanyRepresentative rep = new CompanyRepresentative(
                "valid.date@company.com",
                "Valid Date Rep",
                "password",
                "ValidDateCorp",
                "HR",
                "Manager",
                true
            );

            CompanyController controller = new CompanyController(sys);

            // Create with valid dates
            controller.createInternship(
                rep,
                "Valid Date Internship",
                "Opening before closing",
                InternshipLevel.BASIC,
                "Computer Science",
                "2025-01-01",  // Opening
                "2025-12-31",  // Closing
                3
            );

            List<Internship> internships = controller.getInternships(rep);

            if (internships.size() == 1) {
                Internship i = internships.get(0);
                System.out.println("   Opening date: " + i.getOpeningDate());
                System.out.println("   Closing date: " + i.getClosingDate());
                System.out.println("PASSED: Valid date order accepted\n");
                testsPassed++;
            } else {
                System.out.println("FAILED: Internship not created\n");
                testsFailed++;
            }

        } catch (Exception e) {
            System.out.println("FAILED: " + e.getMessage() + "\n");
            testsFailed++;
        }
    }

    // Test 2: Invalid - same date
    private static void testInvalidDateOrder_SameDate() {
        System.out.println("Test 2: Invalid date order - opening equals closing");
        try {
            Repository repo = new Repository();
            SystemController sys = new SystemController(repo);

            CompanyRepresentative rep = new CompanyRepresentative(
                "same.date@company.com",
                "Same Date Rep",
                "password",
                "SameDateCorp",
                "Engineering",
                "Lead",
                true
            );

            CompanyController controller = new CompanyController(sys);

            boolean exceptionThrown = false;
            String errorMessage = "";

            try {
                // Try to create with same dates
                controller.createInternship(
                    rep,
                    "Same Date Internship",
                    "Opening equals closing",
                    InternshipLevel.BASIC,
                    "Computer Science",
                    "2025-06-01",  // Opening
                    "2025-06-01",  // Closing (same!)
                    3
                );
            } catch (IllegalArgumentException e) {
                exceptionThrown = true;
                errorMessage = e.getMessage();
                System.out.println("   Error caught: " + errorMessage);
            }

            if (exceptionThrown && errorMessage.contains("Opening date must be before closing date")) {
                System.out.println("PASSED: Same date rejected correctly\n");
                testsPassed++;
            } else {
                System.out.println("FAILED: Same date should be rejected\n");
                testsFailed++;
            }

        } catch (Exception e) {
            System.out.println("FAILED: Unexpected error: " + e.getMessage() + "\n");
            testsFailed++;
        }
    }

    // Test 3: Invalid - opening after closing
    private static void testInvalidDateOrder_OpenAfterClose() {
        System.out.println("Test 3: Invalid date order - opening after closing");
        try {
            Repository repo = new Repository();
            SystemController sys = new SystemController(repo);

            CompanyRepresentative rep = new CompanyRepresentative(
                "reverse.date@company.com",
                "Reverse Date Rep",
                "password",
                "ReverseDateCorp",
                "IT",
                "Manager",
                true
            );

            CompanyController controller = new CompanyController(sys);

            boolean exceptionThrown = false;
            String errorMessage = "";

            try {
                // Try to create with reversed dates
                controller.createInternship(
                    rep,
                    "Reverse Date Internship",
                    "Opening after closing",
                    InternshipLevel.BASIC,
                    "Computer Science",
                    "2025-12-31",  // Opening (later)
                    "2025-01-01",  // Closing (earlier!)
                    3
                );
            } catch (IllegalArgumentException e) {
                exceptionThrown = true;
                errorMessage = e.getMessage();
                System.out.println("   Error caught: " + errorMessage);
            }

            if (exceptionThrown && errorMessage.contains("Opening date must be before closing date")) {
                System.out.println("PASSED: Reversed dates rejected correctly\n");
                testsPassed++;
            } else {
                System.out.println("FAILED: Reversed dates should be rejected\n");
                testsFailed++;
            }

        } catch (Exception e) {
            System.out.println("FAILED: Unexpected error: " + e.getMessage() + "\n");
            testsFailed++;
        }
    }

    // Test 4: Modify with invalid dates
    private static void testModifyWithInvalidDates() {
        System.out.println("Test 4: Cannot modify to invalid date order");
        try {
            Repository repo = new Repository();
            SystemController sys = new SystemController(repo);

            CompanyRepresentative rep = new CompanyRepresentative(
                "modify.invalid@company.com",
                "Modify Invalid Rep",
                "password",
                "ModifyInvalidCorp",
                "Marketing",
                "Lead",
                true
            );

            CompanyController controller = new CompanyController(sys);

            // Create with valid dates
            controller.createInternship(
                rep,
                "To Modify",
                "Will try invalid modification",
                InternshipLevel.BASIC,
                "Computer Science",
                "2025-03-01",
                "2025-09-30",
                3
            );

            List<Internship> internships = controller.getInternships(rep);
            String internshipID = internships.get(0).getInternshipID();

            System.out.println("   Original: 2025-03-01 to 2025-09-30");

            boolean exceptionThrown = false;
            String errorMessage = "";

            try {
                // Try to modify to invalid date order
                controller.modifyInternship(
                    rep,
                    internshipID,
                    "Modified Title",
                    "Modified Description",
                    "Computer Science",
                    "2025-12-31",  // Opening (after closing!)
                    "2025-01-01",  // Closing
                    3
                );
            } catch (IllegalArgumentException e) {
                exceptionThrown = true;
                errorMessage = e.getMessage();
                System.out.println("   Error caught: " + errorMessage);
            }

            // Check that dates weren't changed
            internships = controller.getInternships(rep);
            Internship modified = internships.get(0);

            if (exceptionThrown &&
                modified.getOpeningDate().equals("2025-03-01") &&
                modified.getClosingDate().equals("2025-09-30")) {
                System.out.println("PASSED: Invalid date modification rejected\n");
                testsPassed++;
            } else {
                System.out.println("FAILED: Invalid dates should be rejected\n");
                testsFailed++;
            }

        } catch (Exception e) {
            System.out.println("FAILED: " + e.getMessage() + "\n");
            testsFailed++;
        }
    }

    // Test 5: Valid date modification
    private static void testValidDateModification() {
        System.out.println("Test 5: Can modify to valid date order");
        try {
            Repository repo = new Repository();
            SystemController sys = new SystemController(repo);

            CompanyRepresentative rep = new CompanyRepresentative(
                "modify.valid@company.com",
                "Modify Valid Rep",
                "password",
                "ModifyValidCorp",
                "Operations",
                "Manager",
                true
            );

            CompanyController controller = new CompanyController(sys);

            // Create with valid dates
            controller.createInternship(
                rep,
                "To Modify Valid",
                "Will modify to valid dates",
                InternshipLevel.BASIC,
                "Computer Science",
                "2025-03-01",
                "2025-09-30",
                3
            );

            List<Internship> internships = controller.getInternships(rep);
            String internshipID = internships.get(0).getInternshipID();

            System.out.println("   Original: 2025-03-01 to 2025-09-30");

            // Modify to different but valid dates
            controller.modifyInternship(
                rep,
                internshipID,
                "Modified Title",
                "Modified Description",
                "Computer Science",
                "2025-04-01",  // New opening (still before closing)
                "2025-11-30",  // New closing
                5
            );

            internships = controller.getInternships(rep);
            Internship modified = internships.get(0);

            System.out.println("   Modified: " + modified.getOpeningDate() + " to " + modified.getClosingDate());

            if (modified.getOpeningDate().equals("2025-04-01") &&
                modified.getClosingDate().equals("2025-11-30")) {
                System.out.println("PASSED: Valid date modification accepted\n");
                testsPassed++;
            } else {
                System.out.println("FAILED: Valid modification should succeed\n");
                testsFailed++;
            }

        } catch (Exception e) {
            System.out.println("FAILED: " + e.getMessage() + "\n");
            testsFailed++;
        }
    }
}
