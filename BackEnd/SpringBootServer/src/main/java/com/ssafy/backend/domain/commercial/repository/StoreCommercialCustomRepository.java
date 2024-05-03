package com.ssafy.backend.domain.commercial.repository;

import java.util.List;
import java.util.Map;

public interface StoreCommercialCustomRepository {
    Map<String, Object> getOtherStoreByPeriodCodeAndCommercialCode(String periodCode);

    Map<String, Object> getAdministrationStoreByPeriodCodeAndCommercialCode(List<String> commercialCodes, String periodCode);
}
