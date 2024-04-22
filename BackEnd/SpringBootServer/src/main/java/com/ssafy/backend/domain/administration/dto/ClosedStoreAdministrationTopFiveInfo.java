package com.ssafy.backend.domain.administration.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClosedStoreAdministrationTopFiveInfo {

    private String administrationCode;

    private String administrationCodeName;

    private Long curTotalStore;

    private Long curClosedStore;

}
