package com.marcinfriedrich.planningpoker.web;

import com.marcinfriedrich.planningpoker.exception.NameIsTakenException;
import com.marcinfriedrich.planningpoker.domain.User;
import com.marcinfriedrich.planningpoker.service.RoomService;
import com.marcinfriedrich.planningpoker.web.request.*;
import com.marcinfriedrich.planningpoker.web.response.KeyResponse;
import com.marcinfriedrich.planningpoker.web.response.UserAnswerResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
class RoomResource {

    private final RoomService roomService;


    @GetMapping("/{key}/users")
    public List<UserAnswerResponse> getUsersFromRoom(@PathVariable(name = "key") String roomId) {
        return roomService.getUsersWithHiddenAnswersFromRoom(roomId);
    }

    @GetMapping("/{key}/reveal")
    public void getAnswers(@PathVariable(name = "key") String roomId) {
        roomService.revealAnswersForRoom(roomId);
    }

    @GetMapping("/{key}/clean")
    public void clean(@PathVariable(name = "key") String roomId) {
        roomService.cleanAnswersForRoom(roomId);
    }

    @PostMapping("/{key}/{name}/{isObserver}")
    public ResponseEntity<KeyResponse> joinRoom(
            @PathVariable(name = "key") String roomId,
            @PathVariable String name,
            @PathVariable Boolean isObserver) throws NameIsTakenException {
        final var user = User.of(name, isObserver);
        final var room = roomService.userJoinRoom(roomId, user);

        return new ResponseEntity<>(KeyResponse.builder()
                .roomId(room.getId())
                .roomName(room.getName())
                .roomType(room.getRoomType())
                .userId(user.getId())
                .userName(user.getName())
                .observer(user.isObserver())
                .build(), HttpStatus.OK);
    }

    @PostMapping("/createRoomWithOwner")
    public KeyResponse createRoom(@RequestBody CreateRoomRequest roomRequest) {
        final var owner = User.of(roomRequest.getUserName(), false);
        final var room = roomService.createRoom(roomRequest.getRoomName(), owner, roomRequest.getRoomType());
        return KeyResponse.builder()
                .roomId(room.getId())
                .roomName(room.getName())
                .roomType(room.getRoomType())
                .userId(owner.getId())
                .userName(owner.getName())
                .observer(false)
                .build();
    }

    @PostMapping("/answer")
    public void answer(@RequestBody AnswerRequest answerRequest) {
        roomService.userAnsweredInRoom(
                answerRequest.getRoomKey(),
                answerRequest.getUserKey(),
                answerRequest.getSize());
    }

    @PostMapping("/leaveRoom")
    public void leaveRoom(@RequestBody RoomAndUserRequest roomAndUserRequest) {
        roomService.userLeavesRoom(roomAndUserRequest.getRoomKey(), roomAndUserRequest.getUserKey());
    }

    @PostMapping("/deleteUserFromRoom")
    public void deleteUserFromRoom(@RequestBody DeleteUserFromRoom deleteUserFromRoom) throws Exception {
        roomService.deleteUserFromRoom(
                deleteUserFromRoom.getRoomKey(),
                deleteUserFromRoom.getUserKey(),
                deleteUserFromRoom.getUserNameToDelete());
    }

    @PostMapping("/changeRoomType")
    public void changeRoomType(@RequestBody ChangeRoomTypeRequest changeRoomTypeRequest) {
        roomService.changeRoomType(
                changeRoomTypeRequest.getRoomKey(),
                changeRoomTypeRequest.getRoomType());
    }

}
