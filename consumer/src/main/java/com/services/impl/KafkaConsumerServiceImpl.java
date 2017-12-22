package com.services.impl;

import com.dto.UserModel;
import com.services.KafkaConsumerService;
import com.services.StorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

/**
 * Created by alex on 22.10.17.
 */
@Slf4j
@Service
public class KafkaConsumerServiceImpl implements KafkaConsumerService {

    @Autowired
    StorageService storageService;

    @Override
    @KafkaListener(topics = "${kafka.topic}")
    public UserModel getUserFromKafka(UserModel userModel) {
        log.info("User {} was got from kafka", userModel);
        storageService.putData(userModel);
        return userModel;
    }
}
