import org.aucalibray.dao.DatabaseConnection;
import org.aucalibray.dao.RoomDAO;
import org.aucalibray.dao.ShelfDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class RoomDAOTest {

    private RoomDAO roomDAO;
    private ShelfDAO mockShelfDAO;

    @BeforeEach
    void setUp() throws SQLException {
        // Mock the database connection and prepared statement
        Connection mockConnection = mock(Connection.class);
        PreparedStatement mockPreparedStatement = mock(PreparedStatement.class);

        // Mock the behavior of DatabaseConnection.getInstance() and getConnection()
        DatabaseConnection mockDatabaseConnection = mock(DatabaseConnection.class);
        when(mockDatabaseConnection.getConnection()).thenReturn(mockConnection);

        // Mock the prepareStatement behavior
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);

        // Mock ShelfDAO
        mockShelfDAO = mock(ShelfDAO.class);
        roomDAO = new RoomDAO();
        roomDAO.dbConnection = mockDatabaseConnection;
        roomDAO.shelfDAO = mockShelfDAO;  // Inject the mock ShelfDAO
    }

    @Test
    void testTotalNumberBooksInRoom() {
        // Create a sample room ID
        UUID roomId = UUID.randomUUID();

        // Mock the result of calculateAvailableStockByRoomId to return a specific value
        int expectedTotalBooks = 50;
        when(mockShelfDAO.calculateAvailableStockByRoomId(roomId)).thenReturn(expectedTotalBooks);

        // Call the method and assert the result
        int actualTotalBooks = roomDAO.totalNumberBooksInRoom(roomId);
        assertEquals(expectedTotalBooks, actualTotalBooks);

        // Verify if calculateAvailableStockByRoomId was called once with the correct room ID
        verify(mockShelfDAO, times(1)).calculateAvailableStockByRoomId(roomId);
    }
}
