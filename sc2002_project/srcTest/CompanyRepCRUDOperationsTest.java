import controller.CompanyController;
import controller.StaffController;
import controller.SystemController;
import entities.CompanyRepresentative;
import entities.Internship;
import entities.Repository;
import enums.InternshipLevel;
import enums.InternshipStatus;

import java.util.List;

public class CompanyRepCRUDOperationsTest {

    private static int testsPassed = 0;
    private static int testsFailed = 0;

    public static void main(String[] args) {
        System.out.println("=== Company Representative CRUD Operations Test ===\n");
        System.out.println("Testing: Company Representatives can add, modify (before approval), and remove opportunities\n");

        // Run tests
        testAddNewOpportunity();
        testModifyPendingOpportunity();
        testCannotModifyApprovedOpportunity();
        testCannotModifyRejectedOpportunity();
        testRemovePendingOpportunity();
        testCannotRemoveApprovedOpportunity();
        testCannotRemoveRejectedOpportunity();
        testCannotModifyOtherRepOpportunity();
        testCannotRemoveOtherRepOpportunity();
        testMultipleModificationsBeforeApproval();

        // Print summary
        System.out.println("\n=== Test Summary ===");
        System.out.println("Tests Passed: " + testsPassed);
        System.out.println("Tests Failed: " + testsFailed);
        System.out.println("Total Tests: " + (testsPassed + testsFailed));

        if (testsFailed == 0) {
            System.out.println("\nPASSED: All tests passed!");
            System.out.println("CRUD operations work correctly with proper access control.");
        } else {
            System.out.println("\nFAILED: Some tests failed!");
        }
    }

    // Test 1: Add new opportunity
    private static void testAddNewOpportunity() {
        System.out.println("Test 1: Company Rep can add new internship opportunity");
        try {
            Repository repo = new Repository();
            SystemController sys = new SystemController(repo);

            CompanyRepresentative rep = new CompanyRepresentative(
                "add.test@company.com",
                "Add Test Rep",
                "password",
                "AddTestCorp",
                "HR",
                "Manager",
                true
            );

            CompanyController controller = new CompanyController(sys);

            int initialCount = controller.getInternships(rep).size();

            // Add new opportunity
            controller.createInternship(
                rep,
                "New Software Internship",
                "Backend Development Position",
                InternshipLevel.INTERMEDIATE,
                "Computer Science",
                "2025-03-01",
                "2025-12-31",
                5
            );

            List<Internship> internships = controller.getInternships(rep);
            int finalCount = internships.size();

            System.out.println("   Initial count: " + initialCount);
            System.out.println("   Final count: " + finalCount);

            if (finalCount == initialCount + 1) {
                Internship newInternship = internships.get(internships.size() - 1);
                System.out.println("   Created internship ID: " + newInternship.getInternshipID());
                System.out.println("   Status: " + newInternship.getStatus());
                System.out.println("   Title: " + newInternship.getTitle());

                if (newInternship.getStatus() == InternshipStatus.PENDING) {
                    System.out.println("PASSED: New opportunity added successfully\n");
                    testsPassed++;
                } else {
                    System.out.println("FAILED: New opportunity should have PENDING status\n");
                    testsFailed++;
                }
            } else {
                System.out.println("FAILED: Opportunity not added\n");
                testsFailed++;
            }

        } catch (Exception e) {
            System.out.println("FAILED: " + e.getMessage() + "\n");
            testsFailed++;
        }
    }

    // Test 2: Modify PENDING opportunity
    private static void testModifyPendingOpportunity() {
        System.out.println("Test 2: Company Rep can modify PENDING opportunity");
        try {
            Repository repo = new Repository();
            SystemController sys = new SystemController(repo);

            CompanyRepresentative rep = new CompanyRepresentative(
                "modify.pending@company.com",
                "Modify Pending Rep",
                "password",
                "ModifyPendingCorp",
                "Engineering",
                "Lead",
                true
            );

            CompanyController controller = new CompanyController(sys);

            // Create opportunity
            controller.createInternship(
                rep,
                "Original Title",
                "Original Description",
                InternshipLevel.BASIC,
                "Computer Science",
                "2025-04-01",
                "2025-11-30",
                3
            );

            List<Internship> internships = controller.getInternships(rep);
            String internshipID = internships.get(0).getInternshipID();
            InternshipStatus status = internships.get(0).getStatus();

            System.out.println("   Internship status: " + status);

            // Try to modify (currently only visibility can be modified)
            boolean modifySuccess = true;
            try {
                controller.modifyInternship(rep, internshipID, "Updated Title",
                    "Updated Description", "Computer Science", "2025-05-01", "2025-12-31", 5);
            } catch (Exception e) {
                modifySuccess = false;
                System.out.println("   Modify failed: " + e.getMessage());
            }

            // Check if modification succeeded
            internships = controller.getInternships(rep);
            Internship modified = internships.get(0);

            if (modifySuccess && modified.getTitle().equals("Updated Title") &&
                modified.getDescription().equals("Updated Description")) {
                System.out.println("   Original title: Original Title");
                System.out.println("   Updated title: " + modified.getTitle());
                System.out.println("   Updated description: " + modified.getDescription());
                System.out.println("PASSED: PENDING opportunity modified successfully\n");
                testsPassed++;
            } else {
                System.out.println("FAILED: Could not modify PENDING opportunity\n");
                testsFailed++;
            }

        } catch (Exception e) {
            System.out.println("FAILED: " + e.getMessage() + "\n");
            testsFailed++;
        }
    }

    // Test 3: Cannot modify APPROVED opportunity
    private static void testCannotModifyApprovedOpportunity() {
        System.out.println("Test 3: Company Rep cannot modify APPROVED opportunity");
        try {
            Repository repo = new Repository();
            SystemController sys = new SystemController(repo);

            CompanyRepresentative rep = new CompanyRepresentative(
                "modify.approved@company.com",
                "Modify Approved Rep",
                "password",
                "ModifyApprovedCorp",
                "IT",
                "Manager",
                true
            );

            CompanyController controller = new CompanyController(sys);
            StaffController staffController = new StaffController(sys);

            // Create and approve opportunity
            controller.createInternship(
                rep,
                "Approved Internship",
                "This will be approved",
                InternshipLevel.BASIC,
                "Computer Science",
                "2025-05-01",
                "2025-12-31",
                2
            );

            List<Internship> internships = controller.getInternships(rep);
            String internshipID = internships.get(0).getInternshipID();

            staffController.decideInternship(internshipID, true);

            internships = controller.getInternships(rep);
            String originalTitle = internships.get(0).getTitle();

            System.out.println("   Status after approval: " + internships.get(0).getStatus());
            System.out.println("   Original title: " + originalTitle);

            // Try to modify
            boolean modifyBlocked = false;
            try {
                controller.modifyInternship(rep, internshipID, "Modified Title",
                    "Modified Description", "Data Science", "2025-06-01", "2025-12-31", 3);
            } catch (IllegalStateException e) {
                modifyBlocked = true;
                System.out.println("   Modification blocked: " + e.getMessage());
            }

            // Check if title unchanged
            internships = controller.getInternships(rep);
            String finalTitle = internships.get(0).getTitle();

            if (modifyBlocked && originalTitle.equals(finalTitle)) {
                System.out.println("PASSED: Cannot modify APPROVED opportunity\n");
                testsPassed++;
            } else {
                System.out.println("FAILED: APPROVED opportunity should not be modifiable\n");
                testsFailed++;
            }

        } catch (Exception e) {
            System.out.println("FAILED: " + e.getMessage() + "\n");
            testsFailed++;
        }
    }

    // Test 4: Cannot modify REJECTED opportunity
    private static void testCannotModifyRejectedOpportunity() {
        System.out.println("Test 4: Company Rep cannot modify REJECTED opportunity");
        try {
            Repository repo = new Repository();
            SystemController sys = new SystemController(repo);

            CompanyRepresentative rep = new CompanyRepresentative(
                "modify.rejected@company.com",
                "Modify Rejected Rep",
                "password",
                "ModifyRejectedCorp",
                "Marketing",
                "Lead",
                true
            );

            CompanyController controller = new CompanyController(sys);
            StaffController staffController = new StaffController(sys);

            // Create and reject opportunity
            controller.createInternship(
                rep,
                "Rejected Internship",
                "This will be rejected",
                InternshipLevel.BASIC,
                "Business",
                "2025-06-01",
                "2025-12-31",
                2
            );

            List<Internship> internships = controller.getInternships(rep);
            String internshipID = internships.get(0).getInternshipID();

            staffController.decideInternship(internshipID, false);

            System.out.println("   Status after rejection: " + internships.get(0).getStatus());

            // Try to modify
            boolean modifyBlocked = false;
            try {
                controller.modifyInternship(rep, internshipID, "Modified Title",
                    "Modified Description", "Business", "2025-07-01", "2025-12-31", 2);
            } catch (IllegalStateException e) {
                modifyBlocked = true;
                System.out.println("   Modification blocked: " + e.getMessage());
            }

            if (modifyBlocked) {
                System.out.println("PASSED: Cannot modify REJECTED opportunity\n");
                testsPassed++;
            } else {
                System.out.println("FAILED: REJECTED opportunity should not be modifiable\n");
                testsFailed++;
            }

        } catch (Exception e) {
            System.out.println("FAILED: " + e.getMessage() + "\n");
            testsFailed++;
        }
    }

    // Test 5: Remove PENDING opportunity
    private static void testRemovePendingOpportunity() {
        System.out.println("Test 5: Company Rep can remove PENDING opportunity");
        try {
            Repository repo = new Repository();
            SystemController sys = new SystemController(repo);

            CompanyRepresentative rep = new CompanyRepresentative(
                "remove.pending@company.com",
                "Remove Pending Rep",
                "password",
                "RemovePendingCorp",
                "Operations",
                "Manager",
                true
            );

            CompanyController controller = new CompanyController(sys);

            // Create opportunity
            controller.createInternship(
                rep,
                "To Be Removed",
                "This will be removed",
                InternshipLevel.BASIC,
                "Computer Science",
                "2025-07-01",
                "2025-12-31",
                3
            );

            List<Internship> internships = controller.getInternships(rep);
            int initialCount = internships.size();
            String internshipID = internships.get(0).getInternshipID();

            System.out.println("   Initial count: " + initialCount);
            System.out.println("   Internship ID to remove: " + internshipID);

            // Remove opportunity
            boolean removeSuccess = true;
            try {
                controller.removeInternship(rep, internshipID);
            } catch (Exception e) {
                removeSuccess = false;
                System.out.println("   Remove failed: " + e.getMessage());
            }

            internships = controller.getInternships(rep);
            int finalCount = internships.size();

            System.out.println("   Final count: " + finalCount);

            if (removeSuccess && finalCount == initialCount - 1) {
                System.out.println("PASSED: PENDING opportunity removed successfully\n");
                testsPassed++;
            } else {
                System.out.println("FAILED: Could not remove PENDING opportunity\n");
                testsFailed++;
            }

        } catch (Exception e) {
            System.out.println("FAILED: " + e.getMessage() + "\n");
            testsFailed++;
        }
    }

    // Test 6: Cannot remove APPROVED opportunity
    private static void testCannotRemoveApprovedOpportunity() {
        System.out.println("Test 6: Company Rep cannot remove APPROVED opportunity");
        try {
            Repository repo = new Repository();
            SystemController sys = new SystemController(repo);

            CompanyRepresentative rep = new CompanyRepresentative(
                "remove.approved@company.com",
                "Remove Approved Rep",
                "password",
                "RemoveApprovedCorp",
                "HR",
                "Director",
                true
            );

            CompanyController controller = new CompanyController(sys);
            StaffController staffController = new StaffController(sys);

            // Create and approve opportunity
            controller.createInternship(
                rep,
                "Approved Cannot Remove",
                "This is approved",
                InternshipLevel.BASIC,
                "Computer Science",
                "2025-08-01",
                "2025-12-31",
                2
            );

            List<Internship> internships = controller.getInternships(rep);
            String internshipID = internships.get(0).getInternshipID();

            staffController.decideInternship(internshipID, true);

            int countBeforeRemove = controller.getInternships(rep).size();
            System.out.println("   Status: " + controller.getInternships(rep).get(0).getStatus());

            // Try to remove
            boolean removeBlocked = false;
            try {
                controller.removeInternship(rep, internshipID);
            } catch (IllegalStateException e) {
                removeBlocked = true;
                System.out.println("   Removal blocked: " + e.getMessage());
            }

            int countAfterRemove = controller.getInternships(rep).size();

            if (removeBlocked && countBeforeRemove == countAfterRemove) {
                System.out.println("PASSED: Cannot remove APPROVED opportunity\n");
                testsPassed++;
            } else {
                System.out.println("FAILED: APPROVED opportunity should not be removable\n");
                testsFailed++;
            }

        } catch (Exception e) {
            System.out.println("FAILED: " + e.getMessage() + "\n");
            testsFailed++;
        }
    }

    // Test 7: Cannot remove REJECTED opportunity
    private static void testCannotRemoveRejectedOpportunity() {
        System.out.println("Test 7: Company Rep cannot remove REJECTED opportunity");
        try {
            Repository repo = new Repository();
            SystemController sys = new SystemController(repo);

            CompanyRepresentative rep = new CompanyRepresentative(
                "remove.rejected@company.com",
                "Remove Rejected Rep",
                "password",
                "RemoveRejectedCorp",
                "IT",
                "Manager",
                true
            );

            CompanyController controller = new CompanyController(sys);
            StaffController staffController = new StaffController(sys);

            // Create and reject opportunity
            controller.createInternship(
                rep,
                "Rejected Cannot Remove",
                "This is rejected",
                InternshipLevel.BASIC,
                "Computer Science",
                "2025-09-01",
                "2025-12-31",
                2
            );

            List<Internship> internships = controller.getInternships(rep);
            String internshipID = internships.get(0).getInternshipID();

            staffController.decideInternship(internshipID, false);

            int countBeforeRemove = controller.getInternships(rep).size();

            // Try to remove
            boolean removeBlocked = false;
            try {
                controller.removeInternship(rep, internshipID);
            } catch (IllegalStateException e) {
                removeBlocked = true;
                System.out.println("   Removal blocked: " + e.getMessage());
            }

            int countAfterRemove = controller.getInternships(rep).size();

            if (removeBlocked && countBeforeRemove == countAfterRemove) {
                System.out.println("PASSED: Cannot remove REJECTED opportunity\n");
                testsPassed++;
            } else {
                System.out.println("FAILED: REJECTED opportunity should not be removable\n");
                testsFailed++;
            }

        } catch (Exception e) {
            System.out.println("FAILED: " + e.getMessage() + "\n");
            testsFailed++;
        }
    }

    // Test 8: Cannot modify other rep's opportunity
    private static void testCannotModifyOtherRepOpportunity() {
        System.out.println("Test 8: Company Rep cannot modify other representative's opportunity");
        try {
            Repository repo = new Repository();
            SystemController sys = new SystemController(repo);

            CompanyRepresentative rep1 = new CompanyRepresentative(
                "rep1.modify@company.com",
                "Rep One",
                "password",
                "Company1",
                "HR",
                "Manager",
                true
            );

            CompanyRepresentative rep2 = new CompanyRepresentative(
                "rep2.modify@company.com",
                "Rep Two",
                "password",
                "Company2",
                "Engineering",
                "Lead",
                true
            );

            CompanyController controller = new CompanyController(sys);

            // Rep1 creates opportunity
            controller.createInternship(
                rep1,
                "Rep1 Internship",
                "Created by Rep1",
                InternshipLevel.BASIC,
                "Computer Science",
                "2025-10-01",
                "2025-12-31",
                3
            );

            List<Internship> rep1Internships = controller.getInternships(rep1);
            String internshipID = rep1Internships.get(0).getInternshipID();
            String originalTitle = rep1Internships.get(0).getTitle();

            System.out.println("   Rep1's internship: " + internshipID);

            // Rep2 tries to modify Rep1's opportunity
            boolean modifyBlocked = false;
            try {
                controller.modifyInternship(rep2, internshipID, "Hacked Title",
                    "Hacked Description", "Hacking", "2025-11-01", "2025-12-31", 1);
            } catch (Exception e) {
                modifyBlocked = true;
                System.out.println("   Modification blocked (no match or error)");
            }

            // Check if title unchanged
            rep1Internships = controller.getInternships(rep1);
            String finalTitle = rep1Internships.get(0).getTitle();

            if (originalTitle.equals(finalTitle)) {
                System.out.println("PASSED: Cannot modify other rep's opportunity\n");
                testsPassed++;
            } else {
                System.out.println("FAILED: Other rep's opportunity was modified (SECURITY RISK!)\n");
                testsFailed++;
            }

        } catch (Exception e) {
            System.out.println("FAILED: " + e.getMessage() + "\n");
            testsFailed++;
        }
    }

    // Test 9: Cannot remove other rep's opportunity
    private static void testCannotRemoveOtherRepOpportunity() {
        System.out.println("Test 9: Company Rep cannot remove other representative's opportunity");
        try {
            Repository repo = new Repository();
            SystemController sys = new SystemController(repo);

            CompanyRepresentative rep1 = new CompanyRepresentative(
                "rep1.remove@company.com",
                "Rep One Remove",
                "password",
                "RemoveCompany1",
                "HR",
                "Manager",
                true
            );

            CompanyRepresentative rep2 = new CompanyRepresentative(
                "rep2.remove@company.com",
                "Rep Two Remove",
                "password",
                "RemoveCompany2",
                "Engineering",
                "Lead",
                true
            );

            CompanyController controller = new CompanyController(sys);

            // Rep1 creates opportunity
            controller.createInternship(
                rep1,
                "Rep1 Internship Remove",
                "Created by Rep1",
                InternshipLevel.BASIC,
                "Computer Science",
                "2025-11-01",
                "2025-12-31",
                3
            );

            List<Internship> rep1Internships = controller.getInternships(rep1);
            String internshipID = rep1Internships.get(0).getInternshipID();
            int initialCount = rep1Internships.size();

            // Rep2 tries to remove Rep1's opportunity
            try {
                controller.removeInternship(rep2, internshipID);
            } catch (Exception e) {
                System.out.println("   Removal blocked (no match or error)");
            }

            // Check if still exists
            rep1Internships = controller.getInternships(rep1);
            int finalCount = rep1Internships.size();

            if (initialCount == finalCount) {
                System.out.println("PASSED: Cannot remove other rep's opportunity\n");
                testsPassed++;
            } else {
                System.out.println("FAILED: Other rep's opportunity was removed (SECURITY RISK!)\n");
                testsFailed++;
            }

        } catch (Exception e) {
            System.out.println("FAILED: " + e.getMessage() + "\n");
            testsFailed++;
        }
    }

    // Test 10: Multiple modifications before approval
    private static void testMultipleModificationsBeforeApproval() {
        System.out.println("Test 10: Company Rep can make multiple modifications before approval");
        try {
            Repository repo = new Repository();
            SystemController sys = new SystemController(repo);

            CompanyRepresentative rep = new CompanyRepresentative(
                "multi.modify@company.com",
                "Multi Modify Rep",
                "password",
                "MultiModifyCorp",
                "Operations",
                "Manager",
                true
            );

            CompanyController controller = new CompanyController(sys);

            // Create opportunity
            controller.createInternship(
                rep,
                "Version 1 Title",
                "Version 1 Description",
                InternshipLevel.BASIC,
                "Computer Science",
                "2025-12-01",
                "2025-12-31",
                3
            );

            List<Internship> internships = controller.getInternships(rep);
            String internshipID = internships.get(0).getInternshipID();

            System.out.println("   Version 1: " + internships.get(0).getTitle());

            // Modify 1st time
            controller.modifyInternship(rep, internshipID, "Version 2 Title",
                "Version 2 Description", "Computer Science", "2025-12-01", "2025-12-31", 4);

            internships = controller.getInternships(rep);
            System.out.println("   Version 2: " + internships.get(0).getTitle());

            // Modify 2nd time
            controller.modifyInternship(rep, internshipID, "Version 3 Title",
                "Version 3 Description", "Data Science", "2025-12-01", "2025-12-31", 5);

            internships = controller.getInternships(rep);
            System.out.println("   Version 3: " + internships.get(0).getTitle());

            Internship finalInternship = internships.get(0);

            if (finalInternship.getTitle().equals("Version 3 Title") &&
                finalInternship.getDescription().equals("Version 3 Description") &&
                finalInternship.getSlots() == 5) {
                System.out.println("PASSED: Multiple modifications allowed before approval\n");
                testsPassed++;
            } else {
                System.out.println("FAILED: Multiple modifications not working correctly\n");
                testsFailed++;
            }

        } catch (Exception e) {
            System.out.println("FAILED: " + e.getMessage() + "\n");
            testsFailed++;
        }
    }
}
