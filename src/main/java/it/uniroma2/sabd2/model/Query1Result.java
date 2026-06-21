package it.uniroma2.sabd2.model;

public class Query1Result {

    private int TotalFlights;

    private int CompletedFlights;

    private float MeanDepDelay;

    private float CancellationRate;

    private float LateDepartureRate;

    public int getTotalFlights() {
        return TotalFlights;
    }

    public void setTotalFlights(int totalFlights) {
        TotalFlights = totalFlights;
    }

    public int getCompletedFlights() {
        return CompletedFlights;
    }

    public void setCompletedFlights(int completedFlights) {
        CompletedFlights = completedFlights;
    }

    public float getMeanDepDelay() {
        return MeanDepDelay;
    }

    public void setMeanDepDelay(float meanDepDelay) {
        MeanDepDelay = meanDepDelay;
    }

    public float getCancellationRate() {
        return CancellationRate;
    }

    public void setCancellationRate(float cancellationRate) {
        CancellationRate = cancellationRate;
    }

    public float getLateDepartureRate() {
        return LateDepartureRate;
    }

    public void setLateDepartureRate(float lateDepartureRate) {
        LateDepartureRate = lateDepartureRate;
    }
}
