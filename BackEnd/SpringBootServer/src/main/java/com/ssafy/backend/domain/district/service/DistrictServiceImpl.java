package com.ssafy.backend.domain.district.service;

import com.ssafy.backend.domain.administration.dto.ClosedStoreAdministrationTopFiveInfo;
import com.ssafy.backend.domain.administration.dto.OpenedStoreAdministrationTopFiveInfo;
import com.ssafy.backend.domain.administration.repository.StoreAdministrationRepository;
import com.ssafy.backend.domain.district.dto.*;
import com.ssafy.backend.domain.district.dto.response.*;
import com.ssafy.backend.domain.district.entity.ChangeDistrict;
import com.ssafy.backend.domain.district.entity.FootTrafficDistrict;
import com.ssafy.backend.domain.district.repository.ChangeDistrictRepository;
import com.ssafy.backend.domain.district.repository.FootTrafficDistrictRepository;
import com.ssafy.backend.domain.district.repository.SalesDistrictRepository;
import com.ssafy.backend.domain.district.repository.StoreDistrictRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class DistrictServiceImpl implements DistrictService {
    private final FootTrafficDistrictRepository footTrafficDistrictRepository;
    private final SalesDistrictRepository salesDistrictRepository;
    private final StoreDistrictRepository storeDistrictRepository;
    private final ChangeDistrictRepository changeDistrictRepository;
    private final StoreAdministrationRepository storeAdministrationRepository;

    @Override
    public DistrictTopTenResponse getTopTenDistricts() {
        List<FootTrafficDistrictTopTenResponse> footTrafficResponseList = new ArrayList<>();
        List<FootTrafficDistrictTopTenInfo> footTrafficInfoList = getTopTenFootTrafficDistrictByPeriodCode();

        // 유동 인구 Top 10
        for (FootTrafficDistrictTopTenInfo footTrafficDistrictTopTenInfo : footTrafficInfoList) {
            Long prevTotalFootTraffic = footTrafficDistrictTopTenInfo.getPrevTotalFootTraffic();
            Long curTotalFootTraffic = footTrafficDistrictTopTenInfo.getCurTotalFootTraffic();
            String districtCodeName = footTrafficDistrictTopTenInfo.getDistrictCodeName();
            String districtCode = footTrafficDistrictTopTenInfo.getDistrictCode();

            footTrafficResponseList.add(new FootTrafficDistrictTopTenResponse(districtCode,districtCodeName, curTotalFootTraffic, (float) ((curTotalFootTraffic-prevTotalFootTraffic)/(float)prevTotalFootTraffic*100)));
        }

        // 매출 Top 10
        List<SalesDistrictTopTenResponse> salesResponseList = new ArrayList<>();
        List<String> districtNames = getTopTenSalesDistrictCodeNameByPeriodCode();
        List<SalesDistrictTopTenInfo> salesInfoList = salesDistrictRepository.getTopTenSalesDistrictByPeriodCode(districtNames);
        for (SalesDistrictTopTenInfo salesDistrictTopTenInfo : salesInfoList) {
            Long prevTotalSales = salesDistrictTopTenInfo.getPrevTotalSales();
            Long curTotalSales = salesDistrictTopTenInfo.getCurTotalSales();
            String districtCodeName = salesDistrictTopTenInfo.getDistrictCodeName();
            String districtCode = salesDistrictTopTenInfo.getDistrictCode();

            salesResponseList.add(new SalesDistrictTopTenResponse(districtCode, districtCodeName, curTotalSales, ((curTotalSales-prevTotalSales)/(float)prevTotalSales*100)));
        }

        // 개업률 Top 10
        List<OpenedStoreDistrictTopTenResponse> openedStoreResponseList = new ArrayList<>();
        List<String> openedStores = getTopTenOpenedStoreDistrictCodeNameByPeriodCode();
        List<OpenedStoreDistrictTopTenInfo> openedStoreInfoList = storeDistrictRepository.getTopTenOpenedStoreDistrictByPeriodCode(openedStores);
        for (OpenedStoreDistrictTopTenInfo openedStoreDistrictTopTenInfo : openedStoreInfoList) {
            Long prevTotalStore = openedStoreDistrictTopTenInfo.getPrevTotalStore();
            Long prevOpenedStore = openedStoreDistrictTopTenInfo.getPrevOpenedStore();
            Float prevOpenedRate = ((float) prevOpenedStore / prevTotalStore * 100);

            Long curTotalStore = openedStoreDistrictTopTenInfo.getCurTotalStore();
            Long curOpenedStore = openedStoreDistrictTopTenInfo.getCurOpenedStore();
            Float curOpenedRate = ((float) curOpenedStore / curTotalStore * 100);

            String districtCodeName = openedStoreDistrictTopTenInfo.getDistrictCodeName();
            String districtCode = openedStoreDistrictTopTenInfo.getDistrictCode();
            //System.out.println("자치구명: " + districtCodeName + "이번총점포수: " + curTotalStore + "이번개업점포수: " + curOpenedStore + "이전총점포수: " + prevTotalStore + "이전개업점포수: " + prevOpenedStore);
            openedStoreResponseList.add(new OpenedStoreDistrictTopTenResponse(districtCode, districtCodeName, curOpenedRate, (curOpenedRate-prevOpenedRate)/prevOpenedRate*100));
        }

        // 폐업률 Top 5
        List<ClosedStoreDistrictTopTenResponse> closedStoreResponseList = new ArrayList<>();
        List<String> closedStores = getTopTenClosedStoreDistrictCodeNameByPeriodCode();
        List<ClosedStoreDistrictTopTenInfo> closedStoreInfoList = storeDistrictRepository.getTopTenClosedStoreDistrictByPeriodCode(closedStores);
        for (ClosedStoreDistrictTopTenInfo closedStoreDistrictTopTenInfo : closedStoreInfoList) {
            Long prevTotalStore = closedStoreDistrictTopTenInfo.getPrevTotalStore();
            Long prevClosedStore = closedStoreDistrictTopTenInfo.getPrevClosedStore();
            Float prevClosedRate = ((float) prevClosedStore / prevTotalStore * 100);

            Long curTotalStore = closedStoreDistrictTopTenInfo.getCurTotalStore();
            Long curClosedStore = closedStoreDistrictTopTenInfo.getCurClosedStore();
            Float curClosedRate = ((float) curClosedStore / curTotalStore * 100);

            String districtCodeName = closedStoreDistrictTopTenInfo.getDistrictCodeName();
            String districtCode = closedStoreDistrictTopTenInfo.getDistrictCode();
            //System.out.println("자치구명: " + districtCodeName + "이번총점포수: " + curTotalStore + "이번폐업점포수: " + curClosedStore + "이전총점포수: " + prevTotalStore + "이전폐업점포수: " + prevClosedStore);
            closedStoreResponseList.add(new ClosedStoreDistrictTopTenResponse(districtCode, districtCodeName, curClosedRate, (curClosedRate-prevClosedRate)/prevClosedRate*100));
        }

        return new DistrictTopTenResponse(footTrafficResponseList, salesResponseList, openedStoreResponseList, closedStoreResponseList);
    //    return null;
    }

    public DistrictDetailResponse getDistrictDetail(String districtCode) {
        // 상권 변화 지표 관련
        ChangeDistrict changeDistrict = changeDistrictRepository.getChangeIndicatorDistrictByDistrictCodeAndPeriodCode(districtCode);
        ChangeIndicatorDistrictResponse changeIndicatorDistrictResponse = new ChangeIndicatorDistrictResponse(changeDistrict.getChangeIndicator(), changeDistrict.getChangeIndicatorName(), changeDistrict.getOpenedMonths(), changeDistrict.getClosedMonths());

        // 유동인구 관련
        List<FootTrafficDistrict> footTrafficDetailList = footTrafficDistrictRepository.getFootTrafficDistrictDetail(districtCode);
        List<Long> footTrafficDistrictListByPeriod = new ArrayList<>();
        List<Long> footTrafficDistrictListByTime = new ArrayList<>();
        List<Long> footTrafficDistrictListByGender = new ArrayList<>();
        List<Long> footTrafficDistrictListByAge = new ArrayList<>();
        List<Long> footTrafficDistrictListByDay = new ArrayList<>();

        for (FootTrafficDistrict footTrafficDistrict: footTrafficDetailList){
            // 총 유동인구
            footTrafficDistrictListByPeriod.add(footTrafficDistrict.getTotalFootTraffic());

            if (footTrafficDistrict.getPeriodCode().equals("20233")){
                // 시간대별
                footTrafficDistrictListByTime.add(footTrafficDistrict.getFootTraffic00());
                footTrafficDistrictListByTime.add(footTrafficDistrict.getFootTraffic06());
                footTrafficDistrictListByTime.add(footTrafficDistrict.getFootTraffic11());
                footTrafficDistrictListByTime.add(footTrafficDistrict.getFootTraffic14());
                footTrafficDistrictListByTime.add(footTrafficDistrict.getFootTraffic17());
                footTrafficDistrictListByTime.add(footTrafficDistrict.getFootTraffic21());

                // 남녀
                footTrafficDistrictListByGender.add(footTrafficDistrict.getMaleFootTraffic());
                footTrafficDistrictListByGender.add(footTrafficDistrict.getFemaleFootTraffic());

                // 연령대별
                footTrafficDistrictListByAge.add(footTrafficDistrict.getTeenFootTraffic());
                footTrafficDistrictListByAge.add(footTrafficDistrict.getTwentyFootTraffic());
                footTrafficDistrictListByAge.add(footTrafficDistrict.getThirtyFootTraffic());
                footTrafficDistrictListByAge.add(footTrafficDistrict.getFortyFootTraffic());
                footTrafficDistrictListByAge.add(footTrafficDistrict.getFiftyFootTraffic());
                footTrafficDistrictListByAge.add(footTrafficDistrict.getSixtyFootTraffic());

                // 요일별
                footTrafficDistrictListByDay.add(footTrafficDistrict.getMonFootTraffic());
                footTrafficDistrictListByDay.add(footTrafficDistrict.getTueFootTraffic());
                footTrafficDistrictListByDay.add(footTrafficDistrict.getWedFootTraffic());
                footTrafficDistrictListByDay.add(footTrafficDistrict.getThuFootTraffic());
                footTrafficDistrictListByDay.add(footTrafficDistrict.getFriFootTraffic());
                footTrafficDistrictListByDay.add(footTrafficDistrict.getSatFootTraffic());
                footTrafficDistrictListByDay.add(footTrafficDistrict.getSunFootTraffic());
            }
        }
        FootTrafficDistrictDetailResponse footTrafficDistrictDetailResponse = new FootTrafficDistrictDetailResponse(footTrafficDistrictListByPeriod, footTrafficDistrictListByTime, footTrafficDistrictListByGender, footTrafficDistrictListByAge, footTrafficDistrictListByDay);

        // 점포 관련
        // 매출 Top 8 서비스 업종, 업종 코드명, 점포 개수
        List<StoreDistrictTotalTopEightInfo> storeDistrictTotalTopEightList = getTopEightTotalStoreByServiceCode(districtCode);

        // 지역구 코드로 해당 지역구에 속하는 행정동 코드 리스트 가져오기
        List<String> allAdministrationCodes = new ArrayList<>();
        allAdministrationCodes.add("11500591");
        allAdministrationCodes.add("11500550");
        allAdministrationCodes.add("11500535");

        List<String> topFiveOpenedAdministrations = getTopFiveOpenedStoreAdministrationCodeByDistrictCode(allAdministrationCodes);
        List<String> topFiveClosedAdministrations = getTopFiveClosedStoreAdministrationCodeByDistrictCode(allAdministrationCodes);

        // 개업률 top 5 행정동
        List<OpenedStoreAdministrationTopFiveInfo> openedStoreAdministrationTopFiveList = storeAdministrationRepository.getTopFiveOpenedRateAdministration(topFiveOpenedAdministrations);
        // 폐업률 top 5 행정동
        List<ClosedStoreAdministrationTopFiveInfo> closedStoreAdministrationTopFiveList = storeAdministrationRepository.getTopFiveClosedRateAdministration(topFiveClosedAdministrations);

        StoreDistrictDetailResponse storeDistrictDetailResponse = new StoreDistrictDetailResponse(storeDistrictTotalTopEightList, openedStoreAdministrationTopFiveList, closedStoreAdministrationTopFiveList);
        return new DistrictDetailResponse(changeIndicatorDistrictResponse, footTrafficDistrictDetailResponse, storeDistrictDetailResponse);
    }



    public List<FootTrafficDistrictTopTenInfo> getTopTenFootTrafficDistrictByPeriodCode() {
        Pageable pageable = PageRequest.of(0, 10); // 첫 번째 페이지에서 5개의 결과만 가져옴
        Page<FootTrafficDistrictTopTenInfo> page =  footTrafficDistrictRepository.getTopTenFootTrafficDistrictByPeriodCode(pageable);
        return new ArrayList<>(page.getContent());
    }

    public List<String> getTopTenSalesDistrictCodeNameByPeriodCode() {
        Pageable pageable = PageRequest.of(0, 10); // 첫 번째 페이지에서 5개의 결과만 가져옴
        Page<String> page = salesDistrictRepository.getTopTenSalesDistrictCodeNameByPeriodCode(pageable);
        return new ArrayList<>(page.getContent());
    }

    public List<String> getTopTenOpenedStoreDistrictCodeNameByPeriodCode() {
        Pageable pageable = PageRequest.of(0, 10); // 첫 번째 페이지에서 5개의 결과만 가져옴
        Page<String> page = storeDistrictRepository.getTopTenOpenedStoreDistrictCodeNameByPeriodCode(pageable);
        return new ArrayList<>(page.getContent());
    }

    public List<String> getTopTenClosedStoreDistrictCodeNameByPeriodCode() {
        Pageable pageable = PageRequest.of(0, 10); // 첫 번째 페이지에서 5개의 결과만 가져옴
        Page<String> page = storeDistrictRepository.getTopTenClosedStoreDistrictCodeNameByPeriodCode(pageable);
        return new ArrayList<>(page.getContent());
    }

    public List<StoreDistrictTotalTopEightInfo> getTopEightTotalStoreByServiceCode(String districtCode) {
        Pageable pageable = PageRequest.of(0, 8); // 첫 번째 페이지에서 5개의 결과만 가져옴
        Page<StoreDistrictTotalTopEightInfo> page = storeDistrictRepository.getTopEightTotalStoreByServiceCode(districtCode, pageable);
        return new ArrayList<>(page.getContent());
    }

    public List<String> getTopFiveOpenedStoreAdministrationCodeByDistrictCode(List<String> allAdministrationCodes) {
        Pageable pageable = PageRequest.of(0, 5); // 첫 번째 페이지에서 5개의 결과만 가져옴
        Page<String> page = storeAdministrationRepository.getTopFiveOpenedStoreAdministrationByAdministrationCode(allAdministrationCodes, pageable);
        return new ArrayList<>(page.getContent());
    }

    public List<String> getTopFiveClosedStoreAdministrationCodeByDistrictCode(List<String> allAdministrationCodes) {
        Pageable pageable = PageRequest.of(0, 5); // 첫 번째 페이지에서 5개의 결과만 가져옴
        Page<String> page = storeAdministrationRepository.getTopFiveClosedStoreAdministrationByAdministrationCode(allAdministrationCodes, pageable);
        return new ArrayList<>(page.getContent());
    }
}
