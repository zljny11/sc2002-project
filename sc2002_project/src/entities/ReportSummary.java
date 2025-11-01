package entities;

import java.util.Map;

import enums.ReportCategory;

// keep in view
public class ReportSummary extends Report{
	public ReportSummary(String id, ReportCategory category) { super(id, category); }

    @Override
    public void generate(Repository repo, Map<String, String> filters) {
        int internships = repo.getAllInternships().size();
        int applications = repo.getAllApplications().size();
        int withdrawals = repo.getAllWithdrawals().size();
        StringBuilder sb = new StringBuilder();
        sb.append("Internships: ").append(internships).append("\n");
        sb.append("Applications: ").append(applications).append("\n");
        sb.append("Withdrawals: ").append(withdrawals).append("\n");
        this.content = sb.toString();
    }
}
