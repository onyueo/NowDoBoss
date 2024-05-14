package com.ssafy.backend.global.component.sse;

import com.ssafy.backend.global.component.sse.service.SseEmitterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ScheduleUpdateTask {
    private final SseEmitterService sseEmitterService;

    @Scheduled(fixedDelay = 10 * 60 * 1000) // 10분 간격
    public void broadcastPeriodicUpdates() {
        sseEmitterService.broadcastUpdates();
    }
}