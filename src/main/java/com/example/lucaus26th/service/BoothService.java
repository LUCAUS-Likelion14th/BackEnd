package com.example.lucaus26th.service;

import com.example.lucaus26th.domain.Setting;
import com.example.lucaus26th.domain.booth.Booth;
import com.example.lucaus26th.domain.booth.BoothCategory;
import com.example.lucaus26th.domain.booth.Category;
import com.example.lucaus26th.dto.request.BoothRequestDto;
import com.example.lucaus26th.repository.BoothCategoryRepository;
import com.example.lucaus26th.repository.BoothRepository;
import com.example.lucaus26th.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class BoothService {
    private final BoothRepository boothRepository;
    private final CategoryRepository categoryRepository;
    private final BoothCategoryRepository boothCategoryRepository;

    public Long createBooth(BoothRequestDto request) {

        // Setting 생성 (요청에 Setting 있을 시)
        Setting setting = null;
        if(request.getSetting() != null){
            BoothRequestDto.SettingRequest sr = request.getSetting();
            setting = Setting.builder()
                    .mon(sr.getMon())
                    .tue(sr.getTue())
                    .wed(sr.getWed())
                    .thu(sr.getThu())
                    .fri(sr.getFri())
                    .build();
            // Setting은 Booth의 cascade로 자동 저장되므로 별도 save 불필요
        }

        // Booth 생성
        Booth booth = Booth.builder()
                .locationId(request.getLocationId())
                .name(request.getName())
                .owner(request.getOwner())
                .location(request.getLocation())
                .info(request.getInfo())
                .image(request.getImage())
                .locationImage(request.getLocationImage())
                .instagram(request.getInstagram())
                .build();
        if(setting != null){
            booth.setSetting(setting);
        }

        boothRepository.save(booth);

        // Category 연결 (categoryIds 가 있을 시)
        if (request.getCategoryIds() != null && !request.getCategoryIds().isEmpty()) {
            for (Long categoryId : request.getCategoryIds()){
                Category category = categoryRepository.findById(categoryId)
                        .orElseThrow(() -> new IllegalArgumentException(("존재하지 않는 카테고리 id :") + categoryId));
                boothCategoryRepository.save(BoothCategory.builder()
                        .booth(booth)
                        .category(category)
                        .build());
            }
        }

        return booth.getId();
    }

}
