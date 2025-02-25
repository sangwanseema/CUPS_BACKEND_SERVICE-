package com.flipkart.cups.resources;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.flipkart.cups.utils.DataExtractorUtil;
import com.flipkart.cups.utils.HttpClientUtil;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;

@Path("/cups")
@Produces(MediaType.APPLICATION_JSON)
public class CupsResource {
    // Base URL for fetching product data from the Zulu API
    private static final String ZULU_API_URL = "http://10.83.47.208/v2/product/xif0q/";

    /**
     * API endpoint to fetch product information.
     * Example: GET /cups/{productId}
   **/
    @GET
    @Path("/{productId}")
    public Response getProductInfo(@PathParam("productId") String productId) {
        // Construct the full API URL
        String url = ZULU_API_URL + productId;

        // Fetch response from external API using utility class
        Response zuluResponse = HttpClientUtil.fetchResponse(url);

        // If the response status is not 200 (OK), return an error response
        if (zuluResponse.getStatus() != 200) {
            String errorResponse = zuluResponse.readEntity(String.class);
            if(zuluResponse.getStatus() == 404) {
                return Response.status(404).entity("Product Id Not Found").build();
            }
            return Response.status(zuluResponse.getStatus())
                    .entity(errorResponse)
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }

        try {
            // Read API response as a JSON string
            String jsonResponse = zuluResponse.readEntity(String.class);

            // Extract required data (ar_info & related_entities) using utility class
            ObjectNode extractedData = DataExtractorUtil.extractRequiredData(jsonResponse);

            // Return the extracted data as a JSON response
            return Response.ok(extractedData.toString(), MediaType.APPLICATION_JSON).build();

        } catch (IOException e) {
            // Handle errors during JSON processing
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"Error processing response\"}")
                    .build();
        }
    }
}
