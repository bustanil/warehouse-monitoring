# Warehouse Monitoring Microservices

Two separate Spring Reactive applications communicating via Kafka.

## Architecture

```
Sensors → Warehouse Service → Kafka → Monitoring Service → (Alarm)
```

### Warehouse Service
- Collects UDP sensor data
- Publishes measurement received events to Kafka
- Default ports: 3333 (temperature) and 4444 (humidity)

### Monitoring Service  
- Consumes measurement received events from Kafka messages
- Triggers threshold alarms

## Manual Build

```bash
# Build both services
cd warehouse && mvn clean package && cd ..
cd central-monitoring && mvn clean package && cd ..

# Start infrastructure
docker-compose up -d
```

## Run Locally

```bash
# Terminal 1: Warehouse Service
cd warehouse
mvn spring-boot:run

# Terminal 2: Monitoring Service  
cd central-monitoring
mvn spring-boot:run
```

## Test

```bash
# Send test data
echo "sensor_id=t1; value=40" | nc -u localhost 3333
echo "sensor_id=h1; value=65" | nc -u localhost 4444
```

## Configuration

Each service has independent configuration in `application.properties`. 
