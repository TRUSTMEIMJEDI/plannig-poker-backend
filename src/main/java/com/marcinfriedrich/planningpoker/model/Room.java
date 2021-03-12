package com.marcinfriedrich.planningpoker.model;

import com.marcinfriedrich.planningpoker.util.RandomUtil;

import java.util.ArrayList;
import java.util.List;

public class Room {
    private final String name;
    private final String key;
    private List<User> users;

    public Room(String name) {
        this.name = name;
        this.users = new ArrayList<>();
        this.key = RandomUtil.generateRoomKey();
    }

    public void addUser(User user) {
        users.add(user);
    }

    public void removeUserByName(String name) {
        users.stream()
                .filter(u -> name.equals(u.getName()))
                .findAny().ifPresent(user -> users.remove(user));

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
}
