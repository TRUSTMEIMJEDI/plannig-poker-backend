package com.marcinfriedrich.planningpoker.domain;

import com.marcinfriedrich.planningpoker.util.RandomUtil;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Room {
    private final String id;
    private final String name;
    private final Instant createdAt;
    private List<User> users;
    private RoomType roomType;

    @Builder
    public static Room of(String name, User owner, RoomType roomType, Instant createdAt) {
        owner.setOwner(true);
        List<User> users = new ArrayList<>();
        users.add(owner);
        return new Room(RandomUtil.generateRoomKey(), name, createdAt, users, roomType);
    }

    public boolean addUserToRoom(User user) {
        return users.add(user);
    }

    public boolean removeUserFromRoom(User user) {
        return users.remove(user);
    }

    public void setUserAnswer(String userId, Size size) {
        User user = users.stream().filter(u -> u.getId().equals(userId)).findFirst().orElseThrow();
        user.setSize(size);
        user.setAnswered(size != null);
    }

    public void cleanAnswers() {
        users.forEach(user -> {
            user.setSize(null);
            user.setAnswered(false);
        });
    }

    public User getOwner() {
        return users.stream()
                .filter(User::isOwner)
                .findFirst()
                .orElseThrow();
    }

    public User getUserByName(String name) {
        return users.stream()
                .filter(u -> name.equals(u.getName()))
                .findAny()
                .orElse(null);
    }

    public User getUserByKey(String userId) {
        return users.stream()
                .filter(u -> userId.equals(u.getId()))
                .findAny()
                .orElse(null);
    }

    public boolean isNameTaken(String name) {
        return users.stream()
                .anyMatch(user -> name.equalsIgnoreCase(user.getName()));
    }

    @Override
    public String toString() {
        return "Room{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", createdAt=" + createdAt +
                ", users=" + users +
                ", roomType=" + roomType +
                '}';
    }
}
