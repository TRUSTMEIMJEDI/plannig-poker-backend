package com.marcinfriedrich.planningpoker.web;

import com.marcinfriedrich.planningpoker.exception.NameIsTakenException;
import com.marcinfriedrich.planningpoker.web.request.ChangePasswordRequest;
import com.marcinfriedrich.planningpoker.web.response.KeyResponse;
import com.marcinfriedrich.planningpoker.service.RoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
class UserResource {

    private final RoomService roomService;

    @PutMapping("/{roomKey}/{userKey}/{isObserver}")
    public ResponseEntity<KeyResponse> changeUserType(@PathVariable(name = "roomKey") String roomId,
                                                      @PathVariable(name = "userKey") String userId,
                                                      @PathVariable boolean isObserver) {
        final var room = roomService.changeUserType(roomId, userId, isObserver);

        return new ResponseEntity<>(KeyResponse.builder()
                .roomId(room.getId())
                .roomName(room.getName())
                .roomType(room.getRoomType())
                .userId(userId)
                .userName(room.getUserByKey(userId).getName())
                .observer(isObserver)
                .build(), HttpStatus.OK);
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/change-username")
    public ResponseEntity<KeyResponse> changeUsername(@RequestBody ChangePasswordRequest changePasswordRequest) throws NameIsTakenException {
        final var userId = changePasswordRequest.getUserKey();
        final var newUserName = changePasswordRequest.getNewUsername();
        final var room = roomService.changeUsername(changePasswordRequest.getRoomKey(), userId, newUserName);

        log.info("User with id {} changed his name to {} in room with key {}", userId, newUserName, room.getId());

        return new ResponseEntity<>(KeyResponse.builder()
                .roomId(room.getId())
                .roomName(room.getName())
                .roomType(room.getRoomType())
                .userId(userId)
                .userName(newUserName)
                .observer(room.getUserByKey(userId).isObserver())
                .build(), HttpStatus.OK);
    }
}
