package controller;

import entities.Internship;
import entities.Student;
import enums.InternshipLevel;

public class EligibilityController {
	
	public static boolean isEligible(Student s, Internship i) {
		if (s.getYear() <= 2) { return i.getLevel() == InternshipLevel.BASIC; } // if internship is basic, return true
		return true;
	}
	
}
