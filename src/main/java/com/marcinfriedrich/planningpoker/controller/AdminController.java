package com.marcinfriedrich.planningpoker.controller;

import com.marcinfriedrich.planningpoker.cache.RoomCacheManager;
import com.marcinfriedrich.planningpoker.model.Room;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@RestController
public class AdminController {

    private RoomCacheManager roomCacheManager;

    public AdminController() {
        this.roomCacheManager = RoomCacheManager.getInstance();
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        String password = loginRequest.getPassword();
        LocalDate dateNow = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String passwordToMatch = formatter.format(dateNow);
        if (password.equals(passwordToMatch)) {
            return ResponseEntity.ok(true);
        } else {
            return ResponseEntity.ok(false);
        }
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/rooms")
    public ResponseEntity<List<Room>> rooms(@RequestBody LoginRequest loginRequest) {
        String password = loginRequest.getPassword();
        LocalDate dateNow = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String passwordToMatch = formatter.format(dateNow);
        if (password.equals(passwordToMatch)) {
            return ResponseEntity.ok(roomCacheManager.getRooms());
        } else {
            return ResponseEntity.ok(new ArrayList<>());
        }
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/deleteRoom")
    public ResponseEntity<List<Room>> deleteRoom(@RequestBody LoginRequest loginRequest) {
        String password = loginRequest.getPassword();
        LocalDate dateNow = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String passwordToMatch = formatter.format(dateNow);
        if (password.equals(passwordToMatch)) {
            roomCacheManager.deleteRoom(loginRequest.getRoomKey());
            return ResponseEntity.ok(roomCacheManager.getRooms());
        } else {
            return ResponseEntity.ok(new ArrayList<>());
        }
    }

}

class LoginRequest {
    private String password;
    private String roomKey;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRoomKey() {
        return roomKey;
    }

    public void setRoomKey(String roomKey) {
        this.roomKey = roomKey;
    }
}
