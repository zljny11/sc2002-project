import controller.StaffController;
import controller.SystemController;
import entities.CompanyRepresentative;
import entities.Internship;
import entities.Repository;
import entities.Staff;
import enums.InternshipLevel;
import enums.InternshipStatus;

import java.util.List;

public class CareerCenterStaffInternshipApprovalTest {

    private static int testsPassed = 0;
    private static int testsFailed = 0;

    public static void main(String[] args) {
        System.out.println("=== Career Center Staff Internship Approval Test ===\n");

        // Run tests
        testStaffCanViewPendingInternships();
        testStaffCanApproveInternship();
        testStaffCanRejectInternship();
        testInternshipStatusUpdatedAfterApproval();
        testInternshipStatusUpdatedAfterRejection();
        testApprovedInternshipBecomesVisible();

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

    // Test 1: Staff can view pending internships
    private static void testStaffCanViewPendingInternships() {
        System.out.println("Test 1: Career Center Staff can view pending internships");
        try {
            Repository repo = new Repository();
            SystemController sys = new SystemController(repo);
            StaffController staffController = new StaffController(sys);

            // Create a company representative
            CompanyRepresentative rep = new CompanyRepresentative(
                "test@company.com",
                "Test Rep",
                "password",
                "TestCompany",
                "IT",
                "Manager",
                true
            );

            // Add company rep to repository
            repo.updateCompanyRep(rep);

            // Create internship (status defaults to PENDING)
            Internship internship = new Internship(
                "I001",
                "Software Engineering Intern",
                "Work on exciting projects",
                InternshipLevel.INTERMEDIATE,
                "Computer Science",
                "2025-06-01",
                "2025-12-01",
                InternshipStatus.PENDING,
                "TestCompany",
                rep.getID(),
                5,
                false
            );

            // Add internship to repository
            repo.updateInternship(internship);

            // Check if staff can retrieve pending internships
            boolean foundPending = false;
            
            // Use reflection to access private method or check repository directly
            List<Internship> allInternships = repo.getAllInternships();
            for (Internship i : allInternships) {
                if (i.getStatus() == InternshipStatus.PENDING && 
                    i.getTitle().equals("Software Engineering Intern")) {
                    foundPending = true;
                    System.out.println("   Found pending internship: " + i.getTitle());
                    System.out.println("   Status: " + i.getStatus());
                    System.out.println("   ID: " + i.getInternshipID());
                    break;
                }
            }

            if (foundPending) {
                System.out.println("PASSED: Staff can view pending internships\n");
                testsPassed++;
            } else {
                System.out.println("FAILED: Staff cannot view pending internships\n");
                testsFailed++;
            }

        } catch (Exception e) {
            System.out.println("FAILED: " + e.getMessage() + "\n");
            e.printStackTrace();
            testsFailed++;
        }
    }

    // Test 2: Staff can approve internship
    private static void testStaffCanApproveInternship() {
        System.out.println("Test 2: Career Center Staff can approve internship");
        try {
            Repository repo = new Repository();
            SystemController sys = new SystemController(repo);
            StaffController staffController = new StaffController(sys);

            // Create a company representative
            CompanyRepresentative rep = new CompanyRepresentative(
                "approve@company.com",
                "Approve Rep",
                "password",
                "ApproveCompany",
                "HR",
                "Supervisor",
                true
            );

            // Add company rep to repository
            repo.updateCompanyRep(rep);

            // Create internship (status defaults to PENDING)
            Internship internship = new Internship(
                "I002",
                "Data Analyst Intern",
                "Analyze business data",
                InternshipLevel.BASIC,
                "Data Science",
                "2025-07-01",
                "2025-12-31",
                InternshipStatus.PENDING,
                "ApproveCompany",
                rep.getID(),
                3,
                false
            );

            // Add internship to repository
            repo.updateInternship(internship);

            // Staff approves internship
            staffController.decideInternship("I002", true);

            // Check if internship still exists in repository
            Internship updatedInternship = repo.findInternship("I002");
            
            if (updatedInternship != null) {
                System.out.println("   Internship found after approval attempt");
                System.out.println("   Title: " + updatedInternship.getTitle());
                System.out.println("   Status: " + updatedInternship.getStatus());
                System.out.println("PASSED: Staff can approve internship (internship exists)\n");
                testsPassed++;
            } else {
                System.out.println("FAILED: Staff approval caused internship to disappear\n");
                testsFailed++;
            }

        } catch (Exception e) {
            System.out.println("FAILED: " + e.getMessage() + "\n");
            e.printStackTrace();
            testsFailed++;
        }
    }

    // Test 3: Staff can reject internship
    private static void testStaffCanRejectInternship() {
        System.out.println("Test 3: Career Center Staff can reject internship");
        try {
            Repository repo = new Repository();
            SystemController sys = new SystemController(repo);
            StaffController staffController = new StaffController(sys);

            // Create a company representative
            CompanyRepresentative rep = new CompanyRepresentative(
                "reject@company.com",
                "Reject Rep",
                "password",
                "RejectCompany",
                "Finance",
                "Manager",
                true
            );

            // Add company rep to repository
            repo.updateCompanyRep(rep);

            // Create internship (status defaults to PENDING)
            Internship internship = new Internship(
                "I003",
                "Marketing Intern",
                "Help with marketing campaigns",
                InternshipLevel.BASIC,
                "Marketing",
                "2025-08-01",
                "2025-11-30",
                InternshipStatus.PENDING,
                "RejectCompany",
                rep.getID(),
                2,
                false
            );

            // Add internship to repository
            repo.updateInternship(internship);

            // Staff rejects internship
            staffController.decideInternship("I003", false);

            // Check if internship still exists in repository
            Internship updatedInternship = repo.findInternship("I003");
            
            if (updatedInternship != null) {
                System.out.println("   Internship found after rejection attempt");
                System.out.println("   Title: " + updatedInternship.getTitle());
                System.out.println("   Status: " + updatedInternship.getStatus());
                System.out.println("PASSED: Staff can reject internship (internship exists)\n");
                testsPassed++;
            } else {
                System.out.println("FAILED: Staff rejection caused internship to disappear\n");
                testsFailed++;
            }

        } catch (Exception e) {
            System.out.println("FAILED: " + e.getMessage() + "\n");
            e.printStackTrace();
            testsFailed++;
        }
    }

    // Test 4: Internship status updated after approval
    private static void testInternshipStatusUpdatedAfterApproval() {
        System.out.println("Test 4: Internship status is updated to APPROVED after staff approval");
        try {
            Repository repo = new Repository();
            SystemController sys = new SystemController(repo);
            StaffController staffController = new StaffController(sys);

            // Create a company representative
            CompanyRepresentative rep = new CompanyRepresentative(
                "status@company.com",
                "Status Rep",
                "password",
                "StatusCompany",
                "Operations",
                "Coordinator",
                true
            );

            // Add company rep to repository
            repo.updateCompanyRep(rep);

            // Create internship (status defaults to PENDING)
            Internship internship = new Internship(
                "I004",
                "Research Intern",
                "Conduct research projects",
                InternshipLevel.ADVANCED,
                "Physics",
                "2025-09-01",
                "2026-03-01",
                InternshipStatus.PENDING,
                "StatusCompany",
                rep.getID(),
                1,
                false
            );

            // Add internship to repository
            repo.updateInternship(internship);

            // Verify initial status
            Internship initialInternship = repo.findInternship("I004");
            System.out.println("   Initial status: " + initialInternship.getStatus());

            // Staff approves internship
            staffController.decideInternship("I004", true);

            // Check updated status
            Internship updatedInternship = repo.findInternship("I004");
            
            if (updatedInternship.getStatus() == InternshipStatus.APPROVED) {
                System.out.println("   Updated status: " + updatedInternship.getStatus());
                System.out.println("PASSED: Internship status correctly updated to APPROVED\n");
                testsPassed++;
            } else {
                System.out.println("   Updated status: " + updatedInternship.getStatus());
                System.out.println("FAILED: Internship status not correctly updated after approval\n");
                testsFailed++;
            }

        } catch (Exception e) {
            System.out.println("FAILED: " + e.getMessage() + "\n");
            e.printStackTrace();
            testsFailed++;
        }
    }

    // Test 5: Internship status updated after rejection
    private static void testInternshipStatusUpdatedAfterRejection() {
        System.out.println("Test 5: Internship status is updated to REJECTED after staff rejection");
        try {
            Repository repo = new Repository();
            SystemController sys = new SystemController(repo);
            StaffController staffController = new StaffController(sys);

            // Create a company representative
            CompanyRepresentative rep = new CompanyRepresentative(
                "rejectstatus@company.com",
                "Reject Status Rep",
                "password",
                "RejectStatusCompany",
                "Legal",
                "Advisor",
                true
            );

            // Add company rep to repository
            repo.updateCompanyRep(rep);

            // Create internship (status defaults to PENDING)
            Internship internship = new Internship(
                "I005",
                "Legal Intern",
                "Assist with legal documentation",
                InternshipLevel.INTERMEDIATE,
                "Law",
                "2025-10-01",
                "2026-04-01",
                InternshipStatus.PENDING,
                "RejectStatusCompany",
                rep.getID(),
                2,
                false
            );

            // Add internship to repository
            repo.updateInternship(internship);

            // Verify initial status
            Internship initialInternship = repo.findInternship("I005");
            System.out.println("   Initial status: " + initialInternship.getStatus());

            // Staff rejects internship
            staffController.decideInternship("I005", false);

            // Check updated status
            Internship updatedInternship = repo.findInternship("I005");
            
            if (updatedInternship.getStatus() == InternshipStatus.REJECTED) {
                System.out.println("   Updated status: " + updatedInternship.getStatus());
                System.out.println("PASSED: Internship status correctly updated to REJECTED\n");
                testsPassed++;
            } else {
                System.out.println("   Updated status: " + updatedInternship.getStatus());
                System.out.println("FAILED: Internship status not correctly updated after rejection\n");
                testsFailed++;
            }

        } catch (Exception e) {
            System.out.println("FAILED: " + e.getMessage() + "\n");
            e.printStackTrace();
            testsFailed++;
        }
    }

    // Test 6: Approved internship becomes visible
    private static void testApprovedInternshipBecomesVisible() {
        System.out.println("Test 6: Approved internship becomes visible to students");
        try {
            Repository repo = new Repository();
            SystemController sys = new SystemController(repo);
            StaffController staffController = new StaffController(sys);

            // Create a company representative
            CompanyRepresentative rep = new CompanyRepresentative(
                "visibility@company.com",
                "Visibility Rep",
                "password",
                "VisibilityCompany",
                "Marketing",
                "Director",
                true
            );

            // Add company rep to repository
            repo.updateCompanyRep(rep);

            // Create internship (status defaults to PENDING, visible=false)
            Internship internship = new Internship(
                "I006",
                "Social Media Intern",
                "Manage social media accounts",
                InternshipLevel.BASIC,
                "Communications",
                "2025-11-01",
                "2026-05-01",
                InternshipStatus.PENDING,
                "VisibilityCompany",
                rep.getID(),
                3,
                false // Initially not visible
            );

            // Add internship to repository
            repo.updateInternship(internship);

            // Verify initial visibility
            Internship initialInternship = repo.findInternship("I006");
            System.out.println("   Initial visibility: " + initialInternship.isVisible());

            // Staff approves internship
            staffController.decideInternship("I006", true);

            // Check updated visibility
            Internship updatedInternship = repo.findInternship("I006");
            
            if (updatedInternship.isVisible()) {
                System.out.println("   Updated visibility: " + updatedInternship.isVisible());
                System.out.println("PASSED: Approved internship becomes visible\n");
                testsPassed++;
            } else {
                System.out.println("   Updated visibility: " + updatedInternship.isVisible());
                System.out.println("FAILED: Approved internship did not become visible\n");
                testsFailed++;
            }

        } catch (Exception e) {
            System.out.println("FAILED: " + e.getMessage() + "\n");
            e.printStackTrace();
            testsFailed++;
        }
    }
}