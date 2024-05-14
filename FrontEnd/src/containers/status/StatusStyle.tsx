import styled from 'styled-components'

export const StatusContainer = styled.div`
  display: flex;
  flex-direction: row;
  // 너비 어떻게??
  //width: window.innerWidth;
  height: 90.5vh;
  overflow: hidden;
  //background-color: #ededef;

  @media (max-width: 768px) {
    flex-direction: column;
  }
`

export const Sidebar = styled.div`
  min-width: 380px;
  height: 70vh;
  min-height: 70vh;
  box-shadow: 10px 0 10px -5px rgba(130, 130, 130, 0.2);
  border: 1px solid #dce5f2;
  border-radius: 5px;
  margin: 10px 50px;
  background-color: white;

  @media (max-width: 768px) {
    display: none;
  }
`

export const EmptyContainer = styled.div`
  width: 700px;
`

export const Content = styled.div`
  flex: 1;
`
