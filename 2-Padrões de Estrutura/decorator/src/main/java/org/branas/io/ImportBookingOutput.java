package org.branas.io;

import java.util.List;

import org.jetbrains.annotations.NotNull;

public record ImportBookingOutput(
    @NotNull List<String> codes
) {
}
