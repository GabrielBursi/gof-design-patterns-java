package org.branas.io;

import java.time.LocalDateTime;
import java.util.List;

import org.jetbrains.annotations.NotNull;

public interface RoomRepository {
    List<Room> findAvailableRoomsByPeriodAndCategory(@NotNull LocalDateTime checkinDate, @NotNull LocalDateTime checkoutDate, @NotNull String category);
    Room findById(@NotNull Integer roomId);
}
