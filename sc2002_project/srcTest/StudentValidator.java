import entities.Student;
import java.io.*;
import java.util.*;

/**
 * Comprehensive validator for Student CSV test cases
 * Tests Student ID format (U + 7 digits + letter) and email validation (@e.ntu.edu.sg)
 */
public class StudentValidator {

    public static void main(String[] args) {
        System.out.println("=== Testing Student CSV ===");
        testStudentCSV();
    }

    private static void testStudentCSV() {
        String csvFile = "srcTest/students.csv";
        int lineNumber = 0;
        int successCount = 0;
        int errorCount = 0;
        List<String> uncaughtErrors = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            String line;
            boolean isHeader = true;

            while ((line = br.readLine()) != null) {
                lineNumber++;

                // Skip header
                if (isHeader) {
                    isHeader = false;
                    System.out.println("Header: " + line);
                    continue;
                }

                // Parse line using commas as delimiter
                String[] values = line.split(",", -1);

                // Skip empty lines
                if (values.length == 0 || (values.length == 1 && values[0].trim().isEmpty())) {
                    continue;
                }

                System.out.println("\nLine " + lineNumber + ": " + line);
                System.out.println("  Fields count: " + values.length);

                // Display parsed fields for debugging
                if (values.length >= 6) {
                    System.out.println("  ID: '" + values[0] + "'");
                    System.out.println("  Name: '" + values[1] + "'");
                    System.out.println("  Password: '" + values[2] + "'");
                    System.out.println("  Email: '" + values[3] + "'");
                    System.out.println("  Year: '" + values[4] + "'");
                    System.out.println("  Major: '" + values[5] + "'");
                }

                try {
                    // Check field count
                    if (values.length < 6) {
                        System.out.println("  ✗ ERROR: Insufficient fields (expected 6, got " + values.length + ")");
                        errorCount++;
                        continue;
                    }

                    // Try to parse
                    String[] studentData = Arrays.copyOfRange(values, 0, 6);
                    Student student = Student.fromCSVRow(studentData);

                    // Check for expected errors that should have been caught
                    boolean hasError = false;
                    String errorReason = "";

                    // Validate Student ID format: U + 7 digits + letter
                    String id = values[0];
                    if (!isValidStudentID(id)) {
                        hasError = true;
                        errorReason = "Invalid Student ID format (expected U + 7 digits + letter): " + id;
                    }

                    // Validate email ends with @e.ntu.edu.sg
                    String email = values[3];
                    if (!email.endsWith("@e.ntu.edu.sg")) {
                        hasError = true;
                        errorReason = "Email does not end with @e.ntu.edu.sg: " + email;
                    }

                    // Validate year is positive and reasonable
                    try {
                        int year = Integer.parseInt(values[4]);
                        if (year < 1 || year > 6) {
                            hasError = true;
                            errorReason = "Year out of valid range (1-6): " + year;
                        }
                    } catch (NumberFormatException e) {
                        hasError = true;
                        errorReason = "Invalid year format: " + values[4];
                    }

                    // Validate empty fields
                    if (values[0].trim().isEmpty() || values[1].trim().isEmpty() ||
                        values[2].trim().isEmpty() || values[3].trim().isEmpty() ||
                        values[5].trim().isEmpty()) {
                        hasError = true;
                        errorReason = "Empty required field detected";
                    }

                    if (hasError) {
                        System.out.println("  ⚠ UNCAUGHT ERROR: " + errorReason);
                        uncaughtErrors.add("Line " + lineNumber + ": " + errorReason);
                        successCount++; // Still parsed successfully (but shouldn't have)
                    } else {
                        System.out.println("  SUCCESS: " + student.getID() + " - " + student.getName());
                        System.out.println("    Email: " + values[3]);
                        System.out.println("    Year: " + student.getYear() + ", Major: " + student.getMajor());
                        successCount++;
                    }

                } catch (IllegalArgumentException e) {
                    System.out.println("  SUCCESS: ERROR CAUGHT: " + e.getMessage());
                    errorCount++;
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.out.println("  SUCCESS: ERROR CAUGHT: Array index out of bounds");
                    errorCount++;
                } catch (Exception e) {
                    System.out.println("  ✗ UNEXPECTED ERROR: " + e.getClass().getSimpleName() + ": " + e.getMessage());
                    errorCount++;
                }
            }

            System.out.println("\n" + "=".repeat(70));
            System.out.println("--- Student CSV Summary ---");
            System.out.println("Total lines processed: " + (lineNumber - 1));
            System.out.println("Successful parses: " + successCount);
            System.out.println("Caught errors: " + errorCount);
            System.out.println("Uncaught errors: " + uncaughtErrors.size());

            if (!uncaughtErrors.isEmpty()) {
                System.out.println("\n⚠ VALIDATION GAPS FOUND:");
                System.out.println("The following errors were NOT caught by Student.fromCSVRow():");
                for (String error : uncaughtErrors) {
                    System.out.println("  • " + error);
                }
            } else {
                System.out.println("\nSUCCESS: All validation working correctly!");
            }

            System.out.println("=".repeat(70));

        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }

    /**
     * Validates Student ID format: U + 7 digits + letter
     * Examples: U2345123F, U1234567A
     */
    private static boolean isValidStudentID(String id) {
        if (id == null || id.length() != 9) {
            return false;
        }

        // Check first character is 'U'
        if (id.charAt(0) != 'U') {
            return false;
        }

        // Check next 7 characters are digits
        for (int i = 1; i <= 7; i++) {
            if (!Character.isDigit(id.charAt(i))) {
                return false;
            }
        }

        // Check last character is a letter
        if (!Character.isLetter(id.charAt(8))) {
            return false;
        }

        return true;
    }
}
