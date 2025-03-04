package com.flipkart.cups;

import com.flipkart.cups.resources.CupsResource;
import com.flipkart.cups.resources.HealthCheck;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.eclipse.jetty.servlets.CrossOriginFilter;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import java.util.EnumSet;

public class CupsBackendServiceApplication extends Application<CupsServiceConfig> {

    // CupsBackendServiceApplication entry point of the application.
    public static void main(String[] args) throws Exception {
        new CupsBackendServiceApplication().run(args);
    }


    // Returns the name of the Dropwizard application.
    @Override
    public String getName() {
        return "CUPS_SERVICE";
    }

    // Initializes the application. Currently, no specific bootstrap setup is required.
    @Override
    public void initialize(Bootstrap<CupsServiceConfig> bootstrap) {
        // No initialization logic needed for now
    }

   // Enables Cross-Origin Resource Sharing (CORS) to allow API access from different domains.
    private void enableCors(Environment environment) {
        // Register CORS filter to allow cross-origin requests
        final FilterRegistration.Dynamic cors = environment.servlets().addFilter("CORS", CrossOriginFilter.class);

        // Configure CORS parameters
        cors.setInitParameter(CrossOriginFilter.ALLOWED_ORIGINS_PARAM, "*"); // Allow requests from any origin
        cors.setInitParameter(CrossOriginFilter.ALLOWED_HEADERS_PARAM,
                "X-Requested-With,Content-Type,Accept,Origin,Authorization"); // Allow specific headers
        cors.setInitParameter(CrossOriginFilter.ALLOWED_METHODS_PARAM,
                "OPTIONS,GET,PUT,POST,DELETE,HEAD"); // Allow specific HTTP methods

        // Apply the CORS filter to all URL patterns
        cors.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");
    }

     // Runs the Dropwizard application by registering resources and enabling configurations.
    @Override
    public void run(CupsServiceConfig configuration, Environment environment) {
        // Enable CORS to allow cross-origin API requests
           enableCors(environment);
        // Register the API resource class (CupsResource) to handle requests
           environment.jersey().register(new CupsResource());
           environment.jersey().register(new HealthCheck());
    }
}
