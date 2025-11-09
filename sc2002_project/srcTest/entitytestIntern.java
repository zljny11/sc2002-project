import entities.Internship;
import enums.InternshipLevel;
import enums.InternshipStatus;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.junit.Assert.assertTrue;

public class entitytestIntern {

    @Test
    public void testInternshipConstructor() {
        Internship internship = new Internship("I001", "Software Engineer", "Develop software", InternshipLevel.INTERMEDIATE, "Computer Science", "2023-01-01", "2023-12-31", InternshipStatus.PENDING, "TechCorp", "CR001", 5, true);

        assertEquals("I001", internship.getInternshipID());
        assertEquals("Software Engineer", internship.getTitle());
        assertEquals("Develop software", internship.getDescription());
        assertEquals(InternshipLevel.INTERMEDIATE, internship.getLevel());
        assertEquals("Computer Science", internship.getPreferredMajor());
        assertEquals("2023-01-01", internship.getOpeningDate());
        assertEquals("2023-12-31", internship.getClosingDate());
        assertEquals(InternshipStatus.PENDING, internship.getStatus());
        assertEquals("TechCorp", internship.getCompanyName());
        assertEquals("CR001", internship.getCompanyRepID());
        assertEquals(5, internship.getSlots());
        assertTrue(internship.isVisible());
    }

    @Test
    public void testInternshipGettersAndSetters() {
        Internship internship = new Internship("I001", "Software Engineer", "Develop software", InternshipLevel.INTERMEDIATE, "Computer Science", "2023-01-01", "2023-12-31", InternshipStatus.PENDING, "TechCorp", "CR001", 5, true);

        internship.setStatus(InternshipStatus.APPROVED);
        internship.setSlots(10);
        internship.setVisible(false);

        assertEquals(InternshipStatus.APPROVED, internship.getStatus());
        assertEquals(10, internship.getSlots());
        assertFalse(internship.isVisible());
    }

    @Test
    public void testInternshipToCSVRow() {
        Internship internship = new Internship("I001", "Software Engineer", "Develop software", InternshipLevel.INTERMEDIATE, "Computer Science", "2023-01-01", "2023-12-31", InternshipStatus.PENDING, "TechCorp", "CR001", 5, true);
        String[] csvRow = internship.toCSVRow();

        assertArrayEquals(new String[]{"I001", "Software Engineer", "Develop software", InternshipLevel.INTERMEDIATE.name(), "Computer Science", "2023-01-01", "2023-12-31", InternshipStatus.PENDING.name(), "TechCorp", "CR001", "5", "true"}, csvRow);
    }

    @Test
    public void testInternshipFromCSVRow() {
        String[] csvRow = {"I001", "Software Engineer", "Develop software", InternshipLevel.INTERMEDIATE.name(), "Computer Science", "2023-01-01", "2023-12-31", InternshipStatus.PENDING.name(), "TechCorp", "CR001", "5", "true"};
        Internship internship = Internship.fromCSVRow(csvRow);

        assertEquals("I001", internship.getInternshipID());
        assertEquals("Software Engineer", internship.getTitle());
        assertEquals("Develop software", internship.getDescription());
        assertEquals(InternshipLevel.INTERMEDIATE, internship.getLevel());
        assertEquals("Computer Science", internship.getPreferredMajor());
        assertEquals("2023-01-01", internship.getOpeningDate());
        assertEquals("2023-12-31", internship.getClosingDate());
        assertEquals(InternshipStatus.PENDING, internship.getStatus());
        assertEquals("TechCorp", internship.getCompanyName());
        assertEquals("CR001", internship.getCompanyRepID());
        assertEquals(5, internship.getSlots());
        assertTrue(internship.isVisible());
    }

}
