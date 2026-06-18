import java.sql.Timestamp;

public class FlightEvent {

    /// Class that represents a Flight Event and its respective info.
    ///

    private Timestamp eventTime; //YEAR, MONTH, DAY_OF_MONTH, CRS_DEP_TIME (flight date + CRS_DEP_TIME, timezone UTC)

    // identifier
    private String carrierFlightNum;

    private String crsDepTime;
    private String carrier;
    private Integer originAirportID;
    private Integer destAirportID;

    private Boolean isCancelled;
    private Boolean isDiverted;
    private Double depDelay;




    // Constructor

    public FlightEvent(){
        new FlightEvent();
    }

    // Helper method

    public void createTimestamp(String year, String month, String day, String crsDepTime){

       // todo finish timestamp = new Timestamp();

        this.setEventTime(eventTime);
    }


    // Setters and Getters for private methods

    public Timestamp getEventTime() {
        return eventTime;
    }

    public void setEventTime(Timestamp eventTime) {
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

    public Integer getOriginAirportID() {
        return originAirportID;
    }

    public void setOriginAirportID(Integer originAirportID) {
        this.originAirportID = originAirportID;
    }

    public Integer getDestAirportID() {
        return destAirportID;
    }

    public void setDestAirportID(Integer destAirportID) {
        this.destAirportID = destAirportID;
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
