package com.marcinfriedrich.planningpoker.service;

import com.marcinfriedrich.planningpoker.ClockTestConfig;
import com.marcinfriedrich.planningpoker.domain.Room;
import com.marcinfriedrich.planningpoker.domain.RoomType;
import com.marcinfriedrich.planningpoker.domain.User;
import com.marcinfriedrich.planningpoker.repository.RoomRepository;
import com.marcinfriedrich.planningpoker.web.response.UserAnswerResponse;
import com.marcinfriedrich.planningpoker.web.response.UserFullResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

// IT or Mock tests ???
//public class RoomServiceTests extends IntegrationTest {
//}

//@Import(ClockTestConfig.class)
@ExtendWith(MockitoExtension.class)
class RoomServiceTests {

//    public static final String ROOM_ID = "K5tTd";
//    private static final Instant CREATED_AT = Instant.parse("2021-07-24T12:00:00.000Z");
//
//    @Mock
//    private SimpMessagingTemplate template;
//
//    @Mock
//    private RoomRepository roomRepository;
//
//    @Autowired
//    private ClockTestConfig.FixedClock clock;
//
//    private RoomService roomService;
//
//    @BeforeEach
//    void setUp() {
//        roomService = new RoomService(template, roomRepository, clock);
//    }

    @Mock
    private SimpMessagingTemplate template;

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private ClockTestConfig.FixedClock clock;

    @InjectMocks
    private RoomService roomService;

    @Test
    @DisplayName("should get users with hidden answers from room")
    void shouldGetUsersWithHiddenAnswersFromRoom() {
        // given
        Room room = aRoomWithUsers();

        given(roomRepository.getRoomById(room.getId()))
                .willReturn(room);

        // when
        final var users = roomService.getUsersWithHiddenAnswersFromRoom(room.getId());

        // then
        assertThat(users)
                .hasSize(3)
                .extracting(
                        UserAnswerResponse::getName,
                        UserAnswerResponse::isAnswered,
                        UserAnswerResponse::isObserver)
                .contains(
                        tuple("owner", false, false),
                        tuple("testUser1", false, false),
                        tuple("testUser2", false, false)
                );
    }

    @Test
    @DisplayName("should get users in room")
    void shouldGetUsersInRoom() {
        // given
        Room room = aRoomWithUsers();
        given(roomRepository.getRoomById(room.getId()))
                .willReturn(room);

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
                        tuple("owner", true, false, false, null),
                        tuple("testUser1", false, false, false, null),
                        tuple("testUser2", false, false, false, null)
                );
    }

    @Test
    @DisplayName("should reveal answers for the room")
    void revealAnswersForRoom() {
        // given
        Room room = aRoomWithUsers();
        given(roomRepository.getRoomById(room.getId()))
                .willReturn(room);
        final var messageDestination = "/room/" + room.getId() + "/reveal";
        final var userFullResponses = room.getUsers()
                .stream()
                .map(UserFullResponse::new)
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
        User owner = User.of("owner", false);
        Room room = Room.builder()
                .name("testRoom")
                .owner(owner)
                .roomType(RoomType.T_SHIRTS)
                .createdAt(clock.getCurrentDate())
                .build();

        given(roomRepository.addRoom(any()))
                .willReturn(room);

        // when
        final var actualRoom = roomService.createRoom("testRoom", owner, RoomType.T_SHIRTS);

        // then
        assertThat(actualRoom)
                .extracting(
                        Room::getId,
                        Room::getName,
                        Room::getOwner,
                        Room::getCreatedAt,
                        Room::getRoomType)
                .contains(
                        room.getId(),
                        room.getName(),
                        room.getOwner(),
                        room.getCreatedAt(),
                        room.getRoomType()
                );
    }

    private Room aRoomWithUsers() {
        Room room = aRoomWithOwner(User.of("owner", false));
        room.addUserToRoom(User.of("testUser1", false));
        room.addUserToRoom(User.of("testUser2", false));
        return room;
    }

    private Room aRoomWithOwner(User owner) {
        return Room.builder()
                .name("roomTest")
                .owner(owner)
                .roomType(RoomType.FIBONACCI)
                .createdAt(clock.getCurrentDate())
                .build();
    }
}
