import { useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import selectPlaceStore from '@src/stores/selectPlaceStore'
import analysisStore from '@src/stores/analysisStore'
import { ResultIntroPropsType } from '@src/types/AnalysisType'
import FlowSummaryCard from '@src/components/analysis/FlowSummaryCard'
import SalesSummaryCard from '@src/components/analysis/SalesSummaryCard'
import TipBox from '@src/components/analysis/TipBox'
import TotalSummaryCard from '@src/components/analysis/TotalSummaryCard'
import * as r from '@src/components/styles/analysis/ResultIntroStyle'
import { useMutation } from '@tanstack/react-query'
import { reportAnalysisKaKaoUrl } from '@src/api/kakaoShareApi'
import { AnalysisDataType } from '@src/types/KaKaoShareType'

const { Kakao } = window

const ResultIntro = (props: ResultIntroPropsType) => {
  const { handlePostAnalysisBookmarks } = props
  const navigate = useNavigate()
  const userLoggedIn = localStorage.getItem('isLogIn') === 'true' // 로그인 상태 확인 (localStorage 사용)
  const selectedGoo = selectPlaceStore(state => state.selectedGoo)
  const selectedDong = selectPlaceStore(state => state.selectedDong)
  const selectedCommercial = selectPlaceStore(state => state.selectedCommercial)
  const selectedServiceType = analysisStore(state => state.selectedServiceType)
  const selectedService = analysisStore(state => state.selectedService)
  const [type, setType] = useState('')

  useEffect(() => {
    switch (selectedServiceType) {
      case 'RESTAURANT':
        setType('음식점')
        break
      case 'ACADEMY':
        setType('교육')
        break
      case 'LEISURE':
        setType('레저/오락')
        break
      case 'SERVICE':
        setType('서비스')
        break
      case 'RETAIL':
        setType('도소매')
        break
      case 'HOUSEHOLDS':
        setType('생활용품')
        break
      default:
        setType('')
    }
  }, [selectedServiceType])

  // 카카오톡 공유
  useEffect(() => {
    Kakao.cleanup()
    Kakao.init('e55da734c04c78fcc069c3dab68f3c1e')
    // console.log(Kakao.isInitialized())
  }, [])

  // 카카오톡 share 탬플릿
  const serverUrl = 'http://localhost:5173'
  // const serverUrl = 'https://k10c208.p.ssafy.io'
  const shareKakao = (data: string) => {
    window.Kakao.Link.sendCustom({
      templateId: 107914,
      templateArgs: {
        Server_Url: serverUrl,
        Path: 'share/analysis',
        Token: data,
      },
    })
  }

  // 카톡 공유 temp
  const { mutate: mutateKakaoReport } = useMutation({
    mutationFn: reportAnalysisKaKaoUrl,
    onSuccess: res => {
      shareKakao(res.dataBody.token)
    },
    onError: error => {
      console.error(error)
    },
  })

  const onClickShare = () => {
    const reportCreateData: AnalysisDataType = {
      url: `${serverUrl}/api/v1/analysis`,
      input: {
        districtCode: selectedGoo.code,
        administrationCode: selectedDong.code,
        commercialCode: selectedCommercial.code,
        serviceCode: selectedService.serviceCode,
        periodCode: null,
      },
    }
    mutateKakaoReport(reportCreateData)
  }
  console.log(onClickShare)

  return (
    <r.Container>
      <r.LeftWrap>
        <r.InfoDiv>
          <r.ServiceText>
            {selectedService.serviceCodeName} ({type})
          </r.ServiceText>
          <r.CommercialText>{selectedCommercial.name}</r.CommercialText>
          <r.GuText>
            서울시 {selectedGoo.name} {selectedDong.name}
          </r.GuText>
          {userLoggedIn ? (
            <r.BookmarksDiv onClick={() => handlePostAnalysisBookmarks()}>
              <r.BookmarkText>분석 리포트 저장하기</r.BookmarkText>
            </r.BookmarksDiv>
          ) : (
            <r.BookmarksDiv onClick={() => navigate('/login')}>
              <r.BookmarkText>분석 리포트 저장하기</r.BookmarkText>
              <r.BookmarksSmallText>
                로그인이 필요한 서비스입니다.
              </r.BookmarksSmallText>
            </r.BookmarksDiv>
          )}
        </r.InfoDiv>
        {/* <r.ShareBox onClick={onClickShare}> */}
        {/*  <r.ShareBoxText>카카오톡 공유 바로가기</r.ShareBoxText> */}
        {/* </r.ShareBox> */}
      </r.LeftWrap>
      <r.RightWrap>
        <r.SummaryWrap>
          <r.FlowWrap>
            <r.FlowCard>
              <FlowSummaryCard />
            </r.FlowCard>
            <r.TipTitle>
              나도보스 <span>TIP</span>
            </r.TipTitle>
            <r.TipBox>
              <TipBox />
            </r.TipBox>
          </r.FlowWrap>
          <r.SalesCard>
            <SalesSummaryCard />
          </r.SalesCard>
        </r.SummaryWrap>
        <r.SummaryBox>
          <TotalSummaryCard />
        </r.SummaryBox>
      </r.RightWrap>
    </r.Container>
  )
}

export default ResultIntro
