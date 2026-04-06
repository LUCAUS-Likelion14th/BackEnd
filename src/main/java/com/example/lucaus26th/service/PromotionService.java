package com.example.lucaus26th.service;

import com.example.lucaus26th.dto.response.PromotionResponseDTO;
import com.example.lucaus26th.repository.PromotionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PromotionService {
    public final PromotionRepository promotionRepository;

    public List<PromotionResponseDTO> getPromotionList() {
        return promotionRepository.findTop5ByOrderByIdDesc()
                .stream().map(PromotionResponseDTO::from)
                .toList();
    }
}
