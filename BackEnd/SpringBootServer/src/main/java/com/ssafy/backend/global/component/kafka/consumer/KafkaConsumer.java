package com.ssafy.backend.global.component.kafka.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.backend.domain.chat.dto.response.ChatMessageResponse;
import com.ssafy.backend.global.component.kafka.KafkaConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaConsumer {
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = KafkaConstants.KAFKA_TOPIC)
    public void handleChatMessage(String message) throws IOException {
        log.info("채팅 메시지 이벤트 수신 : {}", message);
        ChatMessageResponse chatMessage = objectMapper.readValue(message, ChatMessageResponse.class);
        log.info("변환 후 채팅 메시지 이벤트 수신 : {}", chatMessage);
        simpMessagingTemplate.convertAndSend("/topic/public/rooms/" + chatMessage.getChatRoomId(), chatMessage);
    }

    @KafkaListener(topics = KafkaConstants.KAFKA_TOPIC_ANALYSIS)
    public void handleCommercialAnalysis(String message) {
        log.info("상업 분석 메시지 수신 : {}", message);
        // TODO: 추가적인 로직 구현

    }
}