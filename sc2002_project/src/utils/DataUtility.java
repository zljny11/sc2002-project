package utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DataUtility {
	public static String currentDate() { return LocalDate.now().format(DateTimeFormatter.ISO_DATE); }
}
