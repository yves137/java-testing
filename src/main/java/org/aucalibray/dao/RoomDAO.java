package org.aucalibray.dao;

import org.aucalibray.model.Room;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.UUID;

public class RoomDAO {
    public DatabaseConnection dbConnection;
    public ShelfDAO shelfDAO;
    public RoomDAO() {
        try {
            this.dbConnection = DatabaseConnection.getInstance();
            this.shelfDAO = new ShelfDAO();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void createRoom(Room room){
        String insertRoomSQL = "INSERT INTO Room (room_id, room_code) VALUES (?, ?)";
        try {
            Connection connection = dbConnection.getConnection();
            var preparedStatement = connection.prepareStatement(insertRoomSQL);
            preparedStatement.setObject(1, room.getRoomId());
            preparedStatement.setString(2, room.getRoomCode());
            preparedStatement.executeUpdate();
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public int totalNumberBooksInRoom(UUID roomId){
        return shelfDAO.calculateAvailableStockByRoomId(roomId);
    }

    public Room getRoomById(UUID roomId){
        String selectRoomSQL = "SELECT * FROM Room WHERE room_id = ?";
        try {
            Connection connection = dbConnection.getConnection();
            var preparedStatement = connection.prepareStatement(selectRoomSQL);
            preparedStatement.setObject(1, roomId);
            var resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                return new Room(UUID.fromString(resultSet.getString("room_id")), resultSet.getString("room_code"));
            }
            return null;
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public Room getRoomWithFewestBook(){
        var roomId= shelfDAO.getRoomWithFewStock();
        if(roomId==null){
            return null;
        }
        return getRoomById(roomId);
    }
}
