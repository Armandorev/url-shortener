package it.w0rd.persistence.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.w0rd.api.ShortenedUrl;

import java.io.IOException;

// TODO: replace OBJECT_MAPPER instances and use this instead
public class JsonHelper {

    public static ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static String serialize(ShortenedUrl shortenedUrl) throws JsonProcessingException {
        return OBJECT_MAPPER.writeValueAsString(shortenedUrl);
    }

    public static ShortenedUrl unserialize(String json) throws IOException {
        return OBJECT_MAPPER.readValue(json, ShortenedUrl.class);
    }
}
