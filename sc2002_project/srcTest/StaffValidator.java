import entities.Staff;
import java.io.*;
import java.util.*;

/**
 * Comprehensive validator for Staff CSV test cases
 * Tests Staff ID (email) format and field validations
 */
public class StaffValidator {

    public static void main(String[] args) {
        System.out.println("=== Testing Staff CSV ===");
        testStaffCSV();
    }

    private static void testStaffCSV() {
        String csvFile = "srcTest/staffmembers.csv";
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
                if (values.length >= 4) {
                    System.out.println("  ID: '" + values[0] + "'");
                    System.out.println("  Name: '" + values[1] + "'");
                    System.out.println("  Password: '" + values[2] + "'");
                    System.out.println("  Department: '" + values[3] + "'");
                }

                try {
                    // Check field count
                    if (values.length < 4) {
                        System.out.println("  ✗ ERROR: Insufficient fields (expected 4, got " + values.length + ")");
                        errorCount++;
                        continue;
                    }

                    // Try to parse
                    String[] staffData = Arrays.copyOfRange(values, 0, 4);
                    Staff staff = Staff.fromCSVRow(staffData);

                    // Check for expected errors that should have been caught
                    boolean hasError = false;
                    String errorReason = "";

                    // Validate Staff ID is a valid email
                    String id = values[0];
                    if (!isValidEmail(id)) {
                        hasError = true;
                        errorReason = "Invalid email format for staff ID: " + id;
                    }

                    // Validate staff email ends with @ntu.edu.sg (not @e.ntu.edu.sg)
                    if (isValidEmail(id) && !id.endsWith("@ntu.edu.sg")) {
                        hasError = true;
                        errorReason = "Staff email must end with @ntu.edu.sg, got: " + id;
                    }

                    // Validate empty fields
                    if (values[0].trim().isEmpty() || values[1].trim().isEmpty() ||
                        values[2].trim().isEmpty() || values[3].trim().isEmpty()) {
                        hasError = true;
                        errorReason = "Empty required field detected";
                    }

                    if (hasError) {
                        System.out.println("  ⚠ UNCAUGHT ERROR: " + errorReason);
                        uncaughtErrors.add("Line " + lineNumber + ": " + errorReason);
                        successCount++; // Still parsed successfully (but shouldn't have)
                    } else {
                        System.out.println("  SUCCESS: " + staff.getID() + " - " + staff.getName());
                        System.out.println("    Department: " + staff.getDepartment());
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
            System.out.println("--- Staff CSV Summary ---");
            System.out.println("Total lines processed: " + (lineNumber - 1));
            System.out.println("Successful parses: " + successCount);
            System.out.println("Caught errors: " + errorCount);
            System.out.println("Uncaught errors: " + uncaughtErrors.size());

            if (!uncaughtErrors.isEmpty()) {
                System.out.println("\n⚠ VALIDATION GAPS FOUND:");
                System.out.println("The following errors were NOT caught by Staff.fromCSVRow():");
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
     * Validates basic email format
     */
    private static boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }

        // @ included
        int atIndex = email.indexOf('@');
        if (atIndex <= 0 || atIndex != email.lastIndexOf('@')) {
            return false; // No @, or @ at start, or multiple @
        }

        // Username before @
        String username = email.substring(0, atIndex);
        if (username.isEmpty()) {
            return false;
        }

        // Domain after @
        String domain = email.substring(atIndex + 1);
        if (domain.isEmpty() || domain.startsWith(".") || domain.endsWith(".")) {
            return false;
        }

        // Must contain dot in domain
        if (!domain.contains(".")) {
            return false;
        }

        // Check TLD
        int lastDotIndex = domain.lastIndexOf('.');
        String tld = domain.substring(lastDotIndex + 1);
        if (tld.isEmpty()) {
            return false;
        }

        return true;
    }
}
