import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public abstract class Ride {

    private Location lastLocation;
    private final String rideId;

    public Ride(double lat, double lon, LocalDateTime date) {
        this.rideId = UUID.randomUUID().toString();
        this.lastLocation = new Location(lat, lon, date);
    }

    public void updateLocation(Location newLocation) {
        this.lastLocation = newLocation;
    }

    abstract double calculateFare(List<Segment> segments);
    abstract Segment createSegment(Location from, Location to);

    public Location getLastLocation() {
        return lastLocation;
    }

    public String getRideId() {
        return rideId;
    }

}
