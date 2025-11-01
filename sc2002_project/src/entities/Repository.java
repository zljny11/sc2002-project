package entities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import utils.FileHandler;

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

    public Repository() { loadAll(); }
    
    private void loadAll() {
    	List<String[]> l = FileHandler.readCSV(STUDENTS_FILE);
    	for (String[] s : l) {
    		Student s1 = Student.fromCSVRow(s);
    		students.put(s1.getID(), s1);
    	}
    	l = FileHandler.readCSV(COMPANYREPS_FILE);
    	for (String[] s : l) {
    		CompanyRepresentative c = CompanyRepresentative.fromCSVRow(s);
    		companyReps.put(c.getID(), c);
    	}
    	l = FileHandler.readCSV(STAFFMEMBERS_FILE);
    	for (String[] s: l) {
    		Staff s1 = Staff.fromCSVRow(s);
    		staffMembers.put(s1.getID(), s1);
    	}
    	l = FileHandler.readCSV(INTERNSHIPS_FILE);
    	for (String[] s: l) {
    		Internship i = Internship.fromCSVRow(s);
    		internships.put(i.getInternshipID(), i);
    	}
    	l = FileHandler.readCSV(APPLICATIONS_FILE);
    	for (String[] s: l) {
    		Application a = Application.fromCSVRow(s);
    		applications.put(a.getApplicationID(), a);
    	}
    	l = FileHandler.readCSV(WITHDRAWALS_FILE);
    	for (String[] s: l) {
    		WithdrawalRequest w = WithdrawalRequest.fromCSVRow(s);
    		withdrawals.put(w.getRequestID(), w);
    	}
    	l = FileHandler.readCSV(REPORTS_FILE);
    	for (String[] s: l) {
    		Report r = Report.fromCSVRow(s);
    		reports.put(r.getID(), r);
    	} 
    }
    
    public void saveStudents() {
        List<String[]> l = new ArrayList<>();
        for (Student s : students.values()) { l.add(s.toCSVRow()); }
        FileHandler.writeCSV(STUDENTS_FILE, l);
    }
    public void saveCompanyReps() {
        List<String[]> l = new ArrayList<>();
        for (CompanyRepresentative c : companyReps.values()) { l.add(c.toCSVRow()); }
        FileHandler.writeCSV(COMPANYREPS_FILE, l);
    }
    public void saveStaffMembers() {
        List<String[]> l = new ArrayList<>();
        for (Staff s : staffMembers.values()) { l.add(s.toCSVRow()); }
        FileHandler.writeCSV(STAFFMEMBERS_FILE, l);
    }
    public void saveInternships() {
        List<String[]> l = new ArrayList<>();
        for (Internship i : internships.values()) { l.add(i.toCSVRow()); }
        FileHandler.writeCSV(INTERNSHIPS_FILE, l);
    }
    public void saveApplications() {
        List<String[]> l = new ArrayList<>();
        for (Application a : applications.values()) { l.add(a.toCSVRow()); }
        FileHandler.writeCSV(APPLICATIONS_FILE, l);
    }
    public void saveWithdrawals() {
        List<String[]> l = new ArrayList<>();
        for (WithdrawalRequest w : withdrawals.values()) { l.add(w.toCSVRow()); }
        FileHandler.writeCSV(WITHDRAWALS_FILE, l);
    }
    public void saveReports() {
        List<String[]> l = new ArrayList<>();
        for (Report r : reports.values()) { l.add(r.toCSVRow()); }
        FileHandler.writeCSV(REPORTS_FILE, l);
    }
    
    public void updateStudent(Student s) {
    	students.put(s.getID(), s);
    	saveStudents();
    }
    public void updateCompanyRep(CompanyRepresentative c) {
    	companyReps.put(c.getID(), c);
    	saveCompanyReps();
    }
    public void updateStaff(Staff s) {
    	staffMembers.put(s.getID(), s);
    	saveStaffMembers();
    }
    public void updateInternship(Internship i) {
    	internships.put(i.getInternshipID(), i);
    	saveInternships();
    }
    public void updateApplication(Application a) {
    	applications.put(a.getApplicationID(), a);
    	saveApplications();
    }
    public void updateWithdrawal(WithdrawalRequest w) {
    	withdrawals.put(w.getRequestID(), w);
    	saveWithdrawals();
    }
    public void updateReport(Report r) {
    	reports.put(r.getID(), r);
    	saveReports();
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
        return l;
    }
}
