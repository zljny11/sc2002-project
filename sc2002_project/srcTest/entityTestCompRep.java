import entities.CompanyRepresentative;
import enums.UserRole;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.junit.Assert.assertTrue;


public class entityTestCompRep {



    @Test
    public void testCompanyRepresentativeConstructor() {
        CompanyRepresentative rep = new CompanyRepresentative("john.doe@techcorp.com", "John Doe", "password123", "TechCorp", "IT", "Manager", true);

        assertEquals("john.doe@techcorp.com", rep.getID());
        assertEquals("John Doe", rep.getName());
        assertEquals("password123", rep.getPassword());
        assertEquals(UserRole.COMPANY_REP, rep.getRole());
        assertEquals("TechCorp", rep.getCompanyName());
        assertEquals("IT", rep.getDepartment());
        assertEquals("Manager", rep.getPosition());
        assertTrue(rep.isApproved());
    }

    @Test
    public void testCompanyRepresentativeGettersAndSetters() {
        CompanyRepresentative rep = new CompanyRepresentative("john.doe@techcorp.com", "John Doe", "password123", "TechCorp", "IT", "Manager", true);

        rep.setApproved(false);
        assertFalse(rep.isApproved());
    }

    @Test
    public void testCompanyRepresentativeToCSVRow() {
        CompanyRepresentative rep = new CompanyRepresentative("john.doe@techcorp.com", "John Doe", "password123", "TechCorp", "IT", "Manager", true);
        String[] csvRow = rep.toCSVRow();

        assertArrayEquals(new String[]{"john.doe@techcorp.com", "John Doe", "password123", "TechCorp", "IT", "Manager", "true"}, csvRow);
    }

    @Test
    public void testCompanyRepresentativeFromCSVRow() {
        String[] csvRow = {"john.doe@techcorp.com", "John Doe", "password123", "TechCorp", "IT", "Manager", "true"};
        CompanyRepresentative rep = CompanyRepresentative.fromCSVRow(csvRow);

        assertEquals("john.doe@techcorp.com", rep.getID());
        assertEquals("John Doe", rep.getName());
        assertEquals("password123", rep.getPassword());
        assertEquals("TechCorp", rep.getCompanyName());
        assertEquals("IT", rep.getDepartment());
        assertEquals("Manager", rep.getPosition());
        assertTrue(rep.isApproved());
    }

    // ==================== 新增：正常数据测试 ====================

    @Test
    public void testValidEmailFormats() {
        // 测试各种有效的邮箱格式
        String[][] validCases = {
                {"john.doe@techcorp.com", "John Doe", "Pass123!", "TechCorp", "Engineering", "Senior Engineer", "true"},
                {"jane.smith@innovate.com", "Jane Smith", "Secure456", "Innovate Solutions", "Marketing", "Marketing Manager", "true"},
                {"bob.wilson@startup.io", "Bob Wilson", "MyPass789", "StartUp Inc", "Sales", "Sales Director", "false"}
        };

        for (String[] csvRow : validCases) {
            CompanyRepresentative rep = CompanyRepresentative.fromCSVRow(csvRow);
            assertNotNull(rep);
            assertEquals(csvRow[0], rep.getID());
            assertEquals(csvRow[1], rep.getName());
        }
    }

    @Test
    public void testApprovedAndPendingStatus() {
        // repre approved
        String[] approvedRow = {"admin@approved.com", "Approved User", "Pass123", "Approved Co", "IT", "Manager", "true"};
        CompanyRepresentative approved = CompanyRepresentative.fromCSVRow(approvedRow);
        assertTrue(approved.isApproved());

        // to be approved
        String[] pendingRow = {"admin@pending.com", "Pending User", "Pass456", "Pending Co", "Sales", "Rep", "false"};
        CompanyRepresentative pending = CompanyRepresentative.fromCSVRow(pendingRow);
        assertFalse(pending.isApproved());
    }

    // ==================== 无效邮箱格式 ====================

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidEmail_NoAtSymbol() {
        // lack of @ symbol
        String[] csvRow = {"invalid.email", "Tom Jones", "Pass123", "Invalid Corp", "IT", "Manager", "true"};
        CompanyRepresentative.fromCSVRow(csvRow);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidEmail_NoUsername() {
        // lack of name
        String[] csvRow = {"@company.com", "Lisa Brown", "Pass789", "StartUp", "Marketing", "Lead", "true"};
        CompanyRepresentative.fromCSVRow(csvRow);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidEmail_NoDomain() {
        // lack of domain
        String[] csvRow = {"user@", "Mark Smith", "Pass456", "Tech Inc", "Sales", "Rep", "false"};
        CompanyRepresentative.fromCSVRow(csvRow);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidEmail_DoubleAtSymbol() {
        // @@
        String[] csvRow = {"user@@double.com", "John Tan", "Pass000", "Double Inc", "Finance", "Analyst", "false"};
        CompanyRepresentative.fromCSVRow(csvRow);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidEmail_NoTopLevelDomain() {
        // 测试缺少顶级域名的邮箱
        String[] csvRow = {"user@company", "Sarah Lee", "Pass111", "No Domain", "HR", "Manager", "true"};
        CompanyRepresentative.fromCSVRow(csvRow);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidEmail_DomainStartsWithDot() {
        // dot start
        String[] csvRow = {"user@.com", "Mike Chen", "Pass222", "Dot Start", "IT", "Developer", "false"};
        CompanyRepresentative.fromCSVRow(csvRow);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidEmail_DomainEndsWithDot() {
        // dot end
        String[] csvRow = {"user@company.", "Emma Tan", "Pass333", "Dot End", "Sales", "Lead", "true"};
        CompanyRepresentative.fromCSVRow(csvRow);
    }

    // ==================== 缺失字段 ====================

    @Test(expected = IllegalArgumentException.class)
    public void testMissingField_ID() {

        String[] csvRow = {"", "Kate Wilson", "Pass444", "Empty Corp", "Marketing", "Manager", "false"};
        CompanyRepresentative.fromCSVRow(csvRow);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMissingField_Name() {

        String[] csvRow = {"admin@test.com", "", "Pass555", "Test Inc", "IT", "Admin", "true"};
        CompanyRepresentative.fromCSVRow(csvRow);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMissingField_Password() {

        String[] csvRow = {"admin@company.com", "John Doe", "", "NoPass Corp", "Sales", "Rep", "false"};
        CompanyRepresentative.fromCSVRow(csvRow);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMissingField_CompanyName() {

        String[] csvRow = {"admin@enterprise.com", "Jane Smith", "Pass666", "", "Finance", "Analyst", "true"};
        CompanyRepresentative.fromCSVRow(csvRow);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMissingField_Department() {

        String[] csvRow = {"admin@startup.com", "Bob Lee", "Pass777", "StartUp Co", "", "Manager", "false"};
        CompanyRepresentative.fromCSVRow(csvRow);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMissingField_Position() {

        String[] csvRow = {"admin@global.com", "Alice Tan", "Pass888", "Global Inc", "Marketing", "", "true"};
        CompanyRepresentative.fromCSVRow(csvRow);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMissingField_Approved() {

        String[] csvRow = {"admin@tech.com", "David Chen", "Pass999", "Tech Corp", "Sales", "Sales Rep", ""};
        CompanyRepresentative.fromCSVRow(csvRow);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWhitespaceOnlyField_Name() {

        String[] csvRow = {"admin@test.com", "   ", "Pass123", "Test Corp", "IT", "Manager", "true"};
        CompanyRepresentative.fromCSVRow(csvRow);
    }

    // ==================== 布尔值错误 ====================

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidBoolean_InvalidString() {

        String[] csvRow = {"admin@software.com", "Emma Lee", "Pass000", "Software Inc", "IT", "Developer", "INVALID"};
        CompanyRepresentative.fromCSVRow(csvRow);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidBoolean_NumericOne() {

        String[] csvRow = {"admin@digital.com", "Michael Tan", "Pass111", "Digital Corp", "Marketing", "Manager", "1"};
        CompanyRepresentative.fromCSVRow(csvRow);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidBoolean_YesString() {

        String[] csvRow = {"admin@consulting.com", "Sarah Wong", "Pass222", "Consulting", "Finance", "Analyst", "yes"};
        CompanyRepresentative.fromCSVRow(csvRow);
    }

    @Test
    public void testValidBoolean_CaseInsensitive() {

        String[] trueRow = {"admin@test1.com", "User One", "Pass123", "Test Co", "IT", "Manager", "TRUE"};
        CompanyRepresentative rep1 = CompanyRepresentative.fromCSVRow(trueRow);
        assertTrue(rep1.isApproved());

        String[] falseRow = {"admin@test2.com", "User Two", "Pass456", "Test Co", "Sales", "Rep", "FALSE"};
        CompanyRepresentative rep2 = CompanyRepresentative.fromCSVRow(falseRow);
        assertFalse(rep2.isApproved());

        String[] mixedRow = {"admin@test3.com", "User Three", "Pass789", "Test Co", "HR", "Lead", "FaLsE"};
        CompanyRepresentative rep3 = CompanyRepresentative.fromCSVRow(mixedRow);
        assertFalse(rep3.isApproved());
    }

    // ====================  字段数量不匹配 ====================

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidFieldCount_TooFew() {

        String[] csvRow = {"admin@solution.com", "Tom Lee"};
        CompanyRepresentative.fromCSVRow(csvRow);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidFieldCount_TooMany() {

        String[] csvRow = {"admin@enterprise.sg", "Kate Tan", "Pass666", "Enterprise", "Sales", "Manager", "true", "extra"};
        CompanyRepresentative.fromCSVRow(csvRow);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidFieldCount_WayTooMany() {

        String[] csvRow = {"admin@startup.sg", "Mike Wong", "Pass777", "StartUp", "Marketing", "Lead", "false", "extra1", "extra2"};
        CompanyRepresentative.fromCSVRow(csvRow);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullArray() {

        CompanyRepresentative.fromCSVRow(null);
    }

    // ==================== 边界值测试 ====================

    @Test
    public void testEdgeCase_LongEmail() {

        String[] csvRow = {"very.long.email.address@verylongdomainname.company.com",
                "Long Email User", "Pass123", "Long Corp", "IT", "Manager", "true"};
        CompanyRepresentative rep = CompanyRepresentative.fromCSVRow(csvRow);
        assertNotNull(rep);
        assertEquals("very.long.email.address@verylongdomainname.company.com", rep.getID());
    }

    @Test
    public void testEdgeCase_MinimalEmail() {

        String[] csvRow = {"a@b.c", "Min User", "Pass123", "Min Corp", "IT", "Dev", "false"};
        CompanyRepresentative rep = CompanyRepresentative.fromCSVRow(csvRow);
        assertNotNull(rep);
        assertEquals("a@b.c", rep.getID());
    }

    @Test
    public void testEdgeCase_LongNames() {

        String[] csvRow = {"admin@test.com",
                "Dr. Jonathan Alexander Christopher Winchester-Smith III",
                "Pass123",
                "International Business Machines Corporation Limited",
                "Information Technology Services",
                "Senior Vice President of Engineering",
                "true"};
        CompanyRepresentative rep = CompanyRepresentative.fromCSVRow(csvRow);
        assertNotNull(rep);
        assertEquals("Dr. Jonathan Alexander Christopher Winchester-Smith III", rep.getName());
    }

    @Test
    public void testEdgeCase_SpecialCharactersInName() {

        String[] csvRow = {"admin@test.com", "O'Brien-Smith, Jr.", "Pass123", "Test Corp", "IT", "Manager", "true"};
        CompanyRepresentative rep = CompanyRepresentative.fromCSVRow(csvRow);
        assertNotNull(rep);
        assertEquals("O'Brien-Smith, Jr.", rep.getName());
    }

    // ==================== overall ====================

    @Test
    public void testRoundTrip_ToCSVAndBack() {

        CompanyRepresentative original = new CompanyRepresentative(
                "roundtrip@test.com",
                "Round Trip User",
                "Pass123",
                "RoundTrip Corp",
                "Testing",
                "Test Engineer",
                true
        );

        String[] csvRow = original.toCSVRow();
        CompanyRepresentative reconstructed = CompanyRepresentative.fromCSVRow(csvRow);

        assertEquals(original.getID(), reconstructed.getID());
        assertEquals(original.getName(), reconstructed.getName());
        assertEquals(original.getPassword(), reconstructed.getPassword());
        assertEquals(original.getCompanyName(), reconstructed.getCompanyName());
        assertEquals(original.getDepartment(), reconstructed.getDepartment());
        assertEquals(original.getPosition(), reconstructed.getPosition());
        assertEquals(original.isApproved(), reconstructed.isApproved());
    }

    @Test
    public void testMultipleValidRecords() {

        String[][] validRecords = {
                {"user1@company.com", "User One", "Pass1", "Company A", "IT", "Dev", "true"},
                {"user2@company.com", "User Two", "Pass2", "Company B", "Sales", "Rep", "false"},
                {"user3@company.com", "User Three", "Pass3", "Company C", "HR", "Manager", "true"}
        };

        for (String[] record : validRecords) {
            CompanyRepresentative rep = CompanyRepresentative.fromCSVRow(record);
            assertNotNull(rep);
            assertEquals(record[0], rep.getID());
        }
    }
}