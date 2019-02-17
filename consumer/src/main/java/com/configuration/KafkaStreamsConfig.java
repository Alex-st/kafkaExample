package com.configuration;

import com.dto.UserModel;
import com.serializer.JsonSerializer;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.Serializer;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static org.apache.kafka.streams.StreamsConfig.APPLICATION_ID_CONFIG;
import static org.apache.kafka.streams.StreamsConfig.BOOTSTRAP_SERVERS_CONFIG;

/**
 * Created by alex on 12.11.17.
 */
@Slf4j
@Configuration
@Profile("stream")
public class KafkaStreamsConfig {
    @Value("${kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${kafka.topic}")
    private String topic;

    @Bean
    public StreamsBuilder streamsBuilder(Serializer<UserModel> jsonSerializer,
                                         Deserializer<UserModel> userModelDeserializer) {
        StreamsBuilder streamsBuilder = new StreamsBuilder();
        Serde<UserModel> userModelSerde = Serdes.serdeFrom(jsonSerializer, userModelDeserializer);
        //DSL example
        KStream<String, UserModel> kStream = streamsBuilder.stream(topic, Consumed.with(Serdes.String(), userModelSerde))
                .peek((key, value) -> log.info("Message {} was retrieved from kafka", value))
                .mapValues(userModel -> userModel.toBuilder()
                        .id(userModel.getId())
                        .username(userModel.getUsername().toUpperCase())
                        .build());


        return streamsBuilder;
    }

    @Bean
    public Deserializer<UserModel> userModelDeserializer() {
        return new JsonDeserializer<>(UserModel.class);
    }

    @Bean
    public Serializer<UserModel> jsonSerializer() {
        return new JsonSerializer<>();
    }

    @Bean
    public KafkaStreams kafkaStreams(StreamsBuilder streamsBuilder) {
        KafkaStreams kafkaStreams = new KafkaStreams(streamsBuilder.build(), streamConfig());
        kafkaStreams.start();
        log.info("---------STREAMS UP----------");
        return kafkaStreams;
    }

    private Properties streamConfig() {
        Map<String, Object> props = new HashMap<>();
        props.put(BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(APPLICATION_ID_CONFIG, "Streaming-QuickStart");

        Properties properties = new Properties();
        properties.putAll(props);
        return properties;
    }

}
