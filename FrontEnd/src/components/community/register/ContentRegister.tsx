import GoBackButton from '@src/common/GoBackButton'
import * as c from '@src/components/styles/community/CommunityRegisterStyle'
import React, { useEffect, useState } from 'react'
import arrow_down from '@src/assets/arrow_down.svg'
import arrow_up from '@src/assets/arrow_up.svg'
import ImageUpload from '@src/components/community/register/ImageUpload'
import { useMutation, useQuery } from '@tanstack/react-query'
import { useNavigate } from 'react-router-dom'
import {
  communityCreate,
  communityModify,
  fetchCommunityDetail,
} from '@src/api/communityApi'
import Swal from 'sweetalert2'

interface ContentRegisterPropsType {
  modifyCommunityId: number
}

const ContentRegister = (props: ContentRegisterPropsType) => {
  const { modifyCommunityId } = props
  const navigate = useNavigate()
  const [titleValue, setTitle] = useState<string>('')
  const [contentValue, setContent] = useState<string>('')
  const [selectedCategoryValue, setSelectedCategory] = useState<string>('')
  // 보여주기 위한 값
  const [outputCategoryValue, setOutputCategory] =
    useState<string>('카테고리를 선택해주세요.')
  // const [imageFilesValue, setImageFiles] = useState<File[]>([])

  const [imageViewValue, setImageView] = useState<string[]>([])
  const [isDropdownOpen, setIsDropdownOpen] = useState<boolean>(false)
  const categories = [
    { name: '이모저모', value: 'ETC' },
    { name: '인테리어', value: 'INTERIOR' },
    { name: '상권공유', value: 'COMMERCIAL_AREA' },
    { name: '동업제안', value: 'PARTNERSHIP' },
    { name: '창업고민', value: 'START_UP' },
  ]
  const [isValid, setIsValid] = useState<boolean>(false)
  useEffect(() => {
    if (titleValue && contentValue && selectedCategoryValue) {
      setIsValid(true)
    } else {
      setIsValid(false)
    }
  }, [titleValue, contentValue, selectedCategoryValue])

  // 수정 페이지에서 쓸 데이터 요청
  const { data } = useQuery({
    queryKey: ['communityDetail', modifyCommunityId],
    queryFn: () => fetchCommunityDetail(modifyCommunityId),
    enabled: !!modifyCommunityId,
  })

  useEffect(() => {
    if (data && modifyCommunityId) {
      setTitle(data.dataBody.title)
      setContent(data.dataBody.content)
      setSelectedCategory(data.dataBody.category)
    }
  }, [modifyCommunityId, data])

  // 게시글 생성
  const { mutate: mutateCreateForm } = useMutation({
    mutationKey: ['communityCreateForm'],
    mutationFn: communityCreate,
    onSuccess() {
      navigate(`/community`)
    },
    onError() {
      console.log('에러남')
    },
  })

  // 게시글 수정
  const { mutate: mutateModifyForm } = useMutation({
    mutationKey: ['communityModify'],
    mutationFn: communityModify,
    onSuccess() {
      navigate(`/community/${modifyCommunityId}`)

      // 수정 완료 모달
      const Toast = Swal.mixin({
        toast: true,
        position: 'bottom-end',
        showConfirmButton: false,
        timer: 3000,
        timerProgressBar: true,
        didOpen: toast => {
          // eslint-disable-next-line no-param-reassign
          toast.onmouseenter = Swal.stopTimer
          // eslint-disable-next-line no-param-reassign
          toast.onmouseleave = Swal.resumeTimer
        },
      })
      Toast.fire({
        icon: 'success',
        title: '게시글이 수정되었습니다.',
      })
    },
    onError() {
      console.log('에러남')
    },
  })

  const handleSubmit = () => {
    // 수정하는 상황이 아니라면
    if (modifyCommunityId === 0) {
      const ArticleData = {
        title: titleValue,
        content: contentValue,
        category: selectedCategoryValue,
        images: imageViewValue,
      }
      mutateCreateForm(ArticleData)
    } else {
      // 수정하는 상황이라면
      const ArticleData = {
        communityId: modifyCommunityId,
        data: {
          title: titleValue,
          content: contentValue,
          images: imageViewValue,
        },
      }
      mutateModifyForm(ArticleData)
    }
  }

  return (
    <c.Container>
      <c.Header>
        <GoBackButton />

        {/* 제출 버튼 */}
        <c.SubmitButton
          $isValid={isValid}
          onClick={() => {
            handleSubmit()
          }}
        >
          완료
        </c.SubmitButton>
      </c.Header>
      {/* 내용 입력 */}
      <c.TitleInput
        $isActive={titleValue.length > 0}
        placeholder="제목을 입력해주세요."
        defaultValue={titleValue}
        maxLength={19}
        onChange={(e: React.ChangeEvent<HTMLTextAreaElement>) => {
          setTitle(e.target.value)
        }}
      />
      <c.ContentCount>{titleValue.length} / 20</c.ContentCount>

      <c.ContentInput
        $isActive={contentValue.length > 0}
        placeholder="내용을 입력해주세요."
        defaultValue={contentValue}
        maxLength={499}
        onChange={(e: React.ChangeEvent<HTMLTextAreaElement>) => {
          setContent(e.target.value)
        }}
      />
      <c.ContentCount>{contentValue.length} / 500</c.ContentCount>

      {/* 드롭다운 메뉴 토글 버튼 */}
      <c.Dropdown>
        <c.DropdownTitle>카테고리</c.DropdownTitle>
        <c.DropdownButton onClick={() => setIsDropdownOpen(!isDropdownOpen)}>
          <c.SelectedContent> {outputCategoryValue}</c.SelectedContent>
          <c.DropdownIcon src={isDropdownOpen ? arrow_up : arrow_down} />
        </c.DropdownButton>

        {/* 드롭다운 메뉴 항목 */}
        {isDropdownOpen && (
          <c.DropdownBox>
            {categories.map(category => (
              <c.DropdownContent
                key={category.name}
                onClick={() => {
                  setOutputCategory(category.name)
                  setSelectedCategory(category.value)
                  setIsDropdownOpen(false)
                }}
              >
                {category.name}
              </c.DropdownContent>
            ))}
          </c.DropdownBox>
        )}
      </c.Dropdown>

      {/* 사진 추가 */}
      <ImageUpload
        imageViewValue={imageViewValue}
        setImageView={setImageView}
      />
    </c.Container>
  )
}
export default ContentRegister
