package it.uniroma2.sabd2.queries;

import java.io.Serializable;

public class Query1Accumulator implements Serializable {

    private long totalCount;
    private long completedCount;
    private long cancelledCount;
    private long divertedCount;
    private double depDelaySum;
    private long lateDepartureCount;

    public Query1Accumulator() {
        this.totalCount = 0;
        this.completedCount = 0;
        this.cancelledCount = 0;
        this.divertedCount = 0;
        this.depDelaySum = 0.0;
        this.lateDepartureCount = 0;
    }

    public long getTotalCount() {
        return totalCount;
    }

    public void incrementTotalCount() {
        this.totalCount++;
    }

    public long getCompletedCount() {
        return completedCount;
    }

    public void incrementCompletedCount() {
        this.completedCount++;
    }

    public long getCancelledCount() {
        return cancelledCount;
    }

    public void incrementCancelledCount() {
        this.cancelledCount++;
    }

    public long getDivertedCount() {
        return divertedCount;
    }

    public void incrementDivertedCount() {
        this.divertedCount++;
    }

    public double getDepDelaySum() {
        return depDelaySum;
    }

    public void addDepDelay(double depDelay) {
        this.depDelaySum += depDelay;
    }

    public long getLateDepartureCount() {
        return lateDepartureCount;
    }

    public void incrementLateDepartureCount() {
        this.lateDepartureCount++;
    }

    public void merge(Query1Accumulator other) {
        this.totalCount += other.totalCount;
        this.completedCount += other.completedCount;
        this.cancelledCount += other.cancelledCount;
        this.divertedCount += other.divertedCount;
        this.depDelaySum += other.depDelaySum;
        this.lateDepartureCount += other.lateDepartureCount;
    }
}