package entities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import utils.FileHandler;

/**
 * Central data repository for the internship management system.
 * Handles loading/saving all entity data from CSV files and provides
 * access methods for CRUD operations.
 *
 * All data is stored in memory using HashMaps for quick lookup.
 * Changes are persisted to CSV files immediately when update methods are called.
 */
public class Repository {
    private static final String STUDENTS_FILE = "students.csv";
    private static final String COMPANYREPS_FILE = "companyreps.csv";
    private static final String STAFFMEMBERS_FILE = "staffmembers.csv";
    private static final String INTERNSHIPS_FILE = "internships.csv";
    private static final String APPLICATIONS_FILE = "applications.csv";
    private static final String WITHDRAWALS_FILE = "withdrawals.csv";
    private static final String REPORTS_FILE = "reports.csv";

    private Map<String, Student> students = new HashMap<>();
    private Map<String, CompanyRepresentative> companyReps = new HashMap<>();
    private Map<String, Staff> staffMembers = new HashMap<>();
    private Map<String, Internship> internships = new HashMap<>();
    private Map<String, Application> applications = new HashMap<>();
    private Map<String, WithdrawalRequest> withdrawals = new HashMap<>();
    private Map<String, Report> reports = new HashMap<>();

    /**
     * Creates a new repository instance and loads all data from CSV files.
     */
    public Repository() { loadAll(); }

    /**
     * Loads all entity data from CSV files into memory.
     * Invalid entries are skipped and logged to stderr.
     */
    private void loadAll() {
    	List<String[]> l = FileHandler.readCSV(STUDENTS_FILE);
    	for (String[] s : l) {
    		try {
    			Student s1 = Student.fromCSVRow(s);
    			students.put(s1.getID(), s1);
    		} catch (IllegalArgumentException e) {
    			// Skip invalid entries
    			System.err.println("Skipping invalid student entry: " + e.getMessage());
    		}
    	}
    	l = FileHandler.readCSV(COMPANYREPS_FILE);
    	for (String[] s : l) {
    		try {
    			CompanyRepresentative c = CompanyRepresentative.fromCSVRow(s);
    			companyReps.put(c.getID(), c);
    		} catch (IllegalArgumentException e) {
    			// Skip invalid entries
    			System.err.println("Skipping invalid company rep entry: " + e.getMessage());
    		}
    	}
    	l = FileHandler.readCSV(STAFFMEMBERS_FILE);
    	for (String[] s: l) {
    		try {
    			Staff s1 = Staff.fromCSVRow(s);
    			staffMembers.put(s1.getID(), s1);
    		} catch (IllegalArgumentException e) {
    			// Skip invalid entries
    			System.err.println("Skipping invalid staff entry: " + e.getMessage());
    		}
    	}
    	l = FileHandler.readCSV(INTERNSHIPS_FILE);
    	for (String[] s: l) {
    		try {
    			Internship i = Internship.fromCSVRow(s);
    			internships.put(i.getInternshipID(), i);
    		} catch (IllegalArgumentException e) {
    			// Skip invalid entries
    			System.err.println("Skipping invalid internship entry: " + e.getMessage());
    		}
    	}
    	l = FileHandler.readCSV(APPLICATIONS_FILE);
    	for (String[] s: l) {
    		try {
    			Application a = Application.fromCSVRow(s);
    			applications.put(a.getApplicationID(), a);
    		} catch (IllegalArgumentException e) {
    			// Skip invalid entries
    			System.err.println("Skipping invalid application entry: " + e.getMessage());
    		}
    	}
    	l = FileHandler.readCSV(WITHDRAWALS_FILE);
    	for (String[] s: l) {
    		try {
    			WithdrawalRequest w = WithdrawalRequest.fromCSVRow(s);
    			withdrawals.put(w.getRequestID(), w);
    		} catch (IllegalArgumentException e) {
    			// Skip invalid entries
    			System.err.println("Skipping invalid withdrawal entry: " + e.getMessage());
    		}
    	}
    	l = FileHandler.readCSV(REPORTS_FILE);
    	for (String[] s: l) {
    		try {
    			Report r = Report.fromCSVRow(s);
    			reports.put(r.getID(), r);
    		} catch (IllegalArgumentException e) {
    			// Skip invalid entries
    			System.err.println("Skipping invalid report entry: " + e.getMessage());
    		}
    	}
    }
    
    public void saveStudents() {
        List<String[]> l = new ArrayList<>();
        for (Student s : students.values()) { l.add(s.toCSVRow()); }
        String[] header = {"id", "name", "pw", "email", "year", "major"};
        FileHandler.writeCSV(STUDENTS_FILE, l, header);
    }
    public void saveCompanyReps() {
        List<String[]> l = new ArrayList<>();
        for (CompanyRepresentative c : companyReps.values()) { l.add(c.toCSVRow()); }
        String[] header = {"id", "name", "pw", "companyName", "dept", "pos", "approved"};
        FileHandler.writeCSV(COMPANYREPS_FILE, l, header);
    }
    public void saveStaffMembers() {
        List<String[]> l = new ArrayList<>();
        for (Staff s : staffMembers.values()) { l.add(s.toCSVRow()); }
        String[] header = {"id", "name", "pw", "dept"};
        FileHandler.writeCSV(STAFFMEMBERS_FILE, l, header);
    }
    public void saveInternships() {
        List<String[]> l = new ArrayList<>();
        for (Internship i : internships.values()) { l.add(i.toCSVRow()); }
        String[] header = {"id", "title", "description", "level", "preferredMajor", "openingDate", "closingDate", "status", "companyName", "repID", "slots", "visible"};
        FileHandler.writeCSV(INTERNSHIPS_FILE, l, header);
    }
    public void saveApplications() {
        List<String[]> l = new ArrayList<>();
        for (Application a : applications.values()) { l.add(a.toCSVRow()); }
        String[] header = {"aID", "iID", "sID", "status", "applyDate", "acceptedByStudent"};
        FileHandler.writeCSV(APPLICATIONS_FILE, l, header);
    }
    public void saveWithdrawals() {
        List<String[]> l = new ArrayList<>();
        for (WithdrawalRequest w : withdrawals.values()) { l.add(w.toCSVRow()); }
        String[] header = {"wID", "aID", "sID", "status", "reqDate"};
        FileHandler.writeCSV(WITHDRAWALS_FILE, l, header);
    }
    public void saveReports() {
        List<String[]> l = new ArrayList<>();
        for (Report r : reports.values()) { l.add(r.toCSVRow()); }
        String[] header = {"id", "category", "genDate", "content"};
        FileHandler.writeCSV(REPORTS_FILE, l, header);
    }
    
    /**
     * Updates or adds a student record and persists to CSV.
     * @param s the student to update
     */
    public void updateStudent(Student s) {
    	students.put(s.getID(), s);
    	saveStudents();
    }

    /**
     * Updates or adds a company rep record and persists to CSV.
     * @param c the company representative to update
     */
    public void updateCompanyRep(CompanyRepresentative c) {
    	companyReps.put(c.getID(), c);
    	saveCompanyReps();
    }

    /**
     * Updates or adds a staff member record and persists to CSV.
     * @param s the staff member to update
     */
    public void updateStaff(Staff s) {
    	staffMembers.put(s.getID(), s);
    	saveStaffMembers();
    }

    /**
     * Updates or adds an internship record and persists to CSV.
     * @param i the internship to update
     */
    public void updateInternship(Internship i) {
    	internships.put(i.getInternshipID(), i);
    	saveInternships();
    }

    /**
     * Updates or adds an application record and persists to CSV.
     * @param a the application to update
     */
    public void updateApplication(Application a) {
    	applications.put(a.getApplicationID(), a);
    	saveApplications();
    }

    /**
     * Updates or adds a withdrawal request and persists to CSV.
     * @param w the withdrawal request to update
     */
    public void updateWithdrawal(WithdrawalRequest w) {
    	withdrawals.put(w.getRequestID(), w);
    	saveWithdrawals();
    }

    /**
     * Updates or adds a report and persists to CSV.
     * @param r the report to update
     */
    public void updateReport(Report r) {
    	reports.put(r.getID(), r);
    	saveReports();
    }

    public void removeInternship(String iID) {
    	internships.remove(iID);
    	saveInternships();
    }

    public Student findStudent(String id) { return students.get(id); }
    public CompanyRepresentative findCompanyRep(String id) { return companyReps.get(id); }
    public Staff findStaff(String id) { return staffMembers.get(id); }
    public Internship findInternship(String id) { return internships.get(id); }
    public Application findApplication(String id) { return applications.get(id); }
    public WithdrawalRequest findWithdrawal(String id) { return withdrawals.get(id); }
    public Report findReport(String id) { return reports.get(id); }
    
    
    public List<Student> getAllStudents() {
        List<Student> l = new ArrayList<>();
        for (Student s : students.values()) { l.add(s); }
        return l;
    }
    public List<CompanyRepresentative> getAllCompanyReps() {
        List<CompanyRepresentative> l = new ArrayList<>();
        for (CompanyRepresentative c : companyReps.values()) { l.add(c); }
        return l;
    }
    public List<Staff> getAllStaffMembers() {
        List<Staff> l = new ArrayList<>();
        for (Staff s : staffMembers.values()) { l.add(s); }
        return l;
    }
    public List<Internship> getAllInternships() {
        List<Internship> l = new ArrayList<>();
        for (Internship i : internships.values()) { l.add(i); }
        return l;
    }
    public List<Application> getAllApplications() {
        List<Application> l = new ArrayList<>();
        for (Application a : applications.values()) { l.add(a); }
        return l;
    }
    public List<Application> getApplicationsByStudent(String sID) {
        List<Application> l = applications.values().stream().filter(a -> a.getStudentID().equals(sID)).collect(Collectors.toList());
        return l;
    }
    public List<Application> getApplicationsByInternship(String iID) {
        List<Application> l = applications.values().stream().filter(a -> a.getInternshipID().equals(iID)).collect(Collectors.toList());
        return l;
    }
    public List<WithdrawalRequest> getAllWithdrawals() {
        List<WithdrawalRequest> l = new ArrayList<>();
        for (WithdrawalRequest w : withdrawals.values()) { l.add(w); }
        return l;
    }
    public List<Report> getAllReports() {
        List<Report> l = new ArrayList<>();
        for (Report r : reports.values()) { l.add(r); }
        // Sort reports by ID to ensure consistent order
        l.sort((r1, r2) -> r1.getID().compareTo(r2.getID()));
        return l;
    }

}
