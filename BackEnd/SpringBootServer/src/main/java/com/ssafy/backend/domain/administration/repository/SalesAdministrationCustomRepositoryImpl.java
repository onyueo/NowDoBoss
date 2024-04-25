package com.ssafy.backend.domain.administration.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.backend.domain.administration.dto.ClosedStoreAdministrationTopFiveInfo;
import com.ssafy.backend.domain.administration.dto.OpenedStoreAdministrationTopFiveInfo;
import com.ssafy.backend.domain.administration.dto.SalesAdministrationTopFiveInfo;
import com.ssafy.backend.domain.administration.entity.QSalesAdministration;
import com.ssafy.backend.domain.administration.entity.QStoreAdministration;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class SalesAdministrationCustomRepositoryImpl implements SalesAdministrationCustomRepository{
    private final JPAQueryFactory queryFactory;

    @Override
    public List<SalesAdministrationTopFiveInfo> getTopFiveSalesAdministrationByAdministrationCode(List<String> allAdministrationCodes, String periodCode) {
        QSalesAdministration sa = QSalesAdministration.salesAdministration;

        // 서브쿼리를 이용해 개업률 top 5 행정동 코드 목록 구하기
        List<String> topAdministrationCodes = queryFactory
                .select(sa.administrationCode)
                .from(sa)
                .where(sa.periodCode.eq(periodCode),
                        sa.administrationCode.in(allAdministrationCodes))
                .groupBy(sa.administrationCode)
                .orderBy(sa.monthSales.sum().desc())
                .limit(5)
                .fetch();

        return queryFactory
                .select(Projections.constructor(
                        SalesAdministrationTopFiveInfo.class,
                        sa.administrationCode,
                        sa.administrationCodeName,
                        new CaseBuilder().when(sa.periodCode.eq(periodCode)).then(sa.monthSales).otherwise(0L).sum()
                ))
                .from(sa)
                .where(sa.administrationCode.in(topAdministrationCodes))
                .groupBy(sa.administrationCode, sa.administrationCodeName )
                .orderBy(new CaseBuilder().when(sa.periodCode.eq(periodCode)).then(sa.monthSales).otherwise(0L).sum().desc())
                .fetch();
    }
}
