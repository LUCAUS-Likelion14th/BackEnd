package com.example.lucaus26th.repository.stamp;

import com.example.lucaus26th.domain.Member;
import com.example.lucaus26th.domain.booth.Booth;
import com.example.lucaus26th.domain.stamp.Stamp;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StampRepository extends JpaRepository<Stamp,Long> {
    boolean existsByBoothAndMember(Booth booth, Member member);
    List<Stamp> findByMember(Member member);
    Long countByMember(Member member);
}
