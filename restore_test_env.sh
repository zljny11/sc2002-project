#!/bin/bash

echo "=== restoring envir ==="

BACKUP_DIR=".backup_csv"
DATA_DIR="data"


if [ ! -d "$BACKUP_DIR" ]; then
    echo "error: cannot find $BACKUP_DIR dir"
    exit 1
fi

echo "1. restore 4 core csv..."
cp "$BACKUP_DIR/companyreps.csv" "$DATA_DIR/companyreps.csv"
cp "$BACKUP_DIR/internships.csv" "$DATA_DIR/internships.csv"
cp "$BACKUP_DIR/staffmembers.csv" "$DATA_DIR/staffmembers.csv"
cp "$BACKUP_DIR/students.csv" "$DATA_DIR/students.csv"
echo "   ✅ companyreps.csv, internships.csv, staffmembers.csv, students.csv restored"

echo ""
echo "2. clean 3 immuable csv only keep header..."
echo "aID,iID,sID,status,applyDate,acceptedByStudent" > "$DATA_DIR/applications.csv"
echo "requestID,aID,sID,status,requestDate" > "$DATA_DIR/withdrawals.csv"
echo "rID,sID,iID,submissionDate,approved,summary" > "$DATA_DIR/reports.csv"
echo "   ✅ applications.csv, withdrawals.csv, reports.csv cleaned"

echo ""
echo "3. cleaning srcTest csv..."
rm -f sc2002_project/srcTest/*.csv 2>/dev/null
echo "   ✅ srcTest dir cleaned"

echo ""
echo "=== test envir restored ==="
echo ""
echo " file status:"
echo " core(restored):"
wc -l "$DATA_DIR/companyreps.csv" "$DATA_DIR/internships.csv" "$DATA_DIR/staffmembers.csv" "$DATA_DIR/students.csv" | grep -v total
echo ""
echo " immutable files(cleaned up):"
wc -l "$DATA_DIR/applications.csv" "$DATA_DIR/withdrawals.csv" "$DATA_DIR/reports.csv" | grep -v total
echo ""
echo " can run the test now"
