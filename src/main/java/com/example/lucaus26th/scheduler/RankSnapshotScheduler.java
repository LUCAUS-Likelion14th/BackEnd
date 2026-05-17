package com.example.lucaus26th.scheduler;

import com.example.lucaus26th.domain.logs.EventLog;
import com.example.lucaus26th.dto.response.booth.BoothResponseDto;
import com.example.lucaus26th.dto.response.foodTruck.HotFoodTruckResponseDto;
import com.example.lucaus26th.repository.logs.EventLogRepository;
import com.example.lucaus26th.service.booth.BoothService;
import com.example.lucaus26th.service.foodTruck.FoodTruckService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class RankSnapshotScheduler {

    private final EventLogRepository eventLogRepository;
    private final ObjectMapper objectMapper;
    private final BoothService boothService;
    private final FoodTruckService foodTruckService;

    @Scheduled(fixedRate = 600000)
    public void logBoothRank() {
        List<BoothResponseDto.Hot> booths = boothService.getBoothHot(null);
        for (int i = 0; i < booths.size(); i++) {
            try {
                eventLogRepository.save(EventLog.builder()
                        .eventType("booth_rank_snapshot")
                        .userId(null)
                        .sessionId(null)
                        .targetType("BOOTH")
                        .targetId((long) booths.get(i).getBooth_id())
                        .payload(objectMapper.writeValueAsString(Map.of(
                                "booth_id", booths.get(i).getBooth_id(),
                                "rank", i + 1
                        )))
                        .build());
            } catch (Exception e) {
                log.error("부스 랭크 스냅샷 저장 실패", e);
            }
        }
    }

    @Scheduled(fixedRate = 600000)
    public void logFoodTruckRank() {
        List<HotFoodTruckResponseDto> trucks = foodTruckService.getHotFoodTrucks(null);
        for (int i = 0; i < trucks.size(); i++) {
            try {
                eventLogRepository.save(EventLog.builder()
                        .eventType("foodtruck_rank_snapshot")
                        .userId(null)
                        .sessionId(null)
                        .targetType("FOODTRUCK")
                        .targetId(trucks.get(i).getId())
                        .payload(objectMapper.writeValueAsString(Map.of(
                                "foodtruck_id", trucks.get(i).getId(),
                                "rank", i + 1
                        )))
                        .build());
            } catch (Exception e) {
                log.error("푸드트럭 랭크 스냅샷 저장 실패", e);
            }
        }
    }
}