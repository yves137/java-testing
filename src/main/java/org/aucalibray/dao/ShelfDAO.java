package org.aucalibray.dao;

import org.aucalibray.model.Shelf;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.UUID;

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

    public boolean increaseStockAvailable(UUID shelfId, int stock){
        String updateStockSQL = "UPDATE Shelf SET available_stock = available_stock + ? WHERE shelf_id = ?";
        try {
            Connection connection = dbConnection.getConnection();
            var preparedStatement = connection.prepareStatement(updateStockSQL);
            preparedStatement.setInt(1, stock);
            preparedStatement.setObject(2, shelfId);
            preparedStatement.executeUpdate();
            return true;
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public int calculateAvailableStockByRoomId(UUID roomId){
        String selectStockSQL = "SELECT SUM(available_stock) FROM Shelf WHERE room_id = ?";
        try {
            Connection connection = dbConnection.getConnection();
            var preparedStatement = connection.prepareStatement(selectStockSQL);
            preparedStatement.setObject(1, roomId);
            var resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                return resultSet.getInt(1);
            }
            return 0;
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public UUID getRoomWithFewStock(){
        String selectRoomSQL = "SELECT room_id FROM Shelf GROUP BY room_id ORDER BY SUM(available_stock) ASC LIMIT 1";
        try {
            Connection connection = dbConnection.getConnection();
            var preparedStatement = connection.prepareStatement(selectRoomSQL);
            var resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                return UUID.fromString(resultSet.getObject(1).toString()) ;
            }
            return null;
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public boolean removeStock(UUID shelfId, int stock){
        String updateStockSQL = "UPDATE Shelf SET available_stock = available_stock - ? WHERE shelf_id = ?";
        try{
            Connection connection = dbConnection.getConnection();
            var preparedStatement = connection.prepareStatement(updateStockSQL);
            preparedStatement.setInt(1, stock);
            preparedStatement.setObject(2, shelfId);
            preparedStatement.executeUpdate();
            return true;
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
