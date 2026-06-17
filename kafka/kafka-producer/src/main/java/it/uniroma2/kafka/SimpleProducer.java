package it.uniroma2.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import java.util.Properties;
import java.util.Random;

public class SimpleProducer {

    public static void main(String[] args) throws InterruptedException {

        //properties for producer
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("key.serializer", "org.apache.kafka.common.serialization.IntegerSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        //create producer
        try (Producer<Integer, String> producer = new KafkaProducer<>(props)) {
            var rnd = new Random();

            //send messages to topic "flink-events"
            int j = 1;
            while (true) {
                for (int i = 0; i < 100; i++) {
                    long ts = System.currentTimeMillis();
                    var message = String.format("%d;%d;%s;%f", 100 * (j - 1) + i, ts, "Test Message #" + i, rnd.nextDouble());
                    var producerRecord = new ProducerRecord<>("flink-events", i, message);
                    producer.send(producerRecord);
                    System.out.printf("Send: %s%n", message);
                }
                producer.flush();
                j = j + 1;
                Thread.sleep(5000);
            }
        }
    }
}
