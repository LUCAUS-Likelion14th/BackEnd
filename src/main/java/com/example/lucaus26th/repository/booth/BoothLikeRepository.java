package com.example.lucaus26th.repository.booth;

import com.example.lucaus26th.domain.Member;
import com.example.lucaus26th.domain.booth.Booth;
import com.example.lucaus26th.domain.booth.BoothLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BoothLikeRepository extends JpaRepository<BoothLike,Long> {
    // 좋아요 존재 확인
    boolean existsByBoothAndMember(Booth booth, Member member);
    Optional<BoothLike> findByBoothAndMember(Booth booth, Member member);

    void deleteAllByBooth(Booth booth);

    List<BoothLike> findByMember(Member member);
}
