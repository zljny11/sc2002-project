package entities;

import enums.InternshipLevel;
import enums.InternshipStatus;

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
		this.title = title;
		this.desc = desc;
		this.lvl = lvl;
		this.preferredMajor = preferredMajor;
		this.openDate = openDate;
		this.closeDate = closeDate;
		this.status = status;
		this.companyName = companyName;
		this.cID = cID;
		this.slots = slots;
		this.visible = visible;
	}

	public String[] toCSVRow() { return new String[] {iID, title, desc, lvl.name(), preferredMajor, openDate, closeDate, status.name(), companyName, cID, String.valueOf(slots), String.valueOf(visible)}; }

	public static Internship fromCSVRow(String[] s) {
		String iID = s[0];
		String title = s[1];
		String desc = s[2];
		InternshipLevel lvl = InternshipLevel.valueOf(s[3]);
		String major = s[4];
		String openDate = s[5];
		String closeDate = s[6];
		InternshipStatus status = InternshipStatus.valueOf(s[7]);
		String company = s[8];
		String cID = s[9];
		int slots = Integer.parseInt(s[10]);
		boolean visible = Boolean.valueOf(s[11]);
		return new Internship(iID, title, desc, lvl, major, openDate, closeDate, status, company, cID, slots, visible);
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
}
