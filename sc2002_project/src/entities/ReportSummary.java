package entities;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import enums.ReportCategory;
import enums.InternshipStatus;
import enums.ApplicationStatus;
import enums.ApprovalStatus;
import enums.InternshipLevel;

// keep in view
public class ReportSummary extends Report{
	public ReportSummary(String id, ReportCategory category) { super(id, category); }

    @Override
    public void generate(Repository repo, Map<String, String> filters) {
        // get filtered data
        List<Internship> filteredInternships = filterInternships(repo.getAllInternships(), filters);
        List<Application> filteredApplications = filterApplications(repo.getAllApplications(), filters);
        List<WithdrawalRequest> filteredWithdrawals = filterWithdrawals(repo.getAllWithdrawals(), filters);

        // construct the report
        StringBuilder sb = new StringBuilder();

        // condition demonstration
        if (filters != null && !filters.isEmpty()) {
            sb.append("=== Filter Criteria ===\n");
            appendFilterInfo(sb, filters);
            sb.append("\n");
        }

        // basic statistics
        sb.append("=== Summary Statistics ===\n");
        sb.append("Total Internships: ").append(filteredInternships.size()).append("\n");
        sb.append("Total Applications: ").append(filteredApplications.size()).append("\n");
        sb.append("Total Withdrawals: ").append(filteredWithdrawals.size()).append("\n");

        // classify and return result
        if (!filteredInternships.isEmpty()) {
            sb.append("\n=== Internship Breakdown ===\n");
            appendInternshipBreakdown(sb, filteredInternships);
        }

        if (!filteredApplications.isEmpty()) {
            sb.append("\n=== Application Breakdown ===\n");
            appendApplicationBreakdown(sb, filteredApplications);
        }

        if (!filteredWithdrawals.isEmpty()) {
            sb.append("\n=== Withdrawal Breakdown ===\n");
            appendWithdrawalBreakdown(sb, filteredWithdrawals);
        }

        this.content = sb.toString();
    }

    /**
     * - status:  (PENDING, APPROVED, REJECTED, FILLED)
     * - level: (BASIC, INTERMEDIATE, ADVANCED)
     * - companyName
     * - startDate: (openingDate >= startDate)
     * - endDate: (closingDate <= endDate)
     * - visible: (true/false)
     */
    private List<Internship> filterInternships(List<Internship> internships, Map<String, String> filters) {
        if (filters == null || filters.isEmpty()) {
            return internships;
        }

        return internships.stream()
            .filter(i -> matchesFilter(filters, "status", i.getStatus().name()))
            .filter(i -> matchesFilter(filters, "level", i.getLevel().name()))
            .filter(i -> matchesFilterIgnoreCase(filters, "companyName", i.getCompanyName()))
            .filter(i -> matchesFilter(filters, "preferredMajor", i.getPreferredMajor()))
            .filter(i -> matchesDateRange(filters, i.getOpeningDate(), i.getClosingDate()))
            .filter(i -> matchesBooleanFilter(filters, "visible", i.isVisible()))
            .collect(Collectors.toList());
    }

    /**
     * - status: (PENDING, SUCCESSFUL, UNSUCCESSFUL, WITHDRAWN)
     * - studentID
     * - internshipID
     * - startDate
     * - endDate
     * - acceptedByStudent: (true/false)
     */
    private List<Application> filterApplications(List<Application> applications, Map<String, String> filters) {
        if (filters == null || filters.isEmpty()) {
            return applications;
        }

        return applications.stream()
            .filter(a -> matchesFilter(filters, "status", a.getStatus().name()))
            .filter(a -> matchesFilter(filters, "studentID", a.getStudentID()))
            .filter(a -> matchesFilter(filters, "internshipID", a.getInternshipID()))
            .filter(a -> matchesDateFilter(filters, a.getApplyDate()))
            .filter(a -> matchesBooleanFilter(filters, "acceptedByStudent", a.isAcceptedByStudent()))
            .collect(Collectors.toList());
    }

    /**
     * - status:  (PENDING, APPROVED, REJECTED)
     * - studentID
     * - applicationID
     * - startDate
     * - endDate
     */
    private List<WithdrawalRequest> filterWithdrawals(List<WithdrawalRequest> withdrawals, Map<String, String> filters) {
        if (filters == null || filters.isEmpty()) {
            return withdrawals;
        }

        return withdrawals.stream()
            .filter(w -> matchesFilter(filters, "status", w.getStatus().name()))
            .filter(w -> matchesFilter(filters, "studentID", w.getStudentID()))
            .filter(w -> matchesFilter(filters, "applicationID", w.getApplicationID()))
            .filter(w -> matchesDateFilter(filters, w.getRequestDate()))
            .collect(Collectors.toList());
    }

    // ==================== assistance method ====================


    private boolean matchesFilter(Map<String, String> filters, String key, String value) {
        if (!filters.containsKey(key)) return true;
        String filterValue = filters.get(key);
        if (filterValue == null || filterValue.isEmpty()) return true;
        return filterValue.equals(value);
    }

    private boolean matchesFilterIgnoreCase(Map<String, String> filters, String key, String value) {
        if (!filters.containsKey(key)) return true;
        String filterValue = filters.get(key);
        if (filterValue == null || filterValue.isEmpty()) return true;
        return value != null && value.toLowerCase().contains(filterValue.toLowerCase());
    }

    private boolean matchesBooleanFilter(Map<String, String> filters, String key, boolean value) {
        if (!filters.containsKey(key)) return true;
        String filterValue = filters.get(key);
        if (filterValue == null || filterValue.isEmpty()) return true;
        return Boolean.parseBoolean(filterValue) == value;
    }

    private boolean matchesDateFilter(Map<String, String> filters, String date) {
        String startDate = filters.get("startDate");
        String endDate = filters.get("endDate");

        if (startDate != null && !startDate.isEmpty()) {
            if (date.compareTo(startDate) < 0) return false;
        }
        if (endDate != null && !endDate.isEmpty()) {
            if (date.compareTo(endDate) > 0) return false;
        }
        return true;
    }

    private boolean matchesDateRange(Map<String, String> filters, String openDate, String closeDate) {
        String startDate = filters.get("startDate");
        String endDate = filters.get("endDate");

        if (startDate != null && !startDate.isEmpty()) {
            if (closeDate.compareTo(startDate) < 0) return false;
        }
        if (endDate != null && !endDate.isEmpty()) {
            if (openDate.compareTo(endDate) > 0) return false;
        }
        return true;
    }

    private void appendFilterInfo(StringBuilder sb, Map<String, String> filters) {
        filters.forEach((key, value) -> {
            if (value != null && !value.isEmpty()) {
                sb.append("  ").append(key).append(": ").append(value).append("\n");
            }
        });
    }

    private void appendInternshipBreakdown(StringBuilder sb, List<Internship> internships) {
        // by status
        long pending = internships.stream().filter(i -> i.getStatus().name().equals("PENDING")).count();
        long approved = internships.stream().filter(i -> i.getStatus().name().equals("APPROVED")).count();
        long rejected = internships.stream().filter(i -> i.getStatus().name().equals("REJECTED")).count();
        long filled = internships.stream().filter(i -> i.getStatus().name().equals("FILLED")).count();

        sb.append("  By Status:\n");
        sb.append("    - PENDING: ").append(pending).append("\n");
        sb.append("    - APPROVED: ").append(approved).append("\n");
        sb.append("    - REJECTED: ").append(rejected).append("\n");
        sb.append("    - FILLED: ").append(filled).append("\n");

        // by level
        long basic = internships.stream().filter(i -> i.getLevel().name().equals("BASIC")).count();
        long intermediate = internships.stream().filter(i -> i.getLevel().name().equals("INTERMEDIATE")).count();
        long advanced = internships.stream().filter(i -> i.getLevel().name().equals("ADVANCED")).count();

        sb.append("  By Level:\n");
        sb.append("    - BASIC: ").append(basic).append("\n");
        sb.append("    - INTERMEDIATE: ").append(intermediate).append("\n");
        sb.append("    - ADVANCED: ").append(advanced).append("\n");

        // by visibility
        long visible = internships.stream().filter(Internship::isVisible).count();
        long hidden = internships.size() - visible;
        sb.append("  By Visibility:\n");
        sb.append("    - Visible: ").append(visible).append("\n");
        sb.append("    - Hidden: ").append(hidden).append("\n");
    }

    private void appendApplicationBreakdown(StringBuilder sb, List<Application> applications) {
        // by status
        long pending = applications.stream().filter(a -> a.getStatus().name().equals("PENDING")).count();
        long successful = applications.stream().filter(a -> a.getStatus().name().equals("SUCCESSFUL")).count();
        long unsuccessful = applications.stream().filter(a -> a.getStatus().name().equals("UNSUCCESSFUL")).count();
        long withdrawn = applications.stream().filter(a -> a.getStatus().name().equals("WITHDRAWN")).count();

        sb.append("  By Status:\n");
        sb.append("    - PENDING: ").append(pending).append("\n");
        sb.append("    - SUCCESSFUL: ").append(successful).append("\n");
        sb.append("    - UNSUCCESSFUL: ").append(unsuccessful).append("\n");
        sb.append("    - WITHDRAWN: ").append(withdrawn).append("\n");

        // by acceptance
        long accepted = applications.stream().filter(Application::isAcceptedByStudent).count();
        long notAccepted = applications.size() - accepted;
        sb.append("  By Student Acceptance:\n");
        sb.append("    - Accepted: ").append(accepted).append("\n");
        sb.append("    - Not Accepted: ").append(notAccepted).append("\n");
    }


    private void appendWithdrawalBreakdown(StringBuilder sb, List<WithdrawalRequest> withdrawals) {
        // by status
        long pending = withdrawals.stream().filter(w -> w.getStatus().name().equals("PENDING")).count();
        long approved = withdrawals.stream().filter(w -> w.getStatus().name().equals("APPROVED")).count();
        long rejected = withdrawals.stream().filter(w -> w.getStatus().name().equals("REJECTED")).count();

        sb.append("  By Status:\n");
        sb.append("    - PENDING: ").append(pending).append("\n");
        sb.append("    - APPROVED: ").append(approved).append("\n");
        sb.append("    - REJECTED: ").append(rejected).append("\n");
    }
}