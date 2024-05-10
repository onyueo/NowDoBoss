package com.ssafy.backend.domain.simulation.dto.info;

public record FranchiseeInfo(
        // 단위: 만원
        int totalPrice,
        String brandName,

        // 단위: 만원
        int subscription,

        // 단위: 만원
        int education,

        // 단위: 만원
        int deposit,

        // 단위: 만원
        int etc,

        // 단위: 만원
        int interior
) {
}