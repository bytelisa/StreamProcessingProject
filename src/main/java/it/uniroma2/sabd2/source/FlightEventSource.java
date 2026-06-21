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

    public static DataStream<FlightEvent> sourceEvents(StreamExecutionEnvironment env) {

        KafkaSource<String> source = KafkaSource.<String>builder()
                .setBootstrapServers("localhost:9092")
                .setTopics("flights")
                .setGroupId("flight-event-source-test")
                .setStartingOffsets(OffsetsInitializer.earliest())
                .setValueOnlyDeserializer(new SimpleStringSchema())
                .build();

        DataStream<String> rawStream = env.fromSource(
                source,
                // todo check watermarking strategy
                WatermarkStrategy.noWatermarks(),
                "Kafka Source"
        );

        DataStream<FlightEvent> flightEvents = rawStream
                .map((MapFunction<String, FlightEvent>) FlightEventSource::parseFlightEvent)
                .returns(FlightEvent.class)
                .assignTimestampsAndWatermarks(
                        WatermarkStrategy
                                // todo check watermarking strategy
                                 .<FlightEvent>forBoundedOutOfOrderness(Duration.ofSeconds(5))
                                .withTimestampAssigner(
                                        (event, recordTimestamp) -> parseEventTimeMillis(event.getEventTime())
                                )
                                .withIdleness(Duration.ofSeconds(10))
                );

//        flightEvents.print();


        return flightEvents;
    }

    private static FlightEvent parseFlightEvent(String rawJson) {
        return gson.fromJson(rawJson, FlightEvent.class);
    }

    private static long parseEventTimeMillis(String eventTime) {
        return Instant.parse(eventTime).toEpochMilli();
    }


    }