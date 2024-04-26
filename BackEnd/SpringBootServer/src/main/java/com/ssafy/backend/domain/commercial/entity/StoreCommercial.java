package com.ssafy.backend.domain.commercial.entity;

import com.ssafy.backend.domain.district.entity.enums.ServiceType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Table(indexes = {
        @Index(name = "idx_period_code", columnList = "periodCode"),
        @Index(name = "idx_commercial_code", columnList = "commercialCode"),
        @Index(name = "idx_service_code", columnList = "serviceCode")
})
public class StoreCommercial {
    @Id
    @Comment("점포_상권 아이디")
    @Column(columnDefinition = "INT UNSIGNED")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Comment("기준 년분기 코드")
    @Column(columnDefinition = "VARCHAR(5)", nullable = false)
    private String periodCode;

    @Comment("상권 구분 코드")
    @Column(columnDefinition = "VARCHAR(1)", nullable = false)
    private String commercialClassificationCode;

    @Comment("상권 구분 코드 명")
    @Column(columnDefinition = "VARCHAR(4)", nullable = false)
    private String commercialClassificationCodeName;

    @Comment("상권 코드")
    @Column(columnDefinition = "VARCHAR(8)", nullable = false)
    private String commercialCode;

    @Comment("상권 코드 명")
    @Column(columnDefinition = "VARCHAR(80)", nullable = false)
    private String commercialCodeName;

    @Comment("서비스 업종 코드")
    @Column(columnDefinition = "VARCHAR(8)", nullable = false)
    private String serviceCode;

    @Comment("서비스 업종 코드명")
    @Column(columnDefinition = "VARCHAR(20)", nullable = false)
    private String serviceCodeName;

    @Comment("서비스 업종 타입")
    @Enumerated(EnumType.STRING)
    private ServiceType serviceType;

    @Comment("점포 수")
    @Column(columnDefinition = "INT UNSIGNED")
    private Long totalStore;

    @Comment("유사 업종 점포 수")
    @Column(columnDefinition = "INT UNSIGNED")
    private Long similarStore;

    @Comment("개업률")
    @Column(columnDefinition = "FLOAT")
    private Float openedRate;

    @Comment("폐업률")
    @Column(columnDefinition = "FLOAT")
    private Float closedRate;

    @Comment("프렌차이즈 점포 수")
    @Column(columnDefinition = "INT UNSIGNED")
    private Long franchiseStore;
}
