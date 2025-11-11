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
import enums.InternshipStatus;

import java.util.List;

public class CompanyRepApplicationSlotManagementTest {

    private static int testsPassed = 0;
    private static int testsFailed = 0;

    public static void main(String[] args) {
        System.out.println("=== Company Rep Application & Slot Management Test ===\n");
        System.out.println("Testing: Company Representatives retrieve correct student applications,");
        System.out.println("         update slot availability accurately, and correctly confirm placement details\n");

        // Run tests
        testRetrieveApplicationsForOwnInternship();
        testCannotSeeOtherRepApplications();
        testSlotDecrementOnStudentAcceptance();
        testInternshipFilledWhenSlotsReachZero();
        testPlacementDetailsCorrectness();
        testMultipleApplicationsToSameInternship();
        testSlotManagementWithMultipleStudents();

        // Print summary
        System.out.println("\n=== Test Summary ===");
        System.out.println("Tests Passed: " + testsPassed);
        System.out.println("Tests Failed: " + testsFailed);
        System.out.println("Total Tests: " + (testsPassed + testsFailed));

        if (testsFailed == 0) {
            System.out.println("\nPASSED: All tests passed!");
            System.out.println("Application retrieval and slot management working correctly.");
        } else {
            System.out.println("\nFAILED: Some tests failed!");
        }
    }

    // Test 1: Company rep can retrieve applications for their own internships
    private static void testRetrieveApplicationsForOwnInternship() {
        System.out.println("Test 1: Company Rep can retrieve applications for their own internships");
        try {
            Repository repo = new Repository();
            SystemController sys = new SystemController(repo);

            CompanyRepresentative rep = new CompanyRepresentative(
                "retrieve.app@company.com",
                "Retrieve Rep",
                "password",
                "RetrieveCorp",
                "HR",
                "Manager",
                true
            );

            Student student1 = new Student("U1111111A", "Student One", "password",
                    "student1@e.ntu.edu.sg", 3, "Computer Science");
            Student student2 = new Student("U2222222B", "Student Two", "password",
                    "student2@e.ntu.edu.sg", 2, "Computer Science");

            CompanyController companyController = new CompanyController(sys);
            StudentController studentController = new StudentController(sys);
            StaffController staffController = new StaffController(sys);

            // Create internship
            companyController.createInternship(
                rep,
                "Software Engineering Internship",
                "Backend development",
                InternshipLevel.BASIC,
                "Computer Science",
                "2025-01-01",
                "2025-12-31",
                3
            );

            List<Internship> internships = companyController.getInternships(rep);
            String internshipID = internships.get(0).getInternshipID();

            // Staff approves internship (automatically sets visible=true)
            staffController.decideInternship(internshipID, true);

            // Students apply
            studentController.apply(student1, internshipID);
            studentController.apply(student2, internshipID);

            // Company rep retrieves applications
            List<Application> applications = companyController.getApplicants(internshipID);

            System.out.println("   Internship ID: " + internshipID);
            System.out.println("   Applications retrieved: " + applications.size());

            boolean foundStudent1 = false;
            boolean foundStudent2 = false;

            for (Application a : applications) {
                System.out.println("   - Application " + a.getApplicationID() +
                        " from Student " + a.getStudentID() + " (Status: " + a.getStatus() + ")");
                if (a.getStudentID().equals(student1.getID())) foundStudent1 = true;
                if (a.getStudentID().equals(student2.getID())) foundStudent2 = true;
            }

            if (applications.size() == 2 && foundStudent1 && foundStudent2) {
                System.out.println("PASSED: Company Rep retrieved all applications correctly\n");
                testsPassed++;
            } else {
                System.out.println("FAILED: Expected 2 applications, found " + applications.size() + "\n");
                testsFailed++;
            }

        } catch (Exception e) {
            System.out.println("FAILED: " + e.getMessage() + "\n");
            testsFailed++;
        }
    }

    // Test 2: Company rep cannot see applications for other rep's internships
    private static void testCannotSeeOtherRepApplications() {
        System.out.println("Test 2: Company Rep cannot see applications for other representatives' internships");
        try {
            Repository repo = new Repository();
            SystemController sys = new SystemController(repo);

            CompanyRepresentative rep1 = new CompanyRepresentative(
                "rep1.iso@company.com",
                "Rep One",
                "password",
                "Company1",
                "HR",
                "Manager",
                true
            );

            CompanyRepresentative rep2 = new CompanyRepresentative(
                "rep2.iso@company.com",
                "Rep Two",
                "password",
                "Company2",
                "Engineering",
                "Lead",
                true
            );

            Student student = new Student("U3333333C", "Student Three", "password",
                    "student3@e.ntu.edu.sg", 3, "Computer Science");

            CompanyController companyController = new CompanyController(sys);
            StudentController studentController = new StudentController(sys);
            StaffController staffController = new StaffController(sys);

            // Rep1 creates internship
            companyController.createInternship(
                rep1,
                "Rep1 Internship",
                "Description",
                InternshipLevel.BASIC,
                "Computer Science",
                "2025-01-01",
                "2025-12-31",
                2
            );

            // Rep2 creates internship
            companyController.createInternship(
                rep2,
                "Rep2 Internship",
                "Description",
                InternshipLevel.BASIC,
                "Computer Science",
                "2025-01-01",
                "2025-12-31",
                2
            );

            List<Internship> rep1Internships = companyController.getInternships(rep1);
            List<Internship> rep2Internships = companyController.getInternships(rep2);

            String rep1InternshipID = rep1Internships.get(0).getInternshipID();
            String rep2InternshipID = rep2Internships.get(0).getInternshipID();

            // Approve (automatically sets visible=true)
            staffController.decideInternship(rep1InternshipID, true);
            staffController.decideInternship(rep2InternshipID, true);

            // Student applies to Rep1's internship
            studentController.apply(student, rep1InternshipID);

            // Rep1 gets applications (should see 1)
            List<Application> rep1Applications = companyController.getApplicants(rep1InternshipID);

            // Rep2 gets applications for their internship (should see 0)
            List<Application> rep2Applications = companyController.getApplicants(rep2InternshipID);

            System.out.println("   Rep1's internship applications: " + rep1Applications.size());
            System.out.println("   Rep2's internship applications: " + rep2Applications.size());

            if (rep1Applications.size() == 1 && rep2Applications.size() == 0) {
                System.out.println("PASSED: Application isolation between representatives works\n");
                testsPassed++;
            } else {
                System.out.println("FAILED: Application counts don't match expected values\n");
                testsFailed++;
            }

        } catch (Exception e) {
            System.out.println("FAILED: " + e.getMessage() + "\n");
            testsFailed++;
        }
    }

    // Test 3: Slot count decrements when student accepts offer
    private static void testSlotDecrementOnStudentAcceptance() {
        System.out.println("Test 3: Slot count decrements when student accepts offer");
        try {
            Repository repo = new Repository();
            SystemController sys = new SystemController(repo);

            CompanyRepresentative rep = new CompanyRepresentative(
                "slot.test@company.com",
                "Slot Rep",
                "password",
                "SlotCorp",
                "IT",
                "Manager",
                true
            );

            Student student = new Student("U4444444D", "Student Four", "password",
                    "student4@e.ntu.edu.sg", 3, "Computer Science");

            CompanyController companyController = new CompanyController(sys);
            StudentController studentController = new StudentController(sys);
            StaffController staffController = new StaffController(sys);

            // Create internship with 3 slots
            companyController.createInternship(
                rep,
                "Slot Test Internship",
                "Testing slot decrement",
                InternshipLevel.BASIC,
                "Computer Science",
                "2025-01-01",
                "2025-12-31",
                3
            );

            List<Internship> internships = companyController.getInternships(rep);
            String internshipID = internships.get(0).getInternshipID();
            int initialSlots = internships.get(0).getSlots();

            System.out.println("   Initial slots: " + initialSlots);

            // Approve (automatically sets visible=true)
            staffController.decideInternship(internshipID, true);

            // Student applies
            studentController.apply(student, internshipID);

            // Get application
            List<Application> applications = companyController.getApplicants(internshipID);
            String applicationID = applications.get(0).getApplicationID();

            // Staff approves application
            staffController.decideApplication(applicationID, true);

            // Student accepts offer
            studentController.acceptOffer(student, applicationID);

            // Check slots after acceptance
            internships = companyController.getInternships(rep);
            int slotsAfterAcceptance = internships.get(0).getSlots();

            System.out.println("   Slots after acceptance: " + slotsAfterAcceptance);

            if (slotsAfterAcceptance == initialSlots - 1) {
                System.out.println("PASSED: Slot count decremented correctly\n");
                testsPassed++;
            } else {
                System.out.println("FAILED: Expected " + (initialSlots - 1) + " slots, found " + slotsAfterAcceptance + "\n");
                testsFailed++;
            }

        } catch (Exception e) {
            System.out.println("FAILED: " + e.getMessage() + "\n");
            testsFailed++;
        }
    }

    // Test 4: Internship status changes to FILLED when slots reach 0
    private static void testInternshipFilledWhenSlotsReachZero() {
        System.out.println("Test 4: Internship status changes to FILLED when slots reach 0");
        try {
            Repository repo = new Repository();
            SystemController sys = new SystemController(repo);

            CompanyRepresentative rep = new CompanyRepresentative(
                "filled.test@company.com",
                "Filled Rep",
                "password",
                "FilledCorp",
                "HR",
                "Director",
                true
            );

            Student student1 = new Student("U5555555E", "Student Five", "password",
                    "student5@e.ntu.edu.sg", 3, "Computer Science");
            Student student2 = new Student("U6666666F", "Student Six", "password",
                    "student6@e.ntu.edu.sg", 3, "Computer Science");

            CompanyController companyController = new CompanyController(sys);
            StudentController studentController = new StudentController(sys);
            StaffController staffController = new StaffController(sys);

            // Create internship with only 2 slots
            companyController.createInternship(
                rep,
                "Limited Slot Internship",
                "Only 2 slots available",
                InternshipLevel.BASIC,
                "Computer Science",
                "2025-01-01",
                "2025-12-31",
                2
            );

            List<Internship> internships = companyController.getInternships(rep);
            String internshipID = internships.get(0).getInternshipID();

            System.out.println("   Initial status: " + internships.get(0).getStatus());
            System.out.println("   Initial slots: " + internships.get(0).getSlots());

            // Approve (automatically sets visible=true)
            staffController.decideInternship(internshipID, true);

            // Both students apply
            studentController.apply(student1, internshipID);
            studentController.apply(student2, internshipID);

            // Get applications
            List<Application> applications = companyController.getApplicants(internshipID);

            // Staff approves both applications
            for (Application app : applications) {
                staffController.decideApplication(app.getApplicationID(), true);
            }

            // Find each student's application
            String app1ID = null, app2ID = null;
            for (Application app : applications) {
                if (app.getStudentID().equals(student1.getID())) app1ID = app.getApplicationID();
                if (app.getStudentID().equals(student2.getID())) app2ID = app.getApplicationID();
            }

            // Student 1 accepts
            studentController.acceptOffer(student1, app1ID);

            // Check status after first acceptance
            internships = companyController.getInternships(rep);
            System.out.println("   Status after 1st acceptance: " + internships.get(0).getStatus());
            System.out.println("   Slots after 1st acceptance: " + internships.get(0).getSlots());

            // Student 2 accepts
            studentController.acceptOffer(student2, app2ID);

            // Check status after second acceptance
            internships = companyController.getInternships(rep);
            InternshipStatus finalStatus = internships.get(0).getStatus();
            int finalSlots = internships.get(0).getSlots();

            System.out.println("   Final status: " + finalStatus);
            System.out.println("   Final slots: " + finalSlots);

            if (finalStatus == InternshipStatus.FILLED && finalSlots == 0) {
                System.out.println("PASSED: Internship status changed to FILLED when slots reached 0\n");
                testsPassed++;
            } else {
                System.out.println("FAILED: Expected FILLED status with 0 slots, found " + finalStatus + " with " + finalSlots + " slots\n");
                testsFailed++;
            }

        } catch (Exception e) {
            System.out.println("FAILED: " + e.getMessage() + "\n");
            testsFailed++;
        }
    }

    // Test 5: Placement details are correct
    private static void testPlacementDetailsCorrectness() {
        System.out.println("Test 5: Placement details (student info, internship info) are correct");
        try {
            Repository repo = new Repository();
            SystemController sys = new SystemController(repo);

            CompanyRepresentative rep = new CompanyRepresentative(
                "details.check@company.com",
                "Details Rep",
                "password",
                "DetailsCorp",
                "Engineering",
                "Manager",
                true
            );

            Student student = new Student("U7777777G", "John Smith", "password",
                    "john.smith@e.ntu.edu.sg", 3, "Computer Science");

            CompanyController companyController = new CompanyController(sys);
            StudentController studentController = new StudentController(sys);
            StaffController staffController = new StaffController(sys);

            // Create internship
            String expectedTitle = "Data Science Internship";
            String expectedDesc = "Machine Learning Project";
            companyController.createInternship(
                rep,
                expectedTitle,
                expectedDesc,
                InternshipLevel.INTERMEDIATE,
                "Computer Science",
                "2025-02-01",
                "2025-11-30",
                3
            );

            List<Internship> internships = companyController.getInternships(rep);
            String internshipID = internships.get(0).getInternshipID();

            // Approve (automatically sets visible=true)
            staffController.decideInternship(internshipID, true);

            // Student applies
            studentController.apply(student, internshipID);

            // Get application
            List<Application> applications = companyController.getApplicants(internshipID);
            Application app = applications.get(0);

            // Verify placement details
            boolean correctStudentID = app.getStudentID().equals(student.getID());
            boolean correctInternshipID = app.getInternshipID().equals(internshipID);
            boolean hasApplicationID = app.getApplicationID() != null && !app.getApplicationID().isEmpty();
            boolean hasApplyDate = app.getApplyDate() != null && !app.getApplyDate().isEmpty();
            boolean correctStatus = app.getStatus() == ApplicationStatus.PENDING;
            boolean notAcceptedYet = !app.isAcceptedByStudent();

            System.out.println("   Application ID: " + app.getApplicationID());
            System.out.println("   Student ID: " + app.getStudentID() + " (Expected: " + student.getID() + ")");
            System.out.println("   Internship ID: " + app.getInternshipID() + " (Expected: " + internshipID + ")");
            System.out.println("   Status: " + app.getStatus());
            System.out.println("   Apply Date: " + app.getApplyDate());
            System.out.println("   Accepted by Student: " + app.isAcceptedByStudent());

            // Get internship details
            Internship internship = internships.get(0);
            boolean correctTitle = internship.getTitle().equals(expectedTitle);
            boolean correctDescription = internship.getDescription().equals(expectedDesc);
            boolean correctCompany = internship.getCompanyName().equals(rep.getCompanyName());

            System.out.println("   Internship Title: " + internship.getTitle());
            System.out.println("   Company: " + internship.getCompanyName());

            if (correctStudentID && correctInternshipID && hasApplicationID && hasApplyDate &&
                correctStatus && notAcceptedYet && correctTitle && correctDescription && correctCompany) {
                System.out.println("PASSED: All placement details are correct\n");
                testsPassed++;
            } else {
                System.out.println("FAILED: Some placement details are incorrect\n");
                testsFailed++;
            }

        } catch (Exception e) {
            System.out.println("FAILED: " + e.getMessage() + "\n");
            testsFailed++;
        }
    }

    // Test 6: Multiple applications to same internship
    private static void testMultipleApplicationsToSameInternship() {
        System.out.println("Test 6: Company Rep can see multiple applications to the same internship");
        try {
            Repository repo = new Repository();
            SystemController sys = new SystemController(repo);

            CompanyRepresentative rep = new CompanyRepresentative(
                "multi.app@company.com",
                "Multi App Rep",
                "password",
                "MultiAppCorp",
                "HR",
                "Lead",
                true
            );

            Student student1 = new Student("U8888888H", "Alice Lee", "password",
                    "alice.lee@e.ntu.edu.sg", 3, "Computer Science");
            Student student2 = new Student("U9999999I", "Bob Tan", "password",
                    "bob.tan@e.ntu.edu.sg", 2, "Computer Science");
            Student student3 = new Student("U0000000J", "Carol Ng", "password",
                    "carol.ng@e.ntu.edu.sg", 3, "Computer Science");

            CompanyController companyController = new CompanyController(sys);
            StudentController studentController = new StudentController(sys);
            StaffController staffController = new StaffController(sys);

            // Create internship
            companyController.createInternship(
                rep,
                "Popular Internship",
                "Many students interested",
                InternshipLevel.BASIC,
                "Computer Science",
                "2025-03-01",
                "2025-12-31",
                5
            );

            List<Internship> internships = companyController.getInternships(rep);
            String internshipID = internships.get(0).getInternshipID();

            // Approve (automatically sets visible=true)
            staffController.decideInternship(internshipID, true);

            // Three students apply
            studentController.apply(student1, internshipID);
            studentController.apply(student2, internshipID);
            studentController.apply(student3, internshipID);

            // Company rep retrieves all applications
            List<Application> applications = companyController.getApplicants(internshipID);

            System.out.println("   Total applications: " + applications.size());
            for (Application app : applications) {
                System.out.println("   - Application " + app.getApplicationID() +
                        " from " + app.getStudentID() + " (Status: " + app.getStatus() + ")");
            }

            if (applications.size() == 3) {
                System.out.println("PASSED: Company Rep can see all applications\n");
                testsPassed++;
            } else {
                System.out.println("FAILED: Expected 3 applications, found " + applications.size() + "\n");
                testsFailed++;
            }

        } catch (Exception e) {
            System.out.println("FAILED: " + e.getMessage() + "\n");
            testsFailed++;
        }
    }

    // Test 7: Slot management with multiple students accepting
    private static void testSlotManagementWithMultipleStudents() {
        System.out.println("Test 7: Slot management with multiple students accepting offers");
        try {
            Repository repo = new Repository();
            SystemController sys = new SystemController(repo);

            CompanyRepresentative rep = new CompanyRepresentative(
                "multi.slot@company.com",
                "Multi Slot Rep",
                "password",
                "MultiSlotCorp",
                "Operations",
                "Manager",
                true
            );

            Student student1 = new Student("U1010101K", "David Wong", "password",
                    "david.wong@e.ntu.edu.sg", 3, "Computer Science");
            Student student2 = new Student("U2020202L", "Emma Chen", "password",
                    "emma.chen@e.ntu.edu.sg", 3, "Computer Science");
            Student student3 = new Student("U3030303M", "Frank Lim", "password",
                    "frank.lim@e.ntu.edu.sg", 2, "Computer Science");

            CompanyController companyController = new CompanyController(sys);
            StudentController studentController = new StudentController(sys);
            StaffController staffController = new StaffController(sys);

            // Create internship with 4 slots
            companyController.createInternship(
                rep,
                "Multi Slot Internship",
                "Track slot decrement",
                InternshipLevel.BASIC,
                "Computer Science",
                "2025-04-01",
                "2025-12-31",
                4
            );

            List<Internship> internships = companyController.getInternships(rep);
            String internshipID = internships.get(0).getInternshipID();

            System.out.println("   Initial slots: " + internships.get(0).getSlots());

            // Approve (automatically sets visible=true)
            staffController.decideInternship(internshipID, true);

            // Three students apply
            studentController.apply(student1, internshipID);
            studentController.apply(student2, internshipID);
            studentController.apply(student3, internshipID);

            // Get applications and approve all
            List<Application> applications = companyController.getApplicants(internshipID);
            for (Application app : applications) {
                staffController.decideApplication(app.getApplicationID(), true);
            }

            // Find each student's application
            String app1ID = null, app2ID = null, app3ID = null;
            for (Application app : applications) {
                if (app.getStudentID().equals(student1.getID())) app1ID = app.getApplicationID();
                if (app.getStudentID().equals(student2.getID())) app2ID = app.getApplicationID();
                if (app.getStudentID().equals(student3.getID())) app3ID = app.getApplicationID();
            }

            // First student accepts
            studentController.acceptOffer(student1, app1ID);
            internships = companyController.getInternships(rep);
            System.out.println("   Slots after 1st acceptance: " + internships.get(0).getSlots());

            // Second student accepts
            studentController.acceptOffer(student2, app2ID);
            internships = companyController.getInternships(rep);
            System.out.println("   Slots after 2nd acceptance: " + internships.get(0).getSlots());

            // Third student accepts
            studentController.acceptOffer(student3, app3ID);
            internships = companyController.getInternships(rep);
            int finalSlots = internships.get(0).getSlots();
            InternshipStatus finalStatus = internships.get(0).getStatus();

            System.out.println("   Final slots: " + finalSlots);
            System.out.println("   Final status: " + finalStatus);

            // Should have 1 slot remaining and still APPROVED
            if (finalSlots == 1 && finalStatus == InternshipStatus.APPROVED) {
                System.out.println("PASSED: Slot management works correctly with multiple acceptances\n");
                testsPassed++;
            } else {
                System.out.println("FAILED: Expected 1 slot and APPROVED status, found " +
                        finalSlots + " slots and " + finalStatus + " status\n");
                testsFailed++;
            }

        } catch (Exception e) {
            System.out.println("FAILED: " + e.getMessage() + "\n");
            testsFailed++;
        }
    }
}
