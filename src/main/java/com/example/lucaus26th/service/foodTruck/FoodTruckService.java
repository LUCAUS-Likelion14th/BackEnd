package com.example.lucaus26th.service.foodTruck;

import com.example.lucaus26th.domain.Member;
import com.example.lucaus26th.domain.foodTruck.FoodTruckLike;
import com.example.lucaus26th.domain.foodTruck.FoodTruck;
import com.example.lucaus26th.domain.foodTruck.FoodTruckSetting;
import com.example.lucaus26th.domain.foodTruck.Menu;
import com.example.lucaus26th.dto.request.foodTruck.FoodTruckRequestDto;
import com.example.lucaus26th.dto.request.foodTruck.FoodTruckSettingRequestDto;
import com.example.lucaus26th.dto.request.foodTruck.MenuRequestDto;
import com.example.lucaus26th.dto.response.foodTruck.*;
import com.example.lucaus26th.global.S3Service;
import com.example.lucaus26th.repository.foodTruck.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class FoodTruckService {
    private final FoodTruckRepository foodTruckRepository;
    private final FoodTruckLikeRepository foodTruckLikeRepository;
    private final MemberRepository memberRepository;
    private final MenuRepository menuRepository;
    private final FoodTruckSettingRepository foodTruckSettingRepository;
    private final S3Service s3Service;
    private final FoodTruckCacheService foodTruckCacheService;

    // 푸드트럭 생성
    @CacheEvict(value = "cache_10min", allEntries = true)
    public FoodTruckResponseDto createFoodTruck(FoodTruckRequestDto request) {
        String imageUrl;
        try{
            imageUrl = s3Service.upload(request.getImage(), "foodtruck");
        } catch(IOException e){
            throw new RuntimeException("S3 이미지 업로드에 실패했습니다.", e);
        }
        FoodTruck foodTruck = new FoodTruck(
                request.getName(),
                request.getLocationId(),
                request.getLocation(),
                imageUrl,
                request.getBestMenu(),
                0L,
                request.getFoodTruckInfo()
        );

        FoodTruck savedFoodTruck = foodTruckRepository.save(foodTruck);
        return FoodTruckResponseDto.from(savedFoodTruck, false);
    }

    // 푸드트럭 수정
    @CacheEvict(value = "cache_10min", allEntries = true)
    public FoodTruckResponseDto updateFoodTruck(Long foodTruckId, FoodTruckRequestDto request) {
        FoodTruck foodTruck = foodTruckRepository.findById(foodTruckId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 푸드트럭입니다."));

        String imageUrl = s3Service.uploadIfPresent(request.getImage(), "foodtruck");
        foodTruck.update(
                request.getName(),
                request.getLocationId(),
                request.getLocation(),
                imageUrl,
                request.getBestMenu(),
                request.getFoodTruckInfo()
        );

        return FoodTruckResponseDto.from(foodTruck, false);
    }

    // 푸드트럭 삭제
    @CacheEvict(value = "cache_10min", allEntries = true)
    public void deleteFoodTruck(Long foodTruckId) {
        FoodTruck foodTruck = foodTruckRepository.findById(foodTruckId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 푸드트럭입니다."));

        foodTruckRepository.delete(foodTruck);
    }

    // 푸드트럭 좋아요 생성
    @CacheEvict(value = "cache_10min", allEntries = true)
    public void createFoodLike(Long foodTruckId, Long memberId) {
        FoodTruck foodTruck = foodTruckRepository.findById(foodTruckId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 푸드트럭입니다."));

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        if (foodTruckLikeRepository.existsByFoodTruckIdAndMemberId(foodTruckId, memberId)) {
            throw new IllegalStateException("이미 좋아요를 누른 푸드트럭입니다.");
        }

        FoodTruckLike foodTruckLike = new FoodTruckLike(foodTruck, member);
        foodTruckLikeRepository.save(foodTruckLike);

        foodTruck.increaseLikeCount();
    }

    // 푸드트럭 좋아요 삭제
    @CacheEvict(value = "cache_10min", allEntries = true)
    public void deleteFoodLike(Long foodTruckId, Long memberId) {
        FoodTruck foodTruck = foodTruckRepository.findById(foodTruckId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 푸드트럭입니다."));

        if (!foodTruckLikeRepository.existsByFoodTruckIdAndMemberId(foodTruckId, memberId)) {
            throw new IllegalArgumentException("좋아요를 누르지 않은 푸드트럭입니다.");
        }

        foodTruckLikeRepository.deleteByFoodTruckIdAndMemberId(foodTruckId, memberId);
        foodTruck.decreaseLikeCount();
    }

    // 메뉴 생성
    @CacheEvict(value = "cache_10min", allEntries = true)
    public MenuResponseDto createMenu(Long foodTruckId, MenuRequestDto dto) {
        FoodTruck foodTruck = foodTruckRepository.findById(foodTruckId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 푸드트럭입니다."));

        String imageUrl;
        try{
            imageUrl = s3Service.upload(dto.getImage(), "foodtruck/menu");
        } catch(IOException e){
            throw new RuntimeException("S3 이미지 업로드에 실패했습니다.", e);
        }
        Menu menu = Menu.builder()
                .name(dto.getName())
                .price(dto.getPrice())
                .image(imageUrl)
                .foodTruck(foodTruck)
                .build();

        Menu savedMenu = menuRepository.save(menu);
        return MenuResponseDto.from(savedMenu);
    }

    // 메뉴 수정
    @CacheEvict(value = "cache_10min", allEntries = true)
    public MenuResponseDto updateMenu(Long foodTruckId, Long menuId, MenuRequestDto dto) {
        Menu menu = menuRepository.findByIdAndFoodTruckId(menuId, foodTruckId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 메뉴입니다."));

        String imageUrl = s3Service.uploadIfPresent(dto.getImage(), "foodtruck/menu");
        menu.update(dto.getName(), dto.getPrice(), imageUrl);

        return MenuResponseDto.from(menu);
    }

    // 메뉴 삭제
    @CacheEvict(value = "cache_10min", allEntries = true)
    public void deleteMenu(Long foodTruckId, Long menuId) {
        Menu menu = menuRepository.findByIdAndFoodTruckId(menuId, foodTruckId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 메뉴입니다."));

        menuRepository.delete(menu);
    }

    // 푸드트럭 운영정보 생성
    @CacheEvict(value = "cache_10min", allEntries = true)
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
    @CacheEvict(value = "cache_10min", allEntries = true)
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
    @CacheEvict(value = "cache_10min", allEntries = true)
    public void deleteSetting(Long foodTruckId, Long settingId) {
        FoodTruckSetting setting = foodTruckSettingRepository
                .findByIdAndFoodTruckId(settingId, foodTruckId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 운영정보입니다."));

        foodTruckSettingRepository.delete(setting);
    }

    // 전체 조회
    public List<FoodTruckResponseDto> getFoodTrucks(String location, String date, Long memberId) {
        List<FoodTruckResponseDto> foodTrucks =
                foodTruckCacheService.getFoodTrucks(location, date);

        if (memberId == null) {
            return foodTrucks;
        }

        return foodTrucks.stream()
                .map(dto -> dto.withLiked(
                        foodTruckLikeRepository.existsByFoodTruckIdAndMemberId(dto.getId(), memberId)
                ))
                .toList();
    }

    public FoodTruckDetailResponseDto getFoodTruckDetail(Long foodTruckId, Long memberId) {
        FoodTruckDetailResponseDto detail =
                foodTruckCacheService.getFoodTruckDetail(foodTruckId);

        if (memberId == null) {
            return detail;
        }

        boolean liked = foodTruckLikeRepository.existsByFoodTruckIdAndMemberId(foodTruckId, memberId);

        return detail.withLiked(liked);
    }


    public List<HotFoodTruckResponseDto> getHotFoodTrucks(Long memberId) {
        List<HotFoodTruckResponseDto> hotFoodTrucks =
                foodTruckCacheService.getHotFoodTrucks();

        if (memberId == null) {
            return hotFoodTrucks;
        }

        return hotFoodTrucks.stream()
                .map(dto -> dto.withLiked(
                        foodTruckLikeRepository.existsByFoodTruckIdAndMemberId(dto.getId(), memberId)
                ))
                .toList();
    }
}