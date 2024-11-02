package org.aucalibray.model;

import org.aucalibray.aucaenum.BookStatus;
import java.util.UUID;
import java.util.Date;

public class Book {
    private UUID bookId;
    private BookStatus bookStatus;
    private int edition;
    private String ISBNCode;
    private Date publicationYear;
    private String publisherName;
    private UUID shelfId;
    private String title;

    public Book() {
    }

    public Book(BookStatus bookStatus, int edition, String ISBNCode, Date publicationYear, String publisherName, UUID shelfId, String title) {
        this.bookStatus = bookStatus;
        this.edition = edition;
        this.ISBNCode = ISBNCode;
        this.publicationYear = publicationYear;
        this.publisherName = publisherName;
        this.shelfId = shelfId;
        this.title = title;
        this.bookId = UUID.randomUUID();
    }

    public UUID getBookId() {
        return bookId;
    }

    public void setBookId(UUID bookId) {
        this.bookId = bookId;
    }

    public BookStatus getBookStatus() {
        return bookStatus;
    }

    public void setBookStatus(BookStatus bookStatus) {
        this.bookStatus = bookStatus;
    }

    public int getEdition() {
        return edition;
    }

    public void setEdition(int edition) {
        this.edition = edition;
    }

    public String getISBNCode() {
        return ISBNCode;
    }

    public void setISBNCode(String ISBNCode) {
        this.ISBNCode = ISBNCode;
    }

    public Date getPublicationYear() {
        return publicationYear;
    }

    public void setPublicationYear(Date publicationYear) {
        this.publicationYear = publicationYear;
    }

    public String getPublisherName() {
        return publisherName;
    }

    public void setPublisherName(String publisherName) {
        this.publisherName = publisherName;
    }

    public UUID getShelfId() {
        return shelfId;
    }

    public void setShelfId(UUID shelfId) {
        this.shelfId = shelfId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}

