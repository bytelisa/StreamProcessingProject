package it.uniroma2.sabd2;

import it.uniroma2.sabd2.model.FlightEvent;
import it.uniroma2.sabd2.model.Query1Result;
import it.uniroma2.sabd2.queries.Query1Job;
import it.uniroma2.sabd2.sinks.SinkFactory;
import it.uniroma2.sabd2.source.FlightEventSource;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;


public class Main {
    public static void main(String[] args) {

        // Event Source (from Kafka)
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        // todo REMOVE THIS LATER!!
        env.setParallelism(1);

        // need  checkpointing to use FileSystem FileSink connector
        env.enableCheckpointing(10_000);

        try {
            DataStream <FlightEvent> flightEvents = FlightEventSource.sourceEvents(env);

            // todo calcolo dei tempi esterno al lancio delle query
            // Query 1
            DataStream <Query1Result> query1Results = Query1Job.execute(flightEvents);
            query1Results.print();

            query1Results
                    .map(Query1Result::toCsv)
                    .print("Q1 CSV");

            SinkFactory.addQ1CSVSink(query1Results, "results/q1");

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