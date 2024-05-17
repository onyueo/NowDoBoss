package com.ssafy.backend.domain.simulation.dto.request;

public record SimulationRequest(
        // 프랜차이즈 여부
        // if 프랜차이즈 여부 true : 프랜차이즈 브랜드 명 존재
        // if 프랜차이즈 여부 false : 프랜차이즈 브랜드 명 null
        Boolean isFranchisee,
        String brandName,
        // 위치 (시 구 동)
//        LocationInfo location,
        String gugun,

        // 업종 코드
        String serviceCode,

        // 업종
        String serviceCodeName,

        // 매장 크기
        int storeSize,

        // 층수 (FIRST_FLOOR, ETC)
        String floor,
        
        ///// 초기 투자 비용
        // 시설비(만원)
        int facilityFee,


        // 기타 비용(만원)
        int otherInvestmentCosts,

        // 대출금(만원)
        int loan,

        // 대출 이자율(%)
        double interestRate,


        // 감가상각 기간(년)
        int depreciationPeriod,


        /////// 월 매출 / 월 비용 현황
        // 월매출(만원)
//        int monthlySales,


        // 재료비(만원)
        int materialCost,

//        // 임차료(만원)
//        int hire,


//        // 손익(만원)
//        int profitAndLoss,

        // 인건비(만원)
        int personnelExpenses,


        // 기타비용(만원)
        int otherCosts,


        // 객 단가(원)
        int perGuestPrice
        
) {
}
