package org.aucalibray.dao;

import org.aucalibray.model.Borrower;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;

public class BorrowerDAO {
    public DatabaseConnection dbConnection;
    public MembershipDAO membershipDAO;
    public BorrowerDAO() {
        try {
            this.dbConnection = DatabaseConnection.getInstance();
            this.membershipDAO = new MembershipDAO();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void borrowerBook(Borrower borrower){
        String insertBorrowerSQL = "INSERT INTO Borrower (id,book_id,due_date,fine,late_charge_fees,pickup_date,reader_id,return_date) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            Connection connection = dbConnection.getConnection();
            var preparedStatement = connection.prepareStatement(insertBorrowerSQL);
            preparedStatement.setObject(1, borrower.getId());
            preparedStatement.setObject(2, borrower.getBookId());
            preparedStatement.setDate(3, new Date(borrower.getDueDate().getTime()));
            preparedStatement.setInt(4, borrower.getFine());
            preparedStatement.setInt(5, borrower.getLateChargeFees());
            preparedStatement.setDate(6, new Date(borrower.getPickupDate().getTime()));
            preparedStatement.setString(7, borrower.getReaderId());
            preparedStatement.setDate(8, new Date(borrower.getReturnDate().getTime()));
            preparedStatement.executeUpdate();
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    // the method to validate if the user does not borrow more books than his/her membership books
    public boolean validateBorrower(String readerId,int numberOfBooks){
        var readerMembershipType= membershipDAO.getMembershipTypeByReaderId(readerId);
        return readerMembershipType.getMaxBooks() >= numberOfBooks;
    }
}