package com.marcinfriedrich.planningpoker.service;

import com.marcinfriedrich.planningpoker.ClockTestConfig;
import com.marcinfriedrich.planningpoker.IntegrationTest;
import com.marcinfriedrich.planningpoker.domain.Room;
import com.marcinfriedrich.planningpoker.domain.RoomType;
import com.marcinfriedrich.planningpoker.domain.User;
import com.marcinfriedrich.planningpoker.repository.RoomRepository;
import com.marcinfriedrich.planningpoker.web.response.UserAnswerResponse;
import com.marcinfriedrich.planningpoker.web.response.UserFullResponse;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@Import(ClockTestConfig.class)
class RoomServiceIT extends IntegrationTest {

    public static final String OWNER_NAME = "owner";

    private final SimpMessagingTemplate template;
    private final RoomRepository roomRepository;
    private final ClockTestConfig.FixedClock clock;
    private final RoomService roomService;

    @Autowired
    public RoomServiceIT(SimpMessagingTemplate template,
                         RoomRepository roomRepository,
                         ClockTestConfig.FixedClock clock,
                         RoomService roomService) {
        this.template = template;
        this.roomRepository = roomRepository;
        this.clock = clock;
        this.roomService = roomService;
    }

    @Test
    @DisplayName("should get users with hidden answers from room")
    void shouldGetUsersWithHiddenAnswersFromRoom() {
        // given
        Room room = givenRoomWithUserNames("user1", "user2");

        // when
        final var users = roomService.getUsersWithHiddenAnswersInRoom(room.getId());

        // then
        assertThat(users)
                .hasSize(3)
                .extracting(
                        UserAnswerResponse::getName,
                        UserAnswerResponse::isAnswered,
                        UserAnswerResponse::isObserver)
                .contains(
                        tuple(OWNER_NAME, false, false),
                        tuple("user1", false, false),
                        tuple("user2", false, false)
                );
    }

    @Test
    @DisplayName("should get users in room")
    void shouldGetUsersInRoom() {
        // given
        Room room = givenRoomWithUserNames("testUser1", "testUser2");

        // when
        final var users = roomService.getUsersInRoom(room.getId());

        // then
        assertThat(users)
                .hasSize(3)
                .extracting(
                        User::getName,
                        User::isOwner,
                        User::isObserver,
                        User::isAnswered,
                        User::getSize)
                .contains(
                        tuple(OWNER_NAME, true, false, false, null),
                        tuple("testUser1", false, false, false, null),
                        tuple("testUser2", false, false, false, null)
                );
    }

    @Test
    @Disabled
    @DisplayName("should reveal answers for the room")
    void revealAnswersForRoom() {
        // given
        Room room = givenRoomWithUserNames("testUser1", "testUser2");

        final var messageDestination = "/room/" + room.getId() + "/reveal";
        final var userFullResponses = room.getUsers()
                .stream()
                .map(UserFullResponse::of)
                .collect(Collectors.toList());

        // when
        roomService.revealAnswersForRoom(room.getId());

        // then
        verify(template, times(1))
                .convertAndSend(messageDestination, userFullResponses);

//        await().atMost(5, TimeUnit.SECONDS)
//                .untilAsserted(() -> verify(template, times(1))
//                        .convertAndSend(messageDestination, userFullResponses));
    }

    @Test
    @DisplayName("should create room with owner")
    void shouldCreateRoom() {
        // given
        User owner = User.of(OWNER_NAME, false);

        // when
        final var actualRoom = roomService.createRoom("testRoom", owner, RoomType.T_SHIRTS);

        // then
        assertThat(actualRoom)
                .extracting(
                        Room::getName,
                        Room::getOwner,
                        Room::getCreatedAt,
                        Room::getRoomType)
                .contains(
                        "testRoom",
                        owner,
                        clock.getCurrentDate(),
                        RoomType.T_SHIRTS
                );
    }

    private Room givenRoomWithUserNames(String... values) {
        Room room = givenRoomWithOwner(User.of(OWNER_NAME, false));
        for (String value : values) {
            room.addUserToRoom(User.of(value, false));
        }
        return roomRepository.addRoom(room);
    }

    private Room givenRoomWithOwner(User owner) {
        return Room.builder()
                .name("roomTest")
                .owner(owner)
                .roomType(RoomType.FIBONACCI)
                .createdAt(clock.getCurrentDate())
                .build();
    }
}
