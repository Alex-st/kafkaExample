package integration;

import com.dto.UserDto;
import com.services.MainService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.listener.config.ContainerProperties;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.test.rule.KafkaEmbedded;
import org.springframework.kafka.test.utils.ContainerTestUtils;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;


/**
 * Created by alex on 21.10.17.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = com.Main.class)
@DirtiesContext
@Slf4j
public class KafkaProducerIntegrationTest {

    private static final String KAFKA_TOPIC = "testtopic";

    @ClassRule
    public static KafkaEmbedded embeddedKafka = new KafkaEmbedded(1, true, KAFKA_TOPIC);

    @Autowired
    private MainService mainService;

    private KafkaMessageListenerContainer<String, UserDto> container;

    private BlockingQueue<ConsumerRecord<String, UserDto>> records;

    @Before
    public void setUp() throws Exception {
        // set up the Kafka consumer properties
        Map<String, Object> consumerProperties =
                KafkaTestUtils.consumerProps("sender", "false", embeddedKafka);

        // create a Kafka consumer factory
        DefaultKafkaConsumerFactory<String, UserDto> consumerFactory =
                new DefaultKafkaConsumerFactory<>(consumerProperties);
        consumerFactory.setValueDeserializer(new JsonDeserializer(UserDto.class));

        // set the topic that needs to be consumed
        ContainerProperties containerProperties = new ContainerProperties(KAFKA_TOPIC);

        // create a Kafka MessageListenerContainer
        container = new KafkaMessageListenerContainer<>(consumerFactory, containerProperties);

        // create a thread safe queue to store the received message
        records = new LinkedBlockingQueue<>();

        // setup a Kafka message listener
        container.setupMessageListener((MessageListener<String, UserDto>) record -> {
                log.debug("test-listener received message='{}'", record.toString());
                records.add(record);
            });

        // start the container and underlying message listener
        container.start();

        // wait until the container has the required number of assigned partitions
        ContainerTestUtils.waitForAssignment(container, embeddedKafka.getPartitionsPerTopic());
    }

    @Test
    public void putMessageToKafkaSuccessfully() throws InterruptedException {
        UserDto userDto = createUserDto();
        mainService.putUser(userDto);

        ConsumerRecord<String, UserDto> received = records.poll(10, TimeUnit.SECONDS);

        UserDto resultedUser = received.value();
        assertThat(resultedUser, is(userDto));
    }

    @After
    public void tearDown() {
        // stop the container
        container.stop();
    }

    private UserDto createUserDto() {
        UserDto userDto = new UserDto();
        userDto.setId(1);
        userDto.setUsername("Ivanov");
        return userDto;
    }

}
