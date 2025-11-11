import controller.CompanyController;
import controller.StaffController;
import controller.SystemController;
import entities.CompanyRepresentative;
import entities.Internship;
import entities.Repository;
import enums.InternshipLevel;
import enums.InternshipStatus;

import java.util.List;

public class CompanyRepViewInternshipStatusTest {

    private static int testsPassed = 0;
    private static int testsFailed = 0;

    public static void main(String[] args) {
        System.out.println("=== Company Representative View Internship Status Test ===\n");

        // Run tests
        testViewPendingInternships();
        testViewApprovedInternships();
        testViewRejectedInternships();
        testViewMixedStatusInternships();
        testFilterInternshipsByStatus();

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

    // Test 1: View pending internships
    private static void testViewPendingInternships() {
        System.out.println("Test 1: Company Representative can view PENDING internships");
        try {
            Repository repo = new Repository();
            SystemController sys = new SystemController(repo);

            CompanyRepresentative rep = new CompanyRepresentative(
                "pending.rep@company.com",
                "Pending Rep",
                "password",
                "PendingCorp",
                "HR",
                "Manager",
                true
            );

            CompanyController companyController = new CompanyController(sys);

            // Create an internship (defaults to PENDING status)
            companyController.createInternship(
                rep,
                "Pending Software Internship",
                "This internship is pending approval",
                InternshipLevel.BASIC,
                "Computer Science",
                "2025-02-01",
                "2025-07-31",
                3
            );

            // Retrieve internships for this representative
            List<Internship> internships = companyController.getInternships(rep);

            boolean foundPending = false;
            for (Internship i : internships) {
                if (i.getStatus() == InternshipStatus.PENDING) {
                    System.out.println("   Found PENDING internship: " + i.getTitle());
                    System.out.println("   Status: " + i.getStatus());
                    foundPending = true;
                }
            }

            if (foundPending) {
                System.out.println("PASSED: Company Representative can view PENDING internships\n");
                testsPassed++;
            } else {
                System.out.println("FAILED: Could not find PENDING internship\n");
                testsFailed++;
            }

        } catch (Exception e) {
            System.out.println("FAILED: " + e.getMessage() + "\n");
            testsFailed++;
        }
    }

    // Test 2: View approved internships
    private static void testViewApprovedInternships() {
        System.out.println("Test 2: Company Representative can view APPROVED internships");
        try {
            Repository repo = new Repository();
            SystemController sys = new SystemController(repo);

            CompanyRepresentative rep = new CompanyRepresentative(
                "approved.rep@company.com",
                "Approved Rep",
                "password",
                "ApprovedCorp",
                "Engineering",
                "Recruiter",
                true
            );

            CompanyController companyController = new CompanyController(sys);
            StaffController staffController = new StaffController(sys);

            // Create an internship
            companyController.createInternship(
                rep,
                "Approved Data Science Internship",
                "This internship has been approved",
                InternshipLevel.INTERMEDIATE,
                "Data Science",
                "2025-03-01",
                "2025-08-31",
                5
            );

            // Get the internship ID to approve it
            List<Internship> internships = companyController.getInternships(rep);
            String internshipID = internships.get(0).getInternshipID();

            // Staff approves the internship
            staffController.decideInternship(internshipID, true);

            // Retrieve internships again to see the updated status
            internships = companyController.getInternships(rep);

            boolean foundApproved = false;
            for (Internship i : internships) {
                if (i.getStatus() == InternshipStatus.APPROVED) {
                    System.out.println("   Found APPROVED internship: " + i.getTitle());
                    System.out.println("   Status: " + i.getStatus());
                    System.out.println("   Visible: " + i.isVisible());
                    foundApproved = true;
                }
            }

            if (foundApproved) {
                System.out.println("PASSED: Company Representative can view APPROVED internships\n");
                testsPassed++;
            } else {
                System.out.println("FAILED: Could not find APPROVED internship\n");
                testsFailed++;
            }

        } catch (Exception e) {
            System.out.println("FAILED: " + e.getMessage() + "\n");
            testsFailed++;
        }
    }

    // Test 3: View rejected internships
    private static void testViewRejectedInternships() {
        System.out.println("Test 3: Company Representative can view REJECTED internships");
        try {
            Repository repo = new Repository();
            SystemController sys = new SystemController(repo);

            CompanyRepresentative rep = new CompanyRepresentative(
                "rejected.rep@company.com",
                "Rejected Rep",
                "password",
                "RejectedCorp",
                "Marketing",
                "HR Lead",
                true
            );

            CompanyController companyController = new CompanyController(sys);
            StaffController staffController = new StaffController(sys);

            // Create an internship
            companyController.createInternship(
                rep,
                "Rejected Marketing Internship",
                "This internship has been rejected",
                InternshipLevel.BASIC,
                "Business",
                "2025-04-01",
                "2025-09-30",
                2
            );

            // Get the internship ID to reject it
            List<Internship> internships = companyController.getInternships(rep);
            String internshipID = internships.get(0).getInternshipID();

            // Staff rejects the internship
            staffController.decideInternship(internshipID, false);

            // Retrieve internships again to see the updated status
            internships = companyController.getInternships(rep);

            boolean foundRejected = false;
            for (Internship i : internships) {
                if (i.getStatus() == InternshipStatus.REJECTED) {
                    System.out.println("   Found REJECTED internship: " + i.getTitle());
                    System.out.println("   Status: " + i.getStatus());
                    foundRejected = true;
                }
            }

            if (foundRejected) {
                System.out.println("PASSED: Company Representative can view REJECTED internships\n");
                testsPassed++;
            } else {
                System.out.println("FAILED: Could not find REJECTED internship\n");
                testsFailed++;
            }

        } catch (Exception e) {
            System.out.println("FAILED: " + e.getMessage() + "\n");
            testsFailed++;
        }
    }

    // Test 4: View mixed status internships
    private static void testViewMixedStatusInternships() {
        System.out.println("Test 4: Company Representative can view internships with mixed statuses");
        try {
            Repository repo = new Repository();
            SystemController sys = new SystemController(repo);

            CompanyRepresentative rep = new CompanyRepresentative(
                "mixed.rep@company.com",
                "Mixed Rep",
                "password",
                "MixedCorp",
                "Operations",
                "Manager",
                true
            );

            CompanyController companyController = new CompanyController(sys);
            StaffController staffController = new StaffController(sys);

            // Create multiple internships
            companyController.createInternship(rep, "Internship 1", "Pending internship",
                    InternshipLevel.BASIC, "CS", "2025-05-01", "2025-10-31", 3);

            companyController.createInternship(rep, "Internship 2", "Will be approved",
                    InternshipLevel.INTERMEDIATE, "DS", "2025-05-01", "2025-10-31", 4);

            companyController.createInternship(rep, "Internship 3", "Will be rejected",
                    InternshipLevel.ADVANCED, "BA", "2025-05-01", "2025-10-31", 2);

            // Get internships and approve/reject some
            List<Internship> internships = companyController.getInternships(rep);

            if (internships.size() >= 3) {
                staffController.decideInternship(internships.get(1).getInternshipID(), true);  // Approve
                staffController.decideInternship(internships.get(2).getInternshipID(), false); // Reject
            }

            // Retrieve all internships and count by status
            internships = companyController.getInternships(rep);

            int pendingCount = 0, approvedCount = 0, rejectedCount = 0;

            for (Internship i : internships) {
                System.out.println("   " + i.getTitle() + " - Status: " + i.getStatus());
                switch (i.getStatus()) {
                    case PENDING -> pendingCount++;
                    case APPROVED -> approvedCount++;
                    case REJECTED -> rejectedCount++;
                    default -> {}
                }
            }

            System.out.println("   Total: PENDING=" + pendingCount + ", APPROVED=" + approvedCount + ", REJECTED=" + rejectedCount);

            if (pendingCount >= 1 && approvedCount >= 1 && rejectedCount >= 1) {
                System.out.println("PASSED: Company Representative can view all status types\n");
                testsPassed++;
            } else {
                System.out.println("FAILED: Not all status types found\n");
                testsFailed++;
            }

        } catch (Exception e) {
            System.out.println("FAILED: " + e.getMessage() + "\n");
            testsFailed++;
        }
    }

    // Test 5: Filter internships by status
    private static void testFilterInternshipsByStatus() {
        System.out.println("Test 5: Company Representative can filter internships by status");
        try {
            Repository repo = new Repository();
            SystemController sys = new SystemController(repo);

            CompanyRepresentative rep = new CompanyRepresentative(
                "filter.rep@company.com",
                "Filter Rep",
                "password",
                "FilterCorp",
                "IT",
                "Director",
                true
            );

            CompanyController companyController = new CompanyController(sys);
            StaffController staffController = new StaffController(sys);

            // Create multiple internships
            for (int i = 1; i <= 4; i++) {
                companyController.createInternship(rep, "Internship " + i, "Description " + i,
                        InternshipLevel.BASIC, "CS", "2025-06-01", "2025-11-30", 3);
            }

            List<Internship> internships = companyController.getInternships(rep);

            // Approve 2, reject 1, leave 1 pending
            if (internships.size() >= 4) {
                staffController.decideInternship(internships.get(0).getInternshipID(), true);
                staffController.decideInternship(internships.get(1).getInternshipID(), true);
                staffController.decideInternship(internships.get(2).getInternshipID(), false);
            }

            // Retrieve and filter by APPROVED status
            internships = companyController.getInternships(rep);
            long approvedCount = internships.stream()
                    .filter(i -> i.getStatus() == InternshipStatus.APPROVED)
                    .count();

            System.out.println("   Approved internships count: " + approvedCount);

            if (approvedCount == 2) {
                System.out.println("PASSED: Successfully filtered APPROVED internships\n");
                testsPassed++;
            } else {
                System.out.println("FAILED: Expected 2 approved, found " + approvedCount + "\n");
                testsFailed++;
            }

        } catch (Exception e) {
            System.out.println("FAILED: " + e.getMessage() + "\n");
            testsFailed++;
        }
    }
}
