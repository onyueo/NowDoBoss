import { DetailDataBody } from '@src/types/StatusType'
import * as c from '@src/components/styles/status/DeatilComponentStyle'
import DoughnutChart2 from '@src/common/DoughnutChart2'
import BarChartCompare from '@src/common/BarChartCompare'
import useStateStore from '@src/stores/statusStore'
import ContainerBox from '@src/common/ContainerBox'

interface DetailCloseRateProps {
  props: DetailDataBody
}

const DetailCloseRateComponent = ({ props }: DetailCloseRateProps) => {
  const { selectedRegion } = useStateStore()

  const OpenData =
    props!.storeDistrictDetail.closedStoreAdministrationTopFiveList
  const OpenLabels = OpenData.map(data => data.administrationCodeName)
  const ChangeData = props!.changeIndicatorDistrictDetail

  return (
    <div>
      <c.MixConatiner>
        <c.MixInnerConatiner>
          <c.AnalysisTitle>폐업률 분석</c.AnalysisTitle>
          <c.AnalysisSubTitle>
            폐업률이 가장 높은 동은
            <c.AnalysiEemphasis>
              {OpenData[0].administrationCodeName}
            </c.AnalysiEemphasis>
            입니다
          </c.AnalysisSubTitle>
          <ContainerBox height={10} />
          <DoughnutChart2
            labels={OpenLabels}
            value={[20, 30, 50, 20, 80]}
            subTextCenter="개업률 1위"
            textCenter={OpenData[0].administrationCodeName}
          />
        </c.MixInnerConatiner>
        <c.MixInnerConatiner>
          <c.AnalysisTitle>평균 폐업 영업개월</c.AnalysisTitle>
          <c.AnalysisSubTitle>
            폐업 영업개월은 서울시 평균보다
            <c.AnalysiEemphasis>
              {ChangeData.closedMonths > 52 ? ' 높습' : ' 낮습'}
            </c.AnalysiEemphasis>
            니다
          </c.AnalysisSubTitle>
          <ContainerBox height={20} />
          <BarChartCompare
            labels={[selectedRegion!, '서울시']}
            values={[ChangeData.closedMonths, 52]}
            minvalue={0}
          />
          <ContainerBox height={20} />
        </c.MixInnerConatiner>
      </c.MixConatiner>
    </div>
  )
}
export default DetailCloseRateComponent
