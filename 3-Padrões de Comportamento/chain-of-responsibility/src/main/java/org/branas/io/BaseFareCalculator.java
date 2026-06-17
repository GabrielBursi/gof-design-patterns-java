package org.branas.io;

public abstract class BaseFareCalculator implements FareCalculator {

    private FareCalculator next;

    @Override
    public FareCalculator setNext(FareCalculator next) {
        this.next = next;
        return next;
    }

    protected double passToNext(Segment segment) {
        if (next != null) return next.calculate(segment);
        throw new IllegalStateException("No handler found for segment");
    }
}
