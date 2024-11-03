package org.aucalibray.dao;

import org.aucalibray.model.Shelf;

import java.sql.Connection;
import java.sql.SQLException;

public class ShelfDAO {
    public DatabaseConnection dbConnection;
    public ShelfDAO() {
        try {
            this.dbConnection = DatabaseConnection.getInstance();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void createShelf(Shelf shelf){
        String insertShelfSQL = "INSERT INTO Shelf (shelf_id, available_stock, book_category,borrowed_number,initial_stock,room_id) VALUES (?, ?, ?, ?, ?, ?)";
        try {
            Connection connection = dbConnection.getConnection();
            var preparedStatement = connection.prepareStatement(insertShelfSQL);
            preparedStatement.setObject(1, shelf.getShelfId());
            preparedStatement.setInt(2, shelf.getAvailableStock());
            preparedStatement.setString(3, shelf.getBookCategory());
            preparedStatement.setInt(4, shelf.getBorrowedNumber());
            preparedStatement.setInt(5, shelf.getInitialStock());
            preparedStatement.setObject(6, shelf.getRoomId());
            preparedStatement.executeUpdate();
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
