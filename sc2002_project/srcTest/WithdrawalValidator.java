import entities.WithdrawalRequest;
import java.io.*;
import java.util.*;

/**
 * Comprehensive validator for WithdrawalRequest CSV test cases
 * Tests withdrawal ID, application ID, student ID, status, and date validations
 */
public class WithdrawalValidator {

    public static void main(String[] args) {
        System.out.println("=== Testing WithdrawalRequest CSV ===");
        testWithdrawalCSV();
    }

    private static void testWithdrawalCSV() {
        String csvFile = "srcTest/withdrawals.csv";
        
        // Try alternative paths if the first one doesn't work
        File file = new File(csvFile);
        if (!file.exists()) {
            // Try with absolute path
            file = new File("sc2002_project/srcTest/withdrawals.csv");
            if (!file.exists()) {
                // Try with relative path from project root
                file = new File("./sc2002_project/srcTest/withdrawals.csv");
            }
        }
        
        if (!file.exists()) {
            System.err.println("Error: Cannot find withdrawals.csv file in any expected location");
            return;
        }
        
        csvFile = file.getPath();
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
                if (values.length >= 5) {
                    System.out.println("  wID: '" + values[0] + "'");
                    System.out.println("  aID: '" + values[1] + "'");
                    System.out.println("  sID: '" + values[2] + "'");
                    System.out.println("  Status: '" + values[3] + "'");
                    System.out.println("  Date: '" + values[4] + "'");
                }

                try {
                    // Check field count
                    if (values.length < 5) {
                        System.out.println("  ✗ ERROR: Insufficient fields (expected 5, got " + values.length + ")");
                        errorCount++;
                        continue;
                    }

                    // Try to parse
                    String[] withdrawalData = Arrays.copyOfRange(values, 0, 5);
                    WithdrawalRequest withdrawal = WithdrawalRequest.fromCSVRow(withdrawalData);

                    // Check for expected errors that should have been caught
                    boolean hasError = false;
                    String errorReason = "";

                    // Validate date format
                    String date = values[4];
                    if (!isValidDate(date)) {
                        hasError = true;
                        errorReason = "Invalid date format (expected YYYY-MM-DD): " + date;
                    }

                    // Validate empty fields
                    if (values[0].trim().isEmpty() || values[1].trim().isEmpty() ||
                        values[2].trim().isEmpty() || values[3].trim().isEmpty() ||
                        values[4].trim().isEmpty()) {
                        hasError = true;
                        errorReason = "Empty required field detected";
                    }

                    // Check for extra fields
                    if (values.length > 5) {
                        hasError = true;
                        errorReason = "Extra fields detected (" + (values.length - 5) + " extra)";
                    }

                    if (hasError) {
                        System.out.println("  ⚠ UNCAUGHT ERROR: " + errorReason);
                        uncaughtErrors.add("Line " + lineNumber + ": " + errorReason);
                        successCount++; // Still parsed successfully (but shouldn't have)
                    } else {
                        System.out.println("  SUCCESS: " + withdrawal.getRequestID());
                        System.out.println("    Application: " + withdrawal.getApplicationID());
                        System.out.println("    Student: " + withdrawal.getStudentID());
                        System.out.println("    Status: " + withdrawal.getStatus());
                        System.out.println("    Date: " + withdrawal.getRequestDate());
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
            System.out.println("--- WithdrawalRequest CSV Summary ---");
            System.out.println("Total lines processed: " + (lineNumber - 1));
            System.out.println("Successful parses: " + successCount);
            System.out.println("Caught errors: " + errorCount);
            System.out.println("Uncaught errors: " + uncaughtErrors.size());

            if (!uncaughtErrors.isEmpty()) {
                System.out.println("\n⚠ VALIDATION GAPS FOUND:");
                System.out.println("The following errors were NOT caught by WithdrawalRequest.fromCSVRow():");
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
     * Validates date format: YYYY-MM-DD
     */
    private static boolean isValidDate(String date) {
        try {
            // YYYY-MM-DD
            String[] parts = date.split("-");
            if (parts.length != 3) {
                return false;
            }
            // Check the length of date
            int year = Integer.parseInt(parts[0]);
            int month = Integer.parseInt(parts[1]);
            int day = Integer.parseInt(parts[2]);
            // Check the range of year
            if (year < 1000 || year > 9999) return false;
            if (month < 1 || month > 12) return false;
            if (day < 1 || day > 31) return false;
            // Check the range of day
            int[] daysInMonth = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
            if (isLeapYear(year)) {
                daysInMonth[1] = 29;
            }
            return day <= daysInMonth[month - 1];
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private static boolean isLeapYear(int year) {
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
    }
}
