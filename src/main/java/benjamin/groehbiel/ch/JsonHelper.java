package benjamin.groehbiel.ch;

import benjamin.groehbiel.ch.shortener.ShortenerHandle;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class JsonHelper {

    public static ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static String serialize(ShortenerHandle shortenerHandle) throws JsonProcessingException {
        return OBJECT_MAPPER.writeValueAsString(shortenerHandle);
    }

    public static ShortenerHandle unserialize(String json) throws IOException {
        return OBJECT_MAPPER.readValue(json, ShortenerHandle.class);
    }
}
