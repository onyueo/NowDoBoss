import styled, { keyframes } from 'styled-components'

const loadingBounce = keyframes`
  0% {
    transform: scale(1, 0.7);
  }

  40% {
    transform: scale(0.8, 1.2);
  }

  60% {
    transform: scale(1, 1);
  }

  100% {
    bottom: 140px;
  }
`

const loadingStep = keyframes`
  0% {
    box-shadow: 0 10px 0 rgba(0, 0, 0, 0),
                0 10px 0 #f2f2f2,
                -35px 50px 0 #f2f2f2,
                -70px 90px 0 #f2f2f2;
  }

  100% {
    box-shadow: 0 10px 0 #f2f2f2,
                -35px 50px 0 #f2f2f2,
                -70px 90px 0 #f2f2f2,
                -70px 90px 0 rgba(0, 0, 0, 0);
  }
`

const LoaderContainer = styled.div`
  position: relative;
  width: 120px;
  height: 90px;
  margin: 0 auto;
`

const LoaderBall = styled.div`
  content: '';
  position: absolute;
  bottom: 30px;
  left: 50px;
  height: 30px;
  width: 30px;
  border-radius: 50%;
  background: #2a9d8f;
  animation: ${loadingBounce} 0.5s ease-in-out infinite alternate;
`

const LoaderSteps = styled.div`
  content: '';
  position: absolute;
  right: 0;
  top: 0;
  height: 7px;
  width: 45px;
  border-radius: 4px;
  box-shadow:
    0 5px 0 #f2f2f2,
    -35px 50px 0 #f2f2f2,
    -70px 95px 0 #f2f2f2;
  animation: ${loadingStep} 1s ease-in-out infinite;
`

const LoadingSpinner = () => (
  <LoaderContainer>
    <LoaderBall />
    <LoaderSteps />
  </LoaderContainer>
)

export default LoadingSpinner
