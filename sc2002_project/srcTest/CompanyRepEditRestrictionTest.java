import controller.CompanyController;
import controller.StaffController;
import controller.SystemController;
import entities.CompanyRepresentative;
import entities.Internship;
import entities.Repository;
import enums.InternshipLevel;
import enums.InternshipStatus;

import java.util.List;

public class CompanyRepEditRestrictionTest {

    private static int testsPassed = 0;
    private static int testsFailed = 0;

    public static void main(String[] args) {
        System.out.println("=== Company Representative Edit Restriction Test ===\n");
        System.out.println("Testing: Edit functionality should be restricted after approval by Staff\n");

        // Run tests
        testCanEditPendingInternship();
        testCannotEditApprovedInternship();
        testCannotEditRejectedInternship();
        testCanEditMultiplePendingInternships();
        testEditRestrictionMessage();

        // Print summary
        System.out.println("\n=== Test Summary ===");
        System.out.println("Tests Passed: " + testsPassed);
        System.out.println("Tests Failed: " + testsFailed);
        System.out.println("Total Tests: " + (testsPassed + testsFailed));

        if (testsFailed == 0) {
            System.out.println("\nPASSED: All tests passed!");
            System.out.println("Edit restrictions are properly enforced after Staff approval.");
        } else {
            System.out.println("\nFAILED: Some tests failed!");
            System.out.println("⚠️  SECURITY RISK: Company Representatives may be able to edit approved internships!");
        }
    }

    // Test 1: Company rep CAN edit PENDING internships
    private static void testCanEditPendingInternship() {
        System.out.println("Test 1: Company Rep CAN edit PENDING internships");
        try {
            Repository repo = new Repository();
            SystemController sys = new SystemController(repo);

            CompanyRepresentative rep = new CompanyRepresentative(
                "edit.pending@company.com",
                "Edit Pending Rep",
                "password",
                "EditPendingCorp",
                "HR",
                "Manager",
                true
            );

            CompanyController controller = new CompanyController(sys);

            // Create internship (status: PENDING)
            controller.createInternship(
                rep,
                "Pending Internship",
                "This is pending",
                InternshipLevel.BASIC,
                "Computer Science",
                "2025-03-01",
                "2025-08-31",
                3
            );

            List<Internship> internships = controller.getInternships(rep);
            String internshipID = internships.get(0).getInternshipID();

            System.out.println("   Initial status: " + internships.get(0).getStatus());
            System.out.println("   Initial visibility: " + internships.get(0).isVisible());

            // Try to edit (set visibility)
            boolean canEdit = true;
            try {
                controller.setVisibility(rep, internshipID, true);
            } catch (Exception e) {
                canEdit = false;
                System.out.println("   Cannot edit: " + e.getMessage());
            }

            // Check if visibility changed
            internships = controller.getInternships(rep);
            boolean visibilityChanged = internships.get(0).isVisible();

            System.out.println("   After edit visibility: " + visibilityChanged);

            if (canEdit && visibilityChanged) {
                System.out.println("PASSED: Can edit PENDING internships\n");
                testsPassed++;
            } else {
                System.out.println("FAILED: Cannot edit PENDING internships (should be allowed)\n");
                testsFailed++;
            }

        } catch (Exception e) {
            System.out.println("FAILED: " + e.getMessage() + "\n");
            testsFailed++;
        }
    }

    // Test 2: Company rep CANNOT edit APPROVED internships
    private static void testCannotEditApprovedInternship() {
        System.out.println("Test 2: Company Rep CANNOT edit APPROVED internships");
        try {
            Repository repo = new Repository();
            SystemController sys = new SystemController(repo);

            CompanyRepresentative rep = new CompanyRepresentative(
                "edit.approved@company.com",
                "Edit Approved Rep",
                "password",
                "EditApprovedCorp",
                "Engineering",
                "Director",
                true
            );

            CompanyController companyController = new CompanyController(sys);
            StaffController staffController = new StaffController(sys);

            // Create internship
            companyController.createInternship(
                rep,
                "Approved Internship",
                "This will be approved",
                InternshipLevel.INTERMEDIATE,
                "Data Science",
                "2025-04-01",
                "2025-09-30",
                5
            );

            List<Internship> internships = companyController.getInternships(rep);
            String internshipID = internships.get(0).getInternshipID();

            // Staff approves the internship
            staffController.decideInternship(internshipID, true);

            // Verify it's approved
            internships = companyController.getInternships(rep);
            InternshipStatus status = internships.get(0).getStatus();
            boolean initialVisibility = internships.get(0).isVisible();

            System.out.println("   Status after approval: " + status);
            System.out.println("   Visibility after approval: " + initialVisibility);

            // Try to edit (should fail or be prevented)
            boolean editPrevented = false;
            String errorMessage = "";

            try {
                companyController.setVisibility(rep, internshipID, false);
            } catch (IllegalStateException e) {
                editPrevented = true;
                errorMessage = e.getMessage();
                System.out.println("   Edit blocked: " + errorMessage);
            } catch (Exception e) {
                editPrevented = true;
                errorMessage = e.getMessage();
                System.out.println("   Edit blocked: " + errorMessage);
            }

            // Check if visibility actually changed
            internships = companyController.getInternships(rep);
            boolean visibilityChanged = (internships.get(0).isVisible() != initialVisibility);

            System.out.println("   Visibility changed: " + visibilityChanged);

            if (editPrevented || !visibilityChanged) {
                System.out.println("PASSED: Cannot edit APPROVED internships\n");
                testsPassed++;
            } else {
                System.out.println("⚠️  FAILED: Can still edit APPROVED internships (SECURITY RISK!)\n");
                System.out.println("   Expected: Edit should be blocked");
                System.out.println("   Actual: Edit was allowed");
                testsFailed++;
            }

        } catch (Exception e) {
            System.out.println("FAILED: " + e.getMessage() + "\n");
            testsFailed++;
        }
    }

    // Test 3: Company rep CANNOT edit REJECTED internships
    private static void testCannotEditRejectedInternship() {
        System.out.println("Test 3: Company Rep CANNOT edit REJECTED internships");
        try {
            Repository repo = new Repository();
            SystemController sys = new SystemController(repo);

            CompanyRepresentative rep = new CompanyRepresentative(
                "edit.rejected@company.com",
                "Edit Rejected Rep",
                "password",
                "EditRejectedCorp",
                "Marketing",
                "Lead",
                true
            );

            CompanyController companyController = new CompanyController(sys);
            StaffController staffController = new StaffController(sys);

            // Create internship
            companyController.createInternship(
                rep,
                "Rejected Internship",
                "This will be rejected",
                InternshipLevel.BASIC,
                "Business",
                "2025-05-01",
                "2025-10-31",
                2
            );

            List<Internship> internships = companyController.getInternships(rep);
            String internshipID = internships.get(0).getInternshipID();

            // Staff rejects the internship
            staffController.decideInternship(internshipID, false);

            internships = companyController.getInternships(rep);
            InternshipStatus status = internships.get(0).getStatus();
            boolean initialVisibility = internships.get(0).isVisible();

            System.out.println("   Status after rejection: " + status);
            System.out.println("   Initial visibility: " + initialVisibility);

            // Try to edit (should fail or be prevented)
            boolean editPrevented = false;

            try {
                companyController.setVisibility(rep, internshipID, true);
            } catch (Exception e) {
                editPrevented = true;
                System.out.println("   Edit blocked: " + e.getMessage());
            }

            // Check if visibility changed
            internships = companyController.getInternships(rep);
            boolean visibilityChanged = (internships.get(0).isVisible() != initialVisibility);

            if (editPrevented || !visibilityChanged) {
                System.out.println("PASSED: Cannot edit REJECTED internships\n");
                testsPassed++;
            } else {
                System.out.println("⚠️  FAILED: Can still edit REJECTED internships\n");
                testsFailed++;
            }

        } catch (Exception e) {
            System.out.println("FAILED: " + e.getMessage() + "\n");
            testsFailed++;
        }
    }

    // Test 4: Can edit multiple PENDING internships
    private static void testCanEditMultiplePendingInternships() {
        System.out.println("Test 4: Can edit multiple PENDING internships simultaneously");
        try {
            Repository repo = new Repository();
            SystemController sys = new SystemController(repo);

            CompanyRepresentative rep = new CompanyRepresentative(
                "edit.multiple@company.com",
                "Edit Multiple Rep",
                "password",
                "EditMultipleCorp",
                "IT",
                "Manager",
                true
            );

            CompanyController controller = new CompanyController(sys);

            // Create 3 pending internships
            for (int i = 1; i <= 3; i++) {
                controller.createInternship(rep, "Pending Internship " + i, "Description " + i,
                        InternshipLevel.BASIC, "CS", "2025-06-01", "2025-11-30", 2);
            }

            List<Internship> internships = controller.getInternships(rep);

            int editedCount = 0;
            for (Internship i : internships) {
                if (i.getStatus() == InternshipStatus.PENDING) {
                    try {
                        controller.setVisibility(rep, i.getInternshipID(), true);
                        editedCount++;
                    } catch (Exception e) {
                        // Edit failed
                    }
                }
            }

            System.out.println("   Created 3 PENDING internships");
            System.out.println("   Successfully edited: " + editedCount);

            if (editedCount == 3) {
                System.out.println("PASSED: Can edit all PENDING internships\n");
                testsPassed++;
            } else {
                System.out.println("FAILED: Could only edit " + editedCount + " out of 3\n");
                testsFailed++;
            }

        } catch (Exception e) {
            System.out.println("FAILED: " + e.getMessage() + "\n");
            testsFailed++;
        }
    }

    // Test 5: Verify error message when trying to edit approved internship
    private static void testEditRestrictionMessage() {
        System.out.println("Test 5: Verify appropriate error message for edit restriction");
        try {
            Repository repo = new Repository();
            SystemController sys = new SystemController(repo);

            CompanyRepresentative rep = new CompanyRepresentative(
                "edit.message@company.com",
                "Edit Message Rep",
                "password",
                "EditMessageCorp",
                "Operations",
                "Coordinator",
                true
            );

            CompanyController companyController = new CompanyController(sys);
            StaffController staffController = new StaffController(sys);

            // Create and approve internship
            companyController.createInternship(rep, "Message Test Internship", "Test",
                    InternshipLevel.BASIC, "CS", "2025-07-01", "2025-12-31", 3);

            List<Internship> internships = companyController.getInternships(rep);
            staffController.decideInternship(internships.get(0).getInternshipID(), true);

            // Try to edit and check error message
            String errorMessage = null;
            try {
                companyController.setVisibility(rep, internships.get(0).getInternshipID(), false);
            } catch (IllegalStateException e) {
                errorMessage = e.getMessage();
            } catch (Exception e) {
                errorMessage = e.getMessage();
            }

            boolean hasAppropriateMessage = errorMessage != null &&
                    (errorMessage.toLowerCase().contains("approved") ||
                     errorMessage.toLowerCase().contains("cannot edit") ||
                     errorMessage.toLowerCase().contains("restricted"));

            if (errorMessage != null) {
                System.out.println("   Error message: " + errorMessage);
            }

            if (hasAppropriateMessage) {
                System.out.println("PASSED: Appropriate error message provided\n");
                testsPassed++;
            } else if (errorMessage == null) {
                System.out.println("⚠️  FAILED: No error message (edit was allowed)\n");
                testsFailed++;
            } else {
                System.out.println("⚠️  FAILED: Error message not clear enough\n");
                testsFailed++;
            }

        } catch (Exception e) {
            System.out.println("FAILED: " + e.getMessage() + "\n");
            testsFailed++;
        }
    }
}
