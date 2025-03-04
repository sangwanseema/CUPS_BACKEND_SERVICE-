package com.flipkart.cups.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/healthcheck")
@Produces(MediaType.APPLICATION_JSON)
public class HealthCheck {
    @GET
    public Response healthCheck() {
        return Response.ok("Its Healthy").build();
    }
}
