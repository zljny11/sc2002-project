package entities;

import enums.UserRole;

public class Staff extends User {
	private String dept;

    public Staff(String id, String name, String pw, String dept) {
        super(id, name, pw, UserRole.STAFF);
        this.dept = dept;
    }

    public String[] toCSVRow() { return new String[] {id, name, pw, dept}; }

    /**
     * Validates email format for Staff ID
     */
    private static boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }

        // @ included
        int atIndex = email.indexOf('@');
        if (atIndex <= 0 || atIndex != email.lastIndexOf('@')) {
            return false; // No @, or @ at start, or multiple @
        }

        // Username before @
        String username = email.substring(0, atIndex);
        if (username.isEmpty()) {
            return false;
        }

        // Domain after @
        String domain = email.substring(atIndex + 1);
        if (domain.isEmpty() || domain.startsWith(".") || domain.endsWith(".")) {
            return false;
        }

        // Must contain dot in domain
        if (!domain.contains(".")) {
            return false;
        }

        // Check TLD
        int lastDotIndex = domain.lastIndexOf('.');
        String tld = domain.substring(lastDotIndex + 1);
        if (tld.isEmpty()) {
            return false;
        }

        return true;
    }

    /**
     * Validates staff email ends with @ntu.edu.sg
     */
    private static boolean isValidStaffEmail(String email) {
        if (!isValidEmail(email)) {
            return false;
        }
        return email.endsWith("@ntu.edu.sg");
    }

    private static boolean isValidField(String field) {
        return field != null && !field.trim().isEmpty();
    }

    public static Staff fromCSVRow(String[] s) {
        // Check CSV row length
        if (s == null || s.length != 4) {
            throw new IllegalArgumentException("Invalid CSV row: expected 4 fields, got " + (s == null ? 0 : s.length));
        }

        try {
            String id = s[0];
            String name = s[1];
            String pw = s[2];
            String dept = s[3];

            // Validate required fields
            if (!isValidField(id)) {
                throw new IllegalArgumentException("Invalid staff ID: cannot be empty");
            }
            if (!isValidField(name)) {
                throw new IllegalArgumentException("Invalid name: cannot be empty");
            }
            if (!isValidField(pw)) {
                throw new IllegalArgumentException("Invalid password: cannot be empty");
            }
            if (!isValidField(dept)) {
                throw new IllegalArgumentException("Invalid department: cannot be empty");
            }

            // Validate email format
            if (!isValidEmail(id)) {
                throw new IllegalArgumentException("Invalid email format for staff ID: " + id);
            }

            // Validate staff email domain
            if (!isValidStaffEmail(id)) {
                throw new IllegalArgumentException("Invalid staff email domain. Staff email must end with @ntu.edu.sg, got: " + id);
            }

            return new Staff(id, name, pw, dept);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid value in CSV row: " + e.getMessage());
        }
    }
    
    public String getDepartment() { return dept; }
}
