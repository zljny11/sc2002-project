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

	public void setVisibility(CompanyRepresentative c, String iID, boolean visible) {
		Internship i = sys.repository().findInternship(iID);
		if (i != null && i.getCompanyRepID().equals(c.getID())) {
			i.setVisible(visible);
			sys.repository().updateInternship(i);
			NotificationService.notify("Internship '" + iID + "' visibility set to '" + String.valueOf(visible).toUpperCase() + "'");
		}
	}
}
