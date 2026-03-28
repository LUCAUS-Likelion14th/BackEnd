package com.example.lucaus26th.domain.booth;

import com.example.lucaus26th.domain.BaseTimeEntity;
import com.example.lucaus26th.domain.Setting;
import com.example.lucaus26th.enums.BoothLocation;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.annotation.processing.Generated;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Booth extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private int locationId; // 장소 아이디
    @OneToOne
    @JoinColumn(name = "setting_id")
    private Setting settingId;
    // 운영정보 아이디 fk
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String owner;
    @Column(nullable = false)
    private BoothLocation location;
    @Column(nullable = false)
    private long likeCount;
    @Column(nullable = false)
    private String info;

    private String image; // 이미지 링크
    private String locationImage; // 장소 이미지 링크
    private String instagram; // 인스타 링크

    // 부스가 삭제될 때 연결 데이터도 함께 지워지도록 설정
    @OneToMany(mappedBy = "boothId", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BoothCategory> categories = new ArrayList<>();

}
