package org.aucalibray.dao;

import org.aucalibray.model.Membership;
import org.aucalibray.model.MembershipType;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.UUID;

public class MembershipDAO {

    public DatabaseConnection dbConnection;
    public  MembershipTypeDAO membershipTypeDAO;
    public MembershipDAO() {
        try {
            this.dbConnection = DatabaseConnection.getInstance();
            this.membershipTypeDAO = new MembershipTypeDAO();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void registerMembership(Membership membership){
        String insertMembershipSQL = "INSERT INTO Membership (membership_id, expiring_time, membership_code, membership_type_id, membership_status, reader_id, registration_date) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try {
            Connection connection = dbConnection.getConnection();
            var preparedStatement = connection.prepareStatement(insertMembershipSQL);
            preparedStatement.setObject(1, membership.getMembershipId());
            preparedStatement.setDate(2, new Date(membership.getExpiringTime().getTime()));
            preparedStatement.setString(3, membership.getMembershipCode());
            preparedStatement.setObject(4, membership.getMembershipTypeId());
            preparedStatement.setString(5, membership.getMembershipStatus().toString());
            preparedStatement.setString(6, membership.getReaderId());
            preparedStatement.setDate(7, new Date(membership.getRegistrationDate().getTime()));
            preparedStatement.executeUpdate();
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public MembershipType getMembershipTypeByReaderId(String readerId){
        String membershipSQL = "SELECT * FROM Membership WHERE reader_id = ?";
        try {
            Connection connection = dbConnection.getConnection();
            var preparedStatement = connection.prepareStatement(membershipSQL);
            preparedStatement.setString(1, readerId);
            var resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                return membershipTypeDAO.getMembershipTypeById(UUID.fromString(resultSet.getObject("membership_type_id").toString()));
            }
            return null;
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
