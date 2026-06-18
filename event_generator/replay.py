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
import time
from datetime import datetime


PROJECT_ROOT = Path(__file__).resolve().parents[1]
INPUT_FILE = PROJECT_ROOT / "data" / "processed" / "flights_sorted.jsonl"

KFKA_BOOTSTRAP_SERVERS = "localhost:9092"
TOPIC = "flights"

ACCELERATION_FACTOR = 7200  # 3600s (= 1h) becomes 0.5s



def parse_event_time(event_time: str) -> float:
    """
    Parses an ISO/RFC3339 timestamp like '2025-01-01T08:30:00Z'
    and returns epoch seconds.
    """
    dt = datetime.fromisoformat(event_time.replace("Z", "+00:00"))
    return dt.timestamp()



def main():

    # set up kafka producer (port is the broker as defined in docker-compose.yml)

    producer = KafkaProducer(
        bootstrap_servers='localhost:9092',
        # value_serializer=lambda value: value.encode("utf-8"), deprecated
        acks="all",
    )


    sent = 0
    flight_events = []
    previous_event_time = None

    # read recorded events and publish on kafka
    try:
        with open(INPUT_FILE) as f:

            for line in f:

                # clean line
                line = line.strip()

                if not line:
                    continue

                event = json.loads(line)

                flight_events.append(event)

                # compute replay time
                current_event_time = parse_event_time(event["eventTime"])

                # doesn't compute sleep time for the first flight event
                if previous_event_time is not None:
                    delta = current_event_time - previous_event_time

                    if delta < 0:
                        raise ValueError(
                            # todo check this
                            f"Input file is not ordered by eventTime: "
                            f"previous={previous_event_time}, current={current_event_time}"
                        )

                    sleep_time = delta / ACCELERATION_FACTOR

                    if sleep_time > 0:
                        time.sleep(sleep_time)

                producer.send(TOPIC, value=event.encode("utf-8"))
                print(event)

                previous_event_time = current_event_time

                sent += 1
                if sent % 10_000 == 0:
                    print(f"Sent {sent} events")

        producer.flush()
        print(f"Replay completed. Sent {sent} events to topic '{TOPIC}'.")

    finally:
        producer.close()
        print("done")



if __name__ == "__main__":
    main()