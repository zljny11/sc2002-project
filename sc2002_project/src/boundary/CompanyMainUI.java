package boundary;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

import controller.CompanyController;
import controller.SystemController;
import entities.Application;
import entities.CompanyRepresentative;
import entities.Internship;
import enums.InternshipLevel;

public class CompanyMainUI extends MainUI{
	private SystemController sys;
	private CompanyRepresentative companyRep;
	private CompanyController companyCtrl;

	public CompanyMainUI(SystemController sys, CompanyRepresentative companyRep) {
		this.sys = sys;
		this.companyRep = companyRep;
		this.companyCtrl = new CompanyController(sys);
	}


	private boolean isValidDateFormat(String date) {
		try {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			LocalDate.parse(date, formatter);
			return true;
		} catch (DateTimeParseException e) {
			return false;
		}
	}

	public void show() {
		Scanner sc = new Scanner(System.in);
		while (true) {
			System.out.println("\n======= Company Representative Menu =======");
			System.out.println("1 Create Internship");
			System.out.println("2 View My Internships");
			System.out.println("3 Modify Internship (PENDING only)");
			System.out.println("4 Remove Internship (PENDING only)");
			System.out.println("5 View Applicants for Internship");
			System.out.println("6 Approve/Reject Application");
			System.out.println("7 Toggle Internship Visibility");
			System.out.println("8 Change Password");
			System.out.println("9 Logout");
			System.out.print("Select: ");
			String s = sc.nextLine().trim();
			switch (s) {
			case "1":
                System.out.println("\n======= Create Internship =======");
                try {

                    String title = "";
                    while (title.isEmpty()) {
                        System.out.print("Title: ");
                        title = sc.nextLine().trim();
                        if (title.isEmpty()) {
                            System.out.println("Title cannot be empty.");
                        }
                    }


                    String desc = "";
                    while (desc.isEmpty()) {
                        System.out.print("Description: ");
                        desc = sc.nextLine().trim();
                        if (desc.isEmpty()) {
                            System.out.println("Description cannot be empty.");
                        }
                    }


                    InternshipLevel lvl = null;
                    while (lvl == null) {
                        System.out.print("Level (BASIC/INTERMEDIATE/ADVANCED): ");
                        String levelInput = sc.nextLine().trim().toUpperCase();
                        try {
                            lvl = InternshipLevel.valueOf(levelInput);
                        } catch (IllegalArgumentException e) {
                            System.out.println("Invalid level. Please enter BASIC, INTERMEDIATE, or ADVANCED.");
                        }
                    }


                    String preferredMajor = "";
                    while (preferredMajor.isEmpty()) {
                        System.out.print("Preferred Major: ");
                        preferredMajor = sc.nextLine().trim();
                        if (preferredMajor.isEmpty()) {
                            System.out.println("Preferred Major cannot be empty.");
                        }
                    }


                    String openDate = "";
                    while (openDate.isEmpty() || !isValidDateFormat(openDate)) {
                        System.out.print("Opening Date (YYYY-MM-DD): ");
                        openDate = sc.nextLine().trim();
                        if (openDate.isEmpty()) {
                            System.out.println("Opening Date cannot be empty.");
                        } else if (!isValidDateFormat(openDate)) {
                            System.out.println("Invalid date format. Please use YYYY-MM-DD format.");
                        }
                    }


                    String closeDate = "";
                    while (closeDate.isEmpty() || !isValidDateFormat(closeDate)) {
                        System.out.print("Closing Date (YYYY-MM-DD): ");
                        closeDate = sc.nextLine().trim();
                        if (closeDate.isEmpty()) {
                            System.out.println("Closing Date cannot be empty.");
                        } else if (!isValidDateFormat(closeDate)) {
                            System.out.println("Invalid date format. Please use YYYY-MM-DD format.");
                        }
                    }


                    int slots = -1;
                    while (slots < 0) {
                        try {
                            System.out.print("Slots: ");
                            slots = Integer.parseInt(sc.nextLine().trim());
                            if (slots < 0) {
                                System.out.println("Slots must be a positive number.");
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid number format. Please enter a valid integer.");
                        }
                    }

                    companyCtrl.createInternship(companyRep, title, desc, lvl, preferredMajor, openDate, closeDate, slots);
                    System.out.println("Internship created successfully!");
                } catch (Exception e) {
                    System.out.println("Error creating internship: " + e.getMessage());
                    System.out.println("Please try again.");
                }
                break;

			case "2":
				List<Internship> l = companyCtrl.getInternships(companyRep);
				if (l.isEmpty()) {
					System.out.println("No internships created");
				} else {
					System.out.println("\n======= Internships =======");
					for (Internship i2 : l) {
						// 获取该实习岗位的申请数量
						List<Application> applications = companyCtrl.getApplicants(i2.getInternshipID());
						int totalApplications = applications.size();
						long pendingApplications = applications.stream()
							.filter(a -> a.getStatus() == enums.ApplicationStatus.PENDING)
							.count();
						
						System.out.println(i2.getTitle());
						System.out.println("ID: " + i2.getInternshipID());
						System.out.println("Status: " + i2.getStatus());
						System.out.println("Visibility: " + i2.isVisible());
						System.out.println("Applications: " + totalApplications + " (Pending: " + pendingApplications + ")");
						System.out.println("---------------------------");
					}
					System.out.print("Press Enter to go back...");
					sc.nextLine();
				}
				break;
			case "3":
				System.out.println("\n======= Modify Internship =======");
				try {
					System.out.print("Internship ID to modify: ");
					String modifyID = sc.nextLine().trim();

					Internship toModify = sys.repository().findInternship(modifyID);
					if (toModify == null) {
						System.out.println("Error: Internship not found with ID: " + modifyID);
						break;
					}
					if (!toModify.getCompanyRepID().equals(companyRep.getID())) {
						System.out.println("Error: You can only modify your own internships");
						break;
					}

					System.out.println("\nCurrent details:");
					System.out.println("Title: " + toModify.getTitle());
					System.out.println("Description: " + toModify.getDescription());
					System.out.println("Preferred Major: " + toModify.getPreferredMajor());
					System.out.println("Opening Date: " + toModify.getOpeningDate());
					System.out.println("Closing Date: " + toModify.getClosingDate());
					System.out.println("Slots: " + toModify.getSlots());
					System.out.println("Status: " + toModify.getStatus());

					if (toModify.getStatus() != enums.InternshipStatus.PENDING) {
						System.out.println("\nError: Can only modify PENDING internships.");
						System.out.println("This internship has been " + toModify.getStatus() + " by Career Center Staff.");
						break;
					}

					System.out.println("\nEnter new details (press Enter to keep current value):");

					System.out.print("New Title [" + toModify.getTitle() + "]: ");
					String newTitle = sc.nextLine().trim();
					if (newTitle.isEmpty()) newTitle = toModify.getTitle();

					System.out.print("New Description [" + toModify.getDescription() + "]: ");
					String newDesc = sc.nextLine().trim();
					if (newDesc.isEmpty()) newDesc = toModify.getDescription();

					System.out.print("New Preferred Major [" + toModify.getPreferredMajor() + "]: ");
					String newMajor = sc.nextLine().trim();
					if (newMajor.isEmpty()) newMajor = toModify.getPreferredMajor();

					System.out.print("New Opening Date (YYYY-MM-DD) [" + toModify.getOpeningDate() + "]: ");
					String newOpenDate = sc.nextLine().trim();
					if (newOpenDate.isEmpty()) {
						newOpenDate = toModify.getOpeningDate();
					} else if (!isValidDateFormat(newOpenDate)) {
						System.out.println("Invalid date format. Keeping current opening date.");
						newOpenDate = toModify.getOpeningDate();
					}

					System.out.print("New Closing Date (YYYY-MM-DD) [" + toModify.getClosingDate() + "]: ");
					String newCloseDate = sc.nextLine().trim();
					if (newCloseDate.isEmpty()) {
						newCloseDate = toModify.getClosingDate();
					} else if (!isValidDateFormat(newCloseDate)) {
						System.out.println("Invalid date format. Keeping current closing date.");
						newCloseDate = toModify.getClosingDate();
					}

					System.out.print("New Slots [" + toModify.getSlots() + "]: ");
					String slotsInput = sc.nextLine().trim();
					int newSlots = toModify.getSlots();
					if (!slotsInput.isEmpty()) {
						try {
							newSlots = Integer.parseInt(slotsInput);
							if (newSlots <= 0) {
								System.out.println("Invalid slots number. Keeping current slots.");
								newSlots = toModify.getSlots();
							}
						} catch (NumberFormatException e) {
							System.out.println("Invalid number format. Keeping current slots.");
						}
					}

					companyCtrl.modifyInternship(companyRep, modifyID, newTitle, newDesc,
						newMajor, newOpenDate, newCloseDate, newSlots);
					System.out.println("\nInternship modified successfully!");

				} catch (Exception e) {
					System.out.println("Error modifying internship: " + e.getMessage());
				}
				break;
			case "4":
				System.out.println("\n======= Remove Internship =======");
				try {
					System.out.print("Internship ID to remove: ");
					String removeID = sc.nextLine().trim();

					Internship toRemove = sys.repository().findInternship(removeID);
					if (toRemove == null) {
						System.out.println("Error: Internship not found with ID: " + removeID);
						break;
					}
					if (!toRemove.getCompanyRepID().equals(companyRep.getID())) {
						System.out.println("Error: You can only remove your own internships");
						break;
					}

					System.out.println("\nInternship details:");
					System.out.println("Title: " + toRemove.getTitle());
					System.out.println("Status: " + toRemove.getStatus());

					if (toRemove.getStatus() != enums.InternshipStatus.PENDING) {
						System.out.println("\nError: Can only remove PENDING internships.");
						System.out.println("This internship has been " + toRemove.getStatus() + " by Career Center Staff.");
						break;
					}

					System.out.print("\nAre you sure you want to remove this internship? (yes/no): ");
					String confirm = sc.nextLine().trim().toLowerCase();

					if (confirm.equals("yes") || confirm.equals("y")) {
						companyCtrl.removeInternship(companyRep, removeID);
						System.out.println("Internship removed successfully!");
					} else {
						System.out.println("Operation cancelled.");
					}

				} catch (Exception e) {
					System.out.println("Error removing internship: " + e.getMessage());
				}
				break;
			case "5":
				// 获取公司代表的所有实习岗位
				List<Internship> companyInternships = companyCtrl.getInternships(companyRep);
				if (companyInternships.isEmpty()) {
					System.out.println("No internships found for your company");
					break;
				}
				
				boolean hasApplications = false;
				System.out.println("\n======= All Applicants for Your Internships =======");
				
				// 遍历每个实习岗位并显示申请人
				for (Internship internship : companyInternships) {
					List<Application> applications = companyCtrl.getApplicants(internship.getInternshipID());
					if (!applications.isEmpty()) {
						hasApplications = true;
						System.out.println("\n--- Applicants for '" + internship.getTitle() + "' (ID: " + internship.getInternshipID() + ") ---");
						for (Application app : applications) {
							System.out.println("Application ID: " + app.getApplicationID());
							System.out.println("Student ID:     " + app.getStudentID());
							System.out.println("Status:         " + app.getStatus());
							if (app.getStatus().toString().equals("SUCCESSFUL")) {
								System.out.println("Accepted:       " + (app.isAcceptedByStudent() ? "Yes" : "No"));
							}
							System.out.println("--------------------------");
						}
					}
				}
				
				if (!hasApplications) {
					System.out.println("No applicants found for any of your internships");
				} else {
					System.out.print("Press Enter to go back...");
					sc.nextLine();
				}
				break;
			case "6":
				System.out.println("\n======= Approve/Reject Application =======");
				System.out.print("Internship ID: ");
				String iID = sc.nextLine().trim();
				List<Application> l2 = companyCtrl.getApplicants(iID);
				if (l2.isEmpty()) {
					System.out.println("No applicants found for this internship");
				} else {
					System.out.println("\n======= Pending Applications =======");
					boolean hasPending = false;
					for (Application a : l2) {
						if (a.getStatus().toString().equals("PENDING")) {
							System.out.println("Application ID: " + a.getApplicationID());
							System.out.println("Student ID:     " + a.getStudentID());
							System.out.println("Internship ID:  " + a.getInternshipID());
							System.out.println("Status:         " + a.getStatus());
							System.out.println("--------------------------");
							hasPending = true;
						}
					}
					if (!hasPending) {
						System.out.println("No pending applications found");
					} else {
						System.out.print("\nApplication ID to process: ");
						String aID = sc.nextLine().trim();
						System.out.print("Approve? (yes/no): ");
						String decision = sc.nextLine().trim().toLowerCase();
						if (decision.equals("yes") || decision.equals("y")) {
							companyCtrl.decideApplication(aID, true);
							System.out.println("Application approved successfully");
						} else if (decision.equals("no") || decision.equals("n")) {
							companyCtrl.decideApplication(aID, false);
							System.out.println("Application rejected successfully");
						} else {
							System.out.println("Invalid input. Operation cancelled.");
						}
					}
				}
				break;
			case "7":
				System.out.println("\n======= Toggle Internship Visibility =======");
				System.out.print("Internship ID: ");
                iID = sc.nextLine().trim();
				Internship internship = sys.repository().findInternship(iID);
				if (internship == null) {
					System.out.println("Error: Internship not found with ID: " + iID);
				} else if (internship.isVisible()) {
					companyCtrl.setVisibility(companyRep, iID, false);
				} else {
					companyCtrl.setVisibility(companyRep, iID, true);
				}
				break;
			case "8":
				new ChangePasswordUI(sys).show(companyRep);
				break;
			case "9":
				if (new LogoutUI().confirm()) {
					return;
				}
				break;
			default:
				System.out.println("Invalid option. Try again.");
				break;
			}
		}
	}

}
