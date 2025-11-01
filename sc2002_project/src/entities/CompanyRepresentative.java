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

	public static CompanyRepresentative fromCSVRow(String[] s) {
		String id = s[0];
		String name = s[1];
		String pw = s[2];
		String companyName = s[3];
		String dept = s[4];
		String pos = s[5];
		boolean approved = Boolean.valueOf(s[6]);
		return new CompanyRepresentative(id, name, pw, companyName, dept, pos, approved);
	}
	
	public String getCompanyName() { return companyName; }
	public String getDepartment() { return dept; }
	public String getPosition() { return pos; }
	public boolean isApproved() { return approved; }
	public void setApproved(boolean approved) { this.approved = approved; }
}
