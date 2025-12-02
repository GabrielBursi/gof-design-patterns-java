package org.branas.io;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;

public class RoomRepositoryDatabase implements RoomRepository {

    private final List<Room> rooms = new ArrayList<>();

    public RoomRepositoryDatabase() {
        rooms.add(new Room(1, "suite", 500.0, "available"));
    }

    @Override
    public List<Room> findAvailableRoomsByPeriodAndCategory(@NotNull LocalDateTime checkinDate,
                                                            @NotNull LocalDateTime checkoutDate,
                                                            @NotNull String category) {
        List<Room> available = new ArrayList<>();
        for (Room room : rooms) {
            if (room.getCategory().equalsIgnoreCase(category)
                    && room.getStatus().equalsIgnoreCase("available")) {
                available.add(room);
            }
        }
        return available;
    }

    @Override
    public Room findById(@NotNull Integer roomId) {
        return rooms.stream()
                .filter(r -> r.getRoomId().equals(roomId))
                .findFirst()
                .orElse(null);
    }
}
