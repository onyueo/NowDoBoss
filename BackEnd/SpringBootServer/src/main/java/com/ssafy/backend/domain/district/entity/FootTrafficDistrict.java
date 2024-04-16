package com.ssafy.backend.domain.district.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Table(indexes = {
        @Index(name = "idx_period_code", columnList = "periodCode")
})
public class FootTrafficDistrict {

    @Id
    @Comment("유동인구_자치구_아이디")
    @Column(columnDefinition = "INT UNSIGNED")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Comment("기준 년분기 코드")
    @Column(columnDefinition = "VARCHAR(5)", nullable = false)
    private String periodCode;

    @Comment("자치구 코드")
    @Column(columnDefinition = "VARCHAR(5)", nullable = false)
    private String districtCode;

    @Comment("자치구 코드 명")
    @Column(columnDefinition = "VARCHAR(10)", nullable = false)
    private String districtCodeName;

    @Comment("총 유동인구 수")
    @Column(columnDefinition = "INT UNSIGNED")
    private Long totalFootTraffic;

    @Comment("남성 유동인구 수")
    @Column(columnDefinition = "INT UNSIGNED")
    private Long maleFootTraffic;

    @Comment("여성 유동인구 수")
    @Column(columnDefinition = "INT UNSIGNED")
    private Long femaleFootTraffic;

    @Comment("연령대 10 유동인구 수")
    @Column(columnDefinition = "INT UNSIGNED")
    private Long teenFootTraffic;

    @Comment("연령대 20 유동인구 수")
    @Column(columnDefinition = "INT UNSIGNED")
    private Long twentyFootTraffic;

    @Comment("연령대 30 유동인구 수")
    @Column(columnDefinition = "INT UNSIGNED")
    private Long thirtyFootTraffic;

    @Comment("연령대 40 유동인구 수")
    @Column(columnDefinition = "INT UNSIGNED")
    private Long fortyFootTraffic;

    @Comment("연령대 50 유동인구 수")
    @Column(columnDefinition = "INT UNSIGNED")
    private Long fiftyFootTraffic;

    @Comment("연령대 60 유동인구 수")
    @Column(columnDefinition = "INT UNSIGNED")
    private Long sixtyFootTraffic;

    @Comment("시간대 00 ~ 06 유동인구 수")
    @Column(name = "foot_traffic_00", columnDefinition = "INT UNSIGNED")
    private Long footTraffic00;

    @Comment("시간대 06 ~ 11 유동인구 수")
    @Column(name = "foot_traffic_06", columnDefinition = "INT UNSIGNED")
    private Long footTraffic06;

    @Comment("시간대 11 ~ 14 유동인구 수")
    @Column(name = "foot_traffic_11", columnDefinition = "INT UNSIGNED")
    private Long footTraffic11;

    @Comment("시간대 14 ~ 17 유동인구 수")
    @Column(name = "foot_traffic_14", columnDefinition = "INT UNSIGNED")
    private Long footTraffic14;

    @Comment("시간대 17 ~ 21 유동인구 수")
    @Column(name = "foot_traffic_17", columnDefinition = "INT UNSIGNED")
    private Long footTraffic17;

    @Comment("시간대 21 ~ 24 유동인구 수")
    @Column(name = "foot_traffic_21", columnDefinition = "INT UNSIGNED")
    private Long footTraffic21;

    @Comment("월요일 유동인구 수")
    @Column(columnDefinition = "INT UNSIGNED")
    private Long monFootTraffic;

    @Comment("화요일 유동인구 수")
    @Column(columnDefinition = "INT UNSIGNED")
    private Long tueFootTraffic;

    @Comment("수요일 유동인구 수")
    @Column(columnDefinition = "INT UNSIGNED")
    private Long wedFootTraffic;

    @Comment("목요일 유동인구 수")
    @Column(columnDefinition = "INT UNSIGNED")
    private Long thuFootTraffic;

    @Comment("금요일 유동인구 수")
    @Column(columnDefinition = "INT UNSIGNED")
    private Long friFootTraffic;

    @Comment("토요일 유동인구 수")
    @Column(columnDefinition = "INT UNSIGNED")
    private Long satFootTraffic;

    @Comment("일요일 유동인구 수")
    @Column(columnDefinition = "INT UNSIGNED")
    private Long sunFootTraffic;
}
