package com.example.lucaus26th.domain.booth;

import com.example.lucaus26th.domain.BaseTimeEntity;
import com.example.lucaus26th.dto.request.booth.BoothUpdateRequestDto;
import com.example.lucaus26th.enums.BoothLocation;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
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
    // 운영정보
    @OneToMany(mappedBy = "booth", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<BoothSetting> settings;
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
        this.categories = new ArrayList<>();
    }

    //public void setSetting(Setting setting){
        //this.setting = setting;
    //}

    public void update(String boothImageUrl,String boothLocationImageUrl,BoothUpdateRequestDto request){
        if (request.getLocationId() != null) this.locationId = request.getLocationId();
        if (request.getName() != null) this.name = request.getName();
        if (request.getOwner() != null) this.owner = request.getOwner();
        if (request.getInfo() != null) this.info = request.getInfo();
        if (request.getLocation() != null) this.location = request.getLocation();
        if (boothImageUrl != null) this.image = boothImageUrl;
        if (boothLocationImageUrl != null) this.locationImage = boothLocationImageUrl;
        if (request.getInstagram() != null) this.instagram = request.getInstagram();
    }

    // 좋아요 관련
    public void increaseLikeCount(){
        this.likeCount++;
    }
    public void decreaseLikeCount(){
        this.likeCount--;
    }

    // 상세 조회 관련
    public List<String> getCategoryNames(){
        return categories.stream()
                .map(boothCategory -> boothCategory.getCategory().getName())
                .toList();
    }
    /*public List<String> getDates(){
        List<String> dates = new ArrayList<>();
        String mon =setting.getMon();
        String tue = setting.getTue();
        String wed = setting.getWed();
        String thu = setting.getThu();
        String fri = setting.getFri();
        if (mon != null) dates.add("월요일 : " + mon);
        if (tue != null) dates.add("화요일 : " + tue);
        if (wed != null) dates.add("수요일 : " + wed);
        if (thu != null) dates.add("목요일 : " + thu);
        if (fri != null) dates.add("금요일 : " + fri);
        return dates;
    }*/
    public List<String> getDays(){
        List<String> days = new ArrayList<>(Collections.nCopies(5, null));

        List<String> order = List.of("월요일", "화요일", "수요일", "목요일", "금요일");

        for (BoothSetting setting : settings) {
            int index = order.indexOf(setting.getDay());
            if (index != -1) {
                days.set(index, setting.getDay() + " " + setting.getStartAt() + " - " + setting.getEndAt());
            }
        }

        return days;
    }
}
