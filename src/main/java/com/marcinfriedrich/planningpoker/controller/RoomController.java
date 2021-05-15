package com.marcinfriedrich.planningpoker.controller;

import com.marcinfriedrich.planningpoker.cache.RoomCacheManager;
import com.marcinfriedrich.planningpoker.exception.ErrorResponse;
import com.marcinfriedrich.planningpoker.exception.JsonExceptionHandler;
import com.marcinfriedrich.planningpoker.model.Room;
import com.marcinfriedrich.planningpoker.model.User;
import com.marcinfriedrich.planningpoker.payload.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class RoomController {

    private final SimpMessagingTemplate template;
    private final RoomCacheManager roomCacheManager;

    public RoomController(SimpMessagingTemplate template) {
        this.template = template;
        this.roomCacheManager = RoomCacheManager.getInstance();
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/{key}/users")
    public List<UserAnswerResponse> getUsersFromRoom(@PathVariable String key) {
        Room room = roomCacheManager.getRoom(key);

        return room.getUsers().stream()
                .map(UserAnswerResponse::new)
                .collect(Collectors.toList());
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/{key}/reveal")
    public ResponseEntity<?> getAnswers(@PathVariable String key) {
        Room room = roomCacheManager.getRoom(key);
        List<UserFullResponse> users = room.getUsers().stream()
                .map(UserFullResponse::new)
                .collect(Collectors.toList());
        template.convertAndSend("/room/" + key + "/reveal", users);

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/{key}/clean")
    public ResponseEntity<?> clean(@PathVariable String key) {
        roomCacheManager.clean(key);
        Room room = roomCacheManager.getRoom(key);
        List<UserFullResponse> users = room.getUsers()
                .stream()
                .map(UserFullResponse::new)
                .collect(Collectors.toList());
        template.convertAndSend("/room/" + key + "/reveal", users);

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/{key}/{name}/{isObserver}")
    public ResponseEntity<?> joinRoom(@PathVariable String key, @PathVariable String name, @PathVariable Boolean isObserver) {
        User user = new User(name);
        user.setObserver(isObserver);
        Room room;

        try {
            room = roomCacheManager.joinRoom(key, user);
        } catch (Exception e) {
            return new JsonExceptionHandler().handleAllOtherErrors(e);
        }

        List<UserAnswerResponse> users = room.getUsers().stream()
                .map(UserAnswerResponse::new)
                .collect(Collectors.toList());
        template.convertAndSend("/room/" + key, users);

        return new ResponseEntity<>(new KeyResponse(key, user.getKey(), room.getName(),
                name, isObserver, room.getRoomType()), HttpStatus.OK);
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/createRoomWithOwner")
    public KeyResponse createRoom(@RequestBody CreateRoomRequest roomRequest) {
        return roomCacheManager.createRoom(
                roomRequest.getRoomName(),
                roomRequest.getUserName(),
                roomRequest.getRoomType());
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/answer")
    public ResponseEntity<?> answer(@RequestBody AnswerRequest answerRequest) {
        String roomKey = answerRequest.getRoomKey();
        roomCacheManager.setUserSize(roomKey, answerRequest.getUserKey(), answerRequest.getSize());
        Room room = roomCacheManager.getRoom(roomKey);
        List<UserAnswerResponse> users = room.getUsers().stream()
                .map(UserAnswerResponse::new)
                .collect(Collectors.toList());
        template.convertAndSend("/room/" + roomKey, users);

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/leaveRoom")
    public ResponseEntity<?> leaveRoom(@RequestBody RoomAndUserRequest roomAndUserRequest) {
        String roomKey = roomAndUserRequest.getRoomKey();
        Room room = roomCacheManager.leaveRoom(roomKey, roomAndUserRequest.getUserKey());
        List<UserAnswerResponse> users = room.getUsers().stream()
                .map(UserAnswerResponse::new)
                .collect(Collectors.toList());

        if (users.isEmpty()) {
            roomCacheManager.deleteRoom(roomKey);
        } else {
            this.template.convertAndSend("/room/" + roomKey, users);
        }

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/deleteUserFromRoom")
    public ResponseEntity<?> deleteUserFromRoom(@RequestBody DeleteUserFromRoom deleteUserFromRoom) {
        String roomKey = deleteUserFromRoom.getRoomKey();
        Room room = roomCacheManager.getRoom(roomKey);

        boolean isAllowed = room.getUsers().stream()
                .filter(u -> deleteUserFromRoom.getUserKey().equals(u.getKey()))
                .findFirst()
                .orElse(null) != null;

        if (!isAllowed) {
            return new JsonExceptionHandler().handleAllOtherErrors(new Exception("Nie można usunąć tego użytkownika."));
        }

        User userToDelete = room.getUserByName(deleteUserFromRoom.getUserNameToDelete());
        roomCacheManager.leaveRoom(roomKey, userToDelete.getKey());

        List<UserAnswerResponse> users = room.getUsers().stream()
                .map(UserAnswerResponse::new)
                .collect(Collectors.toList());

        this.template.convertAndSend("/room/" + roomKey, users);
        this.template.convertAndSend("/room/" + roomKey + "/" + userToDelete.getName(), userToDelete.getName());

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @Scheduled(cron = "0 0 16 * * MON-FRI")
    public void removeUnusedRooms() {
        List<Room> rooms = roomCacheManager.getRooms();
        LocalDate now = LocalDate.now().plusDays(1);
        rooms = rooms.stream()
                .filter(room -> room.getCreatedAt().isAfter(now))
                .collect(Collectors.toList());
        rooms.forEach(room -> roomCacheManager.deleteRoom(room.getKey()));
    }

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
