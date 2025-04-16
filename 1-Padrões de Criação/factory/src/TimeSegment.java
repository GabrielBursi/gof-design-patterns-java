import java.time.Duration;

public class TimeSegment extends Segment {

    public TimeSegment(String rideId, Location from, Location to) {
        super(rideId, from, to);
    }

    public long getDiffInMinutes() {
        return Duration.between(from.date, to.date).toMinutes();
    }
}
