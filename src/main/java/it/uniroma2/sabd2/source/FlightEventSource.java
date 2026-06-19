package it.uniroma2.sabd2.source;

import com.google.gson.Gson;
import it.uniroma2.sabd2.model.FlightEvent;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.time.Duration;
import java.time.Instant;

public class FlightEventSource {

    private static final Gson gson = new Gson();

    public static void main(String[] args) throws Exception {

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        KafkaSource<String> source = KafkaSource.<String>builder()
                .setBootstrapServers("localhost:9092")
                .setTopics("flights")
                .setGroupId("flight-event-source-test")
                .setStartingOffsets(OffsetsInitializer.earliest())
                .setValueOnlyDeserializer(new SimpleStringSchema())
                .build();

        DataStream<String> rawStream = env.fromSource(
                source,
                WatermarkStrategy.noWatermarks(),
                "Kafka Source"
        );

        DataStream<FlightEvent> flightEvents = rawStream
                .map((MapFunction<String, FlightEvent>) FlightEventSource::parseFlightEvent)
                .returns(FlightEvent.class)
                .assignTimestampsAndWatermarks(
                        WatermarkStrategy
                                .<FlightEvent>forMonotonousTimestamps()
                                // If you later introduce out-of-order events, use:
                                // .<FlightEvent>forBoundedOutOfOrderness(Duration.ofSeconds(5))
                                .withTimestampAssigner(
                                        (event, recordTimestamp) -> parseEventTimeMillis(event.getEventTime())
                                )
                                .withIdleness(Duration.ofSeconds(10))
                );

        flightEvents.print();

        env.execute("Flight Event Source Test");
    }

    private static FlightEvent parseFlightEvent(String rawJson) {
        return gson.fromJson(rawJson, FlightEvent.class);
    }

    private static long parseEventTimeMillis(String eventTime) {
        return Instant.parse(eventTime).toEpochMilli();
    }
}