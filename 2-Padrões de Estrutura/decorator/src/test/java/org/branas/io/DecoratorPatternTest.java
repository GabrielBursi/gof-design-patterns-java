package org.branas.io;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DecoratorPatternTest {

    @Test
    @DisplayName("should apply log decorator")
    void shouldApplyLogDecorator() {
        RoomRepository roomRepository = new RoomRepositoryDatabase();
        BookingRepository bookingRepository = new BookingRepositoryDatabase();
        BookRoomUseCase bookRoom = new BookRoomUseCase(roomRepository, bookingRepository);

        UseCase<BookRoomInput, BookRoomOutput> decoratedUseCase = new LogDecorator<>(bookRoom);

        BookRoomInput input = new BookRoomInput(
            "test@example.com",
            LocalDateTime.of(2021, 3, 1, 10, 0),
            LocalDateTime.of(2021, 3, 5, 10, 0),
            "suite"
        );

        assertThrows(RuntimeException.class, () -> decoratedUseCase.execute(input));
    }

    @Test
    @DisplayName("should apply security decorator")
    void shouldApplySecurityDecorator() {
        RoomRepository roomRepository = new RoomRepositoryDatabase();
        BookingRepository bookingRepository = new BookingRepositoryDatabase();
        BookRoomUseCase bookRoom = new BookRoomUseCase(roomRepository, bookingRepository);

        UseCase<BookRoomInput, BookRoomOutput> decoratedUseCase = new SecurityDecorator<>(bookRoom);

        BookRoomInput input = new BookRoomInput(
            "test@example.com",
            LocalDateTime.of(2021, 3, 1, 10, 0),
            LocalDateTime.of(2021, 3, 5, 10, 0),
            "suite"
        );

        assertThrows(RuntimeException.class, () -> decoratedUseCase.execute(input));
    }

    @Test
    @DisplayName("should chain multiple decorators")
    void shouldChainMultipleDecorators() {
        RoomRepository roomRepository = new RoomRepositoryDatabase();
        BookingRepository bookingRepository = new BookingRepositoryDatabase();
        BookRoomUseCase bookRoom = new BookRoomUseCase(roomRepository, bookingRepository);

        UseCase<BookRoomInput, BookRoomOutput> decoratedUseCase =
            new SecurityDecorator<>(new LogDecorator<>(bookRoom));

        BookRoomInput input = new BookRoomInput(
            "test@example.com",
            LocalDateTime.of(2021, 3, 1, 10, 0),
            LocalDateTime.of(2021, 3, 5, 10, 0),
            "suite"
        );

        assertThrows(RuntimeException.class, () -> decoratedUseCase.execute(input));
    }

    @Test
    @DisplayName("should apply import booking decorator")
    void shouldApplyImportBookingDecorator() {
        RoomRepository roomRepository = new RoomRepositoryDatabase();
        BookingRepository bookingRepository = new BookingRepositoryDatabase();
        BookRoomUseCase bookRoom = new BookRoomUseCase(roomRepository, bookingRepository);

        UseCase<ImportBookingInput, ImportBookingOutput> importDecorator = 
            new ImportBookingDecorator(bookRoom);

        String fileContent = "email;checkinDate;checkoutDate;category\n" +
            "test1@example.com;2021-03-01T10:00:00;2021-03-05T10:00:00;suite\n" +
            "test2@example.com;2021-03-06T10:00:00;2021-03-10T10:00:00;suite";

        ImportBookingInput input = new ImportBookingInput(fileContent);

        assertThrows(RuntimeException.class, () -> importDecorator.execute(input));
    }
}
