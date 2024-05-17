package com.ssafy.backend.domain.simulation.dto.info;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FixedCostInfo {
    // 임대료
    private long rentPrice;

    // 고정인건비 (인건비 * 0.7)
    private long fixedPersonnelExpenses;

    // 초기투자비용에 대한 월 발생비용 = 감가상각비 + 대출금 이자 + 총투자비 * 0.02
    private long incurredCostByInvestment;

    // 기타비용
    private long otherCosts;

    // 합계
    private long total;

    /**
     * 
     * @param rentPrice 임대료
     * @param personnelExpenses 고정 인건비
     * @param depreciation 감가상각비
     * @param loanInterest 대출금 이자
     * @param investmentCost 총투자비
     * @param otherCosts 기타비용
     */
    @Builder
    public FixedCostInfo(long rentPrice, long personnelExpenses, long depreciation, long loanInterest, long investmentCost, long otherCosts) {
        this.rentPrice = rentPrice;
        this.fixedPersonnelExpenses = (long) (personnelExpenses * 0.7);
        this.incurredCostByInvestment = depreciation + loanInterest + (long) (investmentCost * 0.02);
        this.otherCosts = otherCosts;
        this.total = this.rentPrice + this.fixedPersonnelExpenses + this.incurredCostByInvestment + this.otherCosts;
    }
}
