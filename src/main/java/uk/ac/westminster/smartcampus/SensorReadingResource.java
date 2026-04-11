package uk.ac.westminster.smartcampus;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SensorReadingResource {

    private static final Map<String, List<SensorReading>> history = new ConcurrentHashMap<>();

    private final String sensorId;

    public SensorReadingResource(String sensorId) {
        this.sensorId = sensorId;
    }

    @GET
    public Response getHistory() {
        List<SensorReading> readings = history.getOrDefault(sensorId, new ArrayList<>());
        return Response.ok(readings).build();
    }

    @POST
    public Response addReading(SensorReading reading) {
        history.computeIfAbsent(sensorId, k -> new ArrayList<>()).add(reading);

        Sensor parentSensor = SensorResource.getSensorsMap().get(sensorId);

        if (parentSensor != null) {
            parentSensor.setCurrentValue(reading.getValue());
        }

        return Response.status(Response.Status.CREATED).entity(reading).build();
    }

    public static Map<String, List<SensorReading>> getHistoryMap() {
        return history;
    }
}
