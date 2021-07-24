package com.marcinfriedrich.planningpoker.domain;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class RoomTests {

    public static final String ROOM_NAME = "testRoomName";

    @Test
    void shouldCreateRoom() {
        // given
        User owner = User.of("testOwner", false);
        Instant now = Instant.now();

        // when
        Room room = Room.of(ROOM_NAME, owner, RoomType.T_SHIRTS, now);

        // then
        assertThat(room.getId()).isNotNull();
        assertThat(room)
                .extracting(
                        Room::getCreatedAt,
                        Room::getRoomType,
                        Room::getName,
                        Room::getUsers,
                        Room::getOwner)
                .containsExactly(
                        now,
                        RoomType.T_SHIRTS,
                        ROOM_NAME,
                        List.of(owner),
                        owner
                );

        assertThat(room.getUsers())
                .hasSize(1)
                .extracting(User::isOwner)
                .containsExactly(true);
    }

    @Test
    void shouldAddUserToTheRoom() {
        // given
        User owner = User.of("testOwner", false);

        Room room = Room.of(ROOM_NAME, owner, RoomType.T_SHIRTS, Instant.now());
        User testUser1 = User.of("testUser1", false);
        User testUser2 = User.of("testUser2", false);

        // when
        room.addUserToRoom(testUser1);
        room.addUserToRoom(testUser2);

        // then
        assertThat(room.getUsers()).isNotNull();
        assertThat(room.getUsers().size()).isEqualTo(3);

        assertThat(room.getUsers())
                .hasSize(3)
                .contains(owner, testUser1, testUser2);
    }

    @Test
    void shouldChangeRoomType() {
        // given
        User owner = User.of("testOwner", false);
        Room room = Room.of(ROOM_NAME, owner, RoomType.T_SHIRTS, Instant.now());

        // when
        room.setRoomType(RoomType.FIBONACCI);

        // then
        assertThat(room.getRoomType()).isEqualTo(RoomType.FIBONACCI);
    }

    @Test
    void shouldRemoveUserFromRoom() {
        // given
        User owner = User.of("testOwner", false);
        Room room = Room.of(ROOM_NAME, owner, RoomType.T_SHIRTS, Instant.now());
        User testUser1 = User.of("testUser1", false);
        User testUser2 = User.of("testUser2", false);
        room.addUserToRoom(testUser1);
        room.addUserToRoom(testUser2);

        // when
        room.removeUserFromRoom(testUser1);

        // then
        assertThat(room.getUsers())
                .hasSize(2)
                .contains(owner, testUser2);
    }

    @Test
    void shouldSetUserAnswer() {
        // given
        User owner = User.of("testOwner", false);

        Room room = Room.of(ROOM_NAME, owner, RoomType.T_SHIRTS, Instant.now());
        User testUser1 = User.of("testUser1", false);
        User testUser2 = User.of("testUser2", false);
        room.addUserToRoom(testUser1);
        room.addUserToRoom(testUser2);

        // when
        room.setUserAnswer(owner.getId(), Size.S);
        room.setUserAnswer(testUser1.getId(), Size.M);
        room.setUserAnswer(testUser2.getId(), Size.L);

        // then
        assertThat(room.getUsers())
                .hasSize(3)
                .extracting(User::getSize, User::isAnswered)
                .containsOnly(
                        tuple(Size.S, true),
                        tuple(Size.M, true),
                        tuple(Size.L, true));
    }

    @Test
    void shouldCleanAllUsersAnswers() {
        // given
        User owner = User.of("testOwner", false);

        Room room = Room.of(ROOM_NAME, owner, RoomType.T_SHIRTS, Instant.now());
        User testUser1 = User.of("testUser1", false);
        User testUser2 = User.of("testUser2", false);
        room.addUserToRoom(testUser1);
        room.addUserToRoom(testUser2);
        room.setUserAnswer(owner.getId(), Size.S);
        room.setUserAnswer(testUser1.getId(), Size.M);
        room.setUserAnswer(testUser2.getId(), Size.L);

        // when
        room.cleanAnswers();

        // then
        assertThat(room.getUsers())
                .hasSize(3)
                .extracting(User::getSize, User::isAnswered)
                .containsOnly(
                        tuple(null, false),
                        tuple(null, false),
                        tuple(null, false));
    }
}
