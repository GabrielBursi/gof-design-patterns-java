package org.branas.io;

public class SundayFareCalculator extends BaseFareCalculator {

    private static final double RATE = 2.90;

    @Override
    public double calculate(Segment segment) {
        if (segment.isSunday() && !segment.isOvernight()) {
            return segment.getDistance() * RATE;
        }
        return passToNext(segment);
    }
}
