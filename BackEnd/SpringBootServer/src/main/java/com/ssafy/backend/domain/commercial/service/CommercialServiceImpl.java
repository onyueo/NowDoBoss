package com.ssafy.backend.domain.commercial.service;

import com.ssafy.backend.domain.commercial.dto.info.*;
import com.ssafy.backend.domain.commercial.dto.response.*;
import com.ssafy.backend.domain.commercial.entity.*;
import com.ssafy.backend.domain.commercial.exception.CoordinateTransformationException;
import com.ssafy.backend.domain.commercial.repository.*;
import com.ssafy.backend.global.util.CoordinateConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CommercialServiceImpl implements CommercialService {
    private final AreaCommercialRepository areaCommercialRepository;
    private final FootTrafficCommercialRepository footTrafficCommercialRepository;
    private final SalesCommercialRepository salesCommercialRepository;
    private final PopulationCommercialRepository populationCommercialRepository;
    private final FacilityCommercialRepository facilityCommercialRepository;

    @Override
    @Transactional(readOnly = true)
    public List<CommercialAdministrationResponse> getAdministrativeAreasByDistrict(String districtCode) {
        List<AreaCommercial> areaCommercialList = areaCommercialRepository.findAllByDistrictCode(districtCode);

        Set<String> seenAdministrationCodes = new HashSet<>();
        List<CommercialAdministrationResponse> result = new ArrayList<>();

        for (AreaCommercial ac : areaCommercialList) {
            if (!seenAdministrationCodes.contains(ac.getAdministrationCode())) {
                Point transformedPoint = null;
                try {
                    transformedPoint = CoordinateConverter.transform(ac.getX(), ac.getY());
                } catch (Exception e) {
                    throw new CoordinateTransformationException("좌표 변환에 실패했습니다.", e);
                }
                result.add(new CommercialAdministrationResponse(
                        ac.getAdministrationCodeName(),
                        ac.getAdministrationCode(),
                        transformedPoint != null ? transformedPoint.getX() : 0,
                        transformedPoint != null ? transformedPoint.getY() : 0
                ));
                seenAdministrationCodes.add(ac.getAdministrationCode());
            }
        }

        return result; // 중복 제거된 결과를 반환
    }


    @Override
    @Transactional(readOnly = true)
    public List<CommercialAreaResponse> getCommercialAreasByAdministrationCode(String administrationCode) {
        List<AreaCommercial> areaCommercialList = areaCommercialRepository.findByAdministrationCode(administrationCode);
        return areaCommercialList.stream()
                .map(ac -> {
                    Point transformedPoint = null;
                    try {
                        transformedPoint = CoordinateConverter.transform(ac.getX().doubleValue(), ac.getY().doubleValue());
                    } catch (Exception e) {
                        throw new CoordinateTransformationException("좌표 변환에 실패했습니다.", e);
                    }
                    // 변환된 좌표를 사용하여 CommercialAreaResponse 생성
                    return new CommercialAreaResponse(
                            ac.getCommercialCode(),
                            ac.getCommercialCodeName(),
                            ac.getCommercialClassificationCode(),
                            ac.getCommercialClassificationCodeName(),
                            transformedPoint != null ? transformedPoint.getX() : 0,
                            transformedPoint != null ? transformedPoint.getY() : 0
                    );
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public CommercialFootTrafficResponse getFootTrafficByPeriodAndCommercialCode(String periodCode, String commercialCode) {
        FootTrafficCommercial footTrafficCommercial = footTrafficCommercialRepository.findByPeriodCodeAndCommercialCode(periodCode, commercialCode)
                .orElseThrow(() -> new RuntimeException("유동인구 데이터가 존재하지 않습니다."));

        CommercialTimeSlotFootTrafficInfo timeSlotFootTraffic = new CommercialTimeSlotFootTrafficInfo(
                footTrafficCommercial.getFootTraffic00(),
                footTrafficCommercial.getFootTraffic06(),
                footTrafficCommercial.getFootTraffic11(),
                footTrafficCommercial.getFootTraffic14(),
                footTrafficCommercial.getFootTraffic17(),
                footTrafficCommercial.getFootTraffic21()
        );

        CommercialDayOfWeekFootTrafficInfo dayOfWeekFootTraffic = new CommercialDayOfWeekFootTrafficInfo(
                footTrafficCommercial.getMonFootTraffic(),
                footTrafficCommercial.getTueFootTraffic(),
                footTrafficCommercial.getWedFootTraffic(),
                footTrafficCommercial.getThuFootTraffic(),
                footTrafficCommercial.getFriFootTraffic(),
                footTrafficCommercial.getSatFootTraffic(),
                footTrafficCommercial.getSunFootTraffic()
        );

        CommercialAgeGroupFootTrafficInfo ageGroupFootTraffic = new CommercialAgeGroupFootTrafficInfo(
                footTrafficCommercial.getTeenFootTraffic(),
                footTrafficCommercial.getTwentyFootTraffic(),
                footTrafficCommercial.getThirtyFootTraffic(),
                footTrafficCommercial.getFortyFootTraffic(),
                footTrafficCommercial.getFiftyFootTraffic(),
                footTrafficCommercial.getSixtyFootTraffic()
        );

        CommercialAgeGenderPercentFootTrafficInfo ageGenderPercentFootTraffic = calculateAgeGenderPercentFootTraffic(footTrafficCommercial);

        return new CommercialFootTrafficResponse(timeSlotFootTraffic, dayOfWeekFootTraffic, ageGroupFootTraffic, ageGenderPercentFootTraffic);
    }

    @Override
    public List<CommercialServiceResponse> getServiceByCommercialCode(String commercialCode) {
        List<ServiceCodeProjection> serviceCodeProjectionList = salesCommercialRepository.findDistinctServiceCodesByCommercialCode(commercialCode);

        return serviceCodeProjectionList.stream()
                .map(projection -> new CommercialServiceResponse(
                        projection.getServiceCode(),
                        projection.getServiceCodeName(),
                        projection.getServiceType())
                )
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public CommercialSalesResponse getSalesByPeriodAndCommercialCodeAndServiceCode(String periodCode, String commercialCode, String serviceCode) {
        SalesCommercial salesCommercial = salesCommercialRepository.findByPeriodCodeAndCommercialCodeAndServiceCode(periodCode, commercialCode, serviceCode)
                .orElseThrow(() -> new RuntimeException("매출분석 데이터가 없습니다."));

        CommercialTimeSalesInfo timeSales = new CommercialTimeSalesInfo(
                salesCommercial.getSales00(),
                salesCommercial.getSales06(),
                salesCommercial.getSales11(),
                salesCommercial.getSales14(),
                salesCommercial.getSales17(),
                salesCommercial.getSales21()
        );

        CommercialDaySalesInfo daySales = new CommercialDaySalesInfo(
                salesCommercial.getMonSales(),
                salesCommercial.getTueSales(),
                salesCommercial.getWedSales(),
                salesCommercial.getThuSales(),
                salesCommercial.getFriSales(),
                salesCommercial.getSatSales(),
                salesCommercial.getSunSales()
        );

        CommercialAgeSalesInfo ageSales = new CommercialAgeSalesInfo(
                salesCommercial.getTeenSales(),
                salesCommercial.getTwentySales(),
                salesCommercial.getThirtySales(),
                salesCommercial.getFortySales(),
                salesCommercial.getFiftySales(),
                salesCommercial.getSixtySales()
        );

        CommercialAgeGenderPercentSalesInfo ageGenderPercentSales = calculateAgeGenderPercentSales(salesCommercial);

        CommercialDaySalesCountInfo daySalesCount = new CommercialDaySalesCountInfo(
                salesCommercial.getMonSalesCount(),
                salesCommercial.getTueSalesCount(),
                salesCommercial.getWedSalesCount(),
                salesCommercial.getThuSalesCount(),
                salesCommercial.getFriSalesCount(),
                salesCommercial.getSatSalesCount(),
                salesCommercial.getSunSalesCount()
        );

        CommercialTimeSalesCountInfo timeSalesCount = new CommercialTimeSalesCountInfo(
                salesCommercial.getSalesCount00(),
                salesCommercial.getSalesCount06(),
                salesCommercial.getSalesCount11(),
                salesCommercial.getSalesCount14(),
                salesCommercial.getSalesCount17(),
                salesCommercial.getSalesCount21()
        );

        CommercialGenderSalesCountInfo genderSalesCount = new CommercialGenderSalesCountInfo(
                salesCommercial.getMaleSalesCount(),
                salesCommercial.getFemaleSalesCount()
        );

        // 최근 4분기의 기간 코드를 계산
        List<String> periodCodes = calculateLastFourQuarters(periodCode);

        List<SalesCommercial> salesCommercials = salesCommercialRepository.findByCommercialCodeAndServiceCodeAndPeriodCodeIn(
                commercialCode, serviceCode, periodCodes);

        List<CommercialAnnualQuarterSalesInfo> annualQuarterSalesInfos = salesCommercials.stream()
                .map(sales -> new CommercialAnnualQuarterSalesInfo(
                        sales.getPeriodCode(),
                        salesCommercial.getMonthSales())
                ).toList();

        return new CommercialSalesResponse(
                timeSales,
                daySales,
                ageSales,
                ageGenderPercentSales,
                daySalesCount,
                timeSalesCount,
                genderSalesCount,
                annualQuarterSalesInfos
        );
    }

    @Override
    @Transactional(readOnly = true)
    public CommercialPopulationResponse getPopulationByPeriodAndCommercialCode(String periodCode, String commercialCode) {
        PopulationCommercial populationCommercial = populationCommercialRepository.findByPeriodCodeAndCommercialCode(periodCode, commercialCode)
                .orElseThrow(() -> new RuntimeException("상주인구 분석 데이터가 없습니다."));

        CommercialPopulationInfo population = new CommercialPopulationInfo(
                populationCommercial.getTotalPopulation(),
                populationCommercial.getTeenPopulation(),
                populationCommercial.getTwentyPopulation(),
                populationCommercial.getThirtyPopulation(),
                populationCommercial.getFortyPopulation(),
                populationCommercial.getFiftyPopulation(),
                populationCommercial.getSixtyPopulation()
        );

        // 남자 여자 인구 비율 소수점 첫째자리까지
        Double malePercentage = Math.round((double) populationCommercial.getMalePopulation() / populationCommercial.getTotalPopulation() * 1000) / 10.0;
        Double femalePercentage = Math.round((double) populationCommercial.getFemalePopulation() / populationCommercial.getTotalPopulation() * 1000) / 10.0;

        return new CommercialPopulationResponse(population, malePercentage, femalePercentage);
    }

    @Override
    @Transactional(readOnly = true)
    public CommercialFacilityResponse getFacilityByPeriodAndCommercialCode(String periodCode, String commercialCode) {
        FacilityCommercial facilityCommercial = facilityCommercialRepository.findByPeriodCodeAndCommercialCode(periodCode, commercialCode)
                .orElseThrow(() -> new RuntimeException("집객시설 분석 데이터가 없습니다."));

        CommercialSchoolInfo school = new CommercialSchoolInfo(
                facilityCommercial.getElementarySchoolCnt() + facilityCommercial.getMiddleSchoolCnt() + facilityCommercial.getHighSchoolCnt(),
                facilityCommercial.getUniversityCnt()
        );

        Long facilityCnt = facilityCommercial.getFacilityCnt();
        Long totalTransportCnt = facilityCommercial.getSubwayStationCnt() + facilityCommercial.getBusStopCnt();

        return new CommercialFacilityResponse(facilityCnt, school, totalTransportCnt);
    }

    @Override
    public CommercialAdministrationAreaResponse getAdministrationInfoByCommercialCode(String commercialCode) {
        return areaCommercialRepository.findByCommercialCode(commercialCode);
    }

    private CommercialAgeGenderPercentFootTrafficInfo calculateAgeGenderPercentFootTraffic(FootTrafficCommercial trafficCommercial) {
        Long total = trafficCommercial.getTotalFootTraffic();

        return new CommercialAgeGenderPercentFootTrafficInfo(
                calculatePercent(trafficCommercial.getTeenFootTraffic(), trafficCommercial.getMaleFootTraffic(), total),
                calculatePercent(trafficCommercial.getTeenFootTraffic(), trafficCommercial.getFemaleFootTraffic(), total),
                calculatePercent(trafficCommercial.getTwentyFootTraffic(), trafficCommercial.getMaleFootTraffic(), total),
                calculatePercent(trafficCommercial.getTwentyFootTraffic(), trafficCommercial.getFemaleFootTraffic(), total),
                calculatePercent(trafficCommercial.getThirtyFootTraffic(), trafficCommercial.getMaleFootTraffic(), total),
                calculatePercent(trafficCommercial.getThirtyFootTraffic(), trafficCommercial.getFemaleFootTraffic(), total),
                calculatePercent(trafficCommercial.getFortyFootTraffic(), trafficCommercial.getMaleFootTraffic(), total),
                calculatePercent(trafficCommercial.getFortyFootTraffic(), trafficCommercial.getFemaleFootTraffic(), total),
                calculatePercent(trafficCommercial.getFiftyFootTraffic(), trafficCommercial.getMaleFootTraffic(), total),
                calculatePercent(trafficCommercial.getFiftyFootTraffic(), trafficCommercial.getFemaleFootTraffic(), total),
                calculatePercent(trafficCommercial.getSixtyFootTraffic(), trafficCommercial.getMaleFootTraffic(), total),
                calculatePercent(trafficCommercial.getSixtyFootTraffic(), trafficCommercial.getFemaleFootTraffic(), total)
        );
    }

    private CommercialAgeGenderPercentSalesInfo calculateAgeGenderPercentSales(SalesCommercial salesCommercial) {
        Long total = salesCommercial.getMaleSales() + salesCommercial.getFemaleSales();

        return new CommercialAgeGenderPercentSalesInfo(
                calculatePercent(salesCommercial.getTeenSales(), salesCommercial.getMaleSales(), total),
                calculatePercent(salesCommercial.getTeenSales(), salesCommercial.getFemaleSales(), total),
                calculatePercent(salesCommercial.getTwentySales(), salesCommercial.getMaleSales(), total),
                calculatePercent(salesCommercial.getTwentySales(), salesCommercial.getFemaleSales(), total),
                calculatePercent(salesCommercial.getThirtySales(), salesCommercial.getMaleSales(), total),
                calculatePercent(salesCommercial.getThirtySales(), salesCommercial.getFemaleSales(), total),
                calculatePercent(salesCommercial.getFortySales(), salesCommercial.getMaleSales(), total),
                calculatePercent(salesCommercial.getFortySales(), salesCommercial.getFemaleSales(), total),
                calculatePercent(salesCommercial.getFiftySales(), salesCommercial.getMaleSales(), total),
                calculatePercent(salesCommercial.getFiftySales(), salesCommercial.getFemaleSales(), total),
                calculatePercent(salesCommercial.getSixtySales(), salesCommercial.getMaleSales(), total),
                calculatePercent(salesCommercial.getSixtySales(), salesCommercial.getFemaleSales(), total)
        );
    }

    private double calculatePercent(Long ageGroupCount, Long genderCount, Long total) {
        if (total == 0) return 0.0;
        double percent = 100.0 * (ageGroupCount.doubleValue() * (genderCount.doubleValue() / total)) / total;
        return Math.round(percent * 100.0) / 100.0;
    }

    private List<String> calculateLastFourQuarters(String currentPeriod) {
        List<String> periods = new ArrayList<>();
        int year = Integer.parseInt(currentPeriod.substring(0, 4));
        int quarter = Integer.parseInt(currentPeriod.substring(4));

        for (int i = 0; i < 4; i++) {
            periods.add(year + "" + quarter);
            if (quarter == 1) {
                quarter = 4;
                year--;
            } else {
                quarter--;
            }
        }
        return periods;
    }

}
