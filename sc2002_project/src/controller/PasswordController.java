package controller;

import entities.User;
import enums.UserRole;
import services.AccountManager;

public class PasswordController {
	private AccountManager accountMgr;
	
    public PasswordController(AccountManager accountMgr) { this.accountMgr = accountMgr; }
    
    // forgot password
    public boolean changePassword(String id, String name, UserRole role, String newPW) { return accountMgr.changePassword(id, name, role, newPW); }
    
    // user logged in
    public boolean changePassword(User u, String oldPW, String newPW) { return accountMgr.changePassword(u, oldPW, newPW); }
}
