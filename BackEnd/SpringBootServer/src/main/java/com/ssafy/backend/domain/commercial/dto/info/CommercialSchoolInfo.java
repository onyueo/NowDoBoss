package com.ssafy.backend.domain.commercial.dto.info;

/**
 * 해당 상권 내의 학교 수 정보를 담는 DTO.
 */
public record CommercialSchoolInfo(
        Long totalSchoolsCount, // 총 학교 수 (초등 + 중등 + 고등)
        Long universityCount // 대학교 수
) {
}
