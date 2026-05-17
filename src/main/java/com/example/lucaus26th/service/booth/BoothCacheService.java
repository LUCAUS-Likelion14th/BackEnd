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
import com.example.lucaus26th.domain.booth.BoothSetting;

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

    public BoothSetting getDisplaySetting(Booth booth, String date) {

        String targetDate;
        if (date == null) {
            targetDate = LocalDate.now(ZoneId.of("Asia/Seoul"))
                    .format(DateTimeFormatter.ofPattern("MMdd"));
        } else {
            targetDate = date;
        }
        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("MMdd");

        MonthDay monthDay =
                MonthDay.parse(targetDate, formatter);

        return booth.getSettings().stream()
                .filter(setting ->
                        MonthDay.from(setting.getDate())
                                .equals(monthDay)
                )
                .findFirst()
                .orElse(null);
    }

    @Cacheable(value = "booth", key = "'list_' + #date + '_' + #location + '_' + #category + '_' + #search")
    public List<BoothResponseDto.Lists> getBoothList(String date, String location, String category, String search) {

        final String targetDate;

        if (date == null) {
            targetDate = LocalDate.now(ZoneId.of("Asia/Seoul"))
                    .format(DateTimeFormatter.ofPattern("MMdd"));
        } else {
            targetDate = date;
        }

        return boothRepository.findAll().stream()
                .map(booth -> {
                    BoothSetting setting =
                            getDisplaySetting(booth, targetDate);

                    return new Object[]{booth, setting};
                })
                .filter(arr -> arr[1] != null)
                .filter(arr -> {
                    BoothSetting setting = (BoothSetting) arr[1];
                    return location == null ||
                            (
                                    setting.getLocation() != null &&
                                            setting.getLocation()
                                                    .getDescription()
                                                    .equals(location)
                            );
                })

                .filter(arr -> {
                    Booth booth = (Booth) arr[0];
                    return categoryFilter(booth, category);
                })

                .filter(arr -> {
                    Booth booth = (Booth) arr[0];
                    return searchFilter(booth, search);
                })

                .map(arr -> {
                    Booth booth = (Booth) arr[0];
                    BoothSetting setting = (BoothSetting) arr[1];

                    return BoothResponseDto.Lists.fromEntity(
                            booth,
                            setting,
                            false
                    );
                })

                .toList();
    }

    @Cacheable(value = "booth", key = "'stamp'")

    public List<BoothResponseDto.Lists> getBoothStamp(){
        return stampBoothRepository.findAll().stream()
                .map(StampBooth::getBooth)
                .map(booth -> {
                    BoothSetting setting = booth.getSettings().stream()
                            .findFirst()
                            .orElse(null);

                    return BoothResponseDto.Lists.fromEntity(booth, setting, false);
                })
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

        String todayStr = today.format(
                DateTimeFormatter.ofPattern("MMdd")
        );

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
                .map(booth -> {
                    BoothSetting setting =
                            getDisplaySetting(booth, todayStr);

                    return BoothResponseDto.Hot.fromEntity(
                            booth,
                            setting,
                            false
                    );
                })
                .toList();
    }

    private boolean settingFilter(Booth booth, String date, String location) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMdd");
        MonthDay monthDay = MonthDay.parse(date, formatter);

        return booth.getSettings().stream()
                .anyMatch(setting -> {
                    boolean dateMatch = MonthDay.from(setting.getDate()).equals(monthDay);

                    boolean locationMatch = location == null ||
                                    (
                                            setting.getLocation() != null &&
                                                    setting.getLocation()
                                                            .getDescription()
                                                            .equals(location)
                                    );

                    return dateMatch && locationMatch;
                });
    }

    private boolean categoryFilter(Booth booth, String category) {
        if (category == null) return true;

        // 유효한 category 값인지 검증
        boolean validCategory = categoryRepository.existsByName(category);
        if (!validCategory) throw new BusinessException(ErrorCode.CATEGORY_NOT_FOUND);

        return booth.getCategoryNames().contains(category);
    }


    private boolean searchFilter(Booth booth, String search) {
        if (search == null) return true;

        String name = booth.getName() != null ? booth.getName() : "";
        String owner = booth.getOwner() != null ? booth.getOwner() : "";

        return name.contains(search) || owner.contains(search);
    }
}