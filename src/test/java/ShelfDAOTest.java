import org.aucalibray.dao.DatabaseConnection;
import org.aucalibray.dao.ShelfDAO;
import org.aucalibray.model.Shelf;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;

public class ShelfDAOTest {

    private ShelfDAO shelfDAO;
    private PreparedStatement mockPreparedStatement;
    private Connection mockConnection;

    @BeforeEach
    void setUp() throws SQLException {
        // Mock the database connection and prepared statement
        mockConnection = mock(Connection.class);
        mockPreparedStatement = mock(PreparedStatement.class);

        // Mock the behavior of DatabaseConnection.getInstance() and getConnection()
        DatabaseConnection mockDatabaseConnection = mock(DatabaseConnection.class);
        when(mockDatabaseConnection.getConnection()).thenReturn(mockConnection);

        // Mock the prepareStatement behavior
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);

        // Initialize ShelfDAO with mocked DatabaseConnection
        shelfDAO = new ShelfDAO();
        shelfDAO.dbConnection = mockDatabaseConnection;
    }

    @Test
    void testCreateShelf() throws SQLException {
        // Create a sample Shelf object to insert
        Shelf shelf = new Shelf(
                UUID.randomUUID(), // shelf_id
                50,               // available_stock
                "Science Fiction", // book_category
                5,                // borrowed_number
                55,               // initial_stock
                UUID.randomUUID()  // room_id
        );

        // Test that createShelf does not throw an exception
        assertDoesNotThrow(() -> shelfDAO.createShelf(shelf));

        // Verify that prepareStatement was called with the correct SQL
        verify(mockConnection).prepareStatement("INSERT INTO Shelf (shelf_id, available_stock, book_category,borrowed_number,initial_stock,room_id) VALUES (?, ?, ?, ?, ?, ?)");

        // Verify if the prepared statement parameters were set correctly
        verify(mockPreparedStatement).setObject(1, shelf.getShelfId());
        verify(mockPreparedStatement).setInt(2, shelf.getAvailableStock());
        verify(mockPreparedStatement).setString(3, shelf.getBookCategory());
        verify(mockPreparedStatement).setInt(4, shelf.getBorrowedNumber());
        verify(mockPreparedStatement).setInt(5, shelf.getInitialStock());
        verify(mockPreparedStatement).setObject(6, shelf.getRoomId());

        // Verify if the executeUpdate method was called once
        verify(mockPreparedStatement, times(1)).executeUpdate();
    }
}
