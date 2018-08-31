package com.services.storage;

import com.dto.UserDto;
import com.services.MainService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;

/**
 * Created by Oleksandr on 6/16/2017.
 */
@Slf4j
@AllArgsConstructor
public class MainServiceImpl implements MainService {

    private final KafkaTemplate<String, UserDto> kafkaTemplate;

    private final String topic;

    @Override
    public void putUser(UserDto dto) {
        log.info("Sending user {} to topic {}", dto, topic);
        kafkaTemplate.send(topic, dto);
    }
}
