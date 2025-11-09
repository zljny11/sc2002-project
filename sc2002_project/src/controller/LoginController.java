package controller;

import entities.User;
import enums.UserRole;
import services.AccountManager;

/**
 * Handles user authentication for all user types.
 */
public class LoginController {
	private AccountManager accountMgr;
	
    public LoginController(AccountManager accountMgr) { this.accountMgr = accountMgr; }
    
    /**
     * Authenticates a user with provided credentials.
     * @param id user ID
     * @param pw password
     * @param role expected user role
     * @return User object if authentication succeeds, null otherwise
     */
    public User login(String id, String pw, UserRole role) {
        return accountMgr.authenticate(id, pw, role);
    }
}