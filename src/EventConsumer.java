


public class EventConsumer {

    /***
     * Class Responsibility: consume flight events from Kafka Broker & turn them into FlightEvent Java objects.
     *
     * Note: could use that OOP pattern where I update a "global" list of events that other classes can observe?
     * Using singleton+observer(??)
     * That would be nice: Query1 and Query2 can subscribe to the global FlightEventList and receive updates on incoming
     * flights. They have to compute real time statistics using windows (tumbling/sliding), so I think this is the way.
     */

    // todo usa KafkaConectorExample come base. Aggiusta il pom per gli import!!!
}
