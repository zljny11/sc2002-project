package entities;

import enums.UserRole;

/**
 * Base class for all user types in the system.
 * Subclasses include Student, CompanyRepresentative, and Staff.
 */
public abstract class User {
	protected String id;
	protected String name;
	protected String pw;
	protected UserRole role;

	public User(String id, String name, String pw, UserRole role) {
		this.id = id;
		this.name = name;
		this.pw = pw;
		this.role = role;
	}

	public String getID() { return id; }
	public String getName() { return name; }
	public String getPassword() { return pw; }
	public void setPassword(String pw) { this.pw = pw; }
	public UserRole getRole() { return role; }
}
