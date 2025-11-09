package services;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import controller.EligibilityController;
import entities.Internship;
import entities.Repository;
import entities.Student;
import enums.InternshipLevel;
import enums.InternshipStatus;
import utils.IDGenerator;

/**
 * Manages internship postings and filtering.
 * Each company rep can create up to 5 internships.
 */
public class InternshipManager {
	private Repository repo;
	private static final int MAX_INTERNSHIPS_PER_REPRESENTATIVE = 5;

	public InternshipManager(Repository repo) { this.repo = repo; }

	/**
	 * Creates a new internship posting.
	 * Enforces max 5 internships per company rep.
	 * @param title internship title
	 * @param desc description
	 * @param lvl difficulty level
	 * @param preferredMajor preferred major
	 * @param openDate opening date (YYYY-MM-DD)
	 * @param closeDate closing date (YYYY-MM-DD)
	 * @param companyName company name
	 * @param cID company rep ID
	 * @param slots number of available slots
	 * @return newly created Internship object
	 * @throws IllegalStateException if rep has reached max internships
	 */
	public Internship createInternship(String title, String desc, InternshipLevel lvl, String preferredMajor, String openDate, String closeDate, String companyName, String cID, int slots) {
		// Check if representative has reached the maximum limit
		List<Internship> existingInternships = listByCompanyRep(cID);
		if (existingInternships.size() >= MAX_INTERNSHIPS_PER_REPRESENTATIVE) {
			throw new IllegalStateException("Maximum internships limit reached. Each representative can create up to " + MAX_INTERNSHIPS_PER_REPRESENTATIVE + " internships.");
		}

		String iID = IDGenerator.nextInternshipID();
		Internship i = new Internship(iID, title, desc, lvl, preferredMajor, openDate, closeDate, InternshipStatus.PENDING, companyName, cID, slots, false);
		repo.updateInternship(i);
		return i;
	}

	/**
	 * Gets all visible internships that the student is eligible for.
	 * Results are sorted by title.
	 * @param s the student
	 * @return list of eligible internships
	 */
	public List<Internship> listVisibleForStudent(Student s) {
		List<Internship> l = new ArrayList<>();
		for (Internship i : repo.getAllInternships()) { if (i.isVisible() && EligibilityController.isEligible(s, i)) { l.add(i); } }
		// sort by title
		l.sort(Comparator.comparing(Internship::getTitle));
		return l;
	}

	public List<Internship> listByCompanyRep(String cID) {
		List<Internship> l = new ArrayList<>();
		for (Internship i : repo.getAllInternships()) { if (i.getCompanyRepID().equals(cID)) { l.add(i); } }
		// sort by internship id
		l.sort(Comparator.comparing(Internship::getInternshipID));
		return l;
	}
}
