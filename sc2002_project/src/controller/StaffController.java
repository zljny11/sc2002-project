package controller;

import java.util.List;
import java.util.Map;

import entities.Application;
import entities.CompanyRepresentative;
import entities.Internship;
import entities.ReportSummary;
import entities.WithdrawalRequest;
import enums.ApplicationStatus;
import enums.ApprovalStatus;
import enums.InternshipStatus;
import enums.ReportCategory;
import utils.IDGenerator;
import utils.NotificationService;

public class StaffController {
	private SystemController sys;
	
	public StaffController(SystemController sys) { this.sys = sys; }

	public void decideCompany(String id, boolean approve) {
		CompanyRepresentative c = sys.repository().findCompanyRep(id);
		c.setApproved(approve);
		sys.repository().updateCompanyRep(c);
		NotificationService.notify("Company representative '" + id + "' " + (approve ? "approved" : "rejected"));
	}
	
	public void decideInternship(String iID, boolean approve) {
		Internship i = sys.repository().findInternship(iID);
		if (i != null) {
			i.setStatus(approve ? InternshipStatus.APPROVED : InternshipStatus.REJECTED);
			if (approve) i.setVisible(true);
			sys.repository().updateInternship(i);
			NotificationService.notify("Internship '" + iID + "' " + (approve ? "approved" : "rejected"));
		}
	}
	
	public void decideApplication(String aID, boolean approve) {
		if (approve) {
			sys.applications().staffSetApplicationStatus(aID, ApplicationStatus.SUCCESSFUL);
		} else {
			sys.applications().staffSetApplicationStatus(aID, ApplicationStatus.UNSUCCESSFUL);
		}
	}

	public void decideWithdrawal(String wID, boolean approve) {
		WithdrawalRequest w = sys.repository().findWithdrawal(wID);
		if (w != null) {
			w.setStatus(approve ? ApprovalStatus.APPROVED : ApprovalStatus.REJECTED);
			sys.repository().updateWithdrawal(w);
			if (approve) {
				// set application withdrawn
				Application a = sys.repository().findApplication(w.getApplicationID());
				if (a != null) {
					a.setStatus(ApplicationStatus.WITHDRAWN);
					sys.repository().updateApplication(a);
					// if accepted earlier and acceptedByStudent true, free a slot
					if (a.isAcceptedByStudent()) {
						Internship i = sys.repository().findInternship(a.getInternshipID());
						if (i != null) {
							i.setSlots(i.getSlots()+1);
							if (i.getStatus()==InternshipStatus.FILLED && i.getSlots()>0) i.setStatus(InternshipStatus.APPROVED);
							sys.repository().updateInternship(i);
						}
					}
				}
				NotificationService.notify("Withdrawal request '" + wID + "' approved");
			}
		}
	}
	
	public void generateReport(ReportCategory category, Map<String,String> filters) {
		ReportSummary r = new ReportSummary(IDGenerator.nextReportID(), category);
		r.generate(sys.repository(), filters);
		sys.repository().updateReport(r);
		System.out.println("Report '" + r.getID() + "' generated");
	}
	
	public void getPendingCompanies() {
		List<CompanyRepresentative> l = sys.repository().getAllCompanyReps();
		if (l.isEmpty()) System.out.println("No pending company representative applications");
		else {
			for (CompanyRepresentative c : l) {
				if (!c.isApproved()) {
					System.out.println(c.getID() + " " + c.getName());
					System.out.println("Company: "+c.getCompanyName());
					System.out.println("---------------------------------------------");
				}
			}
		}
	}

	public void getPendingInternships() {
		List<Internship> l = sys.repository().getAllInternships();
		if (l.isEmpty()) System.out.println("No pending internships");
		else {	
			for (Internship i : l) {
				if (i.getStatus()==InternshipStatus.PENDING) {
					System.out.println(i.getInternshipID()+" "+i.getTitle());
				}
			}
		}
	}
	
	public void getWithdrawals() {
		List<WithdrawalRequest> l = sys.repository().getAllWithdrawals();
		if (l.isEmpty()) System.out.println("No pending withdrawal requests");
		else {
			for (WithdrawalRequest w : l) {
				if (w.getStatus()==ApprovalStatus.PENDING) {
					System.out.println(w.getRequestID());
					System.out.println("Application ID: " + w.getApplicationID());
					System.out.println("Student ID: " + w.getStudentID());
					System.out.println("-----------------------------------");
				}
			}
		}
	}
}