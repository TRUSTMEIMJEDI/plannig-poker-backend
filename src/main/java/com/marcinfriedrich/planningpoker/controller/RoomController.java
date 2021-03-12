package com.marcinfriedrich.planningpoker.controller;

import com.marcinfriedrich.planningpoker.cache.RoomCacheManager;
import com.marcinfriedrich.planningpoker.model.Room;
import com.marcinfriedrich.planningpoker.model.User;
import com.marcinfriedrich.planningpoker.payload.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class RoomController {

    private RoomCacheManager roomCacheManager;
    private final SimpMessagingTemplate template;

    public RoomController(SimpMessagingTemplate template) {
        this.template = template;
        this.roomCacheManager = RoomCacheManager.getInstance();
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/{key}/users")
    public List<UserAnswerResponse> getUsersFromRoom(@PathVariable String key) {
        Room room = roomCacheManager.getRoom(key);
        return room.getUsers().stream().map(UserAnswerResponse::new).collect(Collectors.toList());
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/{key}/reveal")
    public List<UserFullResponse> getAnswers(@PathVariable String key) {
        Room room = roomCacheManager.getRoom(key);
        return room.getUsers().stream().map(UserFullResponse::new).collect(Collectors.toList());
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/{key}/clean")
    public ResponseEntity<?> clean(@PathVariable String key) {
        roomCacheManager.clean(key);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/{key}/{name}")
    public KeyResponse joinRoom(@PathVariable String key, @PathVariable String name) {
        User user = new User(name);
        Room room = roomCacheManager.joinRoom(key, user);
        return new KeyResponse(key, user.getKey(), room.getName(), name);
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/createRoomWithOwner")
    public KeyResponse createRoom(@RequestBody CreateRoomRequest roomRequest) {
        return roomCacheManager.createRoom(roomRequest.getRoomName(), roomRequest.getUserName());
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/answer")
    public ResponseEntity<?> answer(@RequestBody AnswerRequest answerRequest) {
        roomCacheManager.setUserSize(answerRequest.getRoomKey(), answerRequest.getUserKey(), answerRequest.getSize());
        this.template.convertAndSend("/answer",  true);
        return ResponseEntity.ok(HttpStatus.OK);
    }

}
