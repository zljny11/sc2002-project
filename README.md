# SC2002 Object-Oriented Programming Project

## Internship Placement Management System

[![Java](https://img.shields.io/badge/Java-orange)](https://github.com/topics/java)
[![Git](https://img.shields.io/badge/Git-red)](https://github.com/topics/git)
[![UML Class Diagram](https://img.shields.io/badge/UML%20Class%20Diagram-blue)](https://github.com/topics/uml-class-diagram)
[![SOLID Design Principles](https://img.shields.io/badge/SOLID%20Design%20Principles-red)](https://github.com/topics/solid-design-principles)
[![OOP Concepts](https://img.shields.io/badge/OOP%20Concepts-red)](https://github.com/topics/oop-concepts)
[![Shell](https://img.shields.io/badge/Shell-green)](https://github.com/topics/shell)

**Academic Year:** 2025 Semester 1

**Course Code:** SC2002

**Course Instructors:** Professor Zhang Jie, Professor Li Fang

**Institution:** Nanyang Technological University

**Group:** SMAC Group 5

### Project Team

| Name | School |
|------|--------|
| CHONG CHENG YU | CCDS |
| HEER JIE MIN, RACHEL | SPMS |
| LIM GECK BOEY CISLYN | SPMS |
| MA SHUNLIANG | SPMS |
| ZHEN BINWEI | SPMS |



---

## Table of Contents

1. [Introduction](#introduction)
2. [System Features](#system-features)
3. [Project Setup](#project-setup)
   - [Prerequisites](#prerequisites)
   - [Cloning the Repository](#cloning-the-repository)
   - [Running the Application](#running-the-application)
4. [Testing and Data Management](#testing-and-data-management)
   - [Running Tests Safely](#running-tests-safely)
   - [Restoring Environment Only](#restoring-environment-only)
5. [Project Structure](#project-structure)
6. [Documentation](#documentation)
7. [Data Files](#data-files)
8. [Authentication](#authentication)
9. [Test Cases](#test-cases)

---

## Introduction

The Internship Placement Management System is a Java-based application designed to facilitate the management of internship opportunities within an educational institution. This system serves as a centralized platform connecting three distinct user groups: students seeking internship positions, company representatives offering internship opportunities, and career center staff members who oversee and approve the internship process.

---

## System Features

### Student Functions
Students can access the following features after logging into the system:
- Browse available internship opportunities filtered by eligibility criteria
- Submit applications for internships of interest (max: 3)
- Track application status in real-time
- Withdraw applications when necessary
- Accept internship offers once approved
- Update account password for security

### Company Representative Functions
Company representatives have access to tools for managing their internship postings:
- Create new internship opportunities with detailed information (max: 5)
- View all internships posted by their company
- Modify pending internships before approval
- Remove pending internships that are no longer available
- Review applications submitted by students
- Approve or reject student applications
- Toggle internship visibility to control applicant flow
- Update account password

### Career Center Staff Functions
Staff members perform administrative and oversight functions:
- Review and approve company representative registrations
- Approve or reject internship postings before they become visible to students
- Process withdrawal requests from students
- Generate comprehensive reports on system activity
- Manage account credentials

---

## Project Setup

### Prerequisites

Before setting up the project, ensure you have the following installed on your system:
- Java Development Kit (JDK) 11 or higher
- Git for version control
- A Unix-like environment (Linux, macOS, or Windows Subsystem for Linux) for running shell scripts

### Cloning the Repository

To clone the repository to your local machine, open a terminal and execute:

```bash
git clone <repository-url>
cd sc2002-project
```



### Running the Application

1. Compile the source files:
```bash
javac -d bin sc2002_project/src/**/*.java sc2002_project/src/**/**/*.java
```

2. Run the application:
```bash
java -cp bin main.Main
```

The system will then display a welcome screen in the command line.

---

## Testing and Data Management

### Running Tests Safely

**IMPORTANT:** After any use of the system, you must run the safe test script to restore the database to its original state and verify data integrity. This prevents data pollution and ensures consistency across test runs.

To run the complete test suite with automatic environment restoration:

```bash
./run_test_safe.sh
```

This script performs the following operations:
1. Restores all core CSV files from backup
2. Clears variable data files (applications, withdrawals, reports)
3. Compiles the test classes
4. Executes all 14 test cases
5. Restores the data files again
6. Cleans up compiled class files
7. Verifies the integrity of core files using MD5 checksums

The script ensures that the testing environment remains clean and consistent, preventing test contamination and data corruption.

### Restoring Environment Only

If you need to restore the test environment without running tests:

```bash
./restore_test_env.sh
```

This script restores all CSV files to their backed-up state and clears variable data files.

---

## Project Structure

The project follows a modular architecture with clear separation of concerns:

```
sc2002-project/
│
├── sc2002_project/              # Main application directory
│   ├── src/                     # Source code
│   │   ├── boundary/            # User interface classes
│   │   │   ├── WelcomeUI.java
│   │   │   ├── LoginUI.java
│   │   │   ├── StudentMainUI.java
│   │   │   ├── CompanyMainUI.java
│   │   │   ├── StaffMainUI.java
│   │   │   └── ...
│   │   ├── controller/          # Business logic controllers
│   │   │   ├── SystemController.java
│   │   │   ├── LoginController.java
│   │   │   ├── StudentController.java
│   │   │   ├── CompanyController.java
│   │   │   ├── StaffController.java
│   │   │   └── ...
│   │   ├── entities/            # Domain models
│   │   │   ├── User.java
│   │   │   ├── Student.java
│   │   │   ├── CompanyRepresentative.java
│   │   │   ├── Staff.java
│   │   │   ├── Internship.java
│   │   │   ├── Application.java
│   │   │   ├── Repository.java
│   │   │   └── ...
│   │   ├── enums/               # Enumeration types
│   │   │   ├── UserRole.java
│   │   │   ├── ApplicationStatus.java
│   │   │   ├── InternshipStatus.java
│   │   │   ├── ApprovalStatus.java
│   │   │   └── ...
│   │   ├── services/            # Service layer classes
│   │   │   ├── AccountManager.java
│   │   │   ├── ApplicationManager.java
│   │   │   └── InternshipManager.java
│   │   ├── utils/               # Utility classes
│   │   │   ├── FileHandler.java
│   │   │   ├── DataUtility.java
│   │   │   ├── InputValidator.java
│   │   │   └── ...
│   │   └── main/                # Application entry point
│   │       └── Main.java
│   │
│   └── srcTest/                 # Test classes
│       ├── CompanyRepApplicationSlotManagementTest.java
│       ├── InternshipVisibilityTest.java
│       ├── LoginDashboardAccessTest.java
│       └── ...
│
├── data/                        # CSV data files (runtime)
│   ├── students.csv
│   ├── companyreps.csv
│   ├── staffmembers.csv
│   ├── internships.csv
│   ├── applications.csv
│   ├── withdrawals.csv
│   └── reports.csv
│
├── .backup_csv/                 # Backup data files
│   ├── students.csv
│   ├── companyreps.csv
│   ├── staffmembers.csv
│   └── internships.csv
│
├── javadoc/                     # Generated documentation
│   └── index.html               # Documentation entry point
│
├── run_test_safe.sh             # Safe test execution script
├── restore_test_env.sh          # Environment restoration script
└── README.md                    # This file
```

The architecture follows the Model-View-Controller (MVC) pattern:
- **Boundary** layer handles user interaction
- **Controller** layer implements business logic
- **Entities** represent the data model
- **Services** provide reusable business operations
- **Utils** offer helper functions for common tasks

---

## Documentation
The documentation includes detailed descriptions of all public APIs, method parameters, return values, and class relationships.

To view the documentation:

1. Navigate to the `javadoc` directory in your file browser
2. Open `index.html` in a web browser
3. Use the navigation panel to browse packages, classes, and methods

Or in terminal:

```bash
open javadoc/index.html         # macOS
xdg-open javadoc/index.html     # Linux
start javadoc/index.html        # Windows
```

---

## Data Files

The system relies on seven CSV files for data persistence. Four of these are core files that contain user and internship data, while three are variable files that track applications, withdrawals, and reports.

### Core Data Files

These files are backed up and verified using MD5 checksums to ensure data integrity:

| File | Description |
|------|-------------|
| students.csv | Student account information |
| companyreps.csv | Company representative accounts |
| staffmembers.csv | Staff member accounts |
| internships.csv | Internship opportunity listings | 

- The integrity of these files is automatically verified after running the test suite. Any discrepancies will be reported by the `run_test_safe.sh` script.

### Variable Data Files

These files are cleared and regenerated during testing:

| File | Description | Content |
|------|-------------|---------|
| applications.csv | Student internship applications | Application records with status tracking |
| withdrawals.csv | Withdrawal requests | Student requests to withdraw from internships |
| reports.csv | System reports | Reports generated by staff members |

All data files must remain synchronized with their backup counterparts in the `.backup_csv` directory. The restoration scripts ensure consistency between runtime and backup data.

---

## Authentication

### Default Credentials

All user accounts in the system are initialized with a default password for testing purposes:

**Default Password:** `password`

Users can log in with their email address and this default password. After the first login, users are strongly encouraged to change their password using the "Change Password" option available in their respective menus.

### User Roles

The system supports three distinct user roles:
- **Student:** Identified by student email addresses
- **Company Representative:** Identified by company email domains
- **Staff:** Identified by staff member email addresses

Each role has specific permissions and access to different parts of the system. The authentication mechanism uses the user's role to direct them to the appropriate interface after successful login.

---

## Test Cases

The project includes 14 comprehensive test cases that validate core system functionality (which will be hard to test directly in terminal due to the complexity of the system). 

These tests are executed automatically by the `run_test_safe.sh` script:

1. **CompanyRepApplicationSlotManagementTest**
   Validates that company representatives can manage application slots correctly

2. **InternshipVisibilityTest**
   Verifies internship visibility toggling and access control

3. **LoginDashboardAccessTest**
   Tests authentication and role-based dashboard access

4. **CompanyRepViewInternshipStatusTest**
   Confirms company representatives can view internship status information

5. **PlacementConfirmationStatusTest**
   Validates the placement confirmation workflow

6. **CareerCenterStaffInternshipApprovalTest**
   Tests staff approval process for internships

7. **CompanyRepAccessInternshipDetailsTest**
   Verifies detailed internship information access for company representatives

8. **ReportGenerationTest**
   Tests report generation functionality for staff members

9. **WithdrawalApprovalTest**
   Validates the withdrawal request approval process

10. **CompanyRepCRUDOperationsTest**
    Tests create, read, update, and delete operations for internships

11. **CompanyRepEditRestrictionTest**
    Verifies that company representatives cannot edit approved internships

12. **ReportSummaryFilterTest**
    Tests report filtering and summary generation

13. **DateValidationTest**
    Validates date format and range checking

14. **InternshipCreationValidationTest**
    Tests input validation during internship creation

- Each test is designed to run independently and verify specific aspects of the system. The safe test runner ensures that each test starts with a clean database state, preventing interference between tests.
- Always run `./run_test_safe.sh` after using the system to prevent data pollution
- All dates in the system follow the format `yyyy-MM-dd`
