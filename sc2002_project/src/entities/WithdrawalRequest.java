package entities;

import enums.ApprovalStatus;

public class WithdrawalRequest {
	private String wID;
	private String aID;
	private String sID;
	private ApprovalStatus status;
	private String reqDate; // YYYY-MM-DD

	public WithdrawalRequest(String wID, String aID, String sID, ApprovalStatus status, String reqDate) {
		this.wID = wID;
		this.aID = aID;
		this.sID = sID;
		this.status = status;
		this.reqDate = reqDate;
	}

	public String[] toCSVRow() { return new String[] {wID, aID, sID, status.name(), reqDate}; }

	public static WithdrawalRequest fromCSVRow(String[] s) {
		String wID = s[0];
		String aID = s[1];
		String sID = s[2];
		ApprovalStatus status = ApprovalStatus.valueOf(s[3]);
		String reqDate = s[4];
		return new WithdrawalRequest(wID, aID, sID, status, reqDate);
	}

	public String getRequestID() { return wID; }
	public String getApplicationID() { return aID; }
	public String getStudentID() { return sID; }
	public ApprovalStatus getStatus() { return status; }
	public void setStatus(ApprovalStatus s) { this.status = s; }
	public String getRequestDate() { return reqDate; }
}
