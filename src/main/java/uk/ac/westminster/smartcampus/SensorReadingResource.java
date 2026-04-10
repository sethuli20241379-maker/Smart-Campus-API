package uk.ac.westminster.smartcampus;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SensorReadingResource {

    private static final Map<String, List<SensorReading>> history = new ConcurrentHashMap<>();

    private final String sensorId;

    public SensorReadingResource(String sensorId) {
        this.sensorId = sensorId;
    }

    public static Map<String, List<SensorReading>> getHistoryMap() {
        return history;
    }
}
