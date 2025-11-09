import entities.Application;
import enums.ApplicationStatus;
import org.junit.Test;

import java.util.Objects;

import static org.junit.Assert.*;

public class entityTestApplication {

    @Test
    public void testApplicationConstructor() {
        Application application = new Application("A001", "I001", "S001", ApplicationStatus.PENDING, "2025-01-01", true);

        assertEquals("A001", application.getApplicationID());
        assertEquals("I001", application.getInternshipID());
        assertEquals("S001", application.getStudentID());
        assertEquals(ApplicationStatus.PENDING, application.getStatus());
        assertEquals("2025-01-01", application.getApplyDate());
        assertTrue(application.isAcceptedByStudent());
    }

    @Test
    public void testApplicationGettersAndSetters() {
        Application application = new Application("A001", "I001", "S001", ApplicationStatus.PENDING, "2023-01-01", true);

        // Test setters
        application.setStatus(ApplicationStatus.SUCCESSFUL);
        application.setAcceptedByStudent(false);

        // Verify getters
        assertEquals(ApplicationStatus.SUCCESSFUL, application.getStatus());
        assertFalse(application.isAcceptedByStudent());
    }

    @Test
    public void testApplicationToCSVRow() {
        Application application = new Application("A001", "I001", "S001", ApplicationStatus.PENDING, "2023-01-01", true);
        String[] csvRow = application.toCSVRow();

        assertArrayEquals(new String[]{"A001", "I001", "S001", ApplicationStatus.PENDING.name(), "2023-01-01", "true"}, csvRow);
    }

    @Test
    public void testApplicationFromCSVRow() {
        String[] csvRow = {"A001", "I001", "S001", ApplicationStatus.PENDING.name(), "2023-01-01", "true"};
        Application application = Application.fromCSVRow(csvRow);

        assertEquals("A001", application.getApplicationID());
        assertEquals("I001", application.getInternshipID());
        assertEquals("S001", application.getStudentID());
        assertEquals(ApplicationStatus.PENDING, application.getStatus());
        assertEquals("2023-01-01", application.getApplyDate());
        assertTrue(application.isAcceptedByStudent());
    }

    @Test
    public void testApplicationFromCSVRowErrorCases() {
        // insufficient fields
        try {
            Application.fromCSVRow(new String[]{"A001", "I001"});
            fail("Should throw IllegalArgumentException for insufficient fields");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("fields"));
        }

        // invalid enums
        try {
            Application.fromCSVRow(new String[]{"A001", "I001", "S001", "INVALID", "2023-01-01", "true"});
            fail("Should throw IllegalArgumentException for invalid status");
        } catch (IllegalArgumentException e) {
        }
    }



}
