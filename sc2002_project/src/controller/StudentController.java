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

public class StudentController {
	private SystemController sys;
	
	public StudentController(SystemController sys) { this.sys = sys; }

	public List<Internship> viewAvailable(Student s) { return sys.internships().listVisibleForStudent(s); }

	public boolean apply(Student s, String iID) { return sys.applications().applyForInternship(s, iID); }

	public List<Application> myApplications(Student s) { return sys.applications().getApplicationsForStudent(s); }

	public boolean acceptOffer(Student s, String aID) { return sys.applications().studentAcceptOffer(s, aID); }

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
