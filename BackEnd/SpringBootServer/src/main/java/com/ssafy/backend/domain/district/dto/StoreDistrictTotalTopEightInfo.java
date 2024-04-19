package com.ssafy.backend.domain.district.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StoreDistrictTotalTopEightInfo {
    private String serviceCode;
    private String serviceCodeName;
    private Long totalStore;
}
