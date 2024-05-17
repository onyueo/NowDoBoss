package com.ssafy.backend.domain.simulation.service;

import com.ssafy.backend.domain.district.entity.SalesDistrict;
import com.ssafy.backend.domain.district.repository.SalesDistrictRepository;
import com.ssafy.backend.domain.simulation.document.SimulationDocument;
import com.ssafy.backend.domain.simulation.dto.info.*;
import com.ssafy.backend.domain.simulation.dto.request.CreateSimulationRequest;
import com.ssafy.backend.domain.simulation.dto.request.SimulationRequest;
import com.ssafy.backend.domain.simulation.dto.request.SearchFranchiseeRequest;
import com.ssafy.backend.domain.simulation.dto.response.SearchFranchiseeResponse;
import com.ssafy.backend.domain.simulation.dto.response.SimulationDocumentResponse;
import com.ssafy.backend.domain.simulation.dto.response.SimulationResponse;
import com.ssafy.backend.domain.simulation.dto.response.StoreResponse;
import com.ssafy.backend.domain.simulation.entity.Franchisee;
import com.ssafy.backend.domain.simulation.entity.Rent;
import com.ssafy.backend.domain.simulation.entity.ServiceType;
import com.ssafy.backend.domain.simulation.exception.SimulationErrorCode;
import com.ssafy.backend.domain.simulation.exception.SimulationException;
import com.ssafy.backend.domain.simulation.repository.FranchiseeRepository;
import com.ssafy.backend.domain.simulation.repository.RentRepository;
import com.ssafy.backend.domain.simulation.repository.ServiceRepository;
import com.ssafy.backend.domain.simulation.repository.SimulationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class SimulationServiceImpl implements SimulationService {
    private static final double SQUARE_METER_CONVERSION = 3.3;
    private static final int THOUSAND_MULTIPLIER = 1000;
    private static final int TEN_THOUSAND_MULTIPLIER = 10000;

    private final FranchiseeRepository franchiseeRepository;
    private final ServiceRepository serviceRepository;
    private final RentRepository rentRepository;
    private final SalesDistrictRepository salesDistrictRepository;
    private final SimulationRepository simulationRepository;

    @Override
    public List<SearchFranchiseeResponse> searchFranchisee(SearchFranchiseeRequest request) {
        return franchiseeRepository.searchFranchisee(request);
    }

    @Override
    public StoreResponse selectStoreSize(String serviceCode) {
        ServiceType serviceType = serviceRepository.findByServiceCode(serviceCode)
                .orElseThrow(() -> new SimulationException(SimulationErrorCode.NOT_EXIST_SERVICE));

        return StoreResponse.builder()
                .small(SizeInfo.builder().squareMeter(serviceType.getSmallSize()).build())
                .medium(SizeInfo.builder().squareMeter(serviceType.getMediumSize()).build())
                .large(SizeInfo.builder().squareMeter(serviceType.getLargeSize()).build())
                .build();
    }

    private long calculateNonFranchiseeInteriorCost(String serviceCode, int storeSize) {
        double unitArea = franchiseeRepository.findAvgByService(serviceCode);

        System.out.println("=========================== 단위면적당 인테리어비용 평균값(천원) : " + unitArea);

        long interiorCost = (long) (storeSize / SQUARE_METER_CONVERSION * unitArea * THOUSAND_MULTIPLIER);

        System.out.println("=========================== 인테리어 비용(원) : " + interiorCost);

        return interiorCost;
    }

    @Override
    public SimulationResponse simulate(SimulationRequest request) {
        //////////////////////////////////////////////////////////// 전체 비용 계산
        ServiceType serviceType = serviceRepository.findByServiceCode(request.serviceCode())
                .orElseThrow(() -> new SimulationException(SimulationErrorCode.NOT_EXIST_SERVICE));

        Rent rent = rentRepository.findByDistrictCodeName(request.gugun())
                .orElseThrow(() -> new SimulationException(SimulationErrorCode.NOT_EXIST_SERVICE));


        long rentPrice = rent.calculateRent(request.storeSize(), request.floor());
        long deposit = rent.calculateDeposit(rentPrice);
        long investmentCost = rentPrice + deposit;

        log.info("임대료(원): {}", rentPrice);
        log.info("보증금(원) : {}", deposit);

        Long totalLevy = 0L;
        Long totalInterior = 0L;

        // 프랜차이즈O
        if (request.isFranchisee()) {
            Franchisee franchisee = franchiseeRepository.findByBrandName(request.brandName())
                    .orElseThrow(() -> new SimulationException(SimulationErrorCode.NOT_EXIST_BRAND));

            totalLevy = franchisee.getLevy();
            totalInterior = franchisee.getTotalInterior();
            investmentCost += (totalLevy + totalInterior);
        } else {    // 프랜차이즈X
            // 인테리어 비용
            // avg(해당 업종의 프랜차이즈 단위면적 인테리어 비용) * (입력한 면적 / 3.3)
            totalInterior = calculateNonFranchiseeInteriorCost(request.serviceCode(), request.storeSize());
            investmentCost += totalInterior;
            totalLevy = null;
        }

        log.info("부담금(원) : {}", totalLevy);
        log.info("인테리어비용(원) : {}", totalInterior);
        log.info("창업 비용(원) : {}", investmentCost);

        KeyMoneyInfo keyMoneyInfo = KeyMoneyInfo.builder()
                .keyMoneyRatio(serviceType.getKeyMoneyRatio())
                .keyMoney(serviceType.getKeyMoney())
                .keyMoneyLevel(serviceType.getKeyMoneyLevel())
                .build();

        if (totalLevy != null) {
            totalLevy /= TEN_THOUSAND_MULTIPLIER;
        }

        DetailInfo detailInfo = DetailInfo.builder()
                .rentPrice(rentPrice/TEN_THOUSAND_MULTIPLIER)
                .deposit(deposit/TEN_THOUSAND_MULTIPLIER)
                .interior(totalInterior/TEN_THOUSAND_MULTIPLIER)
                .levy(totalLevy)
                .build();

        //////////////////////////////////////////////////////////// 분석

        // 성별, 연령대 분석
        GenderAndAgeAnalysisInfo analysisInfo = analyzeGenderAndAge(request.gugun(), request.serviceCode());

        // 성수기, 비성수기 분석
        MonthAnalysisInfo monthAnalysisInfo = analyzePeakAndOffPeak(request.gugun(), request.serviceCode());

        //////////////////////////////////////////////////////////// 프랜차이즈 상위 5개 비교
        // 비용(원) >> 보증금 + 임대료 + 아래 내용
        long franchiseePrice = rentPrice + deposit;

        
        // TODO 현재 계산결과가 조금 이상 >> 확인해보기
        
        // 투자비 = 권리금(결과에 존재) + 시설비(입력) + 가맹관련(결과에 존재) + 기타 비용(입력) + 보증금(결과에 존재)
        // >> totalPrice를 투자비로 처리해서 totalPrice에 권리금, 시설비, 기타 비용 더하기
        // if 창업 >> totalLevy X
        // if 프랜차이즈 >> totalLevy O
        investmentCost /= TEN_THOUSAND_MULTIPLIER;
        investmentCost += keyMoneyInfo.keyMoney() + request.facilityFee() + request.otherInvestmentCosts();

        log.info("투자비 : {}", investmentCost);

        List<FranchiseeInfo> franchisees = franchiseeRepository.findByServiceCode(franchiseePrice, investmentCost * TEN_THOUSAND_MULTIPLIER, request.serviceCode());


        // 감가상각비 = 투자비(계산 결과) / 감가상각 기간(입력) >> 고정비 같은 월 단위 계산에 포함되니까 월단위로 나누자
        long depreciation = investmentCost / (request.depreciationPeriod() * 12);

        // 매달 총 비용 = 재료비 + 임차료 + 감가상각비 + 인건비 + 기타비용 + 대출이자?
        // TODO 대출이자도 포함?
//        long monthlyCost = request.materialCost() + rentPrice + depreciation + request.personnelExpenses() + request.otherCosts();
//        monthlyCost += (long) (request.loan() * request.interestRate() / 100.0);
        
        // 고정비
        FixedCostInfo fixedCostInfo = FixedCostInfo.builder()
                .rentPrice(rentPrice)
                .personnelExpenses(request.personnelExpenses())
                .depreciation(depreciation)
                .loanInterest((long) (request.loan() * request.interestRate() / 100.0))
                .investmentCost(investmentCost)
                .otherCosts(request.otherCosts())
                .build();

        // 변동비
        VariableCostInfo variableCostInfo = VariableCostInfo.builder()
                .materialCost(request.materialCost())
                .personnelExpenses(request.personnelExpenses())
                .build();


        List<TargetSalesInfo> targetSalesInfos = new ArrayList<>();

        // 근무일
        int workDate = 30;

        // 매달 총 비용 = 고정비 + 변동비
        long monthlyCost = fixedCostInfo.getTotal() + variableCostInfo.getTotal();


        /////// 투자금 회수 X
        // 손익분기의 월 추정경상이익 = 0
        long monthlyEstimatedOrdinaryProfitX = 0;


        // 손익분기 = (총비용 + 0.07 * 월 추정경상이익) / 0.93 >> 투자금 회수 X인 월 목표매출
        long breakEvenX = (long) (monthlyCost / 0.93);

        // 세금
        long taxX = (long) (breakEvenX * 0.07);

        // 일 평균 목표 매출 = 월 목표 매출 / 근무일(24일)
        long averageDailyTargetSalesX = breakEvenX / workDate;

        // 일평균 목표고객수 = 일평균 목표매출 / 객단가
        long customersPerDayX = averageDailyTargetSalesX / request.perGuestPrice();

        targetSalesInfos.add(TargetSalesInfo.builder()
                .monthlyTargetSales(breakEvenX)
                .averageDailyTargetSales(averageDailyTargetSalesX)
                .customersPerDay(customersPerDayX)
                .tax(taxX)
                .monthlyEstimatedOrdinaryProfit(monthlyEstimatedOrdinaryProfitX)
                .build());


        //////// 투자금 회수 O
        // 회수기간 >> 2년, 3년 이내 회수가능
        for (int year = 2; year >= 3; year++) {
            // 월 추정경상이익 = 투자비(입력받았던 값, 권리금 + 시설비 + 가맹관련 + 기타 비용 + 보증금) / 회수기간(개월수)
            long monthlyEstimatedOrdinaryProfitO = investmentCost / (12 * year);

            // 월 목표매출 = (총비용 + 0.07 * 월 추정경상이익) / 0.93
            // 월 목표 매출 = 손익분기 + 월 추정경상이익
            long monthlyTargetSalesO = breakEvenX + monthlyEstimatedOrdinaryProfitO;

            // 세금
            long taxO = (long) (monthlyTargetSalesO * 0.07);

            // 일 평균 목표 매출 = 월 목표 매출 / 근무일
            long averageDailyTargetSalesO = monthlyTargetSalesO / workDate;

            // 일평균 목표고객수 = 일평균 목표매출 / 객단가
            long customersPerDayO = (long) (averageDailyTargetSalesO / (request.perGuestPrice() / 100.0));

            targetSalesInfos.add(TargetSalesInfo.builder()
                    .monthlyTargetSales(monthlyTargetSalesO)
                    .averageDailyTargetSales(averageDailyTargetSalesO)
                    .customersPerDay(customersPerDayO)
                    .tax(taxO)
                    .monthlyEstimatedOrdinaryProfit(monthlyEstimatedOrdinaryProfitO)
                    .build());
        }

        /*/////// 투자금 회수 X
        // 손익분기의 월 추정경상이익 = 0
        long monthlyEstimatedOrdinaryProfitX = 0;


        // 손익분기 = (총비용 + 0.07 * 월 추정경상이익) / 0.93 >> 투자금 회수 X인 월 목표매출
        long breakEvenX = (long) ((monthlyCost + 0.07 * monthlyEstimatedOrdinaryProfitX)/ 0.93);

        // 세금
        long taxX = (long) (breakEvenX * 0.07);

        // 일 평균 목표 매출 = 월 목표 매출 / 근무일(24일)
        long averageDailyTargetSalesX = breakEvenX / workDate;

        // 일평균 목표고객수 = 일평균 목표매출 / 객단가
        long customersPerDayX = (long) (averageDailyTargetSalesX / (request.perGuestPrice() / 100.0));

        targetSalesInfos.add(TargetSalesInfo.builder()
                .monthlyTargetSales(breakEvenX)
                .averageDailyTargetSales(averageDailyTargetSalesX)
                .customersPerDay(customersPerDayX)
                .tax(taxX)
                .monthlyEstimatedOrdinaryProfit(monthlyEstimatedOrdinaryProfitX)
                .build());*/

        return SimulationResponse.builder()
                .investmentCost(investmentCost)
                .keyMoneyInfo(keyMoneyInfo)
                .detail(detailInfo)
                .franchisees(franchisees)
                .genderAndAgeAnalysisInfo(analysisInfo)
                .monthAnalysisInfo(monthAnalysisInfo)
                .monthlyCost(monthlyCost)
                .fixedCostInfo(fixedCostInfo)
                .variableCostInfo(variableCostInfo)
                .targetSalesInfos(targetSalesInfos)
                .build();
    }

    private MonthAnalysisInfo analyzePeakAndOffPeak(String district, String serviceCode) {
        List<QuarterSalesInfo> quarterSales = salesDistrictRepository.findMonthSalesByOption("2022", district, serviceCode);

        int size = quarterSales.size();

        // 비성수기
        int offPeakQuarter = getQuarter(quarterSales, 0);
        log.info("비성수기 : {}", offPeakQuarter);

        // 성수기
        int peakQuarter = getQuarter(quarterSales, size - 1);
        log.info("성수기 : {}", peakQuarter);

        return MonthAnalysisInfo.builder()
                .peak(peakQuarter)
                .offPeak(offPeakQuarter)
                .build();
    }

    private static int getQuarter(List<QuarterSalesInfo> quarterSales, int index) {
        String seasonPeriodCode = quarterSales.get(index).periodCode();
        return seasonPeriodCode.charAt(seasonPeriodCode.length() - 1) - '0';
    }


    private GenderAndAgeAnalysisInfo analyzeGenderAndAge(String district, String serviceCode) {
        SalesDistrict salesDistrict = salesDistrictRepository.findSalesDistrictByOption("20233", district, serviceCode)
                .orElseThrow(() -> new SimulationException(SimulationErrorCode.NOT_EXIST_SALES));

        return GenderAndAgeAnalysisInfo.create(salesDistrict);
    }

    @Override
    public void createSimulation(Long memberId, CreateSimulationRequest request) {
        SimulationDocument simulationDocument = SimulationDocument.builder()
                .memberId(memberId)
                .totalPrice(request.totalPrice())
                .isFranchisee(request.isFranchisee())
                .brandName(request.brandName())
                .gugun(request.gugun())
                .serviceCode(request.serviceCode())
                .serviceCodeName(request.serviceCodeName())
                .storeSize(request.storeSize())
                .floor(request.floor())
                .build();

        if (!simulationRepository.existsBySimulationDocument(simulationDocument)) {
            simulationRepository.save(simulationDocument);
        }
    }

    @Override
    public List<SimulationDocumentResponse> selectSimulation(Long memberId) {
        return simulationRepository.findByMemberId(memberId).stream().map(
                s -> new SimulationDocumentResponse(s)
        ).toList();
    }
}
