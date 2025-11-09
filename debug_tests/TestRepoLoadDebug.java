import java.util.List;
import utils.FileHandler;

public class TestRepoLoadDebug {
    public static void main(String[] args) {
        System.out.println("Reading reports.csv...");
        List<String[]> rows = FileHandler.readCSV("reports.csv");
        System.out.println("Total rows read: " + rows.size());
        for (int i = 0; i < Math.min(3, rows.size()); i++) {
            String[] row = rows.get(i);
            System.out.println("Row " + i + " has " + row.length + " fields:");
            for (int j = 0; j < row.length; j++) {
                System.out.println("  Field " + j + ": " + (row[j].length() > 50 ? row[j].substring(0, 50) + "..." : row[j]));
            }
        }
    }
}
