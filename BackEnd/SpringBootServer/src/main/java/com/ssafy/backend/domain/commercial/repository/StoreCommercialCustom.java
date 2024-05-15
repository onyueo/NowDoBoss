package com.ssafy.backend.domain.commercial.repository;

import java.util.List;
import java.util.Map;

public interface StoreCommercialCustom {
    Map<String, Object> getOtherStoreByPeriodCodeAndCommercialCode(String periodCode);

    Map<String, Object> getAdministrationStoreByPeriodCodeAndCommercialCode(List<String> commercialCodes, String periodCode);

    Map<String, Long> getAdministrationStoreByServiceCode(List<String> commercialCodes, String periodCode);
    Map<String, Long> getMyStoreByServiceCode(String commercialCode, String periodCode);
    Map<String, Double> getCommercialRateByCommercialCode(String commercialCode, String periodCode);

}
