package com.serializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Deserializer;

import java.util.Map;

/**
 * Created by alex on 28.01.19.
 */
public class JsonDeserializer<T> implements Deserializer<T> {

    private ObjectMapper objectMapper;

    @Override
    public void configure(Map<String, ?> map, boolean b) {

    }

    @Override
    public T deserialize(String s, byte[] bytes) {
        return null;
    }

    @Override
    public void close() {

    }
}
