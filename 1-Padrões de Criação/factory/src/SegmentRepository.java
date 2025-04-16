import java.util.List;

public interface SegmentRepository {
    void save(Segment segment);
    List<Segment> listByRideId(String rideId);
}
