package org.aucalibray.model;

import java.util.UUID;

public class Room {
    private String roomCode;
    private UUID roomId;

    public Room() {
    }

    public Room(String roomCode) {
        this.roomCode = roomCode;
        this.roomId = UUID.randomUUID();
    }

    public Room(UUID roomId,String roomCode) {
        this.roomCode = roomCode;
        this.roomId = roomId;
    }

    public String getRoomCode() {
        return roomCode;
    }

    public void setRoomCode(String roomCode) {
        this.roomCode = roomCode;
    }

    public UUID getRoomId() {
        return roomId;
    }

    public void setRoomId(UUID roomId) {
        this.roomId = roomId;
    }
}

