package entities;

import enums.UserRole;

public class Staff extends User {
	private String dept;

    public Staff(String id, String name, String pw, String dept) {
        super(id, name, pw, UserRole.STAFF);
        this.dept = dept;
    }

    public String[] toCSVRow() { return new String[] {id, name, pw, dept}; }

    public static Staff fromCSVRow(String[] s) {
        String id = s[0];
        String name = s[1];
        String pw = s[2];
        String dept = s[3];
        return new Staff(id, name, pw, dept);
    }
    
    public String getDepartment() { return dept; }
}
