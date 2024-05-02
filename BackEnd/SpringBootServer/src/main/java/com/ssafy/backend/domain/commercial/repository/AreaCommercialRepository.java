package com.ssafy.backend.domain.commercial.repository;

import com.ssafy.backend.domain.commercial.dto.CommercialAdministrationAreaResponse;
import com.ssafy.backend.domain.commercial.entity.AreaCommercial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AreaCommercialRepository extends JpaRepository<AreaCommercial, Long> {
    List<AreaCommercial> findAllByDistrictCode(String districtCode);

    List<AreaCommercial> findByAdministrationCode(String administrationCode);

    CommercialAdministrationAreaResponse findByCommercialCode(String commercialCode);
}
