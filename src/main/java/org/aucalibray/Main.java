package org.aucalibray;

import org.aucalibray.dao.DatabaseConnection;
import org.aucalibray.dao.LocationDAO;
import org.aucalibray.dao.PersonDAO;
import org.aucalibray.dao.UserDAO;
import org.aucalibray.model.Room;

import java.sql.SQLException;

// TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {

        System.out.println("Hello and welcome!");
        try {
//            DatabaseConnection dbConnection = DatabaseConnection.getInstance();
//            LocationDAO locationDAO = new LocationDAO();
//            locationDAO.createLocations();
            PersonDAO personDAO = new PersonDAO();
//            personDAO.createPerson();
            String provinceName = personDAO.getPersonProvinceName("USR-01");
            System.out.println(provinceName);
            UserDAO userDAO = new UserDAO();
            boolean isAuthorised=userDAO.authenticateUser("johndoe", "rawPassword");
            System.out.println(isAuthorised);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}