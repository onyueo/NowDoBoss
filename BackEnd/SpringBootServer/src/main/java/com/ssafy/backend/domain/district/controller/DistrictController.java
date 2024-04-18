package com.ssafy.backend.domain.district.controller;

import com.ssafy.backend.domain.district.dto.response.ChangeIndicatorDistrictResponse;
import com.ssafy.backend.domain.district.dto.response.DistrictTopFiveResponse;
import com.ssafy.backend.domain.district.service.DistrictService;
import com.ssafy.backend.global.common.dto.Message;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/district")
public class DistrictController {

    private final DistrictService districtService;

    @Operation(
            summary = "자치구 Top 5 리스트",
            description = "유동인구, 매출, 개업률, 폐업률 Top 5 리스트를 제공하는 기능입니다."
    )
    @GetMapping("/top/five")
    public ResponseEntity<Message<DistrictTopFiveResponse>> getTopFiveDistricts() {
        System.out.println("top5 가져오기!");
        return ResponseEntity.ok().body(Message.success(districtService.getTopFiveDistricts()));
    }

    @Operation(
            summary = "특정 자치구 상세 분석",
            description = "상권변화지표를 제공하는 기능입니다."
    )
    @GetMapping("/detail/{districtCode}")
    public ResponseEntity<Message<ChangeIndicatorDistrictResponse>> getTopFiveDistricts(@PathVariable String districtCode) {
        System.out.println("해당 자치구 상세 분석 가져오기!");
        return ResponseEntity.ok().body(Message.success(districtService.getChangeIndicatorDistrict(districtCode)));
    }
}
