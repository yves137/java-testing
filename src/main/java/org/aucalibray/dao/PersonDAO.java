package org.aucalibray.dao;

import org.aucalibray.model.User;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.UUID;

public class PersonDAO {
    public DatabaseConnection dbConnection;
    public UserDAO userdao;
    public LocationDAO locationDAO;
    public PersonDAO() {
        try {
            this.dbConnection = DatabaseConnection.getInstance();
            this.userdao = new UserDAO();
            this.locationDAO = new LocationDAO();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public User createPerson(User user){

        String insertPersonSQL = "INSERT INTO Person (person_id, first_name, last_name,gender, phone_number) VALUES (?, ?, ?, ?, ?)";
        try {
            UUID villageId = locationDAO.findFirstVillage().getLocationId();
//            Below is the example of user object
//            User user = new User("USR-01","John","Doe", Gender.MALE, "0788888888", "rawPassword", RoleType.STUDENT, "johndoe", villageId);
            user.hashPassword();
            Connection connection = dbConnection.getConnection();
            var preparedStatement = connection.prepareStatement(insertPersonSQL);
            preparedStatement.setString(1, user.getPersonId());
            preparedStatement.setString(2, user.getFirstName());
            preparedStatement.setString(3, user.getLastName());
            preparedStatement.setString(4, user.getGender().toString());
            preparedStatement.setString(5, user.getPhoneNumber());
            preparedStatement.executeUpdate();
            userdao.createUser(user);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
        return user;
    }

    //find person's Province by personId
    public String getPersonProvinceName(String personId){
        return userdao.getUserProvince(personId).getLocationName();
    }
}
