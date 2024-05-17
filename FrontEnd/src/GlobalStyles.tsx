import { createGlobalStyle } from 'styled-components'
import reset from 'styled-reset'

const GlobalStyles = createGlobalStyle`
    ${reset}
    * {
        margin: 0;
        padding: 0;
        box-sizing: border-box;
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
        padding-top: 68px;
        width: calc(100vw - 5px);
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
    
    // swal2 node modules 들어가서 찾아온 icon 경로
    div:where(.swal2-icon).swal2-warning.swal2-icon-show {
        animation: swal2-animate-error-icon 0.5s;
        width: 100%;
        top: 15px;
    }

    // swal2 기본 버튼 색상 변경 로직
    div:where(.swal2-container) button:where(.swal2-styled).swal2-confirm {
        border: 0;
        border-radius: 0.25em;
        background: initial;
        background-color: #233eff;
        color: #fff;
        font-size: 1em;
    }
    div:where(.swal2-container) button:where(.swal2-styled).swal2-confirm:focus {
        box-shadow: 0 0 0 3px rgba(35, 62, 225, 0.5);
    }

`

export default GlobalStyles
