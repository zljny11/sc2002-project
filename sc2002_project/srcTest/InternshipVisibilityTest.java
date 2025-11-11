import controller.CompanyController;
import controller.StaffController;
import controller.StudentController;
import controller.SystemController;
import entities.*;
import enums.InternshipLevel;
import enums.InternshipStatus;
import services.InternshipManager;

import java.util.List;

public class InternshipVisibilityTest {

    private static int testsPassed = 0;
    private static int testsFailed = 0;

    public static void main(String[] args) {
        System.out.println("=== Internship Visibility Test ===\n");

        // Run tests
        testInvisibleInternshipNotVisibleToStudents();
        testVisibleInternshipVisibleToStudents();
        testToggleVisibilityFromInvisibleToVisible();
        testToggleVisibilityFromVisibleToInvisible();
        testStudentViewOnlyVisibleInternships();
        testEligibleStudentCanSeeVisibleInternship();

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

    // Test 1: Invisible internship is not visible to students
    private static void testInvisibleInternshipNotVisibleToStudents() {
        System.out.println("Test 1: Invisible internship is not visible to students");
        try {
            Repository repo = new Repository();
            SystemController sys = new SystemController(repo);
            InternshipManager internshipManager = sys.internships();
            StudentController studentController = new StudentController(sys);

            // Create a company representative
            CompanyRepresentative rep = new CompanyRepresentative(
                "invisible@company.com",
                "Invisible Rep",
                "password",
                "InvisibleCompany",
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

            // Create invisible internship
            Internship internship = new Internship(
                "I001",
                "Invisible Internship",
                "This internship is not visible",
                InternshipLevel.BASIC,
                "Computer Science",
                "2025-06-01",
                "2025-12-01",
                InternshipStatus.PENDING, // Keep as PENDING so it can be modified
                "InvisibleCompany",
                rep.getID(),
                5,
                false // Not visible
            );
            repo.updateInternship(internship);

            // Check if student can see the internship
            List<Internship> visibleInternships = studentController.viewAvailable(student);
            
            boolean foundInvisibleInternship = false;
            for (Internship i : visibleInternships) {
                if (i.getInternshipID().equals("I001")) {
                    foundInvisibleInternship = true;
                    break;
                }
            }

            System.out.println("   Total visible internships for student: " + visibleInternships.size());
            System.out.println("   Found invisible internship in student view: " + foundInvisibleInternship);

            if (!foundInvisibleInternship) {
                System.out.println("PASSED: Invisible internship is not visible to students\n");
                testsPassed++;
            } else {
                System.out.println("FAILED: Invisible internship is visible to students\n");
                testsFailed++;
            }

        } catch (Exception e) {
            System.out.println("FAILED: " + e.getMessage() + "\n");
            e.printStackTrace();
            testsFailed++;
        }
    }

    // Test 2: Visible internship is visible to students
    private static void testVisibleInternshipVisibleToStudents() {
        System.out.println("Test 2: Visible internship is visible to students");
        try {
            Repository repo = new Repository();
            SystemController sys = new SystemController(repo);
            InternshipManager internshipManager = sys.internships();
            StudentController studentController = new StudentController(sys);

            // Create a company representative
            CompanyRepresentative rep = new CompanyRepresentative(
                "visible@company.com",
                "Visible Rep",
                "password",
                "VisibleCompany",
                "IT",
                "Manager",
                true
            );
            repo.updateCompanyRep(rep);

            // Create student
            Student student = new Student(
                "U7654321B",
                "Test Student 2",
                "password",
                "test2@student.edu",
                3,
                "Computer Science"
            );
            repo.updateStudent(student);

            // Create visible internship
            Internship internship = new Internship(
                "I002",
                "Visible Internship",
                "This internship is visible",
                InternshipLevel.BASIC,
                "Computer Science",
                "2025-06-01",
                "2025-12-01",
                InternshipStatus.PENDING, // Keep as PENDING so it can be modified
                "VisibleCompany",
                rep.getID(),
                5,
                true // Visible
            );
            repo.updateInternship(internship);

            // Check if student can see the internship
            List<Internship> visibleInternships = studentController.viewAvailable(student);
            
            boolean foundVisibleInternship = false;
            for (Internship i : visibleInternships) {
                if (i.getInternshipID().equals("I002")) {
                    foundVisibleInternship = true;
                    System.out.println("   Found internship: " + i.getTitle());
                    System.out.println("   Visibility: " + i.isVisible());
                    break;
                }
            }

            System.out.println("   Total visible internships for student: " + visibleInternships.size());

            if (foundVisibleInternship) {
                System.out.println("PASSED: Visible internship is visible to students\n");
                testsPassed++;
            } else {
                System.out.println("FAILED: Visible internship is not visible to students\n");
                testsFailed++;
            }

        } catch (Exception e) {
            System.out.println("FAILED: " + e.getMessage() + "\n");
            e.printStackTrace();
            testsFailed++;
        }
    }

    // Test 3: Toggle visibility from invisible to visible
    private static void testToggleVisibilityFromInvisibleToVisible() {
        System.out.println("Test 3: Toggle visibility from invisible to visible");
        try {
            Repository repo = new Repository();
            SystemController sys = new SystemController(repo);
            CompanyController companyController = new CompanyController(sys);
            StudentController studentController = new StudentController(sys);

            // Create a company representative
            CompanyRepresentative rep = new CompanyRepresentative(
                "toggle1@company.com",
                "Toggle Rep 1",
                "password",
                "ToggleCompany1",
                "HR",
                "Manager",
                true
            );
            repo.updateCompanyRep(rep);

            // Create student
            Student student = new Student(
                "U1111111C",
                "Test Student 3",
                "password",
                "test3@student.edu",
                3,
                "Computer Science"
            );
            repo.updateStudent(student);

            // Create internship (initially invisible)
            companyController.createInternship(
                rep,
                "Toggle Test Internship",
                "Internship to test visibility toggle",
                InternshipLevel.INTERMEDIATE,
                "Computer Science",
                "2025-07-01",
                "2025-12-31",
                3
            );

            // Get the internship ID
            List<Internship> companyInternships = companyController.getInternships(rep);
            String internshipID = companyInternships.get(0).getInternshipID();
            
            System.out.println("   Created internship ID: " + internshipID);
            System.out.println("   Initial visibility: " + companyInternships.get(0).isVisible());

            // Initially, student should not see it (invisible by default)
            List<Internship> visibleBeforeToggle = studentController.viewAvailable(student);
            boolean foundBefore = false;
            for (Internship i : visibleBeforeToggle) {
                if (i.getInternshipID().equals(internshipID)) {
                    foundBefore = true;
                    break;
                }
            }

            // Toggle visibility to visible
            companyController.setVisibility(rep, internshipID, true);

            // Check if student can now see it
            List<Internship> visibleAfterToggle = studentController.viewAvailable(student);
            boolean foundAfter = false;
            for (Internship i : visibleAfterToggle) {
                if (i.getInternshipID().equals(internshipID)) {
                    foundAfter = true;
                    System.out.println("   Internship now visible: " + i.getTitle());
                    System.out.println("   Visibility status: " + i.isVisible());
                    break;
                }
            }

            System.out.println("   Found before toggle: " + foundBefore);
            System.out.println("   Found after toggle: " + foundAfter);

            if (!foundBefore && foundAfter) {
                System.out.println("PASSED: Visibility correctly toggled from invisible to visible\n");
                testsPassed++;
            } else {
                System.out.println("FAILED: Visibility not correctly toggled\n");
                testsFailed++;
            }

        } catch (Exception e) {
            System.out.println("FAILED: " + e.getMessage() + "\n");
            e.printStackTrace();
            testsFailed++;
        }
    }

    // Test 4: Toggle visibility from visible to invisible
    private static void testToggleVisibilityFromVisibleToInvisible() {
        System.out.println("Test 4: Toggle visibility from visible to invisible");
        try {
            Repository repo = new Repository();
            SystemController sys = new SystemController(repo);
            CompanyController companyController = new CompanyController(sys);
            StudentController studentController = new StudentController(sys);
            // Note: Not using StaffController to approve internship as that would prevent visibility changes

            // Create a company representative
            CompanyRepresentative rep = new CompanyRepresentative(
                "toggle2@company.com",
                "Toggle Rep 2",
                "password",
                "ToggleCompany2",
                "Finance",
                "Manager",
                true
            );
            repo.updateCompanyRep(rep);

            // Create student
            Student student = new Student(
                "U2222222D",
                "Test Student 4",
                "password",
                "test4@student.edu",
                3,
                "Computer Science"
            );
            repo.updateStudent(student);

            // Create internship
            companyController.createInternship(
                rep,
                "Toggle Test Internship 2",
                "Internship to test visibility toggle 2",
                InternshipLevel.ADVANCED,
                "Computer Science",
                "2025-08-01",
                "2026-01-31",
                2
            );

            // Get the internship ID
            List<Internship> companyInternships = companyController.getInternships(rep);
            String internshipID = companyInternships.get(0).getInternshipID();
            
            // Set visibility to true (while still PENDING)
            companyController.setVisibility(rep, internshipID, true);
            
            System.out.println("   Created internship ID: " + internshipID);
            System.out.println("   Set visibility to true");

            // Check if student can see it
            List<Internship> visibleBeforeToggle = studentController.viewAvailable(student);
            boolean foundBefore = false;
            for (Internship i : visibleBeforeToggle) {
                if (i.getInternshipID().equals(internshipID)) {
                    foundBefore = true;
                    break;
                }
            }

            // Toggle visibility to invisible
            companyController.setVisibility(rep, internshipID, false);

            // Check if student can no longer see it
            List<Internship> visibleAfterToggle = studentController.viewAvailable(student);
            boolean foundAfter = false;
            for (Internship i : visibleAfterToggle) {
                if (i.getInternshipID().equals(internshipID)) {
                    foundAfter = true;
                    break;
                }
            }

            System.out.println("   Found before toggle: " + foundBefore);
            System.out.println("   Found after toggle: " + foundAfter);

            if (foundBefore && !foundAfter) {
                System.out.println("PASSED: Visibility correctly toggled from visible to invisible\n");
                testsPassed++;
            } else {
                System.out.println("FAILED: Visibility not correctly toggled\n");
                testsFailed++;
            }

        } catch (Exception e) {
            System.out.println("FAILED: " + e.getMessage() + "\n");
            e.printStackTrace();
            testsFailed++;
        }
    }

    // Test 5: Student view only shows visible internships
    private static void testStudentViewOnlyVisibleInternships() {
        System.out.println("Test 5: Student view only shows visible internships");
        try {
            Repository repo = new Repository();
            SystemController sys = new SystemController(repo);
            StudentController studentController = new StudentController(sys);
            CompanyController companyController = new CompanyController(sys);
            // Note: Not using StaffController to approve internships as that would prevent visibility changes

            // Create a company representative
            CompanyRepresentative rep = new CompanyRepresentative(
                "mixed@company.com",
                "Mixed Rep",
                "password",
                "MixedCompany",
                "Operations",
                "Manager",
                true
            );
            repo.updateCompanyRep(rep);

            // Create student
            Student student = new Student(
                "U3333333E",
                "Test Student 5",
                "password",
                "test5@student.edu",
                3,
                "Computer Science"
            );
            repo.updateStudent(student);

            // Create 3 internships
            companyController.createInternship(
                rep,
                "Internship 1",
                "First internship",
                InternshipLevel.BASIC,
                "Computer Science",
                "2025-09-01",
                "2025-12-31",
                3
            );
            
            companyController.createInternship(
                rep,
                "Internship 2",
                "Second internship",
                InternshipLevel.INTERMEDIATE,
                "Computer Science",
                "2025-09-01",
                "2025-12-31",
                2
            );
            
            companyController.createInternship(
                rep,
                "Internship 3",
                "Third internship",
                InternshipLevel.ADVANCED,
                "Computer Science",
                "2025-09-01",
                "2025-12-31",
                1
            );

            // Get internship IDs
            List<Internship> companyInternships = companyController.getInternships(rep);
            String id1 = companyInternships.get(0).getInternshipID();
            String id2 = companyInternships.get(1).getInternshipID();
            String id3 = companyInternships.get(2).getInternshipID();

            // Set different visibility states (while still PENDING)
            companyController.setVisibility(rep, id1, true);  // Visible
            companyController.setVisibility(rep, id2, false); // Invisible
            companyController.setVisibility(rep, id3, true);  // Visible

            // Check what student can see
            List<Internship> studentView = studentController.viewAvailable(student);
            
            int visibleCount = 0;
            boolean foundId1 = false, foundId2 = false, foundId3 = false;
            for (Internship i : studentView) {
                if (i.getInternshipID().equals(id1)) {
                    foundId1 = true;
                    visibleCount++;
                }
                if (i.getInternshipID().equals(id2)) {
                    foundId2 = true;
                    visibleCount++;
                }
                if (i.getInternshipID().equals(id3)) {
                    foundId3 = true;
                    visibleCount++;
                }
            }

            System.out.println("   Student can see " + studentView.size() + " internships");
            System.out.println("   Expected visible count: 2");
            System.out.println("   Actually visible count: " + visibleCount);
            System.out.println("   Found ID1 (visible): " + foundId1);
            System.out.println("   Found ID2 (invisible): " + foundId2);
            System.out.println("   Found ID3 (visible): " + foundId3);

            if (visibleCount == 2 && foundId1 && !foundId2 && foundId3) {
                System.out.println("PASSED: Student view only shows visible internships\n");
                testsPassed++;
            } else {
                System.out.println("FAILED: Student view does not correctly filter by visibility\n");
                testsFailed++;
            }

        } catch (Exception e) {
            System.out.println("FAILED: " + e.getMessage() + "\n");
            e.printStackTrace();
            testsFailed++;
        }
    }

    // Test 6: Eligible student can see visible internship
    private static void testEligibleStudentCanSeeVisibleInternship() {
        System.out.println("Test 6: Eligible student can see visible internship");
        try {
            Repository repo = new Repository();
            SystemController sys = new SystemController(repo);
            StudentController studentController = new StudentController(sys);
            CompanyController companyController = new CompanyController(sys);
            // Note: Not using StaffController to approve internship as that would prevent visibility changes

            // Create a company representative
            CompanyRepresentative rep = new CompanyRepresentative(
                "eligible@company.com",
                "Eligible Rep",
                "password",
                "EligibleCompany",
                "Research",
                "Manager",
                true
            );
            repo.updateCompanyRep(rep);

            // Create eligible student (year 3, can see all internship levels)
            Student eligibleStudent = new Student(
                "U4444444F",
                "Eligible Student",
                "password",
                "eligible@student.edu",
                3, // Year 3 student - can see all internship levels
                "Business Administration" // Different major, but still eligible based on current logic
            );
            repo.updateStudent(eligibleStudent);

            // Create ineligible student (year 1, can only see BASIC level internships)
            Student ineligibleStudent = new Student(
                "U5555555G",
                "Ineligible Student",
                "password",
                "ineligible@student.edu",
                1, // Year 1 student - can only see BASIC level internships
                "Computer Science"
            );
            repo.updateStudent(ineligibleStudent);

            // Create internship with INTERMEDIATE level
            companyController.createInternship(
                rep,
                "Eligibility Test Internship",
                "Internship to test eligibility",
                InternshipLevel.INTERMEDIATE, // INTERMEDIATE level internship
                "Computer Science",
                "2025-10-01",
                "2026-03-31",
                2
            );

            // Get internship ID
            List<Internship> companyInternships = companyController.getInternships(rep);
            String internshipID = companyInternships.get(0).getInternshipID();

            // Set visibility to true (while still PENDING)
            companyController.setVisibility(rep, internshipID, true);

            // Check what each student can see
            List<Internship> eligibleStudentView = studentController.viewAvailable(eligibleStudent);
            List<Internship> ineligibleStudentView = studentController.viewAvailable(ineligibleStudent);

            boolean eligibleStudentSeesInternship = false;
            for (Internship i : eligibleStudentView) {
                if (i.getInternshipID().equals(internshipID)) {
                    eligibleStudentSeesInternship = true;
                    System.out.println("   Eligible student sees: " + i.getTitle());
                    break;
                }
            }

            boolean ineligibleStudentSeesInternship = false;
            for (Internship i : ineligibleStudentView) {
                if (i.getInternshipID().equals(internshipID)) {
                    ineligibleStudentSeesInternship = true;
                    break;
                }
            }

            System.out.println("   Eligible student view count: " + eligibleStudentView.size());
            System.out.println("   Ineligible student view count: " + ineligibleStudentView.size());
            System.out.println("   Eligible student sees internship: " + eligibleStudentSeesInternship);
            System.out.println("   Ineligible student sees internship: " + ineligibleStudentSeesInternship);

            // Year 3 student should see INTERMEDIATE level internship (eligible)
            // Year 1 student should NOT see INTERMEDIATE level internship (ineligible)
            if (eligibleStudentSeesInternship && !ineligibleStudentSeesInternship) {
                System.out.println("PASSED: Eligibility correctly handled based on student year\n");
                testsPassed++;
            } else if (!eligibleStudentSeesInternship && !ineligibleStudentSeesInternship) {
                System.out.println("FAILED: Neither student can see the internship\n");
                testsFailed++;
            } else if (eligibleStudentSeesInternship && ineligibleStudentSeesInternship) {
                System.out.println("FAILED: Ineligible student can see internship they shouldn't\n");
                testsFailed++;
            } else {
                System.out.println("FAILED: Unexpected eligibility behavior\n");
                testsFailed++;
            }

        } catch (Exception e) {
            System.out.println("FAILED: " + e.getMessage() + "\n");
            e.printStackTrace();
            testsFailed++;
        }
    }
}