import { forwardRef, Ref, useEffect, useState } from 'react'
import { useQuery } from '@tanstack/react-query'
import selectPlaceStore from '@src/stores/selectPlaceStore'
import analysisStore from '@src/stores/analysisStore'
import { getFlowPopulationData } from '@src/api/analysisApi'
import CategoryTitleCard from '@src/components/analysis/CategoryTitleCard'
import TodayChart from '@src/components/analysis/flowPopulation/TodayChart'
import WeekChart from '@src/components/analysis/flowPopulation/WeekChart'
import AgeChart from '@src/components/analysis/flowPopulation/AgeChart'
import TimeChart from '@src/components/analysis/flowPopulation/TimeChart'
import * as f from '@src/components/styles/analysis/FlowPopulationAnalysisStyle'

const FlowPopulationAnalysisContainer = forwardRef(
  (_, ref: Ref<HTMLDivElement>) => {
    const selectedCommercial = selectPlaceStore(
      state => state.selectedCommercial,
    )
    const setFlowPopulationDataBody = analysisStore(
      state => state.setFlowPopulationDataBody,
    )
    const [flowErr, setFlowErr] = useState('') // 유동인구 에러 메세지

    // 유동인구
    const { data: FlowPopulationData, status: flowPopulationStatus } = useQuery(
      {
        queryKey: ['GetFlowPopulationData', selectedCommercial.code],
        queryFn: () => getFlowPopulationData(String(selectedCommercial.code)),
        enabled: selectedCommercial.code !== 0, // 상권 코드가 0일때는 보내지 않는 조건
      },
    )

    useEffect(() => {
      if (
        flowPopulationStatus === 'success' &&
        FlowPopulationData?.dataHeader.successCode === 0
      ) {
        setFlowPopulationDataBody(FlowPopulationData.dataBody)
        setFlowErr('')
      }
      // 호출 실패
      else if (
        flowPopulationStatus === 'success' &&
        FlowPopulationData?.dataHeader.successCode === 1
      ) {
        setFlowErr(FlowPopulationData.dataHeader.resultMessage)
      }
    }, [flowPopulationStatus, FlowPopulationData]) // eslint-disable-line react-hooks/exhaustive-deps

    return (
      <div ref={ref}>
        <CategoryTitleCard src="/images/flow_population.png" title="유동인구" />
        {flowErr ? (
          <f.ErrBox>{flowErr}</f.ErrBox>
        ) : (
          <>
            <f.FirstLowContainer>
              <TodayChart />
              <WeekChart />
            </f.FirstLowContainer>
            <f.SecondLowContainer>
              <AgeChart />
              <TimeChart />
            </f.SecondLowContainer>
          </>
        )}
      </div>
    )
  },
)

FlowPopulationAnalysisContainer.displayName = 'FlowPopulationAnalysisContainer'
export default FlowPopulationAnalysisContainer
