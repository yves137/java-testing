package org.aucalibray.dao;

import org.aucalibray.aucaenum.MembershipLevel;
import org.aucalibray.model.MembershipType;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class MembershipTypeDAO {
    public DatabaseConnection dbConnection;
    public MembershipTypeDAO() {
        try {
            this.dbConnection = DatabaseConnection.getInstance();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public MembershipType getMembershipTypeById(UUID membershipTypeId){
        String selectMembershipTypeSQL = "SELECT * FROM MembershipType WHERE membership_type_id = ?";
        try {
            Connection connection = dbConnection.getConnection();
            var preparedStatement = connection.prepareStatement(selectMembershipTypeSQL);
            preparedStatement.setObject(1, membershipTypeId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                return new MembershipType(
                        UUID.fromString(resultSet.getString("membership_type_id")),
                        MembershipLevel.valueOf(resultSet.getString("membership_name"))
                );
            }
            return null;
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }


}
