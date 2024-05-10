import styled from 'styled-components'
import bookMark from '@src/assets/bookmark.svg'

const Header = styled.div<{ $isOpen: boolean }>`
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  padding: 10px 10px 5px 20px;
  border-top-left-radius: 5px;
  border-top-right-radius: 5px;
  border-bottom-left-radius: ${props => (props.$isOpen ? '0' : '5px')};
  border-bottom-right-radius: ${props => (props.$isOpen ? '0' : '5px')};

  border: ${props => (props.$isOpen ? '' : '2px solid #236cff')};
  border-bottom: ${props => (props.$isOpen ? '2px solid #d9d9d9' : '')};
  background-color: ${props => (props.$isOpen ? '#f2f2f2' : '#236cff')};
  color: ${props => (props.$isOpen ? '#000000' : '#ffffff')};

  @media only screen and (max-width: 400px) {
  }
`

const Icon = styled.img``

const Container = styled.div`
  display: flex;
  flex-direction: row;
  align-items: end;
`
const Content = styled.div``

const Title = styled.div`
  font-weight: 600;
  font-size: 1.4rem;
`
const SubTitle = styled.div`
  font-size: 0.9rem;
  padding: 0 0 5px;
`
const CloseButton = styled.div`
  background-color: #e2ebf7;
  border-radius: 100%;

  color: #236cff;
  font-weight: 600;
  width: 2.2rem;
  height: 2.2rem;
  font-size: 1.3rem;
  display: flex;
  justify-content: center;
  align-items: center;
  user-select: none;

  &:hover {
    background-color: #cfdcff;
    color: #236cff;
  }

  &:active {
    background-color: #78a1ff;
    color: #e2ebf7;
    //box-shadow: 0 0 0 2px #e2ebf7;
  }
`

type SidebarHeaderPropsType = {
  title: string
  subTitle: string
  close: boolean
  // eslint-disable-next-line react/require-default-props
  setOpen?: (open: boolean) => void
  icon: boolean
  isOpen: boolean
}

const SidebarHeader = (props: SidebarHeaderPropsType) => {
  const { title, subTitle, close, setOpen, icon, isOpen = true } = props

  return (
    <Header $isOpen={isOpen}>
      <Container>
        {icon && <Icon src={bookMark} />}
        <Content>
          <Title>{title}</Title>
          <SubTitle>{subTitle}</SubTitle>
        </Content>
      </Container>
      {close && (
        <CloseButton onClick={() => setOpen && setOpen(false)}>Ⅹ</CloseButton>
      )}
    </Header>
  )
}
export default SidebarHeader
