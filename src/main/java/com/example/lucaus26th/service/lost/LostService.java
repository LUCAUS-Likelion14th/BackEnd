package com.example.lucaus26th.service.lost;


import com.example.lucaus26th.domain.lost.Lost;
import com.example.lucaus26th.dto.request.lost.LostRequestDto;
import com.example.lucaus26th.dto.response.lost.LostResponseDto;
import com.example.lucaus26th.global.S3Service;
import com.example.lucaus26th.global.exception.BusinessException;
import com.example.lucaus26th.global.exception.ErrorCode;
import com.example.lucaus26th.repository.lost.LostRepository;
import com.example.lucaus26th.security.CustomUserDetails;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class LostService {

    private final LostRepository lostRepository;
    private final S3Service s3Service;
    private static final List<String> VALID_CATEGORIES = List.of("전자기기", "지갑/카드", "화장품", "우산", "기타");


    public LostResponseDto createLost(LostRequestDto request, CustomUserDetails userDetails){

        // 카테고리
        String category = request.getCategory();
        if (category != null){
            if (!VALID_CATEGORIES.contains(category)){
                throw new BusinessException(ErrorCode.WRONG_LOST_CATEGORY);
            }
        }

        // s3 업로드 처리
        String imageUrl = null;
        if (request.getImage() != null && !request.getImage().isEmpty()){
            try{
                imageUrl = s3Service.upload(request.getImage(), "lost");
            } catch(IOException e){
                throw new BusinessException(ErrorCode.FILE_UPLOAD_FAILED);
            }
        }

        Lost lost = Lost.builder()
                .category(request.getCategory())
                .name(request.getName())
                .image(imageUrl)
                .date(request.getDate())
                .findLocation(request.getFindLocation())
                .build();

        lostRepository.save(lost);

        return LostResponseDto.fromEntity(lost);
    }

    // 전체조회
    // 관련 필터 함수
    private boolean categoryFilter(Lost lost, String category){
        if(category == null){ return true;}
        if (!VALID_CATEGORIES.contains(category)){
            throw new BusinessException(ErrorCode.WRONG_LOST_CATEGORY);
        }
        return lost.getCategory().equals(category);
    }

    private boolean dateFilter(Lost lost, String date){
        if(date == null) return true;

        // "MMDD" 형식 검증
        if (date.length() != 4 || !date.matches("\\d{4}")) {
            throw new BusinessException(ErrorCode.WRONG_DATE_FORMAT);
        }

        // "MMDD" → "MM.DD" 변환
        String formatted = date.substring(0, 2) + "." + date.substring(2, 4);

        return lost.getDate().equals(formatted);
    }

    public Page<LostResponseDto> getLost(String category, String date, Pageable pageable){
        List<Lost> losts = lostRepository.findAll();

        List<LostResponseDto> lostList = losts.stream()
                .filter(lost -> categoryFilter(lost, category))
                .filter(lost -> dateFilter(lost,date))
                .map(lost -> {
                    return LostResponseDto.fromEntity(lost);
                })
                .toList();

        // List -> Page 변환
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), lostList.size());
        // 페이지 범위 초과 처리
        if (start >= lostList.size()) {
            return new PageImpl<>(List.of(), pageable, lostList.size());
        }
        return new PageImpl<>(lostList.subList(start,end),pageable, lostList.size());
    }

    public LostResponseDto updateLost(Long lostId,LostRequestDto request, CustomUserDetails userDetails){
        // Role 체크하기

        Lost lost = lostRepository.findById(lostId)
                .orElseThrow(() -> new BusinessException(ErrorCode.LOST_NOT_FOUND));

        // s3 업로드 처리
        String imageUrl = null;
        if (request.getImage() != null && !request.getImage().isEmpty()){
            try{
                imageUrl = s3Service.upload(request.getImage(), "lost");
            }catch (IOException e){
                throw new BusinessException(ErrorCode.FILE_UPLOAD_FAILED);
            }
        }

        lost.update(imageUrl,request);

        return  LostResponseDto.fromEntity(lost);
    }

    public void deleteLost(Long lostId, CustomUserDetails userDetails){
        // Role 체크하기
        
        Lost lost = lostRepository.findById(lostId)
                .orElseThrow(() -> new BusinessException(ErrorCode.LOST_NOT_FOUND));

        lostRepository.delete(lost);
    }
}
