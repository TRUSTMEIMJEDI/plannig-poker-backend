package com.marcinfriedrich.planningpoker.model;

import com.marcinfriedrich.planningpoker.enums.RoomType;
import com.marcinfriedrich.planningpoker.util.RandomUtil;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Room {
    private final String name;
    private final String key;
    private final List<User> users;
    private final LocalDate createdAt;
    private final RoomType roomType;

    public Room(String name, RoomType roomType) {
        this.name = name;
        this.users = new ArrayList<>();
        this.key = RandomUtil.generateRoomKey();
        this.createdAt = LocalDate.now();
        this.roomType = roomType;
    }

    public void addUser(User user) {
        users.add(user);
    }

    public void removeUserByName(String name) {
        users.stream()
                .filter(u -> name.equals(u.getName()))
                .findAny().ifPresent(users::remove);

    }

    public boolean isNameTaken(String name) {
        return users.stream()
                .anyMatch(user -> name.equalsIgnoreCase(user.getName()));
    }

    public void removeUserByKey(String key) {
        users.stream()
                .filter(u -> key.equals(u.getKey()))
                .findAny().ifPresent(users::remove);
    }

    public User getUserByName(String name) {
        return users.stream()
                .filter(u -> name.equals(u.getName()))
                .findAny()
                .orElse(null);
    }

    public User getUserByKey(String userKey) {
        return users.stream()
                .filter(u -> userKey.equals(u.getKey()))
                .findAny()
                .orElse(null);
    }

    public String getName() {
        return name;
    }

    public List<User> getUsers() {
        return users;
    }

    public String getKey() {
        return key;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public RoomType getRoomType() {
        return roomType;
    }
}
