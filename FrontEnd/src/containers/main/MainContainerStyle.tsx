import styled, { keyframes } from 'styled-components'

export const MainContainer = styled.div`
  scroll-behavior: smooth;
  @media only screen and (max-width: 992px) {
    //
  }
`
// 페이지 별 기본 컨테이너 div
export const Container = styled.div`
  width: calc(100vw - 5px);
  //height: calc(100vh - 70px);
  height: calc(100vh);
  display: flex;

  margin: auto;
  @media only screen and (max-width: 992px) {
    //
    display: flex;
    flex-direction: column;
    justify-content: center;
  }
`

// 내용이 들어갈 div
export const Content = styled.div`
  height: 100%;
  margin: 0 10%;
  //background-color: #d9d9d9;
  width: 100%;
  display: flex;
  flex-direction: row;
  justify-content: space-between;

  @media only screen and (max-width: 992px) {
    flex-direction: column;
    justify-content: center;
  }
`

// 글로 소개하는 부분
export const Text = styled.div`
  width: 50%;
  //background-color: #ff7070;
  display: flex;
  flex-direction: column;
  justify-content: center;
  padding: 0 40px;
`

// 파란색 부가 설명
export const BlueText = styled.div`
  color: #336dd3;
  margin-bottom: 12px;
  font-size: 1.25rem;
`

// 제목
export const Title = styled.div`
  color: #191f28;
  margin-bottom: 16px;
  font-size: 2.5rem;
  font-weight: 700;
  word-break: keep-all;
  line-height: 140%;
`

// 설명
export const TextContent = styled.div`
  color: #606d85;
  font-size: 1.5rem;
  font-weight: 600;
  line-height: 160%;
  margin-bottom: 40px;
`

// 바로가기 버튼
export const BannerArrow = styled.div``

// 바로가기 버튼
export const GoButton = styled.div`
  font-weight: 600;
  font-size: 1.5rem;
  color: #333;
  display: flex;

  &:hover {
    cursor: pointer;
    // BannerArrow에만 호버 효과 적용

    ${BannerArrow} {
      transform: translateX(15px);
      transition: transform 0.3s ease;
    }
  }
`
// 카드 들어가는 div
export const CardList = styled.div`
  width: 50%;
  //background-color: #96ff89;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  overflow-y: auto;
  padding: 2rem;
`

// 이미지 넣을 card
export const Card = styled.div`
  width: 35vw;
  height: auto;
  //margin: calc((100vh - 70px - 450px) / 2) 0;
  margin: calc((100vh - 400px) / 2) 0;
  background: #f8f9fa;
  padding: 1rem;
  border-radius: 0.25rem;
  box-shadow: 0 0.125rem 0.25rem rgba(0, 0, 0, 0.075);

  @media only screen and (max-width: 992px) {
    margin: 0;
    width: 100%;
  }
`
export const CardImg = styled.img`
  width: 100%;
  height: auto;
  border: 2px solid #d9d9d9;
  border-radius: 5px;
`

export const CardContent = styled.div`
  display: flex;
  justify-content: center;
  font-weight: 600;
  font-size: 2rem;
  margin: 5px 0;
  color: #4a4a4a;
`

export const Recommend = styled.div`
  opacity: 0;
  transform: translateY(100px);
  transition:
    opacity 1s ease-out,
    transform 1s ease-out;

  &.visible {
    opacity: 1;
    transform: translateY(0);
    align-items: center;
  }
`

// moreService
export const EctContainer = styled.div`
  width: calc(100vw - 5px);
  height: calc(100vh);
  display: flex;
  flex-direction: column;
  align-items: center;
  margin: auto;
  padding-top: 80px;
  background-color: #f0f5ff;
  @media only screen and (max-width: 992px) {
    //
    display: flex;
    flex-direction: column;
    justify-content: center;
  }
`

// 내용이 들어갈 div
export const EctContent = styled.div`
  height: 100%;
  margin: 0 10%;
  //background-color: #d9d9d9;
  width: 100%;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;

  @media only screen and (max-width: 992px) {
    flex-direction: column;
    justify-content: center;
  }
`

// 글로 소개하는 부분
export const EctText = styled.div`
  width: 70%;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  padding: 0 40px;
`

// 설명
export const EctTextContent = styled.div`
  color: #606d85;
  font-size: 1.4rem;
  font-weight: 600;
  line-height: 160%;
  margin-bottom: 40px;
  text-align: center;
`

export const EctCardContainer = styled.div`
  width: 100%;
  display: flex;
  flex-direction: row;
  flex-wrap: wrap;
  justify-content: center;
  background-color: #f0f5ff;
  padding: 50px 40px 100px 40px;
  gap: 50px;

  @media only screen and (max-width: 992px) {
    flex-direction: column;
    align-items: center;
  }
`
const bounce = keyframes`
  0% { transform: translateY(0); }
  50% { transform: translateY(-10px); }
  100% { transform: translateY(0); }
`

export const EctCard = styled.div<{ $isup: boolean }>`
  width: 23%;
  min-width: 300px;
  height: 400px;
  background-color: white;
  margin-top: ${props => (props.$isup ? 0 : `60px`)};
  border-radius: 20px;
  padding: 10px;
  box-shadow: 0 0 30px 5px #d2def8;
  animation: ${bounce} 2s infinite;

  @media only screen and (max-width: 992px) {
    width: 70%;
    margin: 20px 0;
  }
`

export const EctCardContent = styled.div`
  width: 100%;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  padding-top: 30px;
`

export const EctImgIcon = styled.img`
  width: 150px;
`

export const CardTitle = styled.div`
  color: #191f28;
  margin: 10px 0;
  font-size: 1.5rem;
  font-weight: 700;
  word-break: keep-all;
  line-height: 140%;
`

export const CardSubTitle = styled.div`
  width: 80%;
  color: #606d85;
  font-size: 17px;
  text-align: center;
  margin-top: 10px;
  font-weight: 500;
`
