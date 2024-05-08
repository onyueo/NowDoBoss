import * as c from '@src/containers/simulation/ReportStyle'
import LightIcon from '@src/assets/lightBulbIcon.svg'
import SimulationStore from '@src/stores/simulationStore'
import ReportStore from '@src/stores/reportStore'
import { SimulationReportType } from '@src/types/SimulationType'

const reportSummary = ({
  ReportData,
}: {
  ReportData: SimulationReportType
}) => {
  const { subCategoryName, bulidingSize, floor } = SimulationStore()
  const { query } = ReportStore()
  console.log(ReportData)

  const TotalPrice = ReportData.totalPrice
  let formattedNumber

  if (TotalPrice >= 100000000) {
    const billions = Math.floor(TotalPrice / 100000000)
    const millions = Math.floor((TotalPrice % 100000000) / 10000)
    formattedNumber = `${billions}억 ${millions.toFixed(0)}만원`
  } else {
    formattedNumber = `${(TotalPrice / 10000).toFixed(2)}만원`
  }

  return (
    <c.Container>
      <c.SummaryContainer>
        <c.SummaryHeader>
          <c.LightIcon src={LightIcon} alt="close" />
          <c.HighLight>핵심 요약 분석</c.HighLight>
        </c.SummaryHeader>
        <c.SummaryBody>
          <c.BodyTop>
            <c.BodyTopTitle>{formattedNumber}</c.BodyTopTitle>
            <c.BodyTopSubTitle>
              주변상권 및 업종을 반영하여 계산한 비용으로 실제와 다를 수
              있습니다.
            </c.BodyTopSubTitle>
          </c.BodyTop>
          <c.SplitLine />
          <c.BodyBottom>
            <c.BodyBottomLeft>
              {['지역', '업종', '면적', '층'].map(data => (
                <c.BottomText key={data}>{data}</c.BottomText>
              ))}
            </c.BodyBottomLeft>
            <c.BodyBottomRight>
              <c.BottomText>{query.split('서울특별시')}</c.BottomText>
              <c.BottomText>{subCategoryName}</c.BottomText>
              <c.BottomText>{bulidingSize}㎡</c.BottomText>
              <c.BottomText>{floor}</c.BottomText>
            </c.BodyBottomRight>
          </c.BodyBottom>
        </c.SummaryBody>
      </c.SummaryContainer>
    </c.Container>
  )
}

export default reportSummary
