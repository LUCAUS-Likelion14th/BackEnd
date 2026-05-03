package com.example.lucaus26th.service.booth;

import com.example.lucaus26th.domain.booth.Booth;
import com.example.lucaus26th.dto.response.booth.BoothResponseDto;
import com.example.lucaus26th.repository.booth.BoothRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BoothCacheService {

    private final BoothRepository boothRepository;

    @Cacheable(value = "booth", key = "'list_' + #date + '_' + #location + '_' + #category + '_' + #search")
    public List<BoothResponseDto.Lists> getBoothList(String date, String location, String category, String search) {
        return boothRepository.findAll().stream()
                .filter(booth -> locationFilter(booth, location))
                .filter(booth -> categoryFilter(booth, category))
                .filter(booth -> dateFilter(booth, date))
                .filter(booth -> searchFilter(booth, search))
                .map(booth -> BoothResponseDto.Lists.fromEntity(booth, false))
                .toList();
    }

    @Cacheable(value = "booth", key = "'detail_' + #boothId")
    public BoothResponseDto.Detail getBoothDetail(Long boothId) {
        Booth booth = boothRepository.findById(boothId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 부스입니다 :" + boothId));

        return BoothResponseDto.Detail.fromEntity(booth, false);
    }

    @Cacheable(value = "booth", key = "'hot'")
    public List<BoothResponseDto.Hot> getBoothHot() {
        ZonedDateTime nowSeoul = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));
        LocalDate today = nowSeoul.toLocalDate();
        LocalTime nowTime = nowSeoul.toLocalTime();

        return boothRepository.findAll().stream()
                .filter(booth -> booth.getSettings().stream()
                        .anyMatch(setting ->
                                setting.getDate().equals(today) &&
                                        !nowTime.isBefore(setting.getStartAt()) &&
                                        !nowTime.isAfter(setting.getEndAt())
                        )
                )
                .sorted(Comparator.comparing(Booth::getLikeCount).reversed())
                .limit(3)
                .map(booth -> BoothResponseDto.Hot.fromEntity(booth, false))
                .toList();
    }

    private boolean locationFilter(Booth booth, String location) {
        if (location == null) return true;
        return booth.getLocation().getDescription().equals(location);
    }

    private boolean categoryFilter(Booth booth, String category) {
        if (category == null) return true;
        return booth.getCategoryNames().contains(category);
    }

    private boolean dateFilter(Booth booth, String date) {
        if (date == null) return true;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMdd");
        MonthDay monthDay = MonthDay.parse(date, formatter);

        return booth.getSettings().stream()
                .anyMatch(s -> MonthDay.from(s.getDate()).equals(monthDay));
    }

    private boolean searchFilter(Booth booth, String search) {
        if (search == null) return true;

        String name = booth.getName() != null ? booth.getName() : "";
        String owner = booth.getOwner() != null ? booth.getOwner() : "";

        return name.contains(search) || owner.contains(search);
    }
}