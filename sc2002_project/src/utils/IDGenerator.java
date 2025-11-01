package utils;

public class IDGenerator {
	private static int internshipCounter = 1000;
	private static int applicationCounter = 2000;
	private static int withdrawalCounter = 3000;
	private static int reportCounter = 4000;

	public static String nextInternshipID() { return "I" + (internshipCounter++); }
	public static String nextApplicationID() { return "A" + (applicationCounter++); }
	public static String nextWithdrawalID() { return "W" + (withdrawalCounter++); }
	public static String nextReportID() { return "R" + (reportCounter++); }
}
