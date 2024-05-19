import { forwardRef, Ref, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { useMutation } from '@tanstack/react-query'
import selectPlaceStore from '@src/stores/selectPlaceStore'
import analysisStore from '@src/stores/analysisStore'
import { postAnalysisBookmarks } from '@src/api/analysisApi'
import { AnalysisBookmarksDataType } from '@src/types/AnalysisType'
import ResultIntro from '@src/components/analysis/ResultIntro'
import WarningBox from '@src/components/analysis/WarningBox'
import SaveModal from '@src/components/analysis/SaveModal'
import * as r from '@src/containers/analysis/ResultInroContainerStyle'
import { confetti } from '@src/App'

const ResultIntroContainer = forwardRef((_, ref: Ref<HTMLDivElement>) => {
  const selectedGoo = selectPlaceStore(state => state.selectedGoo)
  const selectedDong = selectPlaceStore(state => state.selectedDong)
  const selectedCommercial = selectPlaceStore(state => state.selectedCommercial)
  const selectedService = analysisStore(state => state.selectedService)

  const navigate = useNavigate()
  const [showModal, setShowModal] = useState(false) // 분석 결과 저장 모달 상태
  const [modalTitle, setModalTitle] = useState('')
  const [modalContent, setModalContent] = useState('')

  // 저장 시 confetti 함수
  const handleConfetti = () => {
    confetti.addConfetti({
      confettiColors: [
        '#ff00ff', // 핑크
        '#ffff00', // 노랑
        '#00ff00', // 녹색
        '#00ffff', // 청록
        '#0000ff', // 파랑
        '#ff0000', // 빨강
        '#800080', // 보라
        '#ffa500', // 주황
        '#008000', // 초록
      ],
      confettiRadius: 5,
      confettiNumber: 800,
    })
  }

  const { mutate: PostAnalysisBookmarks } = useMutation({
    mutationKey: ['PostAnalysisBookmarks'],
    mutationFn: postAnalysisBookmarks,
    onSuccess: res => {
      if (res.dataHeader.successCode === 0) {
        setModalTitle('북마크에 추가됐어요!')
        setModalContent(
          '선택하신 정보가 북마크에 추가되었어요. 언제든지 확인 가능해요.',
        )
        setShowModal(true)
        handleConfetti()
        // 성공
      } else {
        setModalTitle('북마크 추가 실패')
        setModalContent(
          '이미 북마크에 추가된 상권과 업종의 분석 데이터에요. 북마크에서 확인해 보세요!',
        )
        setShowModal(true)
      }
    },
  })

  const handlePostAnalysisBookmarks = () => {
    const data: AnalysisBookmarksDataType = {
      districtCode: String(selectedGoo.code),
      districtCodeName: selectedGoo.name,
      administrationCode: String(selectedDong.code),
      administrationCodeName: selectedDong.name,
      commercialCode: String(selectedCommercial.code),
      commercialCodeName: selectedCommercial.name,
      serviceType: selectedService.serviceType,
      serviceCode: selectedService.serviceCode,
      serviceCodeName: selectedService.serviceCodeName,
    }

    PostAnalysisBookmarks(data)
  }

  const handleGoToBookmarks = () => {
    setShowModal(false)
    navigate('/profile/bookmarks/analysis')
  }

  return (
    <r.Container ref={ref}>
      <r.ImgDiv>
        <img src="/images/Buildings.png" alt="buildings" />
      </r.ImgDiv>
      {showModal && (
        <SaveModal
          title={modalTitle}
          content={modalContent}
          onClose={() => setShowModal(false)}
          onConfirm={handleGoToBookmarks}
        />
      )}
      <ResultIntro handlePostAnalysisBookmarks={handlePostAnalysisBookmarks} />
      <WarningBox />
    </r.Container>
  )
})

ResultIntroContainer.displayName = 'ResultIntroContainer'
export default ResultIntroContainer
