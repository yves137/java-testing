package org.aucalibray.model;

import java.util.UUID;

public class Shelf {
    private int availableStock;
    private String bookCategory;
    private int borrowedNumber;
    private int initialStock;
    private UUID roomId;

    public Shelf() {
    }

    public Shelf(int availableStock, String bookCategory, int borrowedNumber, int initialStock, UUID roomId) {
        this.availableStock = availableStock;
        this.bookCategory = bookCategory;
        this.borrowedNumber = borrowedNumber;
        this.initialStock = initialStock;
        this.roomId = roomId;
    }

    public int getAvailableStock() {
        return availableStock;
    }

    public void setAvailableStock(int availableStock) {
        this.availableStock = availableStock;
    }

    public String getBookCategory() {
        return bookCategory;
    }

    public void setBookCategory(String bookCategory) {
        this.bookCategory = bookCategory;
    }

    public int getBorrowedNumber() {
        return borrowedNumber;
    }

    public void setBorrowedNumber(int borrowedNumber) {
        this.borrowedNumber = borrowedNumber;
    }

    public int getInitialStock() {
        return initialStock;
    }

    public void setInitialStock(int initialStock) {
        this.initialStock = initialStock;
    }

    public UUID getRoomId() {
        return roomId;
    }

    public void setRoomId(UUID roomId) {
        this.roomId = roomId;
    }
}

