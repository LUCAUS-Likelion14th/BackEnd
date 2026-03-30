package com.example.lucaus26th.domain.booth;

import com.example.lucaus26th.domain.BaseTimeEntity;
import com.example.lucaus26th.domain.Setting;
import com.example.lucaus26th.dto.request.BoothUpdateRequestDto;
import com.example.lucaus26th.enums.BoothLocation;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.annotation.processing.Generated;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Booth extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long locationId; // 장소 아이디
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "setting_id")
    private Setting setting;
    // 운영정보 아이디 fk
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String owner;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BoothLocation location;
    @Column(nullable = false)
    private Long likeCount;
    @Column(nullable = false)
    private String info;

    private String image; // 이미지 링크
    private String locationImage; // 장소 이미지 링크
    private String instagram; // 인스타 링크

    // 부스가 삭제될 때 연결 데이터도 함께 지워지도록 설정
    @OneToMany(mappedBy = "booth", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BoothCategory> categories = new ArrayList<>();


    @Builder
    public Booth(Long locationId, String name, String owner, BoothLocation location, String info, String image, String locationImage, String instagram) {
        this.locationId = locationId;
        this.name = name;
        this.owner = owner;
        this.location = location;
        this.likeCount = 0L;
        this.info = info;
        this.image = image;
        this.locationImage = locationImage;
        this.instagram = instagram;
    }

    public void setSetting(Setting setting){
        this.setting = setting;
    }

    public void update(BoothUpdateRequestDto request){
        if (request.getLocationId() != null) this.locationId = request.getLocationId();
        if (request.getName() != null) this.name = request.getName();
        if (request.getOwner() != null) this.owner = request.getOwner();
        if (request.getInfo() != null) this.info = request.getInfo();
        if (request.getLocation() != null) this.location = request.getLocation();
        if (request.getImage() != null) this.image = request.getImage();
        if (request.getLocationImage() != null) this.locationImage = request.getLocationImage();
        if (request.getInstagram() != null) this.instagram = request.getInstagram();
    }


    // 좋아요 관련
    public void increaseLikeCount(){
        this.likeCount++;
    }
    public void decreaseLikeCount(){
        this.likeCount--;
    }

}
