package com.ssafy.backend.domain.administration.repository;

import com.ssafy.backend.domain.administration.dto.SalesAdministrationTopFiveInfo;

import java.util.List;

public interface SalesAdministrationCustomRepository {
    List<SalesAdministrationTopFiveInfo> getTopFiveSalesAdministrationByAdministrationCode(List<String> allAdministrationCodes, String periodCode);
}
