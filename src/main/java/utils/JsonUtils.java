package utils;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtils {

    public static String toJson(Object object) {
        ObjectMapper mapper = new ObjectMapper();

        try {
            return mapper.writeValueAsString(object);
        } catch (Exception e) {
            throw new RuntimeException("Failed to convert object to JSON", e);
        }
    }
}