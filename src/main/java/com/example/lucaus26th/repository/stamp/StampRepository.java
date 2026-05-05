package com.example.lucaus26th.repository.stamp;

import com.example.lucaus26th.domain.Member;
import com.example.lucaus26th.domain.booth.Booth;
import com.example.lucaus26th.domain.stamp.Stamp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface StampRepository extends JpaRepository<Stamp,Long> {
    boolean existsByBoothAndMember(Booth booth, Member member);
    // 쿼리 레벨에서 바로 부스 ID만 중복 없이 Set으로 가져옴
    @Query("select s.booth.id from Stamp s where s.member = :member")
    Set<Long> findBoothIdsByMember(@Param("member") Member member);
    Long countByMember(Member member);
}
