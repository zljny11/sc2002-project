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
		// Validate date format
		if (!isValidDate(reqDate)) {
			throw new IllegalArgumentException("Invalid date format. Expected YYYY-MM-DD");
		}
		this.reqDate = reqDate;
	}

	public String[] toCSVRow() { return new String[] {wID, aID, sID, status.name(), reqDate}; }

	private static boolean isValidDate(String date) {
		try {
			// YYYY-MM-DD
			String[] parts = date.split("-");
			if (parts.length != 3) {
				return false;
			}
			// Check the length of date
			int year = Integer.parseInt(parts[0]);
			int month = Integer.parseInt(parts[1]);
			int day = Integer.parseInt(parts[2]);
			// Check the range of year
			if (year < 1000 || year > 9999) return false;
			if (month < 1 || month > 12) return false;
			if (day < 1 || day > 31) return false;
			// Check the range of day
			int[] daysInMonth = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
			if (isLeapYear(year)) {
				daysInMonth[1] = 29;
			}
			return day <= daysInMonth[month - 1];
		} catch (NumberFormatException e) {
			return false;
		}
	}

	private static boolean isLeapYear(int year) {
		return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
	}

	private static boolean isValidField(String field) {
		return field != null && !field.trim().isEmpty();
	}

	public static WithdrawalRequest fromCSVRow(String[] s) {
		// Check CSV row length
		if (s == null || s.length != 5) {
			throw new IllegalArgumentException("Invalid CSV row: expected 5 fields, got " + (s == null ? 0 : s.length));
		}

		try {
			String wID = s[0];
			String aID = s[1];
			String sID = s[2];
			String statusStr = s[3];
			String reqDate = s[4];

			// Validate required fields
			if (!isValidField(wID)) {
				throw new IllegalArgumentException("Invalid withdrawal ID: cannot be empty");
			}
			if (!isValidField(aID)) {
				throw new IllegalArgumentException("Invalid application ID: cannot be empty");
			}
			if (!isValidField(sID)) {
				throw new IllegalArgumentException("Invalid student ID: cannot be empty");
			}
			if (!isValidField(reqDate)) {
				throw new IllegalArgumentException("Invalid request date: cannot be empty");
			}

			// Parse enum (this will throw if invalid)
			ApprovalStatus status = ApprovalStatus.valueOf(statusStr);

			return new WithdrawalRequest(wID, aID, sID, status, reqDate);
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("Invalid value in CSV row: " + e.getMessage());
		}
	}

	public String getRequestID() { return wID; }
	public String getApplicationID() { return aID; }
	public String getStudentID() { return sID; }
	public ApprovalStatus getStatus() { return status; }
	public void setStatus(ApprovalStatus s) { this.status = s; }
	public String getRequestDate() { return reqDate; }
}
