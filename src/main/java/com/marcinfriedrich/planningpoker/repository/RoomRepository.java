package com.marcinfriedrich.planningpoker.repository;

import com.marcinfriedrich.planningpoker.domain.Room;

import java.util.Collection;

public interface RoomRepository {
    Collection<Room> getRooms();

    Room addRoom(Room room);

    Room getRoomById(String roomId);

    Room deleteRoomById(String roomId);
}
