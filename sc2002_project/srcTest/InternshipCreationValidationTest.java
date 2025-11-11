import controller.CompanyController;
import controller.SystemController;
import entities.CompanyRepresentative;
import entities.Internship;
import entities.Repository;
import enums.InternshipLevel;
import services.InternshipManager;

import java.util.List;

public class InternshipCreationValidationTest {

    private static int testsPassed = 0;
    private static int testsFailed = 0;
    private static final int MAX_INTERNSHIPS_PER_REP = 5; // Define the max allowed

    public static void main(String[] args) {
        System.out.println("=== Internship Creation Validation Test ===\n");

        // Run all tests
        testValidInternshipCreation();
        testInvalidDateFormat();
        testNegativeSlots();
        testEmptyTitle();
        testEmptyDescription();
        testMaxInternshipsPerRepresentative();

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

    // Test 1: Valid internship creation should succeed
    private static void testValidInternshipCreation() {
        System.out.println("Test 1: Valid internship creation");
        try {
            Repository repo = new Repository();
            SystemController sys = new SystemController(repo);

            // Create a company representative
            CompanyRepresentative rep = new CompanyRepresentative(
                "rep1@company.com",
                "John Doe",
                "password",
                "TechCorp",
                "Engineering",
                "HR Manager",
                true
            );

            CompanyController controller = new CompanyController(sys);

            // Create valid internship
            controller.createInternship(
                rep,
                "Software Engineer Intern",
                "Work on exciting projects",
                InternshipLevel.BASIC,
                "Computer Science",
                "2025-01-01",
                "2025-06-30",
                5
            );

            // Verify internship was created
            List<Internship> internships = controller.getInternships(rep);

            if (internships.size() > 0) {
                System.out.println("PASSED: Valid internship created successfully\n");
                testsPassed++;
            } else {
                System.out.println("FAILED: Internship was not created\n");
                testsFailed++;
            }

        } catch (Exception e) {
            System.out.println("FAILED: " + e.getMessage() + "\n");
            testsFailed++;
        }
    }

    // Test 2: Invalid date format should fail
    private static void testInvalidDateFormat() {
        System.out.println("Test 2: Invalid date format should be rejected");
        try {
            Internship i = new Internship(
                "I001",
                "Test Internship",
                "Description",
                InternshipLevel.BASIC,
                "Computer Science",
                "2025/01/01", // Invalid format (should be YYYY-MM-DD)
                "2025-06-30",
                enums.InternshipStatus.PENDING,
                "TechCorp",
                "rep1@company.com",
                5,
                false
            );

            System.out.println("FAILED: Invalid date format was accepted\n");
            testsFailed++;

        } catch (IllegalArgumentException e) {
            System.out.println("PASSED: Invalid date format correctly rejected: " + e.getMessage() + "\n");
            testsPassed++;
        } catch (Exception e) {
            System.out.println("FAILED: Unexpected exception: " + e.getMessage() + "\n");
            testsFailed++;
        }
    }

    // Test 3: Negative slots should fail
    private static void testNegativeSlots() {
        System.out.println("Test 3: Negative slots should be rejected");
        try {
            Internship i = new Internship(
                "I002",
                "Test Internship",
                "Description",
                InternshipLevel.BASIC,
                "Computer Science",
                "2025-01-01",
                "2025-06-30",
                enums.InternshipStatus.PENDING,
                "TechCorp",
                "rep2@company.com",
                -5, // Negative slots
                false
            );

            System.out.println("FAILED: Negative slots were accepted\n");
            testsFailed++;

        } catch (IllegalArgumentException e) {
            System.out.println("PASSED: Negative slots correctly rejected: " + e.getMessage() + "\n");
            testsPassed++;
        } catch (Exception e) {
            System.out.println("FAILED: Unexpected exception: " + e.getMessage() + "\n");
            testsFailed++;
        }
    }

    // Test 4: Empty title should fail
    private static void testEmptyTitle() {
        System.out.println("Test 4: Empty title validation");
        try {
            Internship i = new Internship(
                "I003",
                "", // Empty title
                "Description",
                InternshipLevel.BASIC,
                "Computer Science",
                "2025-01-01",
                "2025-06-30",
                enums.InternshipStatus.PENDING,
                "TechCorp",
                "rep3@company.com",
                5,
                false
            );

            System.out.println("FAILED: Empty title was accepted\n");
            testsFailed++;

        } catch (IllegalArgumentException e) {
            System.out.println("PASSED: Empty title correctly rejected: " + e.getMessage() + "\n");
            testsPassed++;
        } catch (Exception e) {
            System.out.println("FAILED: Unexpected exception: " + e.getMessage() + "\n");
            testsFailed++;
        }
    }

    // Test 5: Empty description should fail
    private static void testEmptyDescription() {
        System.out.println("Test 5: Empty description validation");
        try {
            Internship i = new Internship(
                "I004",
                "Test Internship",
                "", // Empty description
                InternshipLevel.BASIC,
                "Computer Science",
                "2025-01-01",
                "2025-06-30",
                enums.InternshipStatus.PENDING,
                "TechCorp",
                "rep4@company.com",
                5,
                false
            );

            System.out.println("FAILED: Empty description was accepted\n");
            testsFailed++;

        } catch (IllegalArgumentException e) {
            System.out.println("PASSED: Empty description correctly rejected: " + e.getMessage() + "\n");
            testsPassed++;
        } catch (Exception e) {
            System.out.println("FAILED: Unexpected exception: " + e.getMessage() + "\n");
            testsFailed++;
        }
    }

    // Test 6: Maximum internships per representative
    private static void testMaxInternshipsPerRepresentative() {
        System.out.println("Test 6: Maximum internships per representative limit");
        try {
            Repository repo = new Repository();
            SystemController sys = new SystemController(repo);

            CompanyRepresentative rep = new CompanyRepresentative(
                "rep5@company.com",
                "Jane Smith",
                "password",
                "BigCorp",
                "Engineering",
                "Recruiter",
                true
            );

            CompanyController controller = new CompanyController(sys);

            boolean limitEnforced = false;
            int createdCount = 0;

            // Try to create more than MAX_INTERNSHIPS_PER_REP internships
            try {
                for (int i = 0; i < MAX_INTERNSHIPS_PER_REP + 2; i++) {
                    controller.createInternship(
                        rep,
                        "Internship " + i,
                        "Description " + i,
                        InternshipLevel.BASIC,
                        "Computer Science",
                        "2025-01-01",
                        "2025-06-30",
                        5
                    );
                    createdCount++;
                }
            } catch (IllegalStateException e) {
                // Expected exception when limit is reached
                limitEnforced = true;
                System.out.println("   Limit enforcement message: " + e.getMessage());
            }

            List<Internship> internships = controller.getInternships(rep);

            System.out.println("   Created: " + createdCount + " internships before hitting limit");
            System.out.println("   Total in system for this rep: " + internships.size());

            if (limitEnforced && internships.size() <= MAX_INTERNSHIPS_PER_REP) {
                System.out.println("PASSED: Maximum internships limit enforced (created " + internships.size() + ", max = " + MAX_INTERNSHIPS_PER_REP + ")\n");
                testsPassed++;
            } else if (!limitEnforced) {
                System.out.println("FAILED: Created " + createdCount + " internships, exceeds max of " + MAX_INTERNSHIPS_PER_REP);
                System.out.println("   Maximum internships per representative limit not enforced\n");
                testsFailed++;
            } else {
                System.out.println("FAILED: Limit enforced but created " + internships.size() + " internships (expected <= " + MAX_INTERNSHIPS_PER_REP + ")\n");
                testsFailed++;
            }

        } catch (Exception e) {
            System.out.println("FAILED: Unexpected exception: " + e.getMessage() + "\n");
            testsFailed++;
        }
    }
}
