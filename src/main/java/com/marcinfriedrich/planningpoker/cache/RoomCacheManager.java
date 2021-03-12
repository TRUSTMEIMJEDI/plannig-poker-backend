package com.marcinfriedrich.planningpoker.cache;

import com.marcinfriedrich.planningpoker.enums.Size;
import com.marcinfriedrich.planningpoker.model.Room;
import com.marcinfriedrich.planningpoker.model.User;
import com.marcinfriedrich.planningpoker.payload.KeyResponse;

import java.util.concurrent.ConcurrentHashMap;

public class RoomCacheManager {

    private static RoomCacheManager instance;
    private final ConcurrentHashMap<String, Object> cache;

    private RoomCacheManager() {
        cache = new ConcurrentHashMap<>();
    }

    public static synchronized RoomCacheManager getInstance() {
        if (instance == null) {
            instance = new RoomCacheManager();
        }

        return instance;
    }

    public synchronized KeyResponse createRoom(String roomName, String userName) {
        Room room = new Room(roomName);
        User user = new User(userName);
        user.setOwner(true);
        room.addUser(user);

        String roomKey = room.getKey();
        String userKey = user.getKey();
        cache.put(roomKey, room);

        return new KeyResponse(roomKey, userKey, roomName, userName);
    }

    public synchronized Room getRoom(String key) {
        return (Room) cache.get(key);
    }

    public synchronized Room joinRoom(String key, User user) {
        Room room = (Room) cache.get(key);
        room.addUser(user);

        return room;
    }

    public synchronized void removeUserFromRoom(String key, String name) {
        Room room = (Room) cache.get(key);
        room.removeUserByName(name);
    }

    public synchronized void deleteRoom(String key, String name) {
        Room room = (Room) cache.get(key);
        User user = room.getUsers().stream()
                .filter(u -> name.equals(u.getName()))
                .findAny()
                .orElse(null);

        if (user != null && user.isOwner()) {
            cache.remove(key);
        }
    }

    public synchronized void setUserSize(String roomKey, String userKey, Size size) {
        Room room = getRoom(roomKey);
        User user = room.getUserByKey(userKey);
        user.setSize(size);
        user.setAnswer(size != null);
    }

    public synchronized void clean(String key) {
        Room room = (Room) cache.get(key);
        room.getUsers().forEach(user -> {
            user.setAnswer(false);
            user.setSize(null);
        });
    }

}
