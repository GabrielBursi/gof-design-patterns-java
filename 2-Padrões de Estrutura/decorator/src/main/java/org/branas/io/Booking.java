package org.branas.io;

import java.time.LocalDateTime;
import java.util.UUID;

import org.jetbrains.annotations.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Booking {
    @NotNull
    private final String code;
    @NotNull
    private final Integer roomId;
    @NotNull
    private final String email;
    @NotNull
    private final LocalDateTime checkinDate;
    @NotNull
    private final LocalDateTime checkoutDate;
    @NotNull
    private final Long duration;
    @NotNull
    private final Double price;
    private String status;

    public static Booking create(@NotNull Room room, @NotNull String email, @NotNull LocalDateTime checkinDate, @NotNull LocalDateTime checkoutDate) {
        String code = UUID.randomUUID().toString();
        long duration = (checkoutDate.toLocalDate().toEpochDay() - checkinDate.toLocalDate().toEpochDay());
        double price = duration * room.getPrice();
        return new Booking(code, room.getRoomId(), email, checkinDate, checkoutDate, duration, price, "confirmed");
    }

    public void cancel() {
        this.status = "cancelled";
    }

    public String getStatus() {
        return this.status;
    }
}
