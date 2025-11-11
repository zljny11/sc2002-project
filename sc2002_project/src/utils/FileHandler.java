package utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileHandler {

	private static final String DATA_DIR = "data";

	/**
	 * Finds the project root directory by looking for .idea or .git folder
	 */
	private static File findProjectRoot() {
		File current = new File(System.getProperty("user.dir"));

		// Try to find project root by looking for .idea or .git
		while (current != null) {
			File ideaDir = new File(current, ".idea");
			File gitDir = new File(current, ".git");
			File dataDir = new File(current, DATA_DIR);

			if (ideaDir.exists() || gitDir.exists() || dataDir.exists()) {
				return current;
			}
			current = current.getParentFile();
		}

		// Fallback to current directory
		return new File(System.getProperty("user.dir"));
	}

	/**
	 * Gets the absolute path to a CSV file in the data directory
	 */
	private static File getDataFile(String filename) {
		File projectRoot = findProjectRoot();
		File dataDir = new File(projectRoot, DATA_DIR);
		return new File(dataDir, filename);
	}

	public static List<String[]> readCSV(String path) {
		List<String[]> rows = new ArrayList<>();
		File f = getDataFile(path);
		if (!f.exists()) {
			System.err.println("[ERROR] File does not exist: " + f.getAbsolutePath());
			// Create parent directory if it doesn't exist
			f.getParentFile().mkdirs();
			try { f.createNewFile(); } catch (IOException e) {}
			return rows;
		}
		try (BufferedReader br = new BufferedReader(new FileReader(f))) {
			String line;
			// skip header row
			boolean firstLine = true;
			
			while ((line = br.readLine()) != null) {
				if (line.trim().isEmpty()) { continue; }
				if (firstLine) {
					firstLine = false;
					continue; // Skip header
				}
				
				String[] parts = parseCSVLine(line);
				rows.add(parts);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return rows;
	}
	
	// Simple CSV parser that handles quoted fields
	private static String[] parseCSVLine(String line) {
		List<String> fields = new ArrayList<>();
		boolean inQuotes = false;
		StringBuilder currentField = new StringBuilder();
		
		for (int i = 0; i < line.length(); i++) {
			char c = line.charAt(i);
			
			if (c == '"') {
				// Check for escaped quote
				if (i + 1 < line.length() && line.charAt(i + 1) == '"') {
					currentField.append('"');
					i++; // Skip next quote
				} else {
					// Toggle quote state
					inQuotes = !inQuotes;
				}
			} else if (c == ',' && !inQuotes) {
				// End of field
				fields.add(currentField.toString());
				currentField = new StringBuilder();
			} else {
				currentField.append(c);
			}
		}
		
		// Add the last field
		fields.add(currentField.toString());
		
		// Post-process fields to handle escaped newlines
		for (int i = 0; i < fields.size(); i++) {
			String field = fields.get(i);
			field = field.replace("\\n", "\n");
			fields.set(i, field);
		}
		
		return fields.toArray(new String[0]);
	}

	public static void writeCSV(String path, List<String[]> rows) {
		writeCSV(path, rows, null);
	}

	public static void writeCSV(String path, List<String[]> rows, String[] header) {
		File f = getDataFile(path);
		// Create parent directory if it doesn't exist
		f.getParentFile().mkdirs();
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(f))) {
			// Write header if provided
			if (header != null) {
				bw.write(String.join(",", header));
				bw.newLine();
			}

			for (String[] row : rows) {
				// Process each field to escape newlines and wrap in quotes if needed
				String[] processedRow = new String[row.length];
				for (int i = 0; i < row.length; i++) {
					String field = row[i];
					if (field != null) {
						// Escape newlines
						field = field.replace("\n", "\\n");
						// Wrap in quotes if field contains comma, newline or quote
						if (field.contains(",") || field.contains("\\n") || field.contains("\"")) {
							field = "\"" + field.replace("\"", "\"\"") + "\"";
						}
					}
					processedRow[i] = field;
				}
				bw.write(String.join(",", processedRow));
				bw.newLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}