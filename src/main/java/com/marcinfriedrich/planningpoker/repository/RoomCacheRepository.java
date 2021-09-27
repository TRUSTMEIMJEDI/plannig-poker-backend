package com.marcinfriedrich.planningpoker.repository;

import com.marcinfriedrich.planningpoker.exception.RoomNotFoundException;
import com.marcinfriedrich.planningpoker.domain.Room;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
class RoomCacheRepository implements RoomRepository {
    private final Map<String, Room> roomCache = new ConcurrentHashMap<>();

    @Override
    public Collection<Room> getRooms() {
        synchronized (roomCache) {
            return roomCache.values();
        }
    }

    @Override
    public Room addRoom(Room room) {
        synchronized (roomCache) {
            roomCache.put(room.getId(), room);
            return room;
        }
    }

    @Override
    public Room getRoomById(String roomId) {
        Room room = roomCache.get(roomId);
        if (room != null) {
            return room;
        }
        throw new RoomNotFoundException(roomId);
    }

    @Override
    public Room deleteRoomById(String roomId) {
        synchronized (roomCache) {
            return roomCache.remove(roomId);
        }
    }

}
