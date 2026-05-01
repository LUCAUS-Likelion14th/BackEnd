package com.example.lucaus26th.service.foodTruck;


import com.example.lucaus26th.domain.foodTruck.FoodTruck;
import com.example.lucaus26th.dto.response.foodTruck.FoodTruckDetailResponseDto;
import com.example.lucaus26th.dto.response.foodTruck.FoodTruckResponseDto;
import com.example.lucaus26th.dto.response.foodTruck.HotFoodTruckResponseDto;
import com.example.lucaus26th.repository.foodTruck.FoodTruckRepository;
import com.example.lucaus26th.repository.foodTruck.FoodTruckSettingRepository;
import com.example.lucaus26th.repository.foodTruck.MenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.MonthDay;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FoodTruckCacheService {

    private final FoodTruckRepository foodTruckRepository;

    @Cacheable(value = "cache_10min", key = "'foodtruck_list_' + #location + '_' + #date")
    public List<FoodTruckResponseDto> getFoodTrucks(String location, String date) {
        List<FoodTruck> foodTrucks = foodTruckRepository.findAll();
        return foodTrucks.stream()
                .filter(ft -> locationFilter(ft, location))
                .filter(ft -> dateFilter(ft, date))
                .map(ft -> FoodTruckResponseDto.from(ft, false)) // liked는 false로 저장
                .toList();
    }

    @Cacheable(value = "cache_10min", key = "'foodtruck_detail_' + #foodTruckId")
    public FoodTruckDetailResponseDto getFoodTruckDetail(Long foodTruckId) {
        FoodTruck foodTruck = foodTruckRepository.findById(foodTruckId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 푸드트럭입니다."));

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

    @Cacheable(value = "cache_10min", key = "'hot_foodtrucks'")
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
        MonthDay monthDay = MonthDay.parse(date, formatter);
        return foodTruck.getSettings().stream()
                .anyMatch(setting -> MonthDay.from(setting.getDate()).equals(monthDay));
    }
}
