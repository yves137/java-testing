import org.aucalibray.aucaenum.BookStatus;
import org.aucalibray.dao.BookDAO;
import org.aucalibray.dao.DatabaseConnection;
import org.aucalibray.dao.ShelfDAO;
import org.aucalibray.model.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class BookDAOTest {

    private BookDAO bookDAO;
    private PreparedStatement mockPreparedStatement;
    private Connection mockConnection;
    private ShelfDAO mockShelfDAO;

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

        // Mock ShelfDAO
        mockShelfDAO = mock(ShelfDAO.class);

        // Initialize BookDAO with mocked DatabaseConnection
        bookDAO = new BookDAO();
        bookDAO.dbConnection = mockDatabaseConnection;
        bookDAO.shelfDAO = mockShelfDAO;
    }

    @Test
    void testCreateBook() throws SQLException {
        // Create a sample Book object
        Book book = new Book(
                UUID.randomUUID(),               // book_id
                BookStatus.AVAILABLE,            // Book_status
                1,                               // edition
                "978-3-16-148410-0",             // ISBNCode
                new java.util.Date(),            // publication_year
                "Publisher Name",                // publisher_name
                "Sample Book Title",             // title
                UUID.randomUUID()                // shelf_id
        );

        // Mock shelfDAO.increaseStockAvailable to return true
        when(mockShelfDAO.increaseStockAvailable(book.getShelfId(), 1)).thenReturn(true);

        // Test that createBook does not throw an exception
        assertDoesNotThrow(() -> bookDAO.createBook(book));

        // Verify that prepareStatement was called with the correct SQL
        verify(mockConnection).prepareStatement("INSERT INTO Book (book_id,Book_status,edition,ISBNCode,publication_year,publisher_name,title,shelf_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");

        // Verify if the prepared statement parameters were set correctly
        verify(mockPreparedStatement).setObject(1, book.getBookId());
        verify(mockPreparedStatement).setString(2, book.getBookStatus().toString());
        verify(mockPreparedStatement).setInt(3, book.getEdition());
        verify(mockPreparedStatement).setString(4, book.getISBNCode());
        verify(mockPreparedStatement).setDate(5, new Date(book.getPublicationYear().getTime()));
        verify(mockPreparedStatement).setString(6, book.getPublisherName());
        verify(mockPreparedStatement).setString(7, book.getTitle());
        verify(mockPreparedStatement).setObject(8, book.getShelfId());

        // Verify if the executeUpdate method was called once
        verify(mockPreparedStatement, times(1)).executeUpdate();
    }

    @Test
    void testCreateBook_ShelfAdditionFails() throws SQLException {
        // Create a sample Book object
        Book book = new Book(
                UUID.randomUUID(),               // book_id
                BookStatus.AVAILABLE,            // Book_status
                1,                               // edition
                "978-3-16-148410-0",             // ISBNCode
                new java.util.Date(),            // publication_year
                "Publisher Name",                // publisher_name
                "Sample Book Title",             // title
                UUID.randomUUID()                // shelf_id
        );

        // Mock shelfDAO.increaseStockAvailable to return false, simulating a failure
        when(mockShelfDAO.increaseStockAvailable(book.getShelfId(), 1)).thenReturn(false);

        // Test that createBook throws a RuntimeException due to failed shelf addition
        assertThrows(RuntimeException.class, () -> bookDAO.createBook(book));

        // Verify that executeUpdate is never called since shelf addition fails
        verify(mockPreparedStatement, times(0)).executeUpdate();

        // Verify if shelfDAO.increaseStockAvailable was called once
        verify(mockShelfDAO, times(1)).increaseStockAvailable(book.getShelfId(), 1);
    }
}
