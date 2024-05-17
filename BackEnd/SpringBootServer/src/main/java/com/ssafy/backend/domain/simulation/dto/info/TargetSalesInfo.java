package com.ssafy.backend.domain.simulation.dto.info;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TargetSalesInfo {
    // 3년 회수, 2년 회수, 손익분기

    // 월 목표매출 = (총비용 + 0.07 * 월 추정경상이익) / 0.93
    private long monthlyTargetSales;

    // 일 평균 목표 매출 = 월 목표 매출 / 근무일
    private long averageDailyTargetSales;

    // 일평균 목표고객수 = 일평균 목표매출 / 객단가
    private long customersPerDay;

    // 세금
    private long tax;

    // 월 추정경상이익 = 투자비(입력받았던 값, 권리금 + 시설비 + 가맹관련 + 기타 비용 + 보증금) / 회수기간(개월수)
    private long monthlyEstimatedOrdinaryProfit;
}
