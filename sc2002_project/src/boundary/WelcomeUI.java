package boundary;

import java.util.Scanner;

import controller.SystemController;

public class WelcomeUI {
	private SystemController sys;
    public WelcomeUI(SystemController sys) { this.sys = sys; }

    public void start() {
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("\n======= Internship Placement Management System =======");
            System.out.println("1 Login");
            System.out.println("2 Register");
            System.out.println("3 Forgot Password");
            System.out.println("4 Exit");
            System.out.print("Select: ");
            
            String s = sc.next();
            switch(s) {
            case "1":
            	new LoginUI(sys).show();
            	break;
            case "2":
            	new RegistrationUI(sys).show();
            	break;
            case "3":
            	new ChangePasswordUI(sys).show();
            	break;
            case "4":
            	System.out.println("Exited");
            	System.exit(0);
            default:
            	System.out.println("Invalid option. Try again");
            	break;
            }
        }
    }
}
