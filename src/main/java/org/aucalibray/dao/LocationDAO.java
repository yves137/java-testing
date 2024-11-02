package org.aucalibray.dao;

import org.aucalibray.aucaenum.LocationType;
import org.aucalibray.model.Location;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.UUID;

public class LocationDAO {
    public DatabaseConnection dbConnection;
    public LocationDAO() {
        try {
            this.dbConnection = DatabaseConnection.getInstance();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void insertLocation(Location location){
        String insertSQL = "INSERT INTO Location (location_id, location_code, location_name, Location_Type, parent_id) VALUES (?, ?, ?, ?, ?)";
        try {
            Connection connection = dbConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
            preparedStatement.setObject(1, location.getLocationId());
            preparedStatement.setString(2, location.getLocationCode());
            preparedStatement.setString(3, location.getLocationName());
            preparedStatement.setString(4, location.getLocationType().toString());
            preparedStatement.setObject(5, location.getParentId());
            preparedStatement.executeUpdate();

        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public void createLocations(){
        Location Province = new Location("PRV-01", "Kigali", LocationType.PROVINCE, null);
        Location District = new Location("DIS-01", "Gasabo", LocationType.DISTRICT, Province.getLocationId());
        Location Sector = new Location("SEC-01", "Kimironko", LocationType.SECTOR, District.getLocationId());
        Location Cell = new Location("CEL-01", "Kibagabaga", LocationType.CELL, Sector.getLocationId());
        Location Village = new Location("VIL-01", "Karisimbi", LocationType.VILLAGE, Cell.getLocationId());

        insertLocation(Province);
        insertLocation(District);
        insertLocation(Sector);
        insertLocation(Cell);
        insertLocation(Village);
    }

    // Method to find the province of a given village
    public Location findProvince(Location location) {
        Location currentLocation = location;
        while (currentLocation.getLocationType() != LocationType.PROVINCE) {
            currentLocation = findLocationById(currentLocation.getParentId());
        }
        return currentLocation;
    }

    //method to find the first village in a location table
    public Location findFirstVillage() {
        String selectSQL = "SELECT * FROM Location WHERE location_type = ?";
        try {
            Connection connection = dbConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(selectSQL);
            preparedStatement.setString(1, LocationType.VILLAGE.toString());
            ResultSet result= preparedStatement.executeQuery();
            if (result.next()) {
                Location location = new Location();
                location.setLocationId(UUID.fromString(result.getObject("location_id").toString()));
                location.setLocationCode(result.getString("location_code"));
                location.setLocationName(result.getString("location_name"));
                location.setLocationType(LocationType.valueOf(result.getString("Location_Type")));
                if(result.getObject("parent_id") != null)
                    location.setParentId(UUID.fromString(result.getObject("parent_id").toString()));
                return location;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    // Method to find a location by its ID
    public Location findLocationById(UUID locationId) {
        String selectSQL = "SELECT * FROM Location WHERE location_id = ?";
        try {
            Connection connection = dbConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(selectSQL);
            preparedStatement.setObject(1, locationId);
            ResultSet result= preparedStatement.executeQuery();
            if (result.next()) {
                Location location = new Location();
                location.setLocationId(UUID.fromString(result.getObject("location_id").toString()));
                location.setLocationCode(result.getString("location_code"));
                location.setLocationName(result.getString("location_name"));
                location.setLocationType(LocationType.valueOf(result.getString("Location_Type")));
                if(result.getObject("parent_id") != null)
                    location.setParentId(UUID.fromString(result.getObject("parent_id").toString()) );
                return location;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
