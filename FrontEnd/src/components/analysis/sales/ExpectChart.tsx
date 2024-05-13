import analysisStore from '@src/stores/analysisStore'
import BarChartCompare from '@src/common/BarChartCompare'
import { TotalSalesErrPropsType } from '@src/types/AnalysisType'
import * as s from '@src/components/styles/analysis/SalesAnalysisStyle'

const ExpectChart = (props: TotalSalesErrPropsType) => {
  const { totalSalesErr } = props
  const totalSalesDataBody = analysisStore(state => state.totalSalesDataBody)

  const labels: string[] = [
    totalSalesDataBody.districtTotalSalesInfo.districtCodeName,
    totalSalesDataBody.administrationTotalSalesInfo.administrationCodeName,
    totalSalesDataBody.commercialTotalSalesInfo.commercialCodeName,
  ]

  const values: number[] = [
    totalSalesDataBody.districtTotalSalesInfo.totalSales,
    totalSalesDataBody.administrationTotalSalesInfo.totalSales,
    totalSalesDataBody.commercialTotalSalesInfo.totalSales,
  ]
  const minvalue = Math.min(...values)

  return (
    <s.ExpectChart>
      <s.ChartTitle>추정 매출액</s.ChartTitle>
      {totalSalesErr ? (
        <div>{totalSalesErr}</div>
      ) : (
        <>
          <s.ChartSubTitle>
            선택 상권의 추정매출액은 행정동 전체의 00% 이에요.
          </s.ChartSubTitle>
          <BarChartCompare
            labels={labels}
            values={values}
            minvalue={minvalue}
          />
        </>
      )}
    </s.ExpectChart>
  )
}

export default ExpectChart
