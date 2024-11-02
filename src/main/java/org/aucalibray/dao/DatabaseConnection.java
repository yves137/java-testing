package org.aucalibray.dao;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {
    private static DatabaseConnection instance;
    private Connection connection;

    private DatabaseConnection() throws SQLException {
            String url = "jdbc:sqlite:library.db";
        try {
            // Load the SQLite JDBC driver (you might need to add it to your dependencies)
            Class.forName("org.sqlite.JDBC");

            // Check if the database file exists
            File dbFile = new File("library.db");
            boolean dbExists = dbFile.exists();

            // Establish the database connection
            this.connection = DriverManager.getConnection(url);

            // If the database did not exist, create necessary tables
            if (!dbExists) {
                initializeDatabase();
            }
        } catch (ClassNotFoundException ex) {
            System.out.println("SQLite JDBC driver not found.");
            ex.printStackTrace();
        }
    }

    public static DatabaseConnection getInstance() throws SQLException {
        if (instance == null) {
            instance = new DatabaseConnection();
        } else if (instance.getConnection().isClosed()) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }

    // Method to initialize the database tables if necessary
    private void initializeDatabase() {
        try (Statement stmt = connection.createStatement()) {
            // Location table
            String createLocationTable = """
                CREATE TABLE IF NOT EXISTS Location (
                    location_id UUID PRIMARY KEY,
                    location_code VARCHAR(50),
                    location_name VARCHAR(100),
                    Location_Type TEXT,
                    parent_id UUID,
                    FOREIGN KEY (parent_id) REFERENCES Location(location_id)
                );
            """;

            // Person table
            String createPersonTable = """
                CREATE TABLE IF NOT EXISTS Person (
                    person_id UUID PRIMARY KEY,
                    first_name VARCHAR(100),
                    last_name VARCHAR(100),
                    gender VARCHAR(10),
                    phone_number VARCHAR(15)
                );
            """;

            // User table
            String createUserTable = """
                CREATE TABLE IF NOT EXISTS User (
                    person_id UUID PRIMARY KEY,
                    password VARCHAR(100),
                    role TEXT,
                    user_name VARCHAR(50),
                    village_id UUID,
                    FOREIGN KEY(person_id) REFERENCES Person(person_id),
                    FOREIGN KEY(village_id) REFERENCES Location(location_id)
                );
            """;

            // Room table
            String createRoomTable = """
                CREATE TABLE IF NOT EXISTS Room (
                    room_id UUID PRIMARY KEY,
                    room_code VARCHAR(50)
                );
            """;

            // Shelf table
            String createShelfTable = """
                CREATE TABLE IF NOT EXISTS Shelf (
                    shelf_id UUID PRIMARY KEY,
                    available_stock INT,
                    book_category VARCHAR(100),
                    borrowed_number INT,
                    initial_stock INT,
                    room_id UUID,
                    FOREIGN KEY(room_id) REFERENCES Room(room_id)
                );
            """;

            // Book table
            String createBookTable = """
                CREATE TABLE IF NOT EXISTS Book (
                    book_id UUID PRIMARY KEY,
                    Book_status TEXT,
                    edition INT,
                    ISBNCode VARCHAR(20),
                    publication_year DATE,
                    publisher_name VARCHAR(100),
                    title VARCHAR(200),
                    shelf_id UUID,
                    FOREIGN KEY(shelf_id) REFERENCES Shelf(shelf_id)
                );
            """;

            // Borrower table
            String createBorrowerTable = """
                CREATE TABLE IF NOT EXISTS Borrower (
                    id UUID PRIMARY KEY,
                    book_id UUID,
                    due_date DATE,
                    fine INT,
                    late_charge_fees INT,
                    pickup_date DATE,
                    reader_id UUID,
                    return_date DATE,
                    FOREIGN KEY(book_id) REFERENCES Book(book_id),
                    FOREIGN KEY(reader_id) REFERENCES User(person_id)
                );
            """;

            // MembershipType table
            String createMembershipTypeTable = """
                CREATE TABLE IF NOT EXISTS MembershipType (
                    membership_type_id UUID PRIMARY KEY,
                    max_books INT,
                    membership_name VARCHAR(50),
                    price INT
                );
            """;

            // Membership table
            String createMembershipTable = """
                CREATE TABLE IF NOT EXISTS Membership (
                    membership_id UUID PRIMARY KEY,
                    expiring_time DATE,
                    membership_code VARCHAR(50),
                    registration_date DATE,
                    membership_status TEXT,
                    membership_type_id UUID,
                    reader_id UUID,
                    FOREIGN KEY(membership_type_id) REFERENCES MembershipType(membership_type_id),
                    FOREIGN KEY(reader_id) REFERENCES User(person_id)
                );
            """;

            // Execute all table creation statements
            stmt.execute(createLocationTable);
            stmt.execute(createPersonTable);
            stmt.execute(createUserTable);
            stmt.execute(createBorrowerTable);
            stmt.execute(createBookTable);
            stmt.execute(createMembershipTable);
            stmt.execute(createShelfTable);
            stmt.execute(createRoomTable);
            stmt.execute(createMembershipTypeTable);

            System.out.println("Database initialized successfully with required tables.");
        } catch (SQLException e) {
            System.out.println("Error initializing database.");
            e.printStackTrace();
        }
    }
}