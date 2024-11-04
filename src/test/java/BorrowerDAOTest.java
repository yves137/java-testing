import org.aucalibray.aucaenum.MembershipLevel;
import org.aucalibray.dao.BookDAO;
import org.aucalibray.dao.DatabaseConnection;
import org.aucalibray.dao.BorrowerDAO;
import org.aucalibray.dao.MembershipDAO;
import org.aucalibray.model.Borrower;
import org.aucalibray.model.MembershipType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.*;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class BorrowerDAOTest {

    private BorrowerDAO borrowerDAO;
    private Connection mockConnection;
    private PreparedStatement mockPreparedStatement;
    private MembershipDAO mockMembershipDAO;
    private ResultSet mockResultSet;
    private BookDAO mockBookDAO;

    @BeforeEach
    void setUp() throws SQLException {
        // Create mock objects
        DatabaseConnection mockDbConnection = mock(DatabaseConnection.class);
        mockConnection = mock(Connection.class);
        mockPreparedStatement = mock(PreparedStatement.class);
        mockMembershipDAO = mock(MembershipDAO.class);
        mockResultSet = mock(ResultSet.class);
        mockBookDAO = mock(BookDAO.class);

        // Mock the connection and statement setup
        when(mockDbConnection.getConnection()).thenReturn(mockConnection);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);

        // Initialize BorrowerDAO with mocked DatabaseConnection
        borrowerDAO = new BorrowerDAO();
        borrowerDAO.dbConnection = mockDbConnection;
        borrowerDAO.membershipDAO= mockMembershipDAO;
        borrowerDAO.bookDAO= mockBookDAO;
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
        // Stub the method to return true
        when(mockBookDAO.removeBookFromShelf(borrower.getBookId())).thenReturn(true);

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

    @Test
    void testCalculateLateChargeFees_LateReturn() throws SQLException {
        // Setup a late return scenario
        UUID borrowerId = UUID.randomUUID();
        Date returnDate = Date.valueOf("2024-09-09"); // Set a past return date
        int lateChargeFees = 100; // Expected late charge fee

        // Configure the ResultSet to simulate a late return
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getDate("return_date")).thenReturn(returnDate);
        when(mockResultSet.getInt("late_charge_fees")).thenReturn(lateChargeFees);

        // Call the method
        int result = borrowerDAO.calculateLateChargeFees(borrowerId);

        // Assert that the late charge fee is correctly returned
        assertEquals(lateChargeFees, result);

        // Verify that the query was executed
        verify(mockPreparedStatement, times(1)).setObject(1, borrowerId);
        verify(mockPreparedStatement, times(1)).executeQuery();
    }

    @Test
    void testCalculateLateChargeFees_TimelyReturn() throws SQLException {
        // Setup a timely return scenario
        UUID borrowerId = UUID.randomUUID();
        Date returnDate = new Date(System.currentTimeMillis()+(24*60*60*1000)); // Set return date to today
        int lateChargeFees = 100; // Late charge fee should not be applied in this case

        // Configure the ResultSet to simulate a timely return
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getDate("return_date")).thenReturn(returnDate);
        when(mockResultSet.getInt("late_charge_fees")).thenReturn(lateChargeFees);

        // Call the method
        int result = borrowerDAO.calculateLateChargeFees(borrowerId);

        // Assert that no late charge fee is returned for a timely return
        assertEquals(0, result);

        // Verify that the query was executed
        verify(mockPreparedStatement, times(1)).setObject(1, borrowerId);
        verify(mockPreparedStatement, times(1)).executeQuery();
    }
}
