import org.aucalibray.aucaenum.MembershipStatus;
import org.aucalibray.dao.DatabaseConnection;
import org.aucalibray.dao.MembershipDAO;
import org.aucalibray.dao.MembershipTypeDAO;
import org.aucalibray.model.Membership;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class MembershipDAOTest {

    private MembershipDAO membershipDAO;
    private Connection mockConnection;
    private PreparedStatement mockPreparedStatement;

    @BeforeEach
    void setUp() throws SQLException {
        // Create mock objects
        DatabaseConnection mockDbConnection = mock(DatabaseConnection.class);
        mockConnection = mock(Connection.class);
        mockPreparedStatement = mock(PreparedStatement.class);

        // Mock the connection and statement setup
        when(mockDbConnection.getConnection()).thenReturn(mockConnection);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);

        // Initialize MembershipDAO with mocked DatabaseConnection
        membershipDAO = new MembershipDAO();
        membershipDAO.dbConnection = mockDbConnection;
        membershipDAO.membershipTypeDAO= mock(MembershipTypeDAO.class);
    }

    @Test
    void testRegisterMembership() throws SQLException {
        // Create a sample Membership object
        Membership membership = new Membership(
                UUID.randomUUID(),
                new Date(),  // Expiring time
                "ABC123",    // Membership code
                UUID.randomUUID(), // Membership type ID
                MembershipStatus.PENDING,
                "Reader-01", // Reader ID
                new Date()   // Registration date
        );

        // Call the registerMembership method
        membershipDAO.registerMembership(membership);

        // Verify that prepareStatement was called with the correct SQL
        verify(mockConnection).prepareStatement("INSERT INTO Membership (membership_id, expiring_time, membership_code, membership_type_id, membership_status, reader_id, registration_date) VALUES (?, ?, ?, ?, ?, ?, ?)");

        // Verify that setObject and other setter methods are called with the expected values
        verify(mockPreparedStatement).setObject(1, membership.getMembershipId());
        verify(mockPreparedStatement).setDate(2, new java.sql.Date(membership.getExpiringTime().getTime()));
        verify(mockPreparedStatement).setString(3, membership.getMembershipCode());
        verify(mockPreparedStatement).setObject(4, membership.getMembershipTypeId());
        verify(mockPreparedStatement).setString(5, membership.getMembershipStatus().toString());
        verify(mockPreparedStatement).setString(6, membership.getReaderId());
        verify(mockPreparedStatement).setDate(7, new java.sql.Date(membership.getRegistrationDate().getTime()));

        // Verify that executeUpdate is called to perform the insert operation
        verify(mockPreparedStatement).executeUpdate();
    }
}
