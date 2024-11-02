import org.aucalibray.aucaenum.Gender;
import org.aucalibray.aucaenum.LocationType;
import org.aucalibray.aucaenum.RoleType;
import org.aucalibray.dao.DatabaseConnection;
import org.aucalibray.dao.LocationDAO;
import org.aucalibray.dao.PersonDAO;
import org.aucalibray.dao.UserDAO;
import org.aucalibray.model.Location;
import org.aucalibray.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CreatePersonDAOTest {

    private PersonDAO personDAO;
    private DatabaseConnection mockDbConnection;
    private UserDAO mockUserDAO;
    private LocationDAO mockLocationDAO;
    private Connection mockConnection;
    private PreparedStatement mockPreparedStatement;

    @BeforeEach
    void setUp() throws SQLException {
        // Create mock objects
        mockDbConnection = mock(DatabaseConnection.class);
        mockUserDAO = mock(UserDAO.class);
        mockLocationDAO = mock(LocationDAO.class);
        mockConnection = mock(Connection.class);
        mockPreparedStatement = mock(PreparedStatement.class);

        // Mock the Connection and PreparedStatement
        when(mockDbConnection.getConnection()).thenReturn(mockConnection);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);

        // Initialize the PersonDAO with mocks
        personDAO = new PersonDAO();
        personDAO.dbConnection = mockDbConnection;
        personDAO.userdao = mockUserDAO;
        personDAO.locationDAO = mockLocationDAO;
    }

    @Test
    void testCreatePerson() throws SQLException {
        // Prepare test data
        String personId = "USR-02";
        String firstName = "John";
        String lastName = "Doe";
        String phoneNumber = "0788888888";
        UUID villageId = UUID.randomUUID();

        User user = new User(personId, firstName, lastName, Gender.MALE, phoneNumber, "rawPassword", RoleType.STUDENT, "johndoe", villageId);

        // Mocking the behavior of dependent methods
        when(mockLocationDAO.findFirstVillage()).thenReturn(new Location(villageId, "Vil","Village1", LocationType.VILLAGE,null));

        // Execute the method
        User createdUser = personDAO.createPerson(user);

        // Assertions to validate the result
        assertNotNull(createdUser);
        assertEquals(personId, createdUser.getPersonId());
        assertEquals(firstName, createdUser.getFirstName());
        assertEquals(lastName, createdUser.getLastName());
        assertEquals(phoneNumber, createdUser.getPhoneNumber());

        // Verify that password is hashed (you need to implement `hashPassword` appropriately)
        assertNotEquals("rawPassword", createdUser.getPassword());

        // Verify interactions with mocked dependencies
        verify(mockLocationDAO).findFirstVillage();
        verify(mockUserDAO).createUser(createdUser);
    }
}