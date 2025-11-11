import entities.Internship;
import java.io.*;
import java.util.*;

public class InternshipValidator {

    public static void main(String[] args) {
        System.out.println("=== Testing Internship CSV ===");
        testInternshipCSV();
    }

    private static void testInternshipCSV() {
        String csvFile = "srcTest/internships.csv";
        int lineNumber = 0;
        int successCount = 0;
        int errorCount = 0;

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
                String[] values = line.split(",", -1); // -1 to keep empty trailing fields

                // Skip empty lines
                if (values.length == 0 || (values.length == 1 && values[0].trim().isEmpty())) {
                    continue;
                }

                System.out.println("\nLine " + lineNumber + ": " + line);
                System.out.println("  Fields count: " + values.length);

                try {
                    // Only process if we have exactly 12 fields
                    if (values.length >= 12) {
                        String[] internData = Arrays.copyOfRange(values, 0, 12);
                        Internship intern = Internship.fromCSVRow(internData);
                        System.out.println("  SUCCESS: " + intern.getInternshipID() + " - " + intern.getTitle());

                        // Display parsed data for verification
                        System.out.println("    Company: " + intern.getCompanyName());
                        System.out.println("    Rep ID: " + intern.getCompanyRepID());
                        System.out.println("    Level: " + intern.getLevel());
                        System.out.println("    Status: " + intern.getStatus());
                        System.out.println("    Dates: " + intern.getOpeningDate() + " to " + intern.getClosingDate());
                        System.out.println("    Slots: " + intern.getSlots() + ", Visible: " + intern.isVisible());

                        // Check for potential logical errors
                        if (intern.getSlots() < 0) {
                            System.out.println("    ⚠ WARNING: Negative slots value");
                        }
                        if (intern.getSlots() == 0) {
                            System.out.println("    ⚠ WARNING: Zero slots available");
                        }

                        // Check date ordering (basic validation)
                        if (intern.getOpeningDate() != null && intern.getClosingDate() != null) {
                            if (intern.getOpeningDate().compareTo(intern.getClosingDate()) > 0) {
                                System.out.println("    ⚠ WARNING: Opening date is after closing date");
                            }
                            if (intern.getOpeningDate().equals(intern.getClosingDate())) {
                                System.out.println("    ⚠ WARNING: Opening and closing dates are the same");
                            }
                        }

                        successCount++;
                    } else if (values.length < 12) {
                        System.out.println("  ✗ ERROR: Insufficient fields (expected 12, got " + values.length + ")");
                        errorCount++;
                    } else {
                        // More than 12 fields - try to parse first 12
                        String[] internData = Arrays.copyOfRange(values, 0, 12);
                        Internship intern = Internship.fromCSVRow(internData);
                        System.out.println("  SUCCESS (with extra fields): " + intern.getInternshipID());
                        System.out.println("    ⚠ WARNING: Extra fields detected (" + (values.length - 12) + " extra)");
                        successCount++;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("  ✗ ERROR: Invalid number format - " + e.getMessage());
                    errorCount++;
                } catch (IllegalArgumentException e) {
                    System.out.println("  ✗ ERROR: " + e.getMessage());
                    errorCount++;
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.out.println("  ✗ ERROR: Array index out of bounds");
                    errorCount++;
                } catch (Exception e) {
                    System.out.println("  ✗ ERROR: Unexpected error - " + e.getClass().getSimpleName() + ": " + e.getMessage());
                    errorCount++;
                }
            }

            System.out.println("\n" + "=".repeat(50));
            System.out.println("--- Internship CSV Summary ---");
            System.out.println("Total lines processed: " + (lineNumber - 1));
            System.out.println("Successful: " + successCount);
            System.out.println("Errors: " + errorCount);
            System.out.println("Success rate: " + String.format("%.1f%%", (successCount * 100.0 / (lineNumber - 1))));
            System.out.println("=".repeat(50));

        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }
}
