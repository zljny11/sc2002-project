import controller.CompanyController;
import controller.SystemController;
import entities.CompanyRepresentative;
import entities.Internship;
import entities.Repository;
import entities.Student;
import enums.InternshipLevel;
import services.InternshipManager;

import java.util.List;

public class CompanyRepAccessInternshipDetailsTest {

    private static int testsPassed = 0;
    private static int testsFailed = 0;

    public static void main(String[] args) {
        System.out.println("=== Company Representative Access Internship Details Test ===\n");

        // Run tests
        testAccessInvisibleInternships();
        testAccessVisibleInternships();
        testAccessFullInternshipDetails();
        testAccessMixedVisibilityInternships();
        testCannotAccessOtherRepInternships();
        testCompareWithStudentAccess();

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

    // Test 1: Company rep can access invisible internships they created
    private static void testAccessInvisibleInternships() {
        System.out.println("Test 1: Company Rep can access invisible (visible=false) internships");
        try {
            Repository repo = new Repository();
            SystemController sys = new SystemController(repo);

            CompanyRepresentative rep = new CompanyRepresentative(
                "invisible.rep@company.com",
                "Invisible Rep",
                "password",
                "InvisibleCorp",
                "IT",
                "Manager",
                true
            );

            CompanyController controller = new CompanyController(sys);

            // Create internship (defaults to visible=false)
            controller.createInternship(
                rep,
                "Invisible Software Internship",
                "This internship is not visible to students",
                InternshipLevel.BASIC,
                "Computer Science",
                "2025-03-01",
                "2025-08-31",
                4
            );

            // Retrieve internships for this rep
            List<Internship> internships = controller.getInternships(rep);

            boolean foundInvisible = false;
            for (Internship i : internships) {
                if (!i.isVisible() && i.getTitle().equals("Invisible Software Internship")) {
                    System.out.println("   Found invisible internship: " + i.getTitle());
                    System.out.println("   Visible: " + i.isVisible());
                    System.out.println("   Company Rep ID: " + i.getCompanyRepID());
                    foundInvisible = true;
                }
            }

            if (foundInvisible) {
                System.out.println("PASSED: Company Rep can access invisible internships\n");
                testsPassed++;
            } else {
                System.out.println("FAILED: Cannot access invisible internship\n");
                testsFailed++;
            }

        } catch (Exception e) {
            System.out.println("FAILED: " + e.getMessage() + "\n");
            testsFailed++;
        }
    }

    // Test 2: Company rep can access visible internships
    private static void testAccessVisibleInternships() {
        System.out.println("Test 2: Company Rep can access visible (visible=true) internships");
        try {
            Repository repo = new Repository();
            SystemController sys = new SystemController(repo);

            CompanyRepresentative rep = new CompanyRepresentative(
                "visible.rep@company.com",
                "Visible Rep",
                "password",
                "VisibleCorp",
                "Engineering",
                "Director",
                true
            );

            CompanyController controller = new CompanyController(sys);

            // Create internship and set it visible
            controller.createInternship(
                rep,
                "Visible Data Science Internship",
                "This internship is visible to students",
                InternshipLevel.INTERMEDIATE,
                "Data Science",
                "2025-04-01",
                "2025-09-30",
                3
            );

            List<Internship> internships = controller.getInternships(rep);
            String internshipID = internships.get(0).getInternshipID();

            // Set visibility to true
            controller.setVisibility(rep, internshipID, true);

            // Retrieve again
            internships = controller.getInternships(rep);

            boolean foundVisible = false;
            for (Internship i : internships) {
                if (i.isVisible() && i.getTitle().equals("Visible Data Science Internship")) {
                    System.out.println("   Found visible internship: " + i.getTitle());
                    System.out.println("   Visible: " + i.isVisible());
                    foundVisible = true;
                }
            }

            if (foundVisible) {
                System.out.println("PASSED: Company Rep can access visible internships\n");
                testsPassed++;
            } else {
                System.out.println("FAILED: Cannot access visible internship\n");
                testsFailed++;
            }

        } catch (Exception e) {
            System.out.println("FAILED: " + e.getMessage() + "\n");
            testsFailed++;
        }
    }

    // Test 3: Company rep can access full internship details
    private static void testAccessFullInternshipDetails() {
        System.out.println("Test 3: Company Rep can access all internship details");
        try {
            Repository repo = new Repository();
            SystemController sys = new SystemController(repo);

            CompanyRepresentative rep = new CompanyRepresentative(
                "details.rep@company.com",
                "Details Rep",
                "password",
                "DetailsCorp",
                "HR",
                "Lead",
                true
            );

            CompanyController controller = new CompanyController(sys);

            String expectedTitle = "Full Details Internship";
            String expectedDesc = "Comprehensive internship description";
            String expectedMajor = "Software Engineering";
            String expectedOpenDate = "2025-05-01";
            String expectedCloseDate = "2025-10-31";
            int expectedSlots = 7;

            // Create internship with specific details
            controller.createInternship(
                rep,
                expectedTitle,
                expectedDesc,
                InternshipLevel.ADVANCED,
                expectedMajor,
                expectedOpenDate,
                expectedCloseDate,
                expectedSlots
            );

            List<Internship> internships = controller.getInternships(rep);

            if (internships.size() > 0) {
                Internship i = internships.get(0);

                boolean allDetailsMatch =
                    i.getTitle().equals(expectedTitle) &&
                    i.getDescription().equals(expectedDesc) &&
                    i.getPreferredMajor().equals(expectedMajor) &&
                    i.getOpeningDate().equals(expectedOpenDate) &&
                    i.getClosingDate().equals(expectedCloseDate) &&
                    i.getSlots() == expectedSlots &&
                    i.getLevel() == InternshipLevel.ADVANCED &&
                    i.getCompanyName().equals("DetailsCorp") &&
                    i.getCompanyRepID().equals(rep.getID());

                System.out.println("   Internship ID: " + i.getInternshipID());
                System.out.println("   Title: " + i.getTitle());
                System.out.println("   Description: " + i.getDescription());
                System.out.println("   Level: " + i.getLevel());
                System.out.println("   Preferred Major: " + i.getPreferredMajor());
                System.out.println("   Opening Date: " + i.getOpeningDate());
                System.out.println("   Closing Date: " + i.getClosingDate());
                System.out.println("   Slots: " + i.getSlots());
                System.out.println("   Status: " + i.getStatus());
                System.out.println("   Company: " + i.getCompanyName());
                System.out.println("   Visible: " + i.isVisible());

                if (allDetailsMatch) {
                    System.out.println("PASSED: All internship details accessible\n");
                    testsPassed++;
                } else {
                    System.out.println("FAILED: Some details don't match\n");
                    testsFailed++;
                }
            } else {
                System.out.println("FAILED: No internships found\n");
                testsFailed++;
            }

        } catch (Exception e) {
            System.out.println("FAILED: " + e.getMessage() + "\n");
            testsFailed++;
        }
    }

    // Test 4: Company rep can access internships with mixed visibility
    private static void testAccessMixedVisibilityInternships() {
        System.out.println("Test 4: Company Rep can access internships regardless of visibility setting");
        try {
            Repository repo = new Repository();
            SystemController sys = new SystemController(repo);

            CompanyRepresentative rep = new CompanyRepresentative(
                "mixed.vis.rep@company.com",
                "Mixed Visibility Rep",
                "password",
                "MixedCorp",
                "Operations",
                "Coordinator",
                true
            );

            CompanyController controller = new CompanyController(sys);

            // Create 3 internships
            controller.createInternship(rep, "Internship 1", "Description 1",
                    InternshipLevel.BASIC, "CS", "2025-06-01", "2025-11-30", 2);
            controller.createInternship(rep, "Internship 2", "Description 2",
                    InternshipLevel.BASIC, "CS", "2025-06-01", "2025-11-30", 2);
            controller.createInternship(rep, "Internship 3", "Description 3",
                    InternshipLevel.BASIC, "CS", "2025-06-01", "2025-11-30", 2);

            List<Internship> internships = controller.getInternships(rep);

            // Set different visibility
            if (internships.size() >= 3) {
                controller.setVisibility(rep, internships.get(0).getInternshipID(), true);  // visible
                controller.setVisibility(rep, internships.get(1).getInternshipID(), false); // invisible
                controller.setVisibility(rep, internships.get(2).getInternshipID(), true);  // visible
            }

            // Retrieve all internships again
            internships = controller.getInternships(rep);

            int visibleCount = 0, invisibleCount = 0;
            for (Internship i : internships) {
                if (i.isVisible()) {
                    visibleCount++;
                    System.out.println("   " + i.getTitle() + " - Visible: true");
                } else {
                    invisibleCount++;
                    System.out.println("   " + i.getTitle() + " - Visible: false");
                }
            }

            System.out.println("   Total accessible: " + internships.size() +
                             " (Visible: " + visibleCount + ", Invisible: " + invisibleCount + ")");

            if (internships.size() == 3 && invisibleCount > 0) {
                System.out.println("PASSED: Can access all internships regardless of visibility\n");
                testsPassed++;
            } else {
                System.out.println("FAILED: Cannot access all internships\n");
                testsFailed++;
            }

        } catch (Exception e) {
            System.out.println("FAILED: " + e.getMessage() + "\n");
            testsFailed++;
        }
    }

    // Test 5: Company rep cannot access other representatives' internships
    private static void testCannotAccessOtherRepInternships() {
        System.out.println("Test 5: Company Rep cannot access other representatives' internships");
        try {
            Repository repo = new Repository();
            SystemController sys = new SystemController(repo);

            CompanyRepresentative rep1 = new CompanyRepresentative(
                "rep1@company.com",
                "Rep One",
                "password",
                "Company1",
                "HR",
                "Manager",
                true
            );

            CompanyRepresentative rep2 = new CompanyRepresentative(
                "rep2@company.com",
                "Rep Two",
                "password",
                "Company2",
                "Engineering",
                "Lead",
                true
            );

            CompanyController controller = new CompanyController(sys);

            // Rep1 creates internships
            controller.createInternship(rep1, "Rep1 Internship 1", "Description",
                    InternshipLevel.BASIC, "CS", "2025-07-01", "2025-12-31", 3);
            controller.createInternship(rep1, "Rep1 Internship 2", "Description",
                    InternshipLevel.BASIC, "CS", "2025-07-01", "2025-12-31", 3);

            // Rep2 creates internships
            controller.createInternship(rep2, "Rep2 Internship 1", "Description",
                    InternshipLevel.BASIC, "DS", "2025-07-01", "2025-12-31", 2);

            // Get Rep1's internships
            List<Internship> rep1Internships = controller.getInternships(rep1);

            // Check if Rep1 can only see their own internships
            boolean onlyOwnInternships = true;
            for (Internship i : rep1Internships) {
                System.out.println("   Rep1 sees: " + i.getTitle() + " (Created by: " + i.getCompanyRepID() + ")");
                if (!i.getCompanyRepID().equals(rep1.getID())) {
                    onlyOwnInternships = false;
                }
            }

            System.out.println("   Rep1 can see " + rep1Internships.size() + " internships");

            if (onlyOwnInternships && rep1Internships.size() == 2) {
                System.out.println("PASSED: Company Rep can only access their own internships\n");
                testsPassed++;
            } else {
                System.out.println("FAILED: Company Rep can access other reps' internships\n");
                testsFailed++;
            }

        } catch (Exception e) {
            System.out.println("FAILED: " + e.getMessage() + "\n");
            testsFailed++;
        }
    }

    // Test 6: Compare with student access (students cannot see invisible internships)
    private static void testCompareWithStudentAccess() {
        System.out.println("Test 6: Compare Company Rep access vs Student access");
        try {
            Repository repo = new Repository();
            SystemController sys = new SystemController(repo);

            CompanyRepresentative rep = new CompanyRepresentative(
                "compare.rep@company.com",
                "Compare Rep",
                "password",
                "CompareCorp",
                "IT",
                "Manager",
                true
            );

            CompanyController companyController = new CompanyController(sys);
            InternshipManager internshipMgr = sys.internships();

            // Create internship (invisible by default)
            companyController.createInternship(rep, "Comparison Internship", "Test description",
                    InternshipLevel.BASIC, "Computer Science", "2025-08-01", "2026-01-31", 4);

            // Create a student
            Student student = new Student("S1234567X", "Test Student", "password",
                                         "student@test.edu", 3, "Computer Science");

            // Get company rep's view
            List<Internship> repView = companyController.getInternships(rep);

            // Get student's view (only visible internships)
            List<Internship> studentView = internshipMgr.listVisibleForStudent(student);

            // Check if student can see the specific invisible internship
            String internshipID = repView.get(0).getInternshipID();
            boolean studentCanSeeInvisible = false;
            for (Internship i : studentView) {
                if (i.getInternshipID().equals(internshipID)) {
                    studentCanSeeInvisible = true;
                    break;
                }
            }

            System.out.println("   Company Rep can see their internships: " + repView.size());
            System.out.println("   Student can see total visible internships: " + studentView.size());
            System.out.println("   Student can see the invisible internship: " + studentCanSeeInvisible);

            boolean repCanSeeInvisible = repView.size() > 0;

            if (repCanSeeInvisible && !studentCanSeeInvisible) {
                System.out.println("PASSED: Company Rep can see invisible internships, Student cannot\n");
                testsPassed++;
            } else {
                System.out.println("FAILED: Access rights not properly differentiated\n");
                System.out.println("   Details: Rep can see=" + repCanSeeInvisible + ", Student can see invisible=" + studentCanSeeInvisible);
                testsFailed++;
            }

        } catch (Exception e) {
            System.out.println("FAILED: " + e.getMessage() + "\n");
            testsFailed++;
        }
    }
}
