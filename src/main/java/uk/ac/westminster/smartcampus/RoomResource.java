package uk.ac.westminster.smartcampus;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Path("/rooms")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RoomResource {

    private static final Map<String, Room> rooms = new ConcurrentHashMap<>();

    @GET
    public Response getAllRooms() {
        return Response.ok(new ArrayList<>(rooms.values())).build();
    }

    @POST
    public Response createRoom(Room room){
        if (room.getId() == null || rooms.containsKey(room.getId())) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Room ID is missing or already exists.").build();
        }
        rooms.put(room.getId(), room);

        return Response.status(Response.Status.CREATED).entity(room).build();
    }

    @GET
    @Path("/{roomId}")
    public Response getRoom(@PathParam("roomId") String roomId) {
        Room room = rooms.get(roomId);
        if (room == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(room).build();
    }

    @DELETE
    @Path("/{roomId}")
    public Response deleteRoom(@PathParam("roomId") String roomId) {
        Room room = rooms.get(roomId);

        if (room = null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Error: Room with ID " + roomId + "does not exist")
                    .build();
        }

        if (room.getSensorIds() != null && !room.getSensorIds().isEmpty()) {
            return Response.status(Response.Status.CONFLICT)
                    .entity("Deletion Blocked: Room contains active sensors. Remove sensors before decommissioning.")
                    .build();
        }

        rooms.remove(roomId);

        return Response.noContent().build();
    }
}
