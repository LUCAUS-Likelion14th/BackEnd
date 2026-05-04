package com.example.lucaus26th.service.stamp;

import com.example.lucaus26th.domain.Member;
import com.example.lucaus26th.dto.request.stamp.StampRegisterRequestDto;
import com.example.lucaus26th.dto.response.stamp.StampMemberInfoResponseDto;
import com.example.lucaus26th.repository.foodTruck.MemberRepository;
import com.example.lucaus26th.repository.stamp.StampBoothRepository;
import com.example.lucaus26th.repository.stamp.StampRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class StampService {
    private final StampBoothRepository stampBoothRepository;
    private final StampRepository stampRepository;
    private final MemberRepository memberRepository;

    // 이름&학번 입력
    public void updateMemberInfo(StampRegisterRequestDto request, Long memberId){
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("해당 회원이 존재하지 않습니다. ID: " + memberId));

        member.updateMemberInfo(request.getName(), request.getStudentId());
    }
    // 이름&학번 조회
    public StampMemberInfoResponseDto.Info getMemberInfo(Long memberId){
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("해당 회원이 존재하지 않습니다. ID: " + memberId));

        return StampMemberInfoResponseDto.Info.from(member);
    }
    
    // 이름&학번 체크
    public StampMemberInfoResponseDto.Check checkMemberInfo(Long memberId){
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("해당 회원이 존재하지 않습니다. ID: " + memberId));

        Boolean check = true;
        if (member.getName() == null || member.getStudentID() == null){
            check = false;
        }

        return StampMemberInfoResponseDto.Check.from(check);
    }

    // 진행 상황 보여주는 함수

    // 도장 전체 조회
    
    // 도장 찍기
    // 혹시 모를 상황에 대해 도장판에 해당하는 부스인지 체크하기

    
    // 도장 - 마이페이지
    
    // 경품 응모
}
