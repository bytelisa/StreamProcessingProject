package it.uniroma2.sabd2.queries;

import it.uniroma2.sabd2.model.FlightEvent;
import it.uniroma2.sabd2.model.Query1Result;
import org.apache.flink.runtime.execution.Environment;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import it.uniroma2.sabd2.model.FlightEvent;
import org.apache.flink.api.common.functions.AggregateFunction;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

import java.time.Instant;

public class Query1Job {
    /***
     Class Responsibility: implement Query #1 using Flink.

     Query 1: Q1 La query Q1 si concentra sul monitoraggio in tempo reale dello stato operativo delle principali compagnie
     aeree. Facendo riferimento ai voli relativi alle compagnie AA (American Airlines), DL (Delta),
     UA (United) e WN (Southwest), aggregare gli eventi usando finestre tumbling di durata pari a 1 ora,
     basate sull’event time.

     Per ciascuna finestra e per ciascuna compagnia, calcolare:
     • il numero totale di voli osservati;
     • il numero di voli completati (cioe' non cancellati e non deviati), cancellati e deviati;
     • il valor medio di DEP DELAY, considerando solo i voli non cancellati;
     • il tasso di cancellazione, definito come percentuale di voli cancellati sul totale dei voli osservati
     nella finestra;
     • il tasso di partenze in ritardo, definito come percentuale di voli non cancellati con DEP DELAY
     maggiore di 15 minuti.

     L’output della query deve avere il seguente schema:
     window start, window end, airline, num flights, completed, cancelled,
     diverted, dep delay mean, cancellation rate, late departure rate

     - 1 hour Tumbling window;
     - Status: 6 variables
     - total_count
     - completed_count
     - cancelled_count
     - diverted_count
     - dep_delay_sum
     - late_departure_count: counts the number of non-cancelled flights with a departure delay greater than 15 min

     */

    public static DataStream<Query1Result> execute(DataStream<FlightEvent> flights) {

        return flights
                .filter(Query1Job::isTargetCarrier)
                .keyBy(FlightEvent::getCarrier)
                .window(TumblingEventTimeWindows.of(Time.hours(1)))
                .aggregate(
                        new Query1AggregateFunction(),
                        new Query1WindowFunction()
                );
    }

    private static boolean isTargetCarrier(FlightEvent event) {
        if (event == null || event.getCarrier() == null) {
            return false;
        }

        String carrier = event.getCarrier();

        return carrier.equals("AA")
                || carrier.equals("DL")
                || carrier.equals("UA")
                || carrier.equals("WN");
    }

    private static boolean isCancelled(FlightEvent event) {
        return Boolean.TRUE.equals(event.getIsCancelled());
    }

    private static boolean isDiverted(FlightEvent event) {
        return Boolean.TRUE.equals(event.getIsDiverted());
    }

    private static boolean isCompleted(FlightEvent event) {
        return !isCancelled(event) && !isDiverted(event);
    }

    private static class Query1AggregateFunction
            implements AggregateFunction<FlightEvent, Query1Accumulator, Query1Accumulator> {

        @Override
        public Query1Accumulator createAccumulator() {
            return new Query1Accumulator();
        }

        @Override
        public Query1Accumulator add(FlightEvent event, Query1Accumulator acc) {

            acc.incrementTotalCount();

            boolean cancelled = isCancelled(event);
            boolean diverted = isDiverted(event);
            boolean completed = isCompleted(event);

            if (cancelled) {
                acc.incrementCancelledCount();
            }

            if (diverted) {
                acc.incrementDivertedCount();
            }

            if (completed) {
                acc.incrementCompletedCount();

                if (event.getDepDelay() != null) {
                    acc.addDepDelay(event.getDepDelay());
                }
            }

            if (!cancelled
                    && event.getDepDelay() != null
                    && event.getDepDelay() > 15.0) {
                acc.incrementLateDepartureCount();
            }

            return acc;
        }

        @Override
        public Query1Accumulator getResult(Query1Accumulator acc) {
            return acc;
        }

        @Override
        public Query1Accumulator merge(Query1Accumulator acc1, Query1Accumulator acc2) {
            acc1.merge(acc2);
            return acc1;
        }
    }

    private static class Query1WindowFunction
            extends ProcessWindowFunction<Query1Accumulator, Query1Result, String, TimeWindow> {

        @Override
        public void process(
                String carrier,
                Context context,
                Iterable<Query1Accumulator> elements,
                Collector<Query1Result> out
        ) {
            Query1Accumulator acc = elements.iterator().next();

            long totalFlights = acc.getTotalCount();
            long completedFlights = acc.getCompletedCount();
            long cancelledFlights = acc.getCancelledCount();
            long divertedFlights = acc.getDivertedCount();

            double meanDepDelay = completedFlights == 0
                    ? 0.0
                    : acc.getDepDelaySum() / completedFlights;

            double cancellationRate = totalFlights == 0
                    ? 0.0
                    : (double) cancelledFlights / totalFlights;

            double lateDepartureRate = completedFlights == 0
                    ? 0.0
                    : (double) acc.getLateDepartureCount() / totalFlights;

            Query1Result result = new Query1Result();

            result.setCarrier(carrier);
            result.setWindowStart(Instant.ofEpochMilli(context.window().getStart()).toString());
            result.setWindowEnd(Instant.ofEpochMilli(context.window().getEnd()).toString());

            result.setTotalFlights(totalFlights);
            result.setCompletedFlights(completedFlights);
            result.setCancelledFlights(cancelledFlights);
            result.setDivertedFlights(divertedFlights);

            result.setMeanDepDelay(meanDepDelay);
            result.setCancellationRate(cancellationRate);
            result.setLateDepartureRate(lateDepartureRate);

            out.collect(result);
        }
    }

}
