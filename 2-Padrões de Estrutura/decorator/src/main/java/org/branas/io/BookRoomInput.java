package org.branas.io;

import java.time.LocalDateTime;

import org.jetbrains.annotations.NotNull;

public record BookRoomInput(
    @NotNull String email,
    @NotNull LocalDateTime checkinDate,
    @NotNull LocalDateTime checkoutDate,
    @NotNull String category
) {
}
