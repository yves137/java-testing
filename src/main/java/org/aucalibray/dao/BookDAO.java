package org.aucalibray.dao;

import org.aucalibray.aucaenum.BookStatus;
import org.aucalibray.model.Book;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.UUID;

public class BookDAO {
    public DatabaseConnection dbConnection;
    public ShelfDAO shelfDAO;
    public BookDAO() {
        try {
            this.dbConnection = DatabaseConnection.getInstance();
            this.shelfDAO = new ShelfDAO();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void createBook(Book book){
        String insertBookSQL = "INSERT INTO Book (book_id,Book_status,edition,ISBNCode,publication_year,publisher_name,title,shelf_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            boolean isAddedToShelf=shelfDAO.increaseStockAvailable(book.getShelfId(), 1);
            if(!isAddedToShelf){
                throw new RuntimeException("The book is not added to the shelf");
            }
            Connection connection = dbConnection.getConnection();
            var preparedStatement = connection.prepareStatement(insertBookSQL);
            preparedStatement.setObject(1, book.getBookId());
            preparedStatement.setString(2, book.getBookStatus().toString());
            preparedStatement.setInt(3, book.getEdition());
            preparedStatement.setString(4, book.getISBNCode());
            preparedStatement.setDate(5, new Date(book.getPublicationYear().getTime()));
            preparedStatement.setString(6, book.getPublisherName());
            preparedStatement.setString(7, book.getTitle());
            preparedStatement.setObject(8, book.getShelfId());
            preparedStatement.executeUpdate();
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public Book getBookById(UUID bookId){
        String selectBookSQL = "SELECT * FROM Book WHERE book_id = ?";
        try {
            Connection connection = dbConnection.getConnection();
            var preparedStatement = connection.prepareStatement(selectBookSQL);
            preparedStatement.setObject(1, bookId);
            var resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                return new Book(
                        UUID.fromString(resultSet.getObject("book_id", UUID.class).toString()),
                        BookStatus.valueOf(resultSet.getString("Book_status")),
                        resultSet.getInt("edition"),
                        resultSet.getString("ISBNCode"),
                        new java.util.Date(resultSet.getDate("publication_year").getTime()),
                        resultSet.getString("publisher_name"),
                        resultSet.getString("title"),
                        UUID.fromString(resultSet.getObject("shelf_id", UUID.class).toString())
                );
            }
            return null;
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public boolean removeBookFromShelf(UUID bookId){
        Book book = getBookById(bookId);
        return shelfDAO.removeStock(book.getShelfId(), 1);
    }
}
