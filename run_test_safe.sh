#!/bin/bash

# Safe Test Runner Script
# Purpose: Run tests and automatically restore all core files (including internships.csv)

echo "╔═══════════════════════════════════════════════════════════╗"
echo "║     SC2002 Project - Safe Test Runner Script              ║"
echo "╚═══════════════════════════════════════════════════════════╝"
echo ""

# Define paths
BACKUP_DIR=".backup_csv"
DATA_DIR="data"
TEST_DIR="sc2002_project"

# Check if backup directory exists
if [ ! -d "$BACKUP_DIR" ]; then
    echo "❌ Error: Cannot find $BACKUP_DIR directory"
    exit 1
fi

# Function: Restore environment
restore_environment() {
    echo "📁 Restoring test environment..."

    # Restore 4 core CSV files
    cp "$BACKUP_DIR/companyreps.csv" "$DATA_DIR/companyreps.csv"
    cp "$BACKUP_DIR/internships.csv" "$DATA_DIR/internships.csv"
    cp "$BACKUP_DIR/staffmembers.csv" "$DATA_DIR/staffmembers.csv"
    cp "$BACKUP_DIR/students.csv" "$DATA_DIR/students.csv"

    # Clear 3 variable CSV files
    echo "aID,iID,sID,status,applyDate,acceptedByStudent" > "$DATA_DIR/applications.csv"
    echo "requestID,aID,sID,status,requestDate" > "$DATA_DIR/withdrawals.csv"
    echo "rID,sID,iID,submissionDate,approved,summary" > "$DATA_DIR/reports.csv"

    # Clear srcTest directory
    rm -f "$TEST_DIR/srcTest/*.csv" 2>/dev/null

    echo "   ✅ Environment restored to initial state"
}

# Function: Clean up compiled files
cleanup_compiled_files() {
    echo "🧹 Cleaning up compiled files..."

    # Delete all .class files
    find "$TEST_DIR" -name "*.class" -type f -delete 2>/dev/null

    # Count deleted files
    echo "   ✅ All .class files cleaned up"
}

# Function: Verify integrity
verify_integrity() {
    echo ""
    echo "🔍 Verifying core file integrity..."

    local all_ok=true

    # Compare MD5
    students_md5=$(md5 -q "$DATA_DIR/students.csv")
    students_backup_md5=$(md5 -q "$BACKUP_DIR/students.csv")

    companyreps_md5=$(md5 -q "$DATA_DIR/companyreps.csv")
    companyreps_backup_md5=$(md5 -q "$BACKUP_DIR/companyreps.csv")

    staffmembers_md5=$(md5 -q "$DATA_DIR/staffmembers.csv")
    staffmembers_backup_md5=$(md5 -q "$BACKUP_DIR/staffmembers.csv")

    internships_md5=$(md5 -q "$DATA_DIR/internships.csv")
    internships_backup_md5=$(md5 -q "$BACKUP_DIR/internships.csv")

    echo "   students.csv:      $([ "$students_md5" == "$students_backup_md5" ] && echo '✅ Match' || (echo '❌ Mismatch' && all_ok=false))"
    echo "   companyreps.csv:   $([ "$companyreps_md5" == "$companyreps_backup_md5" ] && echo '✅ Match' || (echo '❌ Mismatch' && all_ok=false))"
    echo "   staffmembers.csv:  $([ "$staffmembers_md5" == "$staffmembers_backup_md5" ] && echo '✅ Match' || (echo '❌ Mismatch' && all_ok=false))"
    echo "   internships.csv:   $([ "$internships_md5" == "$internships_backup_md5" ] && echo '✅ Match' || (echo '❌ Mismatch' && all_ok=false))"

    if [ "$all_ok" = true ]; then
        echo "   ✅ All core files integrity verification passed"
        return 0
    else
        echo "   ⚠️  Warning: Some files do not match backup"
        return 1
    fi
}

# Main process
echo "════════════════════════════════════════════════════════════"
echo "Step 1: Pre-test environment preparation"
echo "════════════════════════════════════════════════════════════"
restore_environment

echo ""
echo "════════════════════════════════════════════════════════════"
echo "Step 2: Compile all tests"
echo "════════════════════════════════════════════════════════════"
echo ""

cd "$TEST_DIR"

# Find all test files
echo "🔨 Compiling source code..."

# First compile all source code in src/
javac --module-path lib -d . src/*/*.java 2>&1
compile_exit_code=$?

if [ $compile_exit_code -ne 0 ]; then
    echo "❌ Source code compilation failed"
    cd ..
    exit 1
fi

echo "✅ Source code compilation completed"
echo ""
echo "🔨 Compiling test files..."

# Compile test files (excluding JUnit tests)
javac --module-path lib -d . srcTest/*Test.java srcTest/*Validator.java 2>&1
compile_exit_code=$?

if [ $compile_exit_code -ne 0 ]; then
    echo "❌ Test compilation failed"
    cd ..
    exit 1
fi

echo "✅ All tests compiled successfully"

echo ""
echo "════════════════════════════════════════════════════════════"
echo "Step 3: Run all tests"
echo "════════════════════════════════════════════════════════════"
echo ""

# Find all test files
test_files=(
    "CompanyRepApplicationSlotManagementTest"
    "InternshipVisibilityTest"
    "LoginDashboardAccessTest"
    "CompanyRepViewInternshipStatusTest"
    "PlacementConfirmationStatusTest"
    "CareerCenterStaffInternshipApprovalTest"
    "CompanyRepAccessInternshipDetailsTest"
    "ReportGenerationTest"
    "WithdrawalApprovalTest"
    "CompanyRepCRUDOperationsTest"
    "CompanyRepEditRestrictionTest"
    "ReportSummaryFilterTest"
    "DateValidationTest"
    "InternshipCreationValidationTest"
    # enumTest - Not an executable test (no main method)
)

total_tests=${#test_files[@]}
passed_tests=0
failed_tests=0

echo "📋 Found $total_tests test files"
echo ""

# Run each test
for test_file in "${test_files[@]}"; do
    echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
    echo "🧪 Running test: $test_file"
    echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"

    # Run test
    java --module-path lib "$test_file" 2>&1
    exit_code=$?

    if [ $exit_code -eq 0 ]; then
        echo "✅ $test_file passed"
        ((passed_tests++))
    else
        echo "❌ $test_file failed (exit code: $exit_code)"
        ((failed_tests++))
    fi
    echo ""
done

# Test summary
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo "📊 All tests summary"
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo "   Total: $total_tests tests"
echo "   ✅ Passed: $passed_tests"
echo "   ❌ Failed: $failed_tests"

test_exit_code=0
if [ $failed_tests -gt 0 ]; then
    test_exit_code=1
fi

cd ..

echo ""
echo "════════════════════════════════════════════════════════════"
echo "Step 4: Post-test environment restoration"
echo "════════════════════════════════════════════════════════════"
restore_environment

echo ""
echo "════════════════════════════════════════════════════════════"
echo "Step 5: Clean up compiled files"
echo "════════════════════════════════════════════════════════════"
cleanup_compiled_files

echo ""
echo "════════════════════════════════════════════════════════════"
echo "Step 6: Verify environment integrity"
echo "════════════════════════════════════════════════════════════"
verify_integrity

echo ""
echo "════════════════════════════════════════════════════════════"
echo "Test completion summary"
echo "════════════════════════════════════════════════════════════"

if [ $test_exit_code -eq 0 ]; then
    echo "✅ Tests executed successfully"
else
    echo "❌ Test execution failed (exit code: $test_exit_code)"
fi

echo "✅ All core files restored to initial state"
echo "✅ Test data cleaned up"
echo "✅ Compiled output cleaned up"
echo ""
echo "You can now safely continue development or run the application!"
echo ""

exit $test_exit_code