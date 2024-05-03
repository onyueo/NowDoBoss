import { CookiesProvider } from 'react-cookie'
import { BrowserRouter, Route, Routes } from 'react-router-dom'
import Header from '@src/common/Header'
import MainPage from '@src/pages/MainPage'
import SignUpPage from '@src/pages/SignUpPage'
import LoginPage from '@src/pages/LoginPage'
import SocialLoadingPage from '@src/pages/SocialLoadingPage'
import CommunityPage from '@src/pages/CommunityPage'
import CommunityRegisterPage from '@src/pages/CommunityRegisterPage'
import CommunityDetailPage from '@src/pages/CommunityDetailPage'
import StatusPage from '@src/pages/StatusPage'
import AnalysisPage from '@src/pages/AnalysisPage'
import RecommendPage from '@src/pages/RecommendPage'
import SimulationPage from '@src/pages/SimulationPage'
import SimulationReportPage from '@src/pages/SimulationReportPage'
import ChattingPage from '@src/pages/ChattingPage'
import CommunityListPage from '@src/pages/CommunityListPage'

function App() {
  return (
    <CookiesProvider>
      <BrowserRouter>
        <Header />
        <Routes>
          <Route path="/" element={<MainPage />} />
          <Route path="/register" element={<SignUpPage />} />
          <Route path="/login" element={<LoginPage />} />
          <Route
            path="/member/loading/:provider"
            element={<SocialLoadingPage />}
          />
          {/* 커뮤니티 */}
          <Route path="/community/*" element={<CommunityPage />}>
            <Route path="list" element={<CommunityListPage />} />
            <Route path="register" element={<CommunityRegisterPage />} />
            <Route path=":communityId" element={<CommunityDetailPage />} />
            <Route path="chatting/:chatId" element={<ChattingPage />} />
          </Route>
          <Route path="/status" element={<StatusPage />} />
          <Route path="/analysis" element={<AnalysisPage />} />
          <Route path="/recommend" element={<RecommendPage />} />
          <Route path="/simulation" element={<SimulationPage />} />
          <Route path="/simulation/report" element={<SimulationReportPage />} />
        </Routes>
      </BrowserRouter>
    </CookiesProvider>
  )
}

export default App
