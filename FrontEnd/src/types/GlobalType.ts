export type NavigateType = {
  url: string
}

export type PromiseSaveListType = {
  dataHeader: {
    successCode: 0
    resultCode: null
    resultMessage: null
  }
  dataBody: {
    userId: number
    commercialCode: string
    commercialCodeName: string
    districtCode: string
    districtCodeName: string
    administrationCode: string
    administrationCodeName: string
    createdAt: string
  }[]
}
