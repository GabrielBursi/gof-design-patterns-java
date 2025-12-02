package org.branas.io;

import java.util.HashMap;
import java.util.Map;

import org.jetbrains.annotations.NotNull;

public class BookingRepositoryDatabase implements BookingRepository {

    private final Map<String, Booking> bookings = new HashMap<>();

    @Override
    public void save(@NotNull Booking booking) {
        bookings.put(booking.getCode(), booking);
    }

    @Override
    public void update(@NotNull Booking booking) {
        bookings.put(booking.getCode(), booking);
    }

    @Override
    public Booking findByCode(@NotNull String code) {
        return bookings.get(code);
    }
}
