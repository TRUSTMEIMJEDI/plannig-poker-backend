package com.marcinfriedrich.planningpoker.web;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.marcinfriedrich.planningpoker.domain.Room;
import com.marcinfriedrich.planningpoker.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
class AdminResource {

    private final RoomRepository roomRepository;
    private final SimpMessagingTemplate template;

    @PostMapping("/login")
    public ResponseEntity<Boolean> login(@RequestBody LoginRequest loginRequest) {
        String password = loginRequest.getPassword();
        LocalDate dateNow = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String passwordToMatch = formatter.format(dateNow);

        return ResponseEntity.ok(password.equals(passwordToMatch));
    }

    @PostMapping("/rooms")
    public ResponseEntity<Collection<Room>> rooms(@RequestBody LoginRequest loginRequest) {
        String password = loginRequest.getPassword();
        LocalDate dateNow = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String passwordToMatch = formatter.format(dateNow);
        if (password.equals(passwordToMatch)) {
            return ResponseEntity.ok(roomRepository.getRooms());
        }
        return ResponseEntity.ok(new ArrayList<>());
    }

    @PostMapping("/deleteRoom")
    public ResponseEntity<Collection<Room>> deleteRoom(@RequestBody LoginRequest loginRequest) {
        String password = loginRequest.getPassword();
        LocalDate dateNow = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String passwordToMatch = formatter.format(dateNow);

        if (password.equals(passwordToMatch)) {
            roomRepository.deleteRoomById(loginRequest.getRoomKey());
            this.template.convertAndSend("/room/" + loginRequest.getRoomKey(), new ArrayList<>());
            return ResponseEntity.ok(roomRepository.getRooms());
        }
        return ResponseEntity.ok(new ArrayList<>());
    }

    @Value
    private static class LoginRequest {
        @JsonProperty
        String password;

        @JsonProperty
        String roomKey;
    }

}
