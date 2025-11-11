import entities.Report;
import java.io.*;
import java.util.*;

public class ReportValidator {

    public static void main(String[] args) {
        System.out.println("=== Testing Report CSV ===");
        testReportCSV();
    }

    private static void testReportCSV() {
        String csvFile = "srcTest/reports.csv";
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
                    // Only process if we have exactly 4 fields
                    if (values.length >= 4) {
                        String[] reportData = Arrays.copyOfRange(values, 0, 4);
                        Report report = Report.fromCSVRow(reportData);
                        System.out.println("  SUCCESS: " + report.getID() + " - " + report.getCategory());

                        // Display parsed data for verification
                        System.out.println("    Category: " + report.getCategory());
                        System.out.println("    Date: " + report.getGeneratedDate());
                        System.out.println("    Content length: " + report.getContent().length() + " chars");
                        if (report.getContent().contains("\n")) {
                            System.out.println("    ⚠ WARNING: Content contains newlines");
                        }

                        // Check for potential issues
                        if (report.getID() == null || report.getID().trim().isEmpty()) {
                            System.out.println("    ⚠ WARNING: Empty ID");
                        }
                        if (report.getGeneratedDate() == null || report.getGeneratedDate().trim().isEmpty()) {
                            System.out.println("    ⚠ WARNING: Empty date");
                        }

                        successCount++;
                    } else if (values.length < 4) {
                        System.out.println("  ✗ ERROR: Insufficient fields (expected 4, got " + values.length + ")");
                        errorCount++;
                    } else {
                        // More than 4 fields - try to parse first 4
                        String[] reportData = Arrays.copyOfRange(values, 0, 4);
                        Report report = Report.fromCSVRow(reportData);
                        System.out.println("  SUCCESS (with extra fields): " + report.getID());
                        System.out.println("    ⚠ WARNING: Extra fields detected (" + (values.length - 4) + " extra)");
                        successCount++;
                    }
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
            System.out.println("--- Report CSV Summary ---");
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
