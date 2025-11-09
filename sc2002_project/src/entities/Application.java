package entities;

import enums.ApplicationStatus;

/**
 * Represents a student's application to an internship position.
 * Tracks application status, dates, and whether the student accepted an offer.
 */
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
        // check date format
        if (!isValidDate(applyDate)) {
            throw new IllegalArgumentException("Invalid date format. Expected YYYY-MM-DD");
        }
        this.applyDate = applyDate;
		this.acceptedByStudent = acceptedByStudent;
	}

    private boolean isValidDate(String date) {
    try {
        // YYYY-MM-DD
        String[] parts = date.split("-");
        if (parts.length != 3) {
            return false;
        }
//check the length of date
        int year = Integer.parseInt(parts[0]);
        int month = Integer.parseInt(parts[1]);
        int day = Integer.parseInt(parts[2]);
//check the range of year
        if (year < 1000 || year > 9999) return false;
        if (month < 1 || month > 12) return false;
        if (day < 1 || day > 31) return false;
//check the range of day
        int[] daysInMonth = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        if (isLeapYear(year)) {
            daysInMonth[1] = 29;
        }
        return day <= daysInMonth[month - 1];
    } catch (NumberFormatException e) {
        return false;
    }
}

    private boolean isLeapYear(int year) {
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
    }

    private static boolean isValidField(String field) {
        return field != null && !field.trim().isEmpty();
    }

    private static boolean isValidBoolean(String value) {
        if (value == null) {
            return false;
        }
        String lower = value.toLowerCase().trim();
        return lower.equals("true") || lower.equals("false");
    }

	public String[] toCSVRow() { return new String[] {aID, iID, sID, status.name(), applyDate, String.valueOf(acceptedByStudent)}; }

	public static Application fromCSVRow(String[] s) {
//check the length of csv row
        if (s == null || s.length < 6) {
            throw new IllegalArgumentException("CSV row must have at least 6 fields");
        }
        try{
        String aID = s[0];
		String iID = s[1];
		String sID = s[2];
		String statusStr = s[3];
		String date = s[4];
		String acceptedStr = s[5];

		// Validate required fields
		if (!isValidField(aID)) {
			throw new IllegalArgumentException("Invalid application ID: cannot be empty");
		}
		if (!isValidField(iID)) {
			throw new IllegalArgumentException("Invalid internship ID: cannot be empty");
		}
		if (!isValidField(sID)) {
			throw new IllegalArgumentException("Invalid student ID: cannot be empty");
		}

		// Validate boolean
		if (!isValidBoolean(acceptedStr)) {
			throw new IllegalArgumentException("Invalid boolean value for acceptedByStudent: " + acceptedStr);
		}

		ApplicationStatus status = ApplicationStatus.valueOf(statusStr);
		boolean acceptedByStudent = Boolean.valueOf(acceptedStr);
		return new Application(aID, iID, sID, status, date, acceptedByStudent);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid value in CSV row: " + e.getMessage());
        }
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
