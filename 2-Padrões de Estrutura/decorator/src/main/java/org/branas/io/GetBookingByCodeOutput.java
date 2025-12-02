package org.branas.io;

import org.jetbrains.annotations.NotNull;

public record GetBookingByCodeOutput(
    @NotNull String code,
    @NotNull String category,
    @NotNull Long duration,
    @NotNull Double price
) {
}
