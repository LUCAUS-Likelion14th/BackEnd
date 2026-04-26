package com.example.lucaus26th.service.booth;

import com.example.lucaus26th.domain.Member;
import com.example.lucaus26th.domain.booth.Booth;
import com.example.lucaus26th.domain.booth.BoothCategory;
import com.example.lucaus26th.domain.booth.BoothLike;
import com.example.lucaus26th.domain.booth.Category;
import com.example.lucaus26th.dto.request.booth.BoothRequestDto;
import com.example.lucaus26th.dto.request.booth.BoothUpdateRequestDto;
import com.example.lucaus26th.dto.response.booth.BoothResponseDto;
import com.example.lucaus26th.global.S3Service;
import com.example.lucaus26th.repository.foodTruck.MemberRepository;
import com.example.lucaus26th.repository.booth.BoothCategoryRepository;
import com.example.lucaus26th.repository.booth.BoothLikeRepository;
import com.example.lucaus26th.repository.booth.BoothRepository;
import com.example.lucaus26th.repository.booth.CategoryRepository;
import com.example.lucaus26th.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class BoothService {
    private final BoothRepository boothRepository;
    private final CategoryRepository categoryRepository;
    private final BoothCategoryRepository boothCategoryRepository;
    private final MemberRepository memberRepository;
    private final BoothLikeRepository boothLikeRepository;
    private final S3Service s3Service;

    // Booth CRUD 기능
    public Long createBooth(BoothRequestDto request) {

        // s3 업로드 처리
        String boothImageUrl = null;
        String boothLocationImageUrl = null;
        try {
            boothImageUrl = s3Service.upload(request.getImage(), "booth");
            boothLocationImageUrl = s3Service.upload(request.getLocationImage(), "boothLocation");
        }catch(IOException e){
            throw new RuntimeException("S3 이미지 업로드에 실패했습니다.", e);
        }

        // Booth 생성
        Booth booth = Booth.builder()
                .locationId(request.getLocationId())
                .name(request.getName())
                .owner(request.getOwner())
                .location(request.getLocation())
                .info(request.getInfo())
                .image(boothImageUrl)
                .locationImage(boothLocationImageUrl)
                .instagram(request.getInstagram())
                .build();

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

    // 조회 관련 쿼리 함수
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

    private boolean searchFilter(Booth booth, String search){
        if(search == null) return true;
        String name = booth.getName() != null ? booth.getName() : "";
        String owner = booth.getOwner() != null ? booth.getOwner() : "";

        return name.contains(search) || owner.contains(search);
    }

    // 전체조회
    public Page<BoothResponseDto.Lists> getBooth(String date, String location, String category, String search, Pageable pageable, CustomUserDetails userDetails) {
        List<Booth> booths = boothRepository.findAll();
        Member member = (userDetails != null) ? userDetails.getMember() : null;

        List<BoothResponseDto.Lists> boothList = booths.stream()
                .filter(booth -> locationFilter(booth, location))
                .filter(booth -> categoryFilter(booth, category))
                .filter(booth -> dateFilter(booth, date))
                .filter(booth -> searchFilter(booth, search))
                .map(booth -> {
                    boolean isLiked = false;
                    if (userDetails != null) {
                        isLiked = boothLikeRepository.existsByBoothAndMember(booth,member);
                    }
                    return BoothResponseDto.Lists.fromEntity(booth, isLiked);
                })
                .toList();

        // List → Page 변환
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), boothList.size());
        return new PageImpl<>(boothList.subList(start,end), pageable, boothList.size());
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

    // 인기 부스
    public List<BoothResponseDto.Hot> getBoothHot(CustomUserDetails userDetails) {
        // 인기 3개만 보여주기. (Booth.likeCount 로 정렬 후
        // 현재(seoul time 기준) 시간에 영업 안하는거는 제외하고 3개 올려야함(BoothSetting 참고하자)
        ZonedDateTime nowSeoul = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));
        LocalDate today = nowSeoul.toLocalDate();
        LocalTime nowTime = nowSeoul.toLocalTime();

        Member member = (userDetails != null) ? userDetails.getMember() : null;

        List<Booth> hotBooths = boothRepository.findAll().stream()
                .filter(booth -> booth.getSettings().stream()
                        .anyMatch(setting ->
                                setting.getDate().equals(today) &&
                                        !nowTime.isBefore(setting.getStartAt()) &&
                                        !nowTime.isAfter(setting.getEndAt())
                        )
                )
                .sorted(Comparator.comparing
                        (Booth::getLikeCount).reversed())
                .limit(3)
                .toList();

        boolean isLiked = false;
        List<BoothResponseDto.Hot> result = hotBooths.stream()
                .map(booth -> {
                    boolean liked = false;
                    if (member != null) {
                        liked = boothLikeRepository.existsByBoothAndMember(booth, member);
                    }
                    return BoothResponseDto.Hot.fromEntity(booth, liked);
                })
                .toList();

        return result;
    }


    public void updateBooth(Long boothId,/* Long memberId*/ BoothUpdateRequestDto request) {
        // 나중에 관리자 체크 하기
        // adminValidater.validate(memberId);

        Booth booth = boothRepository.findById(boothId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 부스입니다 : " + boothId));

        // s3 업로드 처리
        String boothImageUrl = null;
        if(request.getImage() != null && !request.getImage().isEmpty()){
            try{
                boothImageUrl = s3Service.upload(request.getImage(), "booth");
            }catch (IOException e){
                throw new RuntimeException("S3 이미지 업로드에 실패했습니다.", e);
            }
        }
        String boothLocationImageUrl = null;
        if (request.getLocationImage() != null && !request.getLocationImage().isEmpty()){
            try{
                boothLocationImageUrl = s3Service.upload(request.getLocationImage(), "boothLocation");
            }catch (IOException e){
                throw new RuntimeException("S3 이미지 업로드에 실패했습니다.", e);
            }
        }

        booth.update(boothImageUrl, boothLocationImageUrl,request);



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
