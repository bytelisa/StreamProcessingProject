# Stream Processing for Flight Data Analysis

## Project structure
### Directory Tree
### Notes
### Versions
### Prerequisites

## Useful commands

### 0. Activate the Python virtual environment
```bash
source .venv-wsl/bin/activate
```


### 1. Check Kafka topics on Broker
```bash
docker compose exec broker /opt/kafka/bin/kafka-topics.sh \
  --bootstrap-server broker:29092 \
  --list
```

### 2. Create a new topic "flights" with 1 partition 
```bash
docker compose exec broker /opt/kafka/bin/kafka-topics.sh \
  --bootstrap-server broker:29092 \
  --create \
  --topic flights \
  --partitions 1 \
  --replication-factor 1
```

### 3. Create a simple console consumer reading form "flights" topic
```bash
docker compose exec broker /opt/kafka/bin/kafka-console-consumer.sh \
  --bootstrap-server broker:29092 \
  --topic flights \
  --from-beginning
```

### 3. Preprocess the CSV dataset to create the flight events
```bash
python event_generator/preprocess.py
```

### 4. Launch the flight events producer
```bash
source .venv-wsl/bin/activate
python event_generator/replay.py
```