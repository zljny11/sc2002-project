package entities;

import enums.UserRole;

public class CompanyRepresentative extends User {
	private String companyName;
	private String dept;
	private String pos;
	private boolean approved;

	public CompanyRepresentative(String id, String name, String pw, String companyName, String dept, String pos, boolean approved) {
		super(id, name, pw, UserRole.COMPANY_REP);
		this.companyName = companyName;
		this.dept = dept;
		this.pos = pos;
		this.approved = approved;
	}

	public String[] toCSVRow() { return new String[] {id, name, pw, companyName, dept, pos, String.valueOf(approved)}; }

    private static boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }

        // Check if @ is included
        int atIndex = email.indexOf('@');
        if (atIndex <= 0 || atIndex != email.lastIndexOf('@')) {
            return false; // No @, or @ at the beginning, or multiple @
        }

        // Check username before @
        String username = email.substring(0, atIndex);
        if (username.isEmpty()) {
            return false;
        }

        // Check domain after @
        String domain = email.substring(atIndex + 1);
        if (domain.isEmpty() || domain.startsWith(".") || domain.endsWith(".")) {
            return false;
        }

        // Check for dot
        if (!domain.contains(".")) {
            return false;
        }

        // Check top-level domain
        int lastDotIndex = domain.lastIndexOf('.');
        String tld = domain.substring(lastDotIndex + 1);
        if (tld.isEmpty()) {
            return false;
        }

        return true;
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
	public static CompanyRepresentative fromCSVRow(String[] s) {
        if (s == null || s.length < 7) {
            throw new IllegalArgumentException("Invalid CSV row: expected at least 7 fields, got " + (s == null ? 0 : s.length));
        }
		String id = s[0];
		String name = s[1];
		String pw = s[2];
		String companyName = s[3];
		String dept = s[4];
		String pos = s[5];
        String approvedStr = s[6];
        if (!isValidEmail(id)) {
            throw new IllegalArgumentException("Invalid email format: " + id);
        }

        if (!isValidField(name)) {
            throw new IllegalArgumentException("Invalid name: cannot be empty");
        }
        if (!isValidField(pw)) {
            throw new IllegalArgumentException("Invalid password: cannot be empty");
        }
        if (!isValidField(companyName)) {
            throw new IllegalArgumentException("Invalid company name: cannot be empty");
        }
        if (!isValidField(dept)) {
            throw new IllegalArgumentException("Invalid department: cannot be empty");
        }
        if (!isValidField(pos)) {
            throw new IllegalArgumentException("Invalid position: cannot be empty");
        }

        if (!isValidBoolean(approvedStr)) {
            throw new IllegalArgumentException("Invalid boolean value: " + approvedStr);
        }
		boolean approved = Boolean.valueOf(approvedStr);
		return new CompanyRepresentative(id, name, pw, companyName, dept, pos, approved);
	}
	
	public String getCompanyName() { return companyName; }
	public String getDepartment() { return dept; }
	public String getPosition() { return pos; }
	public boolean isApproved() { return approved; }
	public void setApproved(boolean approved) { this.approved = approved; }
}