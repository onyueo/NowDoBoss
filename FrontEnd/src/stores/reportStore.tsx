import { create } from 'zustand'

interface ReportState {
  address: string
  setAddress: (address: string) => void
  query: string
  setQuery: (query: string) => void
  sido: string
  setSido: (sido: string) => void
  sigungu: string
  setSigungu: (sigungu: string) => void
}

const useReportStore = create<ReportState>(set => ({
  address: '',
  setAddress: address => set({ address }),
  query: '',
  setQuery: query => set({ query }),
  sido: '',
  setSido: sido => set({ sido }),
  sigungu: '',
  setSigungu: sigungu => set({ sigungu }),
}))

export default useReportStore
