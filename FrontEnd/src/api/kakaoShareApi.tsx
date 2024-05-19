import { customAxios } from '@src/util/auth/customAxios'
import { AnalysisDataType, SimulationDataType } from '@src/types/KaKaoShareType'

// 시뮬레이션 카카오톡 공유
export const reportKaKaoUrl = async (data: SimulationDataType) => {
  return customAxios
    .post(`/share`, data)
    .then(res => res.data)
    .catch(err => console.log(err))
}

// 시뮬레이션 카카오톡 공유->요청
export const fetchKaKaoUrl = async (token: string) => {
  return customAxios
    .get(`/share/${token}`)
    .then(res => res.data)
    .catch(err => console.log(err))
}

// 상권분석 카카오톡 공유
export const reportAnalysisKaKaoUrl = async (data: AnalysisDataType) => {
  return customAxios
    .post(`/share`, data)
    .then(res => res.data)
    .catch(err => console.log(err))
}
