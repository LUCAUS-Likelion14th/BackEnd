package com.example.lucaus26th.service.booth;

import com.example.lucaus26th.domain.booth.Booth;
import com.example.lucaus26th.domain.stamp.StampBooth;
import com.example.lucaus26th.dto.response.booth.BoothResponseDto;
import com.example.lucaus26th.enums.BoothLocation;
import com.example.lucaus26th.global.exception.BusinessException;
import com.example.lucaus26th.global.exception.ErrorCode;
import com.example.lucaus26th.repository.booth.BoothRepository;
import com.example.lucaus26th.repository.booth.CategoryRepository;
import com.example.lucaus26th.repository.stamp.StampBoothRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BoothCacheService {

    private final BoothRepository boothRepository;
    private final CategoryRepository categoryRepository;
    private final StampBoothRepository stampBoothRepository;

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

    @Cacheable(value = "booth", key = "'stamp_' + #boothId")
    public List<BoothResponseDto.Lists> getBoothStamp(){
        return stampBoothRepository.findAll().stream()
                .map(StampBooth::getBooth)
                .map(booth -> BoothResponseDto.Lists.fromEntity(booth, false))
                .toList();
    }


    @Cacheable(value = "booth", key = "'detail_' + #boothId")
    public BoothResponseDto.Detail getBoothDetail(Long boothId) {
        Booth booth = boothRepository.findById(boothId)
                .orElseThrow(() -> new BusinessException(ErrorCode.BOOTH_NOT_FOUND));

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

        // 유효한 location 값인지 검증
        boolean validLocation = Arrays.stream(BoothLocation.values())
                .anyMatch(bl -> bl.getDescription().equals(location));
        if (!validLocation) throw new BusinessException(ErrorCode.WRONG_BOOTH_LOCATION);

        return booth.getLocations().stream()
                .anyMatch(bl -> bl.getDescription().equals(location));
    }

    private boolean categoryFilter(Booth booth, String category) {
        if (category == null) return true;

        // 유효한 category 값인지 검증
        boolean validCategory = categoryRepository.existsByName(category);
        if (!validCategory) throw new BusinessException(ErrorCode.CATEGORY_NOT_FOUND);

        return booth.getCategoryNames().contains(category);
    }

    private boolean dateFilter(Booth booth, String date) {
        if (date == null) return true;

        // "MMDD" 형식 검증
        if (date.length() != 4 || !date.matches("\\d{4}")) {
            throw new BusinessException(ErrorCode.WRONG_DATE_FORMAT);
        }

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