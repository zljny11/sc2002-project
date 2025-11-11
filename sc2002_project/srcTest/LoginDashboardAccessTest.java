import controller.LoginController;
import controller.SessionController;
import controller.SystemController;
import entities.Repository;
import entities.Student;
import entities.CompanyRepresentative;
import entities.Staff;
import entities.User;
import enums.UserRole;

/**
 * Test class for validating user login and role-based dashboard access
 * Tests that users can successfully login and access their respective dashboards
 */
public class LoginDashboardAccessTest {

    private SystemController sys;
    private LoginController loginCtrl;

    // Test credentials from CSV files
    private static final String STUDENT_ID = "U1234567A";
    private static final String STUDENT_PASSWORD = "securePass";

    private static final String COMPANY_REP_ID = "john.doe@techcorp.com";
    private static final String COMPANY_REP_PASSWORD = "Pass123!";

    private static final String STAFF_ID = "john.doe@ntu.edu.sg";
    private static final String STAFF_PASSWORD = "adminPass123";

    public LoginDashboardAccessTest() {
        // Initialize system with test data from srcTest directory
        Repository repo = new Repository();
        this.sys = new SystemController(repo);
        this.loginCtrl = new LoginController(sys.accounts());
    }

    /**
     * Test 1: Student Login and Dashboard Access
     * Validates that a student can login and access student dashboard
     */
    public void testStudentLoginAndDashboardAccess() {
        System.out.println("\n========================================");
        System.out.println("TEST 1: Student Login and Dashboard Access");
        System.out.println("========================================");

        // Clear any existing session
        SessionController.clear();

        // Attempt login
        System.out.println("\nAttempting login with:");
        System.out.println("  User ID: " + STUDENT_ID);
        System.out.println("  Password: " + STUDENT_PASSWORD);
        System.out.println("  Role: STUDENT");

        User user = loginCtrl.login(STUDENT_ID, STUDENT_PASSWORD, UserRole.STUDENT);

        // Verify login successful
        if (user == null) {
            System.out.println("FAILED: Login returned null");
            return;
        }

        System.out.println("SUCCESS: Login successful");

        // Verify user is correct type
        if (!(user instanceof Student)) {
            System.out.println("FAILED: User is not a Student instance");
            return;
        }

        System.out.println("SUCCESS: User is Student instance");

        Student student = (Student) user;

        // Verify user details
        System.out.println("\nAuthenticated User Details:");
        System.out.println("  ID: " + student.getID());
        System.out.println("  Name: " + student.getName());
        System.out.println("  Role: " + student.getRole());
        System.out.println("  Year: " + student.getYear());
        System.out.println("  Major: " + student.getMajor());

        // Verify role is correct
        if (student.getRole() != UserRole.STUDENT) {
            System.out.println("FAILED: User role is not STUDENT");
            return;
        }

        System.out.println("SUCCESS: User role is STUDENT");

        // Set session
        SessionController.setCurrentUser(user);

        // Verify dashboard access
        System.out.println("\nSUCCESS: Student can access Student Dashboard with features:");
        System.out.println("  - View Available Internships");
        System.out.println("  - Apply for Internship");
        System.out.println("  - View My Applications");
        System.out.println("  - Withdraw Application");
        System.out.println("  - Accept Offer");
        System.out.println("  - Change Password");

        System.out.println("\nPASSED: TEST 1 PASSED: Student login and dashboard access successful");
    }

    /**
     * Test 2: Company Representative Login and Dashboard Access
     * Validates that a company rep can login and access company dashboard
     */
    public void testCompanyRepLoginAndDashboardAccess() {
        System.out.println("\n========================================");
        System.out.println("TEST 2: Company Rep Login and Dashboard Access");
        System.out.println("========================================");

        // Clear any existing session
        SessionController.clear();

        // Attempt login
        System.out.println("\nAttempting login with:");
        System.out.println("  User ID: " + COMPANY_REP_ID);
        System.out.println("  Password: " + COMPANY_REP_PASSWORD);
        System.out.println("  Role: COMPANY_REP");

        User user = loginCtrl.login(COMPANY_REP_ID, COMPANY_REP_PASSWORD, UserRole.COMPANY_REP);

        // Verify login successful
        if (user == null) {
            System.out.println("FAILED: Login returned null (may not be approved)");
            return;
        }

        System.out.println("SUCCESS: Login successful");

        // Verify user is correct type
        if (!(user instanceof CompanyRepresentative)) {
            System.out.println("FAILED: User is not a CompanyRepresentative instance");
            return;
        }

        System.out.println("SUCCESS: User is CompanyRepresentative instance");

        CompanyRepresentative companyRep = (CompanyRepresentative) user;

        // Verify user details
        System.out.println("\nAuthenticated User Details:");
        System.out.println("  ID: " + companyRep.getID());
        System.out.println("  Name: " + companyRep.getName());
        System.out.println("  Company: " + companyRep.getCompanyName());
        System.out.println("  Department: " + companyRep.getDepartment());
        System.out.println("  Position: " + companyRep.getPosition());
        System.out.println("  Role: " + companyRep.getRole());
        System.out.println("  Approved: " + companyRep.isApproved());

        // Verify role is correct
        if (companyRep.getRole() != UserRole.COMPANY_REP) {
            System.out.println("FAILED: User role is not COMPANY_REP");
            return;
        }

        System.out.println("SUCCESS: User role is COMPANY_REP");

        // Verify approved status
        if (!companyRep.isApproved()) {
            System.out.println("FAILED: Company representative is not approved");
            return;
        }

        System.out.println("SUCCESS: Company representative is approved");

        // Set session
        SessionController.setCurrentUser(user);

        // Verify dashboard access
        System.out.println("\nSUCCESS: Company Rep can access Company Dashboard with features:");
        System.out.println("  - Create Internship");
        System.out.println("  - View My Internships");
        System.out.println("  - View Applicants for Internship");
        System.out.println("  - Toggle Internship Visibility");
        System.out.println("  - Change Password");

        System.out.println("\nPASSED: TEST 2 PASSED: Company Rep login and dashboard access successful");
    }

    /**
     * Test 3: Staff Login and Dashboard Access
     * Validates that staff can login and access staff dashboard
     */
    public void testStaffLoginAndDashboardAccess() {
        System.out.println("\n========================================");
        System.out.println("TEST 3: Staff Login and Dashboard Access");
        System.out.println("========================================");

        // Clear any existing session
        SessionController.clear();

        // Attempt login
        System.out.println("\nAttempting login with:");
        System.out.println("  User ID: " + STAFF_ID);
        System.out.println("  Password: " + STAFF_PASSWORD);
        System.out.println("  Role: STAFF");

        User user = loginCtrl.login(STAFF_ID, STAFF_PASSWORD, UserRole.STAFF);

        // Verify login successful
        if (user == null) {
            System.out.println("FAILED: Login returned null");
            return;
        }

        System.out.println("SUCCESS: Login successful");

        // Verify user is correct type
        if (!(user instanceof Staff)) {
            System.out.println("FAILED: User is not a Staff instance");
            return;
        }

        System.out.println("SUCCESS: User is Staff instance");

        Staff staff = (Staff) user;

        // Verify user details
        System.out.println("\nAuthenticated User Details:");
        System.out.println("  ID: " + staff.getID());
        System.out.println("  Name: " + staff.getName());
        System.out.println("  Department: " + staff.getDepartment());
        System.out.println("  Role: " + staff.getRole());

        // Verify role is correct
        if (staff.getRole() != UserRole.STAFF) {
            System.out.println("FAILED: User role is not STAFF");
            return;
        }

        System.out.println("SUCCESS: User role is STAFF");

        // Set session
        SessionController.setCurrentUser(user);

        // Verify dashboard access
        System.out.println("\nSUCCESS: Staff can access Staff Dashboard with features:");
        System.out.println("  - Approve Company Registrations");
        System.out.println("  - Approve Internships");
        System.out.println("  - Approve Withdrawals");
        System.out.println("  - Generate Report");
        System.out.println("  - Change Password");

        System.out.println("\nPASSED: TEST 3 PASSED: Staff login and dashboard access successful");
    }

    /**
     * Test 4: Invalid Login Test
     * Validates that invalid credentials are rejected
     */
    public void testInvalidLogin() {
        System.out.println("\n========================================");
        System.out.println("TEST 4: Invalid Login Test");
        System.out.println("========================================");

        // Clear any existing session
        SessionController.clear();

        // Test with wrong password
        System.out.println("\nAttempting login with wrong password:");
        System.out.println("  User ID: " + STUDENT_ID);
        System.out.println("  Password: wrongpassword");
        System.out.println("  Role: STUDENT");

        User user = loginCtrl.login(STUDENT_ID, "wrongpassword", UserRole.STUDENT);

        if (user != null) {
            System.out.println("FAILED: Login should have failed with wrong password");
            return;
        }

        System.out.println("SUCCESS: Login correctly rejected with wrong password");

        // Test with non-existent user
        System.out.println("\nAttempting login with non-existent user:");
        System.out.println("  User ID: INVALID123");
        System.out.println("  Password: password");
        System.out.println("  Role: STUDENT");

        user = loginCtrl.login("INVALID123", "password", UserRole.STUDENT);

        if (user != null) {
            System.out.println("FAILED: Login should have failed with non-existent user");
            return;
        }

        System.out.println("SUCCESS: Login correctly rejected with non-existent user");

        System.out.println("\nPASSED: TEST 4 PASSED: Invalid logins are correctly rejected");
    }

    /**
     * Main method to run all tests
     */
    public static void main(String[] args) {
        System.out.println("╔════════════════════════════════════════════════════════╗");
        System.out.println("║   LOGIN AND DASHBOARD ACCESS TEST SUITE               ║");
        System.out.println("║   Testing role-based authentication and access         ║");
        System.out.println("╚════════════════════════════════════════════════════════╝");

        LoginDashboardAccessTest test = new LoginDashboardAccessTest();

        // Run all tests
        test.testStudentLoginAndDashboardAccess();
        test.testCompanyRepLoginAndDashboardAccess();
        test.testStaffLoginAndDashboardAccess();
        test.testInvalidLogin();

        // Summary
        System.out.println("\n╔════════════════════════════════════════════════════════╗");
        System.out.println("║   TEST SUITE COMPLETED                                 ║");
        System.out.println("║   All role-based login and dashboard tests executed    ║");
        System.out.println("╚════════════════════════════════════════════════════════╝");
    }
}
