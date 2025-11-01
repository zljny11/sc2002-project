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

	public static List<String[]> readCSV(String path) {
		List<String[]> rows = new ArrayList<>();
		File f = new File(path);
		if (!f.exists()) {
			try { f.createNewFile(); } catch (IOException e) {}
			return rows;
		}
		try (BufferedReader br = new BufferedReader(new FileReader(f))) {
			String line;
			// assume no header
			while ((line = br.readLine()) != null) {
				if (line.trim().isEmpty()) { continue; }
				String[] parts = line.split(",", -1);
				rows.add(parts);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return rows;
	}

	public static void writeCSV(String path, List<String[]> rows) {
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(path))) {
			for (String[] row : rows) {
				bw.write(String.join(",", row));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
