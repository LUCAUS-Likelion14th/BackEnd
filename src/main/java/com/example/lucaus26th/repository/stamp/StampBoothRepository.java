package com.example.lucaus26th.repository.stamp;

import com.example.lucaus26th.domain.stamp.StampBooth;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StampBoothRepository extends JpaRepository<StampBooth, Long> {
    boolean existsByBoothId(Long boothId);
}
