package com.marcinfriedrich.planningpoker.service;

import com.marcinfriedrich.planningpoker.commons.Clock;
import com.marcinfriedrich.planningpoker.exception.NameIsTakenException;
import com.marcinfriedrich.planningpoker.domain.Room;
import com.marcinfriedrich.planningpoker.domain.RoomType;
import com.marcinfriedrich.planningpoker.domain.Size;
import com.marcinfriedrich.planningpoker.domain.User;
import com.marcinfriedrich.planningpoker.web.response.UserAnswerResponse;
import com.marcinfriedrich.planningpoker.web.response.UserFullResponse;
import com.marcinfriedrich.planningpoker.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoomService {

    private final SimpMessagingTemplate template;
    private final RoomRepository roomRepository;
    private final Clock clock;

    public List<UserAnswerResponse> getUsersWithHiddenAnswersInRoom(String roomId) {
        return getUsersInRoom(roomId)
                .stream()
                .map(UserAnswerResponse::new)
                .collect(Collectors.toList());
    }

    public List<User> getUsersInRoom(String roomId) {
        return roomRepository.getRoomById(roomId).getUsers();
    }

    public void revealAnswersForRoom(String roomId) {
        final var userFullResponses = getUsersInRoom(roomId)
                .stream()
                .map(UserFullResponse::new)
                .collect(Collectors.toList());
        template.convertAndSend("/room/" + roomId + "/reveal", userFullResponses);
    }

    public void cleanAnswersInRoom(String roomId) {
        var room = roomRepository.getRoomById(roomId);
        room.cleanAnswers();
        final var userFullResponses = room.getUsers()
                .stream()
                .map(UserFullResponse::new)
                .collect(Collectors.toList());
        template.convertAndSend("/room/" + roomId + "/reveal", userFullResponses);
    }

    public Room userJoinRoom(String roomId, User user) throws NameIsTakenException {
        var room = roomRepository.getRoomById(roomId);

        if (room.isNameTaken(user.getName())) {
            throw new NameIsTakenException();
        }

        room.addUserToRoom(user);
        template.convertAndSend("/room/" + roomId, getUserAnswerResponses(room));
        log.info("User {} joined room with key {}", user, room.getId());
        return room;
    }

    public Room createRoom(String roomName, User owner, RoomType roomType) {
        final var room = Room.builder()
                .name(roomName)
                .owner(owner)
                .roomType(roomType)
                .createdAt(clock.getCurrentDate())
                .build();
        log.info("Creating room with key {} and owner {}", room.getId(), room.getOwner());
        return roomRepository.addRoom(room);
    }

    public void userAnsweredInRoom(String roomId, String userId, Size size) {
        var room = roomRepository.getRoomById(roomId);
        room.setUserAnswer(userId, size);
        var userAnswerResponses = room.getUsers()
                .stream()
                .map(UserAnswerResponse::new)
                .collect(Collectors.toList());
        template.convertAndSend("/room/" + roomId, userAnswerResponses);
    }

    public void userLeavesRoom(String roomId, String userId) {
        var room = roomRepository.getRoomById(roomId);
        room.getUsers().stream()
                .filter(u -> u.getId().equals(userId))
                .findAny().ifPresent(room::removeUserFromRoom);
        var users = room.getUsers();
        if (users.isEmpty()) {
            roomRepository.deleteRoomById(roomId);
        } else {
            template.convertAndSend("/room/" + roomId, getUserAnswerResponses(room));
        }
    }

    public void deleteUserFromRoom(String roomId, String userId, String userNameToDelete) throws Exception {
        var room = roomRepository.getRoomById(roomId);
        boolean isAllowed = room.getUsers().stream()
                .filter(u -> userId.equals(u.getId()))
                .findFirst()
                .orElse(null) != null;

        if (isAllowed) {
            room.removeUserFromRoom(room.getUserByName(userNameToDelete));
        } else {
            throw new Exception("Nie można usunąć tego użytkownika.");
        }

        var userAnswerResponses = room.getUsers().stream()
                .map(UserAnswerResponse::new)
                .collect(Collectors.toList());
        this.template.convertAndSend("/room/" + roomId, userAnswerResponses);
        this.template.convertAndSend("/room/" + roomId + "/" + userNameToDelete, userNameToDelete);
    }

    public void changeRoomType(String roomId, RoomType roomType) {
        var room = roomRepository.getRoomById(roomId);
        room.setRoomType(roomType);
        template.convertAndSend("/room/" + roomId + "/changeType", roomType.toString());
    }

    public Room changeUserType(String roomId, String userId, boolean isObserver) {
        var room = roomRepository.getRoomById(roomId);
        var user = room.getUserByKey(userId);
        user.setObserver(isObserver);

        if (isObserver) {
            user.setAnswered(false);
            user.setSize(null);
        }

        template.convertAndSend("/room/" + roomId, getUserAnswerResponses(room));
        log.info("User with id {} changed his type to observer {} in room with key {}", userId, isObserver, room.getId());
        return room;
    }

    public Room changeUsername(String roomId, String userId, String newUsername) throws NameIsTakenException {
        var room = roomRepository.getRoomById(roomId);
        if (room.isNameTaken(newUsername)) {
            throw new NameIsTakenException();
        } else {
            var user = room.getUserByKey(userId);
            user.setName(newUsername);
        }
        template.convertAndSend("/room/" + roomId, getUserAnswerResponses(room));
        return room;
    }

    @Scheduled(cron = "0 0 16 * * MON-FRI")
    void removeUnusedRooms() {
        Instant now = Instant.parse(LocalDate.now().plusDays(1).toString());
        roomRepository.getRooms().stream()
                .filter(room -> room.getCreatedAt().isAfter(now))
                .forEach(room -> roomRepository.deleteRoomById(room.getId()));
    }

    private List<UserAnswerResponse> getUserAnswerResponses(Room room) {
        return room.getUsers().stream()
                .map(UserAnswerResponse::new)
                .collect(Collectors.toList());
    }
}
