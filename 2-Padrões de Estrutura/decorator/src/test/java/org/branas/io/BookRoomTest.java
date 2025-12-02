package org.branas.io;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class BookRoomTest {

  @Test
  @DisplayName("it should book a room")
  void shouldBookARoom() {
    RoomRepositoryDatabase roomRepository = new RoomRepositoryDatabase();
    BookingRepositoryDatabase bookingRepository = new BookingRepositoryDatabase();

    BookRoomUseCase bookRoom = new BookRoomUseCase(roomRepository, bookingRepository);
    BookRoomInput bookRoominput = new BookRoomInput("email@teste.com",
        LocalDateTime.parse("2021-03-01T10:00:00", DateTimeFormatter.ISO_LOCAL_DATE_TIME),
        LocalDateTime.parse("2021-03-05T10:00:00", DateTimeFormatter.ISO_LOCAL_DATE_TIME),
        "suite");
    BookRoomOutput bookRoomOutput = bookRoom.execute(bookRoominput);

    GetBookingByCode getBookingByCode = new GetBookingByCode(roomRepository, bookingRepository);
    GetBookingByCodeInput getBookingByCodeInput = new GetBookingByCodeInput(bookRoomOutput.code());
    GetBookingByCodeOutput getBookingByCodeOutput = getBookingByCode.execute(getBookingByCodeInput);
    Assertions.assertEquals(4, getBookingByCodeOutput.duration());
    Assertions.assertEquals(2000, getBookingByCodeOutput.price());
  }
}
