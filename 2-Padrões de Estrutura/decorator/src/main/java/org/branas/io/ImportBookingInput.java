package org.branas.io;

import org.jetbrains.annotations.NotNull;

public record ImportBookingInput(
    @NotNull String file
) {
}
