"""
    Class responsibility: simulate real time stream of flight data, by reading data from the CSV file.

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
            - explanation: easy and readable


"""