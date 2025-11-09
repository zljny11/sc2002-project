package controller;

import entities.Repository;
import services.AccountManager;
import services.ApplicationManager;
import services.InternshipManager;

/**
 * Main system controller that acts as a facade to all service managers.
 * Provides centralized access to account, internship, and application management.
 */
public class SystemController {
	private Repository repo;
	private AccountManager accountMgr;
	private InternshipManager internshipMgr;
	private ApplicationManager applicationMgr;

	public SystemController(Repository repo) {
		this.repo = repo;
		this.accountMgr = new AccountManager(repo);
		this.internshipMgr = new InternshipManager(repo);
		this.applicationMgr = new ApplicationManager(repo);
	}

	public AccountManager accounts() { return accountMgr; }
	public InternshipManager internships() { return internshipMgr; }
	public ApplicationManager applications() { return applicationMgr; }
	public Repository repository() { return repo; }
}
