package uk.ac.westminster.smartcampus;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.LinkedHashMap;
import java.util.Map;

@Path("/")
public class DiscoveryResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDiscovery() {

        Map<String, Object> metadata = new LinkedHashMap<>();

        metadata.put("apiVersion", "1.0");

        Map<String, String> contact = new LinkedHashMap<>();
        contact.put("name", "Lead Backend Architect");
        contact.put("email", "admin@westminster.ac.uk");
        metadata.put("contactDetails", contact);

        Map<String, String> links = new LinkedHashMap<>();
        links.put("rooms", "/api/v1/rooms");
        links.put("sensors", "/api/v1/sensors");
        metadata.put("links", links);

        return Response.ok(metadata).build();
    }
}
