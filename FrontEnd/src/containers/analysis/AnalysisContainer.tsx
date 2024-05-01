import { useEffect } from 'react'
import { useQuery } from '@tanstack/react-query'
import analysisStore from '@src/stores/analysisStore'
import {
  getFlowPopulationData,
  getResidentPopulationData,
} from '@src/api/analysisApi'
import ResultSection from '@src/components/analysis/ResultSection'
import * as a from '@src/containers/analysis/AnalysisContainerStyle'

const AnalysisContainer = () => {
  const {
    selectedCommercialCode,
    setFlowPopulationDataBody,
    setResidentPopulationDataBody,
  } = analysisStore()

  // 유동인구
  const { data: FlowPopulationData, status: flowPopulationStatus } = useQuery({
    queryKey: ['GetFlowPopulationData', selectedCommercialCode],
    queryFn: () => getFlowPopulationData(selectedCommercialCode),
  })

  useEffect(() => {
    if (
      flowPopulationStatus === 'success' &&
      FlowPopulationData?.dataHeader.successCode === 0
    ) {
      setFlowPopulationDataBody(FlowPopulationData.dataBody)
    }
  }, [flowPopulationStatus, FlowPopulationData]) // eslint-disable-line react-hooks/exhaustive-deps

  // 상주인구
  const { data: ResidentPopulationData, status: residentPopulationStatus } =
    useQuery({
      queryKey: ['GetResidentPopulationData', selectedCommercialCode],
      queryFn: () => getResidentPopulationData(selectedCommercialCode),
    })

  useEffect(() => {
    if (
      residentPopulationStatus === 'success' &&
      ResidentPopulationData?.dataHeader.successCode === 0
    ) {
      setResidentPopulationDataBody(ResidentPopulationData.dataBody)
    }
  }, [residentPopulationStatus, ResidentPopulationData]) // eslint-disable-line react-hooks/exhaustive-deps

  return (
    <a.Container>
      <a.SelectLocationContainer>
        {/* <SelectLocationSection /> */}
      </a.SelectLocationContainer>
      <a.ResultSectionContainer>
        <ResultSection />
      </a.ResultSectionContainer>
    </a.Container>
  )
}

export default AnalysisContainer
