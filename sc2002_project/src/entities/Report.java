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

	public static Report fromCSVRow(String[] row) {
		// keep in view
		String id = row[0];
		ReportCategory cat = ReportCategory.valueOf(row[1]);
		String genDate = row[2];
		String content = row[3].replace("\\n","\n");
		ReportSummary r = new ReportSummary(id, cat);
		r.genDate = genDate;
		r.content = content;
		return r;
	}
	
	public String getID() { return id; }
	public ReportCategory getCategory() { return category; }
	public String getGeneratedDate() { return genDate; }
	public String getContent() { return content; }
}
