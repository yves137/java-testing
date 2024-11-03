import org.aucalibray.aucaenum.MembershipLevel;
import org.aucalibray.dao.DatabaseConnection;
import org.aucalibray.dao.BorrowerDAO;
import org.aucalibray.dao.MembershipDAO;
import org.aucalibray.model.Borrower;
import org.aucalibray.model.MembershipType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class BorrowerDAOTest {

    private BorrowerDAO borrowerDAO;
    private Connection mockConnection;
    private PreparedStatement mockPreparedStatement;
    private MembershipDAO mockMembershipDAO;

    @BeforeEach
    void setUp() throws SQLException {
        // Create mock objects
        DatabaseConnection mockDbConnection = mock(DatabaseConnection.class);
        mockConnection = mock(Connection.class);
        mockPreparedStatement = mock(PreparedStatement.class);
        mockMembershipDAO = mock(MembershipDAO.class);

        // Mock the connection and statement setup
        when(mockDbConnection.getConnection()).thenReturn(mockConnection);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);

        // Initialize BorrowerDAO with mocked DatabaseConnection
        borrowerDAO = new BorrowerDAO();
        borrowerDAO.dbConnection = mockDbConnection;
        borrowerDAO.membershipDAO= mockMembershipDAO;
    }

    @Test
    void testBorrowerBook() throws SQLException {
        // Create a sample Borrower object
        Borrower borrower = new Borrower(
                UUID.randomUUID(),         // Borrower ID
                UUID.randomUUID(),         // Book ID
                new java.util.Date(),      // Due Date
                0,                         // Fine
                5,                         // Late Charge Fees
                new java.util.Date(),      // Pickup Date
                "Reader-01",               // Reader ID
                new java.util.Date()       // Return Date
        );

        // Call the borrowerBook method
        borrowerDAO.borrowerBook(borrower);

        // Verify that prepareStatement was called with the correct SQL
        verify(mockConnection).prepareStatement("INSERT INTO Borrower (id,book_id,due_date,fine,late_charge_fees,pickup_date,reader_id,return_date) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");

        // Verify that setObject and other setter methods are called with the expected values
        verify(mockPreparedStatement).setObject(1, borrower.getId());
        verify(mockPreparedStatement).setObject(2, borrower.getBookId());
        verify(mockPreparedStatement).setDate(3, new Date(borrower.getDueDate().getTime()));
        verify(mockPreparedStatement).setInt(4, borrower.getFine());
        verify(mockPreparedStatement).setInt(5, borrower.getLateChargeFees());
        verify(mockPreparedStatement).setDate(6, new Date(borrower.getPickupDate().getTime()));
        verify(mockPreparedStatement).setString(7, borrower.getReaderId());
        verify(mockPreparedStatement).setDate(8, new Date(borrower.getReturnDate().getTime()));

        // Verify that executeUpdate is called to perform the insert operation
        verify(mockPreparedStatement).executeUpdate();
    }

    @Test
    void testValidateBorrowerGoldMembership() {
        // Setup: Create a Gold MembershipType with maxBooks of 5
        MembershipType membershipType = new MembershipType(UUID.randomUUID(), MembershipLevel.GOLD);

        // Stub the method to return the mocked membership type
        String readerId = "Reader-01";
        when(mockMembershipDAO.getMembershipTypeByReaderId(readerId)).thenReturn(membershipType);

        // Pass Test: The user is trying to borrow within the allowed limit for GOLD membership
        boolean passResult = borrowerDAO.validateBorrower(readerId, 4); // Borrowing 4 books, limit is 5

        // Fail Test: The user is trying to borrow more than the allowed limit for GOLD membership
        boolean failResult = borrowerDAO.validateBorrower(readerId, 6); // Borrowing 6 books, limit is 5

        // Assert: The result should be true, as 4 books is within the 5-book limit
        assertTrue(passResult, "The validation should pass for borrowing within the allowed limit for GOLD membership");

        // Assert: The result should be false, as 6 books exceed the 5-book limit
        assertFalse(failResult, "The validation should fail for borrowing beyond the allowed limit for GOLD membership");
    }

    @Test
    void testValidateBorrowerSilverMembership() {
        // Setup: Create a Silver MembershipType with maxBooks of 3
        MembershipType membershipType = new MembershipType(UUID.randomUUID(), MembershipLevel.SILVER);

        // Stub the method to return the mocked membership type
        String readerId = "Reader-02";
        when(mockMembershipDAO.getMembershipTypeByReaderId(readerId)).thenReturn(membershipType);

        // Pass Test: The user is trying to borrow within the allowed limit for SILVER membership
        boolean passResult = borrowerDAO.validateBorrower(readerId, 3); // Borrowing 3 books, limit is 3

        // Fail Test: The user is trying to borrow more than the allowed limit for SILVER membership
        boolean failResult = borrowerDAO.validateBorrower(readerId, 4); // Borrowing 4 books, limit is 3

        // Assert: The result should be true, as 3 books is within the 3-book limit
        assertTrue(passResult, "The validation should pass for borrowing within the allowed limit for SILVER membership");

        // Assert: The result should be false, as 4 books exceed the 3-book limit
        assertFalse(failResult, "The validation should fail for borrowing beyond the allowed limit for SILVER membership");
    }

    @Test
    void testValidateBorrowerStriverMembership() {
        // Setup: Create a default STRIVER MembershipType with maxBooks of 2
        MembershipType membershipType = new MembershipType(UUID.randomUUID(), MembershipLevel.STRIVER);

        // Stub the method to return the mocked membership type
        String readerId = "Reader-03";
        when(mockMembershipDAO.getMembershipTypeByReaderId(readerId)).thenReturn(membershipType);

        // Pass Test: The user is trying to borrow within the allowed limit for STRIVER membership
        boolean passResult = borrowerDAO.validateBorrower(readerId, 2); // Borrowing 2 books, limit is 2

        // Fail Test: The user is trying to borrow more than the allowed limit for STRIVER membership
        boolean failResult = borrowerDAO.validateBorrower(readerId, 3); // Borrowing 3 books, limit is 2

        // Assert: The result should be true, as 2 books is within the 2-book limit
        assertTrue(passResult, "The validation should pass for borrowing within the allowed limit for STRIVER membership");

        // Assert: The result should be false, as 3 books exceed the 2-book limit
        assertFalse(failResult, "The validation should fail for borrowing beyond the allowed limit for STRIVER membership");
    }
}
