import org.aucalibray.aucaenum.RoleType;
import org.aucalibray.dao.DatabaseConnection;
import org.aucalibray.dao.UserDAO;
import org.aucalibray.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class UserDAOTest {

    private UserDAO userDAO;
    private DatabaseConnection mockDbConnection;
    private Connection mockConnection;
    private PreparedStatement mockPreparedStatement;
    private ResultSet mockResultSet;

    @BeforeEach
    void setUp() throws SQLException {
        // Create mock objects
        mockDbConnection = mock(DatabaseConnection.class);
        mockConnection = mock(Connection.class);
        mockPreparedStatement = mock(PreparedStatement.class);
        mockResultSet = mock(ResultSet.class);

        // Mock the Connection and PreparedStatement
        when(mockDbConnection.getConnection()).thenReturn(mockConnection);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);

        // Initialize the UserDAO with mocks
        userDAO = new UserDAO();
        userDAO.dbConnection = mockDbConnection;
    }

    @Test
    void testAuthenticateUser_Success() throws SQLException {
        String userName = "johndoe";
        String password = "rawPassword";
        String hashedPassword = "hashedPassword"; // This should be the result of hashing "rawPassword"
        User loginUser = new User(userName, password);
        loginUser.setPassword(hashedPassword);
        User user = new User("USR-01", hashedPassword, RoleType.STUDENT, userName, UUID.randomUUID());

        // Mocking the behavior of dependent methods
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getString("password")).thenReturn(hashedPassword);
        when(mockPreparedStatement.getConnection()).thenReturn(mockConnection);

        // Mocking user retrieval
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.getString("user_name")).thenReturn(userName);
        when(mockResultSet.getString("role")).thenReturn(user.getRole().toString());
        when(mockResultSet.getString("person_id")).thenReturn(user.getPersonId());
        when(mockResultSet.getObject("village_id")).thenReturn(user.getVillageId());

        // Execute the method
        boolean isAuthenticated = userDAO.authenticateUser(loginUser.getUserName(), loginUser.getPassword());

        // Assertions to validate the result
        assertTrue(isAuthenticated);
    }

    @Test
    void testAuthenticateUser_Failure() throws SQLException {
        String userName = "johndoe";
        String password = "rawPassword";
        String hashedPassword = "hashedPassword"; // This should be the result of hashing "rawPassword"
        User loginUser = new User(userName, password);
        User user = new User("USR-01", hashedPassword, RoleType.STUDENT, userName, UUID.randomUUID());

        // Mocking the behavior of dependent methods
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getString("password")).thenReturn(hashedPassword);
        when(mockPreparedStatement.getConnection()).thenReturn(mockConnection);

        // Mocking user retrieval
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.getString("user_name")).thenReturn(userName);
        when(mockResultSet.getString("role")).thenReturn(user.getRole().toString());
        when(mockResultSet.getString("person_id")).thenReturn(user.getPersonId());
        when(mockResultSet.getObject("village_id")).thenReturn(user.getVillageId());

        // Execute the method
        boolean isAuthenticated = userDAO.authenticateUser(loginUser.getUserName(), loginUser.getPassword());

        // Assertions to validate the result
        assertFalse(isAuthenticated);
    }

    @Test
    void testAuthenticateUser_UserNotFound() throws SQLException {
        String userName = "unknownUser";
        String password = "rawPassword";

        // Mocking the behavior of dependent methods to simulate no user found
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false); // No results found

        // Execute the method
        boolean isAuthenticated = userDAO.authenticateUser(userName, password);

        // Assertions to validate the result
        assertFalse(isAuthenticated);
    }
}
