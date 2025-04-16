import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class DistanceRide extends Ride {

    public DistanceRide(String rideId, double latitude, double longitude, LocalDateTime date) {
        super(latitude, longitude, date);
    }

    @Override
    public double calculateFare(List<Segment> segments) {
        double total = 0;

        for (Segment segment : segments) {
            if (segment instanceof DistanceSegment) {
                total += ((DistanceSegment) segment).getDistance();
            }
        }
        return total * 4;
    }

    @Override
    protected Segment createSegment(Location from, Location to) {
        return new DistanceSegment(this.getRideId(), from, to);
    }

    public static DistanceRide create(double lat, double lon, LocalDateTime date) {
        String rideId = UUID.randomUUID().toString();
        return new DistanceRide(rideId, lat, lon, date);
    }
}
