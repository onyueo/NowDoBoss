import styled from 'styled-components'

export const Container = styled.div`
  width: 100%;
  margin-top: 5vh;
  padding: 0 10%;
  box-sizing: border-box;
  display: flex;
  flex-direction: column;

  @media (max-width: 576px) {
    padding: 0 3%;
  }
`

export const ImgDiv = styled.div`
  height: auto;
  align-content: center;
  //margin-left: -10%; /* Container의 padding을 상쇄 */
  //margin-right: -10%; /* Container의 padding을 상쇄 */
  //
  //@media (max-width: 576px) {
  //  margin-left: -3%; /* 모바일 화면에서 Container의 padding 상쇄 */
  //  margin-right: -3%; /* 모바일 화면에서 Container의 padding 상쇄 */
  //}
  //
  //img {
  //  width: 100%;
  //}
  @media (max-width: 425px) {
    padding: 0 3%;
  }

  img {
    width: 100%;
  }
`

export const ResultWrap = styled.div`
  width: 100%;
  min-height: 3vh;
  border-radius: 10px;
  background-color: #2a65f0;
  display: flex;
  padding: 20px;
  box-sizing: border-box;
`

export const IntroTitle = styled.div`
  font-size: 1.4rem;
  color: #ffffff;
  font-weight: 700;
  margin-right: auto;
`
