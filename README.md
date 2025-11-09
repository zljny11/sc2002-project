# README

## NTU 2025 Semster 1 SC2002 SMAC Group5 OOP group project
Professor taking course: Professor Zhang Jie, Professor Li Fang


Group member: 

CHONG CHENG YU (CCDS)

HEER JIE MIN, RACHEL (SPMS)

LIM GECK BOEY CISLYN (SPMS)

MA SHUNLIANG (SPMS)

ZHEN BINWEI (SPMS)

---

## after each use plz run tests and restore the environment

```bash
./run_test_safe.sh
```

script will do:
1. restore the environment
2. compile the project
3. run 14 test
4. restore data base
5. clean up compilation
6. autocheck integrity

---

## 14 specific test 
- (comprised of complicated cases which cannot be displayed in brief in command line for report, so i place test details here)

1. CompanyRepApplicationSlotManagementTest 
2. InternshipVisibilityTest
3. LoginDashboardAccessTest
4. CompanyRepViewInternshipStatusTest
5. PlacementConfirmationStatusTest
6. CareerCenterStaffInternshipApprovalTest
7. CompanyRepAccessInternshipDetailsTest
8. ReportGenerationTest
9. WithdrawalApprovalTest
10. CompanyRepCRUDOperationsTest
11. CompanyRepEditRestrictionTest
12. ReportSummaryFilterTest
13. DateValidationTest
14. InternshipCreationValidationTest


---

## core files

| files            | MD5 | lines |
|------------------|-----|-------|
| students.csv     | `03ad24e6040cbf51f8675f515647e176` | 14    |
| companyreps.csv  | `9a7572f4a7bab326ad8e4ef70cd91b1a` | 15    |
| staffmembers.csv | `1a5c1aa4dd1271973e53adccb9ba70fb` | 10    |
| internships.csv  | `2c4359a0b35756c162cd50bcd7a65ad0` | 16    |

***keep aligned with backup files***

---

## if just restore the environment

```bash
./restore_test_env.sh
```
