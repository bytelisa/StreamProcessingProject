package it.uniroma2.sabd2.model;

public class Query1Result {

    private String carrier;
    private String windowStart;
    private String windowEnd;

    private long totalFlights;
    private long completedFlights;
    private long cancelledFlights;
    private long divertedFlights;

    private double meanDepDelay;
    private double cancellationRate;
    private double lateDepartureRate;


    public String getCarrier() {
        return carrier;
    }

    public void setCarrier(String carrier) {
        this.carrier = carrier;
    }

    public String getWindowStart() {
        return windowStart;
    }

    public void setWindowStart(String windowStart) {
        this.windowStart = windowStart;
    }

    public String getWindowEnd() {
        return windowEnd;
    }

    public void setWindowEnd(String windowEnd) {
        this.windowEnd = windowEnd;
    }

    public long getTotalFlights() {
        return totalFlights;
    }

    public void setTotalFlights(long totalFlights) {
        this.totalFlights = totalFlights;
    }

    public long getCompletedFlights() {
        return completedFlights;
    }

    public void setCompletedFlights(long completedFlights) {
        this.completedFlights = completedFlights;
    }

    public long getCancelledFlights() {
        return cancelledFlights;
    }

    public void setCancelledFlights(long cancelledFlights) {
        this.cancelledFlights = cancelledFlights;
    }

    public long getDivertedFlights() {
        return divertedFlights;
    }

    public void setDivertedFlights(long divertedFlights) {
        this.divertedFlights = divertedFlights;
    }

    public double getMeanDepDelay() {
        return meanDepDelay;
    }

    public void setMeanDepDelay(double meanDepDelay) {
        this.meanDepDelay = meanDepDelay;
    }

    public double getCancellationRate() {
        return cancellationRate;
    }

    public void setCancellationRate(double cancellationRate) {
        this.cancellationRate = cancellationRate;
    }

    public double getLateDepartureRate() {
        return lateDepartureRate;
    }

    public void setLateDepartureRate(double lateDepartureRate) {
        this.lateDepartureRate = lateDepartureRate;
    }





}