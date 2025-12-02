package org.branas.io;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ImportBookingDecorator implements UseCase<ImportBookingInput, ImportBookingOutput> {
    private final UseCase<BookRoomInput, BookRoomOutput> useCase;

    @Override
    public ImportBookingOutput execute(@NotNull ImportBookingInput input) {
        List<String> codes = new ArrayList<>();
        String[] lines = input.file().split("\n");

        for (int i = 1; i < lines.length; i++) {
            String[] parts = lines[i].trim().split(";");
            if (parts.length < 4) continue;

            String email = parts[0].trim();
            LocalDateTime checkinDate = LocalDateTime.parse(parts[1].trim(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            LocalDateTime checkoutDate = LocalDateTime.parse(parts[2].trim(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            String category = parts[3].trim();

            BookRoomInput bookRoomInput = new BookRoomInput(email, checkinDate, checkoutDate, category);
            BookRoomOutput bookRoomOutput = useCase.execute(bookRoomInput);
            codes.add(bookRoomOutput.code());
        }

        return new ImportBookingOutput(codes);
    }
}
