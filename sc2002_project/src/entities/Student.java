package entities;

import enums.UserRole;

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

	public String[] toCSVRow() { return new String[] {id, name, pw, email, String.valueOf(year), major}; }

	public static Student fromCSVRow(String[] s) {
		String id = s[0];
		String name = s[1];
		String pw = s[2];
		String email = s[3];
		int year = Integer.parseInt(s[4]);;
		String major = s[5];
		return new Student(id, name, pw, email, year, major);
	}
	
	public int getYear() { return year; }
	public String getMajor() { return major; }
}
