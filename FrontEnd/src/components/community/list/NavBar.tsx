import * as n from '@src/components/styles/community/NavbarStyle'
import { useCallback, useEffect, useState } from 'react'
import useCommunityStore, { Category } from '@src/stores/communityStore'
import { useNavigate } from 'react-router-dom'
import penIcon from '@src/assets/pen.svg'
import NotLogin from '@src/common/swal/NotLogin'

export type NavBarPropsType = {
  setCategory: (category: Category) => void
}
const NavBar = (props: NavBarPropsType) => {
  const { setCategory } = props
  const navigate = useNavigate()

  // 로그인 한 사용자인지 확인
  const userLoggedIn = localStorage.getItem('isLogIn') === 'true'

  // store에 저장해둔 카테고리 받아오기
  const { categories, selectedCategory, setModifyCommunityId } =
    useCommunityStore(state => ({
      categories: state.categories,
      selectedCategory: state.selectedCategory,
      setModifyCommunityId: state.setModifyCommunityId,
    }))

  // 선택한 문자열 filter 해서 style prop 하기 위한 값
  const [isChoice, setIsChoice] = useState<string>(
    selectedCategory.name ? selectedCategory.name : '전체보기',
  )

  // 게시글 작성 로직
  const handleCreate = () => {
    if (userLoggedIn) {
      setModifyCommunityId(Number(0))
      navigate('/community/register')
    } else {
      NotLogin(navigate)
    }
  }

  // 스크롤 내렸을 때 사라지게 하는 로직
  const [isTransparent, setIsTransparent] = useState<boolean>(true)
  const [lastScrollY, setLastScrollY] = useState<number>(0)

  // 현재 스크롤과 이전 스크롤 상태 비교해서 올림, 내림 스크롤 판단하는 로직
  const handleScroll = useCallback(() => {
    const currentScrollY = window.scrollY
    if (currentScrollY > lastScrollY) {
      setIsTransparent(false)
    } else {
      setIsTransparent(true)
    }
    setLastScrollY(currentScrollY)
  }, [lastScrollY])

  useEffect(() => {
    window.addEventListener('scroll', handleScroll)
    return () => {
      window.removeEventListener('scroll', handleScroll)
    }
  }, [handleScroll, lastScrollY])

  return (
    <n.Container>
      <n.Community $isTransparent={isTransparent}>
        <n.Title>커뮤니티</n.Title>
        <n.Sub>
          관심사가 비슷한 회원들과 <br />
          소통해서 성공에 다가가세요.
        </n.Sub>
        <n.CreateButton onClick={handleCreate}>
          {/* <b>성공하고싶나요?</b> */}게시글 작성하기 &nbsp;&nbsp;→
        </n.CreateButton>

        {categories.map(navCategory => (
          <n.Category
            key={navCategory.name}
            $isChoice={isChoice === navCategory.name}
            onClick={() => {
              setIsChoice(navCategory.name)
              setCategory(navCategory)
              navigate('/community/list')
            }}
          >
            <n.Icon
              src={
                isChoice === navCategory.name
                  ? navCategory.iconActive
                  : navCategory.iconInactive
              }
              alt=""
            />
            <n.Text>{navCategory.name}</n.Text>
          </n.Category>
        ))}
      </n.Community>
      <n.CreateIcon
        src={penIcon}
        onClick={handleCreate}
        $isTransparent={isTransparent}
      />
    </n.Container>
  )
}

export default NavBar
