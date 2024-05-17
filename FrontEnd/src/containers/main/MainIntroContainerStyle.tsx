import styled from 'styled-components'
// import BackgroundImage from 'public/images/background.png'
import BackgroundImage from 'public/images/background123.png'

export const Container = styled.div`
  margin-top: -68px;

  @media only screen and (max-width: 992px) {
    //
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
`
export const ButtonDiv = styled.div`
  display: flex;
  flex-direction: row;
  justify-content: left;
  padding-left: 12vw;
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
`
