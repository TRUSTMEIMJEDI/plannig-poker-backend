package com.marcinfriedrich.planningpoker.controller;

import com.marcinfriedrich.planningpoker.cache.RoomCacheManager;
import com.marcinfriedrich.planningpoker.model.Room;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class AdminController {

    private final RoomCacheManager roomCacheManager;
    private final SimpMessagingTemplate template;

    public AdminController(SimpMessagingTemplate template) {
        this.template = template;
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
            this.template.convertAndSend("/room/" + loginRequest.getRoomKey(), new ArrayList<>());
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
