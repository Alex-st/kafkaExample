package integration;

import com.dto.UserModel;
import com.services.storage.StorageService;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.kafka.test.rule.EmbeddedKafkaRule;
import org.springframework.kafka.test.utils.ContainerTestUtils;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;

/**
 * Created by alex on 23.10.17.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = com.Main.class)
@DirtiesContext
public class KafkaConsumerIntegrationTest {

    private static final String KAFKA_TOPIC = "testtopic";

    @ClassRule
    public static EmbeddedKafkaRule embeddedKafka = new EmbeddedKafkaRule(1, true, KAFKA_TOPIC);

    private KafkaTemplate<String, UserModel> template;

    @Autowired
    private KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry;

    @Autowired
    private StorageService storageService;

    @Before
    public void setUp() throws Exception {
        // set up the Kafka producer properties
        Map<String, Object> senderProperties = new HashMap<>();
        // list of host:port pairs used for establishing the initial connections to the Kakfa cluster
        senderProperties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, embeddedKafka.getEmbeddedKafka().getBrokersAsString());
        senderProperties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        senderProperties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        

        // create a Kafka producer factory
        ProducerFactory<String, UserModel> producerFactory =
                new DefaultKafkaProducerFactory<>(senderProperties);

        // create a Kafka template
        template = new KafkaTemplate<>(producerFactory);
        // set the default topic to send to
        template.setDefaultTopic(KAFKA_TOPIC);

        // wait until the partitions are assigned
        ContainerTestUtils.waitForAssignment(
                kafkaListenerEndpointRegistry.getListenerContainers().iterator().next(),
                embeddedKafka.getEmbeddedKafka().getPartitionsPerTopic());
    }

    @Test
    public void testKafkaConsumer() {
        UserModel userModel = createUserModel();
        template.sendDefault(userModel);

        await().atMost(2, TimeUnit.SECONDS)
                .until(() -> storageService.getAllUsers().size() == 2);
    }

    private UserModel createUserModel() {
        UserModel userModel = new UserModel();
        userModel.setId(1);
        userModel.setUsername("Ivanov");
        return userModel;
    }
}
