package org.branas.io;

import org.jetbrains.annotations.NotNull;

public record GetBookingByCodeInput(
    @NotNull String code
) {
}
