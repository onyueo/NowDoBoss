package com.ssafy.backend.domain.commercial.controller;

import com.ssafy.backend.domain.commercial.dto.*;
import com.ssafy.backend.domain.commercial.service.CommercialService;
import com.ssafy.backend.global.common.dto.Message;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Tag(name = "상권", description = "상권 관련 API 입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/commercial")
public class CommercialController {
    private final CommercialService commercialService;

    @Operation(
            summary = "해당 자치구에 포함된 행정동 목록 조회",
            description = "해당 자치구에 포함된 행정동 목록을 조회하는 기능입니다."
    )
    @GetMapping("/administration/district/{districtCode}/areas")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    public ResponseEntity<Message<List<CommercialAdministrationAreaResponse>>> getAdministrativeAreasByDistrict(
            @PathVariable String districtCode) {
        List<CommercialAdministrationAreaResponse> administrationAreaResponseList = commercialService.getAdministrativeAreasByDistrict(districtCode);
        return ResponseEntity.ok().body(Message.success(administrationAreaResponseList));
    }

    @Operation(
            summary = "해당 행정동에 포함된 상권 목록 조회",
            description = "해당 행정동에 포함된 상권 목록을 조회하는 기능입니다."
    )
    @GetMapping("/administration/{administrationCode}/areas")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    public ResponseEntity<Message<List<CommercialAreaResponse>>> getCommercialAreasByAdministrationCode(
            @PathVariable String administrationCode) {
        List<CommercialAreaResponse> areaResponseList = commercialService.getCommercialAreasByAdministrationCode(administrationCode);
        return ResponseEntity.ok().body(Message.success(areaResponseList));
    }

    @Operation(
            summary = "해당 상권의 분기별 유동 인구 조회",
            description = "주어진 상권코드에 대해 해당 분기의 유동인구 데이터를 조회합니다. 기준년분기코드가 주어지지 않으면 2023년 3분기의 데이터를 사용합니다."
    )
    @GetMapping("/foot-traffic/{commercialCode}")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    public ResponseEntity<Message<CommercialFootTrafficResponse>> getFootTrafficByCommercialCodeAndPeriod(
            @PathVariable String commercialCode,
            @RequestParam(defaultValue = "20233") String periodCode) {
        CommercialFootTrafficResponse footTrafficResponse = commercialService.getFootTrafficByPeriodAndCommercialCode(periodCode, commercialCode);
        return ResponseEntity.ok().body(Message.success(footTrafficResponse));
    }

    @Operation(
            summary = "해당 상권의 존재하는 업종 목록 조회",
            description = "주어진 상권코드에 대해 해당 상권의 존재하는 업종 목록 데이터를 조회합니다."
    )
    @GetMapping("/service/{commercialCode}")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    public ResponseEntity<Message<List<CommercialServiceResponse>>> getCommercialServiceCodeAndServiceCodeName(
            @PathVariable String commercialCode) {
        List<CommercialServiceResponse> serviceResponseList = commercialService.getServiceByCommercialCode(commercialCode);
        return ResponseEntity.ok().body(Message.success(serviceResponseList));
    }

    @Operation(
            summary = "해당 상권&업종의 분기별 매출 분석 조회",
            description = "주어진 상권코드 및 서비스코드에 대해 해당 분기의 매출분석 데이터를 조회합니다. 기준년분기코드가 주어지지 않으면 2023년 3분기의 데이터를 사용합니다."
    )
    @GetMapping("/sales/{commercialCode}/{serviceCode}")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    public ResponseEntity<Message<CommercialSalesResponse>> getSalesByPeriodAndCommercialCode(
            @PathVariable String commercialCode,
            @PathVariable String serviceCode,
            @RequestParam(defaultValue = "20233") String periodCode) {
        CommercialSalesResponse salesResponse = commercialService.getSalesByPeriodAndCommercialCodeAndServiceCode(periodCode, commercialCode, serviceCode);
        return ResponseEntity.ok().body(Message.success(salesResponse));
    }
}
