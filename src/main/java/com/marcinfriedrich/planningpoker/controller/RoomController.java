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
    public ResponseEntity<?> getAnswers(@PathVariable String key) {
        Room room = roomCacheManager.getRoom(key);
        List<UserFullResponse> users = room.getUsers().stream().map(UserFullResponse::new).collect(Collectors.toList());
        this.template.convertAndSend("/room/" + key + "/reveal", users);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/{key}/clean")
    public ResponseEntity<?> clean(@PathVariable String key) {
        roomCacheManager.clean(key);
        Room room = roomCacheManager.getRoom(key);
        List<UserFullResponse> users = room.getUsers().stream().map(UserFullResponse::new).collect(Collectors.toList());
        this.template.convertAndSend("/room/" + key + "/reveal", users);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/{key}/{name}")
    public KeyResponse joinRoom(@PathVariable String key, @PathVariable String name) {
        User user = new User(name);
        Room room = roomCacheManager.joinRoom(key, user);
        List<UserAnswerResponse> users = room.getUsers().stream().map(UserAnswerResponse::new).collect(Collectors.toList());
        this.template.convertAndSend("/room/" + key, users);
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
        String roomKey = answerRequest.getRoomKey();
        roomCacheManager.setUserSize(roomKey, answerRequest.getUserKey(), answerRequest.getSize());
        Room room = roomCacheManager.getRoom(roomKey);
        List<UserAnswerResponse> users = room.getUsers().stream().map(UserAnswerResponse::new).collect(Collectors.toList());
        this.template.convertAndSend("/room/" + roomKey, users);
        return ResponseEntity.ok(HttpStatus.OK);
    }

//    @DeleteMapping("/{key}/user/{name}")
//    public void removeUserFromRoom(@PathVariable String key, @PathVariable String name) {
//        roomCacheManager.removeUserFromRoom(key, name);
//    }

//    @GetMapping("/createRoom/{name}")
//    public String addUserToRoom(@PathVariable String name) {
//        return roomCacheManager.createRoom(name);
//    }

//    @DeleteMapping("/{key}/owner/{name}")
//    public void deleteRoom(@PathVariable String key, @PathVariable String name) {
//        roomCacheManager.deleteRoom(key, name);
//    }

//    @GetMapping("/createRoom/{name}")
//    public RedirectView addUserToRoom(@PathVariable String name) {
//        roomCacheManager.createRoom(name);
//        return new RedirectView("http://localhost:4200/room");
//    }

}