package org.branas.io;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

@Getter
@AllArgsConstructor
public class Room {
    private final Integer roomId;
    @NotNull
    private final String category;
    @NotNull
    private final Double price;
    @NotNull
    private final String status;
}

