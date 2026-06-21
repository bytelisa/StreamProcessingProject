
import csv
import json
from pathlib import Path
from datetime import datetime, timezone

"""
Class Responsibility: prepare data for the replayer
    1. Preprocess flight events from CSV
    2. Compute event time.
    3. Convert fields to correct type.
    4. Write data on JSON and order  according to event time.
"""
PROJECT_ROOT = Path(__file__).resolve().parents[1]
INPUT_DIR = PROJECT_ROOT / "data"
OUTPUT_FILE = PROJECT_ROOT / "data" / "processed" / "flights_sorted.jsonl"



def compute_event_time(year, month, day, crs_dep_time):
    """
    Computes the logical event time from flight date and scheduled departure time.

    The returned value is an ISO/RFC3339 string with UTC timezone, e.g.:
    2025-01-01T08:30:00Z

    UTC is used as a technical convention for representing the logical event time.
    """

    if year is None or month is None or day is None or crs_dep_time is None:
        raise ValueError(
            f"Cannot compute eventTime with year={year}, month={month}, "
            f"day={day}, crsDepTime={crs_dep_time}"
        )

    # crs_dep_time is expected to be normalized as a 4-character HHMM string.
    if len(crs_dep_time) != 4 or not crs_dep_time.isdigit():
        raise ValueError(f"Invalid CRS_DEP_TIME format: {crs_dep_time}")

    hour = int(crs_dep_time[:2])
    minute = int(crs_dep_time[2:])

    if hour < 0 or hour > 23 or minute < 0 or minute > 59:
        raise ValueError(f"Invalid CRS_DEP_TIME value: {crs_dep_time}")

    event_dt = datetime(
        year=int(year),
        month=int(month),
        day=int(day),
        hour=hour,
        minute=minute,
        second=0,
        microsecond=0,
        tzinfo=timezone.utc,
    )

    # Python would output "+00:00"; replace it with "Z" for cleaner RFC3339/ISO style.
    return event_dt.isoformat().replace("+00:00", "Z")



def empty_to_none(value):
    """
    Converts empty CSV values to None.
    CSV fields are read as strings, so missing values usually appear as "".
    """
    if value is None:
        return None

    value = value.strip()

    if value == "":
        return None

    return value


def to_int(value):
    """
    Converts a CSV numeric field to int.
    Handles values like "10397" and also "10397.0" if present.
    """
    value = empty_to_none(value)

    if value is None:
        return None

    return int(float(value))


def to_float(value):
    """
    Converts a CSV numeric field to float.
    Empty values become None.
    """
    value = empty_to_none(value)

    if value is None:
        return None

    return float(value)


def to_bool_01(value):
    """
    Converts CSV flags like "0", "1", "0.0", "1.0" to boolean.
    """
    value = to_float(value)

    if value is None:
        return None

    if value == 0.0:
        return False

    if value == 1.0:
        return True

    raise ValueError(f"Invalid boolean flag value: {value}")


def normalize_crs_dep_time(value):
    """
    Normalizes CRS_DEP_TIME to a 4-character HHMM string.

    Examples:
    "5"    -> "0005"
    "30"   -> "0030"
    "830"  -> "0830"
    "1435" -> "1435"
    """
    value = empty_to_none(value)

    if value is None:
        return None

    # If the CSV stores it as "830.0", this handles it.
    value = str(int(float(value)))

    return value.zfill(4)


def build_flight_event(row):
    year = to_int(row["YEAR"])
    month = to_int(row["MONTH"])
    day = to_int(row["DAY_OF_MONTH"])
    time = normalize_crs_dep_time(row["CRS_DEP_TIME"])

    flight_event = {
        #event time will be added
        "eventTime": compute_event_time(year, month, day, time),

        "carrier": empty_to_none(row["OP_UNIQUE_CARRIER"]),
        "carrierFlightNum": empty_to_none(row["OP_CARRIER_FL_NUM"]),
        "crsDepTime": time,

        "originAirportId": to_int(row["ORIGIN_AIRPORT_ID"]),
        "destAirportId": to_int(row["DEST_AIRPORT_ID"]),

        "isCancelled": to_bool_01(row["CANCELLED"]),
        "isDiverted": to_bool_01(row["DIVERTED"]),

        "depDelay": to_float(row["DEP_DELAY"]),
    }
    return flight_event


def iter_csv_files():
    csv_files = sorted(INPUT_DIR.glob("*.csv"))

    if not csv_files:
        raise FileNotFoundError(f"No CSV files found in {INPUT_DIR}")

    return csv_files



def load_events():
    flight_events = []

    for csv_path in iter_csv_files():
        print(f"Reading {csv_path.name}")

        with open(csv_path, "r", newline="", encoding="utf-8") as csvfile:
            reader = csv.DictReader(csvfile)

        # todo remove i (DEBUG)
            for i, row in enumerate(reader):
                flight_event = build_flight_event(row)
                flight_events.append(flight_event)
                print(json.dumps(flight_event))

                if i == 25:
                    break

    return flight_events


def write_jsonl(flight_events):
    OUTPUT_FILE.parent.mkdir(parents=True, exist_ok=True)

    with open(OUTPUT_FILE, "w", encoding="utf-8") as outfile:
        for event in flight_events:
            outfile.write(json.dumps(event) + "\n")



def main():

    # parse CSV and load flight events
    flight_events = load_events()

    # order flight events
    flight_events.sort(key=lambda event: event["eventTime"])

    # write on JSONL file
    write_jsonl(flight_events)

    print(f"Wrote {len(flight_events)} events to {OUTPUT_FILE}")


if __name__ == "__main__":
    main()