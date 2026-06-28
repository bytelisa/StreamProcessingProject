package it.uniroma2.sabd2.sinks;

import it.uniroma2.sabd2.model.Query1Result;
import it.uniroma2.sabd2.model.Query2Result;
import org.apache.flink.api.common.serialization.SimpleStringEncoder;
import org.apache.flink.configuration.MemorySize;
import org.apache.flink.connector.file.sink.FileSink;
import org.apache.flink.core.fs.Path;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.functions.sink.filesystem.bucketassigners.BasePathBucketAssigner;
import org.apache.flink.streaming.api.functions.sink.filesystem.rollingpolicies.DefaultRollingPolicy;

import java.time.Duration;
import java.util.Locale;

public class SinkFactory {

    // Q1 print
    // Q2 print

    public static void addQ1PrintSink(DataStream <Query1Result> q1results ) {
        q1results.print("Q1");
    }
    public static void addQ2PrintSink(DataStream <Query2Result> q2results ) {
        q2results.print("Q1");
    }

    // add Q1 sink to csv
    public static void addQ1CSVSink(DataStream<Query1Result> q1results, String outputPath) {
        DataStream<String> csvRows = q1results
                .map(SinkFactory::query1ResultToCsvRow)
                .name("Q1 Result to CSV Row");

        FileSink<String> sink = FileSink
                .forRowFormat(
                        new Path(outputPath),
                        new SimpleStringEncoder<String>("UTF-8")
                )
                .withBucketAssigner(new BasePathBucketAssigner<>()) // only use global bucket
                .withRollingPolicy(
                        DefaultRollingPolicy.builder()
                                .withRolloverInterval(Duration.ofMinutes(1))
                                .withInactivityInterval(Duration.ofSeconds(30))
                                .withMaxPartSize(MemorySize.ofMebiBytes(128))
                                .build()
                )
                .build();

        csvRows
                .sinkTo(sink)
                .name("Q1 CSV Sink");
    }



    private static String query1ResultToCsvRow(Query1Result result) {
        return String.format(
                Locale.US,
                "%s,%s,%s,%d,%d,%d,%d,%.4f,%.6f,%.6f",
                result.getWindowStart(),
                result.getWindowEnd(),
                result.getCarrier(),
                result.getTotalFlights(),
                result.getCompletedFlights(),
                result.getCancelledFlights(),
                result.getDivertedFlights(),
                result.getMeanDepDelay(),
                result.getCancellationRate(),
                result.getLateDepartureRate()
        );
    }

    // add Q2 sink to csv

    // add Q1 sink to influxdb
    // add q2 sink to influxdb
}


