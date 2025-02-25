package com.flipkart.cups.utils;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Response;

public class HttpClientUtil {

    // Create a reusable JAX-RS client instance for making HTTP requests
    private static final Client client = ClientBuilder.newClient();

    //Fetches the response from the given URL using an HTTP GET request.

    public static Response fetchResponse(String url) {


        // Send a GET request and return the response
        return  client.target(url).request().get();
    }
}
