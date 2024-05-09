import * as s from '@src/components/styles/analysis/SideBarMenuStyle'

interface PropsType {
  moveTo: (index: number) => void
}

const SideBarMenu = (props: PropsType) => {
  const { moveTo } = props

  return (
    <s.SidebarContainer>
      <s.MenuItem onClick={() => moveTo(0)}>유동인구</s.MenuItem>
      <s.MenuItem onClick={() => moveTo(1)}>집객시설</s.MenuItem>
      <s.MenuItem onClick={() => moveTo(2)}>점포 수</s.MenuItem>
      <s.MenuItem onClick={() => moveTo(3)}>매출분석</s.MenuItem>
      <s.MenuItem onClick={() => moveTo(4)}>상주인구</s.MenuItem>
      <s.MenuItem onClick={() => moveTo(5)}>지출내역</s.MenuItem>
    </s.SidebarContainer>
  )
}

export default SideBarMenu
