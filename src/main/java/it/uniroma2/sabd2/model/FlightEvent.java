package it.uniroma2.sabd2.model;

public class FlightEvent {

    /***
     * Class that represents a Flight Event Object and its respective info.
    */

    private String eventTime; //YEAR, MONTH, DAY_OF_MONTH, CRS_DEP_TIME (flight date + CRS_DEP_TIME, timezone UTC)

    // identifier
    private String carrierFlightNum;

    private String crsDepTime;
    private String carrier;
    private Integer originAirportId;
    private Integer destAirportId;

    private Boolean isCancelled;
    private Boolean isDiverted;
    private Double depDelay;




    // Constructor

    public FlightEvent(){   }

    @Override
    public String toString() {
        return "FlightEvent{" +
                "eventTime='" + eventTime + '\'' +
                ", carrier='" + carrier + '\'' +
                ", carrierFlightNum='" + carrierFlightNum + '\'' +
                ", crsDepTime='" + crsDepTime + '\'' +
                ", originAirportId=" + originAirportId +
                ", destAirportId=" + destAirportId +
                ", isCancelled=" + isCancelled +
                ", isDiverted=" + isDiverted +
                ", depDelay=" + depDelay +
                '}';
    }

    // Helper method

    public void createTimestamp(String year, String month, String day, String crsDepTime){

       // todo finish timestamp = new Timestamp();

        this.setEventTime(eventTime);
    }


    // Setters and Getters for private methods

    public String getEventTime() {
        return eventTime;
    }

    public void setEventTime(String eventTime) {
        this.eventTime = eventTime;
    }

    public String getCrsDepTime() {
        return crsDepTime;
    }

    public void setCrsDepTime(String crsDepTime) {
        this.crsDepTime = crsDepTime;
    }

    public String getCarrier() {
        return carrier;
    }

    public void setCarrier(String carrier) {
        this.carrier = carrier;
    }

    public Integer getOriginAirportId() {
        return originAirportId;
    }

    public void setOriginAirportId(Integer originAirportId) {
        this.originAirportId = originAirportId;
    }

    public Integer getDestAirportId() {
        return destAirportId;
    }

    public void setDestAirportId(Integer destAirportId) {
        this.destAirportId = destAirportId;
    }

    public Boolean getDiverted() {
        return isDiverted;
    }

    public void setDiverted(Boolean diverted) {
        isDiverted = diverted;
    }

    public Boolean getCancelled() {
        return isCancelled;
    }

    public void setCancelled(Boolean cancelled) {
        isCancelled = cancelled;
    }

    public Double getDepDelay() {
        return depDelay;
    }

    public void setDepDelay(Double depDelay) {
        this.depDelay = depDelay;
    }

    public String getCarrierFlightNum() {
        return carrierFlightNum;
    }

    public void setCarrierFlightNum(String carrierFlightNum) {
        this.carrierFlightNum = carrierFlightNum;
    }



}
