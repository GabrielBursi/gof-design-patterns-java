import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class TimeRide extends Ride {

    public TimeRide(String rideId, double latitude, double longitude, LocalDateTime date) {
        super(latitude, longitude, date);
    }

    @Override
    public double calculateFare(List<Segment> segments) {
        double total = 0;

        for (Segment segment : segments) {
            if (segment instanceof TimeSegment) {
                total += ((TimeSegment) segment).getDiffInMinutes();
            }
        }
        return total * 1;
    }

    @Override
    protected Segment createSegment(Location from, Location to) {
        return new TimeSegment(this.getRideId(), from, to);
    }

    public static TimeRide create(double lat, double lon, LocalDateTime date) {
        String rideId = UUID.randomUUID().toString();
        return new TimeRide(rideId, lat, lon, date);
    }
}
