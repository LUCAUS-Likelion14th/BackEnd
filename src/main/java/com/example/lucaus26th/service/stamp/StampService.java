package com.example.lucaus26th.service.stamp;

import com.example.lucaus26th.domain.Member;
import com.example.lucaus26th.domain.booth.Booth;
import com.example.lucaus26th.domain.stamp.Stamp;
import com.example.lucaus26th.dto.request.stamp.StampRegisterRequestDto;
import com.example.lucaus26th.dto.response.stamp.StampMemberInfoResponseDto;
import com.example.lucaus26th.dto.response.stamp.StampResponseDto;
import com.example.lucaus26th.repository.booth.BoothRepository;
import com.example.lucaus26th.repository.foodTruck.MemberRepository;
import com.example.lucaus26th.repository.stamp.StampBoothRepository;
import com.example.lucaus26th.repository.stamp.StampRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class StampService {

    private final BoothRepository boothRepository;
    @Value("${STAMP_PASSWORD}")
    private String applyPassword;

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
    private Long getStampCount(Member member){
        return stampRepository.countByMember(member);
    }
    private Long getStampAll(){
        return stampBoothRepository.count();
    }
    // 도장 전체 조회
    
    // 도장 찍기
    // 혹시 모를 상황에 대해 도장판에 해당하는 부스인지 체크하기
    public void createStamp(Long memberId, Long boothId,String password){
        if (!stampBoothRepository.existsByBoothId(boothId)){
            throw new IllegalStateException(("도장을 찍을 수 없는 부스입니다." + boothId));
        }
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("해당 회원이 존재하지 않습니다. ID: " + memberId));

        Booth booth = boothRepository.findById(boothId)
                .orElseThrow(() -> new IllegalArgumentException("해당 부스가 존재하지 않습니다. ID: " + boothId));

        if(booth.getStampPwd() == null){
            throw new IllegalStateException("부스에 도장 비밀번호가 등록되어 있지 않습니다.");
        }
        if (!password.equals(booth.getStampPwd())){
            throw new IllegalStateException("비밀번호가 일치하지 않습니다.");
        }

        if (stampRepository.existsByBoothAndMember(booth, member)){
            throw new IllegalStateException("이미 찍은 도장입니다.");
        }

        stampRepository.save(Stamp.builder()
                .booth(booth)
                .member(member)
                .build()
        );
    }


    
    // 도장 - 마이페이지
    public StampResponseDto.My getMyStamp(Long memberId){
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("해당 회원이 존재하지 않습니다. ID: " + memberId));

        Long stampCount =  getStampCount(member);
        Long stampAll = getStampAll();

        return StampResponseDto.My.from(stampCount,stampAll);
    }
    
    // 경품 응모
    public void updateIsApplied(Long memberId, String password){
        
        // 비번 체크
        if (!applyPassword.equals(password)){
            throw new IllegalStateException("비밀번호가 일치하지 않습니다.");
        }

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("해당 회원이 존재하지 않습니다. ID: " + memberId));
        
        // 중복 응모 방지
        if (member.getIsApplied()) {
            throw new IllegalStateException("이미 응모하셨습니다.");
        }

        Long stampCount =  getStampCount(member);
        Long stampAll = getStampAll();
        
        // 스탬프 다 안채움 방지
        if (stampCount < stampAll){
            throw new IllegalStateException("모든 스탬프를 모아야 응모할 수 있습니다. (현재: " + stampCount + "/" + stampAll + ")");
        }
        member.apply();
    }
}
