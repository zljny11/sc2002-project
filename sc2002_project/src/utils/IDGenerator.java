package utils;

import java.util.List;

/**
 * Generates unique IDs for internships, applications, withdrawals, and reports.
 * Automatically initializes counters from existing CSV data to prevent duplicates.
 * ID formats: Ixxx, Axxx, Wxxx, Rxxx (where xxx is a number)
 */
public class IDGenerator {
	private static int internshipCounter = 1000;
	private static int applicationCounter = 2000;
	private static int withdrawalCounter = 3000;
	private static int reportCounter = 4000;

	private static boolean initialized = false;

	/**
	 * Initialize counters by reading existing CSV files to find the maximum ID
	 * This prevents ID collisions when the program restarts
	 */
	public static void initialize() {
		if (initialized) return;

		// Read internships and find max ID
		List<String[]> internships = FileHandler.readCSV("internships.csv");
		for (String[] row : internships) {
			if (row.length > 0 && row[0].startsWith("I")) {
				try {
					int id = Integer.parseInt(row[0].substring(1));
					if (id >= internshipCounter) {
						internshipCounter = id + 1;
					}
				} catch (NumberFormatException e) {
					// Skip invalid IDs
				}
			}
		}

		// Read applications and find max ID
		List<String[]> applications = FileHandler.readCSV("applications.csv");
		for (String[] row : applications) {
			if (row.length > 0 && row[0].startsWith("A")) {
				try {
					int id = Integer.parseInt(row[0].substring(1));
					if (id >= applicationCounter) {
						applicationCounter = id + 1;
					}
				} catch (NumberFormatException e) {
					// Skip invalid IDs
				}
			}
		}

		// Read withdrawals and find max ID
		List<String[]> withdrawals = FileHandler.readCSV("withdrawals.csv");
		for (String[] row : withdrawals) {
			if (row.length > 0 && row[0].startsWith("W")) {
				try {
					int id = Integer.parseInt(row[0].substring(1));
					if (id >= withdrawalCounter) {
						withdrawalCounter = id + 1;
					}
				} catch (NumberFormatException e) {
					// Skip invalid IDs
				}
			}
		}

		// Read reports and find max ID
		List<String[]> reports = FileHandler.readCSV("reports.csv");
		for (String[] row : reports) {
			if (row.length > 0 && row[0].startsWith("R")) {
				try {
					int id = Integer.parseInt(row[0].substring(1));
					if (id >= reportCounter) {
						reportCounter = id + 1;
					}
				} catch (NumberFormatException e) {
					// Skip invalid IDs (like "REP001")
				}
			}
		}

		initialized = true;
		System.out.println("[IDGenerator] Initialized with counters - I:" + internshipCounter +
		                   ", A:" + applicationCounter + ", W:" + withdrawalCounter + ", R:" + reportCounter);
	}

	/**
	 * Generates next internship ID in format Ixxx.
	 * @return new internship ID
	 */
	public static String nextInternshipID() {
		initialize();
		return "I" + (internshipCounter++);
	}

	/**
	 * Generates next application ID in format Axxx.
	 * @return new application ID
	 */
	public static String nextApplicationID() {
		initialize();
		return "A" + (applicationCounter++);
	}

	/**
	 * Generates next withdrawal ID in format Wxxx.
	 * @return new withdrawal ID
	 */
	public static String nextWithdrawalID() {
		initialize();
		return "W" + (withdrawalCounter++);
	}

	/**
	 * Generates next report ID in format Rxxx.
	 * @return new report ID
	 */
	public static String nextReportID() {
		initialize();
		return "R" + (reportCounter++);
	}
}
