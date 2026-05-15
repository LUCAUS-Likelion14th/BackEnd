package com.example.lucaus26th.service.booth;

import com.example.lucaus26th.domain.Member;
import com.example.lucaus26th.domain.booth.Booth;
import com.example.lucaus26th.domain.booth.BoothCategory;
import com.example.lucaus26th.domain.booth.BoothLike;
import com.example.lucaus26th.domain.booth.Category;
import com.example.lucaus26th.domain.stamp.StampBooth;
import com.example.lucaus26th.dto.request.booth.BoothRequestDto;
import com.example.lucaus26th.dto.request.booth.BoothUpdateRequestDto;
import com.example.lucaus26th.dto.response.booth.BoothResponseDto;
import com.example.lucaus26th.global.S3Service;
import com.example.lucaus26th.global.exception.BusinessException;
import com.example.lucaus26th.global.exception.ErrorCode;
import com.example.lucaus26th.repository.foodTruck.MemberRepository;
import com.example.lucaus26th.repository.booth.BoothCategoryRepository;
import com.example.lucaus26th.repository.booth.BoothLikeRepository;
import com.example.lucaus26th.repository.booth.BoothRepository;
import com.example.lucaus26th.repository.booth.CategoryRepository;
import com.example.lucaus26th.repository.stamp.StampBoothRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
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
    private final StampBoothRepository stampBoothRepository;
    private final S3Service s3Service;
    private final EntityManager entityManager;
    private final BoothCacheService boothCacheService;

    // Booth CRUD 기능
    @CacheEvict(value = "booth", allEntries = true)
    public BoothResponseDto.All createBooth(BoothRequestDto request) {

        // s3 업로드 처리
        String boothImageUrl = null;
        try {
            if (request.getImage() != null && !request.getImage().isEmpty()) {
                boothImageUrl = s3Service.upload(request.getImage(), "booth");
            }
        }catch(IOException e){
            throw new BusinessException(ErrorCode.FILE_UPLOAD_FAILED);
        }

        // Booth 생성
        Booth booth = Booth.builder()
                .name(request.getName())
                .owner(request.getOwner())
                .info(request.getInfo())
                .image(boothImageUrl)
                .instagram(request.getInstagram())
                .stampPwd(request.getStampPwd())
                .build();

        boothRepository.save(booth);

        // Category 연결 (categoryIds 가 있을 시)
        if (request.getCategoryIds() != null && !request.getCategoryIds().isEmpty()) {
            for (Long categoryId : request.getCategoryIds()){
                Category category = categoryRepository.findById(categoryId)
                        .orElseThrow(() -> new BusinessException(ErrorCode.CATEGORY_NOT_FOUND));
                boothCategoryRepository.save(BoothCategory.builder()
                        .booth(booth)
                        .category(category)
                        .build());
            }
        }

        entityManager.flush();
        entityManager.clear();
        Booth savedBooth = boothRepository.findById(booth.getId())
                .orElseThrow(() -> new BusinessException(ErrorCode.BOOTH_NOT_FOUND));
        return BoothResponseDto.All.fromEntity(savedBooth);
    }

    // 전체조회
    public Page<BoothResponseDto.Lists> getBooth(String date, String location, String category, String search, Pageable pageable, Member member) {
        List<BoothResponseDto.Lists> boothList = boothCacheService.getBoothList(date, location, category, search);

        if (member != null) {
            boothList = boothList.stream()
                    .map(dto -> {
                        Booth booth = boothRepository.findById(dto.getBooth_id())
                                .orElseThrow(() -> new BusinessException(ErrorCode.BOOTH_NOT_FOUND));

                        boolean liked = boothLikeRepository.existsByBoothAndMember(booth, member);
                        return dto.withLiked(liked);
                    })
                    .toList();
        }

        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), boothList.size());

        // 페이지 범위 초과 처리
        if (start >= boothList.size()) {
            return new PageImpl<>(List.of(), pageable, boothList.size());
        }
        return new PageImpl<>(boothList.subList(start, end), pageable, boothList.size());
    }

    // 상세조회
    public BoothResponseDto.Detail getBoothDetail(Long boothId, Member member) {
        BoothResponseDto.Detail detail = boothCacheService.getBoothDetail(boothId);

        if (member == null) {
            return detail;
        }

        Booth booth = boothRepository.findById(boothId)
                .orElseThrow(() -> new BusinessException(ErrorCode.BOOTH_NOT_FOUND));

        boolean liked = boothLikeRepository.existsByBoothAndMember(booth, member);

        return detail.withLiked(liked);
    }

    // 도장판 대상 부스
    public Page<BoothResponseDto.Lists> getBoothStamp(Pageable pageable,Member member){

        List<BoothResponseDto.Lists> stampBoothList = boothCacheService.getBoothStamp();

        if (member != null){
            stampBoothList = stampBoothList.stream()
                    .map(dto -> {
                        Booth booth = boothRepository.findById(dto.getBooth_id())
                                .orElseThrow(() -> new BusinessException(ErrorCode.BOOTH_NOT_FOUND));
                        boolean liked = boothLikeRepository.existsByBoothAndMember(booth, member);
                        return dto.withLiked(liked);
                    })
                    .toList();
        }

        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), stampBoothList.size());

        // 페이지 범위 초과 처리
        if (start >= stampBoothList.size()) {
            return new PageImpl<>(List.of(), pageable, stampBoothList.size());
        }
        return new PageImpl<>(stampBoothList.subList(start, end), pageable, stampBoothList.size());
    }


    // 인기 부스
    public List<BoothResponseDto.Hot> getBoothHot(Member member) {
        // 인기 3개만 보여주기. (Booth.likeCount 로 정렬 후
        // 현재(seoul time 기준) 시간에 영업 안하는거는 제외하고 3개 올려야함(BoothSetting 참고하자)
        List<BoothResponseDto.Hot> hotBooths = boothCacheService.getBoothHot();

        if (member == null) {
            return hotBooths;
        }

        return hotBooths.stream()
                .map(dto -> {
                    Booth booth = boothRepository.findById(dto.getBooth_id())
                            .orElseThrow(() -> new BusinessException(ErrorCode.BOOTH_NOT_FOUND));

                    boolean liked = boothLikeRepository.existsByBoothAndMember(booth, member);
                    return dto.withLiked(liked);
                })
                .toList();
    }


    @CacheEvict(value = "booth", allEntries = true)
    public BoothResponseDto.All updateBooth(Long boothId,/* Long memberId*/ BoothUpdateRequestDto request) {
        // 나중에 관리자 체크 하기
        // adminValidater.validate(memberId);

        Booth booth = boothRepository.findById(boothId)
                .orElseThrow(() -> new BusinessException(ErrorCode.BOOTH_NOT_FOUND));

        // s3 업로드 처리
        String boothImageUrl = null;
        if(request.getImage() != null && !request.getImage().isEmpty()){
            try{
                boothImageUrl = s3Service.upload(request.getImage(), "booth");
            }catch (IOException e){
                throw new BusinessException(ErrorCode.FILE_UPLOAD_FAILED);
            }
        }
        booth.update(boothImageUrl, request);

        return BoothResponseDto.All.fromEntity(booth);

    }

    @CacheEvict(value = "booth", allEntries = true)
    public void deleteBooth(Long boothId) {
        // 권한 검사할것

        Booth booth = boothRepository.findById(boothId)
                .orElseThrow(() -> new BusinessException(ErrorCode.BOOTH_NOT_FOUND));

        // BoothLike 먼저 삭제
        boothLikeRepository.deleteAllByBooth(booth);
        // Booth 삭제 (카테고리는 이미 cascade로 자동삭제됨)
        boothRepository.delete(booth);

    }

    // Booth 좋아요 관련 기능
    @CacheEvict(value = "booth", allEntries = true)
    public void createBoothLike(Long boothId, Long memberId){

        Booth booth = boothRepository.findById(boothId)
                .orElseThrow(() -> new BusinessException(ErrorCode.BOOTH_NOT_FOUND));
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));

        // 중복 좋아요 체크
        if (boothLikeRepository.existsByBoothAndMember(booth,member)){
            throw new BusinessException(ErrorCode.ALREADY_BOOTH_LIKE); // 이런거 다 400 같이 처리하고싶은데.
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

    @CacheEvict(value = "booth", allEntries = true)
    public void deleteBoothLike(Long boothId, Long memberId){
        Booth booth = boothRepository.findById(boothId)
                .orElseThrow(() -> new BusinessException(ErrorCode.BOOTH_NOT_FOUND));
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));

        BoothLike boothLike = boothLikeRepository.findByBoothAndMember(booth, member)
                .orElseThrow(() -> new BusinessException(ErrorCode.NO_BOOTH_LIKE));

        boothLikeRepository.delete(boothLike);
        booth.decreaseLikeCount();
    }
}
