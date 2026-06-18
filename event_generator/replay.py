"""
    Class responsibility: simulate real time stream of flight data, by reading data from the processed JSONL file.

    Notes:
        - 1 EVENT = 1 FLIGHT
        - EVENT TIME: generated using date and CRS_DEP_TIME
        - OUT OF ORDER EVENTS: kept as out of order, managed using
            - watermarking strategy
            - max allowed lateness
            explanation:
        - SHORTER TIME SCALE: events are accelerated to allow event simulation and analysis
            - acceleration factor:
            - explanation:
        - EVENT FORMAT: JSON
            - explanation: easy and readable, industry standard


"""
from kafka import KafkaProducer
import json
from pathlib import Path

PROJECT_ROOT = Path(__file__).resolve().parents[1]
INPUT_FILE = PROJECT_ROOT / "data" / "processed" / "flights_sorted.jsonl"

KFKA_BOOTSTRAP_SERVERS = "localhost:9092"
TOPIC = "flights"


def main():

    # set up kafka producer (port is the broker as defined in docker-compose.yml)
    producer = KafkaProducer(
        bootstrap_servers='localhost:9092',
        value_serializer=lambda value: value.encode("utf-8"),
        acks="all",
    )

    sent = 0
    flight_events = []

    # read recorded events and publish on kafka
    try:
        with open(INPUT_FILE) as f:

            for event in f:
                event = event.strip()

                if not event:
                    continue

                flight_events.append(json.loads(event))
                print(event)

                producer.send("flights", event)

                sent += 1
                if sent % 10_000 == 0:
                    print(f"Sent {sent} events")

        producer.flush()
        print(f"Replay completed. Sent {sent} events to topic '{TOPIC}'.")

    finally:
        producer.close()




if __name__ == "__main__":
    main()