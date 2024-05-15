import SummaryIconCard from '@src/common/SummaryIconCard'
import * as s from '@src/components/styles/analysis/SummaryCardStyle'

const FlowSummaryCard = () => {
  return (
    <s.Container>
      <s.Title>누가있나요?</s.Title>
      <s.CardWrap>
        <s.CardDiv>
          <SummaryIconCard
            title="가장 많은 성별"
            icon="/icons/toilet.png"
            text="남성"
          />
        </s.CardDiv>
        <s.CardDiv>
          <SummaryIconCard
            title="가장 많은 연령대"
            icon="/icons/three_people.png"
            text="20대"
          />
        </s.CardDiv>
        <s.CardDiv>
          <SummaryIconCard
            title="가장 많은 시간대"
            icon="/icons/clock.png"
            text="18~22시"
          />
        </s.CardDiv>
      </s.CardWrap>
    </s.Container>
  )
}

export default FlowSummaryCard
