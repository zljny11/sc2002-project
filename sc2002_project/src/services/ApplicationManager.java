package services;

import java.util.List;

import entities.Application;
import entities.Internship;
import entities.Repository;
import entities.Student;
import enums.ApplicationStatus;
import enums.InternshipStatus;
import utils.DataUtility;
import utils.IDGenerator;
import utils.NotificationService;

public class ApplicationManager {
	private Repository repo;
	
	public ApplicationManager(Repository repo) { this.repo = repo; }

	public boolean applyForInternship(Student s, String iID) {
		List<Application> l = repo.getApplicationsByStudent(s.getID());

		// count number of pending + successful applications
		long active = l.stream().filter(a -> a.getStatus() == ApplicationStatus.PENDING || a.getStatus() == ApplicationStatus.SUCCESSFUL).count();
		if (active >= 3) {
			System.out.println("Error: You already have 3 active applications. Maximum limit reached.");
			return false;
		}

		// find internship
		Internship i = repo.findInternship(iID);
		if (i == null) {
			System.out.println("Error: Internship not found with ID: " + iID);
			return false;
		}
		if (!i.isVisible()) {
			System.out.println("Error: This internship is not currently visible to students");
			return false;
		}

		if (i.getStatus() != InternshipStatus.APPROVED) {
			System.out.println("Error: This internship is not approved (Status: " + i.getStatus() + ")");
			return false;
		}

		if (i.getStatus() == InternshipStatus.FILLED) {
			System.out.println("Error: This internship is already filled (no slots available)");
			return false;
		}

		String today = DataUtility.currentDate();
		if (i.getClosingDate().compareTo(today) < 0) {
			System.out.println("Error: This internship has closed (Closing date: " + i.getClosingDate() + ")");
			return false;
		}

		// Check if already applied
		for (Application existing : l) {
			if (existing.getInternshipID().equals(iID) &&
				(existing.getStatus() == ApplicationStatus.PENDING || existing.getStatus() == ApplicationStatus.SUCCESSFUL)) {
				System.out.println("Error: You have already applied to this internship");
				return false;
			}
		}

		// apply
		String aID = IDGenerator.nextApplicationID();
		Application a = new Application(aID, iID, s.getID(), ApplicationStatus.PENDING, today, false);
		repo.updateApplication(a);
		NotificationService.notify("Application '" + i.getTitle() + "' submitted");
		return true;
	}

	public List<Application> getApplicationsForStudent(Student s) { return repo.getApplicationsByStudent(s.getID()); }

	public List<Application> getApplicationsForInternship(String iID) { return repo.getApplicationsByInternship(iID); }

	public void staffSetApplicationStatus(String aID, ApplicationStatus status) {
		Application a = repo.findApplication(aID);
		if (a == null) return;
		a.setStatus(status);
		repo.updateApplication(a);
		NotificationService.notify("Application '" + a.getApplicationID() + "' status changed to '" + status + "'");
		
//		Student s = repo.findStudent(a.getStudentID());
//		if (s != null) NotificationService.notify("Application '" + a.getApplicationID() + "' status changed to '" + status + "'");
//		if (status == ApplicationStatus.SUCCESSFUL) {
//			if internship slots filled logic handled when students accept
//			Mark other applications successful? not yet
//		}
	}

	public boolean studentAcceptOffer(Student s, String aID) {
		Application a = repo.findApplication(aID);
		if (a == null) return false;
		if (!a.getStudentID().equals(s.getID())) return false;
		if (a.getStatus() != ApplicationStatus.SUCCESSFUL) return false;
		
		a.setAcceptedByStudent(true);
		repo.updateApplication(a);
		
		// withdraw all other applications
		for (Application a1 : repo.getApplicationsByStudent(s.getID())) {
			if (!a1.getApplicationID().equals(aID)) {
				a1.setStatus(ApplicationStatus.WITHDRAWN);
				repo.updateApplication(a1);
			}
		}
		
		// reduce slot count and mark filled if necessary
		Internship i = repo.findInternship(a.getInternshipID());
		if (i != null) {
			i.setSlots(Math.max(0, i.getSlots()-1));
			if (i.getSlots() == 0) i.setStatus(InternshipStatus.FILLED);
			repo.updateInternship(i);
		}
		NotificationService.notify("Internship '" + aID + "' accepted");
		return true;
	}
}
