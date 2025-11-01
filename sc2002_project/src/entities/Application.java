package entities;

import enums.ApplicationStatus;

public class Application {
	private String aID;
	private String iID;
	private String sID;
	private ApplicationStatus status;
	private String applyDate;
	private boolean acceptedByStudent;

	public Application(String aID, String iID, String sID, ApplicationStatus status, String applyDate, boolean acceptedByStudent) {
		this.aID = aID;
		this.iID = iID;
		this.sID = sID;
		this.status = status;
		this.applyDate = applyDate;
		this.acceptedByStudent = acceptedByStudent;
	}

	public String[] toCSVRow() { return new String[] {aID, iID, sID, status.name(), applyDate, String.valueOf(acceptedByStudent)}; }

	public static Application fromCSVRow(String[] s) {
		String aID = s[0];
		String iID = s[1];
		String sID = s[2];
		ApplicationStatus status = ApplicationStatus.valueOf(s[3]);
		String date = s[4];
		boolean acceptedByStudent = Boolean.valueOf(s[5]);
		return new Application(aID, iID, sID, status, date, acceptedByStudent);
	}

	public String getApplicationID() { return aID; }
	public String getInternshipID() { return iID; }
	public String getStudentID() { return sID; }
	public ApplicationStatus getStatus() { return status; }
	public void setStatus(ApplicationStatus s) { this.status = s; }
	public String getApplyDate() { return applyDate; }
	public boolean isAcceptedByStudent() { return acceptedByStudent; }
	public void setAcceptedByStudent(boolean b) { this.acceptedByStudent = b; }
}
