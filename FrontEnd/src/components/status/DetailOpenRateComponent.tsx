import { DetailDataBody } from '@src/types/StatusType'

interface DetailOpenRateProps {
  props: DetailDataBody | undefined
}

const DetailOpenRateComponent = ({ props }: DetailOpenRateProps) => {
  console.log(props)
  return (
    <>
      <h1>개업률 페이지</h1>
      <p>개업률 ~~~~~~</p>
      <p>개업률 ~~~~~~</p>
      <p>개업률 ~~~~~~</p>
      <p>개업률 ~~~~~~</p>
      <p>개업률 ~~~~~~</p>
      <p>개업률 ~~~~~~</p>
      <p>개업률 ~~~~~~</p>
      <p>개업률 ~~~~~~</p>
      <p>개업률 ~~~~~~</p>
    </>
  )
}

export default DetailOpenRateComponent
