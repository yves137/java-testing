package org.aucalibray.model;

import org.aucalibray.aucaenum.Gender;
import org.aucalibray.aucaenum.RoleType;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

public class User extends Person {
    private String password;
    private RoleType role;
    private String userName;
    private UUID villageId;

    public User() {
    }

    public User(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public User(String personId,String password, RoleType role, String userName, UUID villageId) {
        super(personId);
        this.password = password;
        this.role = role;
        this.userName = userName;
        this.villageId = villageId;
    }

    public User(String personId, String firstName, String lastName, Gender gender, String phoneNumber, String password, RoleType role, String userName, UUID villageId) {
        super(personId,firstName, lastName, gender, phoneNumber);
        this.password = password;
        this.role = role;
        this.userName = userName;
        this.villageId = villageId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public RoleType getRole() {
        return role;
    }

    public void setRole(RoleType role) {
        this.role = role;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public UUID getVillageId() {
        return villageId;
    }

    public void setVillageId(UUID villageId) {
        this.villageId = villageId;
    }

    public void hashPassword() {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = messageDigest.digest(this.password.getBytes());

            // Convert byte array into a hexadecimal string
            StringBuilder sb = new StringBuilder();
            for (byte b : hashedBytes) {
                sb.append(String.format("%02x", b));
            }
            this.password= sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }
}
