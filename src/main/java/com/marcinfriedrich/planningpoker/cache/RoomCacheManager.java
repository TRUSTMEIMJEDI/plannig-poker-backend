package com.marcinfriedrich.planningpoker.cache;

import com.marcinfriedrich.planningpoker.enums.RoomType;
import com.marcinfriedrich.planningpoker.enums.Size;
import com.marcinfriedrich.planningpoker.exception.NameIsTakenException;
import com.marcinfriedrich.planningpoker.exception.NoRoomException;
import com.marcinfriedrich.planningpoker.model.Room;
import com.marcinfriedrich.planningpoker.model.User;
import com.marcinfriedrich.planningpoker.payload.KeyResponse;

import java.util.ArrayList;
import java.util.List;
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

    public synchronized List<Room> getRooms() {
        List<Room> rooms = new ArrayList<>();
        cache.forEach((key, val) -> {
            rooms.add((Room) val);
        });

        return rooms;
    }

    public synchronized KeyResponse createRoom(String roomName, String userName, RoomType roomType) {
        Room room = new Room(roomName, roomType);
        User user = new User(userName);
        user.setOwner(true);
        room.addUser(user);

        String roomKey = room.getKey();
        String userKey = user.getKey();
        cache.put(roomKey, room);

        return new KeyResponse(roomKey, userKey, roomName, userName, false, roomType);
    }

    public synchronized Room getRoom(String key) {
        return (Room) cache.get(key);
    }

    public synchronized Room joinRoom(String key, User user) throws NameIsTakenException, NoRoomException {
        Room room = (Room) cache.get(key);

        if (room == null) {
            throw new NoRoomException();
        }

        if (room.isNameTaken(user.getName())) {
            throw new NameIsTakenException();
        }

        room.addUser(user);

        return room;
    }

    public synchronized Room leaveRoom(String roomKey, String userKey) {
        Room room = (Room) cache.get(roomKey);
        room.removeUserByKey(userKey);
        return room;
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

    public synchronized void deleteRoom(String key) {
        cache.remove(key);
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
