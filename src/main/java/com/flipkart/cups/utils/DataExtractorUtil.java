package com.flipkart.cups.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class DataExtractorUtil {

    // ObjectMapper instance for JSON processing
    private static final ObjectMapper objectMapper = new ObjectMapper();

    //Extracts the required data ('ar_info' and 'related_entities') from the given JSON response.


    public static ObjectNode extractRequiredData(String jsonResponse) throws IOException {
        // Parse the JSON response into a JsonNode object for easy navigation
        JsonNode jsonNode = objectMapper.readTree(jsonResponse);

        // Extract 'ar_info' field from the response
        JsonNode arInfo = jsonNode.path("data")
                .path("semanticAttributes")
                .path("attributeMap")
                .path("ar_info");

        // Extract 'relationships' field which contains related entities
        JsonNode relationships = jsonNode.path("data").path("relationships");

        // Create an array to store unique related entities
        ArrayNode relatedEntitiesArray = objectMapper.createArrayNode();
        Set<String> uniqueEntitiesSet = new HashSet<>();

        // Iterate through different relationship categories (e.g., ITEMIZATION, VARIANTS, etc.)
        relationships.fields().forEachRemaining(entry -> {
            JsonNode categoryNode = entry.getValue();

            // Iterate through each subcategory inside relationships
            categoryNode.fields().forEachRemaining(innerEntry -> {
                JsonNode list = innerEntry.getValue();

                // If the subcategory contains a list, process each item
                if (list.isArray()) {
                    for (JsonNode item : list) {
                        JsonNode relatedEntities = item.path("relatedEntities");

                        // If 'relatedEntities' is an array, process each entity
                        if (relatedEntities.isArray()) {
                            for (JsonNode entity : relatedEntities) {
                                String entityJsonString = entity.toString();

                                // Add only unique related entities to the final list
                                if (!uniqueEntitiesSet.contains(entityJsonString)) {
                                    uniqueEntitiesSet.add(entityJsonString);
                                    relatedEntitiesArray.add(entity);
                                }
                            }
                        }
                    }
                }
            });
        });

        // Create the final response JSON object
        ObjectNode responseNode = objectMapper.createObjectNode();
        responseNode.set("ar_info", arInfo); // Add extracted 'ar_info' field
        responseNode.set("related_entities", relatedEntitiesArray); // Add unique related entities

        return responseNode;
    }
}
