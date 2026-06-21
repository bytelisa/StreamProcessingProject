package it.uniroma2.sabd2;

import it.uniroma2.sabd2.model.FlightEvent;
import it.uniroma2.sabd2.model.Query1Result;
import it.uniroma2.sabd2.queries.Query1Job;
import it.uniroma2.sabd2.source.FlightEventSource;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;


public class Main {
    public static void main(String[] args) {

        // Event Source (from Kafka)
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();


        try {
            DataStream <FlightEvent> flightEvents = FlightEventSource.sourceEvents(env);

            // Query 1
            DataStream <Query1Result> query1Results = Query1Job.execute(flightEvents);
            query1Results.print();

            // Query 2
            //DataStream <Query2Result> query2Results = Query2Job.execute(env, FlightEvents);


            env.execute("Flight Event Source Test");

        } catch (Exception e) {
            System.out.println(
                    "[Error] Received the following exception when trying to execute the pipeline:"
                    + e.getMessage());
        }

    }
}