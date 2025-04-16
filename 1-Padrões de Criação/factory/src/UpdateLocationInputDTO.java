import java.time.LocalDateTime;

public record UpdateLocationInputDTO(String rideId,
        double lat,
        double lon,
        LocalDateTime date) {

}
