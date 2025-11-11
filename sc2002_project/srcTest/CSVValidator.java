import entities.Application;
import entities.CompanyRepresentative;
import java.io.*;
import java.util.*;

public class CSVValidator {

    public static void main(String[] args) {
        System.out.println("=== Testing Application CSV ===");
        testApplicationCSV();

        System.out.println("\n=== Testing CompanyRepresentative CSV ===");
        testCompanyRepCSV();
    }

    private static void testApplicationCSV() {
        String csvFile = "sc2002_project/srcTest/applications.csv";
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

                // Parse line using tabs as delimiter
                String[] values = line.split("\t");

                // Skip empty lines
                if (values.length == 0 || (values.length == 1 && values[0].trim().isEmpty())) {
                    continue;
                }

                System.out.println("\nLine " + lineNumber + ": " + line);
                System.out.println("  Fields: " + Arrays.toString(values));

                try {
                    // Only process if we have at least 6 fields
                    if (values.length >= 6) {
                        String[] appData = Arrays.copyOfRange(values, 0, 6);
                        Application app = Application.fromCSVRow(appData);
                        System.out.println("  SUCCESS: " + app.getApplicationID());
                        successCount++;
                    } else {
                        System.out.println("  ✗ ERROR: Insufficient fields (expected 6, got " + values.length + ")");
                        errorCount++;
                    }
                } catch (Exception e) {
                    System.out.println("  ✗ ERROR: " + e.getMessage());
                    errorCount++;
                }
            }

            System.out.println("\n--- Application CSV Summary ---");
            System.out.println("Total lines processed: " + (lineNumber - 1));
            System.out.println("Successful: " + successCount);
            System.out.println("Errors: " + errorCount);

        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }

    private static void testCompanyRepCSV() {
        String csvFile = "sc2002_project/srcTest/companyreps.csv";
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
                String[] values = line.split(",");

                // Skip empty lines
                if (values.length == 0 || (values.length == 1 && values[0].trim().isEmpty())) {
                    continue;
                }

                System.out.println("\nLine " + lineNumber + ": " + line);
                System.out.println("  Fields: " + Arrays.toString(values));

                try {
                    // Only process if we have exactly 7 fields
                    if (values.length >= 7) {
                        String[] repData = Arrays.copyOfRange(values, 0, 7);
                        CompanyRepresentative rep = CompanyRepresentative.fromCSVRow(repData);
                        System.out.println("  SUCCESS: " + rep.getID());
                        successCount++;
                    } else {
                        System.out.println("  ✗ ERROR: Insufficient fields (expected 7, got " + values.length + ")");
                        errorCount++;
                    }
                } catch (Exception e) {
                    System.out.println("  ✗ ERROR: " + e.getMessage());
                    errorCount++;
                }
            }

            System.out.println("\n--- CompanyRepresentative CSV Summary ---");
            System.out.println("Total lines processed: " + (lineNumber - 1));
            System.out.println("Successful: " + successCount);
            System.out.println("Errors: " + errorCount);

        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }
}