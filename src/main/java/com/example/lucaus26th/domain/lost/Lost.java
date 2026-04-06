package com.example.lucaus26th.domain.lost;


import com.example.lucaus26th.dto.request.lost.LostRequestDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Lost {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    @Builder.Default
    private String category = "기타"; // 이거 디폴트는 기타
    @Column(nullable = false)
    private String name;
    private String image; // nullable ㄱㄴ?
    private String date;
    private String findLocation;
    private String storage;

    public void update(LostRequestDto request){
        if (request.getCategory() != null) this.category = request.getCategory();
        if (request.getName() != null) this.name = request.getName();
        if (request.getImage() != null) this.image = request.getImage();
        if (request.getDate() != null) this.date = request.getDate();
        if (request.getFindLocation() != null) this.findLocation = request.getFindLocation();
        if (request.getStorage() != null) this.storage = request.getStorage();
    }

}
