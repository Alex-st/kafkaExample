package com.configuration;

import com.dto.UserModel;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.HashMap;
import java.util.Map;

import static org.apache.kafka.streams.StreamsConfig.APPLICATION_ID_CONFIG;
import static org.apache.kafka.streams.StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG;
import static org.apache.kafka.streams.StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG;
import static org.apache.kafka.streams.StreamsConfig.BOOTSTRAP_SERVERS_CONFIG;

/**
 * Created by alex on 12.11.17.
 */
@Configuration
@Profile("stream")
public class KafkaStreamsConfig {
    @Value("${kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${kafka.topic}")
    private String topic;

    @Bean
    public StreamsConfig streamConfig() {
        Map<String, Object> props = new HashMap<>();
        props.put(BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(APPLICATION_ID_CONFIG, "Streaming-QuickStart");
//        props.put(DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
//        props.put(DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());

        return new StreamsConfig(props);
    }

    @Bean
    public StreamsBuilder streamsBuilder() {
        StreamsBuilder streamsBuilder = new StreamsBuilder();

//        KStream<String, UserModel> kStream = streamsBuilder.stream(topic, Consumed.with(Serdes.String(), Serdes.))
    return streamsBuilder;
    }

//    @Bean
//    public KafkaStreams kafkaStreams(StreamsConfig streamsConfig,
//                                     StreamsBuilder streamsBuilder) {
//
//    }

}
