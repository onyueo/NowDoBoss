package com.ssafy.backend.domain.simulation.dto.info;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class VariableCostInfo {
    // 재료비
    private long materialCost;

    // 변동인건비(인건비 * 0.3)
    private long variablePersonnelExpenses;

    // 합계
    private long total;

    /**
     *
     * @param materialCost 재료비
     * @param personnelExpenses 인건비
     */
    @Builder
    public VariableCostInfo(long materialCost, long personnelExpenses) {
        this.materialCost = materialCost;
        this.variablePersonnelExpenses = (long) (personnelExpenses * 0.3);
        this.total = this.materialCost + this.variablePersonnelExpenses;
    }
}
