package com.services.plainkafka;

import com.dto.UserModel;

/**
 * Created by alex on 22.10.17.
 */
public interface KafkaConsumerService {

    UserModel getUserFromKafka(UserModel userModel);
}
