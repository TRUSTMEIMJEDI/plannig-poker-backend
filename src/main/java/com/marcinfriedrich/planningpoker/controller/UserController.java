package com.marcinfriedrich.planningpoker.controller;

import com.marcinfriedrich.planningpoker.cache.RoomCacheManager;
import com.marcinfriedrich.planningpoker.exception.JsonExceptionHandler;
import com.marcinfriedrich.planningpoker.model.Room;
import com.marcinfriedrich.planningpoker.model.User;
import com.marcinfriedrich.planningpoker.payload.KeyResponse;
import com.marcinfriedrich.planningpoker.payload.UserAnswerResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
public class UserController {

    private final SimpMessagingTemplate template;
    private final RoomCacheManager roomCacheManager;

    public UserController(SimpMessagingTemplate template) {
        this.template = template;
        this.roomCacheManager = RoomCacheManager.getInstance();
    }

    @PutMapping("/{roomKey}/{userKey}/{isObserver}")
    public ResponseEntity<KeyResponse> changeUserType(@PathVariable String roomKey, @PathVariable String userKey, @PathVariable Boolean isObserver) {
        Room room = roomCacheManager.getRoom(roomKey);
        User user = room.getUserByKey(userKey);
        user.setObserver(isObserver);

        if (isObserver) {
            user.setAnswer(false);
            user.setSize(null);
        }

        List<UserAnswerResponse> users = room.getUsers().stream()
                .map(UserAnswerResponse::new)
                .collect(Collectors.toList());
        template.convertAndSend("/room/" + roomKey, users);

        return new ResponseEntity<>(new KeyResponse(roomKey, user.getKey(), room.getName(), user.getName(), isObserver), HttpStatus.OK);
    }

}
