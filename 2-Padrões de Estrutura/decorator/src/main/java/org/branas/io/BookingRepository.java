package org.branas.io;

import org.jetbrains.annotations.NotNull;

public interface BookingRepository {
    void save(@NotNull Booking booking);
    void update(@NotNull Booking booking);
    Booking findByCode(@NotNull String code);
}
