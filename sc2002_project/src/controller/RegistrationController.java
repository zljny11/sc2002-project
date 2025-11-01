package controller;

import services.AccountManager;

public class RegistrationController {
	private AccountManager accountMgr;
	
    public RegistrationController(AccountManager accountMgr) { this.accountMgr = accountMgr; }

    public boolean registerCompanyRep(String id, String name, String pw, String companyName, String dept, String pos) {
        return accountMgr.registerCompanyRep(id, name, pw, companyName, dept, pos);
    }
}