import { DetailDataBody } from '@src/types/StatusType.ts'

interface DetailCommercialProps {
  props: DetailDataBody | undefined
}
const DetailCommercialComponent = ({ props }: DetailCommercialProps) => {
  console.log(props)
  return (
    <>
      <h1>상권변화 페이지</h1>
      <p>상권변화입니당</p>
      <p>상권변화입니당</p>
      <p>상권변화입니당</p>
      <p>상권변화입니당</p>
      <p>상권변화입니당</p>
      <p>상권변화입니당</p>
      <p>상권변화입니당</p>
    </>
  )
}

export default DetailCommercialComponent
