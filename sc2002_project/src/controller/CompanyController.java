package controller;

import java.util.List;

import entities.Application;
import entities.CompanyRepresentative;
import entities.Internship;
import enums.ApplicationStatus;
import enums.InternshipLevel;
import utils.NotificationService;

public class CompanyController {
	private SystemController sys;
	
	public CompanyController(SystemController sys) { this.sys = sys; }

	public void createInternship(CompanyRepresentative c, String title, String desc, InternshipLevel lvl, String preferredMajor, String openDate, String closeDate, int slots) {
		Internship i = sys.internships().createInternship(title, desc, lvl, preferredMajor, openDate, closeDate, c.getCompanyName(), c.getID(), slots);
		NotificationService.notify("Internship '" + i.getInternshipID() + "' applied");
	}

	public void decideApplication(String aID, boolean approve) {
		if (approve) {
			sys.applications().staffSetApplicationStatus(aID, ApplicationStatus.SUCCESSFUL);
			NotificationService.notify("Application '" + aID + "' status set to '" + String.valueOf(ApplicationStatus.SUCCESSFUL) + "'");
		} else {
			sys.applications().staffSetApplicationStatus(aID, ApplicationStatus.UNSUCCESSFUL);
			NotificationService.notify("Application '" + aID + "' status set to '" + String.valueOf(ApplicationStatus.UNSUCCESSFUL) + "'");
		}
	}
	
	public List<Internship> getInternships(CompanyRepresentative c) { return sys.internships().listByCompanyRep(c.getID()); }

	public List<Application> getApplicants(String iID) { return sys.repository().getApplicationsByInternship(iID); }

	public boolean setVisibility(CompanyRepresentative c, String iID, boolean visible) {
		Internship i = sys.repository().findInternship(iID);
		if (i == null) {
			System.out.println("Error: Internship not found");
			return false;
		}
		if (!i.getCompanyRepID().equals(c.getID())) {
			System.out.println("Error: You can only modify your own internships");
			return false;
		}
		// Check if internship can be edited (only PENDING internships can be edited)
		if (i.getStatus() != enums.InternshipStatus.PENDING) {
			System.out.println("Error: Cannot edit internship after it has been " +
				i.getStatus().toString().toLowerCase() + " by Career Center Staff. " +
				"Only PENDING internships can be modified.");
			return false;
		}
		i.setVisible(visible);
		sys.repository().updateInternship(i);
		NotificationService.notify("Internship '" + iID + "' visibility set to '" + String.valueOf(visible).toUpperCase() + "'");
		return true;
	}

	public void modifyInternship(CompanyRepresentative c, String iID, String title, String desc, String preferredMajor, String openDate, String closeDate, int slots) {
		Internship i = sys.repository().findInternship(iID);
		if (i == null) {
			throw new IllegalArgumentException("Internship not found");
		}
		if (!i.getCompanyRepID().equals(c.getID())) {
			throw new IllegalStateException("Cannot modify internship belonging to another company representative");
		}
		// Check if internship can be edited (only PENDING internships can be edited)
		if (i.getStatus() != enums.InternshipStatus.PENDING) {
			throw new IllegalStateException("Cannot modify internship after it has been " +
				i.getStatus().toString().toLowerCase() + " by Career Center Staff. " +
				"Only PENDING internships can be modified.");
		}
		// Update fields
		i.setTitle(title);
		i.setDescription(desc);
		i.setPreferredMajor(preferredMajor);
		i.setOpeningDate(openDate);
		i.setClosingDate(closeDate);
		i.setSlots(slots);
		sys.repository().updateInternship(i);
		NotificationService.notify("Internship '" + iID + "' modified successfully");
	}

	public void removeInternship(CompanyRepresentative c, String iID) {
		Internship i = sys.repository().findInternship(iID);
		if (i == null) {
			throw new IllegalArgumentException("Internship not found");
		}
		if (!i.getCompanyRepID().equals(c.getID())) {
			throw new IllegalStateException("Cannot remove internship belonging to another company representative");
		}
		// Check if internship can be removed (only PENDING internships can be removed)
		if (i.getStatus() != enums.InternshipStatus.PENDING) {
			throw new IllegalStateException("Cannot remove internship after it has been " +
				i.getStatus().toString().toLowerCase() + " by Career Center Staff. " +
				"Only PENDING internships can be removed.");
		}
		sys.repository().removeInternship(iID);
		NotificationService.notify("Internship '" + iID + "' removed successfully");
	}
}
