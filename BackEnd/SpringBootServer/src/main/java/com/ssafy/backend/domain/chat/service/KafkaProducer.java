package com.ssafy.backend.domain.chat.service;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class KafkaProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;
    public void publishMessage(String topic, String message) {
        kafkaTemplate.send(topic, message);
    }

}
