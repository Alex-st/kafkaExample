package com.configuration;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStreamBuilder;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KStreamBuilderFactoryBean;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

//todo implement
public class KafkaStreamsConfiguration {

    private static final String GROUP_ID = "mytopic";

    @Value("${kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${kafka.topic}")
    private String topic;

    @Bean
    public FactoryBean<KStreamBuilder> kStreamBuilder() {
        StreamsConfig streamsConfig = new StreamsConfig(createStreamProperties());
        return new KStreamBuilderFactoryBean(streamsConfig);
    }

    private Properties createStreamProperties() {
        Properties properties = new Properties();
        properties.put(StreamsConfig.APPLICATION_ID_CONFIG, "kafka-consumer-app");
        // list of host:port pairs used for establishing the initial connections to the Kafka cluster
        properties.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        properties.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        // allows a pool of processes to divide the work of consuming and processing records
        //props.put(ConsumerConfig.GROUP_ID_CONFIG, GROUP_ID);

        return properties;
    }
}
