package org.branas.io;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Ride {

    private final List<Segment> segments = new ArrayList<>();
    private double fare;

    public void addSegment(double distance, LocalDateTime date) {
        segments.add(new Segment(distance, date));
    }

    public void calculateFare() {
        var overnightSunday = new OvernightSundayFareCalculator();
        var overnight = new OvernightFareCalculator();
        var sunday = new SundayFareCalculator();
        var normal = new NormalFareCalculator();

        overnightSunday.setNext(overnight).setNext(sunday).setNext(normal);

        double total = segments.stream()
            .mapToDouble(overnightSunday::calculate)
            .sum();

        this.fare = Math.max(total, 10.0);
    }

    public double getFare() {
        return fare;
    }
}
