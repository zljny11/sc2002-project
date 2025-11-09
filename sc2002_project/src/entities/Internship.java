package entities;

import enums.InternshipLevel;
import enums.InternshipStatus;

/**
 * Represents an internship position in the system.
 * Contains all details about the position including dates, requirements, and availability.
 *
 * Date format: YYYY-MM-DD
 * Opening date must be before closing date.
 * Slots must be positive.
 */
public class Internship {
	private String iID;
	private String title;
	private String desc;
	private InternshipLevel lvl;
	private String preferredMajor;
	private String openDate; // YYYY-MM-DD
	private String closeDate; // YYYY-MM-DD
	private InternshipStatus status;
	private String companyName;
	private String cID;
	private int slots;
	private boolean visible;

	public Internship(String iID, String title, String desc, InternshipLevel lvl, String preferredMajor, String openDate, String closeDate, InternshipStatus status, String companyName, String cID, int slots, boolean visible) {
		this.iID = iID;

		// Validate title
		if (!isValidField(title)) {
			throw new IllegalArgumentException("Title cannot be empty");
		}
		this.title = title;

		// Validate description
		if (!isValidField(desc)) {
			throw new IllegalArgumentException("Description cannot be empty");
		}
		this.desc = desc;

		this.lvl = lvl;
		this.preferredMajor = preferredMajor;

		// Validate date formats
		if (!isValidDate(openDate)) {
			throw new IllegalArgumentException("Invalid opening date format. Expected YYYY-MM-DD");
		}
		if (!isValidDate(closeDate)) {
			throw new IllegalArgumentException("Invalid closing date format. Expected YYYY-MM-DD");
		}
		// Validate that opening date is before closing date
		if (openDate.compareTo(closeDate) >= 0) {
			throw new IllegalArgumentException("Opening date must be before closing date. Opening: " + openDate + ", Closing: " + closeDate);
		}
		this.openDate = openDate;
		this.closeDate = closeDate;
		this.status = status;

		// Validate company name
		if (!isValidField(companyName)) {
			throw new IllegalArgumentException("Company name cannot be empty");
		}
		this.companyName = companyName;
		this.cID = cID;

		// Validate slots
		if (slots < 0) {
			throw new IllegalArgumentException("Slots cannot be negative: " + slots);
		}
		if (slots == 0) {
			throw new IllegalArgumentException("Slots must be greater than 0");
		}
		this.slots = slots;
		this.visible = visible;
	}

	/**
	 * Converts internship data to CSV row format.
	 * @return array containing all internship fields
	 */
	public String[] toCSVRow() { return new String[] {iID, title, desc, lvl.name(), preferredMajor, openDate, closeDate, status.name(), companyName, cID, String.valueOf(slots), String.valueOf(visible)}; }

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

	private static boolean isValidBoolean(String value) {
		if (value == null) {
			return false;
		}
		String lower = value.toLowerCase().trim();
		return lower.equals("true") || lower.equals("false");
	}

	/**
	 * Parses a CSV row and creates an Internship object.
	 * Validates all fields including dates, slots, and required text fields.
	 * @param s CSV row with 12 fields
	 * @return new Internship instance
	 * @throws IllegalArgumentException if validation fails
	 */
	public static Internship fromCSVRow(String[] s) {
		// Check CSV row length
		if (s == null || s.length != 12) {
			throw new IllegalArgumentException("Invalid CSV row: expected 12 fields, got " + (s == null ? 0 : s.length));
		}

		try {
			String iID = s[0];
			String title = s[1];
			String desc = s[2];
			String lvlStr = s[3];
			String major = s[4];
			String openDate = s[5];
			String closeDate = s[6];
			String statusStr = s[7];
			String company = s[8];
			String cID = s[9];
			String slotsStr = s[10];
			String visibleStr = s[11];

			// Validate required fields
			if (!isValidField(iID)) {
				throw new IllegalArgumentException("Invalid internship ID: cannot be empty");
			}
			if (!isValidField(title)) {
				throw new IllegalArgumentException("Invalid title: cannot be empty");
			}
			if (!isValidField(desc)) {
				throw new IllegalArgumentException("Invalid description: cannot be empty");
			}
			if (!isValidField(major)) {
				throw new IllegalArgumentException("Invalid preferred major: cannot be empty");
			}
			if (!isValidField(company)) {
				throw new IllegalArgumentException("Invalid company name: cannot be empty");
			}
			if (!isValidField(cID)) {
				throw new IllegalArgumentException("Invalid company rep ID: cannot be empty");
			}

			// Validate boolean
			if (!isValidBoolean(visibleStr)) {
				throw new IllegalArgumentException("Invalid boolean value for visible: " + visibleStr);
			}

			// Parse enums
			InternshipLevel lvl = InternshipLevel.valueOf(lvlStr);
			InternshipStatus status = InternshipStatus.valueOf(statusStr);
			int slots = Integer.parseInt(slotsStr);
			boolean visible = Boolean.valueOf(visibleStr);

			return new Internship(iID, title, desc, lvl, major, openDate, closeDate, status, company, cID, slots, visible);
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("Invalid value in CSV row: " + e.getMessage());
		}
	}
	
	public String getInternshipID() { return iID; }
	public String getTitle() { return title; }
	public String getDescription() { return desc; }
	public InternshipLevel getLevel() { return lvl; }
	public String getPreferredMajor() { return preferredMajor; }
	public String getOpeningDate() { return openDate; }
	public String getClosingDate() { return closeDate; }
	public InternshipStatus getStatus() { return status; }
	public void setStatus(InternshipStatus s) { this.status = s; }
	public String getCompanyName() { return companyName; }
	public String getCompanyRepID() { return cID; }
	public int getSlots() { return slots; }
	public void setSlots(int slots) { this.slots = slots; }
	public boolean isVisible() { return visible; }
	public void setVisible(boolean visible) { this.visible = visible; }

	// Setters for modifying internship details
	public void setTitle(String title) {
		if (!isValidField(title)) {
			throw new IllegalArgumentException("Title cannot be empty");
		}
		this.title = title;
	}

	public void setDescription(String desc) {
		if (!isValidField(desc)) {
			throw new IllegalArgumentException("Description cannot be empty");
		}
		this.desc = desc;
	}

	public void setPreferredMajor(String major) {
		if (!isValidField(major)) {
			throw new IllegalArgumentException("Preferred major cannot be empty");
		}
		this.preferredMajor = major;
	}

	public void setOpeningDate(String openDate) {
		if (!isValidDate(openDate)) {
			throw new IllegalArgumentException("Invalid opening date format. Expected YYYY-MM-DD");
		}
		// Validate that opening date is before closing date
		if (openDate.compareTo(this.closeDate) >= 0) {
			throw new IllegalArgumentException("Opening date must be before closing date. Opening: " + openDate + ", Closing: " + this.closeDate);
		}
		this.openDate = openDate;
	}

	public void setClosingDate(String closeDate) {
		if (!isValidDate(closeDate)) {
			throw new IllegalArgumentException("Invalid closing date format. Expected YYYY-MM-DD");
		}
		// Validate that closing date is after opening date
		if (this.openDate.compareTo(closeDate) >= 0) {
			throw new IllegalArgumentException("Closing date must be after opening date. Opening: " + this.openDate + ", Closing: " + closeDate);
		}
		this.closeDate = closeDate;
	}
}
