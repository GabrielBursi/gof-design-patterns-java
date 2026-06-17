package org.branas.io;

import java.time.LocalDateTime;

public class Segment {

    private final double distance;
    private final LocalDateTime date;

    public Segment(double distance, LocalDateTime date) {
        if (distance <= 0) throw new IllegalArgumentException("Distance must be positive");
        this.distance = distance;
        this.date = date;
    }

    public double getDistance() {
        return distance;
    }

    public boolean isOvernight() {
        int hour = date.getHour();
        return hour >= 22 || hour < 6;
    }

    public boolean isSunday() {
        return date.getDayOfWeek().getValue() == 7;
    }
}
