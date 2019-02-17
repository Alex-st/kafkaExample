package com.serializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.apache.kafka.common.serialization.Serializer;

import java.nio.charset.Charset;
import java.util.Map;

/**
 * Created by alex on 17.02.19.
 */
public class JsonSerializer<T> implements Serializer<T> {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void configure(Map map, boolean b) {

    }

    @Override
    @SneakyThrows
    public byte[] serialize(String s, T o) {
        return objectMapper.writeValueAsString(o).getBytes(Charset.forName("UTF-8"));
    }

    @Override
    public void close() {

    }
}
