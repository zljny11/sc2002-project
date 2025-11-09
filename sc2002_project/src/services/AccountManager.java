package services;

import entities.CompanyRepresentative;
import entities.Repository;
import entities.Staff;
import entities.Student;
import entities.User;
import enums.UserRole;
import utils.InputValidator;
import utils.NotificationService;

/**
 * Manages user accounts and authentication.
 * Handles login, password changes, and user registration.
 */
public class AccountManager {
	private Repository repo;
	public AccountManager(Repository repo) { this.repo = repo; }

	/**
	 * Authenticates a user by checking ID, password, and role.
	 * Company reps must be approved before login.
	 * @param id user ID
	 * @param pw password
	 * @param role user role to authenticate as
	 * @return User object if auth succeeds, null otherwise
	 */
	public User authenticate(String id, String pw, UserRole role) {
		if (role == null) {
			System.out.println("Invalid user role. Please try again.");
			return null;
		}
		switch(role) {
		case STUDENT:
			Student s = repo.findStudent(id);
			if (s != null && s.getPassword().equals(pw)) {
				NotificationService.notify("Login successful");
				return s;
			} else if (s == null) {
				System.out.println("User does not exist. Try again.");
			} else {
				System.out.println("Incorrect password. Try again.");
			}
			break;
		case COMPANY_REP:
			CompanyRepresentative c = repo.findCompanyRep(id);
			if (c != null && c.getPassword().equals(pw)) {
				if (!c.isApproved()) { return null; }
				NotificationService.notify("Login successful");
				return c;
			} else if (c == null) {
				System.out.println("User does not exist. Try again.");
			} else {
				System.out.println("Incorrect password. Try again.");
			}
			break;
		case STAFF:
			Staff t = repo.findStaff(id);
			if (t != null && t.getPassword().equals(pw)) {
				NotificationService.notify("Login successful");
				return t;
			} else if (t == null) {
				System.out.println("User does not exist. Try again.");
			} else {
				System.out.println("Incorrect password. Try again.");
			}
			break;
		}
		return null;
	}

	// called by RegistrationController
	public boolean registerCompanyRep(String id, String name, String pw, String companyName, String dept, String pos) {
		if (!InputValidator.validEmail(id)) {
			System.out.println("Invalid email. Press [1] to try again or any other key to exit.");
			return false;
		}
		if (repo.findCompanyRep(id) != null) {
			System.out.println("Registration already exists. Press [1] to try again or any other key to exit.");
			return false;
		}
		CompanyRepresentative c = new CompanyRepresentative(id, name, pw, companyName, dept, pos, false);
		repo.updateCompanyRep(c);
		NotificationService.notify("Registration '" + id + "' requested");
		return true;
	}

	// called by PasswordController
	
	// forgot password
	public boolean changePassword(String id, String name, UserRole role, String newPW) {
		switch(role) {
		case STUDENT:
			Student s = repo.findStudent(id);
			if (s != null && s.getName().equals(name)) {
				s.setPassword(newPW);
				repo.updateStudent(s);
				NotificationService.notify("Password changed");
				return true;
			}
		case COMPANY_REP:
			CompanyRepresentative c = repo.findCompanyRep(id);
			if (c != null && c.getName().equals(name)) {
				c.setPassword(newPW);
				repo.updateCompanyRep(c);
				NotificationService.notify("Password changed");
				return true;
			}
		case STAFF:
			Staff s1 = repo.findStaff(id);
			if (s1 != null && s1.getName().equals(name)) {
				s1.setPassword(newPW);
				repo.updateStaff(s1);
				NotificationService.notify("Password changed");
				return true;
			}
		}
		System.out.print("User not found. Press [1] to try again or any other key to exit.");
		return false;
	}

	// user logged in
	public boolean changePassword(User u, String oldPW, String newPW) {
		if (!u.getPassword().equals(oldPW)) {
			System.out.print("Incorrect old password. Press [1] to try again or any other key to exit.");
			return false;
		}
		u.setPassword(newPW);
		switch(u.getRole()) {
		case STUDENT:
			repo.updateStudent((Student) u);
			break;
		case COMPANY_REP:
			repo.updateCompanyRep((CompanyRepresentative) u);
		case STAFF:
			repo.updateStaff((Staff) u);
			break;
		}
		NotificationService.notify("Password changed");
		return true;
	}
}
