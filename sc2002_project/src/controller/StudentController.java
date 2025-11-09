package controller;

import java.util.List;

import entities.Application;
import entities.Internship;
import entities.Student;
import entities.WithdrawalRequest;
import enums.ApprovalStatus;
import utils.DataUtility;
import utils.IDGenerator;
import utils.NotificationService;

/**
 * Controller for student-related operations.
 * Handles viewing internships, applying, managing applications, and withdrawal requests.
 */
public class StudentController {
	private SystemController sys;
	
	public StudentController(SystemController sys) { this.sys = sys; }

	/**
	 * Gets list of internships visible to the student.
	 * @param s the student
	 * @return list of available internships
	 */
	public List<Internship> viewAvailable(Student s) { return sys.internships().listVisibleForStudent(s); }

	/**
	 * Submits an application for an internship.
	 * @param s the student applying
	 * @param iID internship ID
	 * @return true if application was successful
	 */
	public boolean apply(Student s, String iID) { return sys.applications().applyForInternship(s, iID); }

	/**
	 * Gets all applications for a student.
	 * @param s the student
	 * @return list of applications
	 */
	public List<Application> myApplications(Student s) { return sys.applications().getApplicationsForStudent(s); }

	/**
	 * Student accepts an internship offer.
	 * @param s the student
	 * @param aID application ID
	 * @return true if acceptance was successful
	 */
	public boolean acceptOffer(Student s, String aID) { return sys.applications().studentAcceptOffer(s, aID); }

	/**
	 * Requests withdrawal from an application.
	 * Creates a new withdrawal request pending staff approval.
	 * @param s the student
	 * @param aID application ID to withdraw from
	 */
	public void requestWithdrawal(Student s, String aID) {
		Application a = sys.repository().findApplication(aID);
		if (a == null || !a.getStudentID().equals(s.getID())) {
			System.out.println("Withdrawal failed");
			return;
		}
		String wID = IDGenerator.nextWithdrawalID();
		WithdrawalRequest w = new WithdrawalRequest(wID, aID, s.getID(), ApprovalStatus.PENDING, DataUtility.currentDate());
		sys.repository().updateWithdrawal(w);
		NotificationService.notify("Withdrawal '" + wID + "' requested");
	}
}
