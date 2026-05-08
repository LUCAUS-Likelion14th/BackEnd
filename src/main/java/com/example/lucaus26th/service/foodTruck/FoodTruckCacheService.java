package com.example.lucaus26th.service.foodTruck;


import com.example.lucaus26th.domain.foodTruck.FoodTruck;
import com.example.lucaus26th.dto.response.foodTruck.FoodTruckDetailResponseDto;
import com.example.lucaus26th.dto.response.foodTruck.FoodTruckResponseDto;
import com.example.lucaus26th.dto.response.foodTruck.HotFoodTruckResponseDto;
import com.example.lucaus26th.global.exception.BusinessException;
import com.example.lucaus26th.global.exception.ErrorCode;
import com.example.lucaus26th.repository.foodTruck.FoodTruckRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.MonthDay;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true) // 이거 추가!
public class FoodTruckCacheService {

    private final FoodTruckRepository foodTruckRepository;

    @Cacheable(value = "foodtruck", key = "'list_' + #location + '_' + #date")
    public List<FoodTruckResponseDto> getFoodTrucks(String location, String date) {
        List<FoodTruck> foodTrucks = foodTruckRepository.findAll();
        return foodTrucks.stream()
                .filter(ft -> locationFilter(ft, location))
                .filter(ft -> dateFilter(ft, date))
                .map(ft -> FoodTruckResponseDto.from(ft, false)) // liked는 false로 저장
                .toList();
    }

    @Cacheable(value = "foodtruck", key = "'detail_' + #foodTruckId")
    public FoodTruckDetailResponseDto getFoodTruckDetail(Long foodTruckId) {
        FoodTruck foodTruck = foodTruckRepository.findById(foodTruckId)
                .orElseThrow(() -> new BusinessException(ErrorCode.FOOD_TRUCK_NOT_FOUND));

        List<String> dates = foodTruck.getSettings().stream()
                .map(setting ->
                        setting.getDay() + " " +
                                setting.getStartAt().toString().substring(0, 5) +
                                " - " +
                                setting.getEndAt().toString().substring(0, 5))
                .toList();

        List<FoodTruckDetailResponseDto.MenuDto> menus = foodTruck.getMenus().stream()
                .map(menu -> FoodTruckDetailResponseDto.MenuDto.builder()
                        .name(menu.getName())
                        .price(menu.getPrice())
                        .image(menu.getImage())
                        .build())
                .toList();

        return FoodTruckDetailResponseDto.builder()
                .id(foodTruck.getId())
                .name(foodTruck.getName())
                .locationId(foodTruck.getLocationId())
                .location(foodTruck.getLocation())
                .image(foodTruck.getImage())
                .bestMenu(foodTruck.getBestMenu())
                .likeCount(foodTruck.getLikeCount())
                .liked(false) // liked는 false로 저장
                .foodTruckInfo(foodTruck.getFoodTruckInfo())
                .date(dates)
                .menu(menus)
                .build();
    }

    @Cacheable(value = "foodtruck", key = "'hot'")
    public List<HotFoodTruckResponseDto> getHotFoodTrucks() {
        List<FoodTruck> foodTrucks = foodTruckRepository.findTop3ByOrderByLikeCountDesc();
        return foodTrucks.stream()
                .map(ft -> HotFoodTruckResponseDto.from(ft, false)) // liked는 false로 저장
                .toList();
    }

    private boolean locationFilter(FoodTruck foodTruck, String location) {
        if (location == null || location.isBlank()) return true;
        return foodTruck.getLocation().equals(location);
    }

    private boolean dateFilter(FoodTruck foodTruck, String date) {
        if (date == null || date.isBlank()) return true;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMdd");
        MonthDay monthDay;
        try {
            monthDay = MonthDay.parse(date, formatter);
        } catch (DateTimeParseException e) {
            throw new BusinessException(ErrorCode.WRONG_DATE_FORMAT);
        }
        return foodTruck.getSettings().stream()
                .anyMatch(setting -> MonthDay.from(setting.getDate()).equals(monthDay));
    }
}
