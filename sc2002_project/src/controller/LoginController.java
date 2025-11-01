package controller;

import entities.User;
import enums.UserRole;
import services.AccountManager;

public class LoginController {
	private AccountManager accountMgr;
	
    public LoginController(AccountManager accountMgr) { this.accountMgr = accountMgr; }
    
    public User login(String id, String pw, UserRole role) {
        return accountMgr.authenticate(id, pw, role);
    }
}