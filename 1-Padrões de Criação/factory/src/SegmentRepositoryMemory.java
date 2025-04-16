import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SegmentRepositoryMemory implements SegmentRepository {
    private final List<Segment> segments;

    public SegmentRepositoryMemory() {
        this.segments = new ArrayList<>();
    }

    @Override
    public void save(Segment segment) {
        segments.add(segment);
    }

    @Override
    public List<Segment> listByRideId(String rideId) {
        return segments.stream()
                .filter(segment -> segment.rideId.equals(rideId))
                .collect(Collectors.toList());
    }
}