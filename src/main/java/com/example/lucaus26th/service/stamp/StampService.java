package com.example.lucaus26th.service.stamp;

import com.example.lucaus26th.domain.Member;
import com.example.lucaus26th.domain.booth.Booth;
import com.example.lucaus26th.domain.stamp.Stamp;
import com.example.lucaus26th.domain.stamp.StampBooth;
import com.example.lucaus26th.dto.request.stamp.StampRegisterRequestDto;
import com.example.lucaus26th.dto.request.stamp.StampRequestDto;
import com.example.lucaus26th.dto.response.stamp.StampMemberInfoResponseDto;
import com.example.lucaus26th.dto.response.stamp.StampResponseDto;
import com.example.lucaus26th.global.exception.BusinessException;
import com.example.lucaus26th.global.exception.ErrorCode;
import com.example.lucaus26th.repository.booth.BoothRepository;
import com.example.lucaus26th.repository.foodTruck.MemberRepository;
import com.example.lucaus26th.repository.stamp.StampBoothRepository;
import com.example.lucaus26th.repository.stamp.StampRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));

        member.updateMemberInfo(request.getName(), request.getStudentId());
    }
    // 이름&학번 조회
    public StampMemberInfoResponseDto.Info getMemberInfo(Long memberId){
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));

        return StampMemberInfoResponseDto.Info.from(member);
    }
    
    // 이름&학번 체크
    public StampMemberInfoResponseDto.Check checkMemberInfo(Long memberId){
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));

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
    public StampResponseDto.Stamp getStamp(Long memberId){
        // 사용자 정보 조회
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));
        
        // 도장판 정보 입력 안했을 때
        if (member.getName() == null || member.getStudentID() == null){
            throw new BusinessException(ErrorCode.NO_MEMBER_INFO);
        }

        // 전체 스탬프 대상 부스 목록
        List<StampBooth> allStampBooths = stampBoothRepository.findAll();

        // 레포지토리에서 바로 Set<Long>을 받아옴
        Set<Long> stampedBoothIds = stampRepository.findBoothIdsByMember(member);

        List<StampResponseDto.BoothInfo> boothInfos = allStampBooths.stream()
                .map(sb -> {
                    Booth booth = sb.getBooth();
                    return StampResponseDto.BoothInfo.builder()
                            .booth_id(booth.getId())
                            .name(booth.getName())
                            .is_stamped(stampedBoothIds.contains(booth.getId()))
                            .build();
                })
                .collect(Collectors.toList());

        return StampResponseDto.Stamp.from(member.getName(), member.getStudentID(),getStampCount(member), getStampAll(), boothInfos);
    }
    
    // 도장 찍기
    // 혹시 모를 상황에 대해 도장판에 해당하는 부스인지 체크하기
    public void createStamp(Long memberId, Long boothId, StampRequestDto request){
        if (!stampBoothRepository.existsByBoothId(boothId)){
            throw new BusinessException(ErrorCode.UNSTAMPABLE_BOOTH);
        }
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));

        Booth booth = boothRepository.findById(boothId)
                .orElseThrow(() -> new BusinessException(ErrorCode.BOOTH_NOT_FOUND));

        if(booth.getStampPwd() == null){
            throw new BusinessException(ErrorCode.NO_STAMP_PASSWORD);
        }
        String password = request.getPassword();
        if (!password.equals(booth.getStampPwd())){
            throw new BusinessException(ErrorCode.INVALID_PASSWORD);
        }

        if (stampRepository.existsByBoothAndMember(booth, member)){
            throw new BusinessException(ErrorCode.ALREADY_STAMPED);
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
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));

        Long stampCount =  getStampCount(member);
        Long stampAll = getStampAll();

        return StampResponseDto.My.from(stampCount,stampAll);
    }
    
    // 경품 응모
    public void updateIsApplied(Long memberId, StampRequestDto request){
        String password = request.getPassword();
        // 비번 체크
        if (!applyPassword.equals(password)){
            throw new BusinessException(ErrorCode.INVALID_PASSWORD);
        }

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));
        
        // 중복 응모 방지
        if (member.isApplied()) {
            throw new BusinessException(ErrorCode.ALREADY_APPLIED);
        }

        Long stampCount =  getStampCount(member);
        Long stampAll = getStampAll();
        
        // 스탬프 다 안채움 방지 -> 나중에 6개로 고쳐야함
        if (stampCount < stampAll){
            throw new BusinessException(ErrorCode.NOT_ENOUGH_STAMPS);
        }
        member.apply();
    }
}
