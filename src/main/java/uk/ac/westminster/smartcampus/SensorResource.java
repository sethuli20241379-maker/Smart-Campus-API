package uk.ac.westminster.smartcampus;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Path("/sensors")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SensorResource {

    private static final Map<String, Sensor> sensors = new ConcurrentHashMap<>();

    @POST
    public Response registerSensor(Sensor sensor) {

        if (sensor.getId() == null || sensors.containsKey(sensor.getId())) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Sensor ID is missing or already exists").build();
        }

        if (!RoomResource.getRoomsMap().containsKey(sensor.getRoomId())) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Error: The specified Room ID(" + sensor.getRoomId() + ") does not exist.")
                    .build();
        }

        Room parentRoom = RoomResource.getRoomsMap().get(sensor.getRoomId());
        parentRoom.getSensorIds().add(sensor.getId());

        sensors.put(sensor.getId(), sensor);
        return Response.status(Response.Status.CREATED).entity(sensor).build();
    }

    @PUT
    @Path("/{sensorId}")
    public Response updateSensorValue(@PathParam("sensorId") String sensorId, Sensor updatedSensor) {

        Sensor existingSensor = sensors.get(sensorId);

        if (existingSensor == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Error: Sensor with ID " + sensorId + " not found.")
                    .build();
        }

        existingSensor.setCurrentValue(updatedSensor.getCurrentValue());
        existingSensor.setStatus(updatedSensor.getStatus());

        return Response.ok(existingSensor).build();
    }

    public static Map<String, Sensor> getSensorsMap() {
        return sensors;
    }
}
