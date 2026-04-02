package com.example.lucaus26th.service;

import com.example.lucaus26th.domain.Member;
import com.example.lucaus26th.domain.food.FoodLike;
import com.example.lucaus26th.domain.food.FoodTruck;
import com.example.lucaus26th.domain.food.FoodTruckSetting;
import com.example.lucaus26th.domain.food.Menu;
import com.example.lucaus26th.dto.request.FoodTruckRequestDto;
import com.example.lucaus26th.dto.request.FoodTruckSettingRequestDto;
import com.example.lucaus26th.dto.request.MenuRequestDto;
import com.example.lucaus26th.dto.response.FoodTruckResponseDto;
import com.example.lucaus26th.dto.response.FoodTruckSettingResponseDto;
import com.example.lucaus26th.dto.response.MenuResponseDto;
import com.example.lucaus26th.repository.food.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.MonthDay;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class FoodTruckService {
    private final FoodTruckRepository foodTruckRepository;
    private final FoodLikeRepository foodLikeRepository;
    private final MemberRepository memberRepository;
    private final MenuRepository menuRepository;
    private final FoodTruckSettingRepository foodTruckSettingRepository;

    // 푸드트럭 생성
    public FoodTruckResponseDto createFoodTruck(FoodTruckRequestDto request) {
        FoodTruck foodTruck = new FoodTruck(
                request.getName(),
                request.getLocationId(),
                request.getLocation(),
                request.getImage(),
                request.getBestMenu(),
                0L,
                request.getFoodTruckInfo()
        );

        FoodTruck savedFoodTruck = foodTruckRepository.save(foodTruck);
        return FoodTruckResponseDto.from(savedFoodTruck, false);
    }

    // 푸드트럭 수정
    public FoodTruckResponseDto updateFoodTruck(Long foodTruckId, FoodTruckRequestDto request) {
        FoodTruck foodTruck = foodTruckRepository.findById(foodTruckId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 푸드트럭입니다."));

        foodTruck.update(
                request.getName(),
                request.getLocationId(),
                request.getLocation(),
                request.getImage(),
                request.getBestMenu(),
                request.getFoodTruckInfo()
        );

        return FoodTruckResponseDto.from(foodTruck, false);
    }

    // 푸드트럭 삭제
    public void deleteFoodTruck(Long foodTruckId) {
        FoodTruck foodTruck = foodTruckRepository.findById(foodTruckId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 푸드트럭입니다."));

        foodTruckRepository.delete(foodTruck);
    }

    // 푸드트럭 좋아요 생성
    public void createFoodLike(Long foodTruckId, Long memberId) {
        FoodTruck foodTruck = foodTruckRepository.findById(foodTruckId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 푸드트럭입니다."));

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        if (foodLikeRepository.existsByFoodTruckIdAndMemberId(foodTruckId, memberId)) {
            throw new IllegalStateException("이미 좋아요를 누른 푸드트럭입니다.");
        }

        FoodLike foodLike = new FoodLike(foodTruck, member);
        foodLikeRepository.save(foodLike);

        foodTruck.increaseLikeCount();
    }

    // 푸드트럭 좋아요 삭제
    public void deleteFoodLike(Long foodTruckId, Long memberId) {
        FoodTruck foodTruck = foodTruckRepository.findById(foodTruckId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 푸드트럭입니다."));

        if (!foodLikeRepository.existsByFoodTruckIdAndMemberId(foodTruckId, memberId)) {
            throw new IllegalArgumentException("좋아요를 누르지 않은 푸드트럭입니다.");
        }

        foodLikeRepository.deleteByFoodTruckIdAndMemberId(foodTruckId, memberId);
        foodTruck.decreaseLikeCount();
    }

    // 메뉴 생성
    public MenuResponseDto createMenu(Long foodTruckId, MenuRequestDto dto) {
        FoodTruck foodTruck = foodTruckRepository.findById(foodTruckId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 푸드트럭입니다."));

        Menu menu = Menu.builder()
                .name(dto.getName())
                .price(dto.getPrice())
                .image(dto.getImage())
                .foodTruck(foodTruck)
                .build();

        Menu savedMenu = menuRepository.save(menu);
        return MenuResponseDto.from(savedMenu);
    }

    // 메뉴 수정
    public MenuResponseDto updateMenu(Long foodTruckId, Long menuId, MenuRequestDto dto) {
        Menu menu = menuRepository.findByIdAndFoodTruckId(menuId, foodTruckId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 메뉴입니다."));

        menu.update(dto.getName(), dto.getPrice(), dto.getImage());

        return MenuResponseDto.from(menu);
    }

    // 메뉴 삭제
    public void deleteMenu(Long foodTruckId, Long menuId) {
        Menu menu = menuRepository.findByIdAndFoodTruckId(menuId, foodTruckId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 메뉴입니다."));

        menuRepository.delete(menu);
    }

    // 푸드트럭 운영정보 생성
    public FoodTruckSettingResponseDto createSetting(Long foodTruckId, FoodTruckSettingRequestDto dto) {
        FoodTruck foodTruck = foodTruckRepository.findById(foodTruckId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 푸드트럭입니다."));

        FoodTruckSetting setting = new FoodTruckSetting(
                foodTruck,
                dto.getDate(),
                dto.getDay(),
                dto.getStartAt(),
                dto.getEndAt()
        );

        return FoodTruckSettingResponseDto.from(foodTruckSettingRepository.save(setting));
    }

    // 푸드트럭 운영정보 수정
    public FoodTruckSettingResponseDto updateSetting(Long foodTruckId, Long settingId, FoodTruckSettingRequestDto dto) {
        FoodTruckSetting setting = foodTruckSettingRepository
                .findByIdAndFoodTruckId(settingId, foodTruckId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 운영정보입니다."));

        setting.update(
                dto.getDate(),
                dto.getDay(),
                dto.getStartAt(),
                dto.getEndAt()
        );

        return FoodTruckSettingResponseDto.from(setting);
    }

    // 푸드트럭 운영정보 삭제
    public void deleteSetting(Long foodTruckId, Long settingId) {
        FoodTruckSetting setting = foodTruckSettingRepository
                .findByIdAndFoodTruckId(settingId, foodTruckId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 운영정보입니다."));

        foodTruckSettingRepository.delete(setting);
    }

    // 장소 필터링
    private boolean locationFilter(FoodTruck foodTruck, String location) {
        if (location == null || location.isBlank()) return true;
        return foodTruck.getLocation().equals(location);
    }

    // 날짜 필터랑
    private boolean dateFilter(FoodTruck foodTruck, String date) {
        if (date == null || date.isBlank()) return true;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMdd");
        MonthDay monthDay = MonthDay.parse(date, formatter);

        return foodTruck.getSettings().stream()
                .anyMatch(setting -> MonthDay.from(setting.getDate()).equals(monthDay));
    }

    // 전체 조횐
    public List<FoodTruckResponseDto> getFoodTrucks(String location, String date, Long memberId) {
        List<FoodTruck> foodTrucks = foodTruckRepository.findAll();

        return foodTrucks.stream()
                .filter(foodTruck -> locationFilter(foodTruck, location))
                .filter(foodTruck -> dateFilter(foodTruck, date))
                .map(foodTruck -> {
                    boolean liked = false;
                    if (memberId != null) {
                        liked = foodLikeRepository.existsByFoodTruckIdAndMemberId(foodTruck.getId(), memberId);
                    }
                    return FoodTruckResponseDto.from(foodTruck, liked);
                })
                .toList();
    }

}