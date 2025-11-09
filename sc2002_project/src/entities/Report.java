package entities;

import java.util.Map;

import enums.ReportCategory;
import utils.DataUtility;

public abstract class Report {
	protected String id;
	protected ReportCategory category;
	protected String genDate;
	protected String content;

	public Report(String id, ReportCategory category) {
		this.id = id;
		this.category = category;
		this.genDate = DataUtility.currentDate();
		this.content = "";
	}
	
	// keep in view
	public abstract void generate(Repository repo, Map<String, String> filters);

	public String[] toCSVRow() { return new String[] {id, category.name(), genDate, content.replace("\n","\\n")}; }

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

	private static boolean isValidField(String field) {
		return field != null && !field.trim().isEmpty();
	}

	public static Report fromCSVRow(String[] row) {
		// Check CSV row length
		if (row == null || row.length != 4) {
			throw new IllegalArgumentException("Invalid CSV row: expected 4 fields, got " + (row == null ? 0 : row.length));
		}

		try {
			String id = row[0];
			String categoryStr = row[1];
			String genDate = row[2];
			String content = row[3].replace("\\n","\n");

			// Validate required fields
			if (!isValidField(id)) {
				throw new IllegalArgumentException("Invalid report ID: cannot be empty");
			}
			if (!isValidField(genDate)) {
				throw new IllegalArgumentException("Invalid generated date: cannot be empty");
			}

			// Validate date format
			if (!isValidDate(genDate)) {
				throw new IllegalArgumentException("Invalid date format. Expected YYYY-MM-DD");
			}

			// Parse enum (this will throw if invalid)
			ReportCategory cat = ReportCategory.valueOf(categoryStr);

			ReportSummary r = new ReportSummary(id, cat);
			r.genDate = genDate;
			r.content = content;
			return r;
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("Invalid value in CSV row: " + e.getMessage());
		}
	}
	
	public String getID() { return id; }
	public ReportCategory getCategory() { return category; }
	public String getGeneratedDate() { return genDate; }
	public String getContent() { return content; }
}
