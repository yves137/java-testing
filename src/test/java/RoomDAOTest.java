import org.aucalibray.dao.DatabaseConnection;
import org.aucalibray.dao.RoomDAO;
import org.aucalibray.dao.ShelfDAO;
import org.aucalibray.model.Room;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class RoomDAOTest {

    private RoomDAO roomDAO;
    private ShelfDAO mockShelfDAO;
    private ResultSet mockResultSet;

    @BeforeEach
    void setUp() throws SQLException {
        // Mock the database connection and prepared statement
        Connection mockConnection = mock(Connection.class);
        PreparedStatement mockPreparedStatement = mock(PreparedStatement.class);
        mockResultSet = mock(ResultSet .class);

        // Mock the behavior of DatabaseConnection.getInstance() and getConnection()
        DatabaseConnection mockDatabaseConnection = mock(DatabaseConnection.class);
        when(mockDatabaseConnection.getConnection()).thenReturn(mockConnection);

        // Mock the prepareStatement behavior
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);

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

    @Test
    void testGetRoomWithFewestBook_Found() throws SQLException {
        // Create a sample room ID and room details
        UUID roomId = UUID.randomUUID();
        String roomCode = "Room101";

        // Mock the behavior of getRoomWithFewStock to return the roomId
        when(mockShelfDAO.getRoomWithFewStock()).thenReturn(roomId);

        // Mock the ResultSet to simulate room data retrieval
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getString("room_id")).thenReturn(roomId.toString());
        when(mockResultSet.getString("room_code")).thenReturn(roomCode);

        // Call the method and verify the result
        Room room = roomDAO.getRoomWithFewestBook();
        assertEquals(roomId, room.getRoomId());
        assertEquals(roomCode, room.getRoomCode());

        // Verify that getRoomWithFewStock was called exactly once
        verify(mockShelfDAO, times(1)).getRoomWithFewStock();
    }

    @Test
    void testGetRoomWithFewestBook_NotFound() throws SQLException {
        // Mock getRoomWithFewStock to return null, indicating no room with fewest books
        when(mockShelfDAO.getRoomWithFewStock()).thenReturn(null);

        // Call the method and assert that it returns null
        Room room = roomDAO.getRoomWithFewestBook();
        assertNull(room);

        // Verify that getRoomWithFewStock was called exactly once
        verify(mockShelfDAO, times(1)).getRoomWithFewStock();
    }
}
