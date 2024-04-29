package com.ssafy.backend.domain.district.dto;

import lombok.Builder;

@Builder
public record SalesDistrictTopTenResponse(
        String districtCode,
        String districtCodeName,
        Long total,
        Double totalRate,
        int level

) {
}
