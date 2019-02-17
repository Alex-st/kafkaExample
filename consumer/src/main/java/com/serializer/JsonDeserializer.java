package com.serializer;

import com.dto.UserModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.apache.kafka.common.serialization.Deserializer;

import java.util.Map;

/**
 * Created by alex on 28.01.19.
 */
public class JsonDeserializer<T> implements Deserializer<T> {

    private Class<T> deserializedClass;
    private ObjectMapper objectMapper;

    public JsonDeserializer(Class<T> deserializedClass) {
        objectMapper = new ObjectMapper();
        this.deserializedClass = deserializedClass;
    }

    @Override
    public void configure(Map<String, ?> map, boolean b) {

    }

    @Override
    @SneakyThrows
    public T deserialize(String s, byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        String messageString = new String(bytes);
        T object = objectMapper.readValue(messageString, deserializedClass);
        return object;
    }

    @Override
    public void close() {

    }
}
