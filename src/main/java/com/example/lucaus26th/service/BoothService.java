package com.example.lucaus26th.service;

import com.example.lucaus26th.domain.Member;
import com.example.lucaus26th.domain.Setting;
import com.example.lucaus26th.domain.booth.Booth;
import com.example.lucaus26th.domain.booth.BoothCategory;
import com.example.lucaus26th.domain.booth.BoothLike;
import com.example.lucaus26th.domain.booth.Category;
import com.example.lucaus26th.dto.request.BoothRequestDto;
import com.example.lucaus26th.dto.request.BoothUpdateRequestDto;
import com.example.lucaus26th.dto.response.BoothResponseDto;
import com.example.lucaus26th.repository.MemberRepository;
import com.example.lucaus26th.repository.booth.BoothCategoryRepository;
import com.example.lucaus26th.repository.booth.BoothLikeRepository;
import com.example.lucaus26th.repository.booth.BoothRepository;
import com.example.lucaus26th.repository.booth.CategoryRepository;
import com.example.lucaus26th.security.CustomUserDetails;
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
    private final MemberRepository memberRepository;
    private final BoothLikeRepository boothLikeRepository;

    // Booth CRUD 기능
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

    // 상세조회
    public BoothResponseDto.Detail getBoothDetail(Long boothId, CustomUserDetails userDetails) {
        Booth booth = boothRepository.findById(boothId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 부스입니다 :" + boothId));
        if (userDetails == null){
            // 비로그인
            return BoothResponseDto.Detail.fromEntity(booth, false);
        }
        Member member = userDetails.getMember();
        return BoothResponseDto.Detail.fromEntity(booth, boothLikeRepository.existsByBoothAndMember(booth,member));
    }
    // 전체조회

    public void updateBooth(Long boothId,/* Long memberId*/ BoothUpdateRequestDto request) {
        // 나중에 관리자 체크 하기
        // adminValidater.validate(memberId);

        Booth booth = boothRepository.findById(boothId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 부스입니다 : " + boothId));

        booth.update(request);

        // setting 수정 (근데 뭔가 좀 분리하고 싶네)
        if (request.getSetting() != null){
            BoothUpdateRequestDto.SettingRequest sr = request.getSetting();
            Setting setting = booth.getSetting();
            if (setting == null) {
                // setting이 없으면 새로 생성
                Setting newSetting = Setting.builder()
                        .mon(sr.getMon())
                        .tue(sr.getTue())
                        .wed(sr.getWed())
                        .thu(sr.getThu())
                        .fri(sr.getFri())
                        .build();
                booth.setSetting(newSetting);
            } else {
                // 있으면 기존 setting 수정
                setting.setMon(sr.getMon());
                setting.setTue(sr.getTue());
                setting.setWed(sr.getWed());
                setting.setThu(sr.getThu());
                setting.setFri(sr.getFri());
            }
        }

    }

    public void deleteBooth(Long boothId) {
        // 권한 검사할것

        Booth booth = boothRepository.findById(boothId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 부스입니다. :" + boothId));

        // BoothLike 먼저 삭제
        boothLikeRepository.deleteAllByBooth(booth);
        // Booth 삭제 (카테고리는 이미 cascade로 자동삭제됨)
        boothRepository.delete(booth);

    }

    // Booth 좋아요 관련 기능
    public void createBoothLike(Long boothId, Long memberId){

        Booth booth = boothRepository.findById(boothId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 부스입니다 :" + boothId));
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 멤버입니다. :" + memberId));

        // 중복 좋아요 체크
        if (boothLikeRepository.existsByBoothAndMember(booth,member)){
            throw new IllegalStateException("이미 좋아요를 눌렀습니다."); // 이런거 다 400 같이 처리하고싶은데.
        }
        // BoothLike 생성 및 저장
        boothLikeRepository.save(BoothLike.builder()
                .booth(booth)
                .member(member)
                .build()
        );
        // likeCount += 1
        booth.increaseLikeCount();
    }

    public void deleteBoothLike(Long boothId, Long memberId){
        Booth booth = boothRepository.findById(boothId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 부스입니다." + boothId));
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 멤버입니다." + memberId));

        BoothLike boothLike = boothLikeRepository.findByBoothAndMember(booth, member)
                .orElseThrow(() -> new IllegalStateException("좋아요를 누르지 않았습니다."));

        boothLikeRepository.delete(boothLike);
        booth.decreaseLikeCount();;
    }
}
