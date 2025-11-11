import controller.CompanyController;
import controller.StaffController;
import controller.StudentController;
import controller.SystemController;
import entities.Application;
import entities.CompanyRepresentative;
import entities.Internship;
import entities.Repository;
import entities.Student;
import enums.ApplicationStatus;
import enums.InternshipLevel;

import java.util.List;

public class PlacementConfirmationStatusTest {

    private static int testsPassed = 0;
    private static int testsFailed = 0;

    public static void main(String[] args) {
        System.out.println("=== Placement Confirmation Status Test ===\n");
        System.out.println("Testing: Placement confirmation status is updated to reflect the actual confirmation condition\n");

        // Run tests
        testConfirmationStatusSetToTrue();
        testConfirmationStatusInitiallyFalse();
        testCannotConfirmPendingApplication();
        testCannotConfirmUnsuccessfulApplication();
        testCannotConfirmOtherStudentApplication();
        testOtherApplicationsWithdrawnOnConfirmation();
        testConfirmationStatusPersistence();
        testMultipleStudentsConfirmation();

        // Print summary
        System.out.println("\n=== Test Summary ===");
        System.out.println("Tests Passed: " + testsPassed);
        System.out.println("Tests Failed: " + testsFailed);
        System.out.println("Total Tests: " + (testsPassed + testsFailed));

        if (testsFailed == 0) {
            System.out.println("\nPASSED: All tests passed!");
            System.out.println("Placement confirmation status is correctly updated and reflected.");
        } else {
            System.out.println("\nFAILED: Some tests failed!");
        }
    }

    // Test 1: Confirmation status is set to true when student accepts offer
    private static void testConfirmationStatusSetToTrue() {
        System.out.println("Test 1: Confirmation status set to TRUE when student accepts offer");
        try {
            Repository repo = new Repository();
            SystemController sys = new SystemController(repo);

            CompanyRepresentative rep = new CompanyRepresentative(
                "confirm.test@company.com",
                "Confirm Rep",
                "password",
                "ConfirmCorp",
                "HR",
                "Manager",
                true
            );

            Student student = new Student("U1111111A", "Test Student", "password",
                    "test.student@e.ntu.edu.sg", 3, "Computer Science");

            CompanyController companyController = new CompanyController(sys);
            StudentController studentController = new StudentController(sys);
            StaffController staffController = new StaffController(sys);

            // Create and approve internship
            companyController.createInternship(
                rep,
                "Test Internship",
                "Description",
                InternshipLevel.BASIC,
                "Computer Science",
                "2025-01-01",
                "2025-12-31",
                3
            );

            List<Internship> internships = companyController.getInternships(rep);
            String internshipID = internships.get(0).getInternshipID();
            staffController.decideInternship(internshipID, true);

            // Student applies
            studentController.apply(student, internshipID);

            // Get application and check initial status
            List<Application> applications = studentController.myApplications(student);
            Application app = applications.get(0);
            boolean initialConfirmation = app.isAcceptedByStudent();

            System.out.println("   Initial confirmation status: " + initialConfirmation);

            // Staff approves application
            staffController.decideApplication(app.getApplicationID(), true);

            // Student accepts offer
            boolean acceptSuccess = studentController.acceptOffer(student, app.getApplicationID());

            // Check confirmation status after acceptance
            applications = studentController.myApplications(student);
            app = applications.get(0);
            boolean finalConfirmation = app.isAcceptedByStudent();

            System.out.println("   Accept operation success: " + acceptSuccess);
            System.out.println("   Final confirmation status: " + finalConfirmation);

            if (!initialConfirmation && finalConfirmation && acceptSuccess) {
                System.out.println("PASSED: Confirmation status correctly set to TRUE\n");
                testsPassed++;
            } else {
                System.out.println("FAILED: Confirmation status not updated correctly\n");
                testsFailed++;
            }

        } catch (Exception e) {
            System.out.println("FAILED: " + e.getMessage() + "\n");
            e.printStackTrace();
            testsFailed++;
        }
    }

    // Test 2: Confirmation status is initially false
    private static void testConfirmationStatusInitiallyFalse() {
        System.out.println("Test 2: Confirmation status is initially FALSE when application created");
        try {
            Repository repo = new Repository();
            SystemController sys = new SystemController(repo);

            CompanyRepresentative rep = new CompanyRepresentative(
                "initial.test@company.com",
                "Initial Rep",
                "password",
                "InitialCorp",
                "Engineering",
                "Lead",
                true
            );

            Student student = new Student("U2222222B", "Initial Student", "password",
                    "initial.student@e.ntu.edu.sg", 2, "Computer Science");

            CompanyController companyController = new CompanyController(sys);
            StudentController studentController = new StudentController(sys);
            StaffController staffController = new StaffController(sys);

            // Create and approve internship
            companyController.createInternship(
                rep,
                "Initial Test Internship",
                "Description",
                InternshipLevel.BASIC,
                "Computer Science",
                "2025-02-01",
                "2025-11-30",
                2
            );

            List<Internship> internships = companyController.getInternships(rep);
            staffController.decideInternship(internships.get(0).getInternshipID(), true);

            // Student applies
            studentController.apply(student, internships.get(0).getInternshipID());

            // Check confirmation status
            List<Application> applications = studentController.myApplications(student);
            Application app = applications.get(0);

            System.out.println("   Application status: " + app.getStatus());
            System.out.println("   Confirmation status: " + app.isAcceptedByStudent());

            if (!app.isAcceptedByStudent()) {
                System.out.println("PASSED: Initial confirmation status is FALSE\n");
                testsPassed++;
            } else {
                System.out.println("FAILED: Initial confirmation status should be FALSE\n");
                testsFailed++;
            }

        } catch (Exception e) {
            System.out.println("FAILED: " + e.getMessage() + "\n");
            testsFailed++;
        }
    }

    // Test 3: Cannot confirm PENDING application
    private static void testCannotConfirmPendingApplication() {
        System.out.println("Test 3: Student cannot confirm PENDING application");
        try {
            Repository repo = new Repository();
            SystemController sys = new SystemController(repo);

            CompanyRepresentative rep = new CompanyRepresentative(
                "pending.test@company.com",
                "Pending Rep",
                "password",
                "PendingCorp",
                "IT",
                "Manager",
                true
            );

            Student student = new Student("U3333333C", "Pending Student", "password",
                    "pending.student@e.ntu.edu.sg", 3, "Computer Science");

            CompanyController companyController = new CompanyController(sys);
            StudentController studentController = new StudentController(sys);
            StaffController staffController = new StaffController(sys);

            // Create and approve internship
            companyController.createInternship(
                rep,
                "Pending Test Internship",
                "Description",
                InternshipLevel.BASIC,
                "Computer Science",
                "2025-03-01",
                "2025-12-31",
                2
            );

            List<Internship> internships = companyController.getInternships(rep);
            staffController.decideInternship(internships.get(0).getInternshipID(), true);

            // Student applies
            studentController.apply(student, internships.get(0).getInternshipID());

            // Try to accept without staff approval (application is PENDING)
            List<Application> applications = studentController.myApplications(student);
            Application app = applications.get(0);

            System.out.println("   Application status: " + app.getStatus());

            boolean acceptSuccess = studentController.acceptOffer(student, app.getApplicationID());

            // Check if confirmation status changed
            applications = studentController.myApplications(student);
            app = applications.get(0);
            boolean confirmationStatus = app.isAcceptedByStudent();

            System.out.println("   Accept operation success: " + acceptSuccess);
            System.out.println("   Confirmation status: " + confirmationStatus);

            if (!acceptSuccess && !confirmationStatus) {
                System.out.println("PASSED: Cannot confirm PENDING application\n");
                testsPassed++;
            } else {
                System.out.println("FAILED: PENDING application should not be confirmable\n");
                testsFailed++;
            }

        } catch (Exception e) {
            System.out.println("FAILED: " + e.getMessage() + "\n");
            testsFailed++;
        }
    }

    // Test 4: Cannot confirm UNSUCCESSFUL application
    private static void testCannotConfirmUnsuccessfulApplication() {
        System.out.println("Test 4: Student cannot confirm UNSUCCESSFUL application");
        try {
            Repository repo = new Repository();
            SystemController sys = new SystemController(repo);

            CompanyRepresentative rep = new CompanyRepresentative(
                "unsuccessful.test@company.com",
                "Unsuccessful Rep",
                "password",
                "UnsuccessfulCorp",
                "Marketing",
                "Lead",
                true
            );

            Student student = new Student("U4444444D", "Unsuccessful Student", "password",
                    "unsuccessful.student@e.ntu.edu.sg", 2, "Computer Science");

            CompanyController companyController = new CompanyController(sys);
            StudentController studentController = new StudentController(sys);
            StaffController staffController = new StaffController(sys);

            // Create and approve internship
            companyController.createInternship(
                rep,
                "Unsuccessful Test Internship",
                "Description",
                InternshipLevel.BASIC,
                "Computer Science",
                "2025-04-01",
                "2025-12-31",
                2
            );

            List<Internship> internships = companyController.getInternships(rep);
            staffController.decideInternship(internships.get(0).getInternshipID(), true);

            // Student applies
            studentController.apply(student, internships.get(0).getInternshipID());

            // Staff rejects application
            List<Application> applications = studentController.myApplications(student);
            Application app = applications.get(0);
            staffController.decideApplication(app.getApplicationID(), false);

            // Try to accept rejected application
            applications = studentController.myApplications(student);
            app = applications.get(0);

            System.out.println("   Application status: " + app.getStatus());

            boolean acceptSuccess = studentController.acceptOffer(student, app.getApplicationID());

            // Check if confirmation status changed
            applications = studentController.myApplications(student);
            app = applications.get(0);
            boolean confirmationStatus = app.isAcceptedByStudent();

            System.out.println("   Accept operation success: " + acceptSuccess);
            System.out.println("   Confirmation status: " + confirmationStatus);

            if (!acceptSuccess && !confirmationStatus) {
                System.out.println("PASSED: Cannot confirm UNSUCCESSFUL application\n");
                testsPassed++;
            } else {
                System.out.println("FAILED: UNSUCCESSFUL application should not be confirmable\n");
                testsFailed++;
            }

        } catch (Exception e) {
            System.out.println("FAILED: " + e.getMessage() + "\n");
            testsFailed++;
        }
    }

    // Test 5: Student cannot confirm other student's application
    private static void testCannotConfirmOtherStudentApplication() {
        System.out.println("Test 5: Student cannot confirm other student's application");
        try {
            Repository repo = new Repository();
            SystemController sys = new SystemController(repo);

            CompanyRepresentative rep = new CompanyRepresentative(
                "other.test@company.com",
                "Other Rep",
                "password",
                "OtherCorp",
                "Operations",
                "Manager",
                true
            );

            Student student1 = new Student("U5555555E", "Student One", "password",
                    "student.one@e.ntu.edu.sg", 3, "Computer Science");
            Student student2 = new Student("U6666666F", "Student Two", "password",
                    "student.two@e.ntu.edu.sg", 3, "Computer Science");

            CompanyController companyController = new CompanyController(sys);
            StudentController studentController = new StudentController(sys);
            StaffController staffController = new StaffController(sys);

            // Create and approve internship
            companyController.createInternship(
                rep,
                "Other Test Internship",
                "Description",
                InternshipLevel.BASIC,
                "Computer Science",
                "2025-05-01",
                "2025-12-31",
                3
            );

            List<Internship> internships = companyController.getInternships(rep);
            staffController.decideInternship(internships.get(0).getInternshipID(), true);

            // Student1 applies
            studentController.apply(student1, internships.get(0).getInternshipID());

            // Staff approves student1's application
            List<Application> student1Apps = studentController.myApplications(student1);
            Application student1App = student1Apps.get(0);
            staffController.decideApplication(student1App.getApplicationID(), true);

            System.out.println("   Student1 application ID: " + student1App.getApplicationID());

            // Student2 tries to accept student1's application
            boolean acceptSuccess = studentController.acceptOffer(student2, student1App.getApplicationID());

            // Check if confirmation status changed
            student1Apps = studentController.myApplications(student1);
            student1App = student1Apps.get(0);
            boolean confirmationStatus = student1App.isAcceptedByStudent();

            System.out.println("   Student2 accept operation success: " + acceptSuccess);
            System.out.println("   Student1 application confirmation status: " + confirmationStatus);

            if (!acceptSuccess && !confirmationStatus) {
                System.out.println("PASSED: Student cannot confirm other student's application\n");
                testsPassed++;
            } else {
                System.out.println("FAILED: Should not allow confirming other student's application\n");
                testsFailed++;
            }

        } catch (Exception e) {
            System.out.println("FAILED: " + e.getMessage() + "\n");
            testsFailed++;
        }
    }

    // Test 6: Other applications are withdrawn when one is confirmed
    private static void testOtherApplicationsWithdrawnOnConfirmation() {
        System.out.println("Test 6: Other applications withdrawn when student confirms one placement");
        try {
            Repository repo = new Repository();
            SystemController sys = new SystemController(repo);

            CompanyRepresentative rep1 = new CompanyRepresentative(
                "withdraw1.test@company.com",
                "Withdraw Rep1",
                "password",
                "WithdrawCorp1",
                "HR",
                "Manager",
                true
            );

            CompanyRepresentative rep2 = new CompanyRepresentative(
                "withdraw2.test@company.com",
                "Withdraw Rep2",
                "password",
                "WithdrawCorp2",
                "Engineering",
                "Lead",
                true
            );

            Student student = new Student("U7777777G", "Withdraw Student", "password",
                    "withdraw.student@e.ntu.edu.sg", 3, "Computer Science");

            CompanyController companyController = new CompanyController(sys);
            StudentController studentController = new StudentController(sys);
            StaffController staffController = new StaffController(sys);

            // Create two internships
            companyController.createInternship(
                rep1,
                "Internship A",
                "Description A",
                InternshipLevel.BASIC,
                "Computer Science",
                "2025-06-01",
                "2025-12-31",
                2
            );

            companyController.createInternship(
                rep2,
                "Internship B",
                "Description B",
                InternshipLevel.BASIC,
                "Computer Science",
                "2025-06-01",
                "2025-12-31",
                2
            );

            List<Internship> internships1 = companyController.getInternships(rep1);
            List<Internship> internships2 = companyController.getInternships(rep2);

            String internshipID1 = internships1.get(0).getInternshipID();
            String internshipID2 = internships2.get(0).getInternshipID();

            staffController.decideInternship(internshipID1, true);
            staffController.decideInternship(internshipID2, true);

            // Student applies to both
            studentController.apply(student, internshipID1);
            studentController.apply(student, internshipID2);

            // Staff approves both applications
            List<Application> applications = studentController.myApplications(student);
            for (Application app : applications) {
                staffController.decideApplication(app.getApplicationID(), true);
            }

            System.out.println("   Total applications before confirmation: " + applications.size());

            // Student accepts first application
            applications = studentController.myApplications(student);
            Application appToAccept = null;
            for (Application app : applications) {
                if (app.getInternshipID().equals(internshipID1)) {
                    appToAccept = app;
                    break;
                }
            }

            studentController.acceptOffer(student, appToAccept.getApplicationID());

            // Check status of all applications
            applications = studentController.myApplications(student);

            int confirmedCount = 0;
            int withdrawnCount = 0;

            for (Application app : applications) {
                System.out.println("   Application " + app.getApplicationID() +
                        " - Status: " + app.getStatus() +
                        ", Confirmed: " + app.isAcceptedByStudent());

                if (app.isAcceptedByStudent()) confirmedCount++;
                if (app.getStatus() == ApplicationStatus.WITHDRAWN) withdrawnCount++;
            }

            System.out.println("   Confirmed count: " + confirmedCount);
            System.out.println("   Withdrawn count: " + withdrawnCount);

            if (confirmedCount == 1 && withdrawnCount == 1) {
                System.out.println("PASSED: Other applications withdrawn when one is confirmed\n");
                testsPassed++;
            } else {
                System.out.println("FAILED: Expected 1 confirmed and 1 withdrawn\n");
                testsFailed++;
            }

        } catch (Exception e) {
            System.out.println("FAILED: " + e.getMessage() + "\n");
            testsFailed++;
        }
    }

    // Test 7: Confirmation status persists across repository queries
    private static void testConfirmationStatusPersistence() {
        System.out.println("Test 7: Confirmation status persists and can be retrieved correctly");
        try {
            Repository repo = new Repository();
            SystemController sys = new SystemController(repo);

            CompanyRepresentative rep = new CompanyRepresentative(
                "persist.test@company.com",
                "Persist Rep",
                "password",
                "PersistCorp",
                "IT",
                "Director",
                true
            );

            Student student = new Student("U8888888H", "Persist Student", "password",
                    "persist.student@e.ntu.edu.sg", 3, "Computer Science");

            CompanyController companyController = new CompanyController(sys);
            StudentController studentController = new StudentController(sys);
            StaffController staffController = new StaffController(sys);

            // Create and approve internship
            companyController.createInternship(
                rep,
                "Persist Test Internship",
                "Description",
                InternshipLevel.BASIC,
                "Computer Science",
                "2025-07-01",
                "2025-12-31",
                2
            );

            List<Internship> internships = companyController.getInternships(rep);
            staffController.decideInternship(internships.get(0).getInternshipID(), true);

            // Student applies and accepts
            studentController.apply(student, internships.get(0).getInternshipID());

            List<Application> applications = studentController.myApplications(student);
            staffController.decideApplication(applications.get(0).getApplicationID(), true);
            studentController.acceptOffer(student, applications.get(0).getApplicationID());

            // Retrieve multiple times to verify persistence
            List<Application> apps1 = studentController.myApplications(student);
            List<Application> apps2 = companyController.getApplicants(internships.get(0).getInternshipID());
            Application directApp = sys.repository().findApplication(applications.get(0).getApplicationID());

            boolean status1 = apps1.get(0).isAcceptedByStudent();
            boolean status2 = apps2.get(0).isAcceptedByStudent();
            boolean status3 = directApp.isAcceptedByStudent();

            System.out.println("   Status from student query: " + status1);
            System.out.println("   Status from company query: " + status2);
            System.out.println("   Status from direct query: " + status3);

            if (status1 && status2 && status3) {
                System.out.println("PASSED: Confirmation status persists correctly\n");
                testsPassed++;
            } else {
                System.out.println("FAILED: Confirmation status not persisting correctly\n");
                testsFailed++;
            }

        } catch (Exception e) {
            System.out.println("FAILED: " + e.getMessage() + "\n");
            testsFailed++;
        }
    }

    // Test 8: Multiple students can confirm placements independently
    private static void testMultipleStudentsConfirmation() {
        System.out.println("Test 8: Multiple students can confirm placements independently");
        try {
            Repository repo = new Repository();
            SystemController sys = new SystemController(repo);

            CompanyRepresentative rep = new CompanyRepresentative(
                "multi.confirm@company.com",
                "Multi Confirm Rep",
                "password",
                "MultiConfirmCorp",
                "Operations",
                "Manager",
                true
            );

            Student student1 = new Student("U9999999I", "Multi Student 1", "password",
                    "multi1.student@e.ntu.edu.sg", 3, "Computer Science");
            Student student2 = new Student("U0000000J", "Multi Student 2", "password",
                    "multi2.student@e.ntu.edu.sg", 3, "Computer Science");

            CompanyController companyController = new CompanyController(sys);
            StudentController studentController = new StudentController(sys);
            StaffController staffController = new StaffController(sys);

            // Create internship with multiple slots
            companyController.createInternship(
                rep,
                "Multi Confirm Internship",
                "Description",
                InternshipLevel.BASIC,
                "Computer Science",
                "2025-08-01",
                "2025-12-31",
                3
            );

            List<Internship> internships = companyController.getInternships(rep);
            String internshipID = internships.get(0).getInternshipID();
            staffController.decideInternship(internshipID, true);

            // Both students apply
            studentController.apply(student1, internshipID);
            studentController.apply(student2, internshipID);

            // Staff approves both
            List<Application> applications = companyController.getApplicants(internshipID);
            for (Application app : applications) {
                staffController.decideApplication(app.getApplicationID(), true);
            }

            // Find each student's application
            String app1ID = null, app2ID = null;
            for (Application app : applications) {
                if (app.getStudentID().equals(student1.getID())) app1ID = app.getApplicationID();
                if (app.getStudentID().equals(student2.getID())) app2ID = app.getApplicationID();
            }

            // Both students accept
            studentController.acceptOffer(student1, app1ID);
            studentController.acceptOffer(student2, app2ID);

            // Check both confirmations
            Application app1 = sys.repository().findApplication(app1ID);
            Application app2 = sys.repository().findApplication(app2ID);

            System.out.println("   Student1 confirmation: " + app1.isAcceptedByStudent());
            System.out.println("   Student2 confirmation: " + app2.isAcceptedByStudent());
            System.out.println("   Student1 status: " + app1.getStatus());
            System.out.println("   Student2 status: " + app2.getStatus());

            if (app1.isAcceptedByStudent() && app2.isAcceptedByStudent()) {
                System.out.println("PASSED: Multiple students can confirm independently\n");
                testsPassed++;
            } else {
                System.out.println("FAILED: Not all confirmations were recorded\n");
                testsFailed++;
            }

        } catch (Exception e) {
            System.out.println("FAILED: " + e.getMessage() + "\n");
            testsFailed++;
        }
    }
}
