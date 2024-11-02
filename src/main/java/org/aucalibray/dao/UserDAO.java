package org.aucalibray.dao;

import org.aucalibray.aucaenum.RoleType;
import org.aucalibray.model.Location;
import org.aucalibray.model.User;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.UUID;

public class UserDAO {
    public DatabaseConnection dbConnection;
    public LocationDAO locationDAO;
    public UserDAO() {
        try {
            this.dbConnection = DatabaseConnection.getInstance();
            this.locationDAO = new LocationDAO();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void createUser(User user){

        String insertUserSQL = "INSERT INTO User (person_id, password, role, user_name, village_id) VALUES (?, ?, ?, ?, ?)";
        try {
            Connection connection = dbConnection.getConnection();
            var preparedStatement = connection.prepareStatement(insertUserSQL);
            preparedStatement.setString(1, user.getPersonId());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getRole().toString());
            preparedStatement.setString(4, user.getUserName());
            preparedStatement.setObject(5, user.getVillageId());
            preparedStatement.executeUpdate();
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    //get user get user's Province
    public Location getUserProvince(String userId){
        User user = getUserById(userId);
        LocationDAO locationDAO = new LocationDAO();
        return locationDAO.findProvince(locationDAO.findLocationById(user.getVillageId()));
    }

    //get user by Id
    public User getUserById(String userId){
        String selectUserSQL = "SELECT * FROM User WHERE person_id = ?";
        try {
            Connection connection = dbConnection.getConnection();
            var preparedStatement = connection.prepareStatement(selectUserSQL);
            preparedStatement.setString(1, userId);
            var resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                return new User(
                        resultSet.getString("person_id"),
                        resultSet.getString("password"),
                        RoleType.valueOf(resultSet.getString("role")),
                        resultSet.getString("user_name"),
                        UUID.fromString(resultSet.getObject("village_id").toString())
                );
        }
    }catch (Exception e){
        throw new RuntimeException(e);
    }
        return null;
    }

    //get user by username
    public User getUserByUserName(String userName){
        String selectUserSQL = "SELECT * FROM User WHERE user_name = ?";
        try{
            Connection connection = dbConnection.getConnection();
            var preparedStatement = connection.prepareStatement(selectUserSQL);
            preparedStatement.setString(1, userName);
            var resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                return new User(
                        resultSet.getString("person_id"),
                        resultSet.getString("password"),
                        RoleType.valueOf(resultSet.getString("role")),
                        resultSet.getString("user_name"),
                        UUID.fromString(resultSet.getObject("village_id").toString())
                );
            }
        }catch (Exception e){
            throw new RuntimeException(e);
        }
        return null;
    }

    // method to authenticate user by username and password
    public boolean authenticateUser(String userName, String passwordHashed){
        User user = getUserByUserName(userName);
        if (user != null){
            return user.getPassword().equals(passwordHashed);
        }
        return false;
    }
}
