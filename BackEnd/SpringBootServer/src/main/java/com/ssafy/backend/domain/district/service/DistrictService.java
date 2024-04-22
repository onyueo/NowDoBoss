package com.ssafy.backend.domain.district.service;

import com.ssafy.backend.domain.district.dto.DistrictInfo;
import com.ssafy.backend.domain.district.dto.DistrictDetailResponse;
import com.ssafy.backend.domain.district.dto.DistrictTopTenResponse;

import java.util.List;

public interface DistrictService {

    List<DistrictInfo> getAllDistricts();

    DistrictTopTenResponse getTopTenDistricts();
    
    DistrictDetailResponse getDistrictDetail(String districtCode);
}
