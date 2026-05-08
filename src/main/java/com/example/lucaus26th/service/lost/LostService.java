package com.example.lucaus26th.service.lost;


import com.example.lucaus26th.domain.lost.Lost;
import com.example.lucaus26th.dto.request.lost.LostRequestDto;
import com.example.lucaus26th.dto.response.lost.LostResponseDto;
import com.example.lucaus26th.global.S3Service;
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

    public LostResponseDto createLost(LostRequestDto request, CustomUserDetails userDetails){

        // 카테고리
        String category = request.getCategory();
        //if(category != "")
        // s3 업로드 처리
        String imageUrl = null;
        if (request.getImage() != null && !request.getImage().isEmpty()){
            try{
                imageUrl = s3Service.upload(request.getImage(), "lost");
            } catch(IOException e){
                throw new RuntimeException("S3 이미지 업로드에 실패했습니다.", e);
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
        return lost.getCategory().equals(category);
    }

    private boolean dateFilter(Lost lost, String date){
        if(date == null) return true;
        if (date.length() != 4 || !date.matches("\\d{4}")) return false; // 추가

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
        return new PageImpl<>(lostList.subList(start,end),pageable, lostList.size());
    }

    public LostResponseDto updateLost(Long lostId,LostRequestDto request, CustomUserDetails userDetails){
        // Role 체크하기

        Lost lost = lostRepository.findById(lostId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 분실물입니다 : " + lostId));

        // s3 업로드 처리
        String imageUrl = null;
        if (request.getImage() != null && !request.getImage().isEmpty()){
            try{
                imageUrl = s3Service.upload(request.getImage(), "lost");
            }catch (IOException e){
                throw new RuntimeException("S3 이미지 업로드에 실패했습니다.", e);
            }
        }

        lost.update(imageUrl,request);

        return  LostResponseDto.fromEntity(lost);
    }

    public void deleteLost(Long lostId, CustomUserDetails userDetails){
        // Role 체크하기
        
        Lost lost = lostRepository.findById(lostId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 분실물입니다 : " + lostId));

        lostRepository.delete(lost);
    }
}
