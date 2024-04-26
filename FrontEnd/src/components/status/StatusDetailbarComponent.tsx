import * as c from '@src/components/styles/status/StatusDetailbarStyle'
import DetailPopulationComponent from '@src/components/status/DetailPopulationComponent'
import DetailStoreNumberComponent from '@src/components/status/DetailStoreNumberComponent'
import DetailOpenRateComponent from '@src/components/status/DetailOpenRateComponent'
import DetailCloseRateComponent from '@src/components/status/DetailCloseRateComponent'
import DetailAnalysisComponent from '@src/components/status/DetailAnalysisComponent'
import DetailCommercialComponent from '@src/components/status/DetailCommercialComponent'
import Xmark from 'src/assets/xmark_solid_nomal.svg'
import bookmark from 'src/assets/bookmark.svg'
import { useRef, useState, useEffect, useMemo } from 'react'

interface StatusDetailbarProps {
  selectedRegion: string | null
  onClickRegionHandler: (region: string | null) => void
  regionCode: number | null
}

const StatusDetailbarComponent = ({
  selectedRegion,
  onClickRegionHandler,
  regionCode,
}: StatusDetailbarProps) => {
  // console.log(`선택한 지역구 코드: ${regionCode}`)
  const [activeTab, setActiveTab] = useState<string>('유동인구')
  const scrollRef = useRef<HTMLDivElement[]>([])

  const categories = useMemo(
    () => [
      { name: '유동인구', component: DetailPopulationComponent, props: {} },
      { name: '점포수', component: DetailStoreNumberComponent, props: {} },
      { name: '개업률', component: DetailOpenRateComponent, props: {} },
      { name: '폐업률', component: DetailCloseRateComponent, props: {} },
      { name: '매출분석', component: DetailAnalysisComponent, props: {} },
      { name: '상권변화', component: DetailCommercialComponent, props: {} },
    ],
    [],
  )

  const onClickActiveTab = (tab: string) => {
    setActiveTab(tab)
  }

  useEffect(() => {
    const index = categories.findIndex(category => category.name === activeTab)
    scrollRef.current[index]?.scrollIntoView({ behavior: 'smooth' })
  }, [activeTab, categories])

  useEffect(() => {
    // console.log('------------------------')
    // console.log(scrollRef.current)
    const handleScroll = () => {
      console.log('스크롤중~~~')
    }

    document.addEventListener('scroll', handleScroll)

    return () => {
      document.removeEventListener('scroll', handleScroll)
    }
  }, [])

  return (
    <c.Container>
      <c.FixedCategoryBar>
        <c.BarTopHeader>
          <c.BookMarkIcon src={bookmark} alt="bookmark" />
          <c.BarTopTitle>{selectedRegion}</c.BarTopTitle>
          <c.BarTopSubtitle>분석 리포트</c.BarTopSubtitle>
          <c.BarTopSeason>(2023 3분기 기준)</c.BarTopSeason>
          <c.CloseIcon
            src={Xmark}
            alt="close"
            onClick={() => onClickRegionHandler(null)}
          />
        </c.BarTopHeader>
        <c.BarInnerContainer>
          {categories.map((category, index) => (
            <c.BarInnerText
              key={index}
              onClick={() => onClickActiveTab(category.name)}
              isActive={category.name === activeTab}
            >
              {category.name}
            </c.BarInnerText>
          ))}
        </c.BarInnerContainer>
      </c.FixedCategoryBar>

      <p>선택한 지역구 코드: {regionCode} </p>

      {categories.map((category, index) => (
        <div key={index}>
          <c.SeparateLine />
          <c.TabBarContainer
            ref={el => {
              if (el) scrollRef.current[index] = el
            }}
          >
            {/* <category.component props={category.props} /> */}
            <category.component />
          </c.TabBarContainer>
        </div>
      ))}
    </c.Container>
  )
}

export default StatusDetailbarComponent
