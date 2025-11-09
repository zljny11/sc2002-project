package entities;

import enums.UserRole;

/**
 * Represents a student user in the internship system.
 * Students can browse internships, submit applications, and manage their offers.
 *
 * Student ID format: U + 7 digits + letter (e.g., U1234567A)
 * Email must end with @e.ntu.edu.sg
 */
public class Student extends User {
	private String email;
	private int year;
	private String major;

	public Student(String id, String name, String pw, String email, int year, String major) {
		super(id, name, pw, UserRole.STUDENT);
		this.email = email;
		this.year = year;
		this.major = major;
	}

	/**
	 * Converts student data to CSV row format.
	 * @return array of strings representing CSV fields
	 */
	public String[] toCSVRow() { return new String[] {id, name, pw, email, String.valueOf(year), major}; }

	/**
	 * Validates Student ID format: U + 7 digits + letter
	 * Examples: U2345123F, U1234567A
	 */
	private static boolean isValidStudentID(String id) {
		if (id == null || id.length() != 9) {
			return false;
		}

		// Check first character is 'U'
		if (id.charAt(0) != 'U') {
			return false;
		}

		// Check next 7 characters are digits
		for (int i = 1; i <= 7; i++) {
			if (!Character.isDigit(id.charAt(i))) {
				return false;
			}
		}

		// Check last character is a letter
		if (!Character.isLetter(id.charAt(8))) {
			return false;
		}

		return true;
	}

	/**
	 * Validates email ends with @e.ntu.edu.sg
	 */
	private static boolean isValidStudentEmail(String email) {
		if (email == null || email.trim().isEmpty()) {
			return false;
		}
		return email.endsWith("@e.ntu.edu.sg");
	}

	private static boolean isValidField(String field) {
		return field != null && !field.trim().isEmpty();
	}

	/**
	 * Creates a Student object from CSV row data.
	 * Performs validation on all fields including ID format, email domain, and year range.
	 * @param s array of CSV values (id, name, pw, email, year, major)
	 * @return new Student instance
	 * @throws IllegalArgumentException if any field is invalid
	 */
	public static Student fromCSVRow(String[] s) {
		// Check CSV row length
		if (s == null || s.length != 6) {
			throw new IllegalArgumentException("Invalid CSV row: expected 6 fields, got " + (s == null ? 0 : s.length));
		}

		try {
			String id = s[0];
			String name = s[1];
			String pw = s[2];
			String email = s[3];
			String yearStr = s[4];
			String major = s[5];

			// Validate Student ID format
			if (!isValidStudentID(id)) {
				throw new IllegalArgumentException("Invalid Student ID format. Expected: U + 7 digits + letter (e.g., U2345123F), got: " + id);
			}

			// Validate required fields
			if (!isValidField(name)) {
				throw new IllegalArgumentException("Invalid name: cannot be empty");
			}
			if (!isValidField(pw)) {
				throw new IllegalArgumentException("Invalid password: cannot be empty");
			}
			if (!isValidField(email)) {
				throw new IllegalArgumentException("Invalid email: cannot be empty");
			}
			if (!isValidField(major)) {
				throw new IllegalArgumentException("Invalid major: cannot be empty");
			}

			// Validate email domain
			if (!isValidStudentEmail(email)) {
				throw new IllegalArgumentException("Invalid email domain. Student email must end with @e.ntu.edu.sg, got: " + email);
			}

			// Parse and validate year
			int year = Integer.parseInt(yearStr);
			if (year < 1 || year > 6) {
				throw new IllegalArgumentException("Invalid year: must be between 1 and 6, got: " + year);
			}

			return new Student(id, name, pw, email, year, major);
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("Invalid year format: must be a number");
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("Invalid value in CSV row: " + e.getMessage());
		}
	}
	
	public int getYear() { return year; }
	public String getMajor() { return major; }
}
