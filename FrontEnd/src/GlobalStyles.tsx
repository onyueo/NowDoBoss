import { createGlobalStyle } from 'styled-components'
import reset from 'styled-reset'

const GlobalStyles = createGlobalStyle`
    ${reset}
    * {
        margin: 0;
        padding: 0;
        box-sizing: border-box;
    }
    
    @font-face {
        font-family: "Pretendard";
        font-style: normal;
        font-weight: 100;
        src: url('/fonts/Pretendard-Thin.woff2') format('woff2');
        font-display: swap;
    }

    @font-face {
        font-family: "Pretendard";
        font-style: normal;
        font-weight: 200;
        src: url('/fonts/Pretendard-ExtraLight.woff2') format('woff2');
        font-display: swap;
    }

    @font-face {
        font-family: "Pretendard";
        font-style: normal;
        font-weight: 300;
        src: url('/fonts/Pretendard-Light.woff2') format('woff2');
        font-display: swap;
    }

    @font-face {
        font-family: "Pretendard";
        font-style: normal;
        font-weight: 400;
        src: url('/fonts/Pretendard-Regular.woff2') format('woff2');
        font-display: swap;
    }

    @font-face {
        font-family: "Pretendard";
        font-style: normal;
        font-weight: 500;
        src: url('/fonts/Pretendard-Medium.woff2') format('woff2');
        font-display: swap;
    }

    @font-face {
        font-family: "Pretendard";
        font-style: normal;
        font-weight: 600;
        src: url('/fonts/Pretendard-SemiBold.woff2') format('woff2');
        font-display: swap;
    }

    @font-face {
        font-family: "Pretendard";
        font-style: normal;
        font-weight: 700;
        src: url('/fonts/Pretendard-Bold.woff2') format('woff2');
        font-display: swap;
    }

    @font-face {
        font-family: "Pretendard";
        font-style: normal;
        font-weight: 800;
        src: url('/fonts/Pretendard-ExtraBold.woff2') format('woff2');
        font-display: swap;
    }

    @font-face {
        font-family: "Pretendard";
        font-style: normal;
        font-weight: 900;
        src: url('/fonts/Pretendard-Black.woff2') format('woff2');
        font-display: swap;
    }

    html {
        font-family: 'Pretendard', sans-serif;
        line-height: 1.5;
        font-weight: 400;
        margin: 0;
    }

    body {
        font-family: 'Pretendard', sans-serif;
        line-height: 1.5;
        font-weight: 400;
        margin: 0;
        padding-top: 68px;
        width: 100vw;
        height: calc(100vh - 68px);
        overflow-x: hidden;
    }

    :root {
        font-family: 'Pretendard', sans-serif;
        font-synthesis: none;
        text-rendering: optimizeLegibility;
        -webkit-font-smoothing: antialiased;
        -moz-osx-font-smoothing: grayscale;
        --vh: 100%;
    }

    /* placeholder 스타일링 */
    input::placeholder {
        color: grey;
        font-family: 'Pretendard', sans-serif;
    }

    textarea::placeholder {
        color: grey;
        font-family: 'Pretendard', sans-serif;
    }

    /* 전체 문서에 대한 스크롤바 스타일 */
    ::-webkit-scrollbar {
        width: 5px;
    }

    /* 스크롤바 트랙 (바탕) 스타일 */
    ::-webkit-scrollbar-track {
        background: transparent;
        border-radius: 10px;
    }

    /* 스크롤바 핸들 (가로지르는 부분) 스타일 */
    ::-webkit-scrollbar-thumb {
        background: #ccc;
        border-radius: 10px;
    }

    /* 스크롤바 핸들을 호버 시 스타일 */
    ::-webkit-scrollbar-thumb:hover {
        background: #999;
    }



`

export default GlobalStyles
