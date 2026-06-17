package org.branas.io;

public class OvernightFareCalculator extends BaseFareCalculator {

    private static final double RATE = 3.90;

    @Override
    public double calculate(Segment segment) {
        if (segment.isOvernight() && !segment.isSunday()) {
            return segment.getDistance() * RATE;
        }
        return passToNext(segment);
    }
}
