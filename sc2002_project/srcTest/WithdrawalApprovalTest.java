import controller.StaffController;
import controller.StudentController;
import controller.SystemController;
import entities.*;
import enums.ApplicationStatus;
import enums.ApprovalStatus;
import enums.InternshipLevel;
import enums.InternshipStatus;

import java.util.List;

public class WithdrawalApprovalTest {

    private static int testsPassed = 0;
    private static int testsFailed = 0;

    public static void main(String[] args) {
        System.out.println("=== Withdrawal Approval Test ===\n");

        // Run tests
        testWithdrawalRequestCreation();
        testWithdrawalApprovalUpdatesRequestStatus();
        testWithdrawalRejectionUpdatesRequestStatus();
        testWithdrawalApprovalUpdatesApplicationStatus();
        testWithdrawalApprovalFreesUpSlot();
        testWithdrawalApprovalChangesFilledInternshipToApproved();

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

    // Test 1: Withdrawal request can be created
    private static void testWithdrawalRequestCreation() {
        System.out.println("Test 1: Withdrawal request can be created");
        try {
            Repository repo = new Repository();
            SystemController sys = new SystemController(repo);
            StudentController studentController = new StudentController(sys);

            // Create entities
            setupTestData(repo);

            // Get student and application
            Student student = repo.findStudent("U1234567A");
            String applicationID = "A001";

            // Request withdrawal
            studentController.requestWithdrawal(student, applicationID);

            // Check if withdrawal request was created
            List<WithdrawalRequest> allWithdrawals = repo.getAllWithdrawals();
            boolean found = false;
            WithdrawalRequest createdRequest = null;
            for (WithdrawalRequest w : allWithdrawals) {
                if (w.getApplicationID().equals(applicationID) && w.getStudentID().equals(student.getID())) {
                    found = true;
                    createdRequest = w;
                    break;
                }
            }

            System.out.println("   Total withdrawals: " + allWithdrawals.size());
            if (found) {
                System.out.println("   Withdrawal request ID: " + createdRequest.getRequestID());
                System.out.println("   Status: " + createdRequest.getStatus());
                System.out.println("PASSED: Withdrawal request created successfully\n");
                testsPassed++;
            } else {
                System.out.println("FAILED: Withdrawal request not found\n");
                testsFailed++;
            }

        } catch (Exception e) {
            System.out.println("FAILED: " + e.getMessage() + "\n");
            e.printStackTrace();
            testsFailed++;
        }
    }

    // Test 2: Withdrawal approval updates request status
    private static void testWithdrawalApprovalUpdatesRequestStatus() {
        System.out.println("Test 2: Withdrawal approval updates request status");
        try {
            Repository repo = new Repository();
            SystemController sys = new SystemController(repo);
            StudentController studentController = new StudentController(sys);
            StaffController staffController = new StaffController(sys);

            // Create entities
            setupTestData(repo);

            // Get student and application
            Student student = repo.findStudent("U1234567A");
            String applicationID = "A001";

            // Request withdrawal
            studentController.requestWithdrawal(student, applicationID);

            // Get withdrawal request - find the one we just created
            List<WithdrawalRequest> allWithdrawals = repo.getAllWithdrawals();
            WithdrawalRequest withdrawalRequest = null;
            for (WithdrawalRequest w : allWithdrawals) {
                if (w.getApplicationID().equals(applicationID) && w.getStudentID().equals(student.getID())) {
                    withdrawalRequest = w;
                    break;
                }
            }
            if (withdrawalRequest == null) {
                throw new RuntimeException("Withdrawal request not found after creation");
            }
            String withdrawalID = withdrawalRequest.getRequestID();

            System.out.println("   Initial status: " + withdrawalRequest.getStatus());

            // Approve withdrawal
            staffController.decideWithdrawal(withdrawalID, true);

            // Check updated status
            WithdrawalRequest updatedRequest = repo.findWithdrawal(withdrawalID);
            System.out.println("   Updated status: " + updatedRequest.getStatus());

            if (updatedRequest.getStatus() == ApprovalStatus.APPROVED) {
                System.out.println("PASSED: Withdrawal approval updates request status to APPROVED\n");
                testsPassed++;
            } else {
                System.out.println("FAILED: Withdrawal approval did not update request status correctly\n");
                testsFailed++;
            }

        } catch (Exception e) {
            System.out.println("FAILED: " + e.getMessage() + "\n");
            e.printStackTrace();
            testsFailed++;
        }
    }

    // Test 3: Withdrawal rejection updates request status
    private static void testWithdrawalRejectionUpdatesRequestStatus() {
        System.out.println("Test 3: Withdrawal rejection updates request status");
        try {
            Repository repo = new Repository();
            SystemController sys = new SystemController(repo);
            StudentController studentController = new StudentController(sys);
            StaffController staffController = new StaffController(sys);

            // Create entities
            setupTestData(repo);

            // Get student and application
            Student student = repo.findStudent("U1234567A");
            String applicationID = "A001";

            // Request withdrawal
            studentController.requestWithdrawal(student, applicationID);

            // Get withdrawal request - find the one we just created
            List<WithdrawalRequest> allWithdrawals = repo.getAllWithdrawals();
            WithdrawalRequest withdrawalRequest = null;
            for (WithdrawalRequest w : allWithdrawals) {
                if (w.getApplicationID().equals(applicationID) && w.getStudentID().equals(student.getID())) {
                    withdrawalRequest = w;
                    break;
                }
            }
            if (withdrawalRequest == null) {
                throw new RuntimeException("Withdrawal request not found after creation");
            }
            String withdrawalID = withdrawalRequest.getRequestID();

            System.out.println("   Initial status: " + withdrawalRequest.getStatus());

            // Reject withdrawal
            staffController.decideWithdrawal(withdrawalID, false);

            // Check updated status
            WithdrawalRequest updatedRequest = repo.findWithdrawal(withdrawalID);
            System.out.println("   Updated status: " + updatedRequest.getStatus());

            if (updatedRequest.getStatus() == ApprovalStatus.REJECTED) {
                System.out.println("PASSED: Withdrawal rejection updates request status to REJECTED\n");
                testsPassed++;
            } else {
                System.out.println("FAILED: Withdrawal rejection did not update request status correctly\n");
                testsFailed++;
            }

        } catch (Exception e) {
            System.out.println("FAILED: " + e.getMessage() + "\n");
            e.printStackTrace();
            testsFailed++;
        }
    }

    // Test 4: Withdrawal approval updates application status
    private static void testWithdrawalApprovalUpdatesApplicationStatus() {
        System.out.println("Test 4: Withdrawal approval updates application status");
        try {
            Repository repo = new Repository();
            SystemController sys = new SystemController(repo);
            StudentController studentController = new StudentController(sys);
            StaffController staffController = new StaffController(sys);

            // Create entities
            setupTestData(repo);

            // Get student and application
            Student student = repo.findStudent("U1234567A");
            String applicationID = "A001";

            // Request withdrawal
            studentController.requestWithdrawal(student, applicationID);

            // Get withdrawal request - find the one we just created
            List<WithdrawalRequest> allWithdrawals = repo.getAllWithdrawals();
            WithdrawalRequest withdrawalRequest = null;
            for (WithdrawalRequest w : allWithdrawals) {
                if (w.getApplicationID().equals(applicationID) && w.getStudentID().equals(student.getID())) {
                    withdrawalRequest = w;
                    break;
                }
            }
            if (withdrawalRequest == null) {
                throw new RuntimeException("Withdrawal request not found after creation");
            }
            String withdrawalID = withdrawalRequest.getRequestID();

            // Check initial application status
            Application application = repo.findApplication(applicationID);
            System.out.println("   Initial application status: " + application.getStatus());

            // Approve withdrawal
            staffController.decideWithdrawal(withdrawalID, true);

            // Check updated application status
            Application updatedApplication = repo.findApplication(applicationID);
            System.out.println("   Updated application status: " + updatedApplication.getStatus());

            if (updatedApplication.getStatus() == ApplicationStatus.WITHDRAWN) {
                System.out.println("PASSED: Withdrawal approval updates application status to WITHDRAWN\n");
                testsPassed++;
            } else {
                System.out.println("FAILED: Withdrawal approval did not update application status correctly\n");
                testsFailed++;
            }

        } catch (Exception e) {
            System.out.println("FAILED: " + e.getMessage() + "\n");
            e.printStackTrace();
            testsFailed++;
        }
    }

    // Test 5: Withdrawal approval frees up slot
    private static void testWithdrawalApprovalFreesUpSlot() {
        System.out.println("Test 5: Withdrawal approval frees up slot");
        try {
            Repository repo = new Repository();
            SystemController sys = new SystemController(repo);
            StudentController studentController = new StudentController(sys);
            StaffController staffController = new StaffController(sys);

            // Create entities
            setupTestData(repo);

            // Get student and application
            Student student = repo.findStudent("U1234567A");
            String applicationID = "A001";

            // Get the application and mark it as accepted by student BEFORE requesting withdrawal
            Application application = repo.findApplication(applicationID);
            application.setAcceptedByStudent(true);
            repo.updateApplication(application);
            
            // Request withdrawal
            studentController.requestWithdrawal(student, applicationID);

            // Get withdrawal request - find the one we just created
            List<WithdrawalRequest> allWithdrawals = repo.getAllWithdrawals();
            WithdrawalRequest withdrawalRequest = null;
            for (WithdrawalRequest w : allWithdrawals) {
                if (w.getApplicationID().equals(applicationID) && w.getStudentID().equals(student.getID())) {
                    withdrawalRequest = w;
                    break;
                }
            }
            if (withdrawalRequest == null) {
                throw new RuntimeException("Withdrawal request not found after creation");
            }
            String withdrawalID = withdrawalRequest.getRequestID();

            // Get internship and check initial slots
            Internship internship = repo.findInternship(application.getInternshipID());
            int initialSlots = internship.getSlots();
            System.out.println("   Initial slots: " + initialSlots);
            System.out.println("   Application accepted by student: " + application.isAcceptedByStudent());

            // Approve withdrawal
            staffController.decideWithdrawal(withdrawalID, true);

            // Check updated slots
            Internship updatedInternship = repo.findInternship(application.getInternshipID());
            int updatedSlots = updatedInternship.getSlots();
            System.out.println("   Updated slots: " + updatedSlots);

            if (updatedSlots == initialSlots + 1) {
                System.out.println("PASSED: Withdrawal approval freed up a slot\n");
                testsPassed++;
            } else {
                System.out.println("FAILED: Withdrawal approval did not free up a slot\n");
                testsFailed++;
            }

        } catch (Exception e) {
            System.out.println("FAILED: " + e.getMessage() + "\n");
            e.printStackTrace();
            testsFailed++;
        }
    }

    // Test 6: Withdrawal approval changes FILLED internship to APPROVED
    private static void testWithdrawalApprovalChangesFilledInternshipToApproved() {
        System.out.println("Test 6: Withdrawal approval changes FILLED internship to APPROVED");
        try {
            Repository repo = new Repository();
            SystemController sys = new SystemController(repo);
            StudentController studentController = new StudentController(sys);
            StaffController staffController = new StaffController(sys);

            // Create entities
            setupTestData(repo);

            // Get student and application
            Student student = repo.findStudent("U1234567A");
            String applicationID = "A001";

            // Request withdrawal
            studentController.requestWithdrawal(student, applicationID);

            // Get withdrawal request
            List<WithdrawalRequest> allWithdrawals = repo.getAllWithdrawals();
            WithdrawalRequest withdrawalRequest = allWithdrawals.get(0);
            String withdrawalID = withdrawalRequest.getRequestID();

            // Get internship and set it to FILLED with 0 slots to test status change
            Application application = repo.findApplication(applicationID);
            Internship internship = repo.findInternship(application.getInternshipID());
            
            // Mark application as accepted by student - this is required for slot to be freed
            application.setAcceptedByStudent(true);
            repo.updateApplication(application);
            
            // Set internship to FILLED status with 0 slots
            internship.setSlots(0);
            internship.setStatus(InternshipStatus.FILLED);
            repo.updateInternship(internship);
            
            System.out.println("   Initial internship status: " + internship.getStatus());
            System.out.println("   Initial slots: " + internship.getSlots());

            // Approve withdrawal
            staffController.decideWithdrawal(withdrawalID, true);

            // Check updated internship status and slots
            Internship updatedInternship = repo.findInternship(application.getInternshipID());
            System.out.println("   Updated internship status: " + updatedInternship.getStatus());
            System.out.println("   Updated slots: " + updatedInternship.getSlots());

            if (updatedInternship.getStatus() == InternshipStatus.APPROVED && updatedInternship.getSlots() == 1) {
                System.out.println("PASSED: Withdrawal approval changed FILLED internship to APPROVED and freed up a slot\n");
                testsPassed++;
            } else {
                System.out.println("FAILED: Withdrawal approval did not correctly update FILLED internship\n");
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
        // Create company representative
        CompanyRepresentative rep = new CompanyRepresentative(
            "test@company.com",
            "Test Rep",
            "password",
            "TestCompany",
            "IT",
            "Manager",
            true
        );
        repo.updateCompanyRep(rep);

        // Create student
        Student student = new Student(
            "U1234567A",
            "Test Student",
            "password",
            "test@student.edu",
            3,
            "Computer Science"
        );
        repo.updateStudent(student);

        // Create internship with 1 slot
        Internship internship = new Internship(
            "I001",
            "Test Internship",
            "Test Description",
            InternshipLevel.BASIC,
            "Computer Science",
            "2025-06-01",
            "2025-12-01",
            InternshipStatus.APPROVED,
            "TestCompany",
            rep.getID(),
            1,
            true
        );
        repo.updateInternship(internship);

        // Create application
        Application application = new Application(
            "A001",
            "I001",
            "U1234567A",
            ApplicationStatus.SUCCESSFUL,
            "2025-05-01",
            true
        );
        repo.updateApplication(application);
    }
}