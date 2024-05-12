// 게시글 생성 시 보내는 데이터 type
export type CommunityCreateDataType = {
  category: string
  title: string
  content: string
  images: string[]
}

export type ImageType = { imageId: number | null; url: string }

// 게시글 수정 시 보내는 데이터 type
export type CommunityModifyDataType = {
  communityId: number
  data: {
    title: string
    content: string
    images: ImageType[]
  }
}

// 상세 페이지 get 요청 시 받은 데이터 리스트
export type CommunityData = {
  communityId: number
  category: string
  title: string
  content: string
  writerNickname: string
  readCount: number
  commentCount: number
  writerId: number
  writerProfileImage: string
  createdAt: string
  images: {
    imageId: 6
    url: string
  }[]
}

// 리스트 페이지 get 요청 시 받은 데이터 리스트
export type CommunityListData = {
  communityId: number
  category: string
  title: string
  content: string
  image: string | undefined
  writerId: number
  profileImage: string
  writerNickname: string
  readCount: number
  commentCount: number
}[]

export type CommentCreateType = {
  communityId: number
  data: { content: string }
}

// 댓글 type
export type CommentDataType = {
  commentId: number
  content: string
  createdAt: string
  writerId: number
  writerNickname: string
  writerProfileImage: string
}

// 댓글 리스트 type
export type CommentListDataType = {
  dataBody: CommentDataType[]
  dataHeader: { successCode: number; resultCode: number; resultMessage: string }
}

// 댓글 리스트 type
export type CommentDeleteDataType = {
  communityId: number
  commentId: number
}

// 댓글 리스트 type
export type CommentModifyDataType = {
  communityId: number
  commentId: number
  data: { content: string }
}

// 이미지 url promise type
export type ImageUploadPromiseType = {
  dataHeader: { successCode: number; resultCode: number; resultMessage: string }
  dataBody: string
}
