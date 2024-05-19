import styled from 'styled-components'
// import BackgroundImage from 'public/images/background.png'
import BackgroundImage from 'public/images/background123.png'

export const Container = styled.div`
  width: calc(100vw - 5px);
  height: 200vh;
  margin-top: -68px;

  @media only screen and (max-width: 1015px) {
  }
`

export const Main = styled.div`
  width: calc(100vw - 5px);
  //height: calc(100vh - 68px);
  height: calc(100vh);
  scale: 1.005;
  display: flex;
  flex-direction: column;
  justify-content: center;
  background-image: url(${BackgroundImage});
  background-size: cover; /* 이미지가 전체를 덮도록 설정 */
  background-position: center; /* 이미지가 가운데 위치하도록 설정 */
  background-repeat: no-repeat; /* 이미지가 반복되지 않도록 설정 */
  opacity: 0.85;
`
export const MainContent = styled.div`
  font-weight: 700;
  font-size: 3rem;
  padding-left: 12vw;
  @media only screen and (max-width: 1200px) {
    padding-left: 10vw;
    font-size: 2.8rem;
  }
  @media only screen and (max-width: 1015px) {
    padding-left: 8vw;
    font-size: 2.5rem;
  }
  @media only screen and (max-width: 800px) {
    padding-left: 8vw;
    font-size: 2.2rem;
  }
  @media only screen and (max-width: 700px) {
    padding-left: 7vw;
    font-size: 2rem;
  }
  @media only screen and (max-width: 600px) {
    padding-left: 6vw;
    font-size: 1.8rem;
  }
  @media only screen and (max-width: 530px) {
    padding-left: 5vw;
    font-size: 1.6rem;
  }
  @media only screen and (max-width: 470px) {
    padding-left: 18%;
    font-size: 1.4rem;
  }
`
export const ButtonDiv = styled.div`
  display: flex;
  flex-direction: row;
  justify-content: left;
  padding-left: 12vw;
  @media only screen and (max-width: 1200px) {
    padding-left: 10vw;
    font-size: 0.9rem;
  }
  @media only screen and (max-width: 1015px) {
    padding-left: 8vw;
    font-size: 0.8rem;
  }
  @media only screen and (max-width: 800px) {
    padding-left: 8vw;
    font-size: 0.75rem;
  }
  @media only screen and (max-width: 700px) {
    padding-left: 7vw;
    font-size: 0.7rem;
  }
  @media only screen and (max-width: 600px) {
    padding-left: 6vw;
    font-size: 0.65rem;
  }
  @media only screen and (max-width: 470px) {
    padding-left: 18%;
    font-size: 0.5rem;
  }
`
export const MainButton = styled.div`
  background-color: #236eff;
  color: white;
  border-radius: 10px;
  padding: 12px 15px;
  margin: 15px;
  transition: all 0.3s ease-in-out; /* 부드러운 트랜지션 효과 */

  &:hover {
    transform: translateY(-5px); /* 위로 5px 이동 */
    box-shadow: 0 4px 4px rgba(0, 0, 0, 0.3); /* 입체감을 주는 그림자 효과 */
  }

  @media only screen and (max-width: 700px) {
    margin: 10px;
    padding: 10px 12px;
  }
  @media only screen and (max-width: 600px) {
    margin: 8px;
    padding: 8px 10px;
  }
`
export const Sub = styled.div`
  width: calc(100vw - 5px);
  //height: calc(100vh - 70px);
  height: calc(100vh);
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  background: linear-gradient(180deg, #f6f8fa 0%, rgba(246, 248, 251, 0) 100%),
    radial-gradient(
      100% 252.63% at 0% 100%,
      rgba(204, 217, 249, 0.802885) 0%,
      rgba(255, 241, 230, 0.804027) 100%
    );
`
export const SubContent = styled.div`
  font-weight: 700;
  font-size: 1.7rem;
  text-align: center;
  line-height: 200%;
  opacity: 0;
  transform: translateY(50px);
  transition:
    opacity 2s ease-out,
    transform 2s ease-out;

  &.visible {
    opacity: 1;
    transform: translateY(0);
  }

  @media only screen and (max-width: 1200px) {
    font-size: 1.5rem;
  }
  @media only screen and (max-width: 1015px) {
    font-size: 1.3rem;
  }
  @media only screen and (max-width: 800px) {
    font-size: 1.1rem;
  }
  @media only screen and (max-width: 700px) {
    font-size: 1rem;
  }
  @media only screen and (max-width: 600px) {
    font-size: 0.9rem;
  }
  @media only screen and (max-width: 530px) {
    font-size: 0.8rem;
  }
  @media only screen and (max-width: 440px) {
    font-size: 0.7rem;
  }
`
