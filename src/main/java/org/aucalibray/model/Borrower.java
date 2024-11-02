package org.aucalibray.model;

import java.util.Date;
import java.util.UUID;

public class Borrower {
    private UUID id;
    private UUID bookId;
    private Date dueDate;
    private int fine;
    private int lateChargeFees;
    private Date pickupDate;
    private String readerId;
    private Date returnDate;

    public Borrower() {
    }

    public Borrower(UUID bookId, Date dueDate, int fine, int lateChargeFees, Date pickupDate, String readerId, Date returnDate) {
        this.bookId = bookId;
        this.dueDate = dueDate;
        this.fine = fine;
        this.lateChargeFees = lateChargeFees;
        this.pickupDate = pickupDate;
        this.readerId = readerId;
        this.returnDate = returnDate;
        this.id = UUID.randomUUID();
    }

    public Borrower(UUID id,UUID bookId, Date dueDate, int fine, int lateChargeFees, Date pickupDate, String readerId, Date returnDate) {
        this.bookId = bookId;
        this.dueDate = dueDate;
        this.fine = fine;
        this.lateChargeFees = lateChargeFees;
        this.pickupDate = pickupDate;
        this.readerId = readerId;
        this.returnDate = returnDate;
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getBookId() {
        return bookId;
    }

    public void setBookId(UUID bookId) {
        this.bookId = bookId;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public int getFine() {
        return fine;
    }

    public void setFine(int fine) {
        this.fine = fine;
    }

    public int getLateChargeFees() {
        return lateChargeFees;
    }

    public void setLateChargeFees(int lateChargeFees) {
        this.lateChargeFees = lateChargeFees;
    }

    public Date getPickupDate() {
        return pickupDate;
    }

    public void setPickupDate(Date pickupDate) {
        this.pickupDate = pickupDate;
    }

    public String getReaderId() {
        return readerId;
    }

    public void setReaderId(String readerId) {
        this.readerId = readerId;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }
}
